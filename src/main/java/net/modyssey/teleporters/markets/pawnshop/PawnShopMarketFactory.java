package net.modyssey.teleporters.markets.pawnshop;

import net.modyssey.teleporters.markets.Market;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockList;

public class PawnShopMarketFactory implements IMarketFactory {

    private StockList stockList;

    public PawnShopMarketFactory(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public Market createMarket() {
        return new PawnShopMarket(stockList);
    }

    @Override
    public void setStockData(StockList stock) {
        stockList.clear();

        for (int i = 0; i < stock.getCategoryCount(); i++) {
            stockList.addCategory(stock.getCategory(i));
        }
    }

    public StockList getStockList() { return stockList; }
}
