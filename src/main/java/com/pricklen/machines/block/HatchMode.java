package com.pricklen.machines.block;

import net.minecraft.util.StringRepresentable;

public enum HatchMode implements StringRepresentable {
    INPUT,
    OUTPUT;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
