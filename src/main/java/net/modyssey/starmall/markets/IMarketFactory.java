package net.modyssey.starmall.markets;

import net.modyssey.starmall.markets.stock.StockList;

public interface IMarketFactory {
    Market createMarket();
    void setStockData(StockList stock);
    StockList getStockList();
}
