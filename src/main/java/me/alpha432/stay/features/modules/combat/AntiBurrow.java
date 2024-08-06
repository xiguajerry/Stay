/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.misc.InstantMine;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AntiBurrow extends Module {
    public AntiBurrow() {
        super("AntiBurrow",  "AntiBurrow", Module.Category.COMBAT,true,false,false);
    }
    private final Setting<Double> range = register(new Setting<>("Range", 4.0, 0.0, 10.0));
    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (EntityUtils.isntValid(player, range) || Stay.speedManager.getPlayerSpeed(player) > 10.0)
                continue;
            if (target == null) {
                target = player;
                distance = AutoTrap.mc.player.getDistanceSq(player);
                continue;
            }
            if (!(AutoTrap.mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = AutoTrap.mc.player.getDistanceSq(player);
        }
        return target;
    }

    private final Setting<Boolean> disable = register(new Setting<>("disable", true));
    @Override
    public void onUpdate() {
        if (fullNullCheck())
            return;
        if (mc.currentScreen instanceof GuiHopper) {
            return;
        }
        if(ModuleManager.getModuleByName("AutoCev").isEnabled()) return;
    EntityPlayer player =getTarget(range.getValue());
        if (disable.getValue()){
            disable();
        }
    if(player==null){
        return;
    }
    BlockPos pos = new BlockPos(player.posX,player.posY+0.5D,player.posZ);
        if(pos==null){
            return;
        }
        if(InstantMine.breakPos!=null){
            if(InstantMine.breakPos.getZ()==pos.getZ()&&InstantMine.breakPos.getX()==pos.getX()&&InstantMine.breakPos.getY()==pos.getY()){
                return;
            }
        }else {
            return;
        }
    if( mc.world.getBlockState(pos).getBlock()!= Blocks.AIR&&   !this.isOnLiquid() && !this.isInLiquid()&&mc.world.getBlockState(pos).getBlock()!= Blocks.WATER&&mc.world.getBlockState(pos).getBlock()!= Blocks.LAVA){
        Anti32k.mc.player.swingArm(EnumHand.MAIN_HAND);
        Anti32k.mc.playerController.onPlayerDamageBlock(pos, BlockUtil.getRayTraceFacing(pos));


    }


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
