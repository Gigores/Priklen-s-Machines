package com.pricklen.machines.datagen;

import com.pricklen.machines.PMachines;
import com.pricklen.machines.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PMachines.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.FIRECLAY_BLOCK);
        blockWithItem(ModBlocks.FIRECLAY_BRICKS);

        stairsBlock(((StairBlock) ModBlocks.FIRECLAY_BRICK_STAIRS.get()), blockTexture(ModBlocks.FIRECLAY_BRICKS.get()));
        slabBlock(((SlabBlock) ModBlocks.FIRECLAY_BRICK_SLAB.get()), blockTexture(ModBlocks.FIRECLAY_BRICKS.get()), blockTexture(ModBlocks.FIRECLAY_BRICKS.get()));
        wallBlock(((WallBlock) ModBlocks.FIRECLAY_BRICK_WALL.get()), blockTexture(ModBlocks.FIRECLAY_BRICKS.get()));
    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
