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
import me.alpha432.stay.event.BlockEvent;
import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.combat.Surround;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class Speedmine extends Module {
    private static Speedmine INSTANCE = new Speedmine();
    private final Timer timer = new Timer();
    public Setting<Mode> mode = register(new Setting<>("Mode", Mode.PACKET));
    public Setting<Float> damage = register(new Setting<>("Damage", Float.valueOf(0.7f), Float.valueOf(0.0f), Float.valueOf(1.0f), v -> mode.getValue() == Mode.DAMAGE));
    public Setting<Boolean> webSwitch = register(new Setting<>("WebSwitch", false));
    public Setting<Boolean> doubleBreak = register(new Setting<>("DoubleBreak", false));
    public Setting<Boolean> autosw = register(new Setting<>("AutoSwitch", false));
    public Setting<Boolean> render = register(new Setting<>("Render", false));
    public Setting<Boolean> box = register(new Setting<>("Box", Boolean.valueOf(false), v -> render.getValue()));
    private final Setting<Integer> boxAlpha = register(new Setting<>("BoxAlpha", Integer.valueOf(85), Integer.valueOf(0), Integer.valueOf(255), v -> box.getValue() != false && render.getValue() != false));
    public Setting<Boolean> outline = register(new Setting<>("Outline", Boolean.valueOf(true), v -> render.getValue()));
    private final Setting<Float> lineWidth = register(new Setting<>("Width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> outline.getValue() != false && render.getValue() != false));
    public BlockPos currentPos;
    public IBlockState currentBlockState;
    private int lasthotbarslot;

    public Speedmine() {
        super("SpeedMine", "Speeds up mining.", Module.Category.PLAYER, true, false, false);
        setInstance();
    }

    public static Speedmine getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Speedmine();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onTick() {
        if (currentPos != null) {
            if (!Speedmine.mc.world.getBlockState(currentPos).equals(currentBlockState) || Speedmine.mc.world.getBlockState(currentPos).getBlock() == Blocks.AIR) {
                currentPos = null;
                currentBlockState = null;
            } else if (webSwitch.getValue().booleanValue() && currentBlockState.getBlock() == Blocks.WEB && Speedmine.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
                InventoryUtil.switchToHotbarSlot(ItemSword.class, false);
            }
        }
    }

    @Override
    public void onUpdate() {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        Speedmine.mc.playerController.blockHitDelay = 0;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (render.getValue().booleanValue() && currentPos != null && currentBlockState.getBlock() == Blocks.OBSIDIAN) {
            Color color = new Color(timer.passedMs((int) (2000.0f * Stay.serverManager.getTpsFactor())) ? 0 : 255, timer.passedMs((int) (2000.0f * Stay.serverManager.getTpsFactor())) ? 255 : 0, 0, 255);
            RenderUtil.drawBoxESP(currentPos, color, false, color, lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), false);
            if (autosw.getValue()) {
                boolean hasPickaxe = mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE;
                if (!hasPickaxe) {
                    for (int i = 0; i < 9; ++i) {
                        ItemStack stack = mc.player.inventory.getStackInSlot(i);
                        if (stack.isEmpty())
                            continue;
                        lasthotbarslot = Surround.mc.player.inventory.currentItem;
                        if (Speedmine.mc.player.inventory.currentItem != lasthotbarslot) {
                            lasthotbarslot = Speedmine.mc.player.inventory.currentItem;
                        }
                        if (stack.getItem() == Items.DIAMOND_PICKAXE) {
                            hasPickaxe = true;
                            mc.player.inventory.currentItem = i;
                            mc.playerController.updateController();
                            break;
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onBlockEvent(BlockEvent event) {
        if (Speedmine.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 3 && Speedmine.mc.playerController.curBlockDamageMP > 0.1f) {
            Speedmine.mc.playerController.isHittingBlock = true;
        }
        if (event.getStage() == 4) {
            BlockPos above;
            if (BlockUtil.canBreak(event.getPos())) {
                Speedmine.mc.playerController.isHittingBlock = false;
                switch (mode.getValue()) {
                    case PACKET: {
                        if (currentPos == null) {
                            currentPos = event.getPos();
                            currentBlockState = Speedmine.mc.world.getBlockState(currentPos);
                            timer.reset();
                        }
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                        event.setCanceled(true);

                        break;
                    }
                    case DAMAGE: {
                        if (!(Speedmine.mc.playerController.curBlockDamageMP >= damage.getValue()))
                            break;
                        Speedmine.mc.playerController.curBlockDamageMP = 1.0f;
                        break;
                    }
                    case INSTANT: {
                        Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                        Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
                        Speedmine.mc.playerController.onPlayerDestroyBlock(event.getPos());
                        Speedmine.mc.world.setBlockToAir(event.getPos());
                    }
                }
            }
            if (doubleBreak.getValue().booleanValue() && BlockUtil.canBreak(above = event.getPos().add(0, 1, 0)) && Speedmine.mc.player.getDistance(above.getX(), above.getY(), above.getZ()) <= 5.0) {
                Speedmine.mc.player.swingArm(EnumHand.MAIN_HAND);
                Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, above, event.getFacing()));
                Speedmine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, above, event.getFacing()));
                Speedmine.mc.playerController.onPlayerDestroyBlock(above);
                Speedmine.mc.world.setBlockToAir(above);
            }
        }
    }


    @Override
    public String getDisplayInfo() {
        return mode.currentEnumName();
    }

    public enum Mode {
        PACKET,
        DAMAGE,
        INSTANT

    }

}

