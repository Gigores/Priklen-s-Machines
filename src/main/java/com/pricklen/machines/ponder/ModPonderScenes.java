package com.pricklen.machines.ponder;

import com.pricklen.machines.ModConfig_;
import com.pricklen.machines.block.HatchMode;
import com.pricklen.machines.block.KilnControllerBlock;
import com.pricklen.machines.block.KilnHatchBlock;
import com.pricklen.machines.block.ModBlocks;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class ModPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        helper.forComponents(ModBlocks.KILN.getId())
                .addStoryBoard("kiln_controller", ModPonderScenes::kilnController);
        helper.forComponents(ModBlocks.KILN_HATCH.getId())
                .addStoryBoard("kiln_controller", ModPonderScenes::kilnController);
    }
    private static void showLayer(PonderSceneBuilder scene, SceneBuildingUtil util, int y) {
        for (int x = 0; x < 3; x++)
            for(int z = 0; z < 3; z++) {
                scene.idle(2);
                scene.world().showSection(util.select().position(1 + x, y, 1 + z), Direction.DOWN);
            }
    }
    private static void kilnController(SceneBuilder builder, SceneBuildingUtil util) {
        final int TOTAL_LAYERS = 6;

        var scene = new PonderSceneBuilder(builder.getScene());
        scene.title("kiln", "Kiln");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.idle(10);

        var levelsRevealed = 0;

        var controller = new BlockPos(2, 1, 1);
        var inputHatch = new BlockPos(3, 1, 1);
        var outputHatch = new BlockPos(1, 2, 1);

        scene.world().showSection(util.select().position(controller), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(100)
            .placeNearTarget()
            .text("pricklensmachines.ponder.kiln.text_1")
            .pointAt(util.vector().topOf(controller));

        scene.idle(100);

        for (int x = 0; x < 3; x++)
            for(int z = 0; z < 3; z++)
                if (1 + x != controller.getX() || 1 + z != controller.getZ()) {
                    scene.idle(2);
                    scene.world().showSection(util.select().position(1 + x, 1, 1 + z), Direction.DOWN);
                }

        scene.idle(10);

        showLayer(scene, util, 2);
        scene.idle(10);

        scene.overlay().showOutlineWithText(util.select().cuboid(new BlockPos(1, 2, 1), new Vec3i(2, 0, 2)), 100)
                .text("pricklensmachines.ponder.kiln.text_2", ModConfig_.CONFIG.kilnMinLevels.get());
        scene.idle(100);

        if (ModConfig_.CONFIG.kilnMinLevels.get() > 1) {
            for (int i = 1; i < ModConfig_.CONFIG.kilnMinLevels.get() && i < TOTAL_LAYERS - 1; i++) {
                showLayer(scene, util, 2 + i);
                levelsRevealed++;
            }
            scene.idle(5);
        }

        scene.addKeyframe();

        scene.idle(5);
        scene.overlay().showControls(new Vec3(controller.getX() + .5f, controller.getY() + 1, controller.getZ()), Pointing.DOWN, 20).withItem(new ItemStack(Items.RAW_IRON));
        scene.idle(3);
        scene.world().modifyBlock(controller, blockState -> blockState.setValue(KilnControllerBlock.LIT, true), false);
        scene.idle(60);

        scene.world().modifyBlock(controller, blockState -> blockState.setValue(KilnControllerBlock.LIT, false), false);
        scene.overlay().showControls(new Vec3(controller.getX() + .5f, controller.getY(), controller.getZ()), Pointing.UP, 20).withItem(new ItemStack(Items.IRON_INGOT));
        scene.idle(40);

        scene.addKeyframe();

        scene.world().setBlock(inputHatch, ModBlocks.KILN_HATCH.get().defaultBlockState().setValue(KilnHatchBlock.FACING, Direction.NORTH).setValue(KilnHatchBlock.MODE, HatchMode.INPUT), true);
        scene.world().setBlock(outputHatch, ModBlocks.KILN_HATCH.get().defaultBlockState().setValue(KilnHatchBlock.FACING, Direction.WEST).setValue(KilnHatchBlock.MODE, HatchMode.INPUT), true);
        scene.idle(10);

        scene.overlay().showText(100)
                .text("pricklensmachines.ponder.kiln.text_3");
        scene.idle(100);

        scene.overlay().showControls(new Vec3(outputHatch.getX(), outputHatch.getY(), outputHatch.getZ() + .5f), Pointing.DOWN, 20).whileSneaking().rightClick();
        scene.world().setBlock(outputHatch, ModBlocks.KILN_HATCH.get().defaultBlockState().setValue(KilnHatchBlock.FACING, Direction.WEST).setValue(KilnHatchBlock.MODE, HatchMode.OUTPUT), false);
        scene.idle(30);

        scene.overlay().showControls(new Vec3(inputHatch.getX() + .5f, inputHatch.getY() + 1, inputHatch.getZ()), Pointing.DOWN, 20).withItem(new ItemStack(Items.RAW_IRON));
        scene.idle(3);
        scene.world().modifyBlock(controller, blockState -> blockState.setValue(KilnControllerBlock.LIT, true), false);
        scene.idle(60);

        scene.world().modifyBlock(controller, blockState -> blockState.setValue(KilnControllerBlock.LIT, false), false);
        scene.overlay().showControls(new Vec3(outputHatch.getX(), outputHatch.getY(), outputHatch.getZ() + .5f), Pointing.UP, 20).withItem(new ItemStack(Items.IRON_INGOT));
        scene.idle(40);

        scene.addKeyframe();

        for (int i = levelsRevealed + 1; i < ModConfig_.CONFIG.kilnMaxLevels.get() && i < TOTAL_LAYERS; i++)
            showLayer(scene, util, 2 + i);
        scene.overlay().showText(100)
                .text("pricklensmachines.ponder.kiln.text_4", ModConfig_.CONFIG.kilnMaxLevels.get());
        scene.idle(100);

        scene.overlay().showControls(new Vec3(inputHatch.getX() + .5f, inputHatch.getY() + 1, inputHatch.getZ()), Pointing.DOWN, 20).withItem(new ItemStack(Items.RAW_IRON));
        scene.idle(3);
        scene.world().modifyBlock(controller, blockState -> blockState.setValue(KilnControllerBlock.LIT, true), false);
        scene.idle(30);

        scene.world().modifyBlock(controller, blockState -> blockState.setValue(KilnControllerBlock.LIT, false), false);
        scene.overlay().showControls(new Vec3(outputHatch.getX(), outputHatch.getY(), outputHatch.getZ() + .5f), Pointing.UP, 20).withItem(new ItemStack(Items.IRON_INGOT));
        scene.idle(40);

        scene.overlay().showText(100)
                .text("pricklensmachines.ponder.kiln.text_5");

        scene.idle(20);
        scene.markAsFinished();
    }
}
