package net.modyssey.teleporters.markets;

import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

public interface IMarket {
    String getStockTitle();
    ResourceLocation getMarketLogo();
    StockList getStockList();
    void InitializeCart(TileEntityTeleporterController controller);
    boolean allowAddFromStock();
}
