package net.little.ais.client.gui.widget;

import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.math.Matrix;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class CanvasWidget extends AbstractWidget {

    BatchMatrix pixel = new BatchMatrix(28,28,1);
    double pcx=0, pcy=0;

    public CanvasWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x = getX();
        int y = getY();
        double brushSize = this.width/28.;

        graphics.enableScissor(x,y,x+this.width,y+this.height);

        graphics.fill(x,y,x+this.width,y+this.height, Color.RED.getRGB());

        graphics.fill(mouseX, mouseY, (int) (mouseX+brushSize), (int) (mouseY+brushSize),Color.BLACK.getRGB());

        for (int i = 0; i < this.pixel.b(); i++) {
            for (int j = 0; j < this.pixel.r(); j++) {
                int color = (int)(255 - this.pixel.get(i, j, 0));
                double tempX = (x+i*brushSize);
                double tempY = (y+j*brushSize);
                graphics.fill((int) tempX, (int) tempY, (int) (tempX+brushSize), (int) (tempY+brushSize),
                        new Color(color, color, color).getRGB()
                );
            }
        }

        graphics.disableScissor();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        return super.keyPressed(event);
    }

    @Override
    protected void onDrag(MouseButtonEvent event, double dx, double dy) {
        this.pcx = Math.max(0,Math.min(this.width,event.x()-getX()));
        this.pcy = Math.max(0,Math.min(this.height,event.y()-getY()));
        double brushSize = this.width/28.;
        int i = (int) (this.pcx / brushSize);
        int j = (int) (this.pcy / brushSize);
        if (i < this.pixel.b() && j < this.pixel.r()) {
            this.drawLine(i,j,dx,dy);
        }
        super.onDrag(event, dx, dy);
    }

    private void drawLine(int x, int y, double dx, double dy) {
        this.pixel.set(x,y,0,255);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        return super.mouseDragged(event, dx, dy);
    }

}
