package com.pricklen.machines;

import com.pricklen.machines.block.ModBlocks;
import com.pricklen.machines.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PMachines.MODID);

    public static final RegistryObject<CreativeModeTab> MACHINES_TAB = CREATIVE_MODE_TABS.register("machines_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.FIRECLAY_BRICK.get()))
                    .title(Component.translatable("creativetab.machines_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.FIRECLAY.get());
                        pOutput.accept(ModBlocks.FIRECLAY_BLOCK.get());

                        pOutput.accept(ModItems.FIRECLAY_BRICK.get());
                        pOutput.accept(ModBlocks.FIRECLAY_BRICKS.get());
                        pOutput.accept(ModBlocks.FIRECLAY_BRICK_STAIRS.get());
                        pOutput.accept(ModBlocks.FIRECLAY_BRICK_WALL.get());
                        pOutput.accept(ModBlocks.FIRECLAY_BRICK_SLAB.get());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
