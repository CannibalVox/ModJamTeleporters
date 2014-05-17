package net.modyssey.teleporters.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

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
    protected void itemMouseDown(int item) {}
    protected void itemMouseClick(int item) {}

    private int viewportTopPosition;

    private static final int SCROLL_GRIP = -1;
    private static final int TOP_SCROLL_TRACK = -2;
    private static final int BOTTOM_SCROLL_TRACK = -3;
    private static final int NOTHING = -4;
    private static final int OUT_OF_BOUNDS = -5;
    private int mouseDownElement;
    private int lastMouseX;
    private int lastMouseY;
    private boolean isMouseDown;

    protected ScrollingList(Rectangle2D viewportBounds, int entryHeight) {
        this.viewportBounds = viewportBounds;
        this.entryHeight = entryHeight;
    }

    protected int getMouseOverElement(int mouseX, int mouseY) {
        double x = mouseX;
        double y = mouseY;

        if (x >= viewportBounds.getX() && x <= (viewportBounds.getX() + viewportBounds.getWidth()) &&
                y >= viewportBounds.getY() && y <= (viewportBounds.getY() + viewportBounds.getHeight())) {
            y += viewportTopPosition;
            y /= getEntryHeight();

            if (y < 0 || y >= getEntryCount())
                return NOTHING;
            else
                return (int)y;
        }

        Rectangle2D trackBounds = getScrollTrackBounds();

        if (x >= trackBounds.getX() && x <= (trackBounds.getX() + trackBounds.getWidth()) &&
                y >= viewportBounds.getY() && y <= (viewportBounds.getY() + viewportBounds.getHeight())) {

            int gripPos = calculateVisibleGripPosition();
            int gripHeight = calculateVisibleGripHeight();

            if (y < (int)getScrollTrackBounds().getY() + gripPos)
                return TOP_SCROLL_TRACK;
            else if (y >= (int)getScrollTrackBounds().getY() + gripPos + gripHeight)
                return BOTTOM_SCROLL_TRACK;
            else
                return SCROLL_GRIP;
        }

        return OUT_OF_BOUNDS;
    }

    protected void mouseDownOn(int element) {
        if (element >= 0) {
            itemMouseDown(element);
        } else if (element == TOP_SCROLL_TRACK) {
            viewportTopPosition -= (int)viewportBounds.getHeight();
            normalizeScrollPosition();
        } else if (element == BOTTOM_SCROLL_TRACK) {
            viewportTopPosition += (int)viewportBounds.getHeight();
            normalizeScrollPosition();
        }
    }

    protected void mouseClickOn(int element) {
        if (element >= 0) {
            itemMouseClick(element);
        }
    }

    protected void dragGrip(int deltaY) {
        int gripSize = calculateVisibleGripHeight();
        double slideLength = getScrollTrackBounds().getHeight() - gripSize;

        double contentPct = (double)deltaY / slideLength;

        int movementSize = (int)((double)(getEntryCount() * getEntryHeight()) * contentPct);

        viewportTopPosition += movementSize;
        normalizeScrollPosition();
    }

    protected void handleMouseInput(int mouseX, int mouseY) {
        boolean mouseCurrentlyDown = Mouse.isButtonDown(0);
        int mouseOverElement = getMouseOverElement(mouseX, mouseY);

        if (mouseCurrentlyDown != isMouseDown) {
            isMouseDown = mouseCurrentlyDown;
            if (mouseCurrentlyDown) {
                mouseDownElement = mouseOverElement;
                mouseDownOn(mouseOverElement);
            } else if (mouseOverElement == mouseDownElement) {
                mouseClickOn(mouseOverElement);
            }
        } else if (mouseCurrentlyDown && mouseDownElement == SCROLL_GRIP) {
            dragGrip(mouseY - lastMouseY);
        } else if (!mouseCurrentlyDown) {
            if (mouseOverElement >= 0 || mouseOverElement == NOTHING) {
                for (; !Minecraft.getMinecraft().gameSettings.touchscreen && Mouse.next(); Minecraft.getMinecraft().currentScreen.handleMouseInput())
                {
                    int j1 = Mouse.getEventDWheel();

                    if (j1 != 0)
                    {
                        if (j1 > 0)
                        {
                            j1 = -1;
                        }
                        else if (j1 < 0)
                        {
                            j1 = 1;
                        }

                        this.viewportTopPosition += (float)(j1 * 20);
                    }
                }
            }
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
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

        handleMouseInput(mouseX, mouseY);
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

    protected int calculateVisibleGripHeight() {
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

        return gripHeight;
    }

    protected int calculateVisibleGripPosition() {
        double contentSize = getEntryCount() * getEntryHeight();
        double viewportSize = viewportBounds.getHeight();

        int gripHeight = calculateVisibleGripHeight();
        Rectangle2D trackBounds = getScrollTrackBounds();

        //Get the position of the grip on the track
        double slideLength = trackBounds.getHeight() - gripHeight;
        double contentSlide = contentSize - viewportSize;
        double slideToContentRatio = slideLength/contentSlide;

        int gripPosition = (int)(slideToContentRatio * (double)viewportTopPosition);

        return gripPosition;
    }

    protected void drawScrollbar() {

        int gripHeight = calculateVisibleGripHeight();
        int gripPosition = calculateVisibleGripPosition();

        Rectangle2D gripBounds = getScrollGripBounds();
        Rectangle2D trackBounds = getScrollTrackBounds();

        int gripNativeHeight = (int)gripBounds.getHeight();
        int gripNativeWidth = (int)gripBounds.getWidth();
        int trackX = (int)trackBounds.getX();
        int trackY = (int)trackBounds.getY();
        int gripX = (int)gripBounds.getX();
        int gripY = (int)gripBounds.getY();

        trackY += gripPosition;

        int topCap = getScrollGripTopCap();
        int bottomCap = getScrollGripBottomCap();

        Minecraft.getMinecraft().renderEngine.bindTexture(getScrollGripTexture());

        //Draw top cap
        drawTexturedModalRect(trackX, trackY, gripX, gripY, gripNativeWidth, topCap);
        //Draw bottom cap
        drawTexturedModalRect(trackX, trackY + gripHeight - bottomCap, gripX, gripY + gripNativeHeight - bottomCap, gripNativeWidth, bottomCap);

        trackY += topCap;
        gripY += topCap;
        gripHeight -= (bottomCap + topCap);
        gripNativeHeight -= (bottomCap + topCap);

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
