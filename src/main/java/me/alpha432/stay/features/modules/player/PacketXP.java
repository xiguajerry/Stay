/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.player;


import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Bind;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.math.MathUtil;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 * @author Madmegsox1
 * @since 30/04/2021
 */

public class PacketXP extends Module {
    public PacketXP() {
        super("PacketXP", "Allows you to XP instantly", Category.PLAYER, false, false, false);
    }

    public  final Setting<Boolean> allowTakeOff = register(new Setting<>("AutoMend", false));




    private final Setting<Integer> takeOffVal = this.register(new Setting<>("Durable%", 100, 0, 100));
    private final Setting<Integer> delay = this.register(new Setting<>("Delay", 0, 0, 5));
    private final Setting<Bind> bind = this.register(new Setting<>("PacketBind", new Bind(-1)));
    private int delay_count;
    int prvSlot;
    public static   Boolean inft=false;
    public static Bind binds;
    private void rotateToPos(BlockPos pos) {
        float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() - 0.5f, (float) pos.getZ() + 0.5f));
        Stay.rotationManager.setPlayerRotations(angle[0], angle[1]);
        return;
    }

    @Override
    public void onEnable() {
        delay_count = 0;
        return;
    }

    @Override
    public void onUpdate() {
        inft=allowTakeOff.getValue();
        binds=bind.getValue();

        if (findExpInHotbar() == -1) {
            return;
        }

        if (bind.getValue().getKey() > -1) {
            if (Keyboard.isKeyDown(bind.getValue().getKey()) && mc.currentScreen == null) {
                if(findExpInHotbar()==-1){
                    return;
                }
                usedXp();
            }
        } else if (bind.getValue().getKey() < -1) {
            if (Mouse.isButtonDown(convertToMouse(bind.getValue().getKey())) && mc.currentScreen == null) {
                if(findExpInHotbar()==-1){
                    return;
                }
                usedXp();

            }
        }

    }

    public static int convertToMouse(int key) {
        switch (key) {
            case -2:
                return 0;
            case -3:
                return 1;
            case -4:
                return 2;
            case -5:
                return 3;
            case -6:
                return 4;
        }
        return -1;
    }

    private int findExpInHotbar() {
        int slot = 0;
        for (int i = 0; i < 9; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.EXPERIENCE_BOTTLE) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private void usedXp() {


        float pitch =  mc.player.rotationPitch;
        float yawln = mc.player.rotationYaw;
        prvSlot = mc.player.inventory.currentItem; //TODO add better rotations
        mc.player.connection.sendPacket(new CPacketHeldItemChange(findExpInHotbar()));
        mc.player.rotationPitch = 90;
//        Stay.rotationManager.setPlayerRotations(-90,90);
//        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(0, 90f, false));
        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.player.connection.sendPacket(new CPacketHeldItemChange(prvSlot));
        mc.player.rotationPitch = pitch;
//        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(yawln, pitch, false));
        if (allowTakeOff.getValue()) {
            takeArmorOff(); //TODO travis add the ArmourMend take off thing
        }

    }

    private ItemStack getArmor(int first) {
        return mc.player.inventoryContainer.getInventory().get(first);
    }

    private void takeArmorOff() {
        int slot = 5;
        while (slot <= 8) {
            ItemStack item;
            item = getArmor(slot);
            double max_dam = item.getMaxDamage();
            double dam_left = item.getMaxDamage() - item.getItemDamage();
            double percent = (dam_left / max_dam) * 100;

            if (percent >= takeOffVal.getValue() && !item.equals(Items.AIR)) {
                if (!notInInv(Items.AIR)) {
                    return;
                }
                if (delay_count < delay.getValue()) {
                    delay_count++;
                    return;
                }
                delay_count = 0;

                mc.playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, mc.player);

            }
            slot++;
        }
    }

    public Boolean notInInv(Item itemOfChoice) {
        int n;
        n = 0;
        if (itemOfChoice == mc.player.getHeldItemOffhand().getItem()) return true;

        for (int i = 35; i >= 0; i--) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == itemOfChoice) {
                return true;

            } else if (item != itemOfChoice) {
                n++;
            }
        }
        if (n >= 35) {

            return false;
        }
        return true;
    }


}
