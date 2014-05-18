package net.modyssey.starmall.markets.pawnshop;

import net.modyssey.starmall.markets.Market;
import net.modyssey.starmall.markets.IMarketFactory;
import net.modyssey.starmall.markets.stock.StockList;

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
