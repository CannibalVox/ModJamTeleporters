package net.modyssey.teleporters.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.modyssey.teleporters.client.gui.components.Button;
import net.modyssey.teleporters.client.gui.components.NumberOnlyTextField;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockItem;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.network.ModysseyNetwork;
import net.modyssey.teleporters.network.RequestCartAddPacket;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;
import org.lwjgl.opengl.GL11;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class GuiTeleporterController extends GuiContainer {
    private TileEntityTeleporterController controller;
    private ContainerTeleporterController containerTeleporterController;

    private GuiCategoryList categories;
    private GuiItemStockList stockItems;
    private GuiCartList cart;

    private Button addButton;
    private Button exchangeButton;

    private GuiTextField quantity;

    private StockItem selectedItem = null;

    public GuiTeleporterController(TileEntityTeleporterController controller, IMarketFactory[] marketFactories) {
        super(new ContainerTeleporterController(controller, marketFactories));

        this.controller = controller;
        this.containerTeleporterController = (ContainerTeleporterController)inventorySlots;
    }

    public void updateMarketData(List<StockList> marketData) {
        containerTeleporterController.updateMarketData(marketData);

        categories.setStockList(containerTeleporterController.getCurrentMarket().getStockList());
        stockItems.setStockCategory(null);
        selectedItem = null;
    }

    public void updateCategory() {
        stockItems.setStockCategory(containerTeleporterController.getCurrentMarket().getStockList().getCategory(categories.getSelectedCategory()));
        stockItems.clearSelectedItem();
        selectedItem = null;
    }

    public void updateItem() {
        selectedItem = containerTeleporterController.getCurrentMarket().getStockList().getCategory(categories.getSelectedCategory()).get(stockItems.getSelectedItem());
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        super.initGui();

        containerTeleporterController.initMarkets();

        int w = 195;
        int h = 216;

        int x = (width - w) / 2;
        int y = (height - h) / 2;

        categories = new GuiCategoryList(this, fontRendererObj);
        categories.setStockList(containerTeleporterController.getCurrentMarket().getStockList());

        stockItems = new GuiItemStockList(this, fontRendererObj);
        stockItems.setStockCategory(null);

        cart = new GuiCartList(this, fontRendererObj);
        cart.setMarket(containerTeleporterController.getCurrentMarket());

        addButton = new Button(new ResourceLocation("modysseyteleporters:textures/gui/station.png"), new Rectangle2D.Double(84, 162, 35, 22), new Rectangle2D.Double(207, 111, 35, 22), new Rectangle2D.Double(207, 133, 35, 22),
                new Rectangle2D.Double(207, 155, 35, 22), new Rectangle2D.Double(207, 177, 35, 22));

        exchangeButton = new Button(new ResourceLocation("modysseyteleporters:textures/gui/station.png"), new Rectangle2D.Double(129, 162, 49, 22), new Rectangle2D.Double(207, 23, 49, 22), new Rectangle2D.Double(207, 45, 49, 22),
                new Rectangle2D.Double(207, 67, 49, 22), new Rectangle2D.Double(207, 89, 49, 22));

        quantity = new NumberOnlyTextField(fontRendererObj, 86, 148, 31, 14);
        quantity.setMaxStringLength(3);
        quantity.setEnableBackgroundDrawing(false);
        quantity.setFocused(false);
        quantity.setText("1");
        quantity.setCanLoseFocus(true);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int w = 195;
        int h = 216;

        int x = (width - w) / 2;
        int y = (height - h) / 2;

        categories.drawList(mouseX - x - 9, mouseY - y - 25);
        stockItems.drawList(mouseX - x - 9, mouseY - y - 25);
        cart.drawList(mouseX - x - 9, mouseY - y - 25);

        if (containerTeleporterController.getCurrentMarket().allowAddFromStock()) {
            addButton.setEnabled(selectedItem != null);
            quantity.setEnabled(selectedItem != null);

            if (quantity.isFocused() && selectedItem == null)
                quantity.setFocused(false);
            quantity.setCanLoseFocus(selectedItem != null);

            addButton.drawButton(mouseX - x - 9, mouseY - y - 25);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            drawTexturedModalRect(84, 145, 213, 9, 35, 14);
            drawCenteredString(fontRendererObj, StatCollector.translateToLocal("gui.modysseyteleporters.add"), 102, 169, 0xFFFFFF);
            quantity.drawTextBox();
        }

        exchangeButton.setEnabled(containerTeleporterController.getCurrentMarket().getCartSize() != 0);
        exchangeButton.drawButton(mouseX - x - 9, mouseY - y - 25);
        drawCenteredString(fontRendererObj, StatCollector.translateToLocal(containerTeleporterController.getCurrentMarket().getMarketTitle()), 152, 169, 0xFFFFFF);

        drawTabLabels();

        fontRendererObj.drawString(StatCollector.translateToLocal(containerTeleporterController.getCurrentMarket().getStockTitle()), -2, 17, 0x404040, false);
        fontRendererObj.drawString(StatCollector.translateToLocal("gui.modysseyteleporters.cart"), 125, 17, 0x404040, false);

        String totalField = StatCollector.translateToLocal("gui.modysseyteleporters.total") + ":";
        fontRendererObj.drawString(totalField, 124, 142, 0x404040, false);

        int total = containerTeleporterController.getCartTotal();
        String totalLine = "$" + Integer.toString(total);
        fontRendererObj.drawString(totalLine, 155 - fontRendererObj.getStringWidth(totalLine) / 2, 152, 0x404040, false);

        int credits = controller.getCredits();
        String creditLine = StatCollector.translateToLocal("gui.modysseyteleporters.credits") + ": $" + Integer.toString(credits);

        fontRendererObj.drawString(creditLine, 112, 3, 0xFFFFFF, true);

        if (selectedItem != null) {
            drawInfoPane(selectedItem);

            if (addButton.pollClickEvent()) {
                String amount = quantity.getText();

                int intAmount = 1;
                try {
                    intAmount = Integer.parseInt(amount);
                } catch (NumberFormatException ex) {
                    //Looks like some garbage made it into the amount field (or maybe it was blank?)
                    //just use 1
                }

                RequestCartAddPacket packet = new RequestCartAddPacket(containerTeleporterController.windowId, containerTeleporterController.getMarketIndex(), new ItemStack(selectedItem.getItem().getItem(), intAmount, selectedItem.getItem().getItemDamage()));
                ModysseyNetwork.sendToServer(packet);
            }
        }
    }

    private void drawTabLabels() {
        int titleY = 36;
        for (int i = 0; i < containerTeleporterController.getMarketCount(); i++) {
            String title = StatCollector.translateToLocal(containerTeleporterController.getMarketTitle(i));

            fontRendererObj.drawString(title, -52, titleY, 0xFFFFFF, true);

            titleY += 28;
        }
    }

    private void drawInfoPane(StockItem item) {
        ItemStack itemStack = item.getItem();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPushMatrix();
        GL11.glTranslatef(-3, 148, 0);
        GL11.glScalef(2.0f, 2.0f, 1.0f);
        itemRender.renderItemIntoGUI(fontRendererObj, Minecraft.getMinecraft().getTextureManager(), itemStack, 0, 0, true);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);

        fontRendererObj.drawSplitString(itemStack.getDisplayName(), 31, 147, 49, 0xFFFFFF);
        fontRendererObj.drawString("$" + Integer.toString(item.getValue()), 31, 174, 0xFFFFFF, false);
    }

    @Override
    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        boolean doDefaultKeyBehavior = true;
        if (containerTeleporterController.getCurrentMarket().allowAddFromStock() && selectedItem != null) {
            doDefaultKeyBehavior = !quantity.textboxKeyTyped(par1, par2);
        }

        if (doDefaultKeyBehavior)
            super.keyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int par3) {
        super.mouseClicked(mouseX, mouseY, par3);

        double w = 195;
        double h = 216;

        double x = (width - w) / 2;
        double y = (height - h) / 2;

        if (containerTeleporterController.getCurrentMarket().allowAddFromStock()) {
            quantity.mouseClicked(mouseX - (int)x - 9, mouseY - (int)y - 25, par3);
        }

        if (par3 == 0) {
            int leftTabBound = (int)(x - 49);
            int topTabBound = (int)(y + 50);
            int rightTabBound = (int)x;
            int bottomTabBound = (int)(y + 50 + (containerTeleporterController.getMarketCount() * 28));

            if (mouseX >= leftTabBound && mouseX <= rightTabBound && mouseY >= topTabBound && mouseY <= bottomTabBound) {
                int selectedMarket = (mouseY - 1 - topTabBound)/((bottomTabBound - topTabBound)/containerTeleporterController.getMarketCount());

                if (selectedMarket != containerTeleporterController.getMarketIndex()) {
                    containerTeleporterController.setMarketIndex(selectedMarket);
                    categories.setStockList(containerTeleporterController.getCurrentMarket().getStockList());
                    categories.clearSelectedCategory();
                    stockItems.clearSelectedItem();
                    stockItems.setStockCategory(null);
                    selectedItem = null;
                    cart.setMarket(containerTeleporterController.getCurrentMarket());

                    if (!containerTeleporterController.getCurrentMarket().allowAddFromStock())
                        quantity.setFocused(false);
                }

                return;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        double w = 207;
        double h = 216;
        double x = (width - w) / 2;
        double y = (height - h) / 2;

        double f = 0.00390625;
        double f1 = 0.00390625;
        mc.renderEngine.bindTexture(new ResourceLocation("modysseyteleporters:textures/gui/station.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + 53, (double)this.zLevel+18, 0, 0.20703125);
        tessellator.addVertexWithUV(x + w, y + 53, (double)this.zLevel+18, w * f, 0.20703125);
        tessellator.addVertexWithUV(x + w, y, (double)this.zLevel+18, w * f, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel+18, 0, 0);

        tessellator.addVertexWithUV(x, y + 164, (double)this.zLevel, 0, 0.640625);
        tessellator.addVertexWithUV(x + w, y + 164, (double)this.zLevel, w * f, 0.640625);
        tessellator.addVertexWithUV(x + w, y + 53, (double)this.zLevel, w * f, 0.20703125);
        tessellator.addVertexWithUV(x, y + 53, (double)this.zLevel, 0, 0.20703125);

        tessellator.addVertexWithUV(x, y + 216, (double)this.zLevel+18, 0, 0.84375);
        tessellator.addVertexWithUV(x + w, y + 216, (double)this.zLevel+18, w * f, 0.84375);
        tessellator.addVertexWithUV(x + w, y + 164, (double)this.zLevel+18, w * f, 0.640625);
        tessellator.addVertexWithUV(x, y + 164, (double)this.zLevel+18, 0, 0.640625);

        double physicalY = 50;
        for (int i = 0; i < containerTeleporterController.getMarketCount(); i++) {
            if (i == containerTeleporterController.getMarketIndex()) {
                tessellator.addVertexWithUV(x - 49, y + physicalY + 28, (double) this.zLevel+18, 0 * f, 244 * f1);
                tessellator.addVertexWithUV(x+4, y + physicalY + 28, (double) this.zLevel+18, 53 * f, 244 * f1);
                tessellator.addVertexWithUV(x+4, y + physicalY, (double) this.zLevel+18, 53 * f, 216 * f1);
                tessellator.addVertexWithUV(x - 49, y + physicalY, (double) this.zLevel+18, 0 * f, 216 * f1);
            } else {
                tessellator.addVertexWithUV(x - 49, y + physicalY + 28, (double) this.zLevel, 53 * f, 244 * f1);
                tessellator.addVertexWithUV(x, y + physicalY + 28, (double) this.zLevel, 102 * f, 244 * f1);
                tessellator.addVertexWithUV(x, y + physicalY, (double) this.zLevel, 102 * f, 216 * f1);
                tessellator.addVertexWithUV(x - 49, y + physicalY, (double) this.zLevel, 53 * f, 216 * f1);
            }

            physicalY += 28;
        }

        tessellator.draw();

        mc.renderEngine.bindTexture(containerTeleporterController.getCurrentMarket().getMarketLogo());
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + 35, (double)this.zLevel, 0, 1);
        tessellator.addVertexWithUV(x + 109, y + 35, (double)this.zLevel, 1, 1);
        tessellator.addVertexWithUV(x + 109, y, (double)this.zLevel, 1, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel, 0, 0);
        tessellator.draw();
    }
}
