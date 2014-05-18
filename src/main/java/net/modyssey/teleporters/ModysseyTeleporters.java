package net.modyssey.teleporters;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
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
import net.modyssey.teleporters.handlers.ModysseyGuiHandler;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.pawnshop.PawnShopMarketFactory;
import net.modyssey.teleporters.markets.starmall.StarMallMarketFactory;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.network.FullMarketDataPacket;
import net.modyssey.teleporters.network.ModysseyNetwork;
import net.modyssey.teleporters.parser.MarketDataParser;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterPad;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Mod(modid = "modysseyteleporters", version = ModysseyTeleporters.VERSION)
public class ModysseyTeleporters {
    public static final String VERSION = "1.0";

    public static Block teleporterPad;
    public static Block teleporterController;

    public static Item teleportCircuit;

    public static int TeleportControllerRenderId;

    private IMarketFactory[] markets;

    @SidedProxy(clientSide = "net.modyssey.teleporters.client.ClientProxy", serverSide = "net.modyssey.teleporters.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static ModysseyTeleporters instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        MarketDataParser parser = new MarketDataParser();
        File configFile = event.getSuggestedConfigurationFile();

        ResourceLoader.class.getResource(getResourcePath("/" + iconName))

        try {
            InputStream stream = FileUtils.openInputStream(configFile);
            List<StockList> stockLists = parser.parseMarketData(stream);

            IMarketFactory starmall = new StarMallMarketFactory(stockLists.get(0));
            IMarketFactory pawnshop = new PawnShopMarketFactory(stockLists.get(1));

            markets = new IMarketFactory[] { starmall, pawnshop };
        } catch (IOException ex) {
            throw new RuntimeException("Attempting to write out the default didn't work for some weird reason.");
        }

        //Init blocks
        teleporterPad = new BlockTeleporterPad().setBlockName("modysseyteleporter.pad").setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(CreativeTabs.tabRedstone);
        teleporterController = new BlockTeleporterController().setBlockName("modysseyteleporter.controller").setHardness(5.0f).setResistance(10.0f).setBlockTextureName("modysseyteleporters:teleporter_pad_side/teleporter_pad_side_single").setStepSound(Block.soundTypeMetal).setCreativeTab(CreativeTabs.tabRedstone);

        //Init items
        teleportCircuit = new Item().setUnlocalizedName("modysseyteleporter.circuit").setTextureName("modysseyteleporters:circuitboard").setCreativeTab(CreativeTabs.tabMaterials);

        //Register blocks
        GameRegistry.registerBlock(teleporterPad, "pad");
        GameRegistry.registerBlock(teleporterController, "controller");

        //Register items
        GameRegistry.registerItem(teleportCircuit, "circuit");

        //Register renderer ID's
        proxy.registerRendererIds();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModysseyNetwork.init();

        //Register handlers
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new ModysseyGuiHandler(markets));

        //Register tile entities
        GameRegistry.registerTileEntity(TileEntityTeleporterController.class, "TileEntityTeleporterController");
        GameRegistry.registerTileEntity(TileEntityTeleporterPad.class, "TileEntityTeleporterPad");

        //Register tile entity renderers
        proxy.registerBlockRenderers();

        proxy.registerTileEntityRenderers();

        //Register recipes
        GameRegistry.addRecipe(new ShapedOreRecipe(teleportCircuit, "XOX","PPP", 'X', "dustGlowstone", 'O', "gemDiamond", 'P', "plankWood" ));
        GameRegistry.addRecipe(new ShapedOreRecipe(teleporterController, "XXX", "XOX", "XXX", 'X', "gemDiamond", 'O', teleportCircuit));
        GameRegistry.addRecipe(new ShapedOreRecipe(teleporterPad, "XXX", "XOX", "XXX", 'X', Items.iron_ingot, 'O', teleportCircuit));
    }

    public void setMarketData(List<StockList> allMarketData) {
        for (int i = 0; i < markets.length; i++) {
            if (i >= allMarketData.size())
                break;

            markets[i].setStockData(allMarketData.get(i));
        }
    }

    public FullMarketDataPacket getMarketDataPacket() {
        FullMarketDataPacket packet = new FullMarketDataPacket();

        for (int i = 0; i < markets.length; i++) {
            packet.addMarket(markets[i].getStockList());
        }

        return packet;
    }
}
