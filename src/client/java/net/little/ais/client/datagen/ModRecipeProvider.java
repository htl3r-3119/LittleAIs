package net.little.ais.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.little.ais.block.ModBlocks;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);
                shaped(RecipeCategory.REDSTONE, ModBlocks.REDSTONE_FUNCTION_CONVERTER_BLOCK.asItem(),1)
                        .pattern("SRS")
                        .pattern("TCT")
                        .pattern("STS")
                        .define('S', () -> Items.STONE)
                        .define('T', () -> Items.REDSTONE_TORCH)
                        .define('C', () -> Items.COPPER_INGOT)
                        .define('R', () -> Items.REDSTONE)
                        .unlockedBy(getHasName(Items.REDSTONE_TORCH), has(Items.REDSTONE_TORCH))
                        .save(output);
                shaped(RecipeCategory.REDSTONE, ModBlocks.REDSTONE_SECURITY_MODULE_BLOCK.asItem(), 1)
                        .pattern("IPI")
                        .pattern("IOI")
                        .pattern("ICI")
                        .define('P', () -> Items.PAPER)
                        .define('I', () -> Items.IRON_INGOT)
                        .define('O', () -> Items.OBSERVER)
                        .define('C', () -> Items.COPPER_INGOT)
                        .unlockedBy(getHasName(Items.OBSERVER), has(Items.OBSERVER))
                        .save(output);
            }
        };
    }

    @Override
    public String getName() {
        return "RecipeProvider";
    }

}
