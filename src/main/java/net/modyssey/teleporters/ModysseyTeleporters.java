package net.modyssey.teleporters;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.modyssey.teleporters.blocks.BlockTeleporterBeacon;
import net.modyssey.teleporters.blocks.BlockTeleporterController;
import net.modyssey.teleporters.blocks.BlockTeleporterPad;

@Mod(modid = "modysseyteleporters", version = ModysseyTeleporters.VERSION)
public class ModysseyTeleporters {
    public static final String VERSION = "1.0";

    public static Block teleporterPad;
    public static Block teleporterController;
    public static Block teleporterBeacon;

    public static Item teleportCircuit;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Init blocks
        teleporterPad = new BlockTeleporterPad().setBlockName("modysseyteleporter.pad").setStepSound(Block.soundTypeMetal).setCreativeTab(CreativeTabs.tabTransport);
        teleporterController = new BlockTeleporterController().setBlockName("modysseyteleporter.controller").setStepSound(Block.soundTypeMetal).setCreativeTab(CreativeTabs.tabAllSearch.tabTransport);
        teleporterBeacon = new BlockTeleporterBeacon().setBlockName("modysseyteleporter.beacon").setStepSound(Block.soundTypeMetal).setCreativeTab(CreativeTabs.tabTransport);

        //Init items
        teleportCircuit = new Item().setUnlocalizedName("modysseyteleporter.circuit").setCreativeTab(CreativeTabs.tabMaterials);

        //Register blocks
        GameRegistry.registerBlock(teleporterPad, "pad");
        GameRegistry.registerBlock(teleporterController, "controller");
        GameRegistry.registerBlock(teleporterBeacon, "beacon");

        //Register items
        GameRegistry.registerItem(teleportCircuit, "circuit");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //Register recipes
        GameRegistry.addRecipe(new ShapedOreRecipe(teleportCircuit, "XOX","PPP", 'X', "dustGlowstone", 'O', "gemDiamond", 'P', "plankWood" ));
        GameRegistry.addRecipe(new ShapedOreRecipe(teleporterController, "XXX", "XOX", "XXX", 'X', "gemDiamond", 'O', teleportCircuit));
        GameRegistry.addRecipe(new ShapedOreRecipe(teleporterPad, "XXX", "XOX", "XXX", 'X', Items.iron_ingot, 'O', teleportCircuit));
        GameRegistry.addRecipe(new ShapedOreRecipe(teleporterBeacon, "XXX", "XOX", "XXX", 'X', Items.gold_ingot, 'O', teleportCircuit));
    }
}
