/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/13 上午12:26
 */

package me.alpha432.stay.features.modules.player

import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.extension.visible
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.Entity
import net.minecraft.network.play.server.SPacketDisconnect
import net.minecraft.util.text.TextComponentString
import net.minecraft.entity.item.EntityEnderCrystal
import java.awt.Desktop
import java.net.URI

@ApplyModule
object AutoPorn: Module("AutoPorn", "For Zero_One", Category.PLAYER, false, false, false) {
    private val autoMessage by setting("AutoMessage", false)
    private val message by setting("Message", "I'm sexy").visible { autoMessage }
    private val website by setting("WebSite", WebSites.PORNHUB)
    private val autolog by setting("AutoLog", false)
    private val logBy by setting("LogBy", LogBy.CRYSTALS).visible { autolog }
    private val limits by setting("Limit", 10, 0, 20).visible { autolog }

    enum class WebSites(val site: String) {
        PORNHUB("pornhub.com"),
        AVCAR("avcar.vip"),
        FOURTHREENINENINE("4399.com"),
        XYUNSO("xyunso.cc"),
        BAOYUTV("http://www.by29777.com/")
    }

    enum class LogBy(val checker: (EntityPlayerSP, List<Entity>, Int) -> Boolean) {
        CRYSTALS({ _, entities, limits -> entities.filterIsInstance<EntityEnderCrystal>().size >= limits }),
        PLAYERS({ _, entities, limits -> entities.filterIsInstance<net.minecraft.entity.player.EntityPlayer>().filter { it != mc.player }.size >= limits }),
        HEALTH({ player, _, limits -> player.health <= limits }),
    }

    override fun onTick() {
        if (logBy.checker(mc.player, mc.world.loadedEntityList, limits)) {
            mc.player.connection.sendPacket(SPacketDisconnect(TextComponentString("AutoLogged")))
            disable()
        }
    }

    override fun onEnable() {
        if (autoMessage) mc.player.sendChatMessage(message)
        Desktop.getDesktop().browse(URI(website.site))
    }
}