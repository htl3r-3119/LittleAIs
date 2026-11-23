package net.little.ais.client.gui.screen.game;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GameLobbyScreen extends Screen {

    int rounds;
    List<String> players;
    List<Boolean> connected;

    private static final int TEXT_COLOR = Color.WHITE.getRGB();
    private static final int BACKGROUND_COLOR = new Color(0,0,0,0.6f).getRGB();

    private static final int buttonH = 20;
    private static final int buttonW = 120;
    private static final int gap = 30;
    private static final int gapHButton = 12;

    public GameLobbyScreen(int numRounds, List<String> playerNames) {
        super(Component.literal("game-lobby"));
        this.rounds = numRounds;
        this.players = playerNames;
        this.connected = new ArrayList<>(playerNames.size());
        this.connected.add(true);
        for (int i = 1; i < this.players.size(); i++) {
            this.connected.add(false);
        }
    }

    @Override
    protected void init() {
        super.init();
        Button button = Button.builder(
                Component.literal("Start Game"),
                _ -> {
                    System.out.println("SENDING");
                }
        ).bounds(
                this.width/2-buttonW/2,
                this.height*6/8+gapHButton,
                buttonW,
                buttonH
        ).build();
        this.addRenderableWidget(button);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(this.width/6, this.height/10, this.width*5/6, this.height*9/10, BACKGROUND_COLOR);
        Component head = Component.literal("Game Lobby").withStyle(
                style -> style.withUnderlined(true).withBold(true)
        );
        graphics.text(
                this.font,
                head,
                this.width/2-this.font.width(head)/2,
                this.height/10-this.font.lineHeight*3/2,
                Color.WHITE.getRGB()
        );
        super.extractRenderState(graphics, mouseX, mouseY, a);
        int max = this.players.stream().mapToInt(this.font::width).max().orElse(0);
        int startX = this.width/2 - max - gap/2;
        int startY = this.height/8;
        int yoff = this.height*4/6 / this.players.size();
        for (int i = 0; i < this.players.size(); i++) {
            String name = this.players.get(i);
            boolean isconnected = this.connected.get(i);
            String connected = !isconnected ? "❌" : "✔";
            graphics.text(this.font, name, startX, startY + i*yoff, TEXT_COLOR);
            graphics.text(this.font, Component.literal(connected).withStyle(
                    style -> style.withBold(true)
            ), this.width/2+max+gap/2, startY + i*yoff, !isconnected ? Color.RED.getRGB() : Color.GREEN.getRGB());
        }
    }
}
