package net.modyssey.teleporters.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.modyssey.teleporters.CommonProxy;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.client.renderer.RenderTeleporterController;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

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
