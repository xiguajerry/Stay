/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 */
package me.alpha432.stay.features.modules.movement;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class ReverseStep
        extends Module {
    private final Setting<Boolean> twoBlocks = this.register(new Setting<>("2Blocks", false));

    public ReverseStep() {
        super("ReverseStep", "ReverseStep", Module.Category.MOVEMENT, true, false, false);
    }

    @Override
    public void onUpdate() {
        if (ReverseStep.fullNullCheck()) {
            return;
        }
        if(ModuleManager.getModuleByName("InfiniteDive").isEnabled()){
            return;
        }
        IBlockState touchingState = ReverseStep.mc.world.getBlockState(new BlockPos(ReverseStep.mc.player.posX, ReverseStep.mc.player.posY, ReverseStep.mc.player.posZ).down(2));
        IBlockState touchingState2 = ReverseStep.mc.world.getBlockState(new BlockPos(ReverseStep.mc.player.posX, ReverseStep.mc.player.posY, ReverseStep.mc.player.posZ).down(3));
        if (ReverseStep.mc.player.isInLava()) return;
        if (ReverseStep.mc.player.isInWater()) {
            return;
        }
        if (touchingState.getBlock() == Blocks.BEDROCK || touchingState.getBlock() == Blocks.OBSIDIAN) {
            if (!ReverseStep.mc.player.onGround) return;
            ReverseStep.mc.player.motionY -= 1.0;
            return;
        }
        if (!this.twoBlocks.getValue().booleanValue() || touchingState2.getBlock() != Blocks.BEDROCK) {
            if (this.twoBlocks.getValue() == false) return;
            if (touchingState2.getBlock() != Blocks.OBSIDIAN) return;
        }
        if (!ReverseStep.mc.player.onGround) return;
        ReverseStep.mc.player.motionY -= 1.0;
    }
}

