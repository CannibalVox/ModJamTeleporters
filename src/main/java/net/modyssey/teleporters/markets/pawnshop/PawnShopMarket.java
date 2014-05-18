package net.modyssey.teleporters.markets.pawnshop;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.io.PadData;

import java.util.LinkedList;
import java.util.List;

public class PawnShopMarket implements IMarket {
    private StockList stockList;
    private LinkedList<ItemStack> cart = new LinkedList<ItemStack>();

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

        for (int i = cart.size()-1; i >= 0; i--) {
            if (!canAddToCart(cart.get(i)))
                cart.remove(i);
        }
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
    public boolean canAddToCart(ItemStack itemToAdd) {
        for (int i = 0; i < stockList.getCategoryCount(); i++) {
            for (int j = 0; j < stockList.getCategory(i).getItemCount(); j ++) {
                if (PadData.canItemstacksStack(stockList.getCategory(i).get(j).getItem(), itemToAdd))
                    return true;
            }
        }

        return false;
    }

    @Override
    public void updateStock(StockList stock) {
        stockList.clear();

        for (int i = 0; i < stock.getCategoryCount(); i++) {
            stockList.addCategory(stock.getCategory(i));
        }
    }

    @Override
    public void clearCart() {
        cart.clear();
    }

    @Override
    public void directAddToCart(ItemStack stack) {
        cart.add(stack);
    }
}
