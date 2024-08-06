/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/5 上午9:42
 */
@file:Suppress
package me.alpha432.stay.features.modules.render

import com.mojang.authlib.GameProfile
import me.alpha432.stay.event.PacketEvent
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.graphics.opengl.StayTessellator
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.model.ModelBase
import net.minecraft.client.model.ModelPlayer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.server.SPacketEntityStatus
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11
import java.awt.Color

@ApplyModule
object PopChams: Module("PopChams", "", Category.VISUAL, true, false, false) {
    private val self by setting("Self", false)
    private val rL by setting("RedLine", 255, 0, 255)
    private val gL by setting("GreenLine", 26, 0, 255)
    private val bL by setting("BlueLine", 42, 0, 255)
    private val aL by setting("AlphaLine", 42, 0, 255)
    private val rF by setting("RedFill", 255, 0, 255)
    private val gF by setting("GreenFill", 26, 0, 255)
    private val bF by setting("BlueFill", 42, 0, 255)
    private val aF by setting("AlphaFill", 42, 0, 255)
    private val fadeStart by setting("FadeStart", 200, 0, 3000)
    private val fadeTime by setting("FadeStart", 0.5, 0.0, 2.0)
    private val onlyOneEsp by setting("OnlyOneEsp", true)
    val rainbow by setting("Rainbow", false)
    var player: EntityOtherPlayerMP? = null
    private var playerModel: ModelPlayer? = null
    private var startTime: Long? = null
    private var alphaFill = 0.0
    private var alphaLine = 0.0
    private val lock = Any()

    @SubscribeEvent
    fun onPacketReceive(event: PacketEvent.Receive) {
        if (event.packet is SPacketEntityStatus) {
            val packet = event.packet
            if (packet.opCode == 35.toByte() && (self || packet.getEntity(
                    mc.world
                ).getEntityId() != mc.player.getEntityId())
            ) {
                val profile = GameProfile(mc.player.uniqueID, "")
                EntityOtherPlayerMP(mc.world, profile).also { player = it }
                    .copyLocationAndAnglesFrom(packet.getEntity(mc.world))
                playerModel = ModelPlayer(0.0f, false)
                startTime = System.currentTimeMillis()
                synchronized(lock) {
                    if (playerModel != null) {
                        val playerModel1: ModelPlayer = playerModel as ModelPlayer
                        playerModel1.bipedHead.showModel = false
                        playerModel1.bipedBody.showModel = false
                        playerModel1.bipedLeftArmwear.showModel = false
                        playerModel1.bipedLeftLegwear.showModel = false
                        playerModel1.bipedRightArmwear.showModel = false
                        playerModel1.bipedRightLegwear.showModel = false
                        alphaFill = aF.toDouble()
                        alphaLine = aL.toDouble()
                    }
                }
            }
        }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (onlyOneEsp) {
            if (player == null || mc.world == null || mc.player == null) {
                return
            }
            GL11.glLineWidth(1.0f)
            val lineColorS = Color(
                rL,
                bL, gL, aL
            )
            val fillColorS = Color(
                rF,
                bF, gF, aF
            )
            var lineA = lineColorS.alpha
            var fillA = fillColorS.alpha
            val time = System.currentTimeMillis() - startTime!! - (fadeStart).toLong()
            if (System.currentTimeMillis() - startTime!! > (fadeStart).toLong()) {
                var normal: Double =
                    this.normalize(time.toDouble(), 0.0, (fadeTime as Number).toDouble())
                normal = MathHelper.clamp(normal, 0.0, 1.0)
                normal = -normal + 1.0
                lineA *= normal.toInt()
                fillA *= normal.toInt()
            }
            val lineColor: Color = newAlpha(lineColorS, lineA)
            val fillColor: Color = newAlpha(fillColorS, fillA)
            if (player != null && playerModel != null) {
                val player1: EntityOtherPlayerMP = player!!
                val playerModel1: ModelPlayer = playerModel!!
                StayTessellator.prepareGL()
                GL11.glPushAttrib(1048575)
                GL11.glEnable(2881)
                GL11.glEnable(2848)
                if (alphaFill > 1.0) {
                    alphaFill -= fadeTime
                }
                val fillFinal = Color(fillColor.red, fillColor.green, fillColor.blue, alphaFill.toInt())
                if (alphaLine > 1.0) {
                    alphaLine -= fadeTime
                }
                val outlineFinal = Color(lineColor.red, lineColor.green, lineColor.blue, alphaLine.toInt())
                glColor(fillFinal)
                GL11.glPolygonMode(1032, 6914)
                renderEntity(
                    player1,
                    playerModel1,
                    player1.limbSwing,
                    player1.limbSwingAmount,
                    player1.ticksExisted.toFloat(),
                    player1.rotationYawHead,
                    player1.rotationPitch,
                    1.0f
                )
                glColor(outlineFinal)
                GL11.glPolygonMode(1032, 6913)
                renderEntity(
                    player1,
                    playerModel1,
                    player1.limbSwing,
                    player1.limbSwingAmount,
                    player1.ticksExisted.toFloat(),
                    player1.rotationYawHead,
                    player1.rotationPitch,
                    1.0f
                )
                GL11.glPolygonMode(1032, 6914)
                GL11.glPopAttrib()
                StayTessellator.releaseGL()
            }
        }
    }

