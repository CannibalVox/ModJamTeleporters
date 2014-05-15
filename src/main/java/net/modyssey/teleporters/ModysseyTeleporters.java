package net.modyssey.teleporters;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.modyssey.teleporters.blocks.BlockTeleporterController;
import net.modyssey.teleporters.blocks.BlockTeleporterPad;
import net.modyssey.teleporters.client.renderer.RenderTeleporterController;
import net.modyssey.teleporters.handlers.ModysseyGuiHandler;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;

@Mod(modid = "modysseyteleporters", version = ModysseyTeleporters.VERSION)
public class ModysseyTeleporters {
    public static final String VERSION = "1.0";

    public static Block teleporterPad;
    public static Block teleporterController;

    public static Item teleportCircuit;

    @Mod.Instance
    public static ModysseyTeleporters instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Init blocks
        teleporterPad = new BlockTeleporterPad().setBlockName("modysseyteleporter.pad").setStepSound(Block.soundTypeMetal).setCreativeTab(CreativeTabs.tabTransport);
        teleporterController = new BlockTeleporterController().setBlockName("modysseyteleporter.controller").setStepSound(Block.soundTypeMetal).setCreativeTab(CreativeTabs.tabAllSearch.tabTransport);

        //Init items
        teleportCircuit = new Item().setUnlocalizedName("modysseyteleporter.circuit").setCreativeTab(CreativeTabs.tabMaterials);

        //Register blocks
        GameRegistry.registerBlock(teleporterPad, "pad");
        GameRegistry.registerBlock(teleporterController, "controller");

        //Register items
        GameRegistry.registerItem(teleportCircuit, "circuit");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //Register handlers
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModysseyGuiHandler());

        //Register tile entities
        GameRegistry.registerTileEntity(TileEntityTeleporterController.class, "TileEntityTeleporterController");

        //Register tile entity renderers
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTeleporterController.class, new RenderTeleporterController("modysseyteleporters:models/Station.tcn", "modysseyteleporters:textures/models/Station.png"));

        //Register recipes
        GameRegistry.addRecipe(new ShapedOreRecipe(teleportCircuit, "XOX","PPP", 'X', "dustGlowstone", 'O', "gemDiamond", 'P', "plankWood" ));
        GameRegistry.addRecipe(new ShapedOreRecipe(teleporterController, "XXX", "XOX", "XXX", 'X', "gemDiamond", 'O', teleportCircuit));
        GameRegistry.addRecipe(new ShapedOreRecipe(teleporterPad, "XXX", "XOX", "XXX", 'X', Items.iron_ingot, 'O', teleportCircuit));
    }
}
