package net.modyssey.teleporters.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;

public class RequestCartRemovePacket extends ModysseyPacket {

    private int windowId;
    private int marketIndex;
    private int cartIndex;
    private ItemStack itemToRemove;

    public RequestCartRemovePacket() {}

    public RequestCartRemovePacket(int windowId, int marketIndex, int cartIndex, ItemStack itemToRemove) {
        this.windowId = windowId;
        this.marketIndex = marketIndex;
        this.cartIndex = cartIndex;
        this.itemToRemove = itemToRemove;
    }


    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(windowId);
        out.writeInt(marketIndex);
        out.writeInt(cartIndex);

        int itemId = Item.getIdFromItem(itemToRemove.getItem());
        out.writeInt(itemId);
        out.writeInt(itemToRemove.stackSize);
        out.writeInt(itemToRemove.getItemDamage());
    }

    @Override
    public void read(ByteArrayDataInput in) {
        windowId = in.readInt();
        marketIndex = in.readInt();
        cartIndex = in.readInt();

        int itemId = in.readInt();
        int stackSize = in.readInt();
        int damage = in.readInt();

        Item item = (Item)Item.itemRegistry.getObjectById(itemId);
        itemToRemove = new ItemStack(item, stackSize, damage);
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        //Shouldn't be here
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        if (player.currentWindowId == windowId && player.openContainer != null && player.openContainer instanceof ContainerTeleporterController) {
            ((ContainerTeleporterController) player.openContainer).requestRemoveFromCart(player, marketIndex, cartIndex, itemToRemove);
        }
    }
}
