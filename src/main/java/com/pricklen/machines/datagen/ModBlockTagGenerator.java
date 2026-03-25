package com.pricklen.machines.datagen;

import com.pricklen.machines.PMachines;
import com.pricklen.machines.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, PMachines.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(
                        ModBlocks.FIRECLAY_BRICKS.get(),
                        ModBlocks.FIRECLAY_BRICK_SLAB.get(),
                        ModBlocks.FIRECLAY_BRICK_STAIRS.get(),
                        ModBlocks.FIRECLAY_BRICK_WALL.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(
                        ModBlocks.FIRECLAY_BRICKS.get(),
                        ModBlocks.FIRECLAY_BRICK_SLAB.get(),
                        ModBlocks.FIRECLAY_BRICK_STAIRS.get(),
                        ModBlocks.FIRECLAY_BRICK_WALL.get()
                );

        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.FIRECLAY_BLOCK.get());

        this.tag(BlockTags.WALLS)
                .add(ModBlocks.FIRECLAY_BRICK_WALL.get());

        this.tag(BlockTags.SLABS)
                .add(ModBlocks.FIRECLAY_BRICK_SLAB.get());

        this.tag(BlockTags.STAIRS)
                .add(ModBlocks.FIRECLAY_BRICK_STAIRS.get());
    }
}
