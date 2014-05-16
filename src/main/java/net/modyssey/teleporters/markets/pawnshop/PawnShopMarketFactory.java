package net.modyssey.teleporters.markets.pawnshop;

import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockList;

public class PawnShopMarketFactory implements IMarketFactory {

    private StockList stockList;

    public PawnShopMarketFactory(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public IMarket createMarket() {
        return new PawnShopMarket(stockList);
    }
}
