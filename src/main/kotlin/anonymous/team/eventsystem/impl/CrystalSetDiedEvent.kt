package anonymous.team.eventsystem.impl

import anonymous.team.eventsystem.Event
import anonymous.team.eventsystem.EventBus
import anonymous.team.eventsystem.IEventPosting
import net.minecraft.entity.item.EntityEnderCrystal

class CrystalSetDeadEvent(
    val x: Double,
    val y: Double,
    val z: Double,
    val crystals: List<EntityEnderCrystal>
) : Event, IEventPosting by Companion {
    companion object : EventBus()
}