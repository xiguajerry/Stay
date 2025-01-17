/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.MathUtil;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoMinecart extends Module {
    private final Setting<Boolean> web = register(new Setting<>("Web", Boolean.FALSE));
    private final Setting<Boolean> rotate = register(new Setting<>("Rotate", Boolean.FALSE));
    private final Setting<Boolean> packet = register(new Setting<>("PacketPlace", Boolean.FALSE));
    private final Setting<Integer> blocksPerTick = register(new Setting<>("BlocksPerTick", 1, 1, 4));
    private final Setting<Integer> delay = register(new Setting<>("Carts", 20, 0, 50));
    public Setting<Float> minHP = register(new Setting<>("MinHP", Float.valueOf(4.0f), Float.valueOf(0.0f), Float.valueOf(36.0f)));
    int wait;
    int waitFlint;
    int originalSlot;
    private boolean check;

    public AutoMinecart() {
        super("AutoMinecart", "Places and explodes minecarts on other players.", Module.Category.COMBAT, true, false, false);
    }

    @Override
    public void onEnable() {
        if (AutoMinecart.fullNullCheck()) {
            toggle();
        }
        wait = 0;
        waitFlint = 0;
        originalSlot = AutoMinecart.mc.player.inventory.currentItem;
        check = true;
    }

    @Override
    public void onUpdate() {
        EntityPlayer target;
        if (AutoMinecart.fullNullCheck()) {
            toggle();
        }
        int i = InventoryUtil.findStackInventory(Items.TNT_MINECART);
        for (int j = 0; j < 9; ++j) {
            if (AutoMinecart.mc.player.inventory.getStackInSlot(j).getItem() != Items.AIR || i == -1) continue;
            AutoMinecart.mc.playerController.windowClick(AutoMinecart.mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, AutoMinecart.mc.player);
            AutoMinecart.mc.playerController.updateController();
        }
        int webSlot = InventoryUtil.findHotbarBlock(BlockWeb.class);
        int tntSlot = InventoryUtil.getItemHotbar(Items.TNT_MINECART);
        int flintSlot = InventoryUtil.getItemHotbar(Items.FLINT_AND_STEEL);
        int railSlot = InventoryUtil.findHotbarBlock(Blocks.ACTIVATOR_RAIL);
        int picSlot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
        if (tntSlot == -1 || railSlot == -1 || flintSlot == -1 || picSlot == -1 || web.getValue().booleanValue() && webSlot == -1) {
            Command.sendMessage("<" + getDisplayName() + "> " + ChatFormatting.RED + "No (tnt minecart/activator rail/flint/pic/webs) in hotbar disabling...");
            toggle();
        }
        if ((target = getTarget()) == null) {
            return;
        }
        BlockPos pos = new BlockPos(target.posX, target.posY, target.posZ);
        Vec3d hitVec = new Vec3d(pos).add(0.0, -0.5, 0.0);
        if (AutoMinecart.mc.player.getDistanceSq(pos) <= MathUtil.square(6.0)) {
            check = true;
            if (AutoMinecart.mc.world.getBlockState(pos).getBlock() != Blocks.ACTIVATOR_RAIL && !AutoMinecart.mc.world.getEntitiesWithinAABB(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty()) {
                InventoryUtil.switchToHotbarSlot(flintSlot, false);
                BlockUtil.rightClickBlock(pos.down(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, packet.getValue());
            }
            if (AutoMinecart.mc.world.getBlockState(pos).getBlock() != Blocks.ACTIVATOR_RAIL && AutoMinecart.mc.world.getEntitiesWithinAABB(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty() && AutoMinecart.mc.world.getEntitiesWithinAABB(EntityMinecartTNT.class, new AxisAlignedBB(pos.up())).isEmpty() && AutoMinecart.mc.world.getEntitiesWithinAABB(EntityMinecartTNT.class, new AxisAlignedBB(pos.down())).isEmpty()) {
                InventoryUtil.switchToHotbarSlot(railSlot, false);
                BlockUtil.rightClickBlock(pos.down(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, packet.getValue());
                wait = 0;
            }
            if (web.getValue().booleanValue() && wait != 0 && AutoMinecart.mc.world.getBlockState(pos).getBlock() == Blocks.ACTIVATOR_RAIL && !target.isInWeb && (BlockUtil.isPositionPlaceable(pos.up(), false) == 1 || BlockUtil.isPositionPlaceable(pos.up(), false) == 3) && AutoMinecart.mc.world.getEntitiesWithinAABB(EntityMinecartTNT.class, new AxisAlignedBB(pos.up())).isEmpty()) {
                InventoryUtil.switchToHotbarSlot(webSlot, false);
                BlockUtil.placeBlock(pos.up(), EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), false);
            }
            if (AutoMinecart.mc.world.getBlockState(pos).getBlock() == Blocks.ACTIVATOR_RAIL) {
                InventoryUtil.switchToHotbarSlot(tntSlot, false);
                for (int u = 0; u < blocksPerTick.getValue(); ++u) {
                    BlockUtil.rightClickBlock(pos, hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, packet.getValue());
                }
            }
            if (wait < delay.getValue()) {
                ++wait;
                return;
            }
            check = false;
            wait = 0;
            InventoryUtil.switchToHotbarSlot(picSlot, false);
            if (AutoMinecart.mc.world.getBlockState(pos).getBlock() == Blocks.ACTIVATOR_RAIL && !AutoMinecart.mc.world.getEntitiesWithinAABB(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty()) {
                AutoMinecart.mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
            }
            InventoryUtil.switchToHotbarSlot(flintSlot, false);
            if (AutoMinecart.mc.world.getBlockState(pos).getBlock().getBlockState().getBaseState().getMaterial() != Material.FIRE && !AutoMinecart.mc.world.getEntitiesWithinAABB(EntityMinecartTNT.class, new AxisAlignedBB(pos)).isEmpty()) {
                BlockUtil.rightClickBlock(pos.down(), hitVec, EnumHand.MAIN_HAND, EnumFacing.UP, packet.getValue());
            }
        }
    }

    @Override
    public String getDisplayInfo() {
        if (check) {
            return ChatFormatting.GREEN + "Placing";
        }
        return ChatFormatting.RED + "Breaking";
    }

    @Override
    public void onDisable() {
        AutoMinecart.mc.player.inventory.currentItem = originalSlot;
    }

    private EntityPlayer getTarget() {
        EntityPlayer target = null;
        double distance = Math.pow(6.0, 2.0) + 1.0;
        for (EntityPlayer player : AutoMinecart.mc.world.playerEntities) {
            if (EntityUtils.isntValid(player, 6.0) || player.isInWater() || player.isInLava() || !EntityUtils.isTrapped(player, false, false, false, false, false) || player.getHealth() + player.getAbsorptionAmount() > minHP.getValue().floatValue())
                continue;
            if (target == null) {
                target = player;
                distance = AutoMinecart.mc.player.getDistanceSq(player);
                continue;
            }
            if (!(AutoMinecart.mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = AutoMinecart.mc.player.getDistanceSq(player);
        }
        return target;
    }
}

