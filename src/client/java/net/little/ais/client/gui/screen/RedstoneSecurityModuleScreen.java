package net.little.ais.client.gui.screen;

import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.little.ais.client.gui.widget.CanvasWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class RedstoneSecurityModuleScreen extends Screen {

    RedstoneSecurityBlockEntity en;

    public RedstoneSecurityModuleScreen(Component title, RedstoneSecurityBlockEntity entity) {
        super(title);
        this.en = entity;
    }

    @Override
    protected void init() {
        super.init();

        int W = (int) (this.width*0.7);
        int H = (int) (this.height*0.7);
        int S = Math.min(W,H);

        int x = this.width/2-S/2;
        int y = this.height/2-S/2;

        CanvasWidget test = new CanvasWidget(x, y, S, S, Component.literal("test"));
        this.addRenderableWidget(test);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

}
