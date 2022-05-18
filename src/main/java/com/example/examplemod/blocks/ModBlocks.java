package com.example.examplemod.blocks;

import com.example.examplemod.GravityMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GravityMod.MOD_ID);

    public static final RegistryObject<Block> FALLING_COBBLE = BLOCKS.register("falling_cobble", () -> new SlidingBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> FALLING_GRANITE_COBBLE = BLOCKS.register("falling_granite_cobble", () -> new SlidingBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> FALLING_DIORITE_COBBLE = BLOCKS.register("falling_diorite_cobble", () -> new SlidingBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> FALLING_ANDESITE_COBBLE = BLOCKS.register("falling_andesite_cobble", () -> new SlidingBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> GRANITE_COBBLE = BLOCKS.register("granite_cobble", () -> new Block(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DIORITE_COBBLE = BLOCKS.register("diorite_cobble", () -> new Block(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> ANDESITE_COBBLE = BLOCKS.register("andesite_cobble", () -> new Block(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> GRANITE_COBBLE_STAIRS = BLOCKS.register("granite_cobble_stairs", () -> new FallingStairsBlock(() -> GRANITE_COBBLE.get().defaultBlockState(), Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> GRANITE_COBBLE_SLAB = BLOCKS.register("granite_cobble_slab", () -> new FallingSlabBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> GRANITE_COBBLE_WALL = BLOCKS.register("granite_cobble_wall", () -> new WallBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> ANDESITE_COBBLE_STAIRS = BLOCKS.register("andesite_cobble_stairs", () -> new FallingStairsBlock(() -> ANDESITE_COBBLE.get().defaultBlockState(), Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> ANDESITE_COBBLE_SLAB = BLOCKS.register("andesite_cobble_slab", () -> new FallingSlabBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> ANDESITE_COBBLE_WALL = BLOCKS.register("andesite_cobble_wall", () -> new WallBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DIORITE_COBBLE_STAIRS = BLOCKS.register("diorite_cobble_stairs", () -> new FallingStairsBlock(() -> DIORITE_COBBLE.get().defaultBlockState(), Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DIORITE_COBBLE_SLAB = BLOCKS.register("diorite_cobble_slab", () -> new FallingSlabBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));
    public static final RegistryObject<Block> DIORITE_COBBLE_WALL = BLOCKS.register("diorite_cobble_wall", () -> new WallBlock(Block.Properties.of(Material.STONE).strength(3.0F, 3.0F).harvestTool(ToolType.PICKAXE)));

    public static final RegistryObject<Block> GRAVEL_PILE = BLOCKS.register("gravel_pile", () -> new PileBlock(Block.Properties.of(Material.DIRT).harvestTool(ToolType.SHOVEL).sound(SoundType.GRAVEL)));
    public static final RegistryObject<Block> SAND_PILE = BLOCKS.register("sand_pile", () -> new PileBlock(Block.Properties.of(Material.SAND).harvestTool(ToolType.SHOVEL).sound(SoundType.SAND)));
    public static final RegistryObject<Block> DIRT_PILE = BLOCKS.register("dirt_pile", () -> new PileBlock(Block.Properties.of(Material.DIRT).harvestTool(ToolType.SHOVEL).sound(SoundType.GRAVEL)));



}