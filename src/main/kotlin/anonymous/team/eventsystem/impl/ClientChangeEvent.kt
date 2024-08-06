/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/18 上午11:59
 */

package anonymous.team.eventsystem.impl

import anonymous.team.eventsystem.*
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.setting.Setting

sealed class ClientChangeEvent : Event {
    class ModuleToggle(val module: Module, val toggleTo: Boolean) : ClientChangeEvent(), ICancellable by Cancellable(), IEventPosting by Companion {
        companion object : EventBus()
    }

    class SettingChange<T>(val setting: Setting<T>) : ClientChangeEvent(), ICancellable by Cancellable(), IEventPosting by Companion {
        companion object : EventBus()
    }
}
