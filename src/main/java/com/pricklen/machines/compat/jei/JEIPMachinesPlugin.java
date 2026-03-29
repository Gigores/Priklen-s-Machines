package com.pricklen.machines.compat.jei;

import com.pricklen.machines.PMachines;
import com.pricklen.machines.block.ModBlocks;
import com.pricklen.machines.recipe.KilnRecipe;
import com.pricklen.machines.screen.KilnScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class JEIPMachinesPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(PMachines.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new KilnCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var recipeManager = Minecraft.getInstance().level.getRecipeManager();
        var recipes = recipeManager.getAllRecipesFor(KilnRecipe.Type.INSTANCE);
        registration.addRecipes(KilnCategory.KILN_TYPE, recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(KilnScreen.class, 78, 33, 26, 19, KilnCategory.KILN_TYPE);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ItemStack kiln = new ItemStack(ModBlocks.KILN.get());
        registration.addRecipeCatalyst(kiln, KilnCategory.KILN_TYPE);
        registration.addRecipeCatalyst(kiln, mezz.jei.api.constants.RecipeTypes.SMELTING);
        registration.addRecipeCatalyst(kiln, mezz.jei.api.constants.RecipeTypes.BLASTING);
    }
}
