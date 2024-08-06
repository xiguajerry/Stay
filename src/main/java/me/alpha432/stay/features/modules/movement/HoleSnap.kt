package me.alpha432.stay.features.modules.movement

import me.alpha432.stay.features.command.Command
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.features.modules.WrappedModule
import me.alpha432.stay.manager.NotificationManager
import me.alpha432.stay.manager.NotificationType
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.graphics.opengl.RenderUtil
import me.alpha432.stay.util.math.HoleUtil
import me.alpha432.stay.util.math.PathFinder
import me.alpha432.stay.util.math.PathUtils
import me.alpha432.stay.util.math.vector.distanceTo
import me.alpha432.stay.util.world.BlockInteractionHelper
import me.alpha432.stay.util.world.EntityUtil.isInHole
import net.minecraft.util.math.BlockPos
import java.awt.Color

@ApplyModule
object HoleSnap : WrappedModule(name = "HoleSnap", category = Category.MOVEMENT) {
    private val range by setting("Range", 6, 0..10)

    private val hole: BlockPos?
        get() = BlockInteractionHelper.getSphere(
            mc.player.position,
            range.toFloat(),
            range,
            false,
            true,
            0).filter { HoleUtil.isHole(it) }.minByOrNull { mc.player.distanceTo(it.x, it.y, it.z) }

    private val points
        get() =
            if (hole == null) listOf()
            else PathUtils.findBlinkPath(hole!!.x.toDouble(), hole!!.y.toDouble(), hole!!.z.toDouble())

    init {
        // TODO: 2022/1/2
        onEnable {
            player ?: return@onEnable
            if (player!!.isInHole) {
                Command.sendMessage("[HoleSnap] You're already in a hole!")
                NotificationManager.push("[HoleSnap] You're already in a hole!", NotificationType.WARNING, 1000)
                disable()
            }
        }

        onTick {
            player ?: return@onTick
            if (player!!.isInHole) {
//                Command.sendMessage("[HoleSnap] You're already in a hole!")
//                NotificationManager.push("[HoleSnap] You're already in a hole!", NotificationType.WARNING, 1000)
                disable()
            }

            val target = hole ?: return@onTick
            mc.player.setPosition(target.x.toDouble() + 0.5, target.y.toDouble() + 0.5, target.z.toDouble() + 0.5)
        }

        onRender3D {
            hole?.let {
                RenderUtil.drawBlockOutline(hole, Color.RED, 1.5f, true)
            }
            points.forEach {
                RenderUtil.drawBlockOutline(it, Color.PINK, 1.5f, true)
            }
        }
    }
}