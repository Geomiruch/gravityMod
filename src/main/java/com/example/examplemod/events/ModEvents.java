package com.example.examplemod.events;

import com.example.examplemod.GravityMod;
import com.example.examplemod.blocks.ModBlocks;
import com.example.examplemod.items.ModItems;
import com.example.examplemod.sounds.ModSounds;
import com.example.examplemod.util.ModTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GravityMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ModEvents {

    public static BlockPos getDirectionToSlide(BlockPos mainPos, World world)
    {
        if(world.getBlockState(mainPos.east()).isAir() && world.getBlockState(mainPos.east().below()).isAir())
        {
            return mainPos.east();
        }
        if(world.getBlockState(mainPos.west()).isAir() && world.getBlockState(mainPos.west().below()).isAir())
        {
            return mainPos.west();
        }
        if(world.getBlockState(mainPos.north()).isAir() && world.getBlockState(mainPos.north().below()).isAir())
        {
            return mainPos.north();
        }
        if(world.getBlockState(mainPos.south()).isAir() && world.getBlockState(mainPos.south().below()).isAir())
        {
            return mainPos.south();
        }
        return mainPos;
    }
    public static ArrayList<BlockPos> getDirections(BlockPos mainPos)
    {
        BlockPos leftPos = mainPos.east();
        BlockPos rightPos=mainPos.west();
        BlockPos towardPos=mainPos.north();
        BlockPos backwardPos=mainPos.south();
        BlockPos upPos=mainPos.above();
        BlockPos downPos=mainPos.below();

        ArrayList<BlockPos> blockPos = new ArrayList<BlockPos>();
        blockPos.add(leftPos);
        blockPos.add(rightPos);
        blockPos.add(towardPos);
        blockPos.add(backwardPos);
        blockPos.add(downPos);
        blockPos.add(upPos);

        return blockPos;
    }
    public static void Transformation(World world, BlockPos mainPos, int chance )
    {
        ArrayList<BlockPos> blockPos = getDirections(mainPos);
            for (BlockPos block: blockPos)
            {
                double ch = Math.random()*100;
                if (ch <= chance)
                {
                    if(world.getBlockState(block).getBlock()==Blocks.STONE.getBlock())
                    {
                        world.setBlockAndUpdate(block, ModBlocks.FALLING_COBBLE.get().defaultBlockState());
                        if(chance!=15)
                            world.playSound(null, mainPos, ModSounds.ROCK_CRUMBLING_1.get(), SoundCategory.AMBIENT, 0.25F,1);
                        if(chance==15)
                            world.playSound(null, mainPos, ModSounds.ROCK_CRUMBLING_2.get(), SoundCategory.AMBIENT, 0.25F,1);
                        Transformation(world, block, 15);
                    }
                    if(world.getBlockState(block).getBlock()==Blocks.ANDESITE.getBlock())
                    {
                        world.setBlockAndUpdate(block, ModBlocks.FALLING_ANDESITE_COBBLE.get().defaultBlockState());
                        if(chance!=15)
                            world.playSound(null, mainPos, ModSounds.ROCK_CRUMBLING_1.get(), SoundCategory.AMBIENT, 0.25F,1);
                        if(chance==15)
                            world.playSound(null, mainPos, ModSounds.ROCK_CRUMBLING_2.get(), SoundCategory.AMBIENT, 0.25F,1);
                        Transformation(world, block, 15);
                    }
                    if(world.getBlockState(block).getBlock()==Blocks.DIORITE.getBlock())
                    {
                        world.setBlockAndUpdate(block, ModBlocks.FALLING_DIORITE_COBBLE.get().defaultBlockState());
                        if(chance!=15)
                            world.playSound(null, mainPos, ModSounds.ROCK_CRUMBLING_1.get(), SoundCategory.AMBIENT, 0.25F,1);
                        if(chance==15)
                            world.playSound(null, mainPos, ModSounds.ROCK_CRUMBLING_2.get(), SoundCategory.AMBIENT, 0.25F,1);
                        Transformation(world, block, 15);
                    }
                    if(world.getBlockState(block).getBlock()==Blocks.GRANITE.getBlock())
                    {
                        world.setBlockAndUpdate(block, ModBlocks.FALLING_GRANITE_COBBLE.get().defaultBlockState());
                        if(chance!=15)
                            world.playSound(null, mainPos, ModSounds.ROCK_CRUMBLING_1.get(), SoundCategory.AMBIENT, 0.25F,1);
                        if(chance==15)
                            world.playSound(null, mainPos, ModSounds.ROCK_CRUMBLING_2.get(), SoundCategory.AMBIENT, 0.25F,1);
                        Transformation(world, block, 15);
                    }
                }
            }
    }

    /*@SubscribeEvent
    public static void onBlockTransformation(BlockEvent.NeighborNotifyEvent event)
    {
        BlockState state = event.getState();
        if(ModTags.Blocks.CAN_LANDSLIDE.contains(state.getBlock()))
        {
            World world = (World) event.getWorld();
            BlockPos newPos = getDirectionToSlide(event.getPos(), world);
            if(event.getPos()!=newPos)
            {
                world.setBlockAndUpdate(newPos, state);
                world.setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
            }
        }
    }*/

    @SubscribeEvent
    public static void onBlockExplosion(ExplosionEvent.Detonate event)
    {
        List<BlockPos> blockPos = event.getAffectedBlocks();
        for (BlockPos block: blockPos)
        {
            Transformation(event.getWorld(), block, 50);
        }
    }

    @SubscribeEvent
    public static void onBlockPlacing(BlockEvent.EntityPlaceEvent event)
    {
        World world = (World) event.getWorld();
        BlockPos blockPos = event.getPos();

        if(world.getBlockState(blockPos).getBlock()==Blocks.COBBLESTONE.getBlock())
        {
            world.setBlockAndUpdate(blockPos, ModBlocks.FALLING_COBBLE.get().defaultBlockState());
        }
        if(world.getBlockState(blockPos).getBlock()==ModBlocks.ANDESITE_COBBLE.get().getBlock())
        {
            world.setBlockAndUpdate(blockPos, ModBlocks.FALLING_ANDESITE_COBBLE.get().defaultBlockState());
        }
        if(world.getBlockState(blockPos).getBlock()==ModBlocks.DIORITE_COBBLE.get().getBlock())
        {
            world.setBlockAndUpdate(blockPos, ModBlocks.FALLING_DIORITE_COBBLE.get().defaultBlockState());
        }
        if(world.getBlockState(blockPos).getBlock()==ModBlocks.GRANITE_COBBLE.get().getBlock())
        {
            world.setBlockAndUpdate(blockPos, ModBlocks.FALLING_GRANITE_COBBLE.get().defaultBlockState());
        }
    }

    @SubscribeEvent
    public static void onBlockBreaking(BlockEvent.BreakEvent event)
    {
        PlayerEntity player = event.getPlayer();

        if(event.getState().getMaterial() == Material.STONE) {
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.WOODEN_PICKAXE) {
                Transformation((World) event.getWorld(), event.getPos(), 60);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.STONE_PICKAXE) {
                Transformation((World) event.getWorld(), event.getPos(), 40);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.IRON_PICKAXE) {
                Transformation((World) event.getWorld(), event.getPos(), 20);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.GOLDEN_PICKAXE) {
                Transformation((World) event.getWorld(), event.getPos(), 20);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.DIAMOND_PICKAXE) {
                Transformation((World) event.getWorld(), event.getPos(), 10);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.NETHERITE_PICKAXE) {
                Transformation((World) event.getWorld(), event.getPos(), 5);
            }
        }
    }


    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event)
    {
        PlayerEntity player = event.getPlayer();
        BlockPos blockPos = event.getPos();
        World world = (World) event.getWorld();

        if(player.getItemInHand(Hand.MAIN_HAND).getItem()==ModItems.MORTAR.get())
        {
            ItemStack stack = player.getItemInHand(Hand.MAIN_HAND).getStack();
            if(world.getBlockState(blockPos).getBlock()==ModBlocks.FALLING_COBBLE.get().getBlock())
            {
                stack.setCount(stack.getCount()-1);
                world.setBlockAndUpdate(blockPos, Blocks.COBBLESTONE.defaultBlockState());
            }
            if(world.getBlockState(blockPos).getBlock()==ModBlocks.FALLING_ANDESITE_COBBLE.get().getBlock())
            {
                stack.setCount(stack.getCount()-1);
                world.setBlockAndUpdate(blockPos, ModBlocks.ANDESITE_COBBLE.get().defaultBlockState());
            }
            if(world.getBlockState(blockPos).getBlock()==ModBlocks.FALLING_DIORITE_COBBLE.get().getBlock())
            {
                stack.setCount(stack.getCount()-1);
                world.setBlockAndUpdate(blockPos, ModBlocks.DIORITE_COBBLE.get().defaultBlockState());
            }
            if(world.getBlockState(blockPos).getBlock()==ModBlocks.FALLING_GRANITE_COBBLE.get().getBlock())
            {
                stack.setCount(stack.getCount()-1);
                world.setBlockAndUpdate(blockPos, ModBlocks.GRANITE_COBBLE.get().defaultBlockState());
            }
        }
    }

    @SubscribeEvent
    public static void onToolInteract(BlockEvent.BlockToolInteractEvent event) {
        PlayerEntity player = event.getPlayer();
        BlockPos blockPos = event.getPos();
        BlockState blockState = event.getState();
        World world = (World) event.getWorld();
        if (blockState == Blocks.SPRUCE_LOG.defaultBlockState())
        {
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.WOODEN_AXE) {
                ItemStack stack = new ItemStack(ModItems.WOOD_GLUE.get(), 1);
                ItemEntity itementity = new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
                world.addFreshEntity(itementity);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.STONE_AXE) {
                ItemStack stack = new ItemStack(ModItems.WOOD_GLUE.get(), 2);
                ItemEntity itementity = new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
                world.addFreshEntity(itementity);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.IRON_AXE) {
                ItemStack stack = new ItemStack(ModItems.WOOD_GLUE.get(), 3);
                ItemEntity itementity = new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
                world.addFreshEntity(itementity);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.GOLDEN_AXE) {
                ItemStack stack = new ItemStack(ModItems.WOOD_GLUE.get(), 3);
                ItemEntity itementity = new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
                world.addFreshEntity(itementity);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.DIAMOND_AXE) {
                ItemStack stack = new ItemStack(ModItems.WOOD_GLUE.get(), 4);
                ItemEntity itementity = new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
                world.addFreshEntity(itementity);
            }
            if (player.getItemInHand(Hand.MAIN_HAND).getItem() == Items.NETHERITE_AXE) {
                ItemStack stack = new ItemStack(ModItems.WOOD_GLUE.get(), 5);
                ItemEntity itementity = new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), stack);
                world.addFreshEntity(itementity);
            }
        }

    }
}
