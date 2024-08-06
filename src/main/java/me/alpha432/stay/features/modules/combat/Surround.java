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
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.block.BlockAir
 *  net.minecraft.block.BlockDeadBush
 *  net.minecraft.block.BlockEnderChest
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockTallGrass
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
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
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class Surround
extends Module {
    private final Setting<Integer> blocksPerTick = this.register(new Setting<>("BlocksPerTick", 12, 1, 20));
    private final Setting<Integer> delay = this.register(new Setting<>("Delay", 0, 0, 250));
    private final Setting<Boolean> togg1e = this.register(new Setting<>("AutoToggle", false));
    private final Setting<Boolean> noGhost = this.register(new Setting<>("PacketPlace", false));
    private final Setting<Boolean> center = this.register(new Setting<>("TPCenter", false));
    private final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private boolean isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private int obbySlot = -1;
    private boolean offHand = false;

    public Surround() {
        super("Surround", "Surrounds you with Obsidian", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (Surround.fullNullCheck()) {
            return;
        }
        this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        this.startPos = EntityUtils.getRoundedBlockPos((Entity)Surround.mc.player);
        if (this.center.getValue().booleanValue()) {
            Stay.positionManager.setPositionPacket((double)this.startPos.getX() + 0.5, this.startPos.getY(), (double)this.startPos.getZ() + 0.5, true, true, true);
        }
        this.retries.clear();
        this.retryTimer.reset();
    }

    @Override
    public void onTick() {
        if (Surround.fullNullCheck()) {
            return;
        }
        this.doFeetPlace();
    }

    @Override
    public void onDisable() {
        this.isSneaking = EntityUtils.stopSneaking(this.isSneaking);
    }

    @Override
    public String getDisplayInfo() {
        if (!this.isSafe) return ChatFormatting.GREEN + "Safe";
        return ChatFormatting.RED + "Unsafe";
    }

    private void doFeetPlace() {
        if (this.check()) {
            return;
        }
        if (!EntityUtils.isSafe((Entity)Surround.mc.player, 0, true)) {
            this.isSafe = true;
            this.placeBlocks(Surround.mc.player.getPositionVector(), EntityUtils.getUnsafeBlockArray((Entity)Surround.mc.player, 0, true), true, false, false);
        } else {
            this.isSafe = false;
        }
        this.processExtendingBlocks();
        if (this.didPlace) {
            this.timer.reset();
        }
        if (this.togg1e.getValue() == false) return;
        this.toggle();
    }

    private void processExtendingBlocks() {
        if (this.extendingBlocks.size() == 2 && this.extenders < 1) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            Iterator<Vec3d> iterator = this.extendingBlocks.iterator();
            while (iterator.hasNext()) {
                array[i] = iterator.next();
                ++i;
            }
            int placementsBefore = this.placements;
            if (this.areClose(array) != null) {
                this.placeBlocks(this.areClose(array), EntityUtils.getUnsafeBlockArrayFromVec3d(this.areClose(array), 0, true), true, false, true);
            }
            if (placementsBefore >= this.placements) return;
            this.extendingBlocks.clear();
            return;
        }
        if (this.extendingBlocks.size() <= 2) {
            if (this.extenders < 1) return;
        }
        this.extendingBlocks.clear();
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        Vec3d[] vec3dArray = vec3ds;
        int n = vec3dArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                if (matches != 2) return null;
                return Surround.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
            }
            Vec3d vec3d = vec3dArray[n2];
            for (Vec3d pos : EntityUtils.getUnsafeBlockArray((Entity)Surround.mc.player, 0, true)) {
                if (!vec3d.equals((Object)pos)) continue;
                ++matches;
            }
            ++n2;
        }
    }

    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping, boolean isExtending) {
        boolean gotHelp = true;
        Vec3d[] vec3dArray = vec3ds;
        int n = vec3dArray.length;
        int n2 = 0;
        while (n2 < n) {
            Vec3d vec3d = vec3dArray[n2];
            gotHelp = true;
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (this.retries.get(position) == null || this.retries.get(position) < 4) {
                        this.placeBlock(position);
                        this.retries.put(position, this.retries.get(position) == null ? 1 : this.retries.get(position) + 1);
                        this.retryTimer.reset();
                        break;
                    }
                    if (Stay.speedManager.getSpeedKpH() != 0.0 || isExtending || this.extenders >= 1) break;
                    this.placeBlocks(Surround.mc.player.getPositionVector().add(vec3d), EntityUtils.getUnsafeBlockArrayFromVec3d(Surround.mc.player.getPositionVector().add(vec3d), 0, true), hasHelpingBlocks, false, true);
                    this.extendingBlocks.add(vec3d);
                    ++this.extenders;
                    break;
                }
                case 2: {
                    if (!hasHelpingBlocks) break;
                    gotHelp = this.placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true, true);
                }
                case 3: {
                    if (gotHelp) {
                        this.placeBlock(position);
                    }
                    if (isHelping) return true;
                    break;
                }
            }
            ++n2;
        }
        return false;
    }

    private boolean check() {
        if (Surround.fullNullCheck()) {
            if (!this.isOn()) return true;
            this.disable();
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        this.offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        this.didPlace = false;
        this.extenders = 1;
        this.placements = 0;
        this.obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (this.isOff()) {
            return true;
        }
        if (this.retryTimer.passedMs(2500L)) {
            this.retries.clear();
            this.retryTimer.reset();
        }
        if (this.obbySlot == -1 && !this.offHand && echestSlot == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "No Obsidian in hotbar disabling...");
            this.disable();
            return true;
        }
        this.isSneaking = EntityUtils.stopSneaking(this.isSneaking);
        if (Surround.mc.player.inventory.currentItem != this.lastHotbarSlot && Surround.mc.player.inventory.currentItem != this.obbySlot && Surround.mc.player.inventory.currentItem != echestSlot) {
            this.lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!this.startPos.equals((Object) EntityUtils.getRoundedBlockPos((Entity)Surround.mc.player))) {
            this.disable();
            return true;
        }
        if (this.timer.passedMs(this.delay.getValue().intValue())) return false;
        return true;
    }

    private void placeBlock(BlockPos pos) {
        if (fullNullCheck()) return;
        if (this.placements >= this.blocksPerTick.getValue()) return;
        int originalSlot = Surround.mc.player.inventory.currentItem;
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            this.toggle();
        }
        Surround.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
        Surround.mc.playerController.updateController();
        for (BlockPos blockPos : new BlockPos[]{pos.north(), pos.south(), pos.east(), pos.west(), pos.down(), pos.up()}) {
            if (fullNullCheck()) return;
            IBlockState block = Surround.mc.world.getBlockState(blockPos);
            if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) continue;
            this.isSneaking = BlockUtil.placeBlock(pos.down(), this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
        }
        this.isSneaking = BlockUtil.placeBlock(pos, this.offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, this.rotate.getValue(), this.noGhost.getValue(), this.isSneaking);
        Surround.mc.player.inventory.currentItem = originalSlot;
        Surround.mc.playerController.updateController();
        this.didPlace = true;
        ++this.placements;
    }
}

