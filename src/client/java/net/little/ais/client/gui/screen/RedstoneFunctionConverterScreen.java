package net.little.ais.client.gui.screen;

import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.little.ais.client.gui.widget.ScrollableListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class RedstoneFunctionConverterScreen extends Screen {

    RedstoneFunctionConverterBlockEntity entity;

    public RedstoneFunctionConverterScreen(Component title, RedstoneFunctionConverterBlockEntity eR) {
        super(title);
        this.entity = eR;
    }

    @Override
    protected void init() {
        super.init();
        int WIDTH = (int) (this.width*0.8);
        int HEIGHT = (int) (this.height*0.8);
        FittingMultiLineTextWidget multiLineTextWidget = new FittingMultiLineTextWidget(
                this.width/2-WIDTH/2,this.height/2-HEIGHT/2,
                WIDTH/2,HEIGHT,
                Component.literal("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."),
                Minecraft.getInstance().font
        );
        this.addRenderableWidget(multiLineTextWidget);
        ScrollableListWidget test = new ScrollableListWidget(
                this.width / 2, this.height/2-HEIGHT/2,
                WIDTH / 2, HEIGHT,
                120,20,
                Component.literal("TEST"),
                this.entity.getData()
        );
        this.addRenderableWidget(test);
        /*
        int startX = 40;
        int startY = 20;

        for (Map.Entry<DataSet.Input, Integer> entry : this.entity.getData()) {
            DataSet.Input key = entry.getKey();
            int i = entry.getValue();
            DataMapEntryWidget widget = new DataMapEntryWidget(startX, startY += 20, 120, 20, Component.literal("TEEST"), key.a(), key.b(), key.c(), i);
            this.addRenderableWidget(widget);
        }
        for (Map.Entry<DataSet.Input, Integer> entry : this.entity.getData()) {
            DataSet.Input key = entry.getKey();
            int i = entry.getValue();
            DataMapEntryWidget widget = new DataMapEntryWidget(startX, startY += 20, 120, 20, Component.literal("TEEST"), key.a(), key.b(), key.c(), i);
            this.addRenderableWidget(widget);
        }
        for (Map.Entry<DataSet.Input, Integer> entry : this.entity.getData()) {
            DataSet.Input key = entry.getKey();
            int i = entry.getValue();
            DataMapEntryWidget widget = new DataMapEntryWidget(startX, startY += 20, 120, 20, Component.literal("TEEST"), key.a(), key.b(), key.c(), i);
            this.addRenderableWidget(widget);
        }
        // this.addRenderableWidget(new DataMapEntryWidget(startX, startY + 20, 120, 20, Component.literal("teeest"), 15,15,15,15));

        */
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }



}
