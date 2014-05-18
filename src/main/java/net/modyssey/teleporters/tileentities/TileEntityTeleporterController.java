package net.modyssey.teleporters.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.modyssey.teleporters.tileentities.io.PadData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

public class TileEntityTeleporterController extends TileEntity {
    public TileEntityTeleporterController() {
        this.isActive = true;
    }

    public TileEntityTeleporterController(boolean active) {
        this.isActive = active;
    }

    private ArrayList<PadData> padLocations = new ArrayList<PadData>();
    private UUID locationUUID = UUID.randomUUID();
    private int credits = 0;
    private boolean isActive;

    public boolean canInteractWith(EntityPlayer player) {
        if(worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    public int getPadCount() { return padLocations.size(); }
    public boolean isActive() { return isActive; }

    public LinkedList<ItemStack> getPadContents() {
        LinkedList<ItemStack> contents = new LinkedList<ItemStack>();

        for(PadData pad : padLocations) {
            for(ItemStack contentItem : pad.getPadItems(getWorldObj())) {
                addItemCompact(contents, contentItem);
            }
        }

        return contents;
    }

    private void addItemCompact(LinkedList<ItemStack> items, ItemStack item) {
        for (ItemStack candidate : items) {
            if (PadData.canItemstacksStack(candidate, item)) {
                candidate.stackSize += item.stackSize;
                return;
            }
        }

        items.add(item);
    }

    public void registerPad(int x, int y, int z) {

        if (getWorldObj().isRemote)
            return;

        deregisterPad(x, y, z);

        PadData location = new PadData(x, y, z);
        padLocations.add(location);
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void deregisterAllPads() {

        if (worldObj.isRemote)
            return;

        while (padLocations.size() > 0) {
            PadData padLocation = padLocations.get(0);

            TileEntity pad = getWorldObj().getTileEntity(padLocation.getPadXCoord(), padLocation.getPadYCoord(), padLocation.getPadZCoord());

            if (pad != null && pad instanceof  TileEntityTeleporterPad) {
                ((TileEntityTeleporterPad)pad).deregister();
                getWorldObj().setBlockMetadataWithNotify(padLocation.getPadXCoord(), padLocation.getPadYCoord(), padLocation.getPadZCoord(), 0, 3);
            }
        }

        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void deregisterPad(int x, int y, int z) {

        if (worldObj.isRemote)
            return;

        for (int i = padLocations.size() - 1; i >= 0; i--) {
            PadData loc = padLocations.get(i);

            if (loc.getPadXCoord() == x && loc.getPadYCoord() == y && loc.getPadZCoord() == z) {
                padLocations.remove(i);
            }
        }

        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void adjustCreditAmount(int amount) {
        if (worldObj.isRemote)
            return;

        credits += amount;
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public ItemStack placeInPadContents(ItemStack stack) {
        if (worldObj.isRemote)
            return stack;

        for (int i = 0; i < padLocations.size(); i++) {
            PadData pad = padLocations.get(i);
            stack = pad.attemptAddItems(getWorldObj(), stack);

            if (stack == null)
                return null;
        }

        return stack;
    }

    public ItemStack dropOnPad(ItemStack stack, boolean forceDropOnOccupiedSpaces) {
        if (worldObj.isRemote)
            return stack;

        for (int i = 0; i < padLocations.size(); i++) {
            PadData pad = padLocations.get(i);
            stack = pad.dropItemsOnFloor(getWorldObj(), stack, forceDropOnOccupiedSpaces);

            if (stack == null)
                return null;
        }

        return stack;
    }

    public ItemStack sellPadContents(ItemStack stack) {
        if (worldObj.isRemote)
            return stack;

        for (int i = 0; i < padLocations.size(); i++) {
            PadData pad = padLocations.get(i);
            stack = pad.attemptRemoveItems(getWorldObj(), stack, true);

            if (stack == null)
                return null;
        }

        return stack;
    }

    public ItemStack sellPadBlocks(ItemStack stack) {
        if (worldObj.isRemote)
            return stack;

        for (int i = 0; i < padLocations.size(); i++) {
            PadData pad = padLocations.get(i);
            stack = pad.attemptRemoveBlock(getWorldObj(), stack, true);

            if (stack == null)
                return null;
        }

        return stack;
    }

    public int getCredits() { return credits; }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound tags = new NBTTagCompound();
        writeToNBT(tags);

        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tags);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }


    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        NBTTagList locationTags = new NBTTagList();

        for (int i = 0; i < padLocations.size(); i++) {
            NBTTagCompound location = new NBTTagCompound();
            PadData locObj = padLocations.get(i);
            location.setInteger("locX", locObj.getPadXCoord());
            location.setInteger("locY", locObj.getPadYCoord());
            location.setInteger("locZ", locObj.getPadZCoord());
            locationTags.appendTag(location);
        }
        nbtTagCompound.setTag("locations", locationTags);
        nbtTagCompound.setBoolean("isActive", isActive);
        nbtTagCompound.setInteger("credits", credits);
        nbtTagCompound.setString("uuid", locationUUID.toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        isActive = nbtTagCompound.getBoolean("isActive");
        credits = nbtTagCompound.getInteger("credits");
        locationUUID = UUID.fromString(nbtTagCompound.getString("uuid"));
        NBTTagList locations = nbtTagCompound.getTagList("locations", 10);

        padLocations.clear();

        for(int i = 0; i < locations.tagCount(); i++) {
            NBTTagCompound locTag = locations.getCompoundTagAt(i);

            int x = locTag.getInteger("locX");
            int y = locTag.getInteger("locY");
            int z = locTag.getInteger("locZ");

            PadData location = new PadData(x, y, z);

            padLocations.add(location);
        }
    }
}
