package net.modyssey.teleporters.client.renderer;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.client.models.ModelStation;
import org.lwjgl.opengl.GL11;

public class RenderTeleporterController extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    private ResourceLocation controllerTex;
    private ModelStation controllerModel = new ModelStation();

    public RenderTeleporterController(String controllerTex) {
        this.controllerTex = new ResourceLocation(controllerTex);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float angle) {
        GL11.glPushMatrix();
        GL11.glTranslated(x+0.5, y+1.5, z+0.5);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glRotatef(180, 0, 1, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(controllerTex);
        controllerModel.render(0.0625f);
        GL11.glPopMatrix();
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        GL11.glPushMatrix();
        GL11.glTranslated(0, 0.5, 0);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glScaled(0.7, 0.7, 0.7);

        Minecraft.getMinecraft().renderEngine.bindTexture(controllerTex);
        controllerModel.render(0.0625f);

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
