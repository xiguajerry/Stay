package me.alpha432.stay.features.modules.combat

import me.alpha432.stay.client.Stay
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.misc.InstantMineRewrite
import me.alpha432.stay.manager.ModuleManager
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.extension.block
import me.alpha432.stay.util.math.vector.distanceTo
import me.alpha432.stay.util.world.BlockUtil
import net.minecraft.init.Blocks
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos

@ApplyModule
object AntiBurrowPlus : Module("AntiBurrow+", "", Category.COMBAT, true, false, false) {
    private val range by setting("Range", 6.0, 0.0..20.0)
    private val antiSurround by setting("AntiSurround", false)
    private val autoEnable by setting("Enable IM", true)
    private val autoDisable by setting("AutoDisable", false)

    private val target get() = mc.world.playerEntities
        .filter { it != mc.player }
        .filter { !Stay.friendManager!!.isFriend(it) }
        .filter { it.distanceTo(mc.player.positionVector) <= range }
        .minByOrNull { it.distanceTo(mc.player.positionVector) }

    init {
        onEnable {
            if (autoEnable) {
                ModuleManager.getModuleByClass(InstantMineRewrite::class.java)?.run {
                    if (!this@run.isEnabled) this@run.enable()
                }
            }
        }

        onTick {
            if (fullNullCheck()) return@onTick
            val targetNonNull = target ?: return@onTick
            val burrowPos = targetNonNull.position
            var surroundPos: BlockPos? = null
            if (burrowPos.add(0, 0, -1).block == Blocks.OBSIDIAN) {
                surroundPos = burrowPos.add(0, 0, -1)
            } else if (burrowPos.add(0, 0, 1).block == Blocks.OBSIDIAN) {
                surroundPos = burrowPos.add(0, 0, 1)
            } else if (burrowPos.add(-1, 0, -1).block == Blocks.OBSIDIAN) {
                surroundPos = burrowPos.add(-1, 0, -1)
            } else if (burrowPos.add(1, 0, -1).block == Blocks.OBSIDIAN) {
                surroundPos = burrowPos.add(1, 0, -1)
            }
            if (InstantMineRewrite.isBlockMining(burrowPos, surroundPos ?: return@onTick)) return@onTick
            mc.player.swingArm(EnumHand.MAIN_HAND)
            mc.playerController.onPlayerDamageBlock(burrowPos, BlockUtil.getRayTraceFacing(burrowPos))
            if (antiSurround) {
                mc.player.swingArm(EnumHand.MAIN_HAND)
                mc.playerController.onPlayerDamageBlock(surroundPos, BlockUtil.getRayTraceFacing(surroundPos))
            }
            if (autoDisable) disable()
        }
    }
}