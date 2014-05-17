package net.modyssey.teleporters.markets;

import net.modyssey.teleporters.markets.stock.StockCategory;

import java.util.List;

public interface IMarketFactory {
    IMarket createMarket();
    void setStockData(List<StockCategory> stock);
}
