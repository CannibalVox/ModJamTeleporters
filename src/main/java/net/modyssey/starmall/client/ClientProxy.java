package net.modyssey.starmall.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.modyssey.starmall.CommonProxy;
import net.modyssey.starmall.ModysseyTeleporters;
import net.modyssey.starmall.client.renderer.RenderTeleporterController;
import net.modyssey.starmall.tileentities.TileEntityTeleporterController;

public class ClientProxy extends CommonProxy {

    private RenderTeleporterController teleporterController = new RenderTeleporterController("modysseyteleporters:textures/models/Station.png");

    @Override
    public void registerRendererIds() {
        ModysseyTeleporters.TeleportControllerRenderId = RenderingRegistry.getNextAvailableRenderId();
    }

    @Override
    public void registerBlockRenderers() {
        RenderingRegistry.registerBlockHandler(ModysseyTeleporters.TeleportControllerRenderId, teleporterController);
    }

    @Override
    public void registerTileEntityRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeleporterController.class, teleporterController);
    }
}
