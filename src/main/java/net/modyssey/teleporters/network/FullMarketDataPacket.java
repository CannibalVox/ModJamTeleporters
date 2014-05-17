package net.modyssey.teleporters.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.modyssey.teleporters.ModysseyTeleporters;

public class FullMarketDataPacket extends ModysseyPacket {



    @Override
    public void write(ByteArrayDataOutput out) {

    }

    @Override
    public void read(ByteArrayDataInput in) {

    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        ModysseyTeleporters.instance.setMarketData();
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //Shouldn't be getting this
    }
}
