/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/14 下午11:10
 */

package anonymous.team.eventsystem.utils.threads

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.math.max

private val pool0 = Runtime.getRuntime().availableProcessors().let { cpuCount ->
    val maxSize = max(cpuCount * 16, 64)
    ThreadPoolExecutor(
        cpuCount * 2,
        maxSize,
        5L,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        CountingThreadFactory("Troll Hack Pool")
    )
}

private val context0 = pool0.asCoroutineDispatcher()

/**
 * Scope for heavy loaded task in Troll Hack
 */
object EventSystemScope : CoroutineScope by CoroutineScope(context0) {
    val pool = pool0
    val context = context0
}