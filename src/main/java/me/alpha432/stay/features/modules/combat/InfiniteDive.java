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
import me.alpha432.stay.features.modules.misc.InstantMine;
import me.alpha432.stay.features.modules.movement.Phase;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.player.PlayerUtil;
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class InfiniteDive extends Module {
    public InfiniteDive() {
        super("InfiniteDive", "InfiniteDive", Module.Category.COMBAT, true, false, false);
    }
    private final Timer timer = new Timer();
    public Setting<Boolean> buus = this.register(new Setting<>("Burrow", Boolean.FALSE));
    public Setting<Boolean> rotate = this.register(new Setting<>("rotate", Boolean.TRUE, v->buus.getValue()));
    public Setting<Boolean> Knockoff = this.register(new Setting<>("Knock off", Boolean.FALSE));
      public Setting<Boolean> fuck = this.register(new Setting<>("Super ghost hand", Boolean.FALSE));
    public Setting<Boolean> completion = this.register(new Setting<>("completion", Boolean.TRUE));
    public Setting<Boolean> rotate2 = this.register(new Setting<>("completion rotate", Boolean.TRUE, v->completion.getValue()));
    public Setting<Boolean> jump = this.register(new Setting<>("jump", Boolean.FALSE));
    public Setting<Boolean> packet = this.register(new Setting<>("packet", Boolean.TRUE));
    private Setting<Integer> delay = this.register(new Setting<>("Delay", 200, 0, 500));



    private Boolean insulls = true;

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            this.disable();
        }
        if(insulls){
            if(completion.getValue()){
                if(mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY-2, mc.player.posZ)).getBlock() == Blocks.AIR&&InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass())!=-1) {
                    int swapBlock = mc.player.inventory.currentItem;

                    mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
                    mc.player.setSneaking(true);
                    BlockUtil.placeBlock(new BlockPos(mc.player.posX, mc.player.posY-2, mc.player.posZ), EnumHand.MAIN_HAND,rotate2.getValue() , packet.getValue(), true);
                    mc.player.inventory.currentItem = swapBlock;
                }
            }
            issull();
            timer.reset();
            insulls = false;
            if(Knockoff.getValue()){
                BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
                if(InstantMine.breakPos!=null){
                    if(InstantMine.breakPos.getZ()==pos.getZ()&&InstantMine.breakPos.getX()==pos.getX()&&InstantMine.breakPos.getY()==pos.getY()){
                        return;
                    }
                }

                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(pos, BlockUtil.getRayTraceFacing(pos));

            }
        }

        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY+0.5, mc.player.posZ)).getBlock() != Blocks.AIR && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() != Blocks.WATER && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() != Blocks.LAVA) {
            Phase.getInstance().enable();}
        if (this.timer.passedMs(this.delay.getValue().intValue())) {
            disable();
            Phase.getInstance().disable();
            if(jump.getValue()){
                mc.player.jump();
            }

            timer.reset();
        }



    }


    public void onEnable() {
        insulls = true;
    }



        public void issull() {
        if (fullNullCheck()) {
            return;
        }

        if(!buus.getValue()){
            if(InventoryUtil.findSkullSlot2()==-1){
                return;
            }
        }

        if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY+0.5, mc.player.posZ)).getBlock() != Blocks.AIR && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() != Blocks.WATER && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() != Blocks.LAVA) {
            return;
        } else {
            if(buus.getValue()){
                int swapBlock = -1;
                BlockPos originalPos;
                BlockPos playerPos;
                int imp = mc.player.inventory.currentItem;
                swapBlock = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass());
                originalPos = PlayerUtil.getPlayerPos();
                if (InventoryUtil.findHotbarBlock(BlockObsidian.class) == -1) {
                    Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "Obsidian ?");
                    this.disable();
                    return;
                }
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
                                mc.player.posY +3,
                                mc.player.posZ,
                                false
                        )
                );

                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

                return;
            }

            if(fuck.getValue()){

                int slotMain = mc.player.inventory.currentItem;
                if (InventoryUtil.findSkullSlot() ==-1) {
                    int i = InventoryUtil.findSkullSlot2();

                    if(i==-1){
                        return;
                    }


                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                            mc.playerController.updateController();
                            mc.player.setSneaking(true);
                            BlockUtil.placeBlock(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), EnumHand.MAIN_HAND, false, packet.getValue(), true);
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                            mc.playerController.updateController();

                            return;




                }


                mc.player.inventory.currentItem = InventoryUtil.findSkullSlot();
                mc.playerController.updateController();
                mc.player.setSneaking(true);
                BlockUtil.placeBlock(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), EnumHand.MAIN_HAND,false,packet.getValue(),true);
                mc.player.inventory.currentItem = slotMain;
                mc.playerController.updateController();
                return;


            }else {
                if(InventoryUtil.findSkullSlot() == -1){
                    return;
                }
                int slotMain = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = InventoryUtil.findSkullSlot();
                mc.playerController.updateController();
                mc.player.setSneaking(true);
                BlockUtil.placeBlock(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), EnumHand.MAIN_HAND,false,packet.getValue(),true);
                mc.player.inventory.currentItem = slotMain;
                mc.playerController.updateController();
                return;

            }



        }


    }
    private void switchToSlot(final int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }
}
