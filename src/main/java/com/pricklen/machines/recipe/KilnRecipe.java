package com.pricklen.machines.recipe;

import com.google.gson.JsonObject;
import com.pricklen.machines.PMachines;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class KilnRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private int time;

    public KilnRecipe(NonNullList<Ingredient> inputItems, ItemStack output, ResourceLocation id, int time) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.time = time;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) return false;
        return inputItems.stream().anyMatch((i) -> i.test(pContainer.getItem(0)));
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return inputItems;
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }
    public int getTime() {
        return time;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
    public static class Type implements RecipeType<KilnRecipe> {
        public static final Type INSTANCE = new Type();
        public static final String ID = "kiln_smelting";
    }
    public static class Serializer implements RecipeSerializer<KilnRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public KilnRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            var output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
            var ingredients = GsonHelper.getAsJsonArray(pSerializedRecipe, "ingredients");
            var time = GsonHelper.getAsInt(pSerializedRecipe, "time");
            var inputs = NonNullList.withSize(ingredients.size(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++)
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            return new KilnRecipe(inputs, output, pRecipeId, time);
        }

        @Override
        public @Nullable KilnRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var time = pBuffer.readInt();
            var inputs = NonNullList.withSize(pBuffer.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++)
                inputs.set(i, Ingredient.fromNetwork(pBuffer));
            var output = pBuffer.readItem();
            return new KilnRecipe(inputs, output, pRecipeId, time);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, KilnRecipe pRecipe) {
            pBuffer.writeInt(pRecipe.getTime());
            pBuffer.writeInt(pRecipe.inputItems.size());

            for (var ingredient : pRecipe.getIngredients())
                ingredient.toNetwork(pBuffer);

            pBuffer.writeItemStack(pRecipe.getResultItem(null), false);
        }
    }
}
