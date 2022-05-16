package com.example.examplemod.blocks;

import net.minecraft.block.FallingBlock;
import net.minecraft.entity.item.FallingBlockEntity;

public class FallingDamagingBlock extends FallingBlock
{

    public FallingDamagingBlock(Properties p_i48401_1_) {
        super(p_i48401_1_);
    }

    @Override
    protected void falling(FallingBlockEntity fallingEntity) {
        fallingEntity.setHurtsEntities(true);
    }
}
