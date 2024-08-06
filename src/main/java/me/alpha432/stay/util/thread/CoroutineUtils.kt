/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/21 上午1:24
 */

package me.alpha432.stay.util.thread

import kotlinx.coroutines.*

@Suppress("EXPERIMENTAL_API_USAGE")
val loadingScope = CoroutineScope(newFixedThreadPoolContext(10, "Stay-Loading"))

@Suppress("EXPERIMENTAL_API_USAGE")
val mainScope = CoroutineScope(newSingleThreadContext("Stay-Main"))

@Suppress("EXPERIMENTAL_API_USAGE")
val listenerScope by lazy { CoroutineScope(newFixedThreadPoolContext(50, "Stay-Listener")) }

@Suppress("EXPERIMENTAL_API_USAGE")
val taskScope by lazy { CoroutineScope(newFixedThreadPoolContext(70, "Stay-Tasks")) }

val defaultScope = CoroutineScope(Dispatchers.Default)

inline val Job?.isActiveOrFalse get() = this?.isActive ?: false