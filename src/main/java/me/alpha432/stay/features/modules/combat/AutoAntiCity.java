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
import me.alpha432.stay.features.modules.render.HoleESP;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.HoleUtil;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoAntiCity extends Module {
    public AutoAntiCity() {
        super("AutoAntiCity", "AutoAntiCity", Module.Category.COMBAT, true, false, false);
    }

    private final Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    private final Setting<Boolean> noGhost = register(new Setting<>("Packet", false));
    private final Setting<Double> range = register(new Setting<>("Range", 8.0, 1.0, 20.0));
    private int swapBlock = -1;

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }
        swapBlock = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
        if (swapBlock == -1) {
            return;
        }
        BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (pos == null) {
            return;
        }
        if (!HoleUtil.isHole(pos) || getTarget(range.getValue()) == null) {
            return;
        }
        if(HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK){
            return;
        }

        for (Vec3d city : city) {
        BlockPos poss = new BlockPos(city.add(pos.getX(),pos.getY(),pos.getZ()));

            if (mc.world.getBlockState(poss).getBlock()==Blocks.AIR) {
                paley(poss);
            }

        }


    }

    List<Vec3d> city = new ArrayList<>(Arrays.asList(
            new Vec3d(2, 0, 0),
            new Vec3d(2, 1, 0),
            new Vec3d(-2, 0, 0),
            new Vec3d(-2, 1, 0),
            new Vec3d(0, 0, 2),
            new Vec3d(0, 1, 2),
            new Vec3d(0, 0, -2),
            new Vec3d(0, 1, -2)

    ));

    private void switchToSlot(final int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    private void paley(BlockPos pos) {
        int old = mc.player.inventory.currentItem;
        this.switchToSlot(swapBlock);
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
        this.switchToSlot(old);
    }

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

}
