package net.modyssey.teleporters.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class TileEntityTeleporterController extends TileEntity {

    public boolean canInteractWith(EntityPlayer player) {
        if(worldObj.getTileEntity(xCoord, yCoord, zCoord) != this)
        {
            return false;
        }
        return player.getDistanceSq((double)xCoord + 0.5D, (double)yCoord + 0.5D, (double)zCoord + 0.5D) <= 64D;
    }
}
