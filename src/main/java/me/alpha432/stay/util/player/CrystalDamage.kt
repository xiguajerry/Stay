package me.alpha432.stay.util.player

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class CrystalDamage(
    val crystalPos: Vec3d,
    val blockPos: BlockPos,
    val selfDamage: Float
)