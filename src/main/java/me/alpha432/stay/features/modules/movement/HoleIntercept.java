/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.movement;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.math.HoleUtil;
import me.alpha432.stay.util.player.PlayerUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

public class HoleIntercept  extends Module {
    public HoleIntercept() {
        super("HoleIntercept","HoleIntercept" ,Category.MOVEMENT, true, false, false);
    }
    private final Setting<Integer> height = register(new Setting<>("Height", 2, 0, 10));


    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (mc.player == null || mc.world == null || mc.player.isInWater() || mc.player.isInLava()) {
            return;
        }
        BlockPos originalPos = PlayerUtil.getPlayerPos();
        if(!HoleUtil.isInHole()){
            for (int i = 1; i <= height.getValue(); i++) {
                if((originalPos.getY()-i)<=0){
                    break;
                }


                IBlockState blockState2 = mc.world.getBlockState(new BlockPos(originalPos.getX(),originalPos.getY()-i,originalPos.getZ()));
                if(blockState2.getBlock()!= Blocks.AIR){
                  return;
                }

                if(HoleUtil.isHole(new BlockPos(originalPos.getX(),originalPos.getY()-i,originalPos.getZ())) ){
                    if (mc.player.onGround) {
                        --mc.player.motionY;
                    }

                    for (int j = 0; j < i; j++) {

                            Stay.positionManager.setPositionPacket((double) originalPos.getX() + 0.5, originalPos.getY()-0.5*j, (double) originalPos.getZ() + 0.5, true, true, true);


                    }


                    return;
                }
            }
        }
    }
}
