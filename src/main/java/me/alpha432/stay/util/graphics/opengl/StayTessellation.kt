/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午8:19
 */

package me.alpha432.stay.util.graphics.opengl

import anonymous.team.eventsystem.utils.Helper
import me.alpha432.stay.util.basement.wrapper.MinecraftWrapper
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.lwjgl.opengl.GL11
import java.awt.Color


object StayTessellator: Tessellator(0x200000), Helper {
    @JvmStatic
    fun pTicks(): Float {
        return if (mc.isGamePaused) mc.renderPartialTicksPaused else mc.renderPartialTicks
    }

    fun drawBox(blockPos: BlockPos, r: Int, g: Int, b: Int, a: Int, sides: Int, s: Int) {
        drawBox(
            buffer,
            blockPos.x.toFloat(),
            blockPos.y.toFloat(),
            blockPos.z.toFloat(),
            1.0f,
            1.0f,
            1.0f,
            r,
            g,
            b,
            a,
            sides
        )
    }

    fun prepare(mode: Int) {
        prepareGL()
        begin(mode)
    }

    fun prepare(mode_requested: String) {
        var mode = 0
        if (mode_requested.equals("quads", ignoreCase = true)) {
            mode = 7
        } else if (mode_requested.equals("lines", ignoreCase = true)) {
            mode = 1
        }
        prepare_gl()
        begin(mode)
    }

