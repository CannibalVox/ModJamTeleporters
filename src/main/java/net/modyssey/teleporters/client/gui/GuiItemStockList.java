package net.modyssey.teleporters.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.client.gui.components.ScrollingList;
import net.modyssey.teleporters.markets.stock.StockCategory;
import org.lwjgl.opengl.GL11;

import java.awt.geom.Rectangle2D;

public class GuiItemStockList extends ScrollingList {
    private StockCategory stockCategory;
    private GuiTeleporterController parent;
    private RenderItem itemRenderer = new RenderItem();
    private FontRenderer fontRenderer;
    private int selectedItem = -1;

    public GuiItemStockList(GuiTeleporterController parent, FontRenderer fontRenderer) {
        super(new Rectangle2D.Double(57,28,55,110), 20);

        this.parent = parent;
        this.fontRenderer = fontRenderer;
    }

    public void setStockCategory(StockCategory stockCategory) {
        this.stockCategory = stockCategory;
    }

    public int getSelectedItem() { return selectedItem; }
    public void clearSelectedItem() { selectedItem = -1; }

    @Override
    protected void itemMouseClick(int item) {
        selectedItem = item;
        parent.updateItem();
    }

    @Override
    protected Rectangle2D getScrollGripBounds() {
        return new Rectangle2D.Double(207, 12, 6, 11);
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
        return new Rectangle2D.Double(112, 28, 6, 111);
    }

    @Override
    protected int getEntryCount() {
        return (stockCategory == null)?0:stockCategory.getItemCount();
    }

    @Override
    protected void drawEntry(int i, int y) {
        int rectX = getX() + 1;
        int rectY = getY() + y + 1;

        GL11.glEnable(GL11.GL_DEPTH_TEST);

        if (i == selectedItem) {
            drawHorizontalLine(getX(), getX() + getWidth() - 2, rectY-1, 0xFFFFFFFF);
            drawHorizontalLine(getX(), getX() + getWidth() - 2, rectY+getEntryHeight()-2, 0xFFFFFFFF);
            drawVerticalLine(getX(), rectY-1, rectY+getEntryHeight()-2, 0xFFFFFFFF);
            drawVerticalLine(getX() + getWidth() - 2, rectY-1, rectY+getEntryHeight()-2, 0xFFFFFFFF);
        }

        itemRenderer.zLevel = this.zLevel + 1;
        GL11.glEnable(GL11.GL_LIGHTING);
        itemRenderer.renderItemIntoGUI(fontRenderer, Minecraft.getMinecraft().getTextureManager(), stockCategory.get(i).getItem(), rectX, rectY, true);
        GL11.glDisable(GL11.GL_LIGHTING);

        String displayText = stockCategory.get(i).getItem().getDisplayName();
        int displayTextLen = fontRenderer.getStringWidth(displayText);

        if (displayTextLen >= 37) {
            String ellipsis = "...";
            int ellipsisLen = fontRenderer.getStringWidth(ellipsis);

            while (displayText.length() > 0 && displayTextLen + ellipsisLen >= 37) {
                displayText = displayText.substring(0, displayText.length()-2);
                displayTextLen = fontRenderer.getStringWidth(displayText);
            }

            displayText = displayText.concat(ellipsis);
        }

        fontRenderer.drawString(displayText, rectX + 18, rectY + 6, 0xFFFFFF, false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
