package net.modyssey.teleporters.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.modyssey.teleporters.ModysseyTeleporters;
import org.lwjgl.opengl.GL11;

public class RenderTeleporterController extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

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

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        bindTexture(new ResourceLocation(controllerTex));
        //GL11.glTranslated(x, y, z);
        controllerModel.renderAll();
        //GL11.glRotatef(angle, 0, 1.0f, 0);

        GL11.glPopMatrix();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return ModysseyTeleporters.TeleportControllerRenderId;
    }
}
