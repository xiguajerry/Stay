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
import me.alpha432.stay.manager.Mapping;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.world.WorldUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;

public class SelfFill extends Module {
    private BlockPos playerPos;
    private final Setting<Boolean> center = this.register(new Setting<>("TPCenter", false));
    private final Setting<Boolean> timerfill = register(new Setting<>("TimerFill", false));



    public SelfFill() {
        super("SelfFill", "SelfFills yourself in a hole.", Module.Category.COMBAT, true, false, true);
    }

    @Override
    public void onEnable() {

        if (timerfill.getValue()) {
            setTimer(50.0f);
        }

        playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);

        if (mc.world.getBlockState(playerPos).getBlock().equals(Blocks.OBSIDIAN)) {
            disable();
            return;
        }

        mc.player.jump();
    }

    @Override
    public void onDisable() {

        setTimer(1.0f);
    }

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (mc.player.posY > playerPos.getY() + 1.04) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            WorldUtil.placeBlock(playerPos, InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(playerPos.getX(),playerPos.getY()+1.05,playerPos.getZ(), true));
            mc.player.motionY= 1.2;
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            disable();
        }
    }

    private void setTimer(final float value) {
        try {
            final Field timer = Minecraft.class.getDeclaredField(Mapping.timer);
            timer.setAccessible(true);
            final Field tickLength = net.minecraft.util.Timer.class.getDeclaredField(Mapping.tickLength);
            tickLength.setAccessible(true);
            tickLength.setFloat(timer.get(mc), 50.0f / value);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getDisplayInfo() {
        if (timerfill.getValue()) {
            return "Timer";
        } else {
            return "Burrow";
        }
    }

}