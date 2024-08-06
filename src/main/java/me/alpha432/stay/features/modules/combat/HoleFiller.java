/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.basement.wrapper.Wrapper;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.HoleUtil;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.world.TestUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HoleFiller extends Module {
    private static final BlockPos[] surroundOffset;
    private static HoleFiller INSTANCE;

    static {
        HoleFiller.INSTANCE = new HoleFiller();
        surroundOffset = BlockUtil.toBlockPos(EntityUtils.getOffsets(0, true));
    }

    private final Setting<Double> range;
    private final Setting<Integer> delay;
    private final Setting<Boolean> rotate;
    private final Setting<Integer> blocksPerTick;
    private final Setting<Boolean> packet = register(new Setting<>("Packet", false));
    private final Timer offTimer;
    private final Timer timer;
    private boolean isSneaking;
    private boolean hasOffhand = false;
    private final Map<BlockPos, Integer> retries;
    private final Timer retryTimer;
    public Setting<Boolean> boxs;
    private int blocksThisTick;
    private ArrayList<BlockPos> holes;
    private int trie;

    public HoleFiller() {
        super("HoleFill", "Fills holes around you.", Category.COMBAT, true, false, true);
        range = (Setting<Double>) register(new Setting<>("PlaceRange", 5.0, 0.0, 7.0));
        delay = (Setting<Integer>) register(new Setting<>("Delay", 50, 0, 250));
        blocksPerTick = (Setting<Integer>) register(new Setting<>("BlocksPerTick", 20, 8, 30));
        rotate = (Setting<Boolean>) register(new Setting<>("Rotate", true));
         boxs = register(new Setting<>("Double Hole", true));
        offTimer = new Timer();
        timer = new Timer();
        blocksThisTick = 0;
        retries = new HashMap<BlockPos, Integer>();
        retryTimer = new Timer();
        holes = new ArrayList<BlockPos>();
        setInstance();
    }

    public static HoleFiller getInstance() {
        if (HoleFiller.INSTANCE == null) {
            HoleFiller.INSTANCE = new HoleFiller();
        }
        return HoleFiller.INSTANCE;
    }

    private void setInstance() {
        HoleFiller.INSTANCE = this;
    }

    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            disable();
        }
        offTimer.reset();
        trie = 0;
    }

    @Override
    public void onTick() {
        if(isOn() && !(blocksPerTick.getValue() == 1 && rotate.getValue())) {
            doHoleFill();
        }
    }

    @Override
    public void onDisable() {
        retries.clear();
    }

    private void doHoleFill() {
        if (check()) {
            return;
        }
        holes = new ArrayList<BlockPos>();
        Double maxDist = this.range.getValue();
        BlockPos ret = null;
        Double x;
        for (x = maxDist; x >= -maxDist; x--) {
            Double y;
            for (y = maxDist; y >= -maxDist; y--) {

                Double z;
                for (z = maxDist; z >= -maxDist; z--) {
                    BlockPos pos = new BlockPos(Wrapper.getPlayer().posX + x+0.5D, Wrapper.getPlayer().posY + y+0.5D, Wrapper.getPlayer().posZ + z+0.5D);
                    double dist = Wrapper.getPlayer().getDistance(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D);
                    if( mc.world.getBlockState(pos).getBlock()!= Blocks.AIR){
                        continue;
                    }
                    if(boxs.getValue()){
                        if(dist <= maxDist && HoleUtil.is2Hole(pos)!=null){
                            holes.add(pos);
                            holes.add(HoleUtil.is2Hole(pos));
                            continue;
                        }
                    }

                    if(dist <= maxDist &&HoleUtil.isHole(pos)){
                        holes.add(pos);
                        continue;
                    }
                }
            }
        }


        holes.forEach(this::placeBlock);
        toggle();
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

    private boolean check() {
        if (fullNullCheck()) {
            disable();
            return true;
        }
        blocksThisTick = 0;
        if (retryTimer.passedMs(2000L)) {
            retries.clear();
            retryTimer.reset();
        }
        return !timer.passedMs(delay.getValue());
    }
}
