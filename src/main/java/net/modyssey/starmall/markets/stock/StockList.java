package net.modyssey.starmall.markets.stock;

import net.minecraft.item.ItemStack;
import net.modyssey.starmall.tileentities.io.PadData;

import java.util.ArrayList;

public class StockList {
    private ArrayList<StockCategory> categories = new ArrayList<StockCategory>();

    public StockList() {}

    public int getCategoryCount() { return categories.size(); }

    public void addCategory(StockCategory category) {
        categories.add(category);
    }

    public StockCategory getCategory(int i) {
        return categories.get(i);
    }

    public void clear() { categories.clear(); }

    public int getItemValue(ItemStack stack) {
        for (int i = 0; i < categories.size(); i++) {
            StockCategory category = categories.get(i);
            for (int j = 0; j < category.getItemCount(); j++) {
                StockItem item = category.get(j);

                if (PadData.canItemstacksStack(item.getItem(), stack)) {
                    return item.getValue();
                }
            }
        }

        return 0;
    }
}
