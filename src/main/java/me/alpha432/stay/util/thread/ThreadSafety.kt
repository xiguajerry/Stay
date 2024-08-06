/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/6 下午8:39
 */

package me.alpha432.stay.util.thread

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

interface ITask {
    fun invoke()
}

fun runTask(block: () -> Unit) = object : ITask {
    override fun invoke() {
        StayScope.pool.threadFactory.newThread {
            block()
        }
    }
}.also { it.invoke() }

fun runSafe(block: () -> Unit) = object : ITask {
    override fun invoke() {
        StayScope.pool.threadFactory.newThread {
            try {
                block()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}.also { it.invoke() }

fun runSuspend(scope: CoroutineScope = taskScope, block: () -> Unit) = object : ITask {
    override fun invoke() {
        scope.launch {
            block()
        }
    }
}

fun runSafeSuspend(scope: CoroutineScope = taskScope, block: () -> Unit) = object : ITask {
    override fun invoke() {
        scope.launch {
            try {
                block()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}

fun runLoop(amount: Int, delay: Long = 10, block: () -> Unit) = object : ITask {
    override fun invoke() {
        StayScope.pool.threadFactory.newThread {
            repeat(amount) {
                try {
                    block()
                    Thread.sleep(delay)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
}

fun runLoopSuspend(scope: CoroutineScope = taskScope, amount: Int, delay: Long = 10, block: () -> Unit) = object : ITask {
    override fun invoke() {
        scope.launch {
            repeat(amount) {
                try {
                    block()
                    delay(delay)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }
}