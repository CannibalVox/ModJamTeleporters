package net.modyssey.teleporters.client.gui;

import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.client.gui.components.ScrollingList;
import net.modyssey.teleporters.markets.stock.StockCategory;
import net.modyssey.teleporters.markets.stock.StockList;

import java.awt.geom.Rectangle2D;

public class GuiItemStockList extends ScrollingList {
    private StockCategory stockCategory;
    private GuiTeleporterController parent;

    public GuiItemStockList(GuiTeleporterController parent) {
        super(new Rectangle2D.Double(63,28,55,111), 20);

        this.parent = parent;
    }

    public void setStockCategory(StockCategory stockCategory) {
        this.stockCategory = stockCategory;
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
    protected int getScrollGripTopCap() {
        return 2;
    }

    @Override
    protected int getScrollGripBottomCap() {
        return 1;
    }

    @Override
    protected Rectangle2D getScrollTrackBounds() {
        return new Rectangle2D.Double(119, 28, 6, 111);
    }

    @Override
    protected int getEntryCount() {
        return (stockCategory == null)?0:stockCategory.getItemCount();
    }

    @Override
    protected void drawEntry(int i, int y) {
        int rectX = getX() + 1;
        int rectY = getY() + y + 1;

        drawRect(rectX, rectY, rectX + getWidth() - 2, rectY + getEntryHeight() - 2, 0xFFFFFFFF);
    }

    @Override
    protected void handleMouseInput(int mouseX, int mouseY) {
        super.handleMouseInput(mouseX + 54, mouseY - 25);
    }
}
