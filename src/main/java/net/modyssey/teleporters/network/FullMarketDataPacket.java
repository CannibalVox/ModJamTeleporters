package net.modyssey.teleporters.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockItem;

import java.util.ArrayList;
import java.util.List;

public class FullMarketDataPacket extends ModysseyPacket {

    List<List<StockCategory>> allMarkets = new ArrayList<List<StockCategory>>();

    @Override
    public void write(ByteArrayDataOutput out) {
        out.write(allMarkets.size());

        for (int i = 0; i < allMarkets.size(); i++) {
            writeMarket(allMarkets.get(i), out);
        }
    }

    private void writeMarket(List<StockCategory> market, ByteArrayDataOutput out) {
        out.write(market.size());

        for (int i = 0; i < market.size(); i++) {
            StockCategory category = market.get(i);

            out.writeUTF(category.getCategoryName());
            out.write(Item.itemRegistry.getIDForObject(category.getIconItem()));
            out.write(category.getItemCount());

            for (int j = 0; j < category.getItemCount(); j ++) {
                StockItem item = category.get(j);

                item.
            }
        }
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
