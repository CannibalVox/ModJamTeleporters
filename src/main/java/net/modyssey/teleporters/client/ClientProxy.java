package net.modyssey.teleporters.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.modyssey.teleporters.CommonProxy;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.client.gui.GuiTeleporterController;
import net.modyssey.teleporters.client.renderer.RenderTeleporterController;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

import java.util.List;

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

    @Override
    public void updateCurrentStarmallGui(List<StockList> allMarkets) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiTeleporterController) {
            ((GuiTeleporterController)Minecraft.getMinecraft().currentScreen).updateMarketData(allMarkets);
        }
    }
}
