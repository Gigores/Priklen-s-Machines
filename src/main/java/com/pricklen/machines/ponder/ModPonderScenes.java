package com.pricklen.machines.ponder;

import com.pricklen.machines.block.ModBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;

public class ModPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(ModBlocks.KILN.getId()).addStoryBoard("kiln_controller", ModPonderScenes::kilnController);
    }
    private static void kilnController(SceneBuilder builder, SceneBuildingUtil util) {
        var scene = new PonderSceneBuilder(builder.getScene());
        scene.title("kiln", "Kiln");
        scene.idleSeconds(3);
        scene.markAsFinished();
    }
}
