package net.modyssey.teleporters.client.gui;

import cpw.mods.fml.client.GuiScrollingList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.modyssey.teleporters.markets.stock.StockList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiCategoryList extends GuiScrollingList {
    private StockList stockList;
    private GuiTeleporterController parent;

    public GuiCategoryList(GuiTeleporterController parent, int parentX, int parentY) {
        super(parent.mc, 55, 112, parentY+53, parentY+164, parentX+9, 20);

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
    protected void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {

    }

    @Override
    protected void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5) {

    }
}
