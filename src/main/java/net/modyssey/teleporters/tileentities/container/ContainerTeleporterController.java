package net.modyssey.teleporters.tileentities.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.modyssey.teleporters.markets.IMarket;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.network.ModysseyNetwork;
import net.modyssey.teleporters.network.TransmitCartUpdatePacket;
import net.modyssey.teleporters.network.TransmitFullCartPacket;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.io.PadData;

import java.util.List;

public class ContainerTeleporterController extends Container {
    private TileEntityTeleporterController controller;
    private IMarket[] markets;
    private IMarketFactory[] marketFactories;
    private int marketIndex;

    public ContainerTeleporterController(TileEntityTeleporterController controller, IMarketFactory[] marketFactories) {
        this.controller = controller;
        this.marketFactories = marketFactories;
        this.marketIndex = 0;
    }

    public void initMarkets() {
        this.markets = new IMarket[marketFactories.length];

        for (int i = 0; i < marketFactories.length; i++) {
            this.markets[i] = marketFactories[i].createMarket();

            if (!controller.getWorldObj().isRemote)
                this.markets[i].initializeCart(controller);
        }
    }

    public int getMarketIndex() { return marketIndex; }
    public int getMarketCount() { return markets.length; }
    public String getMarketTitle(int i) { return markets[i].getMarketTitle(); }
    public void setMarketIndex(int index) { marketIndex = index; }

    @Override
    public boolean canInteractWith(EntityPlayer var1) {
        return controller.canInteractWith(var1);
    }

    public void updateMarketData(List<StockList> marketData) {
        if (markets == null || markets.length == 0)
            return;

        for (int i = 0; i < markets.length; i++) {
            if (marketData.size() <= i)
                break;

            markets[i].updateStock(marketData.get(i));
        }
    }

    public int getCartTotal() {
        return 999999;
    }

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        if (!controller.getWorldObj().isRemote && par1ICrafting instanceof EntityPlayerMP)
            requestFullCart((EntityPlayerMP)par1ICrafting);
    }

    public IMarket getCurrentMarket() {
        return markets[marketIndex];
    }

    public void receiveFullCart(List<List<ItemStack>> carts) {
        for (int i = 0; i < markets.length; i++) {
            if (carts.size() <= i)
                break;

            List<ItemStack> cart = carts.get(i);
            markets[i].clearCart();

            for (int j = 0; j < cart.size(); j++) {
                markets[i].directAddToCart(cart.get(j));
            }
        }
    }

    public void receiveCartUpdate(int marketIndex, int cartIndex, ItemStack itemToUpdate) {
        IMarket market = markets[marketIndex];

        if (cartIndex < market.getCartSize() && PadData.canItemstacksStack(market.getCartContent(cartIndex), itemToUpdate))
            market.getCartContent(cartIndex).stackSize = itemToUpdate.stackSize;
        else
            market.directAddToCart(itemToUpdate);
    }

    public void requestFullCart(EntityPlayerMP player) {
        TransmitFullCartPacket packet = new TransmitFullCartPacket(this.windowId);

        for (int i = 0; i < markets.length; i++) {
            packet.addCart(markets[i]);
        }

        ModysseyNetwork.sendToPlayer(packet, player);
    }

    public void requestAddToCart(EntityPlayerMP player, int marketId, ItemStack itemToAdd) {
        if (marketId < 0 || marketId >= markets.length)
            return;

        IMarket market = markets[marketId];

        if (!market.canAddToCart(itemToAdd))
            return;

        for (int i = 0; i < market.getCartSize(); i++) {
            ItemStack cartStack = market.getCartContent(i);

            if (PadData.canItemstacksStack(cartStack, itemToAdd)) {
                cartStack.stackSize += itemToAdd.stackSize;
                TransmitCartUpdatePacket packet = new TransmitCartUpdatePacket(windowId, marketId, i, cartStack);
                ModysseyNetwork.sendToPlayer(packet, player);
                return;
            }
        }

        market.directAddToCart(itemToAdd);
        TransmitCartUpdatePacket packet = new TransmitCartUpdatePacket(windowId, marketId, market.getCartSize()-1, itemToAdd);
        ModysseyNetwork.sendToPlayer(packet, player);
    }
}
