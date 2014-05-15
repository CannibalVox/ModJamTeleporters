package net.modyssey.teleporters.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

public class BlockTeleporterController extends BlockContainer {
    public BlockTeleporterController() {
        super(Material.anvil);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityTeleporterController();
    }
}
