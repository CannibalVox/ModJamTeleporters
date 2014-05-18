package net.modyssey.starmall.handlers;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.modyssey.starmall.ModysseyTeleporters;
import net.modyssey.starmall.blocks.BlockTeleporterController;
import net.modyssey.starmall.client.gui.GuiTeleporterController;
import net.modyssey.starmall.markets.IMarketFactory;
import net.modyssey.starmall.network.FullMarketDataPacket;
import net.modyssey.starmall.network.ModysseyNetwork;
import net.modyssey.starmall.tileentities.TileEntityTeleporterController;
import net.modyssey.starmall.tileentities.container.ContainerTeleporterController;

public class ModysseyGuiHandler implements IGuiHandler {
    private IMarketFactory[] marketFactories;

    public ModysseyGuiHandler(IMarketFactory[] marketFactories) {
        this.marketFactories = marketFactories;
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch (ID) {
            case BlockTeleporterController.GUI_ID: {
                FullMarketDataPacket marketData = ModysseyTeleporters.instance.getMarketDataPacket();
                ModysseyNetwork.sendToPlayer(marketData, player);

                ContainerTeleporterController container = new ContainerTeleporterController((TileEntityTeleporterController) world.getTileEntity(x, y, z), marketFactories);
                container.initMarkets();
                return container;
            }
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch (ID) {
            case BlockTeleporterController.GUI_ID:
                return new GuiTeleporterController((TileEntityTeleporterController) world.getTileEntity(x, y, z), marketFactories);
            default:
                return null;
        }
    }
}
