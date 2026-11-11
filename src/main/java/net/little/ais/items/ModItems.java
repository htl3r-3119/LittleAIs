package net.little.ais.items;

import net.little.ais.LittleAIs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class ModItems {

    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, LittleAIs.id(name));
    }

    public static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties settings) {
        Item item = factory.apply(settings.setId(key));

        Registry.register(BuiltInRegistries.ITEM, key, item);

        return item;
    }

    public static void initialize() {
        LittleAIs.LOGGER.info("Initializing Items");
    }

}
