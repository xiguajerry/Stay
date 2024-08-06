/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/21 上午1:21
 */

package me.alpha432.stay.event

import kotlinx.coroutines.launch
import me.alpha432.stay.client.Stay
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.util.thread.listenerScope
import net.minecraftforge.fml.common.eventhandler.Event

private typealias eventHandler = suspend (EventStage) -> Unit

inline fun<reified E: Event> Module.safeListener(noinline function: (E) -> Unit) {
    Listener.register(E::class.java, function, this)
}

object Listener {
    val listeners = mutableListOf<Pair<Pair<Class<out Event>, (Event) -> Unit>, Module>>()

    inline fun<reified E: Event> onEvent(eventStage: E) {
        try {
            listenerScope.launch {
                listeners
                    .filter { it.first.first.name == eventStage::class.java.name }
                    .filter { it.second.isEnabled || it.second.alwaysListening }
                    .forEach {
                        launch {
                            try {
                                it.first.second(eventStage)
                            } catch (e: Throwable) {
                                e.printStackTrace()
                                Stay.LOGGER.fatal("[ThreadSafety] An error has been thrown!")
                                Stay.LOGGER.fatal(e.stackTrace)
                            }
                        }
                    }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @Suppress("unchecked_cast")
    inline fun<reified E: Event> register(clazz: Class<out E>, noinline handler: (E) -> Unit, module: Module) {
        listeners.add((clazz to handler as (Event) -> Unit) to module)
    }
}