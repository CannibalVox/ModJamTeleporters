package net.modyssey.teleporters.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modyssey.teleporters.markets.Market;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;

import java.util.ArrayList;
import java.util.List;

public class TransmitFullCartPacket extends ModysseyPacket {

    private int windowId;
    private List<List<ItemStack>> carts = new ArrayList<List<ItemStack>>();

    public TransmitFullCartPacket() {

    }

    public TransmitFullCartPacket(int windowId) {
        this.windowId = windowId;
    }

    public void addCart(Market singleCart) {
        List<ItemStack> cart = new ArrayList<ItemStack>();

        for (int i = 0; i < singleCart.getCartSize(); i++) {
            cart.add(singleCart.getCartContent(i));
        }

        carts.add(cart);
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(windowId);
        out.writeInt(carts.size());

        for (int i = 0; i < carts.size(); i++) {
            List<ItemStack> cart = carts.get(i);

            out.writeInt(cart.size());

            for (int j = 0; j < cart.size(); j++) {
                ItemStack item = cart.get(j);

                int itemId = Item.getIdFromItem(item.getItem());
                out.writeInt(itemId);
                out.writeInt(item.stackSize);
                out.writeInt(item.getItemDamage());
            }
        }
    }

    @Override
    public void read(ByteArrayDataInput in) {
        windowId = in.readInt();
        int marketCount = in.readInt();

        for (int i = 0; i < marketCount; i++) {
            int itemCount = in.readInt();

            List<ItemStack> cart = new ArrayList<ItemStack>();
            carts.add(cart);

            for (int j = 0; j < itemCount; j++) {
                int itemId = in.readInt();
                int stackSize = in.readInt();
                int damage = in.readInt();

                Item item = (Item)Item.itemRegistry.getObjectById(itemId);
                cart.add(new ItemStack(item, stackSize, damage));
            }
        }
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        if (player.openContainer != null && player.openContainer.windowId == windowId && player.openContainer instanceof ContainerTeleporterController)
            ((ContainerTeleporterController)player.openContainer).receiveFullCart(carts);
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //To client only
    }
}
