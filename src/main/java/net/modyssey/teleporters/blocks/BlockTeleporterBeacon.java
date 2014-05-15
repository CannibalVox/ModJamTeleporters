package net.modyssey.teleporters.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterBeacon;

public class BlockTeleporterBeacon extends BlockContainer {
    public BlockTeleporterBeacon() {
        super(Material.anvil);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityTeleporterBeacon();
    }
}
