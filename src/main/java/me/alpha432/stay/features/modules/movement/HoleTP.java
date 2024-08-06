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
import me.alpha432.stay.util.math.HoleUtil;
import me.alpha432.stay.util.basement.wrapper.Wrapper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

/**
 * Edited by 0b00101010
 *
 * @since 25/01/21
 */


public class HoleTP extends Module {
    public HoleTP() {
        super("HoleTP", "HoleTP", Module.Category.MOVEMENT, true, false, false);

    }


    public void onUpdate() {
        if (mc.world == null || mc.player == null ) {
            return;
        }


        if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.player.fallDistance < 0.5 && this.isInHole() &&   !this.isOnLiquid() && !this.isInLiquid()) {


            if (!mc.gameSettings.keyBindJump.isKeyDown() && !mc.player.isOnLadder()) {
                BlockPos pso =gethole();
                if(pso==null){
                    return;
                }
                System.out.println(pso);
                double dist = Wrapper.getPlayer().getDistance(pso.getX()+0.5D, pso.getY()+0.5D, pso.getZ()+0.5D);
                System.out.println(dist);
                if(dist<=1){
                    Stay.positionManager.setPositionPacket(pso.getX()+0.5D, pso.getY(), pso.getZ()+0.5D, true, true, true);
                }



            }
        }
    }

    private boolean isInHole() {
        final BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY-1, mc.player.posZ);
        return this.isSafeHole(blockPos);
    }

    private double getNearestBlockBelow() {
        for (int y = (int) Math.floor(mc.player.posY); y > 0.0; y--) {
            if (!(mc.world.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock() instanceof BlockSlab) && mc.world.getBlockState(new BlockPos(mc.player.posX, y, mc.player.posZ)).getBlock().getDefaultState().getCollisionBoundingBox(mc.world, new BlockPos(0, 0, 0)) != null) {
                return y ;
            }
        }
        return -1.0;
    }
    private BlockPos gethole(){
        final BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY-1, mc.player.posZ);
        if(HoleUtil.isHole(new BlockPos(blockPos.getX()+1,blockPos.getY(),blockPos.getZ()))){
            return new BlockPos(blockPos.getX()+1,blockPos.getY(),blockPos.getZ());
        }
        if(HoleUtil.isHole(new BlockPos(blockPos.getX()-1,blockPos.getY(),blockPos.getZ()))){
            return new BlockPos(blockPos.getX()-1,blockPos.getY(),blockPos.getZ());
        }
        if(HoleUtil.isHole(new BlockPos(blockPos.getX(),blockPos.getY(),blockPos.getZ()+1))){
            return new BlockPos(blockPos.getX(),blockPos.getY(),blockPos.getZ()+1);
        }
        if(HoleUtil.isHole(new BlockPos(blockPos.getX(),blockPos.getY(),blockPos.getZ()-1))){
            return new BlockPos(blockPos.getX(),blockPos.getY(),blockPos.getZ()-1);
        }
        return null;
    }

    private boolean isSafeHole(BlockPos blockPos) {
        return HoleUtil.isHole(new BlockPos(blockPos.getX()+1,blockPos.getY(),blockPos.getZ()))|| HoleUtil.isHole(new BlockPos(blockPos.getX()-1,blockPos.getY(),blockPos.getZ()))|| HoleUtil.isHole(new BlockPos(blockPos.getX(),blockPos.getY(),blockPos.getZ()+1))|| HoleUtil.isHole(new BlockPos(blockPos.getX(),blockPos.getY(),blockPos.getZ()-1));
    }

    private boolean isOnLiquid() {
        final double y = mc.player.posY - 0.03;
        for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); x++) {
            for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); z++) {
                final BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);
                if (mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInLiquid() {
        final double y = mc.player.posY + 0.01;
        for (int x = MathHelper.floor(mc.player.posX); x < MathHelper.ceil(mc.player.posX); x++) {
            for (int z = MathHelper.floor(mc.player.posZ); z < MathHelper.ceil(mc.player.posZ); z++) {
                final BlockPos pos = new BlockPos(x, (int) y, z);
                if (mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
}