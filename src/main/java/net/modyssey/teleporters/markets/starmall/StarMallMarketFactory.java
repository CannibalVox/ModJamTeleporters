package net.modyssey.teleporters.markets.starmall;

import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockList;

import java.util.List;

public class StarMallMarketFactory implements IMarketFactory {
    private StockList stockList;

    public StarMallMarketFactory(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public IMarket createMarket() {
        return new StarMallMarket(stockList);
    }

    @Override
    public void setStockData(StockList stock) {
        stockList.clear();

        for (int i = 0; i < stock.getCategoryCount(); i++) {
            stockList.addCategory(stock.getCategory(i));
        }
    }

    @Override
    public StockList getStockList() {
        return stockList;
    }
}
