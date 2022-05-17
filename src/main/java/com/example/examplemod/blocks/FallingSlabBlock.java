package com.example.examplemod.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class FallingSlabBlock extends Block implements IWaterLoggable
{
    public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape BOTTOM_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape TOP_AABB = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public FallingSlabBlock(AbstractBlock.Properties p_i48331_1_) {
        super(p_i48331_1_);
        this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    public boolean useShapeForLightOcclusion(BlockState p_220074_1_) {
        return p_220074_1_.getValue(TYPE) != SlabType.DOUBLE;
    }

    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(TYPE, WATERLOGGED);
    }

    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        SlabType slabtype = p_220053_1_.getValue(TYPE);
        switch(slabtype) {
            case DOUBLE:
                return VoxelShapes.block();
            case TOP:
                return TOP_AABB;
            default:
                return BOTTOM_AABB;
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext p_196258_1_) {
        BlockPos blockpos = p_196258_1_.getClickedPos();
        BlockState blockstate = p_196258_1_.getLevel().getBlockState(blockpos);
        if (blockstate.is(this)) {
            return blockstate.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, Boolean.valueOf(false));
        } else {
            FluidState fluidstate = p_196258_1_.getLevel().getFluidState(blockpos);
            BlockState blockstate1 = this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
            Direction direction = p_196258_1_.getClickedFace();
            return direction != Direction.DOWN && (direction == Direction.UP || !(p_196258_1_.getClickLocation().y - (double)blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.setValue(TYPE, SlabType.TOP);
        }
    }

    public boolean canBeReplaced(BlockState p_196253_1_, BlockItemUseContext p_196253_2_) {
        ItemStack itemstack = p_196253_2_.getItemInHand();
        SlabType slabtype = p_196253_1_.getValue(TYPE);
        if (slabtype != SlabType.DOUBLE && itemstack.getItem() == this.asItem()) {
            if (p_196253_2_.replacingClickedOnBlock()) {
                boolean flag = p_196253_2_.getClickLocation().y - (double)p_196253_2_.getClickedPos().getY() > 0.5D;
                Direction direction = p_196253_2_.getClickedFace();
                if (slabtype == SlabType.BOTTOM) {
                    return direction == Direction.UP || flag && direction.getAxis().isHorizontal();
                } else {
                    return direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal();
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public FluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
    }

    public boolean placeLiquid(IWorld p_204509_1_, BlockPos p_204509_2_, BlockState p_204509_3_, FluidState p_204509_4_) {
        return p_204509_3_.getValue(TYPE) != SlabType.DOUBLE ? IWaterLoggable.super.placeLiquid(p_204509_1_, p_204509_2_, p_204509_3_, p_204509_4_) : false;
    }

    public boolean canPlaceLiquid(IBlockReader p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_, Fluid p_204510_4_) {
        return p_204510_3_.getValue(TYPE) != SlabType.DOUBLE ? IWaterLoggable.super.canPlaceLiquid(p_204510_1_, p_204510_2_, p_204510_3_, p_204510_4_) : false;
    }

    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if (p_196271_1_.getValue(WATERLOGGED)) {
            p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
        }

        return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        switch(p_196266_4_) {
            case LAND:
                return false;
            case WATER:
                return p_196266_2_.getFluidState(p_196266_3_).is(FluidTags.WATER);
            case AIR:
                return false;
            default:
                return false;
        }
    }

    public void onPlace(BlockState p_220082_1_, World p_220082_2_, BlockPos p_220082_3_, BlockState p_220082_4_, boolean p_220082_5_) {
        p_220082_2_.getBlockTicks().scheduleTick(p_220082_3_, this, this.getDelayAfterPlace());
    }

    public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
        if (p_225534_2_.isEmptyBlock(p_225534_3_.below()) || isFree(p_225534_2_.getBlockState(p_225534_3_.below())) && p_225534_3_.getY() >= 0) {
            FallingBlockEntity fallingblockentity = new FallingBlockEntity(p_225534_2_, (double)p_225534_3_.getX() + 0.5D, (double)p_225534_3_.getY(), (double)p_225534_3_.getZ() + 0.5D, p_225534_2_.getBlockState(p_225534_3_));
            this.falling(fallingblockentity);
            p_225534_2_.addFreshEntity(fallingblockentity);
        }
    }

    protected void falling(FallingBlockEntity p_149829_1_) {
        p_149829_1_.setHurtsEntities(true);
    }

    protected int getDelayAfterPlace() {
        return 2;
    }

    public static boolean isFree(BlockState p_185759_0_) {
        Material material = p_185759_0_.getMaterial();
        return p_185759_0_.isAir() || p_185759_0_.is(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }

    public void onLand(World p_176502_1_, BlockPos p_176502_2_, BlockState p_176502_3_, BlockState p_176502_4_, FallingBlockEntity p_176502_5_) {
    }

    public void onBroken(World p_190974_1_, BlockPos p_190974_2_, FallingBlockEntity p_190974_3_) {
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        if (p_180655_4_.nextInt(16) == 0) {
            BlockPos blockpos = p_180655_3_.below();
            if (p_180655_2_.isEmptyBlock(blockpos) || isFree(p_180655_2_.getBlockState(blockpos))) {
                double d0 = (double)p_180655_3_.getX() + p_180655_4_.nextDouble();
                double d1 = (double)p_180655_3_.getY() - 0.05D;
                double d2 = (double)p_180655_3_.getZ() + p_180655_4_.nextDouble();
                p_180655_2_.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, p_180655_1_), d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @OnlyIn(Dist.CLIENT)
    public int getDustColor(BlockState p_189876_1_, IBlockReader p_189876_2_, BlockPos p_189876_3_) {
        return -16777216;
    }

}
