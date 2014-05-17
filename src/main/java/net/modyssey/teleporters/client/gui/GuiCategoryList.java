package net.modyssey.teleporters.client.gui;

import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.modyssey.teleporters.markets.stock.StockList;

public class GuiCategoryList extends GuiScrollingList {
    private StockList stockList;
    private GuiTeleporterController parent;

    public GuiCategoryList(GuiTeleporterController parent) {
        super(parent.mc, 56, 112, 53, 164, 9, 20);

        this.parent = parent;
    }

    public void setStockList(StockList stockList) {
        this.stockList = stockList;
    }

    @Override
    protected int getSize() {
        return (stockList == null)?0:stockList.getCategoryCount();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelected(int index) {
        return false;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {

    }
}
