/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/8 下午11:12
 */
//@file:Suppress("unused")
package me.alpha432.stay.event

import me.alpha432.stay.features.Feature
import me.alpha432.stay.features.setting.Setting
import me.alpha432.stay.util.math.Location
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.Entity
import net.minecraft.entity.MoverType
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.network.Packet
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event
import java.util.*

class BlockDestructionEvent(val blockPos: BlockPos) : EventStage()

@Cancelable
class BlockEvent(stage: Int, var pos: BlockPos, var facing: EnumFacing) : EventStage(stage)

@Cancelable
class ChatEvent(val msg: String) : EventStage()

@Cancelable
class ClientEvent : EventStage {
    var feature: Feature? = null
        private set
    var setting: Setting<*>? = null
        private set

    constructor(stage: Int, feature: Feature) : super(stage) {
        this.feature = feature
    }

    constructor(setting: Setting<*>) : super(2) {
        this.setting = setting
    }
}

class ConnectionEvent : EventStage {
    val uuid: UUID
    val entity: EntityPlayer?
    val name: String?

    constructor(stage: Int, uuid: UUID, name: String?) : super(stage) {
        this.uuid = uuid
        this.name = name
        entity = null
    }

    constructor(stage: Int, entity: EntityPlayer, uuid: UUID, name: String?) : super(stage) {
        this.entity = entity
        this.uuid = uuid
        this.name = name
    }
}

class DeathEvent(var player: EntityPlayer) : EventStage()

open class EntityEvent(val entity: Entity) : EventStage() {
    class EntityCollision(entity: Entity, var x: Double, var y: Double, var z: Double) : EntityEvent(entity)
}

class KeyEvent(val key: Int) : EventStage()

class KeyPressedEvent(var info: Boolean, var pressed: Boolean) : EventStage()

@Cancelable
class MoveEvent(var type: MoverType, var x: Double, var y: Double, var z: Double) : EventStage()

@Cancelable
class NoRenderEvent(a: Int) : EventStage(a)

@Cancelable
class Packet(var packet: Any, var type: Type) : Event() {
    enum class Type {
        INCOMING, OUTGOING
    }
}

open class PacketEvent(packet: Packet<*>) : EventStage() {
    val packet: Packet<*>

    init {
        this.packet = packet
    }

//    @Suppress("unchecked_cast")
//    fun <T : Packet<*>> getPacket(): T {
//        return packet as T
//    }

    @Cancelable
    class Receive(packet: Packet<*>) : PacketEvent(packet)

    @Cancelable
    class Send(packet: Packet<*>) : PacketEvent(packet)
}

@Cancelable
class PlayerDamageBlockEvent(stage: Int, var pos: BlockPos, var facing: EnumFacing) : EventStage(stage)

class PlayerJumpEvent(var motionX: Double, var motionY: Double) : EventStage()

@Cancelable
class ProcessRightClickBlockEvent(var pos: BlockPos, var hand: EnumHand, var stack: ItemStack) : EventStage()

@Cancelable
class PushEvent : EventStage {
    var entity: Entity? = null
    var x = 0.0
    var y = 0.0
    var z = 0.0
    var airbone = false

    constructor(entity: Entity, x: Double, y: Double, z: Double, airBone: Boolean) : super(0) {
        this.entity = entity
        this.x = x
        this.y = y
        this.z = z
        this.airbone = airBone
    }

    constructor(stage: Int) : super(stage)
    constructor(stage: Int, entity: Entity) : super(stage) {
        this.entity = entity
    }
}

class Render2DEvent(var partialTicks: Float, private var scaledResolution: ScaledResolution) : EventStage() {
    val screenWidth: Double
        get() = scaledResolution.scaledWidth_double
    val screenHeight: Double
        get() = scaledResolution.scaledHeight_double
}

class Render3DEvent(val partialTicks: Float) : EventStage()

class RenderEvent(private val tessellator: Tessellator, private val renderPos: Vec3d?) : EventStage() {
    fun resetTranslation() {
        setTranslation(renderPos)
    }

    private val buffer: BufferBuilder
        get() = tessellator.buffer

    private fun setTranslation(paramVec3d: Vec3d?) {
        if (paramVec3d != null) {
            buffer.setTranslation(-paramVec3d.x, -paramVec3d.y, -paramVec3d.z)
        }
    }
}

class RenderItemEvent(
    var mainX: Double, var mainY: Double, var mainZ: Double,
    var offX: Double, var offY: Double, var offZ: Double,
    var mainRAngel: Double, var mainRx: Double, var mainRy: Double, var mainRz: Double,
    var offRAngel: Double, var offRx: Double, var offRy: Double, var offRz: Double,
    var mainHandScaleX: Double, var mainHandScaleY: Double, var mainHandScaleZ: Double,
    var offHandScaleX: Double, var offHandScaleY: Double, var offHandScaleZ: Double
) : EventStage()

class RenderWorldEvent(val partialTicks: Float, pass: Int) : EventStage() {
    private val pass: Pass

    init {
        this.pass = Pass.values()[pass]
    }

    enum class Pass {
        ANAGLYPH_CYAN, ANAGLYPH_RED, NORMAL
    }
}

class TotemPopEvent(val player: EntityPlayer) : EventStage()

class TransformSideFirstPersonEvent(val enumHandSide: EnumHandSide) : EventStage()

class MotionUpdateEvent(stage: Int) : EventStage(stage)

class WorldEvent(val world: WorldClient?) : EventStage()

class CrystalDeadEvent(val x: Double, val y: Double, val z: Double, val crystals: List<EntityEnderCrystal>) : EventStage()

class CrystalSpawnEvent(val crystal: EntityEnderCrystal?) : EventStage()