    fun prepare_gl() {
        GL11.glBlendFunc(770, 771)
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        )
        GlStateManager.glLineWidth(1.5f)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GlStateManager.enableAlpha()
        GlStateManager.color(1.0f, 1.0f, 1.0f)
        GL11.glLineWidth(2.0f)
    }

    fun draw_cube_line(blockPos: BlockPos, argb: Int, sides: String) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        draw_cube_line(blockPos, r, g, b, a, sides)
    }

    fun draw_cube_line(x: Float, y: Float, z: Float, argb: Int, sides: String) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        draw_cube_line(buffer, x, y, z, 1.0f, 0.5645f, 1.0f, r, g, b, a, sides)
    }

    fun draw_cube_line(blockPos: BlockPos, r: Int, g: Int, b: Int, a: Int, sides: String) {
        draw_cube_line(
            buffer,
            blockPos.x.toFloat(),
            blockPos.y.toFloat(),
            blockPos.z.toFloat(),
            1.0f,
            1.0f,
            1.0f,
            r,
            g,
            b,
            a,
            sides
        )
    }

    fun drawBox2(bb: AxisAlignedBB, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        val tessellator = getInstance()
        val bufferBuilder = tessellator.buffer
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR)
        if (sides and 0x1 != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x2 != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x4 != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x8 != 0x0) {
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x10 != 0x0) {
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x20 != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
        tessellator.draw()
    }

    fun drawBoxSmall(x: Float, y: Float, z: Float, argb: Int, sides: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBox(buffer, x, y, z, 0.25f, 0.25f, 0.25f, r, g, b, a, sides)
    }

    fun prepareGL() {
        GL11.glBlendFunc(770, 771)
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        )
        GlStateManager.glLineWidth(1.5f)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GlStateManager.enableAlpha()
        GlStateManager.color(1.0f, 1.0f, 1.0f)
    }

    fun begin(mode: Int) {
        buffer.begin(mode, DefaultVertexFormats.POSITION_COLOR)
    }

    fun release() {
        render()
        releaseGL()
    }

    fun render() {
        draw()
    }

    fun releaseGL() {
        GlStateManager.enableCull()
        GlStateManager.depthMask(true)
        GlStateManager.enableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.enableDepth()
        GlStateManager.color(1.0f, 1.0f, 1.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun drawFace(blockPos: BlockPos, argb: Int, sides: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawFace(blockPos, r, g, b, a, sides)
    }

    fun drawFace(blockPos: BlockPos, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        drawFace(
            buffer,
            blockPos.x.toFloat(),
            blockPos.y.toFloat(),
            blockPos.z.toFloat(),
            1.0f,
            1.0f,
            1.0f,
            r,
            g,
            b,
            a,
            sides
        )
    }

    fun drawFace(
        buffer: BufferBuilder,
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        r: Int,
        g: Int,
        b: Int,
        a: Int,
        sides: Int
    ) {
        if (sides and 1 != 0) {
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
    }

    fun drawFace(buffer: BufferBuilder, bb: AxisAlignedBB, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        if (sides and 1 != 0) {
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
        }
    }


    fun drawFullBox(bb: AxisAlignedBB, blockPos: BlockPos, width: Float, red: Int, green: Int, blue: Int, alpha: Int) {
        prepare(7)
        drawBox(blockPos, red, green, blue, alpha, 63)
        release()
        drawBoundingBox(bb, width, red, green, blue, 255)
    }


    fun drawBoundingGay(bb: AxisAlignedBB, width: Float, r: Int, g: Int, b: Int, alpha: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        GL11.glLineWidth(width)
        val tessellator = getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        GL11.glDisable(2848)
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    fun drawBox(bb: AxisAlignedBB, argb: Int, sides: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBox(buffer, bb, r, g, b, a, sides)
    }

    fun drawBox(blockPos: BlockPos, argb: Int, sides: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBox(blockPos, r, g, b, a, sides)
    }

    fun drawHalfBox(blockPos: BlockPos, argb: Int, sides: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawHalfBox(blockPos, r, g, b, a, sides)
    }

    fun drawHalfBox(x: Float, y: Float, z: Float, argb: Int, sides: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBox(buffer, x, y, z, 1.0f, 0.5f, 1.0f, r, g, b, a, sides)
    }

    fun drawBox(x: Float, y: Float, z: Float, argb: Int, sides: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBox(buffer, x, y, z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides)
    }

    fun drawBox(blockPos: BlockPos, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        drawBox(
            buffer,
            blockPos.x.toFloat(),
            blockPos.y.toFloat(),
            blockPos.z.toFloat(),
            1.0f,
            1.0f,
            1.0f,
            r,
            g,
            b,
            a,
            sides
        )
    }

    fun drawHalfBox(blockPos: BlockPos, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        drawBox(
            buffer,
            blockPos.x.toFloat(),
            blockPos.y.toFloat(),
            blockPos.z.toFloat(),
            1.0f,
            0.5f,
            1.0f,
            r,
            g,
            b,
            a,
            sides
        )
    }

    fun drawBox(vec3d: Vec3d, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        drawBox(
            buffer,
            vec3d.x.toFloat(),
            vec3d.y.toFloat(),
            vec3d.z.toFloat(),
            1.0f,
            1.0f,
            1.0f,
            r,
            g,
            b,
            a,
            sides
        )
    }

    fun draw_cube_line(
        buffer: BufferBuilder,
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        r: Int,
        g: Int,
        b: Int,
        a: Int,
        sides: String
    ) {
        if (sides.split("-").contains("downwest") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("upwest") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("downeast") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("upeast") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("downnorth") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("upnorth") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("downsouth") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("upsouth") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("nortwest") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("norteast") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("southweast") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides.split("-").contains("southeast") || sides.equals(
                "all",
                ignoreCase = true
            )
        ) {
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
    }

    fun getBufferBuilder(): BufferBuilder {
        return buffer
    }

    fun drawHead(
        buffer: BufferBuilder,
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        r: Int,
        g: Int,
        b: Int,
        a: Int,
        sides: Int
    ) {
        if (sides and 2 != 0) {
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
    }

    fun drawBox(
        buffer: BufferBuilder,
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        r: Int,
        g: Int,
        b: Int,
        a: Int,
        sides: Int
    ) {
        if (sides and 1 != 0) {
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 2 != 0) {
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 4 != 0) {
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 8 != 0) {
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x10 != 0) {
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x20 != 0) {
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
    }

    fun drawBox(buffer: BufferBuilder, bb: AxisAlignedBB, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        if (sides and 1 != 0) {
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 2 != 0) {
            buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
        if (sides and 4 != 0) {
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 8 != 0) {
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x10 != 0) {
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x20 != 0) {
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
    }

    fun drawSmallBox(vec3d: Vec3d, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        drawBox(
            buffer,
            vec3d.x.toFloat(),
            vec3d.y.toFloat(),
            vec3d.z.toFloat(),
            0.3f,
            0.3f,
            0.3f,
            r,
            g,
            b,
            a,
            sides
        )
    }

    fun drawLines(
        buffer: BufferBuilder,
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        r: Int,
        g: Int,
        b: Int,
        a: Int,
        sides: Int
    ) {
        if (sides and 0x11 != 0) {
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x12 != 0) {
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x21 != 0) {
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x22 != 0) {
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 5 != 0) {
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 6 != 0) {
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 9 != 0) {
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0xA != 0) {
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x14 != 0) {
            buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x24 != 0) {
            buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x18 != 0) {
            buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos(x.toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
        if (sides and 0x28 != 0) {
            buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
            buffer.pos((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        }
    }

    fun drawBoundingBox(bb: BlockPos, width: Float, argb: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBoundingBox(bb, width, r, g, b, a)
    }

    fun drawBoundingBox(bb: AxisAlignedBB, width: Float, argb: Int) {
        val a = argb ushr 24 and 255
        val r = argb ushr 16 and 255
        val g = argb ushr 8 and 255
        val b = argb and 255
        drawBoundingBox(bb, width, r, g, b, a)
    }

    fun drawBoundingBoxKA(bb: BlockPos, width: Float, argb: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBoundingBoxKA(bb, width, r, g, b, a)
    }

    fun drawBoundingBoxKA(pos: BlockPos, width: Float, red: Int, green: Int, blue: Int, alpha: Int) {
        drawBoundingBox(getBoundingFromPos(pos), width, red, green, blue, alpha)
    }

    fun drawBoundingBox(pos: BlockPos, width: Float, red: Int, green: Int, blue: Int, alpha: Int) {
        drawBoundingBox(getBoundingFromPos(pos), width, red, green, blue, alpha)
    }

    fun drawBoundingBox(bb: AxisAlignedBB, qwq: Int) {
        drawBoundingBox(bb, qwq, qwq, qwq, qwq)
    }

    fun drawBoundingBox(bb: AxisAlignedBB, r: Int, g: Int, b: Int, alpha: Int) {
        GL11.glPushMatrix()
        GL11.glEnable(3042)
        GL11.glDisable(2929)
        GL11.glBlendFunc(770, 771)
        GL11.glDisable(3553)
        GL11.glDepthMask(false)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        val tessellator = getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        GL11.glDisable(2848)
        GL11.glDepthMask(true)
        GL11.glEnable(2929)
        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glPopMatrix()
    }

    fun drawBoundingBox(bb: AxisAlignedBB, width: Float, r: Int, g: Int, b: Int, alpha: Int) {
        GL11.glPushMatrix()
        GL11.glEnable(3042)
        GL11.glDisable(2929)
        GL11.glBlendFunc(770, 771)
        GL11.glDisable(3553)
        GL11.glDepthMask(false)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        GL11.glLineWidth(width)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1f)
        val tessellator = getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        GL11.glDisable(2848)
        GL11.glDepthMask(true)
        GL11.glEnable(2929)
        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glPopMatrix()
    }

    fun drawBoundingBoxKA(bb: AxisAlignedBB, width: Float, r: Int, g: Int, b: Int, alpha: Int) {
        GL11.glPushMatrix()
        GL11.glEnable(3042)
        GL11.glDisable(2929)
        GL11.glBlendFunc(770, 771)
        GL11.glDisable(3553)
        GL11.glDepthMask(false)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        GL11.glLineWidth(width)
        val tessellator = getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX - bb.minX, bb.maxY - bb.minY, bb.maxZ - bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        GL11.glDisable(2848)
        GL11.glDepthMask(true)
        GL11.glEnable(2929)
        GL11.glEnable(3553)
        GL11.glDisable(3042)
        GL11.glPopMatrix()
    }

    fun drawBoundingBoxBlockPos(bp: BlockPos, width: Float, r: Int, g: Int, b: Int, alpha: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        GL11.glLineWidth(width)
        val mc = Minecraft.getMinecraft()
        val x = bp.x.toDouble() - mc.getRenderManager().viewerPosX
        val y = bp.y.toDouble() - mc.getRenderManager().viewerPosY
        val z = bp.z.toDouble() - mc.getRenderManager().viewerPosZ
        val bb = AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0)
        val tessellator = getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        GL11.glDisable(2848)
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    fun drawBoundingBoxBlockPos(hitVec: Vec3d, width: Float, r: Int, g: Int, b: Int, alpha: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        GL11.glLineWidth(width)
        val mc = Minecraft.getMinecraft()
        val x: Double = hitVec.x - mc.getRenderManager().viewerPosX
        val y: Double = hitVec.y - mc.getRenderManager().viewerPosY
        val z: Double = hitVec.z - mc.getRenderManager().viewerPosZ
        val bb = AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0)
        val tessellator = getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        bufferbuilder.begin(1, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        GL11.glDisable(2848)
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    fun drawBoundingBoxBottomBlockPos(bp: BlockPos, width: Float, r: Int, g: Int, b: Int, alpha: Int) {
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.disableDepth()
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GL11.glEnable(2848)
        GL11.glHint(3154, 4354)
        GL11.glLineWidth(width)
        val mc = Minecraft.getMinecraft()
        val x = bp.x.toDouble() - mc.getRenderManager().viewerPosX
        val y = bp.y.toDouble() - mc.getRenderManager().viewerPosY
        val z = bp.z.toDouble() - mc.getRenderManager().viewerPosZ
        val bb = AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0)
        val tessellator = getInstance()
        val bufferbuilder = tessellator.buffer
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR)
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, alpha).endVertex()
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, alpha).endVertex()
        tessellator.draw()
        GL11.glDisable(2848)
        GlStateManager.depthMask(true)
        GlStateManager.enableDepth()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    fun drawBoxBottom(
        buffer: BufferBuilder,
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        r: Int,
        g: Int,
        b: Int,
        a: Int
    ) {
        buffer.pos((x + w).toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
        buffer.pos((x + w).toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        buffer.pos(x.toDouble(), y.toDouble(), (z + d).toDouble()).color(r, g, b, a).endVertex()
        buffer.pos(x.toDouble(), y.toDouble(), z.toDouble()).color(r, g, b, a).endVertex()
    }

    fun drawBoxBottom(blockPos: BlockPos, argb: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBoxBottom(blockPos, r, g, b, a)
    }

    fun drawBoxBottom(x: Float, y: Float, z: Float, argb: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBoxBottom(buffer, x, y, z, 1.0f, 1.0f, 1.0f, r, g, b, a)
    }

    fun drawBoxBottom(blockPos: BlockPos, r: Int, g: Int, b: Int, a: Int) {
        drawBoxBottom(
            buffer,
            blockPos.x.toFloat(),
            blockPos.y.toFloat(),
            blockPos.z.toFloat(),
            1.0f,
            1.0f,
            1.0f,
            r,
            g,
            b,
            a
        )
    }

    fun glBillboard(x: Float, y: Float, z: Float) {
        val scale = 0.02666667f
        GlStateManager.translate(
            x.toDouble() - (Minecraft.getMinecraft().getRenderManager() as RenderManager).renderPosX,
            y.toDouble() - (Minecraft.getMinecraft().getRenderManager() as RenderManager).renderPosY,
            z.toDouble() - (Minecraft.getMinecraft().getRenderManager() as RenderManager).renderPosZ
        )
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-Minecraft.getMinecraft().player.rotationYaw, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(
            Minecraft.getMinecraft().player.rotationPitch,
            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) -1.0f else 1.0f,
            0.0f,
            0.0f
        )
        GlStateManager.scale(-scale, -scale, scale)
    }

    fun glBillboardDistanceScaled(x: Float, y: Float, z: Float, player: EntityPlayer, scale: Float) {
        glBillboard(x, y, z)
        val distance = player.getDistance(x.toDouble(), y.toDouble(), z.toDouble()).toInt()
        var scaleDistance = distance.toFloat() / 2.0f / (2.0f + (2.0f - scale))
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance)
    }

    fun drawBox(hitVec: Vec3d, argb: Int, sides: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawBox(hitVec, r, g, b, a, sides)
    }

    fun drawFullBox(pos: BlockPos, width: Float, argb: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawFullBox(pos, width, r, g, b, a)
    }

    fun drawFullBox(pos: BlockPos, width: Float, red: Int, green: Int, blue: Int, alpha: Int) {
        drawBoundingFullBox(getBoundingFromPos(pos), red, green, blue, alpha)
        drawBoundingBox(getBoundingFromPos(pos), width, red, green, blue, 255)
    }

    fun drawFullBox(bb: AxisAlignedBB, width: Float, argb: Int) {
        val a = argb ushr 24 and 0xFF
        val r = argb ushr 16 and 0xFF
        val g = argb ushr 8 and 0xFF
        val b = argb and 0xFF
        drawFullBox(bb, width, r, g, b, a)
    }

    fun drawFullBox(bb: AxisAlignedBB, width: Float, red: Int, green: Int, blue: Int, alpha: Int) {
        drawBoundingFullBox(bb, red, green, blue, alpha)
        drawBoundingBox(bb, width, red, green, blue, 255)
    }

    fun drawBoundingFullBox(bb: AxisAlignedBB, red: Int, green: Int, blue: Int, alpha: Int) {
        GlStateManager.color(
            red.toFloat() / 255.0f,
            green.toFloat() / 255.0f,
            blue.toFloat() / 255.0f,
            alpha.toFloat() / 255.0f
        )
        drawFilledBox(bb)
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
    }

    fun drawBoundingFullBox(pos: BlockPos, red: Int, green: Int, blue: Int, alpha: Int) {
        drawBoundingFullBox(getBoundingFromPos(pos), red, green, blue, alpha)
    }

    fun getBoundingFromPos(render: BlockPos): AxisAlignedBB {
        val iBlockState: IBlockState = MinecraftWrapper.mc.world.getBlockState(render)
        val interp: Vec3d = interpolateEntity(MinecraftWrapper.mc.player, MinecraftWrapper.mc.renderPartialTicks)
        return iBlockState.getSelectedBoundingBox(MinecraftWrapper.mc.world, render).expand(0.002, 0.002, 0.002)
            .offset(-interp.x, -interp.y, -interp.z)
    }

    fun interpolateEntity(entity: Entity, time: Float): Vec3d {
        return Vec3d(
            entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time.toDouble(),
            entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time.toDouble(),
            entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time.toDouble()
        )
    }

    fun interpolateEntityClose(entity: Entity, renderPartialTicks: Float): Vec3d {
        return Vec3d(
            calculateDistanceWithPartialTicks(
                entity.posX,
                entity.lastTickPosX,
                renderPartialTicks
            ) - (Minecraft.getMinecraft().getRenderManager() as RenderManager).renderPosX,
            calculateDistanceWithPartialTicks(
                entity.posY,
                entity.lastTickPosY,
                renderPartialTicks
            ) - (Minecraft.getMinecraft().getRenderManager() as RenderManager).renderPosY,
            calculateDistanceWithPartialTicks(
                entity.posZ,
                entity.lastTickPosZ,
                renderPartialTicks
            ) - (Minecraft.getMinecraft().getRenderManager() as RenderManager).renderPosZ
        )
    }

    fun calculateDistanceWithPartialTicks(n: Double, n2: Double, renderPartialTicks: Float): Double {
        return n2 + (n - n2) * MinecraftWrapper.mc.renderPartialTicks.toDouble()
    }

    fun drawFilledBox(axisAlignedBB: AxisAlignedBB) {
        val tessellator = getInstance()
        val vertexbuffer = tessellator.buffer
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION)
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex()
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex()
        tessellator.draw()
    }

    fun drawBoxTest(
        x: Float,
        y: Float,
        z: Float,
        w: Float,
        h: Float,
        d: Float,
        r: Int,
        g: Int,
        b: Int,
        a: Int,
        sides: Int
    ) {
        GL11.glPushMatrix()
        GL11.glBlendFunc(770, 771)
        GlStateManager.tryBlendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA,
            GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO
        )
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.glLineWidth(1f)
        GlStateManager.disableTexture2D()
        GlStateManager.depthMask(false)
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.disableDepth()
        GlStateManager.disableLighting()
        GlStateManager.disableCull()
        GlStateManager.enableAlpha()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1f)
        RenderUtil.setColor(Color(r, g, b, a))
        GL11.glBegin(7)
        if (sides and 1 != 0) {
            GL11.glVertex3d((x + w).toDouble(), y.toDouble(), z.toDouble())
            GL11.glVertex3d((x + w).toDouble(), y.toDouble(), (z + d).toDouble())
            GL11.glVertex3d(x.toDouble(), y.toDouble(), (z + d).toDouble())
            GL11.glVertex3d(x.toDouble(), y.toDouble(), z.toDouble())
        }
        if (sides and 2 != 0) {
            GL11.glVertex3d((x + w).toDouble(), (y + h).toDouble(), z.toDouble())
            GL11.glVertex3d(x.toDouble(), (y + h).toDouble(), z.toDouble())
            GL11.glVertex3d(x.toDouble(), (y + h).toDouble(), (z + d).toDouble())
            GL11.glVertex3d((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble())
        }
        if (sides and 4 != 0) {
            GL11.glVertex3d((x + w).toDouble(), y.toDouble(), z.toDouble())
            GL11.glVertex3d(x.toDouble(), y.toDouble(), z.toDouble())
            GL11.glVertex3d(x.toDouble(), (y + h).toDouble(), z.toDouble())
            GL11.glVertex3d((x + w).toDouble(), (y + h).toDouble(), z.toDouble())
        }
        if (sides and 8 != 0) {
            GL11.glVertex3d(x.toDouble(), y.toDouble(), (z + d).toDouble())
            GL11.glVertex3d((x + w).toDouble(), y.toDouble(), (z + d).toDouble())
            GL11.glVertex3d((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble())
            GL11.glVertex3d(x.toDouble(), (y + h).toDouble(), (z + d).toDouble())
        }
        if (sides and 0x10 != 0) {
            GL11.glVertex3d(x.toDouble(), y.toDouble(), z.toDouble())
            GL11.glVertex3d(x.toDouble(), y.toDouble(), (z + d).toDouble())
            GL11.glVertex3d(x.toDouble(), (y + h).toDouble(), (z + d).toDouble())
            GL11.glVertex3d(x.toDouble(), (y + h).toDouble(), z.toDouble())
        }
        if (sides and 0x20 != 0) {
            GL11.glVertex3d((x + w).toDouble(), y.toDouble(), (z + d).toDouble())
            GL11.glVertex3d((x + w).toDouble(), y.toDouble(), z.toDouble())
            GL11.glVertex3d((x + w).toDouble(), (y + h).toDouble(), z.toDouble())
            GL11.glVertex3d((x + w).toDouble(), (y + h).toDouble(), (z + d).toDouble())
        }
        GL11.glEnd()
        GlStateManager.enableCull()
        GlStateManager.depthMask(true)
        GlStateManager.enableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.enableDepth()
        GlStateManager.color(1.0f, 1.0f, 1.0f)
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        GL11.glPopMatrix()
    }

    fun drawBoxTest(bb: AxisAlignedBB, color: Color, sides: Int) {
        drawBoxTest(bb, color.red, color.green, color.blue, color.alpha, sides)
    }

    fun drawBoxTest(bb: AxisAlignedBB, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        drawBoxTest(
            bb.minX.toFloat(),
            bb.minY.toFloat(),
            bb.minZ.toFloat(),
            bb.maxX.toFloat() - bb.minX.toFloat(),
            bb.maxY.toFloat() - bb.minY.toFloat(),
            bb.maxZ.toFloat() - bb.minZ.toFloat(),
            r,
            g,
            b,
            a,
            sides
        )
    }

    fun drawBoxTests(bb: AxisAlignedBB, r: Int, g: Int, b: Int, a: Int, sides: Int) {
        val tessellator = getInstance()
        val bufferBuilder = tessellator.buffer
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR)
        if (sides and 0x1 != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x2 != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x4 != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x8 != 0x0) {
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x10 != 0x0) {
            bufferBuilder.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
        }
        if (sides and 0x20 != 0x0) {
            bufferBuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex()
            bufferBuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex()
        }
        tessellator.draw()
    }
}