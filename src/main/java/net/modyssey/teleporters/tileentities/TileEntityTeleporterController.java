package net.modyssey.teleporters.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.ArrayList;

public class TileEntityTeleporterController extends TileEntity {
    private class PadLocation {
        public int x;
        public int y;
        public int z;
    }

    public TileEntityTeleporterController() {
        this.isActive = true;
    }

    public TileEntityTeleporterController(boolean active) {
        this.isActive = active;
    }

    private ArrayList<PadLocation> padLocations = new ArrayList<PadLocation>();
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

    public void registerPad(int x, int y, int z) {

        if (getWorldObj().isRemote)
            return;

        for (int i = 0; i < padLocations.size(); i++) {
            PadLocation loc = padLocations.get(i);

            if (loc.x == x && loc.y == y && loc.z == z)
                return;
        }

        PadLocation location = new PadLocation();
        location.x = x;
        location.y = y;
        location.z = z;
        padLocations.add(location);
        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void deregisterAllPads() {

        if (worldObj.isRemote)
            return;

        while (padLocations.size() > 0) {
            PadLocation padLocation = padLocations.get(0);

            TileEntity pad = getWorldObj().getTileEntity(padLocation.x, padLocation.y, padLocation.z);

            if (pad != null && pad instanceof  TileEntityTeleporterPad) {
                ((TileEntityTeleporterPad)pad).deregister();
                getWorldObj().setBlockMetadataWithNotify(padLocation.x, padLocation.y, padLocation.z, 0, 3);
            }
        }

        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public void deregisterPad(int x, int y, int z) {

        if (worldObj.isRemote)
            return;

        for (int i = padLocations.size() - 1; i >= 0; i--) {
            PadLocation loc = padLocations.get(i);

            if (loc.x == x && loc.y == y && loc.z == z) {
                padLocations.remove(i);
            }
        }

        getWorldObj().markBlockForUpdate(xCoord, yCoord, zCoord);
    }

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
            PadLocation locObj = padLocations.get(i);
            location.setInteger("locX", locObj.x);
            location.setInteger("locY", locObj.y);
            location.setInteger("locZ", locObj.z);
            locationTags.appendTag(location);
        }
        nbtTagCompound.setTag("locations", locationTags);
        nbtTagCompound.setBoolean("isActive", isActive);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        isActive = nbtTagCompound.getBoolean("isActive");
        NBTTagList locations = nbtTagCompound.getTagList("locations", 10);

        padLocations.clear();

        for(int i = 0; i < locations.tagCount(); i++) {
            NBTTagCompound locTag = locations.getCompoundTagAt(i);

            PadLocation location = new PadLocation();
            location.x = locTag.getInteger("locX");
            location.y = locTag.getInteger("locY");
            location.z = locTag.getInteger("locZ");
            padLocations.add(location);
        }
    }
}
