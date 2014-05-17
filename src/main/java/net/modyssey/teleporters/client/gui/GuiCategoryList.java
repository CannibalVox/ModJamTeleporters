package net.modyssey.teleporters.client.gui;

import net.modyssey.teleporters.markets.stock.StockList;

public class GuiCategoryList {
    private StockList stockList;
    private GuiTeleporterController parent;

    public GuiCategoryList(GuiTeleporterController parent, int parentX, int parentY) {


        this.parent = parent;
    }

    public void setStockList(StockList stockList) {
        this.stockList = stockList;
    }

}
