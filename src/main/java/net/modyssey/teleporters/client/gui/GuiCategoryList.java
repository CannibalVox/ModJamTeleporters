package net.modyssey.teleporters.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.client.gui.components.ScrollingList;
import net.modyssey.teleporters.markets.stock.StockList;
import org.lwjgl.opengl.GL11;

import java.awt.geom.Rectangle2D;

public class GuiCategoryList extends ScrollingList {
    private StockList stockList;
    private GuiTeleporterController parent;
    private RenderItem itemRenderer = new RenderItem();
    private FontRenderer fontRenderer;

    public GuiCategoryList(GuiTeleporterController parent, FontRenderer fontRenderer) {
        super(new Rectangle2D.Double(0,28,55,110), 20);

        this.parent = parent;
        this.fontRenderer = fontRenderer;
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
    protected int getScrollGripTopCap() {
        return 2;
    }

    @Override
    protected int getScrollGripBottomCap() {
        return 1;
    }

    @Override
    protected Rectangle2D getScrollTrackBounds() {
        return new Rectangle2D.Double(55, 28, 6, 111);
    }

    @Override
    protected int getEntryCount() {
        return (stockList == null)?0:stockList.getCategoryCount();
    }

    @Override
    protected void drawEntry(int i, int y) {
        int rectX = getX() + 1;
        int rectY = getY() + y + 1;

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        itemRenderer.zLevel = this.zLevel + 1;
        itemRenderer.renderItemIntoGUI(fontRenderer, Minecraft.getMinecraft().getTextureManager(), stockList.getCategory(i).getIconItem(), rectX, rectY, true);

        fontRenderer.drawString(stockList.getCategory(i).getCategoryName(), rectX + 18, rectY + 6, 0xFFFFFF, false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
