package net.modyssey.teleporters.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;
import org.lwjgl.opengl.GL11;

public class GuiTeleporterController extends GuiContainer {
    private TileEntityTeleporterController controller;

    public GuiTeleporterController(TileEntityTeleporterController controller) {
        super(new ContainerTeleporterController(controller));

        this.controller = controller;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString(fontRendererObj, "Pads Registered: " + Integer.toString(controller.getPadCount()), 8, 4, 0xFFFFFF);
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
        mc.renderEngine.bindTexture(new ResourceLocation("modysseyteleporters:textures/gui/station_title_starmall.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, (double)this.zLevel, 0, uvH * f1);
        tessellator.addVertexWithUV(x + w, y + h, (double)this.zLevel, uvW * f, uvH * f1);
        tessellator.addVertexWithUV(x + w, y, (double)this.zLevel, uvW * f, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel, 0, 0);
        tessellator.draw();

        uvW = 266;
        uvH = 70;

        h = uvH * 0.9;
        w = uvW * 0.9;

        mc.renderEngine.bindTexture(new ResourceLocation("modysseyteleporters:textures/gui/station_title_starmall.png"));
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, (double)this.zLevel, 0, uvH * f1);
        tessellator.addVertexWithUV(x + w, y + h, (double)this.zLevel, uvW * f, uvH * f1);
        tessellator.addVertexWithUV(x + w, y, (double)this.zLevel, uvW * f, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel, 0, 0);
        tessellator.draw();
    }
}
