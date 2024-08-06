/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class AutoQuiver extends Module {
    public AutoQuiver() {
        super("AutoQuiver", "Rotates and shoots yourself with good potion effects", Module.Category.COMBAT, true, false, false);

    }
    int randomVariation;
    public Setting<Boolean> autoSwitch = register(new Setting<>("AutoSwitchArrow", true));
    public Setting<Boolean> toggle = register(new Setting<>("Toggle", true));

    public void onTick() {
        PotionEffect speedEffect = mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)));
        PotionEffect strengthEffect = mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(5)));
        boolean hasSpeed = speedEffect != null;
        boolean hasStrength = strengthEffect != null;
        if (mc.player.getHeldItemMainhand().getItem() == Items.BOW) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0.0F, -90.0F, true));
        }

        if (mc.player.getItemInUseMaxCount() == 0) {
            mc.player.setActiveHand(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        } else if (mc.player.getItemInUseMaxCount() > 0) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.stopActiveHand();
        }

        if ((Boolean)this.toggle.getValue()) {
            this.toggle();
        }

    }

    private boolean isArrowInInventory(String type) {
        boolean inInv = false;

        for(int i = 0; i < 36; ++i) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == Items.TIPPED_ARROW && itemStack.getDisplayName().equalsIgnoreCase(type)) {
                inInv = true;
                this.switchArrow(i);
                break;
            }
        }

        return inInv;
    }

    private void switchArrow(int oldSlot) {
        int bowSlot = mc.player.inventory.currentItem;
        int placeSlot = bowSlot + 1;
        if (placeSlot > 8) {
            placeSlot = 1;
        }

        if (placeSlot != oldSlot) {
            if (mc.currentScreen instanceof GuiContainer) {
                return;
            }

            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, placeSlot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, oldSlot, 0, ClickType.PICKUP, mc.player);
        }

    }

    private int getBowCharge() {
        if (this.randomVariation == 0) {
            this.randomVariation = 1;
        }

        return 1 + this.randomVariation;
    }
}
