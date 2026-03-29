package com.pricklen.machines.compat;

import com.pricklen.machines.block.entity.KilnControllerBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.items.ItemStackHandler;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.ProgressArrowElement;

public enum KilnComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {

        var elements = IElementHelper.get();
        var data = accessor.getServerData();

        if (!data.contains("inventory")) return;

        var list = data.getList("inventory", Tag.TAG_COMPOUND);

        var input = ItemStack.EMPTY;
        var fuel = ItemStack.EMPTY;
        var output = ItemStack.EMPTY;

        for (Tag t : list) {
            var tag = (CompoundTag) t;
            var slot = tag.getByte("Slot");
            var stack = ItemStack.of(tag);

            switch (slot) {
                case 0 -> input = stack;
                case 1 -> fuel = stack;
                case 2 -> output = stack;
            }
        }

        var prog = data.getInt("prog");
        var max = data.getInt("mprog");

        var progress = max == 0 ? 0 : (float) prog / max;

        tooltip.add(elements.item(input));
        tooltip.append(elements.item(fuel));
        tooltip.append(new ProgressArrowElement(progress));
        tooltip.append(elements.item(output));
    }

    @Override
    public ResourceLocation getUid() {
        return JadePMachinesPlugin.KILN;
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        var kiln = (KilnControllerBlockEntity) accessor.getBlockEntity();
        var handler = kiln.getItemHandler();

        var list = new ListTag();

        for (int i = 0; i < handler.getSlots(); i++) {
            var stack = handler.getStackInSlot(i);

            if (!stack.isEmpty()) {
                var tag_ = new CompoundTag();
                tag_.putByte("Slot", (byte) i);
                stack.save(tag_);
                list.add(tag_);
            }
        }

        tag.put("inventory", list);
        tag.putInt("prog", kiln.getProgress());
        tag.putInt("mprog", kiln.getMaxProgress());
    }
}
