package net.modyssey.teleporters;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.modyssey.teleporters.blocks.BlockTeleporterBeacon;
import net.modyssey.teleporters.blocks.BlockTeleporterController;
import net.modyssey.teleporters.blocks.BlockTeleporterPad;

@Mod(modid = "modysseyteleporters", version = ModysseyTeleporters.VERSION)
public class ModysseyTeleporters {
    public static final String VERSION = "1.0";

    public static Block teleporterPad;
    public static Block teleporterController;
    public static Block teleporterBeacon;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //Init blocks
        teleporterPad = new BlockTeleporterPad().setBlockName("modysseyteleporter.pad").setStepSound(Block.soundTypeMetal);
        teleporterController = new BlockTeleporterController().setBlockName("modysseyteleporter.controller").setStepSound(Block.soundTypeMetal);
        teleporterBeacon = new BlockTeleporterBeacon().setBlockName("modysseyteleporter.beacon").setStepSound(Block.soundTypeMetal);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //Register blocks
        GameRegistry.registerBlock(teleporterPad, "modysseyteleporters:pad");
        GameRegistry.registerBlock(teleporterController, "modysseyteleporters:controller");
        GameRegistry.registerBlock(teleporterBeacon, "modysseyteleporters:beacon");

        //Register recipes
    }
}
