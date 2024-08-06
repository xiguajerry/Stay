/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:19
 */

package me.alpha432.stay.util.world;


import me.alpha432.stay.util.inventory.InventoryUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class CrystalUtil {
    protected static final double getDirection2D(double d, double d2) {
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
    protected static final Vec3d getVectorForRotation(double d, double d2) {
        float f = MathHelper.cos((float)(-d2 * 0.01745329238474369D - 3.1415927410125732D));
        float f2 = MathHelper.sin((float)(-d2 * 0.01745329238474369D - 3.1415927410125732D));
        float f3 = -MathHelper.cos((float)(-d * 0.01745329238474369D));
        float f4 = MathHelper.sin((float)(-d * 0.01745329238474369D));
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }

    public static Minecraft mc = Minecraft.getMinecraft();
    public static EnumActionResult placeCrystal(BlockPos blockPos) {
        blockPos.offset(EnumFacing.DOWN);
        double d = (double)blockPos.getX() + 0.5D - mc.player.posX;
        double d2 = (double)blockPos.getY() + 0.5D - mc.player.posY - 0.5D - (double)mc.player.getEyeHeight();
        double d3 = (double)blockPos.getZ() + 0.5D - mc.player.posZ;
        double d4 = getDirection2D(d3, d);
        double d5 = getDirection2D(d2, Math.sqrt(d * d + d3 * d3));
        Vec3d vec3d = getVectorForRotation(-d5, d4 - 90.0D);
        if (((ItemStack)mc.player.inventory.offHandInventory.get(0)).getItem().getClass().equals(Item.getItemById(426).getClass())) {
            return mc.playerController.processRightClickBlock(mc.player, mc.world, blockPos.offset(EnumFacing.DOWN), EnumFacing.UP, vec3d, EnumHand.OFF_HAND);
        } else if (InventoryUtil.pickItem(426) != -1) {
            InventoryUtil.setSlot(InventoryUtil.pickItem(426));
            return mc.playerController.processRightClickBlock(mc.player, mc.world, blockPos.offset(EnumFacing.DOWN), EnumFacing.UP, vec3d, EnumHand.MAIN_HAND);
        } else {
            return EnumActionResult.FAIL;
        }
    }
    public static double getRange(Vec3d a, double x, double y, double z) {
        double xl = a.x - x;
        double yl = a.y - y;
        double zl = a.z - z;
        return Math.sqrt(xl * xl + yl * yl + zl * zl);
    }

    public static boolean isReplaceable(Block block) {
        return block == Blocks.FIRE
                || block == Blocks.DOUBLE_PLANT
                || block == Blocks.VINE;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity, Vec3d vec) {
        float doubleExplosionSize = 12.0F;
        double distanceSize = getRange(vec, posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);

        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());

        double v = (1.0D - distanceSize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));
        double finalValue = 1.0;

        if (entity instanceof EntityLivingBase) {
            // we pass null as the exploder here
            //noinspection ConstantConditions
            finalValue = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
        }
        return (float) finalValue;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        Vec3d offset = new Vec3d(entity.posX, entity.posY, entity.posZ);
        return calculateDamage(posX, posY, posZ, entity, offset);
    }

    private static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        try {
            if (entity instanceof EntityPlayer) {
                EntityPlayer ep = (EntityPlayer) entity;
                DamageSource ds = DamageSource.causeExplosionDamage(explosion);
                damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

                int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
                float f = MathHelper.clamp(k, 0.0F, 20.0F);
                damage = damage * (1.0F - f / 25.0F);

                if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                    damage = damage - (damage / 5);
                }

                damage = Math.max(damage, 0.0f);
                return damage;
            }
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            return damage;
        } catch (Exception ignored) {
            return getBlastReduction(entity, damage, explosion);
        }
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    public static EnumFacing enumFacing(final BlockPos blockPos) {
        final EnumFacing[] values;
        final int length = (values = EnumFacing.values()).length;
        int i = 0;
        while (i < length) {
            final EnumFacing enumFacing = values[i];
            final Vec3d vec3d = new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
            final Vec3d vec3d2 = new Vec3d(blockPos.getX() + enumFacing.getDirectionVec().getX(), blockPos.getY() + enumFacing.getDirectionVec().getY(), blockPos.getZ() + enumFacing.getDirectionVec().getZ());
            final RayTraceResult rayTraceBlocks;
            if ((rayTraceBlocks = mc.world.rayTraceBlocks(vec3d, vec3d2, false, true, false)) != null
                    && rayTraceBlocks.typeOfHit.equals(RayTraceResult.Type.BLOCK) && rayTraceBlocks.getBlockPos().equals(blockPos)) {
                return enumFacing;
            }
            i++;
        }
        if (blockPos.getY() > mc.player.posY + mc.player.getEyeHeight()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }

    public static boolean isEating() {
        return mc.player != null && (mc.player.getHeldItemMainhand().getItem() instanceof ItemFood || mc.player.getHeldItemOffhand().getItem() instanceof ItemFood) && mc.player.isHandActive();
    }

    public static boolean canSeeBlock(BlockPos p_Pos) {
        if (mc.player == null)
            return true;

        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(p_Pos.getX(), p_Pos.getY(), p_Pos.getZ()), false, true, false) != null;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public static double getVecDistance(BlockPos a, double posX, double posY, double posZ) {
        double x1 = a.getX() - posX;
        double y1 = a.getY() - posY;
        double z1 = a.getZ() - posZ;
        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }

}
