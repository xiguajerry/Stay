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
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.MathUtil;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AutoWeb extends Module {
    public static boolean isPlacing = false;
    private final Setting<Integer> delay = register(new Setting<>("Delay", 50, 0, 250));
    private final Setting<Integer> blocksPerPlace = register(new Setting<>("BlocksPerTick", 8, 1, 30));
    private final Setting<Boolean> packet = register(new Setting<>("Packet", false));
    private final Setting<Boolean> disable = register(new Setting<>("AutoDisable", false));
    private final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    private final Setting<Boolean> raytrace = register(new Setting<>("Raytrace", false));
    private final Setting<Boolean> lowerbody = register(new Setting<>("Feet", true));
    private final Setting<Boolean> upperBody = register(new Setting<>("Face", false));
    private final Timer timer = new Timer();
    public EntityPlayer target;
    private boolean didPlace = false;
    private boolean switchedItem;
    private boolean isSneaking;
    private int lastHotbarSlot;
    private int placements = 0;
    private boolean smartRotate = false;
    private BlockPos startPos = null;

    public AutoWeb() {
        super("AutoWeb", "Traps other players in webs", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (AutoWeb.fullNullCheck()) {
            return;
        }
        startPos = EntityUtils.getRoundedBlockPos(AutoWeb.mc.player);
        lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
    }

    @Override
    public void onTick() {
        smartRotate = false;
        doTrap();
    }

    @Override
    public String getDisplayInfo() {
        if (target != null) {
            return target.getName();
        }
        return null;
    }

    @Override
    public void onDisable() {
        isPlacing = false;
        isSneaking = EntityUtils.stopSneaking(isSneaking);
        switchItem(true);
    }

    private void doTrap() {
        if (check()) {
            return;
        }
        doWebTrap();
        if (didPlace) {
            timer.reset();
        }
    }

    private void doWebTrap() {
        List<Vec3d> placeTargets = getPlacements();
        placeList(placeTargets);
    }

    private List<Vec3d> getPlacements() {
        ArrayList<Vec3d> list = new ArrayList<Vec3d>();
        Vec3d baseVec = target.getPositionVector();
        if (lowerbody.getValue().booleanValue()) {
            list.add(baseVec);
        }
        if (upperBody.getValue().booleanValue()) {
            list.add(baseVec.add(0.0, 1.0, 0.0));
        }
        return list;
    }

    private void placeList(List<Vec3d> list) {
        list.sort((vec3d, vec3d2) -> Double.compare(AutoWeb.mc.player.getDistanceSq(vec3d2.x, vec3d2.y, vec3d2.z), AutoWeb.mc.player.getDistanceSq(vec3d.x, vec3d.y, vec3d.z)));
        list.sort(Comparator.comparingDouble(vec3d -> vec3d.y));
        for (Vec3d vec3d3 : list) {
            BlockPos position = new BlockPos(vec3d3);
            int placeability = BlockUtil.isPositionPlaceable(position, raytrace.getValue());
            if (placeability != 3 && placeability != 1) continue;
            placeBlock(position);
        }
    }

    private boolean check() {
        isPlacing = false;
        didPlace = false;
        placements = 0;
        int obbySlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        if (isOff()) {
            return true;
        }
        if (disable.getValue().booleanValue() && !startPos.equals(EntityUtils.getRoundedBlockPos(AutoWeb.mc.player))) {
            disable();
            return true;
        }
        if (obbySlot == -1) {
            Command.sendMessage("<" + getDisplayName() + "> " + ChatFormatting.RED + "No Webs in hotbar disabling...");
            toggle();
            return true;
        }
        if (AutoWeb.mc.player.inventory.currentItem != lastHotbarSlot && AutoWeb.mc.player.inventory.currentItem != obbySlot) {
            lastHotbarSlot = AutoWeb.mc.player.inventory.currentItem;
        }
        switchItem(true);
        isSneaking = EntityUtils.stopSneaking(isSneaking);
        target = getTarget(10.0);
        return target == null || !timer.passedMs(delay.getValue().intValue());
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoWeb.mc.world.playerEntities) {
            if (EntityUtils.isntValid(player, range) || player.isInWeb || Stay.speedManager.getPlayerSpeed(player) > 30.0)
                continue;
            if (target == null) {
                target = player;
                distance = AutoWeb.mc.player.getDistanceSq(player);
                continue;
            }
            if (!(AutoWeb.mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = AutoWeb.mc.player.getDistanceSq(player);
        }
        return target;
    }

    private void placeBlock(BlockPos pos) {
        if (placements < blocksPerPlace.getValue() && AutoWeb.mc.player.getDistanceSq(pos) <= MathUtil.square(6.0) && switchItem(false)) {
            isPlacing = true;
            isSneaking = smartRotate ? BlockUtil.placeBlockSmartRotate(pos, EnumHand.MAIN_HAND, true, packet.getValue(), isSneaking) : BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), isSneaking);
            didPlace = true;
            ++placements;
        }
    }

    private boolean switchItem(boolean back) {
        boolean[] value = InventoryUtil.switchItem(back, lastHotbarSlot, switchedItem, InventoryUtil.Switch.NORMAL, BlockWeb.class);
        switchedItem = value[0];
        return value[1];
    }
}

