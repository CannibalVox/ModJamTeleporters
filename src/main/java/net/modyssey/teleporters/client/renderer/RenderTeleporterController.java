package net.modyssey.teleporters.client.renderer;

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
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
        GL11.glPushMatrix();
        bindTexture(new ResourceLocation(controllerTex));
        GL11.glRotatef(var8, 0, 1.0f, 0);
        GL11.glTranslated(var2, var4, var6);
        controllerModel.renderAll();
        GL11.glPopMatrix();
    }
}
