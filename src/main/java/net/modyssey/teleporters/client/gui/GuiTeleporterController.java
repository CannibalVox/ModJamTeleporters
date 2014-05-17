package net.modyssey.teleporters.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.modyssey.teleporters.markets.IMarketFactory;
import net.modyssey.teleporters.markets.stock.StockList;
import net.modyssey.teleporters.tileentities.TileEntityTeleporterController;
import net.modyssey.teleporters.tileentities.container.ContainerTeleporterController;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class GuiTeleporterController extends GuiContainer {
    private TileEntityTeleporterController controller;
    private ContainerTeleporterController containerTeleporterController;

    private GuiCategoryList categories;
    private GuiItemStockList stockItems;
    private GuiCartList cart;

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

        categories = new GuiCategoryList(this);
        categories.setStockList(containerTeleporterController.getCurrentMarket().getStockList());

        stockItems = new GuiItemStockList(this);
        stockItems.setStockCategory(containerTeleporterController.getCurrentMarket().getStockList().getCategory(0));

        cart = new GuiCartList(this);
        cart.setMarket(containerTeleporterController.getCurrentMarket());
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int w = 195;
        int h = 216;

        int x = (width - w) / 2;
        int y = (height - h) / 2;

        categories.drawList(mouseX - x, mouseY - y);
        stockItems.drawList(mouseX - x, mouseY - y);
        cart.drawList(mouseX - x, mouseY - y);
        drawTabLabels();

        fontRendererObj.drawString(StatCollector.translateToLocal(containerTeleporterController.getCurrentMarket().getStockTitle()), 2, 17, 0x404040, false);
        fontRendererObj.drawString(StatCollector.translateToLocal("gui.modysseyteleporters.cart"), 133, 17, 0x404040, false);

        int credits = controller.getCredits();
        String creditLine = StatCollector.translateToLocal("gui.modysseyteleporters.credits") + ": $" + Integer.toString(credits);

        fontRendererObj.drawString(creditLine, 105, 3, 0xFFFFFF, true);
    }

    private void drawTabLabels() {
        int titleY = 36;
        for (int i = 0; i < containerTeleporterController.getMarketCount(); i++) {
            String title = StatCollector.translateToLocal(containerTeleporterController.getMarketTitle(i));

            fontRendererObj.drawString(title, -52, titleY, 0xFFFFFF, true);

            titleY += 28;
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int par3) {
        super.mouseClicked(mouseX, mouseY, par3);

        double w = 195;
        double h = 216;

        double x = (width - w) / 2;
        double y = (height - h) / 2;

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
                }

                return;
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        double w = 195;
        double h = 216;
        double x = (width - w) / 2;
        double y = (height - h) / 2;

        double f = 0.00390625;
        double f1 = 0.00390625;
        mc.renderEngine.bindTexture(new ResourceLocation("modysseyteleporters:textures/gui/station.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, (double)this.zLevel, 0, 0.84375);
        tessellator.addVertexWithUV(x + w, y + h, (double)this.zLevel, w * f, 0.84375);
        tessellator.addVertexWithUV(x + w, y, (double)this.zLevel, w * f, 0);
        tessellator.addVertexWithUV(x, y, (double)this.zLevel, 0, 0);

        double physicalY = 50;
        for (int i = 0; i < containerTeleporterController.getMarketCount(); i++) {
            if (i == containerTeleporterController.getMarketIndex()) {
                tessellator.addVertexWithUV(x - 49, y + physicalY + 28, (double) this.zLevel, 195 * f, 52 * f1);
                tessellator.addVertexWithUV(x+4, y + physicalY + 28, (double) this.zLevel, 248 * f, 52 * f1);
                tessellator.addVertexWithUV(x+4, y + physicalY, (double) this.zLevel, 248 * f, 24 * f1);
                tessellator.addVertexWithUV(x - 49, y + physicalY, (double) this.zLevel, 195 * f, 24 * f1);
            } else {
                tessellator.addVertexWithUV(x - 49, y + physicalY + 28, (double) this.zLevel, 195 * f, 80 * f1);
                tessellator.addVertexWithUV(x, y + physicalY + 28, (double) this.zLevel, 244 * f, 80 * f1);
                tessellator.addVertexWithUV(x, y + physicalY, (double) this.zLevel, 244 * f, 52 * f1);
                tessellator.addVertexWithUV(x - 49, y + physicalY, (double) this.zLevel, 195 * f, 52 * f1);
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
