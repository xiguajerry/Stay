/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/14 下午11:10
 */

package anonymous.team.eventsystem

import net.minecraftforge.fml.common.eventhandler.Event

interface WrappedForgeEvent : ICancellable {
    val event: Event

    override var cancelled: Boolean
        get() = event.isCanceled
        set(value) {
            event.isCanceled = value
        }
}

interface ICancellable {
    var cancelled: Boolean

    fun cancel() {
        cancelled = true
    }
}

open class Cancellable : ICancellable {
    override var cancelled = false
        set(value) {
            field = field || value
        }
}

open class ListenerOwner : IListenerOwner {
    val listeners = ArrayList<Listener>()
    private val parallelListeners = ArrayList<ParallelListener>()

    override fun register(listener: Listener) {
        listeners.add(listener)
    }

    override fun register(listener: ParallelListener) {
        parallelListeners.add(listener)
    }

    override fun subscribe() {
        for (listener in listeners) {
            EventBus[listener.eventID].subscribe(listener)
        }
        for (listener in parallelListeners) {
            EventBus[listener.eventID].subscribe(listener)
        }
    }

    override fun unsubscribe() {
        for (listener in listeners) {
            EventBus[listener.eventID].unsubscribe(listener)
        }
        for (listener in parallelListeners) {
            EventBus[listener.eventID].unsubscribe(listener)
        }
    }
}

interface AlwaysListening : IListenerOwner {
    override fun register(listener: Listener) {
        EventBus[listener.eventID].subscribe(listener)
    }

    override fun register(listener: ParallelListener) {
        EventBus[listener.eventID].subscribe(listener)
    }

    override fun subscribe() {}

    override fun unsubscribe() {}
}

interface IListenerOwner {
    fun register(listener: Listener)

    fun register(listener: ParallelListener)

    fun subscribe()

    fun unsubscribe()
}

interface Event : IEventPosting {
    fun post() {
        post(this)
    }
}

interface IEventPosting {
    val eventBus: EventBus

    fun post(event: Any)
}