package net.modyssey.starmall.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modyssey.starmall.ModysseyStarMall;
import net.modyssey.starmall.markets.stock.StockCategory;
import net.modyssey.starmall.markets.stock.StockItem;
import net.modyssey.starmall.markets.stock.StockList;
import net.modyssey.starmall.tileentities.container.ContainerTeleporterController;

import java.util.ArrayList;
import java.util.List;

public class FullMarketDataPacket extends ModysseyPacket {

    List<StockList> allMarkets = new ArrayList<StockList>();

    public FullMarketDataPacket() {

    }

    public void addMarket(StockList stockList) {
        allMarkets.add(stockList);
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(allMarkets.size());

        for (int i = 0; i < allMarkets.size(); i++) {
            writeMarket(allMarkets.get(i), out);
        }
    }

    private void writeMarket(StockList market, ByteArrayDataOutput out) {
        out.writeInt(market.getCategoryCount());

        for (int i = 0; i < market.getCategoryCount(); i++) {
            StockCategory category = market.getCategory(i);

            out.writeUTF(category.getCategoryName());
            out.writeInt(Item.itemRegistry.getIDForObject(category.getIconItem().getItem()));
            out.writeInt(category.getIconItem().getItemDamage());
            out.writeInt(category.getItemCount());

            for (int j = 0; j < category.getItemCount(); j ++) {
                StockItem item = category.get(j);

                out.writeInt(Item.itemRegistry.getIDForObject(item.getItem().getItem()));
                out.writeInt(item.getItem().getItemDamage());
                out.writeInt((item.getValue()));
            }
        }
    }

    @Override
    public void read(ByteArrayDataInput in) {
        int marketCount = in.readInt();

        allMarkets = new ArrayList<StockList>();
        for (int i = 0; i < marketCount; i++) {
            int categoryCount = in.readInt();

            StockList market = new StockList();
            allMarkets.add(market);

            for (int j = 0; j < categoryCount; j++) {
                String catName = in.readUTF();
                int iconId = in.readInt();
                int iconDamage = in.readInt();

                StockCategory category = new StockCategory(catName, new ItemStack((Item)Item.itemRegistry.getObjectById(iconId), 1, iconDamage));
                market.addCategory(category);

                int itemCount = in.readInt();

                for (int k = 0; k < itemCount; k++) {
                    int itemId = in.readInt();
                    int itemDamage = in.readInt();
                    int value = in.readInt();

                    StockItem item = new StockItem(new ItemStack((Item)Item.itemRegistry.getObjectById(itemId), 1, itemDamage), value);
                    category.addItem(item);
                }
            }
        }
    }

    @Override
    public void handleClient(World world, EntityPlayer player) {
        ModysseyStarMall.instance.setMarketData(allMarkets);

        if (player.openContainer != null && player.openContainer instanceof ContainerTeleporterController)
            ((ContainerTeleporterController)player.openContainer).updateMarketData(allMarkets);
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //Shouldn't be getting this
    }
}
