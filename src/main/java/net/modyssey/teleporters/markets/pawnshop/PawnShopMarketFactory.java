package net.modyssey.teleporters.markets.pawnshop;

import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockList;

import java.util.List;

public class PawnShopMarketFactory implements IMarketFactory {

    private StockList stockList;

    public PawnShopMarketFactory(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public IMarket createMarket() {
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
