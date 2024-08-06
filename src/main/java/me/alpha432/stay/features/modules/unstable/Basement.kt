/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/18 上午11:55
 */
@file:Suppress("Deprecation")
package me.alpha432.stay.features.modules.unstable

import anonymous.team.eventsystem.impl.ClientChangeEvent
import anonymous.team.eventsystem.safeListener
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.manager.NotificationManager
import me.alpha432.stay.manager.NotificationType
import me.alpha432.stay.util.basement.natives.UnsafeFeature
import me.alpha432.stay.util.basement.natives.UnsafeUtils
import me.alpha432.stay.util.delegate.setting

@OptIn(UnsafeFeature::class)
@ApplyModule
object Basement : Module("ThreadController", "", Category.UNSTABLE, true, false, false) {
    private val using = setting("Use", false)
    private val clear = setting("ClearMemory", false)
    private val maxPriority = setting("ThreadPriority", false)

    init {
        safeListener<ClientChangeEvent.SettingChange<Boolean>>(true) { event ->
            if (event.setting == using) {
                if (using.value) NotificationManager.push("YOU ARE USING AN UNSAFE API. ENSURE WHAT YOU ARE DOING.", NotificationType.WARNING, 10000)
            }
            if (using.value) {
                if (event.setting == clear) {
                    event.cancel()
                    clear.value = false
                    UnsafeUtils.forceReleaseMemory(true)
                }
                if (event.setting == maxPriority) {
                    event.cancel()
                    maxPriority.value = false
                    Thread.currentThread().priority = Int.MAX_VALUE
                }
            }
        }
    }
}