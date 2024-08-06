/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */
package me.alpha432.stay.event

import net.minecraft.client.Minecraft
import me.alpha432.stay.event.IMixinHelper

/**
 * @author SagiriXiguajerry
 * @since 8/12/21
 */
interface IMixinHelper {
    companion object {
        val mc = Minecraft.getMinecraft()
        val nullCheck = mc.player == null || mc.world == null
    }
}