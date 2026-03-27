package com.pricklen.machines.block;

import com.pricklen.machines.block.entity.KilnControllerBlockEntity;
import com.pricklen.machines.block.entity.KilnHatchBlockEntity;
import com.pricklen.machines.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class KilnHatchBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<HatchMode> MODE = EnumProperty.create("mode", HatchMode.class);

    protected KilnHatchBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(MODE, HatchMode.INPUT));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new KilnHatchBlockEntity(pos, state);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {

        if (!level.isClientSide && player.isCrouching()) {
            HatchMode mode = state.getValue(MODE);
            HatchMode newMode = (mode == HatchMode.INPUT) ? HatchMode.OUTPUT : HatchMode.INPUT;

//            System.out.println("Mode set to " + newMode);
            level.setBlock(pos, state.setValue(MODE, newMode), 3);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(pos);
            if(entity instanceof KilnHatchBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)player), (KilnHatchBlockEntity) entity, pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);

    }
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {

            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof KilnHatchBlockEntity hatch) {
                hatch.drops();
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
