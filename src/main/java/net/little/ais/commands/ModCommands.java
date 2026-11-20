package net.little.ais.commands;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.little.ais.network.DrawingGameOpenSettingsPayload;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ModCommands {

    public static final Command<CommandSourceStack> DRAWING_GAME = context -> {
        context.getSource().sendSuccess(
                () -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player != null) ServerPlayNetworking.send(player, new DrawingGameOpenSettingsPayload());
                    return Component.literal("Called /littleais.");
                },
                false
        );
        return 1;
    };

    public static void initialize() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                    Commands.literal("littleais")
                    .executes(DRAWING_GAME)
            );
        });

    }

}
