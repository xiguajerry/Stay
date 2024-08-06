/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:29
 */

package me.alpha432.stay.util.world;

import me.alpha432.stay.util.basement.wrapper.Util;
import me.alpha432.stay.util.inventory.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.Item;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class PlacementUtil implements Util {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static boolean isSneaking = false;
    private static int placementConnections = 0;

    public PlacementUtil() {
    }

    public static boolean place(BlockPos blockPos, EnumHand enumHand, boolean bl) {
        return placeBlock(blockPos, enumHand, bl, true, null);
    }

    public static boolean placeBlock(BlockPos blockPos, EnumHand enumHand, boolean bl, boolean bl2, ArrayList<EnumFacing> arrayList) {
        EntityPlayerSP entityPlayerSP = mc.player;
        WorldClient worldClient = mc.world;
        PlayerControllerMP playerControllerMP = mc.playerController;
        if (entityPlayerSP != null && worldClient != null && playerControllerMP != null) {
            if (!worldClient.getBlockState(blockPos).getMaterial().isReplaceable()) {
                return false;
            } else {
                EnumFacing enumFacing = arrayList != null ? BlockUtil.getPlaceableSideExlude(blockPos, arrayList) : BlockUtil.getPlaceableSide(blockPos);
                if (enumFacing == null) {
                    return false;
                } else {
                    BlockPos blockPos2 = blockPos.offset(enumFacing);
                    EnumFacing enumFacing3 = enumFacing.getOpposite();
                    if (!BlockUtil.canBeClicked(blockPos2)) {
                        return false;
                    } else {
                        Vec3d vec3d = (new Vec3d(blockPos2)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(enumFacing3.getDirectionVec())).scale(0.5D));
                        Block block = worldClient.getBlockState(blockPos2).getBlock();
                        if (!isSneaking && BlockUtil.blackList.contains(block) || BlockUtil.shulkerList.contains(block)) {
                            entityPlayerSP.connection.sendPacket(new CPacketEntityAction(entityPlayerSP, CPacketEntityAction.Action.START_SNEAKING));
                            isSneaking = true;
                        }

                        if (bl) {
                            BlockUtil.faceVectorPacketInstant(vec3d, true);
                        }

                        EnumActionResult enumActionResult = playerControllerMP.processRightClickBlock(entityPlayerSP, worldClient, blockPos2, enumFacing3, vec3d, enumHand);
                        if (!bl2 || enumActionResult == EnumActionResult.SUCCESS) {
                            entityPlayerSP.swingArm(enumHand);
                            mc.rightClickDelayTimer = 4;
                        }

                        return enumActionResult == EnumActionResult.SUCCESS;
                    }
                }
            }
        } else {
            return false;
        }
    }

    public static void onDisable() {
        if (--placementConnections == 0 && isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }

    }

    public static boolean placePrecise(BlockPos blockPos, EnumHand enumHand, boolean bl, Vec3d vec3d, EnumFacing enumFacing, boolean bl2, boolean bl3) {
        EntityPlayerSP entityPlayerSP = mc.player;
        WorldClient worldClient = mc.world;
        PlayerControllerMP playerControllerMP = mc.playerController;
        if (entityPlayerSP != null && worldClient != null && playerControllerMP != null) {
            if (!worldClient.getBlockState(blockPos).getMaterial().isReplaceable()) {
                return false;
            } else {
                EnumFacing enumFacing2 = enumFacing == null ? BlockUtil.getPlaceableSide(blockPos) : enumFacing;
                if (enumFacing2 == null) {
                    return false;
                } else {
                    BlockPos blockPos2 = blockPos.offset(enumFacing2);
                    EnumFacing enumFacing4 = enumFacing2.getOpposite();
                    if (!BlockUtil.canBeClicked(blockPos2)) {
                        return false;
                    } else {
                        Vec3d vec3d2 = (new Vec3d(blockPos2)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(enumFacing4.getDirectionVec())).scale(0.5D));
                        Block block = worldClient.getBlockState(blockPos2).getBlock();
                        if (!isSneaking && BlockUtil.blackList.contains(block) || BlockUtil.shulkerList.contains(block)) {
                            entityPlayerSP.connection.sendPacket(new CPacketEntityAction(entityPlayerSP, CPacketEntityAction.Action.START_SNEAKING));
                            isSneaking = true;
                        }


                        if (bl && !bl3) {
                            BlockUtil.faceVectorPacketInstant(vec3d == null ? vec3d2 : vec3d, true);
                        }

                        if (!bl2) {
                            EnumActionResult enumActionResult = playerControllerMP.processRightClickBlock(entityPlayerSP, worldClient, blockPos2, enumFacing4, vec3d == null ? vec3d2 : vec3d, enumHand);
                            if (enumActionResult == EnumActionResult.SUCCESS) {
                                entityPlayerSP.swingArm(enumHand);
                                mc.rightClickDelayTimer = 4;
                            }

                            return enumActionResult == EnumActionResult.SUCCESS;
                        } else {
                            return true;
                        }
                    }
                }
            }
        } else {
            return false;
        }
    }

    public static boolean placeItem(BlockPos blockPos, EnumHand enumHand, boolean bl, Class<? extends Item> clazz) {
        int n = mc.player.inventory.currentItem;
        int n2 = InventoryUtil.findFirstItemSlot(clazz, 0, 8);
        if (n2 == -1) {
            return false;
        } else {
            mc.player.inventory.currentItem = n2;
            boolean bl2 = place(blockPos, enumHand, bl);
            mc.player.inventory.currentItem = n;
            return bl2;
        }
    }

    public static boolean place(BlockPos blockPos, EnumHand enumHand, boolean bl, boolean bl2) {
        return placeBlock(blockPos, enumHand, bl, bl2, (ArrayList)null);
    }

    public static boolean placeBlock(BlockPos blockPos, EnumHand enumHand, boolean bl, Class<? extends Block> clazz) {
        int n = mc.player.inventory.currentItem;
        int n2 = InventoryUtil.findFirstBlockSlot(clazz, 0, 8);
        if (n2 == -1) {
            return false;
        } else {
            mc.player.inventory.currentItem = n2;
            boolean bl2 = place(blockPos, enumHand, bl);
            mc.player.inventory.currentItem = n;
            return bl2;
        }
    }

    public static boolean place(BlockPos blockPos, EnumHand enumHand, boolean bl, ArrayList<EnumFacing> arrayList) {
        return placeBlock(blockPos, enumHand, bl, true, arrayList);
    }

    public static void onEnable() {
        ++placementConnections;
    }
}
