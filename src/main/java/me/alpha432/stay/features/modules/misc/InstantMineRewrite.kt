/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/22 下午10:54
 */

package me.alpha432.stay.features.modules.misc

import me.alpha432.stay.event.PacketEvent
import me.alpha432.stay.event.PlayerDamageBlockEvent
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.WrappedModule
import me.alpha432.stay.features.modules.combat.AutoCev
import me.alpha432.stay.manager.ModuleManager
import me.alpha432.stay.util.counting.Timer
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.extension.visible
import me.alpha432.stay.util.graphics.animations.BlockEasingRender
import me.alpha432.stay.util.graphics.opengl.StayTessellator
import me.alpha432.stay.util.inventory.InventoryUtil
import me.alpha432.stay.util.math.MathUtil
import me.alpha432.stay.util.world.BlockUtil
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.MobEffects
import net.minecraft.inventory.ClickType
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@ApplyModule
object InstantMineRewrite: WrappedModule(name = "InstantMine+", category = Category.MISC) {
    private val haste by setting("Haste", false)
    private val ghostHand by setting("GhostHand", false)
    private val fuck by setting("Super ghost hand", false)
    private val doubleBreak by setting("DoubleBreak", false)
    private val render by setting("Render", true)
    private val red by setting("Red", 255, 0..255).visible { render }
    private val green by setting("Green", 255, 0..255).visible { render }
    private val blue by setting("Blue", 255, 0..255).visible { render }
    private val alpha by setting("Alpha", 60, 0..255).visible { render }
    private val outline by setting("Outline", false).visible { render }
    private val width by setting("LineWidth", 1.5, 0.0..5.0).visible { render && outline }
    private val movingTime by setting("MovingTime", 500, 0..2000).visible { render }
    private val fadingTime by setting("FadingTime", 2000, 0..5000).visible { render }

    private var blockEasingRender = BlockEasingRender(BlockPos(0, 0, 0), movingTime, fadingTime)
    private val godBlocks =
        listOf(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.BEDROCK)
    private var cancelStart = false
    private var empty = false
    private var facing: EnumFacing? = null
    private var breakPos: BlockPos? = null
    private var breakPos2: BlockPos? = null
    private val breakSuccess = Timer()

    private val lock = Any()

