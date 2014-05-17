package net.modyssey.teleporters.markets.pawnshop;

import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

public class PawnShopMarket implements IMarket {
    private StockList stockList;

    public PawnShopMarket(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public String getStockTitle() {
        return "gui.modysseyteleporters.priceguide";
    }

    @Override
    public String getMarketTitle() {
        return "gui.modysseyteleporters.pawnshop";
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
    public void InitializeCart(TileEntityTeleporterController controller) {

    }

    @Override
    public boolean allowAddFromStock() {
        return false;
    }
}
