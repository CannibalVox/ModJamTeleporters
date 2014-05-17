package net.modyssey.teleporters.client.gui;

import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.client.gui.components.ScrollingList;
import net.modyssey.teleporters.markets.IMarket;

import java.awt.geom.Rectangle2D;

public class GuiCartList extends ScrollingList {
    private IMarket market;
    private GuiTeleporterController parent;

    public GuiCartList(GuiTeleporterController parent) {
        super(new Rectangle2D.Double(131,28,41,110), 20);

        this.parent = parent;
    }

    public void setMarket(IMarket market) {
        this.market = market;
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
        return new Rectangle2D.Double(172, 28, 6, 111);
    }

    @Override
    protected int getEntryCount() {
        return (market == null)?0:market.getCartSize();
    }

    @Override
    protected void drawEntry(int i, int y) {
        int rectX = getX() + 1;
        int rectY = getY() + y + 1;

        drawRect(rectX, rectY, rectX + getWidth() - 2, rectY + getEntryHeight() - 2, 0xFFFFFFFF);
    }

    @Override
    protected void handleMouseInput(int mouseX, int mouseY) {
        super.handleMouseInput(mouseX - 9, mouseY - 25);
    }
}
