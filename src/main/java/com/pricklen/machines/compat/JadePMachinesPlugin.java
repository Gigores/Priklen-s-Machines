package com.pricklen.machines.compat;

import com.pricklen.machines.PMachines;
import com.pricklen.machines.block.KilnControllerBlock;
import com.pricklen.machines.block.ModBlocks;
import com.pricklen.machines.block.entity.KilnControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import snownee.jade.JadeClient;
import snownee.jade.api.*;

import java.util.List;

@WailaPlugin
public class JadePMachinesPlugin implements IWailaPlugin {

    public static final ResourceLocation KILN =
            new ResourceLocation(PMachines.MODID, "kiln_cp");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(KilnComponentProvider.INSTANCE, KilnControllerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addRayTraceCallback(-10, JadeClient::builtInOverrides);
        registration.addRayTraceCallback(-100, (hit, accessor, original) -> {
            if (accessor instanceof BlockAccessor block) {
                if (block.getBlock() instanceof KilnControllerBlock) {

                    return registration.blockAccessor()
                            .from(block)
                            .blockState(block.getBlock().defaultBlockState())
                            .build();
                }
            }
            return accessor;
        });
        registration.registerBlockComponent(KilnComponentProvider.INSTANCE, KilnControllerBlock.class);
    }
}
