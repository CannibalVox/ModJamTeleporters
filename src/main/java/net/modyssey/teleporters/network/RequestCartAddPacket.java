package net.modyssey.teleporters.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;

public class RequestCartAddPacket extends ModysseyPacket {

    private int windowId;
    private int marketId;
    private ItemStack itemToAdd;

    public RequestCartAddPacket(int windowId, int marketId, ItemStack itemToAdd) {
        this.windowId = windowId;
        this.marketId = marketId;
        this.itemToAdd = itemToAdd;
    }

    public RequestCartAddPacket() {}

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(windowId);
        out.writeInt(marketId);

        int itemId = Item.getIdFromItem(itemToAdd.getItem());
        out.writeInt(itemId);
        out.writeInt(itemToAdd.stackSize);
        out.writeInt(itemToAdd.getItemDamage());
    }

    @Override
    public void read(ByteArrayDataInput in) {
        windowId = in.readInt();
        marketId = in.readInt();

        int itemId = in.readInt();
        int stackSize = in.readInt();
        int damage = in.readInt();

        Item item = (Item)Item.itemRegistry.getObjectById(itemId);
        itemToAdd = new ItemStack(item, stackSize, damage);
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        //Shouldn't be here
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        if (player.currentWindowId == windowId && player.openContainer != null && player.openContainer instanceof ContainerTeleporterController) {
            ((ContainerTeleporterController)player.openContainer).requestAddToCart(marketId, itemToAdd);
        }
    }
}
