package net.modyssey.teleporters.parser.io;

import java.util.List;

public class MarketData {

    public MarketData() {}

    private List<StockData> buy;
    private List<StockData> sell;

    public List<StockData> getBuy() { return buy; }
    public List<StockData> getSell() { return sell; }
}
