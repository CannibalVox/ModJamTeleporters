package net.modyssey.teleporters.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
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
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.renderEngine.bindTexture(new ResourceLocation("modysseyteleporters:textures/gui/station.png"));
        int l = (width - 195) / 2;
        int i1 = ((height - 256) / 2) - 5;
        drawTexturedModalRect(l, i1, 0, 0, 195, 256);
    }
}
