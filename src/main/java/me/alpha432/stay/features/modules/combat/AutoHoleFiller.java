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
import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;
import me.alpha432.stay.util.basement.wrapper.Wrapper;
import me.alpha432.stay.util.world.BlockInteractionHelper;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;


public class AutoHoleFiller extends Module {
    public Setting<Double> range = register(new Setting<>("Range", 4.5D, 0.0, 6.0));
    public Setting<Boolean> smart = register(new Setting<>("Smart", false));
    public Setting<Integer> smartRange = register(new Setting<>("distance2", 4, 1, 6));
    private final Setting<Boolean> announceUsage = register(new Setting<>("Announce Usage", false));
    private BlockPos render;
    private EntityPlayer closestTarget;
    int resetTimer;
    boolean rotated = false;

    @Override
    public void onUpdate() {

        if (mc.player != null && !ModuleManager.getModuleByName("Freecam").isEnabled()) {
            if (mc.world != null) {
                if ((Boolean)this.smart.getValue()) {
                    this.findClosestTarget();
                }

                BlockPos q = null;
                double dist = 0.0D;
                double prevDist = 0.0D;
                int obsidianSlot = mc.player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN) ? mc.player.inventory.currentItem : -1;
                if (obsidianSlot == -1) {
                    for(int l = 0; l < 9; ++l) {
                        if (mc.player.inventory.getStackInSlot(l).getItem() == Item.getItemFromBlock(Blocks.OBSIDIAN)) {
                            obsidianSlot = l;
                            break;
                        }
                    }
                }


                if (obsidianSlot == -1) {
                    if (this.rotated) {



                            this.resetTimer = 0;
                            this.rotated = false;

                    }

                } else {
                    EntityPlayer w = this.findClosestTarget();
                    if (w == null) {
                        ++this.resetTimer;


                                this.resetTimer = 0;
                                this.rotated = false;



                    } else {
                        List<BlockPos> blocks = this.findCrystalBlocks(mc.player);
                        blocks.sort(Comparator.comparing((e) -> {
                            return BlockInteractionHelper.blockDistance2d((double) e.getX() + 0.5D, (double) e.getZ() + 0.5D, w);
                        }));
                        blocks.removeIf((e) -> {
                            return Math.sqrt(w.getDistanceSq((double) e.getX() + 0.5D, (double) e.getY(), (double) e.getZ() + 0.5D)) > 2.5D || !mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(e)).isEmpty() && mc.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(e)).isEmpty() || Math.sqrt(mc.player.getDistanceSq((double) e.getX(), (double)((float) e.getY() - mc.player.getEyeHeight()), (double) e.getZ())) > (Double)this.range.getValue();
                        });
                        q = (BlockPos)blocks.stream().findFirst().orElse((BlockPos) null);
                        this.render = q;
                        if (q == null) {
                            ++this.resetTimer;
                            if (this.resetTimer >= 5 && this.rotated) {

                                    this.resetTimer = 0;
                                    this.rotated = false;

                            }
                        } else if (mc.player.onGround) {
                            int oldSlot = mc.player.inventory.currentItem;
                            if (mc.player.inventory.currentItem != obsidianSlot) {
                                mc.player.inventory.currentItem = obsidianSlot;
                            }

                            boolean y = this.placeBlockScaffold(this.render);
                            if (!y) {
                                ++this.resetTimer;
                                if (this.resetTimer >= 5 && this.rotated ) {


                                        this.resetTimer = 0;
                                        this.rotated = false;

                                }
                            } else {
                                this.rotated = true;
                                this.resetTimer = 0;
                            }

                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            mc.player.inventory.currentItem = oldSlot;
                        }

                    }
                }
            }
        }
    }





    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;
    @SubscribeEvent
    public void onUpdateWalkingPlayer(PacketEvent.Send event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof CPacketPlayer && isSpoofingAngles) {
            ((CPacketPlayer)packet).yaw = (float)yaw;
            ((CPacketPlayer)packet).pitch = (float)pitch;
        }
    }




    public void onEnable() {
        if ((Boolean)this.announceUsage.getValue()) {
            Command.sendMessage("[HoleFiller] " + ChatFormatting.GREEN.toString() + "Enabled" + ChatFormatting.RESET.toString() + "!");
        }

    }

    public boolean placeBlockScaffold(BlockPos pos) {
        if (mc.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos)).isEmpty() && mc.world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(pos)).isEmpty()) {
            new Vec3d(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY + (double)Wrapper.getPlayer().getEyeHeight(), Wrapper.getPlayer().posZ);
            EnumFacing[] var3 = EnumFacing.values();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                EnumFacing side = var3[var5];
                BlockPos neighbor = pos.offset(side);
                EnumFacing side2 = side.getOpposite();
                if (BlockInteractionHelper.canBeClicked(neighbor)) {
                    Vec3d hitVec = (new Vec3d(neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
                    float[] w = BlockInteractionHelper.getLegitRotations(hitVec);

                    mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                    Wrapper.getPlayer().swingArm(EnumHand.MAIN_HAND);
                    mc.rightClickDelayTimer = 4;
                    return true;
                }
            }
        }

        return false;
    }



    private double getDistanceToBlockPos(BlockPos pos1, BlockPos pos2) {
        double x = (double)(pos1.getX() - pos2.getX());
        double y = (double)(pos1.getY() - pos2.getY());
        double z = (double)(pos1.getZ() - pos2.getZ());
        return Math.sqrt(x * x + y * y + z * z);
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtils.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float)v[0], (float)v[1]);
    }

    private boolean IsHole(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 0, 0);
        BlockPos boost3 = blockPos.add(0, 0, -1);
        BlockPos boost4 = blockPos.add(1, 0, 0);
        BlockPos boost5 = blockPos.add(-1, 0, 0);
        BlockPos boost6 = blockPos.add(0, 0, 1);
        BlockPos boost7 = blockPos.add(0, 2, 0);
        BlockPos boost8 = blockPos.add(0.5D, 0.5D, 0.5D);
        BlockPos boost9 = blockPos.add(0, -1, 0);
        return mc.world.getBlockState(boost).getBlock() == Blocks.AIR && mc.world.getBlockState(boost2).getBlock() == Blocks.AIR && mc.world.getBlockState(boost7).getBlock() == Blocks.AIR && (mc.world.getBlockState(boost3).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost3).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(boost3).getBlock() == Blocks.ENDER_CHEST) && (mc.world.getBlockState(boost4).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost4).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(boost4).getBlock() == Blocks.ENDER_CHEST) && (mc.world.getBlockState(boost5).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost5).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(boost5).getBlock() == Blocks.ENDER_CHEST) && (mc.world.getBlockState(boost6).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost6).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(boost6).getBlock() == Blocks.ENDER_CHEST) && mc.world.getBlockState(boost8).getBlock() == Blocks.AIR && (mc.world.getBlockState(boost9).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(boost9).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(boost9).getBlock() == Blocks.ENDER_CHEST);
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public BlockPos getClosestTargetPos() {
        return this.closestTarget != null ? new BlockPos(Math.floor(this.closestTarget.posX), Math.floor(this.closestTarget.posY), Math.floor(this.closestTarget.posZ)) : null;
    }

    private EntityPlayer findClosestTarget() {
        List<EntityPlayer> playerList = mc.world.playerEntities;
        EntityPlayer closesttarget = null;
        Iterator var3 = playerList.iterator();

        while(var3.hasNext()) {
            EntityPlayer target = (EntityPlayer)var3.next();
            if (target != mc.player && !Stay.friendManager.isFriend(target.getName()) && EntityUtils.isLiving(target) && !(target.getHealth() <= 0.0F)) {
                if (closesttarget == null) {
                    closesttarget = target;
                } else if (mc.player.getDistance(target) < mc.player.getDistance(closesttarget)) {
                    closesttarget = target;
                }
            }
        }

        return closesttarget;
    }

    private boolean isInRange(BlockPos blockPos) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll((Collection)this.getSphere(getPlayerPos(), ((Double)this.range.getValue()).floatValue(), ((Double)this.range.getValue()).intValue(), false, true, 0).stream().filter(this::IsHole).collect(Collectors.toList()));
        return positions.contains(blockPos);
    }

    private List<BlockPos> findCrystalBlocks(EntityPlayer player) {
        NonNullList<BlockPos> positions = NonNullList.create();
        if ((Boolean)this.smart.getValue() && this.closestTarget != null) {
            positions.addAll((Collection)this.getSphere(this.getClosestTargetPos(), ((Integer)this.smartRange.getValue()).floatValue(), ((Double)this.range.getValue()).intValue(), false, true, 0).stream().filter(this::IsHole).filter(this::isInRange).collect(Collectors.toList()));
        } else if (!(Boolean)this.smart.getValue()) {
            positions.addAll((Collection)this.getSphere(player.getPosition(), ((Double)this.range.getValue()).floatValue(), ((Double)this.range.getValue()).intValue(), false, true, 0).stream().filter(this::IsHole).collect(Collectors.toList()));
        }

        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();

        for(int x = cx - (int)r; (float)x <= (float)cx + r; ++x) {
            for(int z = cz - (int)r; (float)z <= (float)cz + r; ++z) {
                for(int y = sphere ? cy - (int)r : cy; (float)y < (sphere ? (float)cy + r : (float)(cy + h)); ++y) {
                    double dist = (double)((cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0));
                    if (dist < (double)(r * r) && (!hollow || !(dist < (double)((r - 1.0F) * (r - 1.0F))))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }

    private static void setYawAndPitch(float yaw1, float pitch1) {
        yaw = (double)yaw1;
        pitch = (double)pitch1;
        isSpoofingAngles = true;
    }

    private static void resetRotation() {
        if (isSpoofingAngles) {
            yaw = (double)mc.player.rotationYaw;
            pitch = (double)mc.player.rotationPitch;
            isSpoofingAngles = false;
        }

    }

    public void onDisable() {
        this.closestTarget = null;
        this.render = null;
        resetRotation();
        if ((Boolean)this.announceUsage.getValue()) {
            Command.sendMessage("[HoleFiller] " + ChatFormatting.RED.toString() + "Disabled" + ChatFormatting.RESET.toString() + "!");
        }

    }


    public AutoHoleFiller() {
        super("AutoHoleFiller", "AutoHoleFiller", Module.Category.COMBAT, true, false, false);
    }

}
