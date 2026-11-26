package net.little.ais.client.gui.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.little.ais.ai.math.Matrix;
import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.little.ais.client.gui.widget.CanvasWidget;
import net.little.ais.network.redstonesecurity.RedstoneSecurityModulePayload;
import net.little.ais.network.redstonesecurity.RedstoneSecurityModulePredictionPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.UUID;

public class RedstoneSecurityModuleScreen extends Screen {

    BlockPos pos;
    UUID owner;
    boolean isOwner;
    FittingMultiLineTextWidget prediction;
    FittingMultiLineTextWidget test;
    CanvasWidget canvas;

    private static final int TEXT_COLOR = Color.WHITE.getRGB();

    public RedstoneSecurityModuleScreen(Component title, RedstoneSecurityBlockEntity entity, UUID player) {
        super(title);
        this.pos = entity.getBlockPos();
        this.owner = entity.getOwner();
        this.isOwner = entity.isOwnedBy(player);
    }

    @Override
    protected void init() {
        super.init();

        int W = (int) (this.width*0.7);
        int H = (int) (this.height*0.7);
        int S = Math.min(W,H);

        int x = this.width/2-S/2;
        int y = this.height/2-S/2-20;

        this.canvas = new CanvasWidget(x, y, S, S, Component.literal("canvas"));
        this.addRenderableWidget(this.canvas);

        int startX = this.canvas.getX();
        int startY = this.canvas.getY()+this.canvas.getHeight();

        // Tool     Test    Prediction
        // Reset    Send    Config

        int topWidgetWidth = S/3;
        int topWidgetHeight = 20;

        Button tool = Button.builder(Component.literal("eraser"), this::switchTool).bounds(
                startX,startY,
                topWidgetWidth,topWidgetHeight
        ).tooltip(Tooltip.create(Component.literal("Change Tool"))).build();

        Button test = Button.builder(Component.literal("test"), _ -> {
            Matrix read = this.canvas.read();
            ClientPlayNetworking.send(new RedstoneSecurityModulePredictionPayload(
                    this.pos,
                    read.values(),
                    true
            ));
        }).bounds(
                startX+topWidgetWidth,startY,
                topWidgetWidth,topWidgetHeight
        ).tooltip(
               Tooltip.create(Component.literal("Sending drawing to check what digit is recognized"))
        ).build();

        this.prediction = new FittingMultiLineTextWidget(
                startX+2*topWidgetWidth,startY,
                topWidgetWidth,topWidgetHeight,
                Component.literal(""),
                Minecraft.getInstance().font
        );
        this.prediction.setTooltip(Tooltip.create(Component.literal("The number last recognized by your drawings")));
        this.prediction.setSize(topWidgetWidth,topWidgetHeight);

        startY+=topWidgetHeight;

        int bottomWidgetWidth = S/3;
        int bottomWidgetHeight = 20;

        Button resetButton = Button.builder(
                Component.literal("reset"),
                _ -> this.canvas.reset()
        ).tooltip(
                Tooltip.create(Component.literal("Reset Canvas"))
        ).bounds(startX,startY,bottomWidgetWidth,bottomWidgetHeight).build();

        Button sendButton = Button.builder(
                Component.literal("send"),
                this::send
        ).tooltip(
                Tooltip.create(Component.literal("Sending drawing for verification"))
        ).bounds(startX+bottomWidgetWidth,startY,bottomWidgetWidth,bottomWidgetHeight).build();

        if (this.isOwner) {
            Button config = Button.builder(
                    Component.literal("config"),
                    this::config
            ).tooltip(
                    Tooltip.create(Component.literal("Sending drawing to change the digit required for verification"))
            ).bounds(startX + 2 * bottomWidgetWidth, startY, bottomWidgetWidth, bottomWidgetHeight).build();
            this.addRenderableWidget(config);
        }

        this.addRenderableWidget(resetButton);
        this.addRenderableWidget(test);
        this.addRenderableWidget(tool);
        this.addRenderableWidget(sendButton);
        this.addRenderableWidget(this.prediction);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Component head = Component.literal("Security Module").withStyle(
                style -> style.withBold(true).withUnderlined(true)
        );
        graphics.text(this.font, head, this.width/2-this.font.width(head)/2, this.canvas.getY()-this.font.lineHeight-2, TEXT_COLOR);
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

    private void switchTool(Button b) {
        Component message = b.getMessage();
        if (message.toString().contains("brush")) {
            b.setMessage(Component.literal("eraser"));
            this.canvas.color = 255;
        }
        else {
            b.setMessage(Component.literal("brush"));
            this.canvas.color = -255;
        }
    }

    private void send(Button button) {
        Matrix read = this.canvas.read();
        ClientPlayNetworking.send(
                new RedstoneSecurityModulePredictionPayload(
                        this.pos,
                        read.values(),
                        false
                )
        );
    }

    private void config(Button button) {
        ClientPlayNetworking.send(
                new RedstoneSecurityModulePayload(
                        this.pos,
                        this.owner.toString(),
                        this.canvas.read().values()
                )
        );
    }

    public void showPrediction(int res) {
        this.prediction.setMessage(Component.literal(String.valueOf(res)));
    }

}
