/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:15
 */

package me.alpha432.stay.util.world;

import kotlin.jvm.JvmStatic;
import me.alpha432.stay.util.basement.wrapper.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static java.lang.Math.floor;

public class BlockUtils implements Util {
    protected static Minecraft mc = Minecraft.getMinecraft();
    public BlockPos pos;
    public double rotx;
    public double roty;
    public double dist;
    public EnumFacing f;
    public int a;

    public BlockUtils(BlockPos blockPos, int n, EnumFacing enumFacing, double d) {
        this.pos = blockPos;
        this.a = n;
        this.f = enumFacing;
        this.dist = d;
    }
    
    public static Vec3d floorVec3(Vec3d vec3) {
        return new Vec3d(floor(vec3.x),floor(vec3.y),floor(vec3.z));
    }

    public static boolean isRePlaceable(BlockPos blockPos) {
        Block block = mc.world.getBlockState(blockPos).getBlock();
        return block.isReplaceable(mc.world, blockPos) && !(block instanceof BlockAir);
    }

    public static double getDirection2D(double d, double d2) {
        double d3;
        if (d2 == 0.0D) {
            d3 = d > 0.0D ? 90.0D : -90.0D;
        } else {
            d3 = Math.atan(d / d2) * 57.2957796D;
            if (d2 < 0.0D) {
                d3 = d > 0.0D ? (d3 += 180.0D) : (d < 0.0D ? (d3 -= 180.0D) : 180.0D);
            }
        }

        return d3;
    }

    public static boolean doPlace(BlockUtils blockUtils, boolean bl) {
        return blockUtils == null ? false : blockUtils.doPlace(bl);
    }

    public static BlockUtils isPlaceable(BlockPos blockPos, double d, boolean bl) {
        BlockUtils blockUtils = new BlockUtils(blockPos, 0, (EnumFacing)null, d);
        if (!isAir(blockPos)) {
            return null;
        } else if (!(mc.player.inventory.getCurrentItem().getItem() instanceof ItemBlock)) {
            return null;
        } else {
            AxisAlignedBB axisAlignedBB = ((ItemBlock)mc.player.inventory.getCurrentItem().getItem()).getBlock().getDefaultState().getCollisionBoundingBox(mc.world, blockPos);
            if (!isAir(blockPos) && mc.world.getBlockState(blockPos).getBlock() instanceof BlockLiquid) {
                Block block = mc.world.getBlockState(blockPos.offset(EnumFacing.UP)).getBlock();
                if (block instanceof BlockLiquid) {
                    blockUtils.f = EnumFacing.DOWN;
                    blockUtils.pos.offset(EnumFacing.UP);
                    return blockUtils;
                } else {
                    blockUtils.f = EnumFacing.UP;
                    blockUtils.pos.offset(EnumFacing.DOWN);
                    return blockUtils;
                }
            } else {
                EnumFacing[] var6 = EnumFacing.values();
                int var7 = var6.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    EnumFacing enumFacing = var6[var8];
                    if (!isAir(new BlockPos(blockPos.getX() - enumFacing.getDirectionVec().getX(), blockPos.getY() - enumFacing.getDirectionVec().getY(), blockPos.getZ() - enumFacing.getDirectionVec().getZ()))) {
                        blockUtils.f = enumFacing;
                        if (bl && axisAlignedBB != Block.NULL_AABB && !mc.world.checkNoEntityCollision(axisAlignedBB.offset(blockPos), (Entity)null)) {
                            return null;
                        }

                        return blockUtils;
                    }
                }

                if (isRePlaceable(blockPos)) {
                    blockUtils.f = EnumFacing.UP;
                    blockUtils.pos.offset(EnumFacing.UP);
                    blockPos.offset(EnumFacing.DOWN);
                    if (bl && axisAlignedBB != Block.NULL_AABB && !mc.world.checkNoEntityCollision(axisAlignedBB.offset(blockPos), (Entity)null)) {
                        return null;
                    } else {
                        return blockUtils;
                    }
                } else {
                    return null;
                }
            }
        }
    }

    public static boolean isAir(BlockPos blockPos) {
        Block block = mc.world.getBlockState(blockPos).getBlock();
        return block instanceof BlockAir;
    }

    public static boolean doBreak(BlockPos blockPos, EnumFacing enumFacing) {
        return mc.playerController.clickBlock(blockPos, enumFacing);
    }

    public void doBreak() {
        mc.playerController.onPlayerDamageBlock(new BlockPos(this.pos.getX() - this.f.getDirectionVec().getX(), this.pos.getY() - this.f.getDirectionVec().getY(), this.pos.getZ() - this.f.getDirectionVec().getZ()), this.f);
    }

    public boolean doPlace(boolean bl) {
        double d = (double)this.pos.getX() + 0.5D - mc.player.posX - (double)this.f.getDirectionVec().getX() / 2.0D;
        double d2 = (double)this.pos.getY() + 0.5D - mc.player.posY - (double)this.f.getDirectionVec().getY() / 2.0D - (double)mc.player.getEyeHeight();
        double d3 = (double)this.pos.getZ() + 0.5D - mc.player.posZ - (double)this.f.getDirectionVec().getZ() / 2.0D;
        double d4 = getDirection2D(d3, d);
        double d5 = getDirection2D(d2, Math.sqrt(d * d + d3 * d3));
        Vec3d vec3d = this.getVectorForRotation(-d5, d4 - 90.0D);
        this.roty = -d5;
        this.rotx = d4 - 90.0D;
        EnumActionResult enumActionResult = mc.playerController.processRightClickBlock(mc.player, mc.world, new BlockPos(this.pos.getX() - this.f.getDirectionVec().getX(), this.pos.getY() - this.f.getDirectionVec().getY(), this.pos.getZ() - this.f.getDirectionVec().getZ()), this.f, vec3d, EnumHand.MAIN_HAND);
        if (enumActionResult == EnumActionResult.SUCCESS) {
            if (bl) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }

            return true;
        } else {
            return false;
        }
    }

    protected final Vec3d getVectorForRotation(float f, float f2) {
        float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-f * 0.017453292F);
        float f6 = MathHelper.sin(-f * 0.017453292F);
        return new Vec3d((double)(f4 * f5), (double)f6, (double)(f3 * f5));
    }

    protected final Vec3d getVectorForRotation(double d, double d2) {
        float f = MathHelper.cos((float)(-d2 * 0.01745329238474369D - 3.1415927410125732D));
        float f2 = MathHelper.sin((float)(-d2 * 0.01745329238474369D - 3.1415927410125732D));
        float f3 = -MathHelper.cos((float)(-d * 0.01745329238474369D));
        float f4 = MathHelper.sin((float)(-d * 0.01745329238474369D));
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
}
