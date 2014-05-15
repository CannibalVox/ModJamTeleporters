package net.modyssey.teleporters.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BlockTeleporterPad extends Block {
    public BlockTeleporterPad() {
        super(Material.anvil);
    }

    private IIcon bottomIcon;
    private IIcon[] sideIcons = new IIcon[4];
    private IIcon[] topIcons = new IIcon[48];

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister p_149651_1_) {

    }
}
