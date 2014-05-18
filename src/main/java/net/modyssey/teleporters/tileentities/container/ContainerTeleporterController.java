package net.modyssey.teleporters.tileentities.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import net.modyssey.teleporters.markets.Market;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockItem;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.network.ModysseyNetwork;
import net.modyssey.teleporters.network.TransmitCartRemoveItemPacket;
import net.modyssey.teleporters.network.TransmitCartUpdatePacket;
import net.modyssey.teleporters.network.TransmitFullCartPacket;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.io.PadData;

import java.util.List;

public class ContainerTeleporterController extends Container {
    private TileEntityTeleporterController controller;
    private Market[] markets;
    private IMarketFactory[] marketFactories;
    private int marketIndex;

    private int selectedCartItem = -1;

    public ContainerTeleporterController(TileEntityTeleporterController controller, IMarketFactory[] marketFactories) {
        this.controller = controller;
        this.marketFactories = marketFactories;
        this.marketIndex = 0;
    }

    public void initMarkets() {
        this.markets = new Market[marketFactories.length];

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
    public int getSelectedCartItem() { return selectedCartItem; }
    public void setSelectedCartItem(int index) { selectedCartItem = index; }

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

    @Override
    public void addCraftingToCrafters(ICrafting par1ICrafting) {
        if (!controller.getWorldObj().isRemote && par1ICrafting instanceof EntityPlayerMP)
            requestFullCart((EntityPlayerMP)par1ICrafting);
    }

    public Market getCurrentMarket() {
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

    public void receiveCartUpdate(int marketIndex, int cartIndex, ItemStack itemToUpdate, boolean addIfUnlocated) {
        if (marketIndex < 0 || marketIndex >= markets.length)
            return;

        Market market = markets[marketIndex];

        if (cartIndex < market.getCartSize() && PadData.canItemstacksStack(market.getCartContent(cartIndex), itemToUpdate))
            market.getCartContent(cartIndex).stackSize = itemToUpdate.stackSize;
        else if (addIfUnlocated)
            market.directAddToCart(itemToUpdate);
    }

    public void receiveCartRemove(int marketIndex, int cartIndex) {
        if (marketIndex < 0 || marketIndex >= markets.length)
            return;

        Market market = markets[marketIndex];

        market.removeCartItem(cartIndex);

        if (cartIndex == selectedCartItem)
            selectedCartItem = -1;
        else if (cartIndex < selectedCartItem)
            selectedCartItem--;
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

        Market market = markets[marketId];

        if (!market.canAddToCart(itemToAdd))
            return;

        for (int i = 0; i < market.getCartSize(); i++) {
            ItemStack cartStack = market.getCartContent(i);

            if (PadData.canItemstacksStack(cartStack, itemToAdd)) {
                cartStack.stackSize += itemToAdd.stackSize;
                TransmitCartUpdatePacket packet = new TransmitCartUpdatePacket(windowId, marketId, i, cartStack, true);
                ModysseyNetwork.sendToPlayer(packet, player);
                return;
            }
        }

        market.directAddToCart(itemToAdd);
        TransmitCartUpdatePacket packet = new TransmitCartUpdatePacket(windowId, marketId, market.getCartSize()-1, itemToAdd, true);
        ModysseyNetwork.sendToPlayer(packet, player);
    }

    public void requestRemoveFromCart(EntityPlayerMP player, int marketIndex, int cartIndex, ItemStack itemToRemove) {
        if (marketIndex < 0 || marketIndex >= markets.length)
            return;

        Market market = markets[marketIndex];

        if (!market.allowAddFromStock())
            return;

        if (cartIndex < 0 || cartIndex >= market.getCartSize())
            return;

        ItemStack cartItem = market.getCartContent(cartIndex);

        if (!PadData.canItemstacksStack(cartItem, itemToRemove))
            return;

        if (cartItem.stackSize > itemToRemove.stackSize) {
            cartItem.stackSize -= itemToRemove.stackSize;
            TransmitCartUpdatePacket packet = new TransmitCartUpdatePacket(windowId, marketIndex, cartIndex, cartItem, false);
            ModysseyNetwork.sendToPlayer(packet, player);
        } else {
            market.removeCartItem(cartIndex);
            TransmitCartRemoveItemPacket packet = new TransmitCartRemoveItemPacket(windowId, marketIndex, cartIndex);
            ModysseyNetwork.sendToPlayer(packet, player);
        }
    }

    public void requestExchange(EntityPlayerMP player, int marketIndex) {
        if (marketIndex < 0 || marketIndex >= markets.length)
            return;

        Market market = markets[marketIndex];

        if (controller.getPadCount() == 0)
            return;

        //For markets that pull their cart from the pad, we should refresh the pad before selling or whatever
        if (!market.allowAddFromStock())
            market.initializeCart(controller);

        if (market.getCartSize() == 0)
            return;

        int total = market.getCartTotal();
        int balance = controller.getCredits();

        if (market.requiresBalanceToExchange() && total > balance)
            return;

        int totalExchangedValue = 0;
        for (int i = market.getCartSize() - 1; i >= 0; i--) {
            totalExchangedValue += exchangeStack(market, i, false);
        }

        for (int i = market.getCartSize() - 1; i >= 0; i--) {
            totalExchangedValue += exchangeStack(market, i, true);
        }

        market.applyBalance(totalExchangedValue, controller);

        if (!market.allowAddFromStock())
            market.initializeCart(controller);

        requestFullCart(player);
    }

    private int exchangeStack(Market market, int cartIndex, boolean forceExchange) {
        ItemStack exchangeStack = market.getCartContent(cartIndex);

        int startSize = exchangeStack.stackSize;
        int endSize = 0;
        int unitValue = market.getStockList().getItemValue(exchangeStack);

        boolean exchangedWholeStack = false;

        if (forceExchange)
            exchangedWholeStack = market.forceExchangeStack(exchangeStack, controller);
        else
            exchangedWholeStack = market.attemptExchangeStack(exchangeStack, controller);

        if (!exchangedWholeStack) {
            endSize = exchangeStack.stackSize;
        } else
            market.removeCartItem(cartIndex);

        if (endSize < startSize) {
            return (startSize - endSize) * unitValue;
        }

        return 0;
    }
}
