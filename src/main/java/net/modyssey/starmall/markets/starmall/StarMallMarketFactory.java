package net.modyssey.starmall.markets.starmall;

import net.modyssey.starmall.markets.Market;
import net.modyssey.starmall.markets.IMarketFactory;
import net.modyssey.starmall.markets.stock.StockList;

public class StarMallMarketFactory implements IMarketFactory {
    private StockList stockList;

    public StarMallMarketFactory(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public Market createMarket() {
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
