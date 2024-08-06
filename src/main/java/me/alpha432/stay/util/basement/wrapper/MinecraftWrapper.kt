/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午8:15
 */

package me.alpha432.stay.util.basement.wrapper

import anonymous.team.eventsystem.AlwaysListening
import anonymous.team.eventsystem.impl.TickEvent
import anonymous.team.eventsystem.safeListener
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.multiplayer.WorldClient

object MinecraftWrapper: AlwaysListening {
    @get:JvmStatic
    val mc: Minecraft = Minecraft.getMinecraft()

    @get:JvmStatic
    var player: EntityPlayerSP? = mc.player
        private set

    @get:JvmStatic
    var world: WorldClient? = mc.world
        private set

    init {
        safeListener<TickEvent.Post> (Int.MAX_VALUE) {
            this@MinecraftWrapper.player = player
            this@MinecraftWrapper.world = world
        }
    }
}