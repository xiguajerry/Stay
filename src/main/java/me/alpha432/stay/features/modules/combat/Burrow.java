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
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.player.PlayerUtil;
import me.alpha432.stay.util.world.BlockUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import me.alpha432.stay.features.setting.Setting;

/**
 * @author linustouchtips & olliem5
 * @since 11/29/2020
 */

public class Burrow extends Module {
    public Burrow() {
        super("Burrow", "Rubberbands you into a block", Category.COMBAT, true, false, false);

    }


    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));

    public Setting<Boolean> Bypass = register(new Setting<>("Bypass", true));
    private final Setting<Double> Force = register(new Setting<>("Force", 1.2, -5.0, 10.0));
    private static Burrow INSTANCE = new Burrow();
    private boolean isSneaking = false;
    private BlockPos startPos = null;
    private boolean noFall = false;
    int swapBlock = -1;
    BlockPos originalPos;
    BlockPos playerPos;
    public static Burrow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Burrow();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
    @Override
    public void onEnable() {
        if (nullCheck())
            return;
        int imp = mc.player.inventory.currentItem;
        swapBlock = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass());
        originalPos = PlayerUtil.getPlayerPos();
        if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "Obsidian ?");
            this.disable();
            return;
        }
        mc.player.motionX = 0;
        mc.player.motionZ = 0;

            playerPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);


    }
    @Override
    public void onUpdate() {
        if (nullCheck())
        return;


        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));

        mc.player.connection.sendPacket(
                new CPacketPlayer.Position(
                        mc.player.posX,
                        mc.player.posY + 0.41999998688698,
                        mc.player.posZ,
                        true
                )
        );
        mc.player.connection.sendPacket(
                new CPacketPlayer.Position(
                        mc.player.posX,
                        mc.player.posY + 0.7531999805211997,
                        mc.player.posZ,
                        true
                )
        );
        mc.player.connection.sendPacket(
                new CPacketPlayer.Position(
                        mc.player.posX,
                        mc.player.posY + 1.00133597911214,
                        mc.player.posZ,
                        true
                )
        );
        mc.player.connection.sendPacket(
                new CPacketPlayer.Position(
                        mc.player.posX,
                        mc.player.posY + 1.06610926093821,
                        mc.player.posZ,
                        true
                )
        );
        int old = mc.player.inventory.currentItem;
        this.switchToSlot(swapBlock);
        BlockUtil.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), true, false);
        this.switchToSlot(old);
        mc.player.connection.sendPacket(
                new CPacketPlayer.Position(
                        mc.player.posX,
                        mc.player.posY +Force.getValue(),
                        mc.player.posZ,
                        false
                )
        );

        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
//        Stay.positionManager.setPositionPacket((double)this.startPos.getX(), this.startPos.getY()-0.25, (double)this.startPos.getZ(), true, true, true);
//      mc.player.motionY = -0.25;
        if(Bypass.getValue() && !mc.player.isSneaking()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.player.setSneaking(true);
            mc.playerController.updateController();
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            mc.player.setSneaking(false);
            mc.playerController.updateController();
        }

        this.disable();
    }
    private void switchToSlot(final int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }


}