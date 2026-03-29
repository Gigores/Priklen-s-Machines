package com.pricklen.machines.datagen;

import com.pricklen.machines.ModTags;
import com.pricklen.machines.PMachines;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, PMachines.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
//        tag(ModTags.Items.OF_IRON)
//                .add(Items.IRON_PICKAXE)
//                .add(Items.IRON_SHOVEL)
//                .add(Items.IRON_AXE)
//                .add(Items.IRON_HOE)
//                .add(Items.IRON_SWORD)
//                .add(Items.IRON_HELMET)
//                .add(Items.IRON_CHESTPLATE)
//                .add(Items.IRON_LEGGINGS)
//                .add(Items.IRON_BOOTS)
//                .add(Items.IRON_HORSE_ARMOR)
//                .add(Items.CHAINMAIL_HELMET)
//                .add(Items.CHAINMAIL_CHESTPLATE)
//                .add(Items.CHAINMAIL_LEGGINGS)
//                .add(Items.CHAINMAIL_BOOTS);
//        tag(ModTags.Items.OF_GOLD)
//                .add(Items.GOLDEN_PICKAXE)
//                .add(Items.GOLDEN_SHOVEL)
//                .add(Items.GOLDEN_AXE)
//                .add(Items.GOLDEN_HOE)
//                .add(Items.GOLDEN_SWORD)
//                .add(Items.GOLDEN_HELMET)
//                .add(Items.GOLDEN_CHESTPLATE)
//                .add(Items.GOLDEN_LEGGINGS)
//                .add(Items.GOLDEN_BOOTS)
//                .add(Items.GOLDEN_HORSE_ARMOR);
    }
}
