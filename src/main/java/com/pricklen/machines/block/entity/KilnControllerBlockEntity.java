package com.pricklen.machines.block.entity;

import com.mojang.logging.LogUtils;
import com.pricklen.machines.block.HatchMode;
import com.pricklen.machines.block.KilnHatchBlock;
import com.pricklen.machines.block.ModBlocks;
import com.pricklen.machines.item.ModItems;
import com.pricklen.machines.screen.KilnMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class KilnControllerBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2);
    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    public KilnControllerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.KILN_CONTROLLER.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> KilnControllerBlockEntity.this.progress;
                    case 1 -> KilnControllerBlockEntity.this.maxProgress;
                    default -> 9;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> KilnControllerBlockEntity.this.progress = pValue;
                    case 1 -> KilnControllerBlockEntity.this.maxProgress = pValue;
                };
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.pricklensmachines.kiln_controller");
    }
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new KilnMenu(pContainerId, pPlayerInventory, this, this.data);
    }
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("kiln_controller.progress", progress);
        super.saveAdditional(pTag);
    }
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("kiln_controller.progress");
    }

    public void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
        var structure = checkStructure(pPos);

        if (structure.isValid()) {
            if (!hasInputItem())
                pullFromHatches(structure.inputHatches());
            if (hasOutputItem())
                pushToHatches(structure.outputHatches());
        }
//        var logger = LogUtils.getLogger();
//
//        logger.debug("INPUT:");
//        for (var pos : structure.inputHatches())
//            logger.debug("    " + pos.toString());
//
//        logger.debug("OUTPUT:");
//        for (var pos : structure.outputHatches())
//            logger.debug("    " + pos.toString());

        if(hasRecipe() && structure.isValid()) {
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if(hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }
    public void pullFromHatches(List<BlockPos> inputHatches) {
        if (inputHatches.isEmpty()) return;
        var fromBE = level.getBlockEntity(inputHatches.get(0));
        if (fromBE == null) return;
        LazyOptional<IItemHandler> fromCap =
                fromBE.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
        LazyOptional<IItemHandler> toCap =
                this.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

        if (fromCap.isPresent() && toCap.isPresent()) {
            IItemHandler from = fromCap.orElseThrow(RuntimeException::new);
            IItemHandler to = toCap.orElseThrow(RuntimeException::new);

            for (int i = 0; i < from.getSlots(); i++) {
                ItemStack extracted = from.extractItem(i, 1, true);
                if (!extracted.isEmpty()) {
                    ItemStack remainder = ItemHandlerHelper.insertItem(to, extracted, true);
                    if (remainder.isEmpty()) {
                        ItemStack realExtract = from.extractItem(i, 1, false);
                        ItemHandlerHelper.insertItem(to, realExtract, false);
                        return;
                    }
                }
            }
        }
    }
    public void pushToHatches(List<BlockPos> outputHatches) {
        if (outputHatches.isEmpty() || level == null) return;

        BlockEntity toBE = level.getBlockEntity(outputHatches.get(0));
        if (toBE == null) return;

        LazyOptional<IItemHandler> fromCap =
                this.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

        LazyOptional<IItemHandler> toCap =
                toBE.getCapability(ForgeCapabilities.ITEM_HANDLER, null);

        if (fromCap.isPresent() && toCap.isPresent()) {

            IItemHandler from = fromCap.orElseThrow(RuntimeException::new);
            IItemHandler to = toCap.orElseThrow(RuntimeException::new);

            int slot = OUTPUT_SLOT;
            if (slot >= from.getSlots()) return;

            ItemStack stack = from.getStackInSlot(slot);
            if (stack.isEmpty()) return;

            ItemStack toInsert = stack.copy();
            toInsert.setCount(1);
            ItemStack remainder = ItemHandlerHelper.insertItem(to, toInsert, true);

            if (remainder.isEmpty()) {

                ItemStack extracted = from.extractItem(slot, 1, false);
                ItemHandlerHelper.insertItem(to, extracted, false);
            }
        }
    }

    public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
        if(hasRecipe() && checkStructure(pPos).isValid()) {
            BlockPos smokePos = calculateRelativeBlockPos(pPos, 0, 0, 1);
            pLevel.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, smokePos.getX() + .5f, smokePos.getY() + 1, smokePos.getZ() + .5f, 0, .1, 0);
        }
    }

    public Direction getFacing() {
        if (level == null) return Direction.NORTH;
        BlockState state = level.getBlockState(worldPosition);
        return state.getValue(HorizontalDirectionalBlock.FACING);
    }

    private BlockPos calculateRelativeBlockPos(BlockPos pos, int dX, int dY, int dZ) {
        /*
          +
        - B + [x-]
          -
         [z|]
         */
        return switch (getFacing()) {
            case NORTH -> new BlockPos(pos.getX() - dX, pos.getY() + dY, pos.getZ() + dZ);
            case SOUTH -> new BlockPos(pos.getX() + dX, pos.getY() + dY, pos.getZ() - dZ);
            case EAST -> new BlockPos(pos.getX() - dZ, pos.getY() + dY, pos.getZ() - dX);
            case WEST -> new BlockPos(pos.getX() + dZ, pos.getY() + dY, pos.getZ() + dX);
            default -> pos;
        };
    }

    private StructureStatus checkStructure(BlockPos pos) {
        var inputHatches = new ArrayList<BlockPos>();
        var outputHatches = new ArrayList<BlockPos>();
        for (StructurePart part : STRUCTURE) {
            BlockPos target = calculateRelativeBlockPos(pos, part.x, part.y, part.z);
            var blockState = level.getBlockState(target);
            if (!blockState.is(part.block)) {
                if (blockState.is(HATCH_BLOCK)) {
                    var mode = blockState.getValue(KilnHatchBlock.MODE);
                    switch (mode) {
                        case INPUT -> inputHatches.add(target);
                        case OUTPUT -> outputHatches.add(target);
                    }
                    continue;
                }
                return new StructureStatus(false, new ArrayList<>(), new ArrayList<>());
            }
        }
        return new StructureStatus(true, inputHatches, outputHatches);
    }
