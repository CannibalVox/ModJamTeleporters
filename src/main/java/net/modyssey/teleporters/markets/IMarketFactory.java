package net.modyssey.teleporters.markets;

import net.modyssey.teleporters.markets.stock.StockList;

public interface IMarketFactory {
    Market createMarket();
    void setStockData(StockList stock);
    StockList getStockList();
}
