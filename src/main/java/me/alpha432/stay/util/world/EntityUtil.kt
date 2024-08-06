/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:21
 */

package me.alpha432.stay.util.world

import net.minecraft.block.BlockLiquid
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.EnumCreatureType
import net.minecraft.entity.monster.EntityEnderman
import net.minecraft.entity.monster.EntityIronGolem
import net.minecraft.entity.monster.EntityPigZombie
import net.minecraft.entity.passive.EntityAmbientCreature
import net.minecraft.entity.passive.EntitySquid
import net.minecraft.entity.passive.EntityWolf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.floor
import kotlin.math.sqrt


object EntityUtil {
    var mc = Minecraft.getMinecraft()
    fun mobTypeSettings(e: Entity, mobs: Boolean, passive: Boolean, neutral: Boolean, hostile: Boolean): Boolean {
        return mobs && (passive && isPassiveMob(e) || neutral && isCurrentlyNeutral(e) || hostile && isMobAggressive(e))
    }

    fun isPassiveMob(e: Entity): Boolean {
        return e is EntityAgeable || e is EntityAmbientCreature || e is EntitySquid
    }

    fun isCurrentlyNeutral(entity: Entity): Boolean {
        return isNeutralMob(entity) && !isMobAggressive(entity)
    }

    fun getInterpolatedAmount(entity: Entity, x: Double, y: Double, z: Double): Vec3d {
        return Vec3d(
            (entity.posX - entity.lastTickPosX) * x,
            (entity.posY - entity.lastTickPosY) * y,
            (entity.posZ - entity.lastTickPosZ) * z
        )
    }

    fun getRelativeX(yaw: Float): Double {
        return MathHelper.sin(-yaw * 0.017453292f).toDouble()
    }

    fun getRelativeZ(yaw: Float): Double {
        return MathHelper.cos(yaw * 0.017453292f).toDouble()
    }

    fun getInterpolatedAmount(entity: Entity, ticks: Double): Vec3d {
        return getInterpolatedAmount(entity, ticks, ticks, ticks)
    }

    fun getInterpolatedRenderPos(entity: Entity, ticks: Float): Vec3d {
        return getInterpolatedPos(entity, ticks).subtract(
            Minecraft.getMinecraft().getRenderManager().renderPosX,
            Minecraft.getMinecraft().getRenderManager().renderPosY,
            Minecraft.getMinecraft().getRenderManager().renderPosZ
        )
    }

    fun getInterpolatedPos(entity: Entity, ticks: Float): Vec3d {
        return Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(
            getInterpolatedAmount(entity, ticks.toDouble())
        )
    }

    fun isFakeLocalPlayer(entity: Entity?): Boolean {
        return entity != null && entity.getEntityId() == -100 && Minecraft.getMinecraft().player !== entity
    }

    fun isAboveWater(entity: Entity?): Boolean {
        return isAboveWater(entity, false)
    }

    fun isAboveWater(entity: Entity?, packet: Boolean): Boolean {
        if (entity == null) return false
        val y =
            entity.posY - if (packet) 0.03 else if (isPlayer(entity)) 0.2 else 0.5 // increasing this seems to flag more in NCP but needs to be increased so the player lands on solid water
        for (x in MathHelper.floor(entity.posX) until MathHelper.ceil(entity.posX)) for (z in MathHelper.floor(entity.posZ) until MathHelper.ceil(
            entity.posZ
        )) {
            val pos = BlockPos(x, MathHelper.floor(y), z)
            if (Minecraft.getMinecraft().world.getBlockState(pos).block is BlockLiquid) return true
        }
        return false
    }

    fun calculateLookAt(px: Double, py: Double, pz: Double, me: EntityPlayer): DoubleArray {
        var dirx = me.posX - px
        var diry = me.posY - py
        var dirz = me.posZ - pz
        val len = sqrt(dirx * dirx + diry * diry + dirz * dirz)
        dirx /= len
        diry /= len
        dirz /= len
        var pitch = asin(diry)
        var yaw = atan2(dirz, dirx)

        //to degree
        pitch = pitch * 180.0 / Math.PI
        yaw = yaw * 180.0 / Math.PI
        yaw += 90.0
        return doubleArrayOf(yaw, pitch)
    }

    fun isMobAggressive(entity: Entity): Boolean {
        if (entity is EntityPigZombie) {
            // arms raised = aggressive, angry = either game or we have set the anger cooldown
            if (entity.isArmsRaised || entity.isAngry) {
                return true
            }
        } else if (entity is EntityWolf) {
            return entity.isAngry &&
                    Minecraft.getMinecraft().player != entity.owner
        } else if (entity is EntityEnderman) {
            return entity.isScreaming
        }
        return isHostileMob(entity)
    }

    fun isHostileMob(entity: Entity): Boolean {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !isNeutralMob(entity)
    }

    fun isNeutralMob(entity: Entity?): Boolean {
        return entity is EntityPigZombie ||
                entity is EntityWolf ||
                entity is EntityEnderman
    }

    fun isPassive(e: Entity?): Boolean {
        if (e is EntityWolf && e.isAngry) {
            return false
        }
        return if (e is EntityAgeable || e is EntityAmbientCreature || e is EntitySquid) {
            true
        } else e is EntityIronGolem && e.revengeTarget == null
    }

    fun isPlayer(entity: Entity?): Boolean {
        return entity is EntityPlayer
    }

    fun isLiving(e: Entity?): Boolean {
        return e is EntityLivingBase
    }

    fun getPlayerName(player: EntityPlayer): String {
        return player.gameProfile.name
    }

    val entityList: List<Entity>
        get() = Minecraft.getMinecraft().world.getLoadedEntityList()
    val localPlayerPosFloored: BlockPos
        get() = BlockPos(floor(mc.player.posX), floor(mc.player.posY), floor(mc.player.posZ))

    val Entity.isInHole: Boolean get() = isEntityInHole(this)

    fun isEntityInHole(entity: Entity): Boolean {
        val blockPos = BlockPos(floor(entity.posX), floor(entity.posY), floor(entity.posZ))
        val blockState = mc.world.getBlockState(blockPos)
        if (blockState.block !== Blocks.AIR) return false
        if (mc.world.getBlockState(blockPos.up()).block !== Blocks.AIR) return false
        if (mc.world.getBlockState(blockPos.down()).block === Blocks.AIR) return false
        val touchingBlocks = arrayOf(blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west())
        var validHorizontalBlocks = 0
        for (touching in touchingBlocks) {
            val touchingState = mc.world.getBlockState(touching)
            if (touchingState.block !== Blocks.AIR && touchingState.isFullBlock) validHorizontalBlocks++
        }
        return validHorizontalBlocks >= 4
    }

    val isPlayerInHole: Boolean
        get() {
            val blockPos = localPlayerPosFloored
            val blockState = mc.world.getBlockState(blockPos)
            if (blockState.block !== Blocks.AIR) return false
            if (mc.world.getBlockState(blockPos.up()).block !== Blocks.AIR) return false
            if (mc.world.getBlockState(blockPos.down()).block === Blocks.AIR) return false
            val touchingBlocks = arrayOf(blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west())
            var validHorizontalBlocks = 0
            for (touching in touchingBlocks) {
                val touchingState = mc.world.getBlockState(touching)
                if (touchingState.block !== Blocks.AIR && touchingState.isFullBlock) validHorizontalBlocks++
            }
            return validHorizontalBlocks >= 4
        }
}