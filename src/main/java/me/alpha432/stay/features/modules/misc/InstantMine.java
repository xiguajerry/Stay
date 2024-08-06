/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.init.Blocks
 *  net.minecraft.init.Items
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayerDigging
 *  net.minecraft.network.play.client.CPacketPlayerDigging$Action
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.stay.features.modules.misc;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.event.PlayerDamageBlockEvent;
import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.combat.AutoCev;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import me.alpha432.stay.util.graphics.animations.AnimationFlag;
import me.alpha432.stay.util.graphics.animations.Easing;
import me.alpha432.stay.util.graphics.color.Colour;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.world.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class InstantMine
        extends Module {
    private final Timer breakSuccess = new Timer();
    private static InstantMine INSTANCE = new InstantMine();
    private final Setting<Boolean> creativeMode = this.register(new Setting<>("CreativeMode", true));
    private final Setting<Boolean> ghostHand = this.register(new Setting<>("GhostHand", Boolean.TRUE, v -> this.creativeMode.getValue()));
    public Setting<Boolean> fuck = this.register(new Setting<>("Super ghost hand", Boolean.TRUE, v -> this.ghostHand.getValue()));
    private final Setting<Boolean> render = this.register(new Setting<>("Render", true));

    private final Setting<Integer> redBefore = this.register(new Setting<>("RedBefore", 255, 0, 255, v -> render.getValue()));
    private final Setting<Integer> greenBefore = this.register(new Setting<>("GreenBefore", 0, 0, 255, v -> render.getValue()));
    private final Setting<Integer> blueBefore = this.register(new Setting<>("BlueBefore", 44, 0, 255, v -> render.getValue()));
    private final Setting<Integer> alphaBefore = this.register(new Setting<>("AlphaBefore", 127, 0, 255, v -> render.getValue()));

    private final Setting<Integer> redAfter = this.register(new Setting<>("RedAfter", 0, 0, 255, v -> render.getValue()));
    private final Setting<Integer> greenAfter = this.register(new Setting<>("GreenAfter", 255, 0, 255, v -> render.getValue()));
    private final Setting<Integer> blueAfter = this.register(new Setting<>("BlueAfter", 44, 0, 255, v -> render.getValue()));
    private final Setting<Integer> alphaAfter = this.register(new Setting<>("AlphaAfter", 127, 0, 255, v -> render.getValue()));
    private final List<Block> godBlocks = Arrays.asList(Blocks.AIR, Blocks.FLOWING_LAVA, Blocks.LAVA, Blocks.FLOWING_WATER, Blocks.WATER, Blocks.BEDROCK);
    private boolean cancelStart = false;
    private boolean empty = false;
    private EnumFacing facing;
    public static BlockPos breakPos;

    public InstantMine() {
        super("InstantMineOld", "InstantMine", Category.MISC, true, false, false);
        this.setInstance();
    }

    public static InstantMine getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new InstantMine();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (InstantMine.fullNullCheck()) {
            return;
        }
        if(Stay.moduleManager.getModuleT(AutoCev.class).isEnabled()) return;
        if (!this.creativeMode.getValue()) return;
        if (!this.cancelStart) return;
        if(InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE) == -1){
            return;
        }
