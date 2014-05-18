package net.modyssey.teleporters.client.gui.components;

import net.minecraft.client.gui.Gui;

import java.awt.geom.Rectangle2D;

public class Button extends Gui {
    private Rectangle2D physicalBounds;
    private Rectangle2D normalUVBounds;
    private Rectangle2D clickedUVBounds;
    private Rectangle2D disabledUVBounds;
    private Rectangle2D hoverUVBounds;

    private boolean isMouseDown = false;
    private boolean isClicked = false;
    private boolean isHovering = false;
    private boolean isEnabled = true;

    public Button(Rectangle2D physicalBounds, Rectangle2D normalUVBounds, Rectangle2D clickedUVBounds, Rectangle2D disabledUVBounds, Rectangle2D hoverUVBounds) {
        this.physicalBounds = physicalBounds;
        this.normalUVBounds = normalUVBounds;
        this.clickedUVBounds = clickedUVBounds;
        this.disabledUVBounds = disabledUVBounds;
        this.hoverUVBounds = hoverUVBounds;
    }

    public void drawButton(int mouseX, int mouseY) {
        processMouseInput(mouseX, mouseY);
    }
}
