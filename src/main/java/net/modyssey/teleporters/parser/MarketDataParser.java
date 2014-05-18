package net.modyssey.teleporters.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockItem;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.parser.io.MarketData;
import net.modyssey.teleporters.parser.io.StockData;
import net.modyssey.teleporters.parser.io.StockDataItem;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class MarketDataParser {
    private Gson gson;

    public List<StockList> parseMarketData(InputStream stream) {
        if (stream == null) {
            throw new RuntimeException("Mod Data json file could not be found.");
        }

        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        gson = builder.create();

        MarketData data = null;
        try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(stream, writer);
            String dataText = writer.toString();
            data = gson.fromJson(dataText, MarketData.class);

            if (data == null) {
                throw new RuntimeException("Mod Data json file parsed to a null value.");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error loading Mod Data json file.", ex);
        } catch (JsonSyntaxException ex) {
            throw new RuntimeException("Error parsing the Mod Data json file.", ex);
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {}
        }

        return buildStockData(data);
    }

    private List<StockList> buildStockData(MarketData data) {
        List<StockList> markets = new ArrayList<StockList>();
        markets.add(buildStockList(data.getBuy()));
        markets.add(buildStockList(data.getSell()));
        return markets;
    }

    private StockList buildStockList(List<StockData> data) {
        StockList list = new StockList();

        for (int i = 0; i < data.size(); i++) {
            StockCategory category = buildCategory(data.get(i));
            list.addCategory(category);
        }

        return list;
    }

    private StockCategory buildCategory(StockData data) {
        String name = data.getName();
        String iconName = data.getIcon();
        ItemStack iconItem = getStackFromName(iconName);

        StockCategory category = new StockCategory(name, iconItem);

        for (int i = 0; i < data.getItemCount(); i++) {
            StockDataItem item = data.getItem(i);

            StockItem stockItem = buildStockItem(item);

            if (stockItem != null)
                category.addItem(stockItem);
        }

        return category;
    }

    private StockItem buildStockItem(StockDataItem item) {
        String itemName = item.getItem();
        int value = item.getValue();
        ItemStack itemStack = getStackFromName(itemName);

        if (itemStack == null)
            return null;

        return new StockItem(itemStack, value);
    }

    private ItemStack getStackFromName(String name) {
        int damage = 0;
        int damageIndex = name.lastIndexOf(':');

        if (damageIndex >= 0) {
            String textAfterColon = name.substring(damageIndex + 1);

            try {
                damage = Integer.parseInt(textAfterColon);
                name = name.substring(0, damageIndex -1);
            } catch (NumberFormatException ex) {
                //This just means that the colon was there to separate modID from item name, not to separate
                //item name from damage value.  NBD, just ignore it
            }
        }

        ItemStack itemStack = getStackFromItemName(name, damage);

        if (itemStack == null)
            itemStack = getStackFromBlockName(name, damage);

        return itemStack;
    }

    private ItemStack getStackFromItemName(String name, int damage) {
        Item item = (Item)Item.itemRegistry.getObject(name);

        if (item == null)
            return null;

        return new ItemStack(item, 1, damage);
    }

    private ItemStack getStackFromBlockName(String name, int damage) {
        Block block = (Block)Block.blockRegistry.getObject(name);

        if (block == null)
            return null;

        Item item = Item.getItemFromBlock(block);

        if (item == null)
            return null;

        return new ItemStack(item, 1, damage);
    }
}
