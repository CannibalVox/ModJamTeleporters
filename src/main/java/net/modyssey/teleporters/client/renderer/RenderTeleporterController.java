package net.modyssey.teleporters.client.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

public class RenderTeleporterController extends TileEntitySpecialRenderer {

    private String controllerTex;
    private IModelCustom controllerModel;

    public RenderTeleporterController(String controllerModel, String controllerTex) {
        this.controllerTex = controllerTex;
        this.controllerModel = AdvancedModelLoader.loadModel(new ResourceLocation(controllerModel));
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float angle) {
        GL11.glPushMatrix();
        bindTexture(new ResourceLocation(controllerTex));
        GL11.glTranslated(x, y, z);
        controllerModel.renderAll();
        GL11.glRotatef(angle, 0, 1.0f, 0);

        GL11.glPopMatrix();
    }
}
