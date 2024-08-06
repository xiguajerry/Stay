/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午11:33
 */

package me.alpha432.stay.util.graphics.opengl

import org.lwjgl.opengl.GL11.*

interface IRenderHelper {
    fun push() = glPushMatrix()

    fun pop() = glPopMatrix()

    fun color4f(r: Float, g: Float, b: Float, a: Float) = glColor4f(r, g, b, a)

    fun enableGl(code: Int) = glEnable(code)

    fun disableGl(code: Int) = glDisable(code)
}