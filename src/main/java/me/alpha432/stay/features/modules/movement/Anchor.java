/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.movement;


import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.math.HoleUtil;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

/**
 * @author Hoosiers
 * @author 0b00101010
 * @since 09/28/20
 * @since 25/01/21
 */


public class Anchor extends Module {
    public Anchor() {
        super("Anchor", "Anchor", Module.Category.MOVEMENT, false, false, false);
    }

    public Setting<Boolean> guarantee = register(new Setting<>("Guarantee Hole", true));

    private final Setting<Integer> activateHeight = register(new Setting<>("Horizontal",2,1,5));
    BlockPos playerPos;

    @Override
    public void onUpdate() {
        if (mc.player == null) {
            return;
        }
        if(mc.currentScreen instanceof GuiHopper){
            return;
        }

        if (mc.player.posY < 0) {
            return;
        }

        double blockX = Math.floor(mc.player.posX);
        double blockZ = Math.floor(mc.player.posZ);

        double offsetX = Math.abs(mc.player.posX - blockX);
        double offsetZ = Math.abs(mc.player.posZ - blockZ);

        if (guarantee.getValue() && (offsetX < 0.3f || offsetX > 0.7f || offsetZ < 0.3f || offsetZ > 0.7f)) {
            return;
        }

        playerPos = new BlockPos(blockX, mc.player.posY, blockZ);

        if (mc.world.getBlockState(playerPos).getBlock() != Blocks.AIR) {
            return;
        }

        BlockPos currentBlock = playerPos.down();
        for (int i = 0; i < activateHeight.getValue(); i++) {
            currentBlock = currentBlock.down();
            if (mc.world.getBlockState(currentBlock).getBlock() != Blocks.AIR) {
                HashMap<HoleUtil.BlockOffset, HoleUtil.BlockSafety> sides = HoleUtil.getUnsafeSides(currentBlock.up());
                sides.entrySet().removeIf(entry -> entry.getValue() == HoleUtil.BlockSafety.RESISTANT);
                if (sides.size() == 0) {
                    mc.player.motionX = 0f;
                    mc.player.motionZ = 0f;
                }
            }
        }
    }
}