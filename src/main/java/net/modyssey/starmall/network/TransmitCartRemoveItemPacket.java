package net.modyssey.starmall.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.modyssey.starmall.tileentities.container.ContainerTeleporterController;

public class TransmitCartRemoveItemPacket extends ModysseyPacket {

    private int windowId;
    private int marketIndex;
    private int cartIndex;

    public TransmitCartRemoveItemPacket() {}
    public TransmitCartRemoveItemPacket(int windowId, int marketIndex, int cartIndex) {
        this.windowId = windowId;
        this.marketIndex = marketIndex;
        this.cartIndex = cartIndex;
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(windowId);
        out.writeInt(marketIndex);
        out.writeInt(cartIndex);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        windowId = in.readInt();
        marketIndex = in.readInt();
        cartIndex = in.readInt();
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        if (player.openContainer != null && player.openContainer.windowId == windowId && player.openContainer instanceof ContainerTeleporterController)
            ((ContainerTeleporterController)player.openContainer).receiveCartRemove(marketIndex, cartIndex);
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //Shouldn't be here
    }
}
