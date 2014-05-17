package net.modyssey.teleporters.client.gui;

import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.modyssey.teleporters.markets.stock.StockList;

public class GuiCategoryList extends GuiListExtended {
    private StockList stockList;
    private GuiTeleporterController parent;

    public GuiCategoryList(GuiTeleporterController parent, int parentX, int parentY) {
        super(parent.mc, 55, 112, parentY+53, parentY+164, 20);
        setSlotXBoundsFromLeft(parentX+9);

        this.parent = parent;
    }

    public void setStockList(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    public IGuiListEntry getListEntry(int var1) {
        return null;
    }

    @Override
    protected int getSize() {
        return stockList.getCategoryCount();
    }

    @Override
    protected void drawContainerBackground(Tessellator tessellator)
    {

    }
}
