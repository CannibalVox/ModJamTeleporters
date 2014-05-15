package net.modyssey.teleporters.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockTeleporterPad extends Block {
    public BlockTeleporterPad() {
        super(Material.anvil);
    }

    private IIcon bottomIcon;
    private IIcon[] sideIcons = new IIcon[4];
    private IIcon[] topIcons = new IIcon[48];

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister registry) {
        bottomIcon = registry.registerIcon("modysseyteleporters:pad_bottom");
        sideIcons[0] = registry.registerIcon("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_single");
        sideIcons[1] = registry.registerIcon("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_left");
        sideIcons[2] = registry.registerIcon("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_right");
        sideIcons[3] = registry.registerIcon("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_middle");

        for (int i = 0; i < 48; i++) {
            topIcons[i] = registry.registerIcon("modysseyteleporters:teleporter_pad_top/"+Integer.toString(i));
        }
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.VALID_DIRECTIONS[side];

        if (dir == ForgeDirection.DOWN)
            return bottomIcon;

        if (dir == ForgeDirection.UP)
            return topIcons[calcEightWayCtm(world, x, y, z, dir)];

        return sideIcons[calcHorizontalCtm(world, x, y, z, dir)];
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
                return topIcons[0];
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
        if (world.getBlock(x + positiveX.offsetX, y + positiveX.offsetY, z + positiveX.offsetZ) == this)
            index |= 1;
        if (world.getBlock(x + negativeX.offsetX, y + negativeX.offsetY, z + negativeX.offsetZ) == this)
            index |= 2;

        return index;
    }

    private int calcEightWayCtm(IBlockAccess world, int x, int y, int z, ForgeDirection dir) {
        ForgeDirection negativeY = getNegativeYAxis(dir);
        ForgeDirection positiveY = ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[negativeY.ordinal()]];
        ForgeDirection positiveX = getPositiveXAxis(dir);
        ForgeDirection negativeX = ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[positiveX.ordinal()]];
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
}
