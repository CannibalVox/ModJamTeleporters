package net.modyssey.teleporters.client.gui;

import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.client.gui.components.ScrollingList;
import net.modyssey.teleporters.markets.stock.StockList;

import java.awt.geom.Rectangle2D;

public class GuiCategoryList extends ScrollingList {
    private StockList stockList;
    private GuiTeleporterController parent;

    public GuiCategoryList(GuiTeleporterController parent, int parentX, int parentY) {
        super(new Rectangle2D.Double(1,28,55,111), 20);

        this.parent = parent;
    }

    public void setStockList(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    protected Rectangle2D getScrollGripBounds() {
        return new Rectangle2D.Double(195, 13, 6, 11);
    }

    @Override
    protected ResourceLocation getScrollGripTexture() {
        return new ResourceLocation("modysseyteleporters:textures/gui/station.png");
    }

    @Override
    protected Rectangle2D getScrollTrackBounds() {
        return new Rectangle2D.Double(56, 28, 6, 111);
    }

    @Override
    protected int getEntryCount() {
        return (stockList == null)?0:stockList.getCategoryCount();
    }

    @Override
    protected void drawEntry(int i, int y) {
        drawTexturedModalRect(getX(),getY() + y,110, 36, 10, 10);
        //drawRect(getX() + 1, y + 1, getWidth() - 2, getEntryHeight() - 2, 0xFFFFFF);
    }
}
