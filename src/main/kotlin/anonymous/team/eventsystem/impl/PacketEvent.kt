/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/14 下午11:10
 */

package anonymous.team.eventsystem.impl

import anonymous.team.eventsystem.*
import net.minecraft.network.Packet

sealed class PacketEvent(val packet: Packet<*>) : Event {
    class Receive(packet: Packet<*>) : PacketEvent(packet), ICancellable by Cancellable(), IEventPosting by Companion {
        companion object : EventBus()
    }

    class PostReceive(packet: Packet<*>) : PacketEvent(packet), IEventPosting by Companion {
        companion object : EventBus()
    }

    class Send(packet: Packet<*>) : PacketEvent(packet), ICancellable by Cancellable(), IEventPosting by Companion {
        companion object : EventBus()
    }

    class PostSend(packet: Packet<*>) : PacketEvent(packet), IEventPosting by Companion {
        companion object : EventBus()
    }
}
