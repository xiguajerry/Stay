package anonymous.team.eventsystem.impl

import anonymous.team.eventsystem.Event
import anonymous.team.eventsystem.EventBus
import anonymous.team.eventsystem.IEventPosting
import me.alpha432.stay.util.player.CrystalDamage

class CrystalSpawnEvent(
    val entityID: Int,
    val crystalDamage: CrystalDamage
) : Event, IEventPosting by Companion {
    companion object : EventBus()
}