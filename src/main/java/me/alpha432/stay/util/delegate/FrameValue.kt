package me.alpha432.stay.util.delegate

import anonymous.team.eventsystem.AlwaysListening
import anonymous.team.eventsystem.impl.RunGameLoopEvent
import anonymous.team.eventsystem.listener
import me.alpha432.stay.util.counting.TickTimer
import me.alpha432.stay.util.counting.TimeUnit
import kotlin.reflect.KProperty

class FrameValue<T>(private val block: () -> T) {
    private var value0: T? = null
    private var lastUpdateFrame = 0

    val value: T
        get() = get()

    init {
        instances.add(this)
    }

    fun get(): T {
        return if (lastUpdateFrame == frame) {
            getLazy()
        } else {
            getForce()
        }
    }

    fun getLazy(): T {
        return value0 ?: getForce()
    }

    fun getForce(): T {
        val value = block.invoke()
        value0 = value
        lastUpdateFrame = frame

        return value
    }

    fun updateLazy() {
        value0 = null
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return get()
    }

    private companion object : AlwaysListening {
        val instances = ArrayList<FrameValue<*>>()
        val timer = TickTimer(TimeUnit.SECONDS)

        var frame = 0

        init {
            listener<RunGameLoopEvent.Render> {
                frame++

                if (timer.tick(1L)) {
                    frame = 0
                    instances.forEach {
                        it.updateLazy()
                    }
                }
            }
        }
    }
}