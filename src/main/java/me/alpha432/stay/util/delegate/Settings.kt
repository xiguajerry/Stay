/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:37
 */

package me.alpha432.stay.util.delegate

import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.setting.Bind
import me.alpha432.stay.util.extension.visible

fun Module.setting(name: String, defaultValue: Int, range: IntRange): Value<Int> {
    return Value(name, defaultValue, range.first, range.last).also { register(it) }
}

fun Module.setting(name: String, defaultValue: Float, range: ClosedFloatingPointRange<Float>): Value<Float> {
    return Value(name, defaultValue, range.start, range.endInclusive).also { register(it) }
}

fun Module.setting(name: String, defaultValue: Double, range: ClosedFloatingPointRange<Double>): Value<Double> {
    return Value(name, defaultValue, range.start, range.endInclusive).also { register(it) }
}

fun Module.setting(name: String, defaultValue: Boolean): Value<Boolean> {
    return Value(name, defaultValue).also { register(it) }
}

fun Module.setting(name: String, bind: Bind): Value<Bind> {
    return Value(name, bind).also { register(it) }
}

fun Module.setting(name: String, stringIn: String): Value<String> {
    return Value(name, stringIn).also { register(it) }
}

fun Module.setting(name: String, defaultValue: Boolean, visibility: (Boolean) -> Boolean): Value<Boolean> {
    return Value(name, defaultValue).visible(visibility).also { register(it) }
}

fun Module.setting(name: String, defaultValue: Int, range: IntRange, visibility: (Int) -> Boolean): Value<Int> {
    return Value(name, defaultValue, range.first, range.last).visible(visibility).also { register(it) }
}

fun<T> Module.setting(name: String, defaultValue: T, minValue: T, maxValue: T): Value<T> {
    return Value(name, defaultValue, minValue, maxValue).also { register(it) }
}

fun<T: Enum<T>> Module.setting(name: String, defaultValue: T): Value<T> {
    return Value(name, defaultValue).also { register(it) }
}

