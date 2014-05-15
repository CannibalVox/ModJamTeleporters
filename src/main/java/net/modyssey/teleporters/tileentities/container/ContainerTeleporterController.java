package net.modyssey.teleporters.tileentities.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

public class ContainerTeleporterController extends Container {
    private TileEntityTeleporterController controller;

    public ContainerTeleporterController(TileEntityTeleporterController controller) {
        this.controller = controller;
    }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        return false;
    }
}
