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
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.HoleUtil;
import me.alpha432.stay.util.player.PlayerUtil;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class NewBuorrw extends Module {
    public NewBuorrw() {
        super("NewBuorrw", "AutoBurrw", Category.COMBAT, true, false, false);
    }

    private final Timer manualTimer = new Timer();
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public Setting<Boolean> Bypass = register(new Setting<>("Bypass", true));
    public Setting<Boolean> noexcavate = register(new Setting<>("excavate", true));
    private final Setting<Double> Force = register(new Setting<>("Force", 1.2, -5.0, 10.0));
    private final Setting<Double> range = register(new Setting<>("Range", 2.5, 0.0, 10.0));

    private boolean cuican = true;

    @Override
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (mc.player == null || mc.world == null || mc.player.isInWater() || mc.player.isInLava()) {
            return;
        }

        int swapBlock = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass());
        if (swapBlock == -1) {
            return;
        }
        EntityPlayer target = getTarget(range.getValue(), true);

        if (HoleUtil.isInHole()) {

            if (target != null) {
                if (mc.player.onGround) {
                    --mc.player.motionY;
                }
                BlockPos originalPos = PlayerUtil.getPlayerPos();
                IBlockState blockState2 = mc.world.getBlockState(originalPos);
                if(noexcavate.getValue()){
                    if(InstantMine.breakPos!=null){
                        if (originalPos.getX()==InstantMine.breakPos.getX()&&originalPos.getY()==InstantMine.breakPos.getY()&&originalPos.getZ()==InstantMine.breakPos.getZ()){
                            return;
                        }
                    }

                }

                if(blockState2.getBlock()==Blocks.AIR&&mc.world.getBlockState(new BlockPos(originalPos.getX(),originalPos.getY()+2,originalPos.getZ())).getBlock()==Blocks.AIR){

                    Burrow();
                }
            }
        }



    }


    private void Burrow() {


        if (nullCheck())
            return;
        cuican = false;
        int imp = mc.player.inventory.currentItem;
        int swapBlock = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass());
        BlockPos originalPos = PlayerUtil.getPlayerPos();
        if (swapBlock == -1) {
            this.disable();
            return;
        }
        mc.player.motionX = 0;
        mc.player.motionZ = 0;

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
                        mc.player.posY + 1.16610926093821,
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
                        mc.player.posY + Force.getValue(),
                        mc.player.posZ,
                        false
                )
        );
        if (Bypass.getValue() && !mc.player.isSneaking()) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            mc.player.setSneaking(true);
            mc.playerController.updateController();
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            mc.player.setSneaking(false);
            mc.playerController.updateController();
        }

    }


    private void switchToSlot(final int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }


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
