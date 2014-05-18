package net.modyssey.starmall.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import net.modyssey.starmall.client.gui.components.ScrollingList;
import net.modyssey.starmall.markets.Market;
import net.modyssey.starmall.tileentities.container.ContainerTeleporterController;
import org.lwjgl.opengl.GL11;

import java.awt.geom.Rectangle2D;

public class GuiCartList extends ScrollingList {
    private Market market;
    private GuiTeleporterController parent;
    private ContainerTeleporterController container;
    private RenderItem itemRenderer = new RenderItem();
    private FontRenderer fontRenderer;

    public GuiCartList(GuiTeleporterController parent, FontRenderer fontRenderer, ContainerTeleporterController container) {
        super(new Rectangle2D.Double(123,28,55,110), 20);

        this.parent = parent;
        this.fontRenderer = fontRenderer;
        itemRenderer.zLevel += 5;
        this.container = container;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public int getSelectedItem() { return container.getSelectedCartItem(); }
    public void clearSelectedItem() { container.setSelectedCartItem(-1); }

    @Override
    protected Rectangle2D getScrollGripBounds() {
        return new Rectangle2D.Double(207, 12, 6, 11);
    }

    @Override
    protected ResourceLocation getScrollGripTexture() {
        return new ResourceLocation("starmall:textures/gui/station.png");
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
        return new Rectangle2D.Double(178, 28, 6, 111);
    }

    @Override
    protected int getEntryCount() {
        return (market == null)?0:market.getCartSize();
    }

    @Override
    protected void itemMouseClick(int item) {
        container.setSelectedCartItem(item);
        parent.updateCartSelection();
    }

    @Override
    protected void drawEntry(int i, int y) {
        int rectX = getX() + 1;
        int rectY = getY() + y + 1;

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        if (i == container.getSelectedCartItem()) {
            drawHorizontalLine(getX(), getX() + getWidth() - 2, rectY-1, 0xFFFFFFFF);
            drawHorizontalLine(getX(), getX() + getWidth() - 2, rectY+getEntryHeight()-2, 0xFFFFFFFF);
            drawVerticalLine(getX(), rectY-1, rectY+getEntryHeight()-2, 0xFFFFFFFF);
            drawVerticalLine(getX() + getWidth() - 2, rectY-1, rectY+getEntryHeight()-2, 0xFFFFFFFF);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, -5);
        itemRenderer.renderItemIntoGUI(fontRenderer, Minecraft.getMinecraft().getTextureManager(), market.getCartContent(i), rectX, rectY, true);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);

        fontRenderer.drawString("x" + Integer.toString(market.getCartContent(i).stackSize), rectX + 18, rectY + 6, 0xFFFFFF, false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
