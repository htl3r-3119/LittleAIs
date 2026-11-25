package net.little.ais.client.gui.widget;

import net.little.ais.ai.math.BatchMatrix;
import net.little.ais.ai.math.Matrix;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class CanvasWidget extends AbstractWidget {

    private static final int BRUSH_RADIUS = 1;
    private static final double alpha = 1;
    public int color = 255;

    BatchMatrix pixel = new BatchMatrix(28,28,1);

    boolean isDrawing;

    public CanvasWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int x = getX();
        int y = getY();
        double brushSize = getBrushSize();

        graphics.enableScissor(x,y,x+this.width,y+this.height);

        graphics.fill(mouseX, mouseY, (int) (mouseX+brushSize), (int) (mouseY+brushSize), Color.BLACK.getRGB());

        for (int i = 0; i < this.pixel.b(); i++) {
            for (int j = 0; j < this.pixel.r(); j++) {
                int color = (int)(255 - this.pixel.get(i, j, 0));
                double tempX = (x+j*brushSize);
                double tempY = (y+i*brushSize);
                graphics.fill((int) tempX, (int) tempY, (int) (tempX+brushSize), (int) (tempY+brushSize),
                        new Color(color, color, color).getRGB()
                );
            }
        }

        graphics.disableScissor();
    }

    private double getBrushSize() {
        return (double) this.width / this.pixel.b();
    }

    private int toCanvasPixel(double i) {
        return (int) (i / this.getBrushSize());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

    private int maxPixel() {
        return this.pixel.b()-1;
    }

    private int clip(int i) {
        return Math.max(0,Math.min(maxPixel(),i));
    }

    private void paintPixel(int x, int y, int color) {
        this.pixel.set(y,x,0, Math.min(255, Math.max(0,(color+this.pixel.get(y,x,0))*alpha + this.pixel.get(y,x,0)*(1-alpha))));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        int x = clip(toCanvasPixel(event.x()-getX()));
        int y = clip(toCanvasPixel(event.y()-getY()));

        drawBrush(x,y);

        if (!doubleClick) this.isDrawing = true;
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == 0) {
            this.isDrawing = false;
            return true;
        }
        return false;
    }


    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        double x = event.x()-getX();
        double y = event.y()-getY();
        double prevX = x-dx;
        double prevY = y-dy;
        drawLine(
                clip(toCanvasPixel(x)),
                clip(toCanvasPixel(y)),
                clip(toCanvasPixel(prevX)),
                clip(toCanvasPixel(prevY))
        );
        return true;
    }

    private void drawBrush(int centerX, int centerY) {
        int extent = BRUSH_RADIUS+1;

        for (int y = -extent; y <= extent; y++) {
            for (int x = -extent; x <= extent; x++) {
                if (y*y + x*x <= extent*extent) {
                    int nextX = centerX + x;
                    int nextY = centerY + y;
                    if (nextX > maxPixel() || nextX < 0 || nextY > maxPixel() || nextY < 0) continue;

                    double v = BRUSH_RADIUS + 0.5 - Math.sqrt(y * y + x * x);

                    double coverage = Math.clamp(v, 0., 1.);

                    if (coverage <= 0) continue;

                    int color = (int)(Math.round(this.color * coverage));

                    paintPixel(
                            nextX,
                            nextY,
                            color
                    );
                }
            }
        }
    }

    private void drawLine(int x, int y, int x1, int y1) {
        int dx = Math.abs(x1-x);
        int dy = Math.abs(y1-y);

        int maxX = this.pixel.r()-1;
        int maxY = this.pixel.b()-1;

        int sx = x < x1 ? 1 : -1;
        int sy = y < y1 ? 1 : -1;

        int error = dx - dy;

        while (true) {
            if (x > maxX || x < 0) break;
            if (y > maxY || y < 0) break;

            drawBrush(x,y);

            if (x == x1 && y == y1) {
                break;
            }

            int e2 = 2 * error;

            if (e2 > -dy) {
                error -= dy;
                x += sx;
            }

            if (e2 < dx) {
                error += dx;
                y += sy;
            }

        }
    }

    public Matrix read() {
        return this.pixel.clone().iterateSelf(x -> x / 255.);
    }

    public void reset() {
        this.pixel.iterateSelf(_ -> 0.);
    }

}
