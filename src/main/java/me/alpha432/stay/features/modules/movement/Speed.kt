/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/8 下午9:46
 */

package me.alpha432.stay.features.modules.movement

import me.alpha432.stay.event.MoveEvent
import me.alpha432.stay.event.MotionUpdateEvent
import me.alpha432.stay.event.safeListener
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.extension.visible
import net.minecraft.entity.Entity
import net.minecraft.init.MobEffects
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@ApplyModule
object Speed: Module("Speed", "HOPPPPPPPPPPPPPPPPPPPPPPPPPPPP", Category.MOVEMENT, true, false, false) {
    private val mode by setting("Mode", SpeedMode.NORMAL)
    private val damageBoost by setting("Damage Boost", false)
    private val hurtTime by setting("Hurt time", 10, 0..20).visible { damageBoost }
    private val boostMotion by setting("Motion Multiplier", 1.0, 1.0..10.0).visible { damageBoost }

    private var lastDist = 0.0
    private var moveSpeed = 0.0
    private var stage = 0

    init {
        safeListener<MotionUpdateEvent> onMotionUpdate@{ event ->
            if (event.stage == 1 && fullNullCheck()) {
                return@onMotionUpdate
            }

            lastDist = sqrt((mc.player.posX - mc.player.prevPosX) * (mc.player.posX - mc.player.prevPosX) + (mc.player.posZ - mc.player.prevPosZ) * (mc.player.posZ - mc.player.prevPosZ))
        }

        onTick {
            if (mc.player.hurtTime <= hurtTime) {
                if (damageBoost) {
                    mc.player.motionX *= boostMotion
                    mc.player.motionZ *= boostMotion
                }
            }
        }

        safeListener<MoveEvent> speedLoop@{ event ->
            if (fullNullCheck()) return@speedLoop
            if (mc.player.isInWater) return@speedLoop
            if (mc.player.isInLava) return@speedLoop

            if (mc.player.onGround) stage = 2

            when (stage) {
                0 -> {
                    ++stage
                    lastDist = 0.0
                }
                2 -> {
                    var motionY = 0.40123128
                    if (mc.player.onGround && mc.gameSettings.keyBindJump.isKeyDown) {
                        if (mc.player.isPotionActive(MobEffects.JUMP_BOOST)) {
                            motionY += ((mc.player.getActivePotionEffect(MobEffects.JUMP_BOOST)!!.amplifier + 1).toFloat() * 0.1f).toDouble()
                        }
                        mc.player.motionY = motionY
                        event.y = mc.player.motionY
                        moveSpeed *= if (mode == SpeedMode.NORMAL) 1.67 else 2.149
                    }
                }
                3 -> {
                    moveSpeed =
                        lastDist - (if (mode == SpeedMode.NORMAL) 0.6896 else 0.795) * (lastDist - this.getBaseMoveSpeed())
                }
                else -> {
                    if ((mc.world.getCollisionBoxes(
                            mc.player as Entity,
                            mc.player.entityBoundingBox.offset(0.0, mc.player.motionY, 0.0)
                        ).size > 0 || mc.player.collidedVertically) && stage > 0
                    ) {
                        stage = if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) 1 else 0
                    }
                    moveSpeed = lastDist - lastDist / if (mode == SpeedMode.NORMAL) 730.0 else 159.0
                }
            }
            moveSpeed =
                if (!mc.gameSettings.keyBindJump.isKeyDown && mc.player.onGround) this.getBaseMoveSpeed() else moveSpeed.coerceAtLeast(
                    this.getBaseMoveSpeed()
                )
            var n = mc.player.movementInput.moveForward.toDouble()
            var n2 = mc.player.movementInput.moveStrafe.toDouble()
            val n3 = mc.player.rotationYaw.toDouble()
            if (n == 0.0 && n2 == 0.0) {
                event.x = 0.0
                event.z = 0.0
            } else if (n != 0.0 && n2 != 0.0) {
                n *= sin(0.7853981633974483)
                n2 *= cos(0.7853981633974483)
            }
            val n4 = if (mode == SpeedMode.NORMAL) 0.993 else 0.99
            event.x =
                (n * moveSpeed * -sin(Math.toRadians(n3)) + n2 * moveSpeed * cos(Math.toRadians(n3))) * n4
            event.z =
                (n * moveSpeed * cos(Math.toRadians(n3)) - n2 * moveSpeed * -sin(Math.toRadians(n3))) * n4
            ++stage
            event.isCanceled = true
        }
    }

    private fun getBaseMoveSpeed(): Double {
        var n = 0.2873
        if (!mc.player.isPotionActive(MobEffects.SPEED)) return n
        n *= 1.0 + 0.2 * ((mc.player.getActivePotionEffect(MobEffects.SPEED)?.amplifier ?: 0) + 1).toDouble()
        return n
    }

    private enum class SpeedMode {
        NORMAL, STRICT
    }
}