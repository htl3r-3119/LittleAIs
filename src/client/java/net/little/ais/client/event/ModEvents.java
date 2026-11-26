package net.little.ais.client.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.little.ais.ai.model.StaticModel;
import net.little.ais.block.ModBlocks;
import net.little.ais.block.RedstoneFunctionConverterBlock;
import net.little.ais.block.RedstoneSecurityModuleBlock;
import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.little.ais.block.entity.RedstoneSecurityBlockEntity;
import net.little.ais.client.gui.screen.RedstoneFunctionConverterScreen;
import net.little.ais.client.gui.screen.RedstoneSecurityModuleScreen;
import net.little.ais.network.redstonesecurity.RedstoneSecurityModulePredictionResultPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
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
                Direction direction = hitResult.getDirection();
                Direction value = blockState.getValue(RedstoneSecurityModuleBlock.FACING);
                if (!itemInHand.isEmpty() || direction != value) return InteractionResult.PASS;
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
                RedstoneSecurityModulePredictionResultPayload.TYPE,
                (payload, context) -> {
                    Screen screen = Minecraft.getInstance().gui.screen();
                    if (screen instanceof RedstoneSecurityModuleScreen rsms) {
                        rsms.showPrediction(payload.res());
                    }
                }
        );

    }

}
