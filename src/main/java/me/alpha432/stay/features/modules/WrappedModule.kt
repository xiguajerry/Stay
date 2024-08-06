package me.alpha432.stay.features.modules

import anonymous.team.eventsystem.AlwaysListening
import anonymous.team.eventsystem.IListenerOwner
import anonymous.team.eventsystem.utils.Helper
import anonymous.team.eventsystem.utils.Nameable

open class WrappedModule(
 override val name: CharSequence,
 description: CharSequence = "",
 category: Category,
 hasListener: Boolean = true,
 hidden: Boolean = false,
 alwaysListening: Boolean = false
) : Module(name.toString(), description.toString(), category, hasListener, hidden, alwaysListening), Nameable, Helper