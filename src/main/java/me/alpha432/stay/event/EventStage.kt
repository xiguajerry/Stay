/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */
package me.alpha432.stay.event

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event

abstract class EventStage : Event, IPostable {
    var stage = 0

    constructor()

    var isCancelled = true

    constructor(stage: Int) {
        this.stage = stage
    }

    override fun post() {
        MinecraftForge.EVENT_BUS.post(this)
    }
}

interface IPostable {
    fun post()
}