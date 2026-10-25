package net.little.ais.itemgroups;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.little.ais.LittleAIs;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ModItemGroups {

    public static ResourceKey<CreativeModeTab> CREATIVE_TAB_KEY = create("little-ais");

    public static ResourceKey<CreativeModeTab> create(String name) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, LittleAIs.id(name));
    }

    public static final CreativeModeTab CREATIVE_TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(Items.REDSTONE))
            .title(Component.translatable("creativeTab.little.ais"))
            .displayItems((parameters, output) -> {
                output.accept(Items.COMPARATOR);
                output.accept(Items.REPEATER);
            })
            .build();

    public static void initialize() {
        LittleAIs.LOGGER.info("Initializing Item Groups");

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CREATIVE_TAB_KEY, CREATIVE_TAB);
    }

}
