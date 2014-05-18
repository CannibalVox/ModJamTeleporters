package net.modyssey.teleporters.markets;

import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

import java.util.List;

public interface IMarket {
    String getStockTitle();
    String getMarketTitle();
    ResourceLocation getMarketLogo();
    StockList getStockList();
    void initializeCart(TileEntityTeleporterController controller);
    boolean allowAddFromStock();
    int getCartSize();
    ItemStack getCartContent(int index);
    void updateStock(StockList stock);
    boolean canAddToCart(ItemStack itemToAdd);

    void clearCart();
    void directAddToCart(ItemStack stack);
}
