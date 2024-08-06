/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/14 下午11:10
 */

package anonymous.team.eventsystem.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.world.World

interface Nameable {
    val name: CharSequence
    val nameAsString: String
        get() = name.toString()
}

interface Helper {
    val mc: Minecraft
        get() = Minecraft.getMinecraft()

    val player: EntityPlayerSP?
        get() = mc.player

    val world: World?
        get() = mc.world
}