//        if(!fuck.getValue()){
//            if(InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1){
//                return;
//            }
//        }

        if (this.godBlocks.contains(InstantMine.mc.world.getBlockState(breakPos).getBlock())) return;
        if (this.ghostHand.getValue() &&(fuck.getValue()||InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) != -1) &&InventoryUtil.getItemHotbars(Items.DIAMOND_PICKAXE) != -1) {
            int slotMain = InstantMine.mc.player.inventory.currentItem;
            if (InstantMine.mc.world.getBlockState(breakPos).getBlock() == Blocks.OBSIDIAN) {
                if (!this.breakSuccess.passedMs(1234L)) return;
                if(fuck.getValue()){
                    if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) ==-1) {
                        for(int i = 9; i < 36; ++i) {
                            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_PICKAXE) {
                                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                                InstantMine.mc.playerController.updateController();
                                InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
                                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                                InstantMine.mc.playerController.updateController();
                                return;
                            }
                        }
                        return;
                    }
                }

               InstantMine.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
               InstantMine.mc.playerController.updateController();
               InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
               InstantMine.mc.player.inventory.currentItem = slotMain;
               InstantMine.mc.playerController.updateController();
                return;
            }
            if(fuck.getValue()){
                if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) ==-1) {
                    for(int i = 9; i < 35; ++i) {
                        if (mc.player.inventory.getStackInSlot(i).getItem() == Items.DIAMOND_PICKAXE) {
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                            InstantMine.mc.playerController.updateController();
                            InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                            InstantMine.mc.playerController.updateController();
                            return;
                        }
                    }
                    return;

                }
            }
           InstantMine.mc.player.inventory.currentItem = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE);
           InstantMine.mc.playerController.updateController();
           InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
           InstantMine.mc.player.inventory.currentItem = slotMain;
           InstantMine.mc.playerController.updateController();
            return;
        }
       InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
    }

    double manxi=0;
    AnimationFlag animationFlag = new AnimationFlag(Easing.OUT_QUINT, 5000);

    @Override
    public void onRender3D(Render3DEvent event) {
        if (InstantMine.fullNullCheck()) {
            return;
        }
        if (this.render.getValue() && this.creativeMode.getValue() && this.cancelStart) {
            if (this.godBlocks.contains(InstantMine.mc.world.getBlockState(breakPos).getBlock())) {
                this.empty = true;
            }


            if (imerS.passedMs(11)){
                if(manxi<=10){
                    manxi = animationFlag.getAndUpdate(11);
                }
                imerS.reset();
            }

            AxisAlignedBB axisAlignedBB = mc.world.getBlockState(breakPos).getSelectedBoundingBox(mc.world, breakPos);

//            GSColor fillColor = new GSColor(new GSColor(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255), 50);
//            GSColor outlineColor = new GSColor(new GSColor(this.empty ? 0 : 255, this.empty ? 255 : 0, 0, 255), 255);
            double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
            double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
            double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
            double progressValX = manxi * ((axisAlignedBB.maxX - centerX) / 10);
            double progressValY = manxi * ((axisAlignedBB.maxY - centerY) / 10);
            double progressValZ = manxi * ((axisAlignedBB.maxZ - centerZ) / 10);
            AxisAlignedBB axisAlignedBB1 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);

            if (empty) {
                RenderUtil.drawBBBox(axisAlignedBB1,
                        new Colour(redAfter.getValue(), greenAfter.getValue(), blueAfter.getValue(), alphaAfter.getValue()),
                        alphaAfter.getValue());
            } else {
                RenderUtil.drawBBBox(axisAlignedBB1,
                        new Colour(redBefore.getValue(), greenBefore.getValue(), blueBefore.getValue(), alphaBefore.getValue()),
                        alphaBefore.getValue());
            }


            return;
        }
        if (!this.cancelStart) return;
        if (!this.render.getValue()) return;
        RenderUtil.drawBoxESP(breakPos, new Color (236, 235, 235), false, new Color(248, 248, 248, 148), 1.0f, true, true, 84, false);
    }


    public final Timer imerS = new Timer();
    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (InstantMine.fullNullCheck()) {
            return;
        }
        if (!(event.getPacket() instanceof CPacketPlayerDigging)) return;
        CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
        if (packet.getAction() != CPacketPlayerDigging.Action.START_DESTROY_BLOCK) return;
        event.setCanceled(this.cancelStart);
    }
    long times = 0;

    @SubscribeEvent
    public void onBlockEvent(PlayerDamageBlockEvent event) {
        if (InstantMine.fullNullCheck()) {
            return;
        }
        if (!BlockUtil.canBreak(event.getPos())) return;
        if(breakPos!=null){
            if(breakPos.getX()== event.getPos().getX()&&breakPos.getY()== event.getPos().getY()&&breakPos.getZ()== event.getPos().getZ()){

                return;
            }
        }

        animationFlag.forceUpdate(0, 0);
        manxi = 0;
        this.empty = false;
        this.cancelStart = false;
        breakPos = event.getPos();
        this.breakSuccess.reset();
        this.facing = event.getFacing();
        if (breakPos == null) return;
       InstantMine.mc.player.swingArm(EnumHand.MAIN_HAND);
       InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, breakPos, this.facing));
        this.cancelStart = true;
       InstantMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, breakPos, this.facing));
        event.setCanceled(true);
    }

    @Override
    public String getDisplayInfo() {
        if (this.ghostHand.getValue() == false) return "Normal";
        return "Ghost";
    }
}

