package net.modyssey.teleporters.markets;

import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.io.PadData;

import java.util.LinkedList;
import java.util.List;

public abstract class Market {

    private StockList stockList;
    private List<ItemStack> cart = new LinkedList<ItemStack>();

    public Market(StockList stockList) {
        this.stockList = stockList;
    }

    public abstract String getStockTitle();
    public abstract String getMarketTitle();
    public abstract ResourceLocation getMarketLogo();
    public abstract void initializeCart(TileEntityTeleporterController controller);
    public abstract boolean allowAddFromStock();
    public abstract boolean requiresBalanceToExchange();
    public abstract void applyBalance(int balance, TileEntityTeleporterController controller);
    public abstract boolean attemptExchangeStack(ItemStack stack, TileEntityTeleporterController controller);
    public abstract boolean forceExchangeStack(ItemStack stack, TileEntityTeleporterController controller);

    public StockList getStockList() {
        return stockList;
    }
    public int getCartSize() {
        return cart.size();
    }
    public ItemStack getCartContent(int index) {
        return cart.get(index);
    }
    public void clearCart() {
        cart.clear();
    }
    public void directAddToCart(ItemStack stack) {
        cart.add(stack);
    }

    protected void setCart(List<ItemStack> cart) {
        this.cart = cart;
    }

    public void updateStock(StockList stock) {
        StockList stockList = getStockList();
        stockList.clear();

        for (int i = 0; i < stock.getCategoryCount(); i++) {
            stockList.addCategory(stock.getCategory(i));
        }
    }

    public boolean canAddToCart(ItemStack itemToAdd) {

        StockList stockList = getStockList();

        for (int i = 0; i < stockList.getCategoryCount(); i++) {
            for (int j = 0; j < stockList.getCategory(i).getItemCount(); j ++) {
                if (PadData.canItemstacksStack(stockList.getCategory(i).get(j).getItem(), itemToAdd))
                    return true;
            }
        }

        return false;
    }

    public int getCartTotal() {
        int total = 0;

        for (int i = 0; i < cart.size(); i++) {
            ItemStack stack = cart.get(i);

            int price = stockList.getItemValue(stack);
            total += (price * stack.stackSize);
        }

        return total;
    }

    public void removeCartItem(int i) {
        cart.remove(i);
    }
}
