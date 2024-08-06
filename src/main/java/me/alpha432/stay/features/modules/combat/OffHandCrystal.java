/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;


import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.player.PacketXP;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;
import me.alpha432.stay.util.world.CrystalUtil;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.EnumHand;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * Created by KillRED on 2020
 * Updated by B_312 on 01/15/21
 */

public class OffHandCrystal extends Module {

    public OffHandCrystal() {
        super("OffHandCrystal", "Allows you to switch up your Offhand.", Module.Category.COMBAT, true, false, false);
    }

    public Setting<Mode> mode = register(new Setting<>("Item", Mode.Crystal));
    public Setting<Integer> delay = register(new Setting<>("Delay", 0, 0, 1000));
    public Setting<Boolean> totem = register(new Setting<>("SwitchTotem", true));
    public Setting<Boolean> autoSwitchback = register(new Setting<>("SwitchBack", true));
    public Setting<Double> sbHealth = register(new Setting<>("Health", 11D, 0D, 36D));
    public Setting<Boolean> autoSwitch = register(new Setting<>("SwitchGap", true));
    public Setting<Boolean> anti32k = register(new Setting<>("Anti32k", true));
    public Setting<Double> rs = register(new Setting<>("AttackRange", 7d, 1d, 20d, v -> anti32k.getValue()));
    public Setting<SwingMode2> switchMode = register(new Setting<>("GapWhen", SwingMode2.Sword));
    public Setting<Boolean> elytra = register(new Setting<>("CheckElytra", true));
    public Setting<Boolean> holeCheck = register(new Setting<>("CheckHole", false));
    public Setting<Double> holeSwitch = register(new Setting<>("HoleHealth", 8d, 0d, 36d, v -> holeCheck.getValue()));
    public Setting<Boolean> crystalCalculate = register(new Setting<>("CalculateDmg", true));
    public Setting<Boolean> xp = register(new Setting<>("Noxp", true));
    public Setting<Boolean> crystalpop = register(new Setting<>("Nocrystalpop", false));
    public Setting<Double> maxSelfDmg = register(new Setting<>("MaxSelfDmg", 26d, 0d, 36d, v -> crystalCalculate.getValue()));
    public static Boolean dev = false;

    private int totems;
    private int count;
    private static final Timer timer = new Timer();

    public enum Mode {
        Crystal,
        Gap,


    }

    public enum SwingMode2 {
        Sword,
        RClick,


    }

    @Override
    public void onUpdate() {



        if (mc.player == null) return;


        if (Auto32k.mc.currentScreen instanceof GuiContainer) {

            return;

        }
        if(dev){
            return;
        }

        int crystals = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            crystals += mc.player.getHeldItemOffhand().getCount();
        }

