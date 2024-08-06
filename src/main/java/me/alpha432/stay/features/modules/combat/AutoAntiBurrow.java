/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.inventory.InventoryUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoAntiBurrow extends Module {
    public AutoAntiBurrow() {
        super("AutoAntiBurrow", "AutoAntiBurrow", Module.Category.COMBAT, true, false, false);
    }
    private final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    private final Setting<Boolean> disable = register(new Setting<>("disable", true));
    private final Setting<Boolean> noGhost = register(new Setting<>("Packet", false));
    private final Setting<Integer> deay = register(new Setting<>("Deay", 30, 0, 100));
    private final Setting<Double> range = register(new Setting<>("Range", 4.0, 0.0, 10.0));
private int tick;
private boolean gs=true;
    @Override
    public void onUpdate() {

        if (tick != 90) {
            if (tick++ >= deay.getValue()) {
                tick = 0;
                gs = true;
            }
        }
        if (mc.player == null || mc.player.isDead) return;
        if (nullCheck()) return;
        if(disable.getValue()){
            this.disable();
        }

        if (InventoryUtil.findHotbarBlock(Blocks.PISTON) == -1) {
            if(disable.getValue()){
            Command.sendMessage("<" + getDisplayName() + "> " + ChatFormatting.RED + "No PISTON...");}

            return;
        }
        if (InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK) == -1) {
            if(disable.getValue()){
            Command.sendMessage("<" + getDisplayName() + "> " + ChatFormatting.RED + "No REDSTONE_BLOCK...");}

            return;
        }
        if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) == -1) {
            if(disable.getValue()){
            Command.sendMessage("<" + getDisplayName() + "> " + ChatFormatting.RED + "No PISTON...");}

            return;
        }
        EntityPlayer target = getTarget(range.getValue(), true);

if(gs){
    gs=false;
        if(target!=null&&mc.world.getBlockState(new BlockPos(target.posX,target.posY,target.posZ)).getBlock()!=Blocks.AIR){
            if(mc.world.getBlockState(new BlockPos(target.posX,target.posY+2,target.posZ)).getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(target.posX+1,target.posY+1,target.posZ)).getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(target.posX-1,target.posY+1,target.posZ)).getBlock()==Blocks.AIR){
                if(mc.world.getBlockState(new BlockPos(target.posX+2,target.posY+1,target.posZ)).getBlock()==Blocks.AIR){
                    if (mc.world.getBlockState(new BlockPos(target.posX+2,target.posY,target.posZ)).getBlock()==Blocks.AIR){
                        int imp =mc.player.inventory.currentItem;
                        mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        BlockUtil.placeBlock(new BlockPos(target.posX+2,target.posY,target.posZ),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                        mc.player.inventory.currentItem = imp;
                    }
                    int imp =mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
                    BlockUtil.placeBlock(new BlockPos(target.posX+2,target.posY+1,target.posZ),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.PISTON);
                    BlockUtil.placeBlock(new BlockPos(target.posX+1,target.posY+1,target.posZ),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                    mc.player.inventory.currentItem=imp;

                    return;
                }
            }

            if(mc.world.getBlockState(new BlockPos(target.posX,target.posY+2,target.posZ)).getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(target.posX,target.posY+1,target.posZ+1)).getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(target.posX,target.posY+1,target.posZ-1)).getBlock()==Blocks.AIR){
                if(mc.world.getBlockState(new BlockPos(target.posX,target.posY+1,target.posZ+2)).getBlock()==Blocks.AIR){
                    if (mc.world.getBlockState(new BlockPos(target.posX,target.posY,target.posZ+2)).getBlock()==Blocks.AIR){
                        int imp =mc.player.inventory.currentItem;
                        mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        BlockUtil.placeBlock(new BlockPos(target.posX,target.posY,target.posZ+2),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                        mc.player.inventory.currentItem = imp;
                    }
                    int imp =mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
                    BlockUtil.placeBlock(new BlockPos(target.posX,target.posY+1,target.posZ+2),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.PISTON);
                    BlockUtil.placeBlock(new BlockPos(target.posX,target.posY+1,target.posZ+1),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                    mc.player.inventory.currentItem=imp;

                    return;
                }
            }
            if(mc.world.getBlockState(new BlockPos(target.posX,target.posY+2,target.posZ)).getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(target.posX,target.posY+1,target.posZ-1)).getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(target.posX,target.posY+1,target.posZ+1)).getBlock()==Blocks.AIR){
                if(mc.world.getBlockState(new BlockPos(target.posX,target.posY+1,target.posZ-2)).getBlock()==Blocks.AIR){
                    if (mc.world.getBlockState(new BlockPos(target.posX,target.posY,target.posZ-2)).getBlock()==Blocks.AIR){
                        int imp =mc.player.inventory.currentItem;
                        mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        BlockUtil.placeBlock(new BlockPos(target.posX,target.posY,target.posZ-2),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                        mc.player.inventory.currentItem = imp;
                    }
                    int imp =mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
                    BlockUtil.placeBlock(new BlockPos(target.posX,target.posY+1,target.posZ-2),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.PISTON);
                    BlockUtil.placeBlock(new BlockPos(target.posX,target.posY+1,target.posZ-1),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                    mc.player.inventory.currentItem=imp;

                    return;
                }
            }

            if(mc.world.getBlockState(new BlockPos(target.posX,target.posY+2,target.posZ)).getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(target.posX-1,target.posY+1,target.posZ)).getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(target.posX+1,target.posY+1,target.posZ)).getBlock()==Blocks.AIR){
                if(mc.world.getBlockState(new BlockPos(target.posX-2,target.posY+1,target.posZ)).getBlock()==Blocks.AIR){
                    if (mc.world.getBlockState(new BlockPos(target.posX-2,target.posY,target.posZ)).getBlock()==Blocks.AIR){
                        int imp =mc.player.inventory.currentItem;
                        mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                        BlockUtil.placeBlock(new BlockPos(target.posX-2,target.posY,target.posZ),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                        mc.player.inventory.currentItem = imp;
                    }
                    int imp =mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.REDSTONE_BLOCK);
                    BlockUtil.placeBlock(new BlockPos(target.posX-2,target.posY+1,target.posZ),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.PISTON);
                    BlockUtil.placeBlock(new BlockPos(target.posX-1,target.posY+1,target.posZ),  EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), true);
                    mc.player.inventory.currentItem=imp;

                    return;
                }
            }






        }


}}



    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (EntityUtils.isntValid(player, range) || trapped && EntityUtils.isTrapped(player, false, false, false, false, false) || Stay.speedManager.getPlayerSpeed(player) > 10.0)
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
}
