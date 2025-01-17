/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:17
 */

package me.alpha432.stay.util.graphics.color

import me.alpha432.stay.util.math.MathUtils
import kotlin.math.max
import kotlin.math.roundToInt

class ColorGradient(vararg stops: Stop) {
    private val colorArray = stops.apply { sortBy { it.value } }

    fun get(valueIn: Float): ColorRGB {
        if (colorArray.isEmpty()) return ColorRGB(255, 255, 255)
        var prevStop = colorArray.last()
        var nextStop = colorArray.last()
        for ((index, pair) in colorArray.withIndex()) {
            if (pair.value < valueIn) continue
            prevStop = if (pair.value == valueIn) pair
            else colorArray[max(index - 1, 0)]
            nextStop = pair
            break
        }
        if (prevStop == nextStop) return prevStop.color
        val r = MathUtils.convertRange(
            valueIn,
            prevStop.value,
            nextStop.value,
            prevStop.color.r.toFloat(),
            nextStop.color.r.toFloat()
        ).roundToInt()
        val g = MathUtils.convertRange(
            valueIn,
            prevStop.value,
            nextStop.value,
            prevStop.color.g.toFloat(),
            nextStop.color.g.toFloat()
        ).roundToInt()
        val b = MathUtils.convertRange(
            valueIn,
            prevStop.value,
            nextStop.value,
            prevStop.color.b.toFloat(),
            nextStop.color.b.toFloat()
        ).roundToInt()
        val a = MathUtils.convertRange(
            valueIn,
            prevStop.value,
            nextStop.value,
            prevStop.color.a.toFloat(),
            nextStop.color.a.toFloat()
        ).roundToInt()
        return ColorRGB(r, g, b, a)
    }

    class Stop(val value: Float, val color: ColorRGB)
}