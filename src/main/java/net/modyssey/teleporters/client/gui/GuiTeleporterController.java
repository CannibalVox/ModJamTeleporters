package net.modyssey.teleporters.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;
import org.lwjgl.opengl.GL11;

public class GuiTeleporterController extends GuiContainer {
    private TileEntityTeleporterController controller;

    private int marketIndex = 0;
    private static final int MARKET_COUNT = 2;
    private static final String[] MARKET_ICONS = {
            "modysseyteleporters:textures/gui/station_title_starmall.png",
            "modysseyteleporters:textures/gui/station_title_starmall.png"
    };

    public GuiTeleporterController(TileEntityTeleporterController controller) {
        super(new ContainerTeleporterController(controller));

        this.controller = controller;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString(fontRendererObj, "Pads Registered: " + Integer.toString(controller.getPadCount()), 8, 4, 0xFFFFFF);
    }

    protected void mouseClicked(int mouseX, int mouseY, int par3) {
        super.mouseClicked(mouseX, mouseY, par3);

        double uvW = 195;
        double uvH = 256;

        double h = uvH * 0.9;
        double w = uvW * 0.9;

        double x = (width - w) / 2;
        double y = ((height - h) / 2);

        if (par3 == 0) {
            int leftTabBound = (int)(x - 44.1);
            int topTabBound = (int)(y + 45);
            int rightTabBound = (int)x;
            int bottomTabBound = (int)(y + 45 + (MARKET_COUNT * 25.2));

            if (mouseX >= leftTabBound && mouseX <= rightTabBound && mouseY >= topTabBound && mouseY <= bottomTabBound) {
                int selectedMarket = (mouseY - 1 - topTabBound)/((bottomTabBound - topTabBound)/MARKET_COUNT);

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
        double uvW = 195;
        double uvH = 256;

        double h = uvH * 0.9;
        double w = uvW * 0.9;

        double x = (width - w) / 2;
        double y = ((height - h) / 2);

        double f = 0.00390625;
        double f1 = 0.00390625;
        mc.renderEngine.bindTexture(new ResourceLocation("modysseyteleporters:textures/gui/station.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, (double)this.zLevel, 0, 1);
        tessellator.addVertexWithUV(x + w, y + h, (double)this.zLevel, uvW * f, 1);
        tessellator.addVertexWithUV(x + w, y, (double)this.zLevel, uvW * f, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel, 0, 0);

        double physicalY = 45;
        for (int i = 0; i < MARKET_COUNT; i++) {
            if (i == marketIndex) {
                tessellator.addVertexWithUV(x - 47.7, y + physicalY + 25.2, (double) this.zLevel, 195 * f, 52 * f1);
                tessellator.addVertexWithUV(x, y + physicalY + 25.2, (double) this.zLevel, 248 * f, 52 * f1);
                tessellator.addVertexWithUV(x, y + physicalY, (double) this.zLevel, 248 * f, 24 * f1);
                tessellator.addVertexWithUV(x - 47.7, y + physicalY, (double) this.zLevel, 195 * f, 24 * f1);
            } else {
                tessellator.addVertexWithUV(x - 44.1, y + physicalY + 25.2, (double) this.zLevel, 195 * f, 80 * f1);
                tessellator.addVertexWithUV(x, y + physicalY + 25.2, (double) this.zLevel, 244 * f, 80 * f1);
                tessellator.addVertexWithUV(x, y + physicalY, (double) this.zLevel, 244 * f, 52 * f1);
                tessellator.addVertexWithUV(x - 44.1, y + physicalY, (double) this.zLevel, 195 * f, 52 * f1);
            }

            physicalY += 25.2;
        }

        tessellator.draw();

        mc.renderEngine.bindTexture(new ResourceLocation(MARKET_ICONS[marketIndex]));
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + 30.6, (double)this.zLevel, 0, 1);
        tessellator.addVertexWithUV(x + 118.8, y + 30.6, (double)this.zLevel, 1, 1);
        tessellator.addVertexWithUV(x + 118.8, y, (double)this.zLevel, 1, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel, 0, 0);
        tessellator.draw();
    }
}
