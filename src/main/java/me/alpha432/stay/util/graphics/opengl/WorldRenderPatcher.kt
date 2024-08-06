/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午8:19
 */

package me.alpha432.stay.util.graphics.opengl

import me.alpha432.stay.event.RenderEvent
import me.alpha432.stay.event.RenderWorldEvent
import me.alpha432.stay.util.world.EntityUtil
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11

object WorldRenderPatcher {

    fun patch(event: RenderWorldEvent) {
        Minecraft.getMinecraft().profiler.startSection("cursa")
        Minecraft.getMinecraft().profiler.startSection("setup")
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.disableDepth()
        GlStateManager.glLineWidth(1f)
        val renderPos = Minecraft.getMinecraft().getRenderViewEntity()?.let {
            getInterpolatedPos(
                it,
                event.partialTicks
            )
        }
        val e = RenderEvent(StayTessellator, renderPos)
        e.resetTranslation()
        MinecraftForge.EVENT_BUS.post(e)
        Minecraft.getMinecraft().profiler.endSection()
//        Stay.moduleManager!!.enabledModules.forEach {
//            try {
//                it.onRenderWorld(e)
//            } catch (exception: Exception) {
//                NotificationManager.push("Error while running onRenderWorld!", NotificationType.ERROR, 1500)
//                exception.printStackTrace()
//            }
//        }
        Minecraft.getMinecraft().profiler.startSection("release")
        GlStateManager.glLineWidth(1f)
        GlStateManager.shadeModel(GL11.GL_FLAT)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.enableCull()
        StayTessellator.releaseGL()
        Minecraft.getMinecraft().profiler.endSection()
    }

    fun getInterpolatedPos(entity: Entity, ticks: Float): Vec3d {
        return Vec3d(
            entity.lastTickPosX,
            entity.lastTickPosY,
            entity.lastTickPosZ
        ).add(EntityUtil.getInterpolatedAmount(entity, ticks.toDouble()))
    }
}