/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/5 下午2:26
 */

package me.alpha432.stay.manager

import me.alpha432.stay.client.Stay
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.manager.NotificationType.*
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.graphics.animations.AnimationFlag
import me.alpha432.stay.util.graphics.animations.Easing
import net.minecraft.client.gui.Gui
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.abs

@ApplyModule
object NotificationManager: Module("Notification", "", Category.CLIENT, true, false, false) {
    private val backgroundAlpha by setting("BG Alpha", 127, 0..255)

    private val notificationQueue: MutableList<NotificationInfo> = CopyOnWriteArrayList()

    init {
        onRender2D {
            if (fullNullCheck()) return@onRender2D
            notificationQueue.forEachIndexed { index, notificationInfo ->
                val xOffset = Stay.fontRenderer!!.getStringWidth(notificationInfo.message)
                val x = notificationInfo.animationX.getAndUpdate(if (!notificationInfo.isEnded) Stay.textManager!!.scaledWidth - xOffset - 10f else Stay.textManager!!.scaledWidth.toFloat())
                notificationInfo.currentX = x
                val y = notificationInfo.animationY.getAndUpdate(Stay.textManager!!.scaledHeight - 20 - (index * 20f)).toInt() - 5
                val height = Stay.fontRenderer!!.fontHeight + 10
                Gui.drawRect(
                    x.toInt(),
                    y,
                    (x + xOffset + 20).toInt(),
                    y + height, Color(0, 0, 0, backgroundAlpha).rgb
                )
                Stay.fontRenderer!!.drawStringWithShadow(notificationInfo.message,
                    x + 5,
                    y + (Stay.fontRenderer!!.fontHeight) / 2f,
                    when(notificationInfo.type) {
                        INFO -> Color.GREEN.rgb
                        WARNING -> Color.ORANGE.rgb
                        ERROR -> Color.RED.rgb
                    }
                )
                Gui.drawRect(x.toInt(),
                    y,
                    x.toInt() + 2,
                    y + height,
                    when(notificationInfo.type) {
                        INFO -> Color.GREEN.rgb
                        WARNING -> Color.ORANGE.rgb
                        ERROR -> Color.RED.rgb
                    }
                )
//                val ms = (System.currentTimeMillis() - notificationInfo.startTime).toString()
            }

            notificationQueue.filter { (System.currentTimeMillis() - it.startTime) >= it.length }.forEach { it.isEnded = true }
            notificationQueue.filter { abs(it.currentX - Stay.fontRenderer!!.scaledWidth) <= 0.1f && it.isEnded }.forEach { it.shouldRemove = true }
            notificationQueue.removeIf { it.shouldRemove }
        }
    }

    fun push(message: Any?, type: NotificationType, length: Long) {
        val stuff = NotificationInfo(message.toString(), type, length, System.currentTimeMillis())
        stuff.animationX.forceUpdate(Stay.textManager!!.scaledWidth.toFloat(), Stay.textManager!!.scaledWidth.toFloat())
        stuff.animationY.forceUpdate(Stay.textManager!!.scaledHeight.toFloat(), Stay.textManager!!.scaledHeight.toFloat())
        notificationQueue.add(stuff)
    }

    data class NotificationInfo(val message: String, val type: NotificationType, val length: Long, val startTime: Long) {
        val animationX = AnimationFlag(Easing.OUT_QUINT, 700f)
        val animationY = AnimationFlag(Easing.OUT_QUINT, 400f)

        var isEnded = false
        var shouldRemove = false
        var currentX = 0f
    }
}

enum class NotificationType {
    INFO, WARNING, ERROR
}