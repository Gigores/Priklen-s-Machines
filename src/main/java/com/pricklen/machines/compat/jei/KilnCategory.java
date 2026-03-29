package com.pricklen.machines.compat.jei;

import com.pricklen.machines.PMachines;
import com.pricklen.machines.block.ModBlocks;
import com.pricklen.machines.recipe.KilnRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class KilnCategory implements IRecipeCategory<KilnRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(PMachines.MODID, "kiln_smelting");
    public static final ResourceLocation TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/furnace.png");
    public static final RecipeType<KilnRecipe> KILN_TYPE =
            new RecipeType<>(UID, KilnRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated flame;
    private final IDrawableAnimated arrow;

    public KilnCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 52, 13, 88, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.KILN.get()));

        this.flame = helper.createAnimatedDrawable(
                helper.createDrawable(TEXTURE, 176, 0, 14, 14),
                200,
                IDrawableAnimated.StartDirection.TOP,
                true
        );

        this.arrow = helper.createAnimatedDrawable(
                helper.createDrawable(TEXTURE, 176, 14, 24, 17),
                200,
                IDrawableAnimated.StartDirection.LEFT,
                false
        );
    }

    @Override
    public RecipeType<KilnRecipe> getRecipeType() {
        return KILN_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.category.kiln");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, KilnRecipe kilnRecipe, IFocusGroup iFocusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 4).addIngredients(kilnRecipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 22).addItemStack(kilnRecipe.getResultItem(null));
    }

    @Override
    public void draw(KilnRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        flame.draw(guiGraphics, 5, 24);
        arrow.draw(guiGraphics, 27, 22);
    }
}
