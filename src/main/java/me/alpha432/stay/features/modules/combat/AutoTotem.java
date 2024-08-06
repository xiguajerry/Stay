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
import me.alpha432.stay.manager.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class AutoTotem extends Module {
    public AutoTotem() {
        super("AutoTotem", "AutoTotem", Module.Category.COMBAT, true, false, false);
    }
     public  Setting<Boolean> soft = register(new Setting<>("Soft", true));
     public Setting<Boolean> pauseInContainers = register(new Setting<>("PauseInInventory", false));
     public Setting<Boolean> pauseInInventory = register(new Setting<>("PauseInContainer", false));

    private int numOfTotems;
    private int preferredTotemSlot;
    public static Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void onUpdate() {
        if(ModuleManager.getModuleByName("OffHandCrystal").isEnabled()){
            soft.setValue(true);
        }



        if (mc.player == null) {
            return;
        }
        if (!findTotems()) {
            return;
        }
        if (pauseInContainers.getValue() && (mc.currentScreen instanceof GuiContainer) && !(mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        if (pauseInInventory.getValue() && (mc.currentScreen instanceof GuiInventory)) {
            return;
        }

        if (soft.getValue()) {
            if (mc.player.getHeldItemOffhand().getItem().equals(Items.AIR)) {
                mc.playerController.windowClick(0, preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.updateController();
            }

        } else {

            if (!mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {

                boolean offhandEmptyPreSwitch = mc.player.getHeldItemOffhand().getItem().equals(Items.AIR);

                mc.playerController.windowClick(0, preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);

                if (!offhandEmptyPreSwitch) {
                    mc.playerController.windowClick(0, preferredTotemSlot, 0, ClickType.PICKUP, mc.player);
                }
                mc.playerController.updateController();

            }

        }

    }

    private boolean findTotems() {
        this.numOfTotems = 0;
        AtomicInteger preferredTotemSlotStackSize = new AtomicInteger();
        preferredTotemSlotStackSize.set(Integer.MIN_VALUE);
        getInventoryAndHotbarSlots().forEach((slotKey, slotValue) -> {
            int numOfTotemsInStack = 0;
            if (slotValue.getItem().equals(Items.TOTEM_OF_UNDYING)) {
                numOfTotemsInStack = slotValue.getCount();
                if (preferredTotemSlotStackSize.get() < numOfTotemsInStack) {
                    preferredTotemSlotStackSize.set(numOfTotemsInStack);
                    preferredTotemSlot = slotKey;
                }
            }

            this.numOfTotems = this.numOfTotems + numOfTotemsInStack;

        });
        if (mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
            this.numOfTotems = this.numOfTotems + mc.player.getHeldItemOffhand().getCount();
        }
        return this.numOfTotems != 0;
    }


    private static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return getInventorySlots(9, 44);
    }

    private static Map<Integer, ItemStack> getInventorySlots(int current, int last) {
        Map<Integer, ItemStack> fullInventorySlots = new HashMap<>();
        while (current <= last) {
            fullInventorySlots.put(current, mc.player.inventoryContainer.getInventory().get(current));
            current++;
        }
        return fullInventorySlots;
    }

}
