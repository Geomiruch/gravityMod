package com.example.examplemod.blocks;

import com.example.examplemod.entity.FallingPileBlockEntity;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PileBlock extends FallingDamagingBlock {
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 1, 8);
    protected static final VoxelShape[] SHAPES = new VoxelShape[] { VoxelShapes.empty(), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };

    public PileBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(LAYERS, 1));
    }

    @Override
    public boolean isPathfindable(BlockState pState, IBlockReader pLevel, BlockPos pPos, PathType pType) {
        if (pType == PathType.LAND) {
            return pState.getValue(LAYERS) < 5;
        }
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState pState, IBlockReader pLevel, BlockPos pPos, ISelectionContext pContext) {
        return SHAPES[pState.getValue(LAYERS)];
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState pState) {
        return true;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, EntityType<?> entityType) {
        return false;
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, IWorld pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext pContext) {
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if (blockstate.getBlock() == this) {
            int i = blockstate.getValue(LAYERS);
            return blockstate.setValue(LAYERS, Math.min(8, i + 1));
        }
        return super.getStateForPlacement(pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LAYERS);
    }

    @Override
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        ItemStack itemStack = pPlayer.inventory.getSelected();

        if (!itemStack.isEmpty()) {
            if (itemStack.getItem() == this.asItem()) {
                if (pState.getValue(LAYERS) < 8) {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LAYERS, pState.getValue(LAYERS) + 1));
                    if (!pPlayer.isCreative()) {
                        itemStack.shrink(1);

                        if (itemStack.isEmpty()) {
                            pPlayer.inventory.setItem(pPlayer.inventory.selected, ItemStack.EMPTY);
                        } else {
                            pPlayer.inventory.setItem(pPlayer.inventory.selected, itemStack);
                        }
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void tick(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRand) {
        if (pLevel.isEmptyBlock(pPos.below()) || isFree(pLevel.getBlockState(pPos.below())) && pPos.getY() >= 0) {
            FallingPileBlockEntity fallingPileBlockEntity = new FallingPileBlockEntity(pLevel, (double)pPos.getX() + 0.5D, (double)pPos.getY(), (double)pPos.getZ() + 0.5D, pLevel.getBlockState(pPos));
            this.falling(fallingPileBlockEntity);
            pLevel.addFreshEntity(fallingPileBlockEntity);
        }
        pLevel.getBlockTicks().scheduleTick(pPos, this, this.getDelayAfterPlace());
        updateTickSlidingPile(pState, pLevel, pPos, pRand);
    }

    @Override
    public void neighborChanged(BlockState pState, World pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        super.neighborChanged(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
        BlockState aboveState = pLevel.getBlockState(pPos.above());
        if(aboveState.getBlock() instanceof PileBlock && pState.getBlock() instanceof PileBlock) {
            int aboveLayers = aboveState.getValue(LAYERS);
            int currentLayers = pState.getValue(LAYERS);
            if(currentLayers < 8 && aboveLayers > 0) {
                if(currentLayers + aboveLayers <= 8) {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LAYERS, currentLayers + aboveLayers));
                    pLevel.removeBlock(pPos.above(), false);
                } else {
                    pLevel.setBlockAndUpdate(pPos, pState.setValue(LAYERS, 8));
                    pLevel.setBlockAndUpdate(pPos.above(), aboveState.setValue(LAYERS, currentLayers + aboveLayers - 8));
                }
            }
        }
    }

    public static boolean canFallHere(ServerWorld pLevel, BlockPos pPos, BlockState pCurState) {
        BlockState blockState = pLevel.getBlockState(pPos);
        if(blockState.getBlock() instanceof PileBlock) {
            int fallLayers = blockState.getValue(LAYERS);
            int currentLayers = pCurState.getValue(LAYERS);
            return fallLayers < 8 && fallLayers + currentLayers <= 8;
        }
        return blockState.canBeReplaced(new DirectionalPlaceContext(pLevel, pPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
    }

    public static boolean canFallBelow(ServerWorld pLevel, BlockPos pPos, BlockState pCurState) {
        return canFallHere(pLevel, pPos.below(), pCurState);
    }

    public static void updateTickSlidingPile(BlockState pState, ServerWorld pLevel, BlockPos pPos, Random pRandom) {
        if (!canFallHere(pLevel, pPos.below(), pState)) {
            List<Direction> avaliableDirections = new ArrayList<>();

            for(Direction dir : Direction.Plane.HORIZONTAL) {
                if(canFallHere(pLevel, pPos.relative(dir), pState) && canFallBelow(pLevel, pPos.relative(dir), pState)) {
                    avaliableDirections.add(dir);
                }
            }

            if (avaliableDirections.size() > 0) {
                Direction fallDirection = avaliableDirections.get(pRandom.nextInt(avaliableDirections.size()));

                pLevel.removeBlock(pPos, false);

                BlockState replaceFallBlockState = pLevel.getBlockState(pPos.relative(fallDirection));
                if(replaceFallBlockState.getBlock() instanceof PileBlock) {
                    int replaceLayers = replaceFallBlockState.getValue(LAYERS);
                    int currentLayers = pState.getValue(LAYERS);
                    pLevel.setBlockAndUpdate(pPos.relative(fallDirection), replaceFallBlockState.setValue(LAYERS, replaceLayers + currentLayers));
                } else {
                    pLevel.setBlockAndUpdate(pPos.relative(fallDirection), pState);
                }


            }

        }
    }
}
