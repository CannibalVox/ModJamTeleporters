package net.modyssey.teleporters.client.gui;

import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.modyssey.teleporters.client.gui.components.Button;
import net.modyssey.teleporters.client.gui.components.NumberOnlyTextField;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockList;
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

    public GuiTeleporterController(TileEntityTeleporterController controller, IMarketFactory[] marketFactories) {
        super(new ContainerTeleporterController(controller, marketFactories));

        this.controller = controller;
        this.containerTeleporterController = (ContainerTeleporterController)inventorySlots;
    }

    public void updateMarketData(List<StockList> marketData) {
        containerTeleporterController.updateMarketData(marketData);

        categories.setStockList(containerTeleporterController.getCurrentMarket().getStockList());
        stockItems.setStockCategory(containerTeleporterController.getCurrentMarket().getStockList().getCategory(0));
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
        stockItems.setStockCategory(containerTeleporterController.getCurrentMarket().getStockList().getCategory(0));

        cart = new GuiCartList(this, fontRendererObj);
        cart.setMarket(containerTeleporterController.getCurrentMarket());

        addButton = new Button(new ResourceLocation("modysseyteleporters:textures/gui/station.png"), new Rectangle2D.Double(91, 162, 35, 22), new Rectangle2D.Double(195, 168, 35, 22), new Rectangle2D.Double(195, 190, 35, 22),
                new Rectangle2D.Double(195, 212, 35, 22), new Rectangle2D.Double(195, 234, 35, 22));

        exchangeButton = new Button(new ResourceLocation("modysseyteleporters:textures/gui/station.png"), new Rectangle2D.Double(130, 162, 49, 22), new Rectangle2D.Double(195, 80, 49, 22), new Rectangle2D.Double(195, 102, 49, 22),
                new Rectangle2D.Double(195, 124, 49, 22), new Rectangle2D.Double(195, 146, 49, 2));

        quantity = new NumberOnlyTextField(fontRendererObj, 93, 148, 31, 14);
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
            addButton.drawButton(mouseX - x - 9, mouseY - y - 25);
            drawTexturedModalRect(91, 145, 213, 9, 35, 14);
            drawCenteredString(fontRendererObj, StatCollector.translateToLocal("gui.modysseyteleporters.add"), 109, 169, 0xFFFFFF);
            quantity.drawTextBox();
        }

        exchangeButton.drawButton(mouseX - x - 9, mouseY - y - 25);
        drawCenteredString(fontRendererObj, StatCollector.translateToLocal(containerTeleporterController.getCurrentMarket().getMarketTitle()), 155, 169, 0xFFFFFF);

        drawTabLabels();

        fontRendererObj.drawString(StatCollector.translateToLocal(containerTeleporterController.getCurrentMarket().getStockTitle()), -2, 17, 0x404040, false);
        fontRendererObj.drawString(StatCollector.translateToLocal("gui.modysseyteleporters.cart"), 125, 17, 0x404040, false);

        String totalField = StatCollector.translateToLocal("gui.modysseyteleporters.total") + ":";
        fontRendererObj.drawString(totalField, 132, 142, 0x404040, false);

        int total = containerTeleporterController.getCartTotal();
        String totalLine = "$" + Integer.toString(total);
        fontRendererObj.drawString(totalLine, 155 - fontRendererObj.getStringWidth(totalLine) / 2, 152, 0x404040, false);

        int credits = controller.getCredits();
        String creditLine = StatCollector.translateToLocal("gui.modysseyteleporters.credits") + ": $" + Integer.toString(credits);

        fontRendererObj.drawString(creditLine, 112, 3, 0xFFFFFF, true);
    }

    private void drawTabLabels() {
        int titleY = 36;
        for (int i = 0; i < containerTeleporterController.getMarketCount(); i++) {
            String title = StatCollector.translateToLocal(containerTeleporterController.getMarketTitle(i));

            fontRendererObj.drawString(title, -52, titleY, 0xFFFFFF, true);

            titleY += 28;
        }
    }

    @Override
    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        boolean doDefaultKeyBehavior = true;
        if (containerTeleporterController.getCurrentMarket().allowAddFromStock()) {
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
                    stockItems.setStockCategory(containerTeleporterController.getCurrentMarket().getStockList().getCategory(0));
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
        tessellator.addVertexWithUV(x, y + 53, (double)this.zLevel+2, 0, 0.20703125);
        tessellator.addVertexWithUV(x + w, y + 53, (double)this.zLevel+2, w * f, 0.20703125);
        tessellator.addVertexWithUV(x + w, y, (double)this.zLevel+2, w * f, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel+2, 0, 0);

        tessellator.addVertexWithUV(x, y + 164, (double)this.zLevel, 0, 0.640625);
        tessellator.addVertexWithUV(x + w, y + 164, (double)this.zLevel, w * f, 0.640625);
        tessellator.addVertexWithUV(x + w, y + 53, (double)this.zLevel, w * f, 0.20703125);
        tessellator.addVertexWithUV(x, y + 53, (double)this.zLevel, 0, 0.20703125);

        tessellator.addVertexWithUV(x, y + 216, (double)this.zLevel+2, 0, 0.84375);
        tessellator.addVertexWithUV(x + w, y + 216, (double)this.zLevel+2, w * f, 0.84375);
        tessellator.addVertexWithUV(x + w, y + 164, (double)this.zLevel+2, w * f, 0.640625);
        tessellator.addVertexWithUV(x, y + 164, (double)this.zLevel+2, 0, 0.640625);

        double physicalY = 50;
        for (int i = 0; i < containerTeleporterController.getMarketCount(); i++) {
            if (i == containerTeleporterController.getMarketIndex()) {
                tessellator.addVertexWithUV(x - 49, y + physicalY + 28, (double) this.zLevel+2, 0 * f, 244 * f1);
                tessellator.addVertexWithUV(x+4, y + physicalY + 28, (double) this.zLevel+2, 53 * f, 244 * f1);
                tessellator.addVertexWithUV(x+4, y + physicalY, (double) this.zLevel+2, 53 * f, 216 * f1);
                tessellator.addVertexWithUV(x - 49, y + physicalY, (double) this.zLevel+2, 0 * f, 216 * f1);
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
