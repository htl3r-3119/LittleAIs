package net.little.ais.client.block.entity;

import net.little.ais.block.entity.ModBlockEntities;
import net.little.ais.client.block.entity.renderer.RedstoneFunctionConverterBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class ModBlockRendering {

    public static void initialize() {
        BlockEntityRenderers.register(
                ModBlockEntities.REDSTONE_FUNCTION_CONVERTER_BLOCK_ENTITY_BLOCK_ENTITY_TYPE,
                RedstoneFunctionConverterBlockEntityRenderer::new
        );
    }

}
