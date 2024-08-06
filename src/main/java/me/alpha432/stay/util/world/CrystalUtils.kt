package me.alpha432.stay.util.world

import anonymous.team.eventsystem.SafeClientEvent
import me.alpha432.stay.util.extension.fastFloor
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.init.Blocks
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.function.Predicate
import kotlin.math.abs

inline val IBlockState.isLiquid: Boolean
    get() = this.material.isLiquid

inline val IBlockState.isWater: Boolean
    get() = this.block == Blocks.WATER

inline val IBlockState.isReplaceable: Boolean
    get() = this.material.isReplaceable

object CrystalUtils {
    val EntityEnderCrystal.blockPos: BlockPos
        get() = BlockPos(this.posX.fastFloor(), this.posY.fastFloor() - 1, this.posZ.fastFloor())

    private val mutableBlockPos = ThreadLocal.withInitial {
        BlockPos.MutableBlockPos()
    }

    /** Checks if the block is valid for placing crystal */
    fun SafeClientEvent.canPlaceCrystalOn(pos: BlockPos): Boolean {
        val block = world.getBlockState(pos).block
        return block == Blocks.BEDROCK || block == Blocks.OBSIDIAN
    }

    fun isValidMaterial(blockState: IBlockState): Boolean {
        return !blockState.isLiquid && blockState.isReplaceable
    }

    fun getCrystalPlacingBB(pos: BlockPos): AxisAlignedBB {
        return getCrystalPlacingBB(pos.x, pos.y, pos.z)
    }

    fun getCrystalPlacingBB(x: Int, y: Int, z: Int): AxisAlignedBB {
        return AxisAlignedBB(
            x + 0.001, y + 1.0, z + 0.001,
            x + 0.999, y + 3.0, z + 0.999
        )
    }

    fun getCrystalPlacingBB(pos: Vec3d): AxisAlignedBB {
        return getCrystalPlacingBB(pos.x, pos.y, pos.z)
    }

    fun getCrystalPlacingBB(x: Double, y: Double, z: Double): AxisAlignedBB {
        return AxisAlignedBB(
            x - 0.499, y, z - 0.499,
            x + 0.499, y + 2.0, z + 0.499
        )
    }

    fun getCrystalBB(pos: BlockPos): AxisAlignedBB {
        return getCrystalBB(pos.x, pos.y, pos.z)
    }

    fun getCrystalBB(x: Int, y: Int, z: Int): AxisAlignedBB {
        return AxisAlignedBB(
            x - 0.5, y + 1.0, z - 0.5,
            x + 1.5, y + 3.0, z + 1.5
        )
    }

    fun getCrystalBB(pos: Vec3d): AxisAlignedBB {
        return getCrystalBB(pos.x, pos.y, pos.z)
    }

    fun getCrystalBB(x: Double, y: Double, z: Double): AxisAlignedBB {
        return AxisAlignedBB(
            x - 1.0, y, z - 1.0,
            x + 1.0, y + 2.0, z + 1.0
        )
    }

    fun placeBoxIntersectsCrystalBox(placePos: BlockPos, crystalPos: BlockPos): Boolean {
        return crystalPos.y - placePos.y in 0..2
                && abs(crystalPos.x - placePos.x) < 2
                && abs(crystalPos.z - placePos.z) < 2
    }

    fun placeBoxIntersectsCrystalBox(placePos: Vec3d, crystalPos: BlockPos): Boolean {
        return crystalPos.y - placePos.y in 0.0..2.0
                && abs(crystalPos.x - placePos.x) < 2.0
                && abs(crystalPos.z - placePos.z) < 2.0
    }

    fun placeBoxIntersectsCrystalBox(placeX: Double, placeY: Double, placeZ: Double, crystalPos: BlockPos): Boolean {
        return crystalPos.y - placeY in 0.0..2.0
                && abs(crystalPos.x - placeX) < 2.0
                && abs(crystalPos.z - placeZ) < 2.0
    }

    fun placeBoxIntersectsCrystalBox(placeX: Double, placeY: Double, placeZ: Double, crystalX: Double, crystalY: Double, crystalZ: Double): Boolean {
        return crystalY - placeY in 0.0..2.0
                && abs(crystalX - placeX) < 2.0
                && abs(crystalZ - placeZ) < 2.0
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
    fun isResistant(blockState: IBlockState) =
        !blockState.isLiquid && blockState.block.getExplosionResistance(null) >= 19.7
}