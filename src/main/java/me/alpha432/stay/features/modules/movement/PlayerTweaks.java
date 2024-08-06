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
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketExplosion
 *  net.minecraftforge.client.event.InputUpdateEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.stay.features.modules.movement;


import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.event.PushEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerTweaks
extends Module {
    public Setting<Boolean> noSlow = this.register(new Setting<>("No Slow", true));
    public Setting<Boolean> antiKnockBack = this.register(new Setting<>("Velocity", true));
    public Setting<Boolean> noEntityPush = this.register(new Setting<>("No PlayerPush", true));
    public Setting<Boolean> noBlockPush = this.register(new Setting<>("No BlockPush", true));
    public Setting<Boolean> noWaterPush = this.register(new Setting<>("No LiquidPush", true));
    public Setting<Boolean> guiMove = this.register(new Setting<>("Gui Move", true));
    public static PlayerTweaks INSTANCE = new PlayerTweaks();

    public PlayerTweaks() {
        super("PlayerTweaks", "XD", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static PlayerTweaks getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new PlayerTweaks();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void Slow(InputUpdateEvent event) {
        if (this.noSlow.getValue() == false) return;
        if (!PlayerTweaks.mc.player.isHandActive()) return;
        if (PlayerTweaks.mc.player.isRiding()) return;
        event.getMovementInput().moveStrafe *= 5.0f;
        event.getMovementInput().moveForward *= 5.0f;
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (PlayerTweaks.fullNullCheck()) {
            return;
        }
        if (this.antiKnockBack.getValue() == false) return;
        if (event.getPacket() instanceof SPacketEntityVelocity && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == PlayerTweaks.mc.player.getEntityId()) {
            event.setCanceled(true);
        }
        if (!(event.getPacket() instanceof SPacketExplosion)) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (PlayerTweaks.fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0 && this.noEntityPush.getValue() && event.getEntity().equals(PlayerTweaks.mc.player)) {
            event.setX(-event.getX() * 0.0);
            event.setY(-event.getY() * 0.0);
            event.setZ(-event.getZ() * 0.0);
            return;
        }
        if (event.getStage() == 1 && this.noBlockPush.getValue()) {
            event.setCanceled(true);
            return;
        }
        if (event.getStage() != 2) return;
        if (this.noWaterPush.getValue() == false) return;
        if (PlayerTweaks.mc.player == null) return;
        if (!PlayerTweaks.mc.player.equals(event.getEntity())) return;
        event.setCanceled(true);
    }
}

