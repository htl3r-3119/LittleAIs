package net.little.ais.client.gui.widget;

import net.little.ais.ai.model.DataSet;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ScrollableListWidget extends AbstractWidget {

    List<DataMapEntryWidget> children = new ArrayList<>();
    DataSet dataSet;
    int childW, childH;
    int scrollProgress = 0;

    DataMapEntryWidget focused;

    public ScrollableListWidget(int x, int y, int width, int height, int childW, int childH, Component message, DataSet dataSet) {
        super(x, y, width, height, message);
        this.dataSet = dataSet;
        this.childW = childW;
        this.childH = childH;
        int startY = y-childH;
        x = x + width/2-childW/2;
        for (Map.Entry<DataSet.Input, Integer> entry : dataSet) {
            DataSet.Input key = entry.getKey();
            int i = entry.getValue();
            DataMapEntryWidget widget = new DataMapEntryWidget(x, startY += childH, childW, childH, Component.literal("TEEST"), key.a(), key.b(), key.c(), i);
            this.children.add(widget);
        }
        for (Map.Entry<DataSet.Input, Integer> entry : dataSet) {
            DataSet.Input key = entry.getKey();
            int i = entry.getValue();
            DataMapEntryWidget widget = new DataMapEntryWidget(x, startY += childH, childW, childH, Component.literal("TEEST"), key.a(), key.b(), key.c(), i);
            this.children.add(widget);
        }
        for (Map.Entry<DataSet.Input, Integer> entry : dataSet) {
            DataSet.Input key = entry.getKey();
            int i = entry.getValue();
            DataMapEntryWidget widget = new DataMapEntryWidget(x, startY += childH, childW, childH, Component.literal("TEEST"), key.a(), key.b(), key.c(), i);
            this.children.add(widget);
        }
    }

    public int totalHeight() {
        return this.children.size() * this.childH;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(getX(), getY(), getX()+this.width, getY()+this.height, Color.RED.getRGB());

        int x = this.getX();
        int y = this.getY();
        graphics.enableScissor(x, y, x + this.getWidth(), y + this.getHeight());
        this.children.forEach(dataMapEntryWidget -> dataMapEntryWidget.extractWidgetRenderState(graphics, mouseX, mouseY, a));
        graphics.disableScissor();
        for (int idx = 0; idx < this.children.size(); idx++) {
            DataMapEntryWidget w = this.children.get(idx);
            w.setY(y + this.childH*idx-this.scrollProgress);
        }
    };

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double x = event.x();
        double y = event.y();
        DataMapEntryWidget w = get((int) x, (int) y);
        if (w == null) return super.mouseClicked(event,doubleClick);
        this.focused = w;
        return this.focused.mouseClicked(event,doubleClick) && super.mouseClicked(event, doubleClick);
    }

    public DataMapEntryWidget get(int x, int y) {
        return this.children.stream().filter(
                w -> w.getX() <= x &&
                        x <= w.getX()+w.getWidth() &&
                        w.getY() <= y &&
                        y <= w.getY()+w.getHeight()
        ).findAny().orElse(null);
    }

    @Override
    public void visitWidgets(Consumer<AbstractWidget> widgetVisitor) {
        for (var x : this.children) {
            widgetVisitor.accept(x);
        }
        super.visitWidgets(widgetVisitor);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.focused == null) return super.keyPressed(event);
        return this.focused.keyPressed(event);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        if (!canScroll()) return false;
        /*
        if (this.children.getLast().getY() <= this.getY()+this.getHeight()-this.childH) {
            return false;
        }*/
        this.scrollProgress = Math.max(0,Math.min(totalHeight()-this.height, this.scrollProgress - (int) scrollY));
        return true;
    }

    private boolean canScroll() {
        return this.totalHeight() > this.height;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

}
