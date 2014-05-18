package net.modyssey.starmall.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.modyssey.starmall.tileentities.container.ContainerTeleporterController;

public class RequestMarketExchangePacket extends ModysseyPacket {
    private int windowId;
    private int marketId;

    public RequestMarketExchangePacket(int windowId, int marketId) {
        this.windowId = windowId;
        this.marketId = marketId;
    }

    public RequestMarketExchangePacket() {

    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(windowId);
        out.writeInt(marketId);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        windowId = in.readInt();
        marketId = in.readInt();
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        //Shouldn't be here
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        if (player.currentWindowId == windowId && player.openContainer != null && player.openContainer instanceof ContainerTeleporterController) {
            ((ContainerTeleporterController) player.openContainer).requestExchange(player, marketId);
        }
    }
}
