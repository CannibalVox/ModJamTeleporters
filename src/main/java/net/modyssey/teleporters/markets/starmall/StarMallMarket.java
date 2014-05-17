package net.modyssey.teleporters.markets.starmall;

import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

public class StarMallMarket implements IMarket {
    private StockList stockList;

    public StarMallMarket(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public String getStockTitle() {
        return "gui.modysseyteleporters.stock";
    }

    @Override
    public String getMarketTitle() {
        return "gui.modysseyteleporters.starmall";
    }

    @Override
    public ResourceLocation getMarketLogo() {
        return new ResourceLocation("modysseyteleporters:textures/gui/station_title_starmall.png");
    }

    @Override
    public StockList getStockList() {
        return stockList;
    }

    @Override
    public void initializeCart(TileEntityTeleporterController controller) {

    }

    @Override
    public boolean allowAddFromStock() {
        return true;
    }
}
