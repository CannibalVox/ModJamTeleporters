package net.modyssey.starmall.markets.stock;

import net.minecraft.item.ItemStack;

public class StockItem {
    private ItemStack item;
    private int value;

    public StockItem(ItemStack item, int value) {
        this.item = item;
        this.value = value;
    }

    public ItemStack getItem() { return item; }
    public int getValue() { return value; }
}
