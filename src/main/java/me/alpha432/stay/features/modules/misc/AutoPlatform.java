/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.combat.HoleFiller;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.world.TestUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AutoPlatform extends Module {
    public AutoPlatform() {
        super("AutoPlatform", "AutoPlatform", Module.Category.MISC, true, false, false);
    }
    private final Setting<Double> range = (Setting<Double>) register(new Setting<>("PlaceRange", 5.0, 0.0, 7.0));
    public Setting<Boolean> Double = register(new Setting<>("Double Hole", true));
    private final Setting<Integer>  blocksPerTick = (Setting<Integer>) register(new Setting<>("BlocksPerTick", 20, 8, 30));
    private final Setting<Boolean>  rotate = (Setting<Boolean>) register(new Setting<>("Rotate", true));
    private final Setting<Boolean> packet = register(new Setting<>("Packet", false));
    private boolean isSneaking;
    private boolean hasOffhand = false;
    private int blocksThisTick = 0;
    private final Timer offTimer = new Timer();
    private final Timer timer = new Timer();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private final Timer retryTimer= new Timer() ;
    public Setting<Boolean> boxs;
    private ArrayList<BlockPos> holes  = new ArrayList<BlockPos>();
    private int trie;
    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtils.isntValid(player, range) || Stay.speedManager.getPlayerSpeed(player) > 10.0)
                continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }
            if (!(mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = mc.player.getDistanceSq(player);
        }
        return target;
    }

    @Override
    public void onEnable() {
        if (mc.player == null||mc.world==null) {return; }
        disable();
        int sand = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
        if(sand==-1){
            return;
        }
        EntityPlayer pos = getTarget(range.getValue());
        if(pos==null){
            return;
        }

        placeBlock(new BlockPos(pos.posX+1,pos.posY-1,pos.posZ));
        placeBlock(new BlockPos(pos.posX-1,pos.posY-1,pos.posZ));
        placeBlock(new BlockPos(pos.posX,pos.posY-1,pos.posZ+1));
        placeBlock(new BlockPos(pos.posX,pos.posY-1,pos.posZ-1));
        placeBlock(new BlockPos(pos.posX+1,pos.posY-1,pos.posZ+1));
        placeBlock(new BlockPos(pos.posX+1,pos.posY-1,pos.posZ-1));
        placeBlock(new BlockPos(pos.posX-1,pos.posY-1,pos.posZ+1));
        placeBlock(new BlockPos(pos.posX-1,pos.posY-1,pos.posZ-1));
    }
    private void placeBlock(final BlockPos pos) {

        for (final Entity entity : HoleFiller.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (entity instanceof EntityLivingBase) {
                return;
            }
        }
        if (blocksThisTick < blocksPerTick.getValue()) {
            final int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            final int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                toggle();
            }
            boolean smartRotate = blocksPerTick.getValue() == 1 && rotate.getValue();
            if (smartRotate) {
                isSneaking = BlockUtil.placeBlockSmartRotate(pos, hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, true, packet.getValue(), isSneaking);
            } else {
                isSneaking = BlockUtil.placeBlock(pos, hasOffhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), isSneaking);
            }
            final int originalSlot = HoleFiller.mc.player.inventory.currentItem;
            HoleFiller.mc.player.inventory.currentItem = ((obbySlot == -1) ? eChestSot : obbySlot);
            HoleFiller.mc.playerController.updateController();
            TestUtil.placeBlock(pos);
            if (HoleFiller.mc.player.inventory.currentItem != originalSlot) {
                HoleFiller.mc.player.inventory.currentItem = originalSlot;
                HoleFiller.mc.playerController.updateController();
            }
            timer.reset();
            ++blocksThisTick;
        }
    }


}
