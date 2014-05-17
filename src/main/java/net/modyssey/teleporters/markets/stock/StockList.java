package net.modyssey.teleporters.markets.stock;

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
}
