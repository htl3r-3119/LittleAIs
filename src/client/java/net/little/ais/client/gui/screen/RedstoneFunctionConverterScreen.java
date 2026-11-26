package net.little.ais.client.gui.screen;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.little.ais.client.gui.widget.ScrollableListWidget;
import net.little.ais.network.redstonefunctionconverter.RedstoneFunctionConverterPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class RedstoneFunctionConverterScreen extends Screen {

    private static final int TEXT_COLOR = Color.WHITE.getRGB();
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
                Component.literal(
                        """
                                This handy redstone component lets you transform a function from up to three redstone signals into a function of your choosing. The entries on the right represent the three inputs and output. The values use the signal strength.
                                
                                This component uses the provided examples to estimate the required function. You don't have to type out every single scenario. Giving examples of some key values should be sufficient. Provide more details if necessary."""),
                Minecraft.getInstance().font
        );
        this.addRenderableWidget(multiLineTextWidget);
        ScrollableListWidget entries = new ScrollableListWidget(
                this.width / 2, this.height/2-HEIGHT/2,
                WIDTH / 2, HEIGHT,
                120,20,
                Component.literal("entries"),
                this.entity.getData()
        );
        this.addRenderableWidget(entries);

        int buttonSpace = 180;
        int buttonGap = 2;

        Button save = Button.builder(
                Component.literal("Save"),
                _ -> {
                    entries.save();
                    this.entity.setChanged();
                    Minecraft.getInstance().setScreenAndShow(null);
                    ClientPlayNetworking.send(new RedstoneFunctionConverterPayload(this.entity.getBlockPos(), this.entity.getData()));
                }
        ).bounds(
                this.width/2-buttonSpace/2, this.height/2+HEIGHT/2+buttonGap,
                buttonSpace/3,20
        ).build();
        Button add = Button.builder(
                Component.literal("Add"),
                _ -> entries.add(0,0,0,0)
        ).bounds(
                this.width/2-buttonSpace/2+buttonSpace/3, this.height/2+HEIGHT/2+buttonGap,
                60,20
        ).build();
        Button remove = Button.builder(
                Component.literal("Remove"),
                _ -> entries.remove()
        ).bounds(
                this.width/2-buttonSpace/2+2*buttonSpace/3, this.height/2+HEIGHT/2+buttonGap,
                60,20
        ).build();
        addRenderableWidget(save);
        addRenderableWidget(add);
        addRenderableWidget(remove);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        Component head = Component.literal("Signal Converter").withStyle(
                style -> style.withBold(true).withUnderlined(true)
        );
        graphics.text(this.font, head, this.width/2-this.font.width(head)/2, (int) (this.height/2.-this.height*0.4)-this.font.lineHeight-2, TEXT_COLOR);
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }



}
