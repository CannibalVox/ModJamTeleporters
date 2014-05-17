package net.modyssey.teleporters.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockItem;
import net.modyssey.teleporters.markets.stock.StockList;

import java.util.ArrayList;
import java.util.List;

public class FullMarketDataPacket extends ModysseyPacket {

    List<StockList> allMarkets = new ArrayList<StockList>();

    public void addMarket(StockList stockList) {
        allMarkets.add(stockList);
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.write(allMarkets.size());

        for (int i = 0; i < allMarkets.size(); i++) {
            writeMarket(allMarkets.get(i), out);
        }
    }

    private void writeMarket(StockList market, ByteArrayDataOutput out) {
        out.write(market.getCategoryCount());

        for (int i = 0; i < market.getCategoryCount(); i++) {
            StockCategory category = market.getCategory(i);

            out.writeUTF(category.getCategoryName());
            out.write(Item.itemRegistry.getIDForObject(category.getIconItem().getItem()));
            out.write(category.getIconItem().getItemDamage());
            out.write(category.getItemCount());

            for (int j = 0; j < category.getItemCount(); j ++) {
                StockItem item = category.get(j);

                out.write(Item.itemRegistry.getIDForObject(item.getItem()));
                out.write(item.getItem().getItemDamage());
                out.write((item.getValue()));
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
        ModysseyTeleporters.instance.setMarketData(allMarkets);
    }

    @Override
    public void handleServer(World world, EntityPlayerMP player) {
        //Shouldn't be getting this
    }
}
