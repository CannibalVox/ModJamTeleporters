package net.modyssey.starmall.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.modyssey.starmall.ModysseyStarMall;
import net.modyssey.starmall.tileentities.TileEntityTeleporterController;
import net.modyssey.starmall.tileentities.TileEntityTeleporterPad;

public class BlockTeleporterController extends BlockContainer {

    public static final int GUI_ID = 0;

    public BlockTeleporterController() {
        super(Material.anvil);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);

        boolean isTopBlock = (meta & 4) != 0;
        meta &= 3;

        ForgeDirection dir = ForgeDirection.SOUTH;

        for (int i = 0; i < meta; i++) {
            dir = dir.getRotation(ForgeDirection.UP);
        }

        float minX = 0;
        float minZ = 0;
        float maxX = 1;
        float maxZ = 1;

        if (dir.offsetX < 0)
            minX -= 0.0625f;
        if (dir.offsetX > 0)
            maxX += 0.0625f;
        if (dir.offsetZ < 0)
            minZ -= 0.0625f;
        if (dir.offsetZ > 0)
            maxZ += 0.0625f;

        float minY = 0;
        float maxY = 1.625f;

        if (isTopBlock) {
            minY -= 1.0f;
            maxY -= 1.0f;
        }

        setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        super.onBlockPreDestroy(world, x, y, z, meta);

        if ((meta & 4) != 0)
            return;

        TileEntityTeleporterController controller = (TileEntityTeleporterController)world.getTileEntity(x, y, z);
        controller.deregisterAllPads();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        super.breakBlock(world, x, y, z, block, meta);

        if ((meta & 4) != 0)
            y--;
        else
            y++;

        if (world.getBlock(x, y, z) == this) {
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityTeleporterController((var2 & 4) == 0);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) {
            int meta = world.getBlockMetadata(x, y, z);

            if ((meta & 4) != 0)
                y--;

            FMLNetworkHandler.openGui(player, ModysseyStarMall.instance, GUI_ID, world, x, y, z);
        }

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
    public int getRenderType() { return ModysseyStarMall.TeleportControllerRenderId; }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item) {
        int quartile = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        world.setBlockMetadataWithNotify(x, y, z, quartile, 2);
        world.setBlock(x, y+1, z, this, quartile | 4, 2);

        if (!world.isRemote) {
            checkForPad(world, x, y, z, ForgeDirection.EAST);
            checkForPad(world, x, y, z, ForgeDirection.WEST);
            checkForPad(world, x, y, z, ForgeDirection.NORTH);
            checkForPad(world, x, y, z, ForgeDirection.SOUTH);
        }
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

        if (block == ModysseyStarMall.teleporterPad) {
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
