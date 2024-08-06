/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.render;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

/**
 * @author Novola
 * @since 19/1/2020 (1/19/2020)
 */
public class Swing extends Module {
    private Setting<Hand> hand = register(new Setting<>("Hand", Hand.OFFHAND));
    public Swing() {
        super("NoSwing", "Changes the hand you swing with", Module.Category.VISUAL, false, false, false);
    }

    @Override
    public void onUpdate() {
        if (mc.world == null)
            return;
        if(hand.getValue().equals(Hand.OFFHAND)) {
            mc.player.swingingHand = EnumHand.OFF_HAND;
        }
        if(hand.getValue().equals(Hand.MAINHAND)) {
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        }
        if(hand.getValue().equals(Hand.PACKETSWING)) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9) {
                mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
                mc.entityRenderer.itemRenderer.itemStackMainHand = mc.player.getHeldItemMainhand();
            }
        }
    }

    public enum Hand {
        OFFHAND,
        MAINHAND,
        PACKETSWING
    }
}