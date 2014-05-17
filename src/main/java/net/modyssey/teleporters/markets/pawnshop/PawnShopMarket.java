package net.modyssey.teleporters.markets.pawnshop;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

import java.util.LinkedList;

public class PawnShopMarket implements IMarket {
    private StockList stockList;
    private LinkedList<ItemStack> cart;

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
        return new ResourceLocation("modysseyteleporters:textures/gui/logo_pawn.png");
    }

    @Override
    public StockList getStockList() {
        return stockList;
    }

    @Override
    public void initializeCart(TileEntityTeleporterController controller) {
        cart = controller.getPadContents();
    }

    @Override
    public boolean allowAddFromStock() {
        return false;
    }

    @Override
    public int getCartSize() {
        return cart.size();
    }

    @Override
    public ItemStack getCartContent(int index) {
        return cart.get(index);
    }

    @Override
    public void addFromStock(ItemStack item) {
        //NOPE
    }
}
