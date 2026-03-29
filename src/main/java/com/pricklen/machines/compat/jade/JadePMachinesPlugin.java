package com.pricklen.machines.compat.jade;

import com.pricklen.machines.PMachines;
import com.pricklen.machines.block.KilnControllerBlock;
import com.pricklen.machines.block.entity.KilnControllerBlockEntity;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;

@WailaPlugin
public class JadePMachinesPlugin implements IWailaPlugin {

    public static final ResourceLocation KILN =
            new ResourceLocation(PMachines.MODID, "kiln_cp");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerItemStorage(KilnExtensionProvider.INSTANCE, KilnControllerBlockEntity.class);
        registration.registerBlockDataProvider(KilnComponentProvider.INSTANCE, KilnControllerBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerItemStorageClient(KilnExtensionProvider.INSTANCE);
        registration.registerBlockComponent(KilnComponentProvider.INSTANCE, KilnControllerBlock.class);
    }
}
