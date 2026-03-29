package com.pricklen.machines.compat.jade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.Accessor;
import snownee.jade.api.view.*;

import java.util.List;

public enum KilnExtensionProvider implements IServerExtensionProvider<BlockEntity, ItemStack>,
        IClientExtensionProvider<ItemStack, ItemView> {

    INSTANCE;

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("pmachines", "kiln_ext");
    }

    @Override
    public List<ViewGroup<ItemStack>> getGroups(ServerPlayer player, ServerLevel level, BlockEntity target, boolean showDetails) {
        return List.of();
    }

    @Override
    public List<ClientViewGroup<ItemView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<ItemStack>> groups) {
        return List.of();
    }
}
