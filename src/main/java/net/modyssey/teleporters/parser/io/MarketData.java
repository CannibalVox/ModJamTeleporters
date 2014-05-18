package net.modyssey.teleporters.parser.io;

public class MarketData {

    public MarketData() {}

    private StockData buy;
    private StockData sell;

    public StockData getBuy() { return buy; }
    public StockData getSell() { return sell; }
}
