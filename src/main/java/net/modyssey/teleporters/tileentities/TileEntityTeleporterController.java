package net.modyssey.teleporters.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
        PadLocation location = new PadLocation();
        location.x = x;
        location.y = y;
        location.z = z;
        padLocations.add(location);
    }

    public void deregisterAllPads() {
        while (padLocations.size() > 0) {
            PadLocation padLocation = padLocations.get(0);

            TileEntity pad = getWorldObj().getTileEntity(padLocation.x, padLocation.y, padLocation.z);

            if (pad != null && pad instanceof  TileEntityTeleporterPad) {
                ((TileEntityTeleporterPad)pad).deregister();
                getWorldObj().setBlockMetadataWithNotify(padLocation.x, padLocation.y, padLocation.z, 0, 3);
            }
        }
    }

    public void deregisterPad(int x, int y, int z) {
        for (int i = padLocations.size() - 1; i >= 0; i--) {
            PadLocation loc = padLocations.get(i);

            if (loc.x == x && loc.y == y && loc.z == z) {
                padLocations.remove(i);
            }
        }
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
