/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/14 下午11:10
 */

package anonymous.team.eventsystem.impl

import anonymous.team.eventsystem.Event
import anonymous.team.eventsystem.IEventPosting
import anonymous.team.eventsystem.NamedProfilerEventBus

sealed class RunGameLoopEvent : Event {
    object Start : RunGameLoopEvent(), IEventPosting by NamedProfilerEventBus("start")
    object Tick : RunGameLoopEvent(), IEventPosting by NamedProfilerEventBus("tick")
    object Render : RunGameLoopEvent(), IEventPosting by NamedProfilerEventBus("render")
    object End : RunGameLoopEvent(), IEventPosting by NamedProfilerEventBus("end")
}