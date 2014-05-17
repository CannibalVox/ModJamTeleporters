package net.modyssey.teleporters.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;
import org.lwjgl.opengl.GL11;

public class GuiTeleporterController extends GuiContainer {
    private TileEntityTeleporterController controller;
    private IMarket[] markets;

    private int marketIndex = 0;

    public GuiTeleporterController(TileEntityTeleporterController controller, IMarketFactory[] marketFactories) {
        super(new ContainerTeleporterController(controller));

        this.controller = controller;
        this.markets = new IMarket[marketFactories.length];

        for (int i = 0; i < marketFactories.length; i++) {
            this.markets[i] = marketFactories[i].createMarket();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawTabLabels();
        drawCreditLine();

        fontRendererObj.drawString(StatCollector.translateToLocal(markets[marketIndex].getStockTitle()), 2, 17, 0x404040, false);
        fontRendererObj.drawString(StatCollector.translateToLocal("gui.modysseyteleporters.cart"), 50, 17, 0x404040, false);

    }

    private void drawCreditLine() {
        int credits = controller.getCredits();
        String creditLine = StatCollector.translateToLocal("gui.modysseyteleporters.credits") + ": $" + Integer.toString(999999);

        GL11.glPushMatrix();
        GL11.glTranslatef(105.5f, 4.5f, 0);
        GL11.glScaled(0.9, 0.9, 1);
        fontRendererObj.drawString(creditLine, 0, 0, 0x000000, false);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef(105, 4, 0);
        GL11.glScaled(0.9, 0.9, 1);
        fontRendererObj.drawString(creditLine, 0, 0, 0xFFFFFF, false);
        GL11.glPopMatrix();
    }

    private void drawTabLabels() {
        int titleY = 40;
        for (int i = 0; i < markets.length; i++) {
            String title = StatCollector.translateToLocal(markets[i].getMarketTitle());

            int lineCount = fontRendererObj.listFormattedStringToWidth(title, 60).size();

            int halfText = (lineCount * 9)/2;

            GL11.glPushMatrix();
            GL11.glTranslatef(-51.5f, titleY - halfText + 0.5f, 0);
            GL11.glScaled(0.8f, 0.8f, 1);
            fontRendererObj.drawSplitString(title, 0, 0, 60, 0x000000);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(-52, titleY - halfText, 0);
            GL11.glScaled(0.8f, 0.8f, 1);
            fontRendererObj.drawSplitString(title, 0, 0, 60, 0xFFFFFF);
            GL11.glPopMatrix();

            titleY += 28;
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int par3) {
        super.mouseClicked(mouseX, mouseY, par3);

        double w = 195;
        double h = 216;

        double x = (width - w) / 2;
        double y = (height - h) / 2;

        if (par3 == 0) {
            int leftTabBound = (int)(x - 49);
            int topTabBound = (int)(y + 50);
            int rightTabBound = (int)x;
            int bottomTabBound = (int)(y + 50 + (markets.length * 28));

            if (mouseX >= leftTabBound && mouseX <= rightTabBound && mouseY >= topTabBound && mouseY <= bottomTabBound) {
                int selectedMarket = (mouseY - 1 - topTabBound)/((bottomTabBound - topTabBound)/markets.length);

                if (selectedMarket != marketIndex) {
                    marketIndex = selectedMarket;
                }

                return;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        double w = 195;
        double h = 216;
        double x = (width - w) / 2;
        double y = (height - h) / 2;

        double f = 0.00390625;
        double f1 = 0.00390625;
        mc.renderEngine.bindTexture(new ResourceLocation("modysseyteleporters:textures/gui/station.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, (double)this.zLevel, 0, 0.84375);
        tessellator.addVertexWithUV(x + w, y + h, (double)this.zLevel, w * f, 0.84375);
        tessellator.addVertexWithUV(x + w, y, (double)this.zLevel, w * f, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel, 0, 0);

        double physicalY = 50;
        for (int i = 0; i < markets.length; i++) {
            if (i == marketIndex) {
                tessellator.addVertexWithUV(x - 49, y + physicalY + 28, (double) this.zLevel, 195 * f, 52 * f1);
                tessellator.addVertexWithUV(x+4, y + physicalY + 28, (double) this.zLevel, 248 * f, 52 * f1);
                tessellator.addVertexWithUV(x+4, y + physicalY, (double) this.zLevel, 248 * f, 24 * f1);
                tessellator.addVertexWithUV(x - 49, y + physicalY, (double) this.zLevel, 195 * f, 24 * f1);
            } else {
                tessellator.addVertexWithUV(x - 49, y + physicalY + 28, (double) this.zLevel, 195 * f, 80 * f1);
                tessellator.addVertexWithUV(x, y + physicalY + 28, (double) this.zLevel, 244 * f, 80 * f1);
                tessellator.addVertexWithUV(x, y + physicalY, (double) this.zLevel, 244 * f, 52 * f1);
                tessellator.addVertexWithUV(x - 49, y + physicalY, (double) this.zLevel, 195 * f, 52 * f1);
            }

            physicalY += 28;
        }

        tessellator.draw();

        mc.renderEngine.bindTexture(markets[marketIndex].getMarketLogo());
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + 34, (double)this.zLevel, 0, 1);
        tessellator.addVertexWithUV(x + 132, y + 34, (double)this.zLevel, 1, 1);
        tessellator.addVertexWithUV(x + 132, y, (double)this.zLevel, 1, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel, 0, 0);
        tessellator.draw();
    }
}
