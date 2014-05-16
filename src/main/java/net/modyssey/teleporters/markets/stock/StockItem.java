package net.modyssey.teleporters.markets.stock;

import net.minecraft.item.Item;

public class StockItem {
    private Item item;
    private int value;

    public StockItem(Item item, int value) {
        this.item = item;
        this.value = value;
    }

    public Item getItem() { return item; }
    public int getValue() { return value; }
}
