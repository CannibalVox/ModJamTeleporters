package net.modyssey.teleporters.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;

public class TransmitCartUpdatePacket extends ModysseyPacket {

    private int windowId;
    private int marketId;
    private int cartIndex;
    private ItemStack itemToAddOrUpdate;
    private boolean addIfUnlocated;

    public TransmitCartUpdatePacket(int windowId, int marketId, int cartIndex, ItemStack itemToAddOrUpdate, boolean addIfUnlocated) {
        this.windowId = windowId;
        this.marketId = marketId;
        this.cartIndex = cartIndex;
        this.itemToAddOrUpdate = itemToAddOrUpdate;
        this.addIfUnlocated = addIfUnlocated;
    }

    public TransmitCartUpdatePacket() {}

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(windowId);
        out.writeInt(marketId);
        out.writeInt(cartIndex);

        int itemId = Item.getIdFromItem(itemToAddOrUpdate.getItem());
        out.writeInt(itemId);
        out.writeInt(itemToAddOrUpdate.stackSize);
        out.writeInt(itemToAddOrUpdate.getItemDamage());

        out.writeBoolean(addIfUnlocated);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        windowId = in.readInt();
        marketId = in.readInt();
        cartIndex = in.readInt();

        int itemId = in.readInt();
        int stackSize = in.readInt();
        int damage = in.readInt();

        Item item = (Item)Item.itemRegistry.getObjectById(itemId);
        itemToAddOrUpdate = new ItemStack(item, stackSize, damage);

        addIfUnlocated = in.readBoolean();
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        if (player.openContainer != null && player.openContainer.windowId == windowId && player.openContainer instanceof ContainerTeleporterController)
            ((ContainerTeleporterController)player.openContainer).receiveCartUpdate(marketId, cartIndex, itemToAddOrUpdate, addIfUnlocated);
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //Shouldn't be here
    }
}
