package net.modyssey.teleporters.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterPad;

public class BlockTeleporterController extends BlockContainer {

    public static final int GUI_ID = 0;

    public BlockTeleporterController() {
        super(Material.anvil);

    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        super.onBlockPreDestroy(world, x, y, z, meta);

        TileEntityTeleporterController controller = (TileEntityTeleporterController)world.getTileEntity(x, y, z);
        controller.deregisterAllPads();
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

    @Override
    public int getRenderType() { return ModysseyTeleporters.TeleportControllerRenderId; }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item) {
        int quartile = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        world.setBlockMetadataWithNotify(x, y, z, quartile, 2);

        checkForPad(world, x, y, z, ForgeDirection.EAST);
        checkForPad(world, x, y, z, ForgeDirection.WEST);
        checkForPad(world, x, y, z, ForgeDirection.NORTH);
        checkForPad(world, x, y, z, ForgeDirection.SOUTH);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        return metadata;
    }

    protected void checkForPad(World world, int stationX, int stationY, int stationZ, ForgeDirection direction) {
        int x = stationX + direction.offsetX;
        int y = stationY + direction.offsetY - 1;
        int z = stationZ + direction.offsetZ;

        Block block = world.getBlock(x, y, z);

        if (block == ModysseyTeleporters.teleporterPad) {
            TileEntity padEntity = world.getTileEntity(x, y, z);

            if (padEntity != null && padEntity instanceof TileEntityTeleporterPad) {
                TileEntityTeleporterPad pad = (TileEntityTeleporterPad)padEntity;

                if (!pad.isRegistered() || pad.getDjikstraNumber() > 1) {
                    pad.registerStation(stationX, stationY, stationZ);
                    world.setBlockMetadataWithNotify(x, y, z, ForgeDirection.OPPOSITES[direction.ordinal()], 3);
                }
            }
        }
    }
}
