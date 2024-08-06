/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:37
 */

package me.alpha432.stay.util.extension

import me.alpha432.stay.util.delegate.Value

inline fun <reified T: Any> Value<T>.visible(noinline block: (T) -> Boolean): Value<T> =
    this.also { it.setVisibility(block) }

inline fun <reified T: Any> Value<T>.limit(noinline block: (T) -> T): Value<T> {
    return this.also { it.setLimiter(block) }
}