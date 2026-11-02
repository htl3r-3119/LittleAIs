package net.little.ais.block.entity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.little.ais.LittleAIs;
import net.little.ais.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

    public static final BlockEntityType<RedstoneFunctionConverterBlockEntity> REDSTONE_FUNCTION_CONVERTER_BLOCK_ENTITY_BLOCK_ENTITY_TYPE = register(
            "redstone_function_converter",
            RedstoneFunctionConverterBlockEntity::new,
            ModBlocks.REDSTONE_FUNCTION_CONVERTER_BLOCK
    );

    public static void initialize() {
        LittleAIs.LOGGER.info("Initializing BlockEntities");
    }

    private static <T extends BlockEntity> BlockEntityType<T> register(
            String name,
            FabricBlockEntityTypeBuilder.Factory<? extends T> entityFactory,
            Block... blocks
    ) {
        Identifier id = LittleAIs.id(name);
        return Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id, FabricBlockEntityTypeBuilder.<T>create(entityFactory, blocks).build());
    }

}
