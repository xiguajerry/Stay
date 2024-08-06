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
 *  net.minecraft.block.BlockObsidian
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3i
 */
package me.alpha432.stay.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockInteractionHelper;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.inventory.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Burrow2
extends Module {

    private Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    private boolean isSneaking = false;
    private BlockPos startPos = null;
    private boolean noFall = false;

    public Burrow2() {
        super("Burrow+", "XD", Category.COMBAT, true, false, false);
    }

    @Override
    public void onDisable() {
        this.isSneaking = EntityUtils.stopSneaking(this.isSneaking);
        if (!this.noFall) return;

        this.noFall = false;
    }
    int oldSlot = -1;
    private BlockPos originalPos;
    public boolean shouldEnable = true;
    @Override
    public void onEnable() {

        if (InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN) == -1) {
            Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "Obsidian ?");
            this.disable();
            return;
        }
        this.originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
        if (!mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) && !intersectsWithEntity(this.originalPos)) {


            if (Burrow2.mc.world.getBlockState(new BlockPos(Burrow2.mc.player.getPositionVector().add(0.0, 3.0, 0.0))).getBlock() != Blocks.AIR || Burrow2.mc.world.getBlockState(new BlockPos(Burrow2.mc.player.getPositionVector().add(0.0, 2.0, 0.0))).getBlock() != Blocks.AIR) {
                Command.sendMessage("<" + this.getDisplayName() + "> " + ChatFormatting.RED + "From above stop you !");
                this.disable();
                return;
            }
            this.shouldEnable = true;
            this.oldSlot = mc.player.inventory.currentItem;
        } else {
            this.shouldEnable = false;
            this.toggle();
        }




    }

    public void onTick() {
        if (mc.player != null && mc.world != null) {
            int blockslot = InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN);
            if (blockslot != -1) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(blockslot));
            }



                    EntityUtils.LocalPlayerfakeJump();
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    this.placeBlock(this.originalPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue(), true);
                    if (this.oldSlot != -1) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
                    }

                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY +3.0, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));

            this.toggle();
        }
    }

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
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
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

