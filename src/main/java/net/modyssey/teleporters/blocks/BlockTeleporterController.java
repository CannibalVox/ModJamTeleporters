package net.modyssey.teleporters.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

public class BlockTeleporterController extends BlockContainer {

    public static final int GUI_ID = 0;

    public BlockTeleporterController() {
        super(Material.anvil);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityTeleporterController();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
            FMLNetworkHandler.openGui(player, ModysseyTeleporters.instance, GUI_ID, world, x, y, z);

        return true;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}
