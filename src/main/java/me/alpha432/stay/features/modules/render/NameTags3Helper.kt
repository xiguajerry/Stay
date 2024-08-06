/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午3:38
 */

package me.alpha432.stay.features.modules.render

import me.alpha432.stay.event.Render3DEvent
import me.alpha432.stay.event.safeListener
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.ApplyModule
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.util.math.Vec3d

@ApplyModule
object NameTags3Helper: Module("NameTags3\$\$Helper\$\$\$", "", Category.VISUAL, true, true, true) {
    @JvmStatic
    var partialTicks = 0.0f; private set
    var camPos: Vec3d = Vec3d.ZERO; private set

    init {
        onTick {
            partialTicks = if (mc.isGamePaused) mc.renderPartialTicksPaused else mc.renderPartialTicks
        }

        safeListener<Render3DEvent>() {
            val entity = mc.renderViewEntity ?: mc.player
            val ticks = partialTicks
            val x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ticks
            val y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ticks
            val z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ticks
            val camOffset = ActiveRenderInfo.getCameraPosition()

            camPos = Vec3d(x + camOffset.x, y + camOffset.y, z + camOffset.z)
        }
    }
}
