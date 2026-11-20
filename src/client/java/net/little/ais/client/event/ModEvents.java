package net.little.ais.client.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.little.ais.block.ModBlocks;
import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.little.ais.client.gui.screen.RedstoneFunctionConverterScreen;
import net.little.ais.client.gui.screen.RedstoneSecurityModuleScreen;
import net.little.ais.client.gui.screen.game.GameSettingsScreen;
import net.little.ais.network.DrawingGameOpenSettingsPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class ModEvents {

    public static void initialize() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            ItemStack itemInHand = player.getItemInHand(hand);
            if (!level.isClientSide()) return InteractionResult.PASS;
            BlockState blockState = level.getBlockState(hitResult.getBlockPos());
            if (blockState.is(ModBlocks.REDSTONE_FUNCTION_CONVERTER_BLOCK)) {
                if (!(player.isShiftKeyDown() && itemInHand.isEmpty())) return InteractionResult.PASS;
                Minecraft.getInstance().gui.setScreen(
                        new RedstoneFunctionConverterScreen(Component.empty(), (RedstoneFunctionConverterBlockEntity) level.getBlockEntity(hitResult.getBlockPos()))
                );
                return InteractionResult.SUCCESS;
            }
            else if (blockState.is(ModBlocks.REDSTONE_SECURITY_MODULE_BLOCK)) {
                if (!itemInHand.isEmpty()) return InteractionResult.PASS;
                Minecraft.getInstance().gui.setScreen(
                        new RedstoneSecurityModuleScreen(
                                Component.empty(),
                                (RedstoneSecurityBlockEntity) level.getBlockEntity(hitResult.getBlockPos()),
                                player.getUUID()
                        )
                );
            }
            return InteractionResult.PASS;
        });

        ClientPlayNetworking.registerGlobalReceiver(
                DrawingGameOpenSettingsPayload.TYPE,
                (payload, context) -> {
                    Minecraft.getInstance().setScreenAndShow(new GameSettingsScreen());
                }
        );

    }

}
