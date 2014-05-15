package net.modyssey.teleporters.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityTeleporterPad extends TileEntity {
    private int registeredStationX = 0;
    private int registeredStationY = 0;
    private int registeredStationZ = 0;
    private boolean isRegistered = false;

    public int getRegisteredStationX() { return registeredStationX; }
    public int getRegisteredStationY() { return registeredStationY; }
    public int getRegisteredStationZ() { return registeredStationZ; }
    public boolean isRegistered() { return isRegistered; }

    public boolean registerStation(int x, int y, int z) {
        World world = getWorldObj();
        TileEntity station = world.getTileEntity(x, y, z);

        if (station == null || !(station instanceof  TileEntityTeleporterController))
            return false;

        ((TileEntityTeleporterController)station).registerPad(this.xCoord, this.yCoord, this.zCoord);
        registeredStationX = x;
        registeredStationY = y;
        registeredStationZ = z;
        isRegistered = true;
        return true;
    }

    public void deregister() {
        if (!isRegistered)
            return;

        World world = getWorldObj();

        TileEntity station = world.getTileEntity(registeredStationX, registeredStationY, registeredStationZ);

        if (station != null && station instanceof TileEntityTeleporterController)
            ((TileEntityTeleporterController)station).deregisterPad(xCoord, yCoord, zCoord);

        registeredStationX = 0;
        registeredStationY = 0;
        registeredStationZ = 0;
        isRegistered = false;
    }

    public boolean registerSameAs(int x, int y, int z) {
        World world = getWorldObj();
        TileEntity pad = world.getTileEntity(x, y, z);

        if (pad == null || !(pad instanceof TileEntityTeleporterPad))
            return false;

        TileEntityTeleporterPad padObj = (TileEntityTeleporterPad)pad;

        if (!padObj.isRegistered())
            return false;

        return registerStation(padObj.getRegisteredStationX(), padObj.getRegisteredStationY(), padObj.getRegisteredStationZ());
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setInteger("stationX", registeredStationX);
        nbtTagCompound.setInteger("stationY", registeredStationY);
        nbtTagCompound.setInteger("stationZ", registeredStationZ);
        nbtTagCompound.setBoolean("isRegistered", isRegistered);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        registeredStationX = nbtTagCompound.getInteger("stationX");
        registeredStationY = nbtTagCompound.getInteger("stationY");
        registeredStationZ = nbtTagCompound.getInteger("stationZ");
        isRegistered = nbtTagCompound.getBoolean("isRegistered");
    }
}