    init {
        onEnable {
            if (fullNullCheck()) {
                rend()
                return@onEnable
            }
            blockEasingRender = BlockEasingRender(BlockPos(0, 0, 0), movingTime, fadingTime)
            blockEasingRender.reset()
        }

        onDisable {
            if (fullNullCheck()) {
                return@onDisable
            }
            rend()
            blockEasingRender.reset()

            if (haste) {
                mc.player.removePotionEffect(MobEffects.HASTE)
            }
        }

        onRender3D {
            if (fullNullCheck()) {
                rend()
                return@onRender3D
            }
            if (!isEnabled) {
                rend()
                return@onRender3D
            }
            blockEasingRender.begin()
            var (bb, _) = blockEasingRender.getBBAndVec3dUpdate()
            val interpolateEntity: Vec3d = MathUtil.interpolateEntity(Module.mc.player, Module.mc.renderPartialTicks)
            bb = bb.offset(-interpolateEntity.x, -interpolateEntity.y, -interpolateEntity.z)
//                        RenderUtil.drawBBBox(bb, color, renderA)
            GlStateManager.pushMatrix()
            StayTessellator.drawBoxTest(
                bb.minX.toFloat(),
                bb.minY.toFloat(),
                bb.minZ.toFloat(),
                bb.maxX.toFloat() - bb.minX.toFloat(),
                bb.maxY.toFloat() - bb.minY.toFloat(),
                bb.maxZ.toFloat() - bb.minZ.toFloat(),
                red,
                green,
                blue,
                alpha,
                63
            )
            if (outline) {
                StayTessellator.drawBoundingBox(
                    bb,
                    width.toFloat(),
                    red,
                    green,
                    blue,
                    255
                )
            }
            GlStateManager.popMatrix()
        }

        onRenderTick {
            if (fullNullCheck()) {
                rend()
                return@onRenderTick
            }
            if (!cancelStart) {
                return@onRenderTick
            }
            if ((ModuleManager.getModuleByClass(AutoCev::class.java) ?: return@onRenderTick).isEnabled) {
                return@onRenderTick
            }
            if (InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE) == -1) {
                return@onRenderTick
            }
            if (!fuck) {
                if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
                    return@onRenderTick
                }
            }
            if (doubleBreak && breakPos2 != null && facing != null) {
                synchronized(lock) {
                    val slotMains = mc.player.inventory.currentItem
                    if (mc.world.getBlockState(breakPos2!!).block !== Blocks.AIR
                        && InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1
                    ) {
                        if (mc.world.getBlockState(breakPos2!!).block === Blocks.OBSIDIAN) {
                            if (!breakSuccess.passedMs(1234L)) {
                                return@onRenderTick
                            }
                        }
                        mc.player.connection.sendPacket(CPacketHeldItemChange(InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE)))
                        mc.player.connection.sendPacket(
                            CPacketPlayerDigging(
                                CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                                breakPos2!!,
                                facing!!
                            )
                        )
                    }
                    if (mc.world.getBlockState(breakPos2!!).block == Blocks.AIR) {
                        breakPos2 = null
                        mc.player.connection.sendPacket(CPacketHeldItemChange(slotMains))
                    }
                }

            }
            if (godBlocks.contains(mc.world.getBlockState(breakPos ?: return@onRenderTick).block)) {
                return@onRenderTick
            }
            if (ghostHand
                && (fuck || InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1)
                && InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE) != -1
            ) {
                val slotMain = mc.player.inventory.currentItem
                if (mc.world.getBlockState(breakPos ?: return@onRenderTick).block === Blocks.OBSIDIAN) {
                    if (!breakSuccess.passedMs(1234L)) {
                        return@onRenderTick
                    }
                    if (fuck) {
                        if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
                            if (breakPos != null && facing != null) {
                                synchronized(lock) {
                                    (9..35).forEach { i ->
                                        if (mc.player.inventory.getStackInSlot(i).getItem() === Items.DIAMOND_PICKAXE) {
                                            mc.playerController.windowClick(
                                                mc.player.inventoryContainer.windowId,
                                                i,
                                                mc.player.inventory.currentItem,
                                                ClickType.SWAP,
                                                mc.player
                                            )
                                            mc.playerController.updateController()
                                            mc.player.connection.sendPacket(
                                                CPacketPlayerDigging(
                                                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                                                    breakPos!!,
                                                    facing!!
                                                )
                                            )
                                            mc.playerController.windowClick(
                                                mc.player.inventoryContainer.windowId,
                                                i,
                                                mc.player.inventory.currentItem,
                                                ClickType.SWAP,
                                                mc.player
                                            )
                                            mc.playerController.updateController()
                                            return@onRenderTick
                                        }
                                    }
                                }
                            }
                            return@onRenderTick
                        }
                    }
                    mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE)
                    mc.playerController.updateController()
                    if (breakPos != null && facing != null) {
                        synchronized(lock) {
                            mc.player.connection.sendPacket(
                                CPacketPlayerDigging(
                                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                                    breakPos!!,
                                    facing!!
                                )
                            )
                        }
                    }
                    mc.player.inventory.currentItem = slotMain
                    mc.playerController.updateController()
                    return@onRenderTick
                }
                if (fuck) {
                    if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
                        if (breakPos != null && facing != null) {
                            synchronized(lock) {
                                for (i in 9..34) {
                                    if (mc.player.inventory.getStackInSlot(i).getItem() === Items.DIAMOND_PICKAXE) {
                                        mc.playerController.windowClick(
                                            mc.player.inventoryContainer.windowId,
                                            i,
                                            mc.player.inventory.currentItem,
                                            ClickType.SWAP,
                                            mc.player
                                        )
                                        mc.playerController.updateController()
                                        mc.player.connection.sendPacket(
                                            CPacketPlayerDigging(
                                                CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                                                breakPos!!,
                                                facing!!
                                            )
                                        )
                                        mc.playerController.windowClick(
                                            mc.player.inventoryContainer.windowId,
                                            i,
                                            mc.player.inventory.currentItem,
                                            ClickType.SWAP,
                                            mc.player
                                        )
                                        mc.playerController.updateController()
                                        return@onRenderTick
                                    }
                                }
                            }
                        }
                        return@onRenderTick
                    }
                }
                mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE)
                mc.playerController.updateController()
                if (breakPos != null && facing != null) {
                    synchronized(lock) {
                        mc.player.connection.sendPacket(
                            CPacketPlayerDigging(
                                CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                                breakPos!!,
                                facing!!
                            )
                        )
                    }
                }
                mc.player.inventory.currentItem = slotMain
                mc.playerController.updateController()
                return@onRenderTick
            }
            if (breakPos != null && facing != null) {
                synchronized(lock) {
                    mc.player.connection.sendPacket(
                        CPacketPlayerDigging(
                            CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos!!,
                            facing!!
                        )
                    )
                    if (doubleBreak) {
                        mc.player.connection.sendPacket(
                            CPacketPlayerDigging(
                                CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos2!!,
                                facing!!
                            )
                        )
                    }
                }
            }
        }
    }

    private fun rend() {
        empty = false
        cancelStart = false
        breakPos = null
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.Send) {
        if (fullNullCheck()) {
//            rend()
            return
        }
        if (!isEnabled) {
//            rend()
            return
        }
        if (event.packet !is CPacketPlayerDigging) {
            return
        }
        val packet = event.packet
        if (packet.action != CPacketPlayerDigging.Action.START_DESTROY_BLOCK) {
            return
        }
        event.isCanceled = cancelStart
    }

    fun onDestroy(pos: BlockPos) {
        if (fullNullCheck()) {
            return
        }
        if (!BlockUtil.canBreak(pos)) {
            return
        }
        if (breakPos != null) {
            if (breakPos == pos) {
                return
            }
        }
        rend()
        blockEasingRender.updatePos(pos)
        blockEasingRender.forceResetSize(0f)
        empty = false
        cancelStart = false
        synchronized(lock) {
            breakPos = pos
            breakSuccess.reset()
            facing = EnumFacing.UP
        }
        breakPos = pos
        breakSuccess.reset()
        facing = EnumFacing.UP
        if (breakPos == null) {
            return
        }
        synchronized(lock) {
            mc.player.swingArm(EnumHand.MAIN_HAND)
            mc.player.connection.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
                    breakPos!!,
                    facing!!
                )
            )
            cancelStart = true
            mc.player.connection.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    breakPos!!,
                    facing!!
                )
            )
        }
    }

    @SubscribeEvent
    fun onPlayerDamageBlock(event: PlayerDamageBlockEvent) {
        if (fullNullCheck()) {
            rend()
            return
        }
        if (!isEnabled) {
            return
        }
        if (!BlockUtil.canBreak(event.pos)) {
            return
        }
        if (breakPos != null) {
            if (breakPos == event.pos) {
                return
            }
        }
        blockEasingRender.updatePos(event.pos)
        blockEasingRender.forceResetSize(0f)
        empty = false
        cancelStart = false
        breakPos2 = breakPos
        synchronized(lock) {
            breakPos = event.pos
            breakSuccess.reset()
            facing = event.facing
        }
        if (breakPos == null || facing == null) {
            return
        }
        synchronized(lock) {
            mc.player.swingArm(EnumHand.MAIN_HAND)
            mc.player.connection.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
                    breakPos!!,
                    facing!!
                )
            )
            cancelStart = true
            mc.player.connection.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
                    breakPos!!,
                    facing!!
                )
            )
        }
        event.isCanceled = true
    }

    fun isBlockMining(pos1: BlockPos, pos2: BlockPos): Boolean {
        return (this.breakPos == pos1 && this.breakPos2 == pos2) || (this.breakPos2 == pos1 && this.breakPos == pos2)
    }

    override fun getDisplayInfo(): String {
        return if (breakPos == null) "Finding..." else "Breaking"
    }
}