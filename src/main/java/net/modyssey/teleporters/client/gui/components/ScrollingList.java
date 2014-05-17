package net.modyssey.teleporters.client.gui.components;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.awt.geom.Rectangle2D;

public abstract class ScrollingList extends Gui {
    private Rectangle2D viewportBounds;
    private int entryHeight;

    protected abstract Rectangle2D getScrollGripUvs();
    protected abstract ResourceLocation getScrollGripTexture();
    protected abstract Rectangle2D getScrollTrackBounds();

    private int viewportTopPosition;

    protected  ScrollingList(Rectangle2D viewportBounds, int entryHeight) {
        this.viewportBounds = viewportBounds;
        this.entryHeight = entryHeight;
    }
}
