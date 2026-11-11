package net.little.ais.client.gui.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.little.ais.client.gui.widget.CanvasWidget;
import net.little.ais.network.RedstoneSecurityModuleCheckPayload;
import net.little.ais.network.RedstoneSecurityModulePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RedstoneSecurityModuleScreen extends Screen {

    RedstoneSecurityBlockEntity en;
    UUID owner;
    FittingMultiLineTextWidget prediction;
    FittingMultiLineTextWidget test;
    CanvasWidget canvas;

    public RedstoneSecurityModuleScreen(Component title, RedstoneSecurityBlockEntity entity, UUID owner) {
        super(title);
        this.en = entity;
        this.owner = owner;
    }

    @Override
    protected void init() {
        super.init();

        int W = (int) (this.width*0.7);
        int H = (int) (this.height*0.7);
        int S = Math.min(W,H);

        int x = this.width/2-S/2;
        int y = this.height/2-S/2;

        this.canvas = new CanvasWidget(x, y, S, S, Component.literal("canvas"));
        this.addRenderableWidget(this.canvas);

        int startX = this.canvas.getX();
        int startY = this.canvas.getY()+this.canvas.getHeight();

        // Tool Test    Prediction
        // Send Config  /

        int topWidgetWidth = S/3;
        int topWidgetHeight = 20;

        Button tool = Button.builder(Component.literal("eraser"), this::switchTool).bounds(
                startX,startY,
                topWidgetWidth,topWidgetHeight
        ).build();

        Button test = Button.builder(Component.literal("test"), _ -> {
            int pred = this.canvas.pred();
            this.prediction.setMessage(Component.literal(String.valueOf(pred)));
        }).bounds(
                startX+topWidgetWidth,startY,
                topWidgetWidth,topWidgetHeight
        ).build();

        this.prediction = new FittingMultiLineTextWidget(
                startX+2*topWidgetWidth,startY,
                topWidgetWidth,topWidgetHeight,
                Component.literal(""),
                Minecraft.getInstance().font
        );
        this.prediction.setSize(topWidgetWidth,topWidgetHeight);

        startY+=topWidgetHeight;

        int bottomWidgetWidth = S/2;
        int bottomWidgetHeight = 20;

        Button sendButton = Button.builder(
                Component.literal("send"),
                this::send
        ).bounds(startX,startY,bottomWidgetWidth,bottomWidgetHeight).build();

        Button config = Button.builder(
                Component.literal("config"),
                this::config
        ).bounds(startX+bottomWidgetWidth,startY,bottomWidgetWidth,bottomWidgetHeight).build();

        this.addRenderableWidget(test);
        this.addRenderableWidget(tool);
        this.addRenderableWidget(sendButton);
        this.addRenderableWidget(config);
        this.addRenderableWidget(this.prediction);
    }

    private void switchTool(Button b) {
        Component message = b.getMessage();
        if (message.toString().contains("brush")) {
            b.setMessage(Component.literal("eraser"));
        }
        else b.setMessage(Component.literal("brush"));
        System.out.println(b.getMessage());
        this.canvas.color = this.canvas.color == 255 ? -255 : 255;
    }

    private void send(Button button) {
        int pred = this.canvas.pred();
        this.prediction.setMessage(Component.literal(String.valueOf(pred)));
        ClientPlayNetworking.send(
                new RedstoneSecurityModuleCheckPayload(
                        this.en.getBlockPos(),
                        pred
                )
        );
    }

    private void config(Button button) {
        ClientPlayNetworking.send(
                new RedstoneSecurityModulePayload(
                        this.en.getBlockPos(),
                        this.en.getOwner().toString(),
                        this.canvas.pred()
                )
        );
    }

}
