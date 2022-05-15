package com.example.examplemod.sounds;

import com.example.examplemod.GravityMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, GravityMod.MOD_ID);

    public static final RegistryObject<SoundEvent> ROCK_CRUMBLING_1 = SOUNDS.register("rock_crumbling_1", () -> new SoundEvent(new ResourceLocation(GravityMod.MOD_ID, "rock_crumbling_1")));
    public static final RegistryObject<SoundEvent> ROCK_CRUMBLING_2 = SOUNDS.register("rock_crumbling_2", () -> new SoundEvent(new ResourceLocation(GravityMod.MOD_ID, "rock_crumbling_2")));
    public static final RegistryObject<SoundEvent> ROCK_CRUMBLING_3 = SOUNDS.register("rock_crumbling_3", () -> new SoundEvent(new ResourceLocation(GravityMod.MOD_ID, "rock_crumbling_3")));

}
