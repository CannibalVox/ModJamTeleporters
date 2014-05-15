package net.modyssey.teleporters.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.modyssey.teleporters.ModysseyTeleporters;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterPad;

public class BlockTeleporterPad extends BlockContainer {
    public BlockTeleporterPad() {
        super(Material.anvil);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        return metadata;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack item)
    {
        searchForStation(world, x, y, z);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbor) {
        deregister(world, x, y, z);

        searchForStation(world, x, y, z);
    }

    @Override
    public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
        super.onBlockPreDestroy(world, x, y, z, meta);
        deregister(world, x, y, z);
    }

    protected void deregister(World world, int x, int y, int z) {
        TileEntityTeleporterPad pad = (TileEntityTeleporterPad)world.getTileEntity(x, y, z);
        pad.deregister();
    }

    protected void searchForStation(World world, int x, int y, int z) {
        ForgeDirection dir = ForgeDirection.EAST;

        for (int i = 0; i < 4; i++) {
            Block block = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

            if (block == ModysseyTeleporters.teleporterController) {
                if (registerStation(world, dir, x, y, z))
                    return;
            } else if (block == this) {
                int metadata = world.getBlockMetadata(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);

                if (metadata != 0 && metadata != ForgeDirection.OPPOSITES[dir.ordinal()]) {
                    if (registerPad(world, dir, x, y, z))
                        return;
                }
            }

            dir = dir.getRotation(ForgeDirection.UP);
        }

        world.setBlockMetadataWithNotify(x, y, z, 0, 3);
    }

    private boolean registerStation(World world, ForgeDirection dir, int x, int y, int z) {
        TileEntityTeleporterPad pad = (TileEntityTeleporterPad)world.getTileEntity(x, y, z);

        if (pad.registerStation(x+dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
            world.setBlockMetadataWithNotify(x, y, z, dir.ordinal(), 3);
            return true;
        }

        return false;
    }

    private boolean registerPad(World world, ForgeDirection dir, int x, int y, int z) {
        TileEntityTeleporterPad pad = (TileEntityTeleporterPad)world.getTileEntity(x, y, z);

        if (pad.registerSameAs(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
            world.setBlockMetadataWithNotify(x, y, z, dir.ordinal(), 3);
            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new TileEntityTeleporterPad();
    }

    //VISUAL ASPECTS
    private IIcon bottomIcon;
    private IIcon[] sideIcons = new IIcon[4];
    private IIcon[] topIconsActive = new IIcon[48];
    private IIcon[] topIconsInactive = new IIcon[48];

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister registry) {
        bottomIcon = registry.registerIcon("modysseyteleporters:teleporter_pad_bottom");
        sideIcons[0] = registry.registerIcon("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_single");
        sideIcons[1] = registry.registerIcon("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_left");
        sideIcons[2] = registry.registerIcon("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_right");
        sideIcons[3] = registry.registerIcon("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_middle");

        for (int i = 0; i < 48; i++) {
            topIconsActive[i] = registry.registerIcon("modysseyteleporters:teleporter_pad_top/"+Integer.toString(i));
            topIconsInactive[i] = registry.registerIcon("modysseyteleporters:teleporter_pad_top_off/"+Integer.toString(i));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];

        if (dir == ForgeDirection.DOWN)
            return bottomIcon;

        if (dir == ForgeDirection.UP) {
            int topIconIndex = calcEightWayCtm(world, x, y, z, dir);
            if (world.getBlockMetadata(x, y, z) == 0)
                return topIconsInactive[topIconIndex];
            else
                return topIconsActive[topIconIndex];
        }

        int sideIndex = calcHorizontalCtm(world, x, y, z, dir);
        return sideIcons[sideIndex];
    }

    /**
     * Gets the block's texture. Args: side, meta
     */
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];

        switch(dir) {
            case UP:
                return topIconsActive[0];
            case DOWN:
                return bottomIcon;
            default:
                return sideIcons[0];
        }
    }

    private int calcHorizontalCtm(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
        ForgeDirection positiveX = getPositiveXAxis(dir);
        ForgeDirection negativeX = ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[positiveX.ordinal()]];

        int index = 0;
        if (doesConnect(world, x, y, z, positiveX))
            index |= 1;
        if (doesConnect(world, x, y, z, negativeX))
            index |= 2;

        //HACK: in 1.7.2 the east and north faces have their icons reversed
        if (dir == ForgeDirection.NORTH || dir == ForgeDirection.EAST) {
            if (index == 1)
                index = 2;
            else if (index == 2)
                index = 1;
        }

        return index;
    }

    private int calcEightWayCtm(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
        ForgeDirection negativeY = getNegativeYAxis(dir);
        ForgeDirection positiveY = ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[negativeY.ordinal()]];
        ForgeDirection positiveX = getPositiveXAxis(dir);
        ForgeDirection negativeX = ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[positiveX.ordinal()]];

        int connectionCode = 0;

        //North check
        if (doesConnect(world, x, y, z, negativeY))
            connectionCode |= 1;
        //South
        if (doesConnect(world, x, y, z, positiveY))
            connectionCode |= 2;
        //East
        if (doesConnect(world, x, y, z, positiveX))
            connectionCode |= 4;
        //West
        if (doesConnect(world, x, y, z, negativeX))
            connectionCode |= 8;

        //Northeast
        if ((connectionCode & 5) == 5 && doesConnect(world, x, y, z, negativeY, positiveX))
            connectionCode |= 0x10;
        //Northwest
        if ((connectionCode & 9) == 9 && doesConnect(world, x, y, z, negativeY, negativeX))
            connectionCode |= 0x20;
        //Southeast
        if ((connectionCode & 6) == 6 && doesConnect(world, x, y, z, positiveY, positiveX))
            connectionCode |= 0x40;
        //Southwest
        if ((connectionCode & 10) == 10 && doesConnect(world, x, y, z, positiveY, negativeX))
            connectionCode |= 0x80;

        return getIndexFromEightWayCtmCode(connectionCode);
    }

    private boolean doesConnect(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
        return doesConnect(world, x, y, z, dir, ForgeDirection.UNKNOWN);
    }

    private boolean doesConnect(IBlockAccess world, int x, int y, int z, ForgeDirection dir1, ForgeDirection dir2) {
        return world.getBlock(x + dir1.offsetX + dir2.offsetX, y + dir1.offsetY + dir2.offsetY, z + dir1.offsetZ + dir2.offsetZ) == this;
    }

    private ForgeDirection getNegativeYAxis(ForgeDirection dir) {
        switch(dir) {
            case UP:
                return ForgeDirection.NORTH;
            case DOWN:
                return ForgeDirection.SOUTH;
            default:
                return ForgeDirection.UP;
        }
    }

    private ForgeDirection getPositiveXAxis(ForgeDirection dir) {
        return getNegativeYAxis(dir).getRotation(dir);
    }

    private int getIndexFromEightWayCtmCode(int connectionCode) {
        switch(connectionCode) {
            case 0:
                return 0;
            case 4:
                return 1;
            case 12:
                return 2;
            case 8:
                return 3;
            case 6:
                return 4;
            case 10:
                return 5;
            case 7:
                return 6;
            case 14:
                return 7;
            case 0x1F:
                return 8;
            case 0x4F:
                return 9;
            case 0xAF:
                return 10;
            case 0x3F:
                return 11;
            case 2:
                return 12;
            case 0x46:
                return 13;
            case 0xCE:
                return 14;
            case 0x8A:
                return 15;
            case 5:
                return 16;
            case 9:
                return 17;
            case 0xD:
                return 18;
            case 0xB:
                return 19;
            case 0x2F:
                return 20;
            case 0x8F:
                return 21;
            case 0xCF:
                return 22;
            case 0x5F:
                return 23;
            case 3:
                return 24;
            case 0x57:
                return 25;
            case 0xFF:
                return 26;
            case 0xAB:
                return 27;
            case 0x47:
                return 28;
            case 0x8E:
                return 29;
            case 0x17:
                return 30;
            case 0x4E:
                return 31;
            case 0xBF:
                return 32;
            case 0x7F:
                return 33;
            case 0x9F:
                return 34;
            case 0x6F:
                return 35;
            case 1:
                return 36;
            case 0x15:
                return 37;
            case 0x3D:
                return 38;
            case 0x29:
                return 39;
            case 0x1D:
                return 40;
            case 0x2B:
                return 41;
            case 0x2D:
                return 42;
            case 0x8B:
                return 43;
            case 0xEF:
                return 44;
            case 0xDF:
                return 45;
            case 15:
                return 46;
            default:
                return 0;
        }
    }
}
