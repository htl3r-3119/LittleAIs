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
        for (Map.Entry<DataSet.Input, Integer> entry : dataSet) {
            DataSet.Input key = entry.getKey();
            int i = entry.getValue();
            add(key.a(), key.b(), key.c(), i);
        }
    }

    public int totalHeight() {
        return this.children.size() * this.childH;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(getX(), getY(), getX()+this.width, getY()+this.height, Color.BLACK.getRGB());

        int x = this.getX();
        int y = this.getY();
        graphics.enableScissor(x, y, x + this.getWidth(), y + this.getHeight());
        this.children.forEach(dataMapEntryWidget -> dataMapEntryWidget.extractWidgetRenderState(graphics, mouseX, mouseY, a));
        graphics.disableScissor();
        this.updateChildPositions();

        x = this.getX();
        y = this.getY();

        int pad = 1;
        int color = this.isFocused() ? Color.WHITE.getRGB() : Color.LIGHT_GRAY.getRGB();
        graphics.fill(x,y,x+this.width,y+pad,color);
        graphics.fill(x,y,x+pad,y+this.height,color);
        graphics.fill(x+this.width-pad,y,x+this.width,y+this.height,color);
        graphics.fill(x,y+this.height-pad,x+this.width,y+this.height,color);
    }

    private void updateChildPositions() {
        int y = getY();
        for (DataMapEntryWidget w : this.children) {
            w.setY(y - this.scrollProgress);
            y += childH;
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        double x = event.x();
        double y = event.y();
        DataMapEntryWidget w = get((int) x, (int) y);
        if (w == null) return super.mouseClicked(event,doubleClick);
        if (this.focused != null) this.focused.setFocused(false);
        this.focused = w;
        w.setFocused(true);
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

    public void remove() {
        if (this.focused == null) return;
        this.children.remove(this.focused);
        this.focused = null;
        this.scroll(-this.childH);
        this.updateChildPositions();
    }

    public void add(int a, int b, int c, int d) {
        int x=this.getX()+this.width/2-this.childW/2, y = 0; // this.children.isEmpty() ? 0 : this.children.getLast().getY()+this.childH;
        this.children.add(
                new DataMapEntryWidget(
                        x,y,childW,childH,
                        Component.literal("entry"),
                        a,b,c,d
                )
        );
        if (this.totalHeight() > this.height) this.scrollProgress = totalHeight()-this.height;
        this.updateChildPositions();
    }

    public void save() {
        this.dataSet.clear();
        this.children.forEach(dataMapEntryWidget -> dataMapEntryWidget.save(this.dataSet));
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

        if (Math.abs(scrollY) >= 1) {
            scrollY *= 5;
        }
        this.scroll((int) scrollY);
        return true;
    }

    private void scroll(int amount) {
        this.scrollProgress = Math.max(0,Math.min(totalHeight()-this.height, this.scrollProgress - amount));
    }

    private boolean canScroll() {
        return this.totalHeight() > this.height;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

}
