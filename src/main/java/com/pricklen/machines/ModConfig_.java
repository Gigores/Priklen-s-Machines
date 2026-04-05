package com.pricklen.machines;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig_ {
    public static final ForgeConfigSpec SPEC;
    public static final ModConfig_ CONFIG;

    public final ForgeConfigSpec.BooleanValue kilnLoadFurnace;
    public final ForgeConfigSpec.BooleanValue kilnLoadBlastFurnace;
    public final ForgeConfigSpec.IntValue kilnMaxLevels;
    public final ForgeConfigSpec.IntValue kilnMinLevels;

    static {
        Pair<ModConfig_, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                .configure(ModConfig_::new);

        CONFIG = pair.getLeft();
        SPEC = pair.getRight();
    }
    public ModConfig_(ForgeConfigSpec.Builder builder) {
        builder.push("kiln");

        kilnLoadFurnace = builder.define("useFurnaceRecipes", true);
        kilnLoadBlastFurnace = builder.define("useBlastFurnaceRecipes", true);
        kilnMaxLevels = builder.defineInRange("maxLayers", 6, 1, Integer.MAX_VALUE);
        kilnMinLevels = builder.defineInRange("minLayers", 3, 1, Integer.MAX_VALUE);

        builder.pop();
    }
}
