package net.modyssey.teleporters.client.gui;

import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.client.gui.components.ScrollingList;
import net.modyssey.teleporters.markets.stock.StockList;

import java.awt.geom.Rectangle2D;

public class GuiCategoryList extends ScrollingList {
    private StockList stockList;
    private GuiTeleporterController parent;

    public GuiCategoryList(GuiTeleporterController parent, int parentX, int parentY) {
        super(new Rectangle2D.Double(9,53,1,1), 20);

        this.parent = parent;
    }

    public void setStockList(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    protected Rectangle2D getScrollGripBounds() {
        return null;
    }

    @Override
    protected ResourceLocation getScrollGripTexture() {
        return null;
    }

    @Override
    protected Rectangle2D getScrollTrackBounds() {
        return null;
    }

    @Override
    protected int getEntryCount() {
        return 0;
    }

    @Override
    protected void drawEntry(int i, int y) {

    }
}
