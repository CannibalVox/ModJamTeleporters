package net.modyssey.teleporters.tileentities;

import net.minecraft.entity.player.EntityPlayer;
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

    }
}
