package net.modyssey.teleporters.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterBeacon;

public class BlockTeleporterBeacon extends BlockContainer {

    public static final int GUI_ID = 0;

    public BlockTeleporterBeacon() {
        super(Material.anvil);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityTeleporterBeacon();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
            FMLNetworkHandler.openGui(player, ModysseyTeleporters.instance, GUI_ID, world, x, y, z);

        return true;
    }
}
