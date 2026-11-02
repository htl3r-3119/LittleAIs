package net.little.ais.client.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.little.ais.block.ModBlocks;
import net.little.ais.block.entity.RedstoneFunctionConverterBlockEntity;
import net.little.ais.client.gui.screen.RedstoneFunctionConverterScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;

public class ModEvents {

    public static void initialize() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            ItemStack itemInHand = player.getItemInHand(hand);
            if (level.isClientSide() && player.isShiftKeyDown() && itemInHand.isEmpty() && level.getBlockState(hitResult.getBlockPos()).is(ModBlocks.REDSTONE_FUNCTION_CONVERTER_BLOCK)) {
                Minecraft.getInstance().gui.setScreen(
                        new RedstoneFunctionConverterScreen(Component.empty(), (RedstoneFunctionConverterBlockEntity) level.getBlockEntity(hitResult.getBlockPos()))
                );
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });
    }

}
