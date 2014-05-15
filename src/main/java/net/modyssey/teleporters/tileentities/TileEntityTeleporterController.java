package net.modyssey.teleporters.tileentities;

import net.minecraft.entity.player.EntityPlayer;
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

    private ArrayList<PadLocation> padLocations = new ArrayList<PadLocation>();

    public boolean canInteractWith(EntityPlayer player) {
        if(worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }

    public void registerPad(int x, int y, int z) {
        PadLocation location = new PadLocation();
        location.x = x;
        location.y = y;
        location.z = z;
        padLocations.add(location);
    }

    public void deregisterPad(int x, int y, int z) {
        for (int i = padLocations.size() - 1; i >= 0; i--) {
            PadLocation loc = padLocations.get(i);

            if (loc.x == x && loc.y == y && loc.z == z) {
                padLocations.remove(i);
            }
        }
    }

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
    }
}
