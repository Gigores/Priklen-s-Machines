package com.pricklen.machines.ponder;

import com.pricklen.machines.PMachines;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class PMachinesPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return PMachines.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        ModPonderScenes.register(helper);
    }
}
