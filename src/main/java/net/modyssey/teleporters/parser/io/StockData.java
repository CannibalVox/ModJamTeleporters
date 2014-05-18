package net.modyssey.teleporters.parser.io;

import java.util.List;

public class StockData {

    public StockData() {}

    private String name;
    private String icon;
    private List<StockDataItem> items;

    public String getName() { return name; }
    public String getIcon() { return icon; }
    public int getItemCount() { return items.size(); }
    public StockDataItem getItem(int index) { return items.get(index); }
}
