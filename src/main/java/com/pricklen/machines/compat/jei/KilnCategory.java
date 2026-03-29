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
import net.minecraft.client.Minecraft;
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
    private final IGuiHelper helper;

    private static final int UI_X_OFFSET = 55;
    private static final int UI_Y_OFFSET = 16;
    private static final int UI_WIDTH = 82;
    private static final int UI_HEIGHT = 54;

    public KilnCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, UI_X_OFFSET, UI_Y_OFFSET, UI_WIDTH, UI_HEIGHT);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.KILN.get()));

        this.flame = helper.createAnimatedDrawable(
                helper.createDrawable(TEXTURE, 176, 0, 14, 14),
                300,
                IDrawableAnimated.StartDirection.TOP,
                true
        );

        this.arrow = helper.createAnimatedDrawable(
                helper.createDrawable(TEXTURE, 176, 14, 24, 17),
                100,
                IDrawableAnimated.StartDirection.LEFT,
                false
        );
        this.helper = helper;
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
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(kilnRecipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 19).addItemStack(kilnRecipe.getResultItem(null));
    }

    @Override
    public void draw(KilnRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        flame.draw(guiGraphics, 1, 20);
        arrow.draw(guiGraphics, 24, 18);
        var timeString = Component.translatable("gui.jei.category.smelting.time.seconds", recipe.getTime() / 20);
        var textWidth = Minecraft.getInstance().font.width(timeString);
        var textHeight = Minecraft.getInstance().font.lineHeight;
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                timeString,
                UI_WIDTH - textWidth, UI_HEIGHT - textHeight,
                0xFF808080,
                false
        );
    }
}
