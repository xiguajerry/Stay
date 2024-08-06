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
import anonymous.team.eventsystem.EventBus
import anonymous.team.eventsystem.IEventPosting

sealed class ConnectionEvent : Event {
    object Connect : ConnectionEvent(), IEventPosting by EventBus()
    object Disconnect : ConnectionEvent(), IEventPosting by EventBus()
}