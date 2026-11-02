package net.little.ais.client.gui.widget;

import net.little.ais.ai.model.DataSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class DataMapEntryWidget extends AbstractWidget {

    int[] data = new int[4];
    int current = 0;

    public DataMapEntryWidget(int x, int y, int width, int height, Component message, int a, int b, int c, int d) {
        super(x, y, width, height, message);
        this.data[0] = a;
        this.data[1] = b;
        this.data[2] = c;
        this.data[3] = d;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int widthPerInp = this.width/5;
        int x = this.getX();
        int y = this.getY();
        int h = this.height;

        int bg = Color.BLACK.getRGB();
        int text = Color.WHITE.getRGB();
        int cursor = new Color(204,0,34).getRGB();

        Font font = Minecraft.getInstance().font;
        graphics.fill(x,y,x+=widthPerInp,y+h, bg);
        graphics.fill(x,y,x+=widthPerInp,y+h, bg);
        graphics.fill(x,y,x+=widthPerInp,y+h, bg);
        graphics.fill(x,y,x+=widthPerInp,y+h, bg);
        graphics.fill(x,y,x+widthPerInp,y+h, bg);

        x = this.getX();
        y = this.getY();

        int padding = 1;

        for (int i = 0; i < 3; i++) {
            graphics.text(font, Component.literal(Integer.toString(this.data[i])).withoutShadow(), x+widthPerInp/2-font.width(Integer.toString(this.data[i]))/2,y+h/2-font.lineHeight/2+padding, Color.WHITE.getRGB(), true);
            x += widthPerInp;
        }
        graphics.text(font, Component.literal("->").withoutShadow(), x+widthPerInp/2-font.width("->")/2,y+h/2-font.lineHeight/2, text, true);
        x += widthPerInp;
        graphics.text(font, Component.literal(Integer.toString(this.data[3])).withoutShadow(), x+widthPerInp/2-font.width(Integer.toString(this.data[3]))/2,y+h/2-font.lineHeight/2+padding, text, true);

        x = this.getX() + this.current*widthPerInp;
        int arrayIdx = Math.min(this.current, 3);
        x = x + widthPerInp/2 + font.width(Integer.toString(this.data[arrayIdx]))/2;
        y = y + h/2 - font.lineHeight/2;

        graphics.fill(x,y,x + font.width("_")/3,y+font.lineHeight, cursor);

        x = this.getX();
        y = this.getY();

        int pad = 1;
        int color = this.isFocused() ? Color.WHITE.getRGB() : Color.DARK_GRAY.getRGB();
        graphics.fill(x,y,x+this.width,y+pad,color);
        graphics.fill(x,y,x+pad,y+this.height,color);
        graphics.fill(x+this.width-pad,y,x+this.width,y+this.height,color);
        graphics.fill(x,y+this.height-pad,x+this.width,y+this.height,color);
    }

    private void updateCursor(int mouseX) {
        int relX = mouseX-this.getX();
        int n = (int) Math.floor(relX/(width/5.));
        if (n != 3) {
            this.current = n;
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        updateCursor((int) event.x());
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        int code = event.keycode();
        int arrIdx = Math.min(this.current,3);
        if (code == 1073741905) {
            this.data[arrIdx] = Math.max(this.data[arrIdx]-1,0);
            return true;
        }
        else if (code == 1073741906) {
            this.data[arrIdx] = Math.min(this.data[arrIdx]+1,15);
            return true;
        }
        else if (code == 1073741904 || (code == 9 && event.hasShiftDown())) {
            this.current = Math.max(0, this.current == 4 ? 2 : this.current-1);
            return true;
        }
        else if (code == 1073741903 || (code == 9 && !event.hasShiftDown())) {
            this.current = Math.min(4, this.current+1 == 3 ? 4 : this.current+1);
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

    public void save(DataSet dataSet) {
        dataSet.add(this.data[0], this.data[1], this.data[2], this.data[3]);
    }

}
