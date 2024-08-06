/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/21 上午1:41
 */

package me.alpha432.stay.event

import me.alpha432.stay.client.Stay
import me.alpha432.stay.util.basement.wrapper.MinecraftWrapper.mc
import me.alpha432.stay.util.basement.wrapper.MinecraftWrapper.world
import me.alpha432.stay.util.graphics.opengl.WorldRenderPatcher
import me.alpha432.stay.util.thread.runSafeSuspend
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.init.SoundEvents
import net.minecraft.network.play.server.SPacketSoundEffect
import net.minecraft.network.play.server.SPacketSpawnObject
import net.minecraft.util.SoundCategory
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import anonymous.team.eventsystem.impl.ConnectionEvent
import anonymous.team.eventsystem.impl.CrystalSetDeadEvent
import me.alpha432.stay.util.player.CrystalDamage
import me.alpha432.stay.util.world.CrystalUtil
import me.alpha432.stay.util.world.CrystalUtils
import net.minecraftforge.client.event.InputUpdateEvent

object ListenerProcessor {
    @SubscribeEvent
    fun onEvent(event: Event) {
        Listener.onEvent(event)
    }

    @SubscribeEvent
    fun onRenderTickEvent(event: TickEvent.RenderTickEvent) {
        Stay.moduleManager!!.enabledModules.forEach { it.onRenderTick0(event) }
    }

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldEvent) {
        WorldRenderPatcher.patch(event)
    }

    @SubscribeEvent
    fun onClientDisconnect(event: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        ConnectionEvent.Disconnect.post()
    }

    @SubscribeEvent
    fun onClientConnect(event: FMLNetworkEvent.ClientConnectedToServerEvent) {
        ConnectionEvent.Connect.post()
    }

    @SubscribeEvent
    fun onLoadWorld(event: WorldEvent.Load) {
        if (event.world.isRemote) {
//            event.world.addEventListener(WorldManager)
            anonymous.team.eventsystem.impl.WorldEvent.Load.post()
        }
    }

    @SubscribeEvent
    fun onUnloadWorld(event: WorldEvent.Unload) {
        if (event.world.isRemote) {
//            event.world.removeEventListener(WorldManager)
            anonymous.team.eventsystem.impl.WorldEvent.Unload.post()
        }
    }

    @SubscribeEvent
    fun onPacketReceive(event: PacketEvent.Receive) {
        try {
            when(event.packet) {
                is SPacketSoundEffect -> {
                    if (event.packet.category == SoundCategory.BLOCKS && event.packet.sound == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                        val list = (mc.world ?: return).loadedEntityList.asSequence()
                            .filterIsInstance<EntityEnderCrystal>()
                            .filter { it.getDistanceSq(event.packet.x, event.packet.y, event.packet.z) <= 144.0 }
                            .onEach(Entity::setDead)
                            .toList()

                        if (list.isNotEmpty()) {
                            runSafeSuspend {
                                list.forEach {
                                    world!!.removeEntity(it)
                                    world!!.removeEntityDangerously(it)
                                }
                            }
                            CrystalDeadEvent(event.packet.x, event.packet.y, event.packet.z, list).post()
                            CrystalSetDeadEvent(event.packet.x, event.packet.y, event.packet.z, list).post()
                        }
                    }
                }
                is SPacketSpawnObject -> {
                    if (event.packet.type == 51) {
                        CrystalSpawnEvent((mc.world ?: return).getEntityByID(event.packet.entityID) as EntityEnderCrystal?).post()
                        mc.world.getEntityByID(event.packet.entityID)?.let {
                            anonymous.team.eventsystem.impl.CrystalSpawnEvent(event.packet.entityID,
                                CrystalDamage(it.positionVector, it.position.down(), CrystalUtil.calculateDamage(it.posX, it.posY, it.posZ, mc.player))
                            ).post()
                        }
                    }
                }
                else -> return
            }
        } catch (e: Exception) {
            Stay.LOGGER.warn("Error while receive packet at ListenerProcessor, stacktrace:", e)
        }
    }

    @SubscribeEvent
    fun onInputUpdate(event: InputUpdateEvent) {
        anonymous.team.eventsystem.impl.InputUpdateEvent(event).post()
    }
}