/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockInteractionHelper;
import me.alpha432.stay.util.inventory.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Burrow3 extends Module {


    private Setting<Boolean> Rotate = this.register(new Setting<>("Rotate", false));
    private final Setting<Float> offset = register(new Setting<>("Force", 0.1f,  -2F,  1f));
    public Setting<Mode> burrowMode = register(new Setting<>("Swing", Mode.FakeJump));
    int oldSlot = -1;
    private BlockPos originalPos;
    public boolean shouldEnable = true;
    public enum Mode {
        FakeJump,
        Jump,


    }
    public Burrow3() {
        super("Burrow3","", Category.COMBAT,  true, false, false);
    }

    @SubscribeEvent
    public void PacketReceiveEvent(PacketEvent.Receive event) {
    }

    public void onEnable() {
        this.originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (!mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) && !intersectsWithEntity(this.originalPos)) {
            this.shouldEnable = true;
            this.oldSlot = mc.player.inventory.currentItem;
        } else {
            this.shouldEnable = false;
            this.toggle();
        }
        if(burrowMode.getValue()==Mode.Jump){
            mc.player.jump();
        }


    }

    public void onDisable() {
        this.shouldEnable = false;
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()){
            this.disable();
        }

        if(burrowMode.getValue()==Mode.Jump){
            if (mc.player.posY > (originalPos.getY() + 1.05)) {
                mc.player.connection.sendPacket(new Position(originalPos.getX(),originalPos.getY()+1.04,originalPos.getZ(), true));
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
                int old = mc.player.inventory.currentItem;
                mc.player.setSneaking(true);
                mc.player.inventory.currentItem= InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass());
                this.placeBlock(this.originalPos, EnumHand.MAIN_HAND, (Boolean)this.Rotate.getValue(), true);
                mc.player.inventory.currentItem =old;
//                if (this.oldSlot != -1) {
//                      mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
//                }

                mc.player.motionY= offset.getValue();
                this.disable();

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
            }
        }

    }

//    public void onTick() {
//        if (mc.player != null && mc.world != null) {
//            int blockslot =  InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass()) ;
//            if (blockslot != -1) {
//                mc.player.connection.sendPacket(new CPacketHeldItemChange(blockslot));
//            }
//            byte var3 = -1;
//          if(burrowMode.getValue()==Mode.FakeJump){
//              var3 = 0;
//          }else {
//              var3 = 1;
//
//          }
//
//
//
//            switch(var3) {
//                case 0:
//                    EntityUtils.LocalPlayerfakeJump();
//                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
//                    this.placeBlock(this.originalPos, EnumHand.MAIN_HAND, (Boolean)this.Rotate.getValue(), true);
//                    if (this.oldSlot != -1) {
//                        mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
//                    }
//
//                    mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + (double)(Float)this.offset.getValue(), mc.player.posZ, false));
//                    mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
//                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
//                    break;
//                case 1:
//
//
//
////                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, mc.player.onGround));
////                    mc.player.jump();
////                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
////
////                    mc.playerController.updateController();
////                    this.placeBlock(this.originalPos, EnumHand.MAIN_HAND, (Boolean)this.Rotate.getValue(), true);
////                    if (this.oldSlot != -1) {
////                        mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
////                    }
////
//////                    mc.player.connection.sendPacket(new Position(mc.player.posX, mc.player.posY + (double)(Float)this.offset.getValue(), mc.player.posZ, false));
////                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
//            }
//
//            this.toggle();
//        }
//    }

    private static boolean intersectsWithEntity(BlockPos pos) {
        Iterator var1 = mc.world.loadedEntityList.iterator();

        Entity entity;
        do {
            if (!var1.hasNext()) {
                return false;
            }

            entity = (Entity)var1.next();
        } while(entity.equals(mc.player) || entity instanceof EntityItem || !(new AxisAlignedBB(pos)).intersects(entity.getEntityBoundingBox()));

        return true;
    }

    public List<EnumFacing> getPossibleSides(BlockPos pos) {
        ArrayList<EnumFacing> facings = new ArrayList();
        if (mc.world != null && pos != null) {
            EnumFacing[] var3 = EnumFacing.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                EnumFacing side = var3[var5];
                BlockPos neighbour = pos.offset(side);
                IBlockState blockState = mc.world.getBlockState(neighbour);
                if (blockState != null && blockState.getBlock().canCollideCheck(blockState, false) && !blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }

            return facings;
        } else {
            return facings;
        }
    }

    public void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.x - (double)pos.getX());
            float f1 = (float)(vec.y - (double)pos.getY());
            float f2 = (float)(vec.z - (double)pos.getZ());
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
        }

        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.rightClickDelayTimer = 4;
    }

    public boolean placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = null;
        Iterator<EnumFacing> iterator = this.getPossibleSides(pos).iterator();
        if (iterator.hasNext()) {
            side = (EnumFacing)iterator.next();
        }

        if (side == null) {
            return isSneaking;
        } else {
            BlockPos neighbour = pos.offset(side);
            EnumFacing opposite = side.getOpposite();
            Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
            Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
            if (!mc.player.isSneaking() && (BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock))) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
                mc.player.setSneaking(true);
                sneaking = true;
            }

            if (rotate) {
                BlockInteractionHelper.faceVectorPacketInstant(hitVec);
            }

            this.rightClickBlock(neighbour, hitVec, hand, opposite, true);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.rightClickDelayTimer = 4;
            return sneaking || isSneaking;
        }
    }
}
