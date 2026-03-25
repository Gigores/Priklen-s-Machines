package com.pricklen.machines.block;

import com.pricklen.machines.item.ModItems;
import com.pricklen.machines.PMachines;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PMachines.MODID);

    public static final RegistryObject<Block> FIRECLAY_BRICKS = registerBlock("fireclay_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))
    );
    public static final RegistryObject<Block> FIRECLAY_BRICK_STAIRS = registerBlock("fireclay_brick_stairs",
            () -> new StairBlock(() -> ModBlocks.FIRECLAY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))
    );
    public static final RegistryObject<Block> FIRECLAY_BRICK_SLAB = registerBlock("fireclay_brick_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))
    );
    public static final RegistryObject<Block> FIRECLAY_BRICK_WALL = registerBlock("fireclay_brick_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS))
    );
    public static final RegistryObject<Block> FIRECLAY_BLOCK = registerBlock("fireclay_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.CLAY))
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
