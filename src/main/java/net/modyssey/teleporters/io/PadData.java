package net.modyssey.teleporters.io;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.LinkedList;

public class PadData {
    private int padXCoord;
    private int padYCoord;
    private int padZCoord;

    public Iterable<ItemStack> getPadItems(World world) {
        LinkedList<ItemStack> allItems = new LinkedList<ItemStack>();
        Block topBlock = world.getBlock(padXCoord, padYCoord + 1, padZCoord);
        Item blockItem = Item.getItemFromBlock(topBlock);

        if (blockItem != null) {
            int damage = topBlock.getDamageValue(world, padXCoord, padYCoord+1, padZCoord);
            allItems.add(new ItemStack(blockItem, 1, damage));

            if (topBlock instanceof BlockContainer) {
                TileEntity tileEntity = world.getTileEntity(padXCoord, padYCoord + 1, padZCoord);

                if (tileEntity != null && tileEntity instanceof IInventory)) {
                    IInventory inventory = (IInventory)tileEntity;

                    for (int i = 0; i < inventory.getSizeInventory(); i++) {
                        ItemStack item = inventory.getStackInSlot(i);

                        if (item != null)
                            allItems.add(item);
                    }
                }
            }
        }

        for(Object item : world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(padXCoord, padYCoord + 1, padZCoord, padXCoord + 1, padYCoord + 2, padZCoord + 1))) {
            EntityItem entity = (EntityItem)item;
            if (!entity.isDead) {
                allItems.add(entity.getEntityItem());
            }
        }

        return allItems;
    }


}
