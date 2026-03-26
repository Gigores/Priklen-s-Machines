package com.pricklen.machines.block.entity;

import com.pricklen.machines.PMachines;
import com.pricklen.machines.block.ModBlocks;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PMachines.MODID);

    public static final RegistryObject<BlockEntityType<KilnControllerBlockEntity>> KILN_CONTROLLER =
            BLOCK_ENTITIES.register("kiln_controlle_be", () ->
                    BlockEntityType.Builder.of(KilnControllerBlockEntity::new,
                            ModBlocks.KILN.get()).build(null));

    public static final RegistryObject<BlockEntityType<KilnHatchBlockEntity>> KILN_HATCH =
            BLOCK_ENTITIES.register("kiln_hatch_be", () ->
                    BlockEntityType.Builder.of(KilnHatchBlockEntity::new,
                            ModBlocks.KILN_HATCH.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
