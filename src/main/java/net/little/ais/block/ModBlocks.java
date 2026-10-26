package net.little.ais.block;

import net.little.ais.LittleAIs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;

public class ModBlocks {

    public static final Block REDSTONE_FUNCTION_CONVERTER_BLOCK = register(
            create("redstone_function_converter"),
            RedstoneFunctionConverterBlock::new,
            BlockBehaviour.Properties.of().isRedstoneConductor(Blocks::never)
    );

    public static void initialize() {
        LittleAIs.LOGGER.info("Initializing Blocks");
    }

    private static BlockItemId create(String name) {
        Identifier id = LittleAIs.id(name);
        return BlockItemId.create(id, id);
    }

    private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Block block = factory.apply(properties.setId(id));

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> factory, BlockBehaviour.Properties properties) {
        Block block = register(id.block(), factory, properties);

        BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);

        return block;
    }

}
