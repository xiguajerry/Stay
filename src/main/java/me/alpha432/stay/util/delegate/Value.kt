/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:36
 */

package me.alpha432.stay.util.delegate

import me.alpha432.stay.features.setting.Setting
import java.util.function.Predicate
import kotlin.reflect.KProperty

class Value<T>: Setting<T> {
    constructor(name: String, defaultValue: T) : super(name, defaultValue)

    constructor(name: String, defaultValue: T, min: T, max: T) : super(name, defaultValue, min, max)

    constructor(
        name: String,
        defaultValue: T,
        min: T,
        max: T,
        visibility: Predicate<T>,
        description: String
    ) : super(name, defaultValue, min, max, visibility, description)

    constructor(name: String, defaultValue: T, min: T, max: T, visibility: Predicate<T>) : super(
        name,
        defaultValue,
        min,
        max,
        visibility
    )

    constructor(name: String, defaultValue: T, visibility: Predicate<T>) : super(name, defaultValue, visibility)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = this.value

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.setValue(value)
    }
}