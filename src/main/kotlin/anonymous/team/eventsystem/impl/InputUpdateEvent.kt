package anonymous.team.eventsystem.impl

import anonymous.team.eventsystem.Event
import anonymous.team.eventsystem.EventBus
import anonymous.team.eventsystem.IEventPosting
import anonymous.team.eventsystem.WrappedForgeEvent
import net.minecraftforge.client.event.InputUpdateEvent

class InputUpdateEvent(override val event: InputUpdateEvent) : Event, WrappedForgeEvent, IEventPosting by Companion {
    val movementInput
        get() = event.movementInput

    companion object : EventBus()
}