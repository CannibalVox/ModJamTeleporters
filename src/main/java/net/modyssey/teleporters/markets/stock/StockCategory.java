package net.modyssey.teleporters.markets.stock;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class StockCategory {
    private String unlocalizedCategoryName;
    private ItemStack iconItem;

    private ArrayList<StockItem> categoryItems = new ArrayList<StockItem>();

    public StockCategory(String categoryName, ItemStack iconItem) {
        this.unlocalizedCategoryName = categoryName;
        this.iconItem = iconItem;
    }

    public String getCategoryName() {
        return unlocalizedCategoryName;
    }

    public ItemStack getIconItem() {
        return iconItem;
    }

    public void addItem(StockItem item) {
        categoryItems.add(item);
    }

    public int getItemCount() {
        return categoryItems.size();
    }

    public StockItem get(int i) {
        return categoryItems.get(i);
    }
}
