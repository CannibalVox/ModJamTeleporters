package net.modyssey.teleporters.client.gui;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.client.gui.components.ScrollingList;
import net.modyssey.teleporters.markets.IMarket;
import org.lwjgl.opengl.GL11;

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

        //drawRect(rectX, rectY, rectX + getWidth() - 2, rectY + getEntryHeight() - 2, 0xFFFFFFFF);

        int par0 = rectX;
        int par1 = rectY;
        int par2 = rectX + getWidth() - 2;
        int par3 = rectY + getEntryHeight() - 2;
        int par4 = 0xFFFFFFFF;

        int j1;

        if (par0 < par2)
        {
            j1 = par0;
            par0 = par2;
            par2 = j1;
        }

        if (par1 < par3)
        {
            j1 = par1;
            par1 = par3;
            par3 = j1;
        }

        float f3 = (float)(par4 >> 24 & 255) / 255.0F;
        float f = (float)(par4 >> 16 & 255) / 255.0F;
        float f1 = (float)(par4 >> 8 & 255) / 255.0F;
        float f2 = (float)(par4 & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f, f1, f2, f3);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)par0, (double)par3, 1);
        tessellator.addVertex((double)par2, (double)par3, 1);
        tessellator.addVertex((double)par2, (double)par1, 1);
        tessellator.addVertex((double)par0, (double)par1, 1);
        tessellator.draw();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
    }
}
