package net.modyssey.teleporters.markets.starmall;

import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockList;

public class StarMallMarketFactory implements IMarketFactory {
    private StockList stockList;

    public StarMallMarketFactory(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public IMarket createMarket() {
        return new StarMallMarket(stockList);
    }
}