//    private boolean check(Level level, BlockPos origin, int dx, int dy, int dz, Block expected) {
//        BlockPos checkPos = calculateRelativeBlockPos(origin, dx, dy, dz);
//        return level.getBlockState(checkPos).is(expected);
//    }

    private void resetProgress() {
        progress = 0;
    }

    private void craftItem() {
        ItemStack result = new ItemStack(ModItems.FIRECLAY_BRICK.get(), 1);
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasInputItem() {
        return !this.itemHandler.getStackInSlot(INPUT_SLOT).isEmpty();
    }
    private boolean hasOutputItem() {
        return !this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty();
    }
    private boolean hasRecipe() {
        boolean hasCraftingItem = this.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == ModItems.FIRECLAY.get();
        ItemStack result = new ItemStack(ModItems.FIRECLAY_BRICK.get());

        return hasCraftingItem && canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    private record StructurePart(int x, int y, int z, Block block) {}

    private static final Block HATCH_BLOCK = ModBlocks.KILN_HATCH.get();
    private static final StructurePart[] STRUCTURE = new StructurePart[] {
            /*
            ...
            WBW
            B B
            WBW
            ---
            WBW
            B B
            WBW
            ---
            WBW
            B B
            WBW
            ---
            BBB
            BBB
            BMB
            ---
            B - ModBlocks.FIRECLAY_BRICKS
            W - ModBlocks.FIRECLAY_BRICK_WALL
            M - this
            */
            new StructurePart(-1, 0, 2, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(0, 0, 2, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 0, 2, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(-1, 0, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(0, 0, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 0, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(-1, 0, 0, ModBlocks.FIRECLAY_BRICKS.get()),
//            new StructurePart(0, 0, 0, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 0, 0, ModBlocks.FIRECLAY_BRICKS.get()),

            new StructurePart(-1, 1, 2, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(0, 1, 2, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 1, 2, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(-1, 1, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 1, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(-1, 1, 0, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(0, 1, 0, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 1, 0, ModBlocks.FIRECLAY_BRICK_WALL.get()),

            new StructurePart(-1, 2, 2, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(0, 2, 2, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 2, 2, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(-1, 2, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 2, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(-1, 2, 0, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(0, 2, 0, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 2, 0, ModBlocks.FIRECLAY_BRICK_WALL.get()),

            new StructurePart(-1, 3, 2, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(0, 3, 2, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 3, 2, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(-1, 3, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 3, 1, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(-1, 3, 0, ModBlocks.FIRECLAY_BRICK_WALL.get()),
            new StructurePart(0, 3, 0, ModBlocks.FIRECLAY_BRICKS.get()),
            new StructurePart(1, 3, 0, ModBlocks.FIRECLAY_BRICK_WALL.get()),
    };
    private record StructureStatus(boolean isValid, List<BlockPos> inputHatches, List<BlockPos> outputHatches) {}
}
