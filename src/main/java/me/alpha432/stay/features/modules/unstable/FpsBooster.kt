/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/18 上午11:54
 */

package me.alpha432.stay.features.modules.unstable

import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.util.delegate.setting
import net.minecraft.client.Minecraft
import anonymous.team.eventsystem.impl.TickEvent
import anonymous.team.eventsystem.safeListener

@ApplyModule
object FpsBooster : Module("Fps Bypass", "LOL", Category.UNSTABLE, true, false, false) {
    private val mode by setting("Mode", BoostMode.ADD)
    private val boost by setting("Boost", 1000, 0..2000)

    var currentFps = mc.fpsCounter

    @Suppress("unused")
    private enum class BoostMode {
        ADD, SET
    }

    init {
        safeListener<TickEvent.Pre> {
            if (mode == BoostMode.ADD) {

                if (Minecraft.getSystemTime() >= mc.debugUpdateTime + 1000) {
                    currentFps = mc.fpsCounter
                    mc.fpsCounter += boost
                } else {
                    mc.fpsCounter = currentFps + boost
                }
            } else {
                mc.fpsCounter = boost
            }
        }
    }
}