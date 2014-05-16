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

    public ItemStack attemptRemoveItems(World world, ItemStack itemsToRemove, boolean checkItemDamage) {
        for (Object item : world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(padXCoord, padYCoord + 1, padZCoord, padXCoord + 1, padYCoord + 2, padZCoord + 1))) {
            EntityItem entity = (EntityItem) item;
            if (!entity.isDead) {
                ItemStack stack = entity.getEntityItem();

                if (itemMatches(stack, itemsToRemove, checkItemDamage)) {
                    if (stack.stackSize >= itemsToRemove.stackSize) {
                        if (stack.stackSize > itemsToRemove.stackSize) {
                            stack.stackSize -= itemsToRemove.stackSize;
                            entity.setEntityItemStack(stack);
                        } else
                            entity.setDead();

                        return null;
                    } else {
                        itemsToRemove.stackSize -= stack.stackSize;
                        entity.setDead();
                    }
                }
            }
        }

        Block topBlock = world.getBlock(padXCoord, padYCoord + 1, padZCoord);

        if (topBlock instanceof BlockContainer) {
            TileEntity tileEntity = world.getTileEntity(padXCoord, padYCoord + 1, padZCoord);

            if (tileEntity != null && tileEntity instanceof IInventory) {
                IInventory inventory = (IInventory) tileEntity;

                for (int i = 0; i < inventory.getSizeInventory(); i++) {
                    ItemStack item = inventory.getStackInSlot(i);

                    if (itemMatches(item, itemsToRemove, checkItemDamage)) {
                        if (item.stackSize <= itemsToRemove.stackSize) {
                            itemsToRemove.stackSize -= item.stackSize;
                            inventory.setInventorySlotContents(i, null);
                        } else {
                            item.stackSize -= itemsToRemove.stackSize;
                            itemsToRemove.stackSize = 0;
                            inventory.setInventorySlotContents(i, item);
                        }

                        if (itemsToRemove.stackSize == 0)
                            return null;
                    }
                }
            }
        }

        return itemsToRemove;
    }

    private boolean itemMatches(ItemStack itemStack1, ItemStack itemStack2, boolean checkItemDamage) {
        if (Item.itemRegistry.getIDForObject(itemStack1.getItem()) != Item.itemRegistry.getIDForObject(itemStack2.getItem()))
            return false;

        if (checkItemDamage && itemStack1.getItemDamage() != itemStack2.getItemDamage())
            return false;

        return true;
    }
}
