package net.modyssey.teleporters.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.awt.geom.Rectangle2D;

public abstract class ScrollingList extends Gui {
    private Rectangle2D viewportBounds;
    private int entryHeight;

    protected int getEntryHeight() { return entryHeight; }
    protected int getX() { return (int)viewportBounds.getX(); }
    protected int getY() { return (int)viewportBounds.getY(); }
    protected int getWidth() { return (int)viewportBounds.getWidth(); }
    protected int getHeight() { return (int)viewportBounds.getHeight(); }

    protected abstract Rectangle2D getScrollGripBounds();
    protected abstract ResourceLocation getScrollGripTexture();
    protected abstract int getScrollGripTopCap();
    protected abstract int getScrollGripBottomCap();
    protected abstract Rectangle2D getScrollTrackBounds();
    protected abstract int getEntryCount();
    protected abstract void drawEntry(int i, int y);

    private int viewportTopPosition;

    protected ScrollingList(Rectangle2D viewportBounds, int entryHeight) {
        this.viewportBounds = viewportBounds;
        this.entryHeight = entryHeight;
    }

    public void drawList(int mouseX, int mouseY) {
        normalizeScrollPosition();

        drawScrollbar();

        int y = -viewportTopPosition;

        for (int i = 0; i < getEntryCount(); i++) {
            int nextY = y + getEntryHeight();

            if (nextY > 0) {
                drawEntry(i, y);
            }

            y = nextY;
        }
    }

    protected void normalizeScrollPosition() {
        int contentSize = getEntryCount() * getEntryHeight();
        int viewportSize = (int)viewportBounds.getHeight() + 1;
        int maxViewportPosition = contentSize - viewportSize;

        if (viewportTopPosition > maxViewportPosition)
            viewportTopPosition = maxViewportPosition;
        if (viewportTopPosition < 0)
            viewportTopPosition = 0;
    }

    protected void drawScrollbar() {
        double contentSize = getEntryCount() * getEntryHeight();
        double viewportSize = viewportBounds.getHeight();

        //Get the % of the track that the scroll grip makes up (can't be more than 100% of the track)
        double gripToTrackRatio = viewportSize/contentSize;
        if (gripToTrackRatio > 1.0)
            gripToTrackRatio = 1;

        //Get the actual grip size in pixels the grip should occupy... shouldn't be smaller than the grip
        //as shown in the source image
        Rectangle2D trackBounds = getScrollTrackBounds();
        int gripHeight = (int)(trackBounds.getHeight() * gripToTrackRatio);
        Rectangle2D gripBounds = getScrollGripBounds();

        int gripNativeHeight = (int)gripBounds.getHeight();
        if (gripNativeHeight > gripHeight)
            gripHeight = gripNativeHeight;

        //Get the position of the grip on the track
        double slideLength = trackBounds.getHeight() - gripHeight;
        double contentSlide = contentSize - viewportSize;
        double slideToContentRatio = slideLength/contentSlide;

        int gripPosition = (int)(slideToContentRatio * (double)viewportTopPosition);

        int gripNativeWidth = (int)gripBounds.getWidth();
        int trackX = (int)trackBounds.getX();
        int trackY = (int)trackBounds.getY();
        int gripX = (int)gripBounds.getX();
        int gripY = (int)gripBounds.getY();

        trackY += gripPosition;

        int topCap = getScrollGripTopCap();
        int bottomCap = getScrollGripBottomCap();

        Minecraft.getMinecraft().renderEngine.bindTexture(getScrollGripTexture());
        while (gripHeight > gripNativeHeight) {
            drawTexturedModalRect(trackX, trackY, gripX, gripY, gripNativeWidth, gripNativeHeight);
            trackY += gripNativeHeight;
            gripHeight -= gripNativeHeight;
        }

        if (gripHeight != 0) {
            drawTexturedModalRect(trackX, trackY, gripX, gripY, gripNativeWidth, gripHeight);
        }
    }
}
