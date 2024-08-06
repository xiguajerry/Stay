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
import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.event.ProcessRightClickBlockEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Offhand extends Module {
    private static Offhand instance;
    private final Queue<InventoryUtil.Task> taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
    private final Timer timer = new Timer();
    private final Timer secondTimer = new Timer();
    public Setting<Boolean> crystal = register(new Setting<>("Crystal", true));
    public Setting<Float> crystalHealth = register(new Setting<>("CrystalHP", Float.valueOf(13.0f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Float> crystalHoleHealth = register(new Setting<>("CrystalHoleHP", Float.valueOf(3.5f), Float.valueOf(0.1f), Float.valueOf(36.0f)));
    public Setting<Boolean> gapple = register(new Setting<>("Gapple", true));
    public Setting<Boolean> armorCheck = register(new Setting<>("ArmorCheck", true));
    public Setting<Boolean> Anti32k = register(new Setting<>("Anti32k", true));
    public Setting<Float> Anti32khp = register(new Setting<>("Anti32kHP", Float.valueOf(25.0f), Float.valueOf(0.1f), Float.valueOf(36.0f), v -> Anti32k.getValue()));
    public Setting<Integer> actions = register(new Setting<>("Packets", 4, 1, 4));
    public Mode2 currentMode = Mode2.TOTEMS;
    public int totems = 0;
    public int crystals = 0;
    public int gapples = 0;
    public int lastTotemSlot = -1;
    public int lastGappleSlot = -1;
    public int lastCrystalSlot = -1;
    public int lastObbySlot = -1;
    public int lastWebSlot = -1;
    public boolean holdingCrystal = false;
    public boolean holdingTotem = false;
    public boolean holdingGapple = false;
    public boolean didSwitchThisTick = false;
    private boolean second = false;
    private boolean switchedForHealthReason = false;

    public Offhand() {
        super("Offhand", "Allows you to switch up your Offhand.", Module.Category.COMBAT, true, false, false);
        instance = this;
    }

    public static Offhand getInstance() {
        if (instance == null) {
            instance = new Offhand();
        }
        return instance;
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
    @SubscribeEvent
    public void onUpdateWalkingPlayer(ProcessRightClickBlockEvent event) {
        if (event.getHand() == EnumHand.MAIN_HAND && event.getStack().getItem() == Items.END_CRYSTAL && Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhand.mc.objectMouseOver != null && event.getPos() == Offhand.mc.objectMouseOver.getBlockPos()) {
            event.setCanceled(true);
            Offhand.mc.player.setActiveHand(EnumHand.OFF_HAND);
            Offhand.mc.playerController.processRightClick(Offhand.mc.player, Offhand.mc.world, EnumHand.OFF_HAND);
        }
    }
    private Boolean cuican;

    @Override
    public void onUpdate() {
        if (mc.currentScreen instanceof GuiContainer) {
            return;
        }

        if (timer.passedMs(50L)) {
            if (Offhand.mc.player != null && Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhand.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && Mouse.isButtonDown(1)) {
                Offhand.mc.player.setActiveHand(EnumHand.OFF_HAND);
                Offhand.mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        } else if (Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhand.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            Offhand.mc.gameSettings.keyBindUseItem.pressed = false;
        }
        if (Offhand.nullCheck()) {
            return;
        }
        if(Anti32k.getValue()){
            EntityPlayer IS =getTarget(15,true);

            if(EntityUtils.getHealth(Offhand.mc.player, true)<=Anti32khp.getValue()&&IS!=null&&IS.getHeldItem(EnumHand.MAIN_HAND).getItem()==Items.DIAMOND_SWORD){
                String nbt=  IS.getHeldItem(EnumHand.MAIN_HAND).serializeNBT().copy().toString();

                if(nbt.indexOf("AttributeModifiers:[{UUIDMost:2345838571545327294L,UUIDLeast:-1985342459327194118L,Amount:32767,AttributeName")!=-1){

                    fck();

                    return;
                }
            }





        }
        doOffhand();
        if (secondTimer.passedMs(50L) && second) {
            second = false;
            timer.reset();
        }
    }
    boolean moving = false;
    boolean returnI = false;
    public void fck() {
        int i;
        int t;
        this.totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totems;
        } else {


            if (mc.player.inventory.getItemStack().isEmpty()) {
                if (this.totems == 0) {
                    return;
                }

                t = -1;
                for (i = 0; i < 45; ++i) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() != Items.TOTEM_OF_UNDYING) continue;
                    t = i;
                    break;
                }
                if (t == -1) {
                    return;
                }
                setMode(Mode2.TOTEMS);
                lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING, false);
                int lastSlot = getLastSlot( Offhand.mc.player.getHeldItemOffhand().getItem(), lastTotemSlot);
                putItemInOffhand(lastTotemSlot, lastSlot);
            }
        }





        if (this.returnI) {
            t = -1;
            for (i = 0; i < 45; ++i) {
                if (!mc.player.inventory.getStackInSlot((int)i).isEmpty) continue;
                t = i;
                break;
            }
            if (t == -1) {
                return;
            }
            mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            this.returnI = false;
        }
    }
    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (!Offhand.fullNullCheck() && Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE && Offhand.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            CPacketPlayerTryUseItem packet;
            if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
                CPacketPlayerTryUseItemOnBlock packet2 = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
                if (packet2.getHand() == EnumHand.MAIN_HAND) {
                    if (timer.passedMs(50L)) {
                        Offhand.mc.player.setActiveHand(EnumHand.OFF_HAND);
                        Offhand.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                    }
                    event.setCanceled(true);
                }
            } else if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet = (CPacketPlayerTryUseItem) event.getPacket()).getHand() == EnumHand.OFF_HAND && !timer.passedMs(50L)) {
                event.setCanceled(true);
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (Offhand.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return "Crystal";
        }
        if (Offhand.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            return "Totem";
        }
        if (Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            return "Gapple";
        }
        return null;
    }






    public void doOffhand() {
        didSwitchThisTick = false;
        holdingCrystal = Offhand.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL;
        holdingTotem = Offhand.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING;
        holdingGapple = Offhand.mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE;
        totems = Offhand.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (holdingTotem) {
            totems += Offhand.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        }
        crystals = Offhand.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (holdingCrystal) {
            crystals += Offhand.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        }
        gapples = Offhand.mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (holdingGapple) {
            gapples += Offhand.mc.player.inventory.offHandInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        }
        doSwitch();
    }

    public void doSwitch() {
        currentMode = Mode2.TOTEMS;
        if (gapple.getValue().booleanValue() && Offhand.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && Offhand.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            currentMode = Mode2.GAPPLES;
        } else if (currentMode != Mode2.CRYSTALS && crystal.getValue().booleanValue() && (EntityUtils.isSafe(Offhand.mc.player) && EntityUtils.getHealth(Offhand.mc.player, true) > crystalHoleHealth.getValue().floatValue() || EntityUtils.getHealth(Offhand.mc.player, true) > crystalHealth.getValue().floatValue())) {
            currentMode = Mode2.CRYSTALS;
        }
        if (currentMode == Mode2.CRYSTALS && crystals == 0) {
            setMode(Mode2.TOTEMS);
        }
        if (currentMode == Mode2.CRYSTALS && (!EntityUtils.isSafe(Offhand.mc.player) && EntityUtils.getHealth(Offhand.mc.player, true) <= crystalHealth.getValue().floatValue() || EntityUtils.getHealth(Offhand.mc.player, true) <= crystalHoleHealth.getValue().floatValue())) {
            if (currentMode == Mode2.CRYSTALS) {
                switchedForHealthReason = true;
            }
            setMode(Mode2.TOTEMS);
        }
        if (switchedForHealthReason && (EntityUtils.isSafe(Offhand.mc.player) && EntityUtils.getHealth(Offhand.mc.player, true) > crystalHoleHealth.getValue().floatValue() || EntityUtils.getHealth(Offhand.mc.player, true) > crystalHealth.getValue().floatValue())) {
            setMode(Mode2.CRYSTALS);
            switchedForHealthReason = false;
        }
        if (currentMode == Mode2.CRYSTALS && armorCheck.getValue().booleanValue() && (Offhand.mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.AIR || Offhand.mc.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == Items.AIR || Offhand.mc.player.getItemStackFromSlot(EntityEquipmentSlot.LEGS).getItem() == Items.AIR || Offhand.mc.player.getItemStackFromSlot(EntityEquipmentSlot.FEET).getItem() == Items.AIR)) {
            setMode(Mode2.TOTEMS);
        }
        if (Offhand.mc.currentScreen instanceof GuiContainer && !(Offhand.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        Item currentOffhandItem = Offhand.mc.player.getHeldItemOffhand().getItem();
        switch (currentMode) {
            case TOTEMS: {
                if (totems <= 0 || holdingTotem) break;
                lastTotemSlot = InventoryUtil.findItemInventorySlot(Items.TOTEM_OF_UNDYING, false);
                int lastSlot = getLastSlot(currentOffhandItem, lastTotemSlot);
                putItemInOffhand(lastTotemSlot, lastSlot);
                break;
            }
            case GAPPLES: {
                if (gapples <= 0 || holdingGapple) break;
                lastGappleSlot = InventoryUtil.findItemInventorySlot(Items.GOLDEN_APPLE, false);
                int lastSlot = getLastSlot(currentOffhandItem, lastGappleSlot);
                putItemInOffhand(lastGappleSlot, lastSlot);
                break;
            }
            default: {
                if (crystals <= 0 || holdingCrystal) break;
                lastCrystalSlot = InventoryUtil.findItemInventorySlot(Items.END_CRYSTAL, false);
                int lastSlot = getLastSlot(currentOffhandItem, lastCrystalSlot);
                putItemInOffhand(lastCrystalSlot, lastSlot);
            }
        }
        for (int i = 0; i < actions.getValue(); ++i) {
            InventoryUtil.Task task = taskList.poll();
            if (task == null) continue;
            task.run();
            if (!task.isSwitching()) continue;
            didSwitchThisTick = true;
        }
    }

    private int getLastSlot(Item item, int slotIn) {
        if (item == Items.END_CRYSTAL) {
            return lastCrystalSlot;
        }
        if (item == Items.GOLDEN_APPLE) {
            return lastGappleSlot;
        }
        if (item == Items.TOTEM_OF_UNDYING) {
            return lastTotemSlot;
        }
        if (InventoryUtil.isBlock(item, BlockObsidian.class)) {
            return lastObbySlot;
        }
        if (InventoryUtil.isBlock(item, BlockWeb.class)) {
            return lastWebSlot;
        }
        if (item == Items.AIR) {
            return -1;
        }
        return slotIn;
    }

    private void putItemInOffhand(int slotIn, int slotOut) {
        if (slotIn != -1 && taskList.isEmpty()) {
            taskList.add(new InventoryUtil.Task(slotIn));
            taskList.add(new InventoryUtil.Task(45));
            taskList.add(new InventoryUtil.Task(slotOut));
            taskList.add(new InventoryUtil.Task());
        }
    }

    public void setMode(Mode2 mode) {
        currentMode = currentMode == mode ? Mode2.TOTEMS : mode;
    }

    public enum Mode2 {
        TOTEMS,
        GAPPLES,
        CRYSTALS

    }
}

