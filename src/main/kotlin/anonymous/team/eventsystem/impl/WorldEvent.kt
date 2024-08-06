/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/14 下午11:10
 */

package anonymous.team.eventsystem.impl

import net.minecraft.block.state.IBlockState
import net.minecraft.util.math.BlockPos
import anonymous.team.eventsystem.Event
import anonymous.team.eventsystem.EventBus
import anonymous.team.eventsystem.IEventPosting

sealed class WorldEvent : Event {
    object Unload : WorldEvent(), IEventPosting by EventBus()
    object Load : WorldEvent(), IEventPosting by EventBus()

    sealed class Entity(val entity: net.minecraft.entity.Entity) : WorldEvent() {
        class Add(entity: net.minecraft.entity.Entity) : Entity(entity), IEventPosting by Companion {
            companion object : EventBus()
        }

        class Remove(entity: net.minecraft.entity.Entity) : Entity(entity), IEventPosting by Companion {
            companion object : EventBus()
        }
    }

    class BlockUpdate(
        val pos: BlockPos,
        val oldState: IBlockState,
        val newState: IBlockState
    ) : WorldEvent(), IEventPosting by Companion {
        companion object : EventBus()
    }

    class RenderUpdate(
        val x1: Int,
        val y1: Int,
        val z1: Int,
        val x2: Int,
        val y2: Int,
        val z2: Int
    ) : WorldEvent(), IEventPosting by Companion {
        companion object : EventBus()
    }
}