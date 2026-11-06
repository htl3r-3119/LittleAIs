package net.little.ais.client.gui.screen;

import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.little.ais.client.gui.widget.CanvasWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
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

        CanvasWidget canvas = new CanvasWidget(x, y, S, S, Component.literal("test"));
        this.addRenderableWidget(canvas);

        int bw = 120;
        int bh = 20;
        int bx = canvas.getX()+canvas.getWidth()/2-bw/2;
        int by = canvas.getY()+canvas.getHeight()+1;

        Button testButton = Button.builder(Component.literal("test"), button -> {
            canvas.test();
        }).bounds(bx,by,bw,bh).build();

        this.addRenderableWidget(testButton);

    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

}
