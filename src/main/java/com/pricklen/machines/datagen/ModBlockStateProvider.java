package com.pricklen.machines.datagen;

import com.pricklen.machines.PMachines;
import com.pricklen.machines.block.HatchMode;
import com.pricklen.machines.block.KilnHatchBlock;
import com.pricklen.machines.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
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

        horizontalBlock(ModBlocks.KILN.get(), new ModelFile.UncheckedModelFile(PMachines.MODID + ":block/kiln_unlit"));
        getVariantBuilder(ModBlocks.KILN_HATCH.get()).forAllStates(state -> {
            Direction facing = state.getValue(KilnHatchBlock.FACING);
            HatchMode mode = state.getValue(KilnHatchBlock.MODE);

            String modelName = (mode == HatchMode.INPUT)
                    ? "block/kiln_hatch_input"
                    : "block/kiln_hatch_output";

            int yRot = switch (facing) {
                case NORTH -> 0;
                case SOUTH -> 180;
                case WEST -> 270;
                case EAST -> 90;
                default -> 0;
            };

            return ConfiguredModel.builder()
                    .modelFile(new ModelFile.UncheckedModelFile(PMachines.MODID + ":" + modelName))
                    .rotationY(yRot)
                    .build();
        });
    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
