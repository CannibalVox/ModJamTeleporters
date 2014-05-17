package net.modyssey.teleporters.client.gui.components;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.awt.geom.Rectangle2D;

public abstract class ScrollingList extends Gui {
    private Rectangle2D viewportBounds;
    private int entryHeight;

    protected int getEntryHeight() { return entryHeight; }

    protected abstract Rectangle2D getScrollGripBounds();
    protected abstract ResourceLocation getScrollGripTexture();
    protected abstract Rectangle2D getScrollTrackBounds();
    protected abstract int getEntryCount();

    private int viewportTopPosition;

    protected ScrollingList(Rectangle2D viewportBounds, int entryHeight) {
        this.viewportBounds = viewportBounds;
        this.entryHeight = entryHeight;
    }

    public void drawList(int mouseX, int mouseY) {
        normalizeScrollPosition();

        drawScrollbar();
    }

    protected void normalizeScrollPosition() {
        int contentSize = getEntryCount() * getEntryHeight();
        int viewportSize = (int)viewportBounds.getHeight() + 1;
        int maxViewportPosition = contentSize - viewportSize;

        if (viewportTopPosition < 0)
            viewportTopPosition = 0;
        if (viewportTopPosition > maxViewportPosition)
            viewportTopPosition = maxViewportPosition;
    }

    protected void drawScrollbar() {
        double contentSize = getEntryCount() * getEntryHeight();
        double viewportSize = viewportBounds.getHeight();

        double gripToTrackRatio = viewportSize/contentSize;

        if (gripToTrackRatio < 1.0)
            gripToTrackRatio = 1;

        Rectangle2D trackBounds = getScrollTrackBounds();
        int gripHeight = (int)(trackBounds.getHeight() * gripToTrackRatio);
        Rectangle2D gripBounds = getScrollGripBounds();

        if ((int)gripBounds.getHeight() > gripHeight)
            gripHeight = (int)gripBounds.getHeight();

        double slideLength = trackBounds.getHeight() - gripHeight;

        int contentSlide =
    }
}
