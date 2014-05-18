package net.modyssey.teleporters.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.geom.Rectangle2D;

public class Button extends Gui {
    private Rectangle2D physicalBounds;
    private Rectangle2D normalUVBounds;
    private Rectangle2D clickedUVBounds;
    private Rectangle2D disabledUVBounds;
    private Rectangle2D hoverUVBounds;
    private ResourceLocation buttonTexture;

    private boolean isMouseDown = false;
    private boolean isClicked = false;
    private boolean isHovering = false;
    private boolean isEnabled = true;

    private boolean hasClickEvent = false;

    public Button(ResourceLocation buttonTexture, Rectangle2D physicalBounds, Rectangle2D normalUVBounds, Rectangle2D clickedUVBounds, Rectangle2D disabledUVBounds, Rectangle2D hoverUVBounds) {
        this.buttonTexture = buttonTexture;
        this.physicalBounds = physicalBounds;
        this.normalUVBounds = normalUVBounds;
        this.clickedUVBounds = clickedUVBounds;
        this.disabledUVBounds = disabledUVBounds;
        this.hoverUVBounds = hoverUVBounds;
    }

    public void drawButton(int mouseX, int mouseY) {
        processMouseInput(mouseX, mouseY);

        if (!isEnabled)
            draw(physicalBounds, disabledUVBounds);
        else if (isClicked && isHovering)
            draw(physicalBounds, clickedUVBounds);
        else if (isHovering)
            draw(physicalBounds, hoverUVBounds);
        else
            draw(physicalBounds, normalUVBounds);
    }

    protected void processMouseInput(int mouseX, int mouseY) {
        boolean mouseDown = Mouse.isButtonDown(0);

        if (!isEnabled) {
            isHovering = false;
            isClicked = false;
            hasClickEvent = false;
            return;
        }

        isHovering = false;

        if (mouseX >= (int)physicalBounds.getX() && mouseX <= (int)(physicalBounds.getX() + physicalBounds.getWidth()) &&
                mouseY >= (int)physicalBounds.getY() && mouseY <= (int)(physicalBounds.getY() + physicalBounds.getHeight()))
            isHovering = true;

        if (mouseDown != isMouseDown) {
            isMouseDown = mouseDown;
            if (isMouseDown && isHovering) {
                isClicked = true;
            } else if (!isMouseDown && isClicked && isHovering) {
                hasClickEvent = true;
            }
        }

        if (!isMouseDown)
            isClicked = false;

    }

    public void setEnabled(boolean enabled) { isEnabled = enabled; }
    public boolean pollClickEvent() {
        boolean event = hasClickEvent;
        hasClickEvent = false;
        return event;
    }

    private void draw(Rectangle2D bounds, Rectangle2D uvBounds) {
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();
        int width = (int)bounds.getWidth();
        int height = (int)bounds.getHeight();
        int u = (int)uvBounds.getX();
        int v = (int)uvBounds.getY();

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().renderEngine.bindTexture(buttonTexture);
        drawTexturedModalRect(x, y, u, v, width, height);
    }
}
