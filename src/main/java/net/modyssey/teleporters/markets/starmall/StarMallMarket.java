package net.modyssey.teleporters.markets.starmall;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.Market;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.io.PadData;

import java.util.LinkedList;

public class StarMallMarket extends Market {
    public StarMallMarket(StockList stockList) {
        super(stockList);
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
    public boolean applyBalance(int balance, TileEntityTeleporterController controller) {
        return false;
    }

    @Override
    public boolean attemptExchangeStack(ItemStack stack, TileEntityTeleporterController controller) {
        return false;
    }

    @Override
    public boolean forceExchangeStack(ItemStack stack, TileEntityTeleporterController controller) {
        return false;
    }
}
