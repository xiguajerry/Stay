/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/18 上午11:54
 */

package me.alpha432.stay.features.modules.unstable

import kotlinx.coroutines.launch
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.util.basement.wrapper.MinecraftWrapper.player
import me.alpha432.stay.util.graphics.animations.AnimationFlag
import me.alpha432.stay.util.graphics.animations.Easing
import me.alpha432.stay.util.thread.mainScope

@ApplyModule
object GuiAnimationHelper: Module("GuiAnimationHelper", "Plz enable this", Category.UNSTABLE, true, false, true) {
    private val hotbarAnimation = AnimationFlag(Easing.OUT_CUBIC, 200.0f)

    init {
        mainScope.launch {
            while (true) {
                val currentPos = (player ?: continue).inventory.currentItem * 20.0f
                hotbarAnimation.forceUpdate(currentPos, currentPos)
            }
        }

        onDisable {
            enable()
        }
    }

    @JvmStatic
    fun updateHotbar(): Float {
        val currentPos = mc.player?.let {
            it.inventory.currentItem * 20.0f
        } ?: 0.0f

        return hotbarAnimation.getAndUpdate(currentPos)
    }
}