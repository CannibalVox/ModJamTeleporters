package net.modyssey.starmall.markets.starmall;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.modyssey.starmall.markets.Market;
import net.modyssey.starmall.markets.stock.StockList;
import net.modyssey.starmall.tileentities.TileEntityTeleporterController;

public class StarMallMarket extends Market {
    public StarMallMarket(StockList stockList) {
        super(stockList);
    }

    @Override
    public String getStockTitle() {
        return "gui.starmall.stock";
    }

    @Override
    public String getMarketTitle() {
        return "gui.starmall.starmall";
    }

    @Override
    public ResourceLocation getMarketLogo() {
        return new ResourceLocation("starmall:textures/gui/station_title_starmall.png");
    }

    @Override
    public void initializeCart(TileEntityTeleporterController controller) {
        //Nothing to initialize
    }

    @Override
    public boolean allowAddFromStock() {
        return true;
    }

    @Override
    public boolean requiresBalanceToExchange() {
        return true;
    }

    @Override
    public void applyBalance(int balance, TileEntityTeleporterController controller) {
        controller.adjustCreditAmount(balance * -1);
    }

    @Override
    public boolean attemptExchangeStack(ItemStack stack, TileEntityTeleporterController controller) {
        stack = controller.placeInPadContents(stack);

        return stack == null;
    }

    @Override
    public boolean forceExchangeStack(ItemStack stack, TileEntityTeleporterController controller) {
        stack = controller.dropOnPad(stack, false);

        if (stack != null)
            controller.dropOnPad(stack, true);

        return stack == null;
    }
}
