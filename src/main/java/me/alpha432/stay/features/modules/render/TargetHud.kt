/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */
package me.alpha432.stay.features.modules.render

import me.alpha432.stay.client.Stay
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.features.modules.client.FontMod
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.graphics.animations.AnimationFlag
import me.alpha432.stay.util.graphics.animations.Easing
import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import java.awt.Color
import java.util.*
import java.util.function.Consumer

@ApplyModule
object TargetHud : Module("TargetHud", "description", Category.VISUAL, true, false, false) {
    private val mode by setting("Mode", Modes.NOVOLINEOLD)
    private val x by setting("X", 50, 0..2000)
    private val y by setting("Y", 50, 0..2000)
    private val backgroundAlpha by setting("Alpha", 80, 0..255)

    enum class Modes {
        NOVOLINEOLD
    }

    private var currentTarget: EntityLivingBase? = mc.player

    init {
        onTick {
            if (!fullNullCheck()) {
                val entities: MutableList<EntityLivingBase> = LinkedList()
                mc.world.loadedEntityList
                    .stream()
                    .filter { obj: Entity? -> EntityPlayer::class.java.isInstance(obj) }
                    .filter { entity: Entity -> checkIsNotPlayer(entity) }
                    .map { obj: Entity? -> EntityLivingBase::class.java.cast(obj) }
                    .sorted(Comparator.comparingDouble { entityLivingBase: EntityLivingBase ->
                        applyAsDouble(
                            entityLivingBase
                        )
                    })
                    .forEach { e: EntityLivingBase -> entities.add(e) }
                currentTarget = if (entities.isNotEmpty()) entities[0] else mc.player
                if (mc.currentScreen is GuiChat) {
                    currentTarget = mc.player
                }
            }
        }

        val animationFlag = AnimationFlag(Easing.OUT_QUINT, 700f)

        onRender2D {
            if (fullNullCheck()) return@onRender2D

            currentTarget?.also { target ->
                when(mode) {
                    Modes.NOVOLINEOLD -> {
                        val fr = mc.fontRenderer

                        // HealthBar's color
                        val color =
                            if (target.health / target.maxHealth > 0.66f) -0xff0100 else if (target.health / target.maxHealth > 0.33f) -0x6700 else -0x10000

                        /* Target Model */GlStateManager.color(1f, 1f, 1f)
                        GuiInventory.drawEntityOnScreen(x + 15, y + 32, 15, 1f, 1f, target)
                        /* Target Model */

                        /* Target Armor&HeldItem */
                        val armorList: MutableList<ItemStack> = LinkedList()
                        val _armorList: MutableList<ItemStack> = LinkedList()
                        target.armorInventoryList.forEach(Consumer { itemStack: ItemStack ->
                            if (!itemStack.isEmpty()) _armorList.add(
                                itemStack
                            )
                        })
                        var i = _armorList.size - 1
                        while (i >= 0) {
                            armorList.add(_armorList[i])
                            --i
                        }
                        var armorSize = 0
                        when (armorList.size) {
                            0 -> {
                                if (!target.heldItemMainhand.isEmpty() && !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemMainhand,
                                        x + 28,
                                        y + 18
                                    )
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemOffhand,
                                        x + 43,
                                        y + 18
                                    )
                                    armorSize += 45
                                } else if (!target.heldItemMainhand.isEmpty() || !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        if (target.heldItemMainhand.isEmpty()) target.heldItemOffhand else target.heldItemMainhand,
                                        x + 28,
                                        y + 18
                                    )
                                    armorSize += 30
                                }
                            }
                            1 -> {
                                armorSize = 15
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[0], x + 28, y + 18)
                                if (!target.heldItemMainhand.isEmpty() && !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemMainhand,
                                        x + 43,
                                        y + 18
                                    )
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemOffhand,
                                        x + 58,
                                        y + 18
                                    )
                                    armorSize += 45
                                } else if (!target.heldItemMainhand.isEmpty() || !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        if (target.heldItemMainhand.isEmpty()) target.heldItemOffhand else target.heldItemMainhand,
                                        x + 43,
                                        y + 18
                                    )
                                    armorSize += 30
                                }
                            }
                            2 -> {
                                armorSize = 30
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[0], x + 28, y + 18)
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[1], x + 43, y + 18)
                                if (!target.heldItemMainhand.isEmpty() && !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemMainhand,
                                        x + 58,
                                        y + 18
                                    )
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemOffhand,
                                        x + 73,
                                        y + 18
                                    )
                                    armorSize += 45
                                } else if (!target.heldItemMainhand.isEmpty() || !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        if (target.heldItemMainhand.isEmpty()) target.heldItemOffhand else target.heldItemMainhand,
                                        x + 58,
                                        y + 18
                                    )
                                    armorSize += 30
                                }
                            }
                            3 -> {
                                armorSize = 45
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[0], x + 28, y + 18)
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[1], x + 43, y + 18)
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[2], x + 58, y + 18)
                                if (!target.heldItemMainhand.isEmpty() && !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemMainhand,
                                        x + 73,
                                        y + 18
                                    )
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemOffhand,
                                        x + 98,
                                        y + 18
                                    )
                                    armorSize += 45
                                } else if (!target.heldItemMainhand.isEmpty() || !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        if (target.heldItemMainhand.isEmpty()) target.heldItemOffhand else target.heldItemMainhand,
                                        x + 73,
                                        y + 18
                                    )
                                    armorSize += 30
                                }
                            }
                            4 -> {
                                armorSize = 60
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[0], x + 28, y + 18)
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[1], x + 43, y + 18)
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[2], x + 58, y + 18)
                                mc.getRenderItem().renderItemAndEffectIntoGUI(armorList[3], x + 73, y + 18)
                                if (!target.heldItemMainhand.isEmpty() && !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemMainhand,
                                        x + 98,
                                        y + 18
                                    )
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        target.heldItemOffhand,
                                        x + 113,
                                        y + 18
                                    )
                                    armorSize += 45
                                } else if (!target.heldItemMainhand.isEmpty() || !target.heldItemOffhand.isEmpty()) {
                                    mc.getRenderItem().renderItemAndEffectIntoGUI(
                                        if (target.heldItemMainhand.isEmpty()) target.heldItemOffhand else target.heldItemMainhand,
                                        x + 98,
                                        y + 18
                                    )
                                    armorSize += 30
                                }
                            }
                        }
                        /* Target Armor&HeldItem */

                        /* Background */
                        var backgroundStopY = y + 35
                        val stringWidth: Int = if (Stay.moduleManager!!.isModuleEnabled(FontMod::class.java)) {
                            fr.getStringWidth(target.name) + 10
                        } else {
                            fr.getStringWidth(target.name) + 30
                        }
                        var backgroundStopX: Int = if (fr.getStringWidth(target.name) > armorSize) x + stringWidth else x + armorSize + 30
                        backgroundStopX += 5
                        backgroundStopY += 5
                        Gui.drawRect(
                            x - 2,
                            y,
                            backgroundStopX,
                            backgroundStopY,
                            Color(0, 0, 0, backgroundAlpha).rgb
                        )
                        /* Background */

                        /* HealthBar */
                        val healthBarLength = animationFlag
                            .getAndUpdate(target.health / target.maxHealth * (backgroundStopX - x)).toInt()
                        Gui.drawRect(
                            x - 2,
                            backgroundStopY - 2,
                            x + healthBarLength,
                            backgroundStopY,
                            Color(color).darker().rgb
                        )
                        Gui.drawRect(
                            x - 2,
                            backgroundStopY - 2,
                            (x + target.health / target.maxHealth * (backgroundStopX - x)).toInt(),
                            backgroundStopY,
                            color
                        )
                        /* HealthBar */

                        //Target Name
                        fr.drawString(
                            target.name,
                            (x + 30).toFloat(),
                            (y + 8).toFloat(),
                            Color(255, 255, 255).rgb,
                            true
                        )
                    }
                }
            }
        }
    }


    private fun applyAsDouble(entityLivingBase: EntityLivingBase): Double {
        return entityLivingBase.getDistance(mc.player).toDouble()
    }

    private fun checkIsNotPlayer(entity: Entity): Boolean {
        return entity != mc.player
    }

}