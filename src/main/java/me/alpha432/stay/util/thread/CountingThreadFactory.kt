package me.alpha432.stay.util.thread

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class CountingThreadFactory(private val prefix: String) : ThreadFactory {
    private val count = AtomicInteger(1)

    override fun newThread(r: Runnable): Thread {
        return Thread(r, "$prefix-${count.getAndIncrement()}").apply { isDaemon = true }
    }
}