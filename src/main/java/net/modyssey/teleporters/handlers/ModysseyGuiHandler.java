package net.modyssey.teleporters.handlers;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.modyssey.teleporters.blocks.BlockTeleporterController;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;

public class ModysseyGuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch (ID) {
            case BlockTeleporterController.GUI_ID:
                return new ContainerTeleporterController((TileEntityTeleporterController)world.getTileEntity(x, y, z));
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        switch (ID) {
            case BlockTeleporterController.GUI_ID:
                return new GuiTeleporterController((TileEntityTeleporterController) world.getTileEntity(x, y, z));
            default:
                return null;
        }
    }
}
