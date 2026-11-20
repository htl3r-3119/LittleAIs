package net.little.ais.client.gui.screen.game;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class GameLobbyScreen extends Screen {

    int rounds;
    List<String> players;

    public GameLobbyScreen(int numRounds, List<String> playerNames) {
        super(Component.literal("game-lobby"));
        this.rounds = numRounds;
        this.players = playerNames;
    }



}