    private fun normalize(value: Double, min: Double, max: Double): Double {
        return (value - min) / (max - min)
    }

    private fun renderEntity(
        entity: EntityLivingBase,
        modelBase: ModelBase,
        limbSwing: Float,
        limbSwingAmount: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float,
        scale: Float
    ) {
        if (mc?.getRenderManager() != null) {
            val partialTicks: Float = mc.renderPartialTicks
            val x: Double = entity.posX - mc.getRenderManager().viewerPosX
            var y: Double = entity.posY - mc.getRenderManager().viewerPosY
            val z: Double = entity.posZ - mc.getRenderManager().viewerPosZ
            GlStateManager.pushMatrix()
            if (entity.isSneaking) {
                y -= 0.125
            }
            val interpolateRotation = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks)
            val interpolateRotation2 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks)
//            var var10000 = interpolateRotation2 - interpolateRotation
//            var10000 = entity.field_70127_C + (entity.rotationPitch - entity.field_70127_C) * partialTicks
            renderLivingAt(x, y, z)
            val f8 = handleRotationFloat(entity, partialTicks)
            prepareRotations(entity)
            val f9 = prepareScale(entity, scale)
            GlStateManager.enableAlpha()
            modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks)
            modelBase.setRotationAngles(
                limbSwing,
                limbSwingAmount,
                f8,
                entity.rotationYaw,
                entity.rotationPitch,
                f9,
                entity
            )
            modelBase.render(
                entity,
                limbSwing,
                limbSwingAmount,
                f8,
                entity.rotationYaw,
                entity.rotationPitch,
                f9
            )
            GlStateManager.popMatrix()
        }
    }

    fun prepareTranslate(entityIn: EntityLivingBase?, x: Double, y: Double, z: Double) {
        renderLivingAt(
            x - mc.getRenderManager().viewerPosX,
            y - mc.getRenderManager().viewerPosY,
            z - mc.getRenderManager().viewerPosZ
        )
    }

    private fun renderLivingAt(x: Double, y: Double, z: Double) {
        GlStateManager.translate(x.toFloat(), y.toFloat(), z.toFloat())
    }

    private fun prepareScale(entity: EntityLivingBase, scale: Float): Float {
        GlStateManager.enableRescaleNormal()
        GlStateManager.scale(-1.0f, -1.0f, 1.0f)
        val widthX: Double = entity.renderBoundingBox.maxX - entity.renderBoundingBox.minX
        val widthZ: Double = entity.renderBoundingBox.maxZ - entity.renderBoundingBox.minZ
        GlStateManager.scale(
            scale.toDouble() + widthX,
            (scale * entity.height).toDouble(),
            scale.toDouble() + widthZ
        )
        val f = 0.0625f
        GlStateManager.translate(0.0f, -1.501f, 0.0f)
        return 0.0625f
    }

    private fun prepareRotations(entityLivingBase: EntityLivingBase) {
        GlStateManager.rotate(180.0f - entityLivingBase.rotationYaw, 0.0f, 1.0f, 0.0f)
    }

    private fun interpolateRotation(prevYawOffset: Float, yawOffset: Float, partialTicks: Float): Float {
        var f: Float = yawOffset - prevYawOffset
        while (f < -180.0f) {
            f += 360.0f
        }
        while (f >= 180.0f) {
            f -= 360.0f
        }
        return prevYawOffset + partialTicks * f
    }

    private fun newAlpha(color: Color, alpha: Int): Color {
        return Color(color.red, color.green, color.blue, alpha)
    }

    private fun glColor(color: Color) {
        GL11.glColor4f(
            color.red.toFloat() / 255.0f,
            color.green.toFloat() / 255.0f,
            color.blue.toFloat() / 255.0f,
            color.alpha.toFloat() / 255.0f
        )
    }

    private fun handleRotationFloat(livingBase: EntityLivingBase?, partialTicks: Float): Float {
        return 0.0f
    }
}