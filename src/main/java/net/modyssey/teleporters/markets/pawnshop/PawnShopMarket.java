package net.modyssey.teleporters.markets.pawnshop;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.markets.Market;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.io.PadData;

import java.util.LinkedList;
import java.util.List;

public class PawnShopMarket extends Market {
    public PawnShopMarket(StockList stockList) {
        super(stockList);
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
    public boolean allowAddFromStock() {
        return false;
    }

    @Override
    public void initializeCart(TileEntityTeleporterController controller) {
         List<ItemStack> cart = controller.getPadContents();

        for (int i = cart.size()-1; i >= 0; i--) {
            if (!canAddToCart(cart.get(i)))
                cart.remove(i);
        }

        setCart(cart);
    }
}
