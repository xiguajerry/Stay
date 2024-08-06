package me.alpha432.stay.util.extension

import me.alpha432.stay.util.basement.wrapper.MinecraftWrapper.world
import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos

val BlockPos.block: Block?
    get() = world?.getBlockState(this)?.block