        int gapple = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            gapple += mc.player.getHeldItemOffhand().getCount();
        }

        totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems++;
        }

        Item item = null;


        if(xp.getValue()){
            if(PacketXP.inft){
                if (PacketXP.binds.getKey() > -1) {
                    if (Keyboard.isKeyDown(PacketXP.binds.getKey()) && mc.currentScreen == null) {
                        return;
                    }
                } else if (PacketXP.binds.getKey() < -1) {
                    if (Mouse.isButtonDown(PacketXP.convertToMouse(PacketXP.binds.getKey())) && mc.currentScreen == null) {
                        return;
                    }
                }
            }
        }
        if(crystalpop.getValue()){
            StormCrystal Stormcrystal = (StormCrystal) Stay.moduleManager.getModuleByDisplayName("StormCrystal");
            if( ModuleManager.getModuleByName("AutoCrystal+").isDisabled()&&ModuleManager.getModuleByName("AutoCrystal").isDisabled()&&((ModuleManager.getModuleByName("StormCrystal").isEnabled()&&Stormcrystal.GhostHand_value.getValue())||ModuleManager.getModuleByName("StormCrystal").isDisabled())){





                if(getItemSlot(Items.TOTEM_OF_UNDYING) != -1&&mc.player.getHeldItemOffhand().getItem()==Items.TOTEM_OF_UNDYING){
                    return;
                }
                if(getItemSlot(Items.TOTEM_OF_UNDYING) != -1&&mc.player.getHeldItemOffhand().getItem()!=Items.TOTEM_OF_UNDYING){
                    switch_Totem();
                    return;
                }
            }
        }
        if (!mc.player.getHeldItemOffhand().isEmpty()) {
            item = mc.player.getHeldItemOffhand().getItem();
        }

        if (item != null) {
            if (item.equals(Items.END_CRYSTAL)) {
                count = crystals;
            } else if (item.equals(Items.TOTEM_OF_UNDYING)) {
                count = totems;
            } else {
                count = gapple;
            }
        } else {
            count = 0;
        }

        Item handItem = mc.player.getHeldItemMainhand().getItem();
        Item offhandItem = mode.getValue() == Mode.Crystal ? Items.END_CRYSTAL : Items.GOLDEN_APPLE;
        Item sOffhandItem = mode.getValue() == Mode.Crystal ? Items.GOLDEN_APPLE : Items.END_CRYSTAL;

        boolean shouldSwitch;
        if (anti32k.getValue()) {
            if (shouldTotem32k() && getItemSlot(Items.TOTEM_OF_UNDYING) != -1&&mc.player.getHeldItemOffhand().getItem()!=Items.TOTEM_OF_UNDYING) {
                switch_Totem();
                return;
            }
            if(mc.player.getHeldItemOffhand().getItem()==Items.TOTEM_OF_UNDYING&&shouldTotem32k()){
                return;
            }
        }
        if (switchMode.getValue() == SwingMode2.Sword) {
            shouldSwitch = mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && Mouse.isButtonDown(1) && autoSwitch.getValue();
        } else {
            shouldSwitch = Mouse.isButtonDown(1)
                    && autoSwitch.getValue()
                    && !(handItem instanceof ItemFood)
                    && !(handItem instanceof ItemExpBottle)
                    && !(handItem instanceof ItemBlock);
        }



        if (shouldTotem() && getItemSlot(Items.TOTEM_OF_UNDYING) != -1) {
            switch_Totem();
        } else {
            if (shouldSwitch && getItemSlot(sOffhandItem) != -1) {
                if (!mc.player.getHeldItemOffhand().getItem().equals(sOffhandItem)) {
                    final int slot = getItemSlot(sOffhandItem) < 9 ? getItemSlot(sOffhandItem) + 36 : getItemSlot(sOffhandItem);
                    switchTo(slot);
                }
            } else if (getItemSlot(offhandItem) != -1) {
                final int slot = getItemSlot(offhandItem) < 9 ? getItemSlot(offhandItem) + 36 : getItemSlot(offhandItem);
                if (!mc.player.getHeldItemOffhand().getItem().equals(offhandItem)) {
                    switchTo(slot);
                }
            }
        }
    }

    private boolean shouldTotem() {
        if (totem.getValue()) {
            return checkHealth()
                    || mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && elytra.getValue()
                    || mc.player.fallDistance >= 5.0f
                    || EntityUtils.isPlayerInHole() && holeCheck.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() <= holeSwitch.getValue()
                    || crystalCalculate.getValue() && calcHealth();
        } else {
            return false;
        }
    }
    private EntityPlayer getTarget(double range, boolean trapped) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtils.isntValid(player, range) || trapped && EntityUtils.isTrapped(player, false, false, false, false, false) || Stay.speedManager.getPlayerSpeed(player) > 10.0)
                continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }
            if (!(mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = mc.player.getDistanceSq(player);
        }
        return target;
    }
    private boolean shouldTotem32k() {
        if (anti32k.getValue()) {
            EntityPlayer IS = getTarget(rs.getValue(),true);
            if (IS != null && IS.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.DIAMOND_SWORD) {
                String nbt = IS.getHeldItem(EnumHand.MAIN_HAND).serializeNBT().copy().toString();
                if (nbt.indexOf("AttributeModifiers:[{UUIDMost:2345838571545327294L,UUIDLeast:-1985342459327194118L,Amount:32767,AttributeName") != -1) {
                    return true;
                }
            }
           return false;
        } else {
            return false;
        }
    }

    private boolean calcHealth() {
        double maxDmg = 0.5;
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (mc.player.getDistance(entity) > 12f) continue;
            double d = CrystalUtil.calculateDamage(entity.posX, entity.posY, entity.posZ, mc.player);
            if (d > maxDmg) maxDmg = d;
        }
        if (maxDmg - 0.5 > mc.player.getHealth() + mc.player.getAbsorptionAmount()) return true;
        return maxDmg > maxSelfDmg.getValue();
    }

    public boolean checkHealth() {
        boolean lowHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount() <= sbHealth.getValue();
        boolean notInHoleAndLowHealth = lowHealth && !EntityUtils.isPlayerInHole();
        return holeCheck.getValue() ? notInHoleAndLowHealth : lowHealth;
    }

    private void switch_Totem() {
        if (totems != 0) {
            if (!mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
                final int slot = getItemSlot(Items.TOTEM_OF_UNDYING) < 9 ? getItemSlot(Items.TOTEM_OF_UNDYING) + 36 : getItemSlot(Items.TOTEM_OF_UNDYING);
                switchTo(slot);
            }
        }
    }

    private void switchTo(int slot) {
        try {
            if (timer.passedMs(delay.getValue())) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                timer.reset();
            }
        } catch (Exception ignored) {
        }
    }

    private int getItemSlot(Item input) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() != input) continue;
            itemSlot = i;
            break;
        }
        return itemSlot;
    }


    @Override
    public void onDisable() {
        if (autoSwitchback.getValue()) {
            switch_Totem();
        }
    }


}
