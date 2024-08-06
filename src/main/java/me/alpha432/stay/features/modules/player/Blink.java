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
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 *  net.minecraft.network.play.client.CPacketClientStatus
 *  net.minecraft.network.play.client.CPacketConfirmTeleport
 *  net.minecraft.network.play.client.CPacketKeepAlive
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketTabComplete
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.stay.features.modules.player;


import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.math.MathUtil;
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Blink
extends Module {
    private Setting<Boolean> cPacketPlayer = this.register(new Setting<>("CPacketPlayer", true));
    private Setting<Mode> autoOff = this.register(new Setting<>("AutoOff", Mode.MANUAL));
    private Setting<Integer> timeLimit = this.register(new Setting<>("Time", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> {
        if (this.autoOff.getValue() != Mode.TIME) return false;
        return true;
    }));
    private Setting<Integer> packetLimit = this.register(new Setting<>("Packets", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> {
        if (this.autoOff.getValue() != Mode.PACKETS) return false;
        return true;
    }));
    private Setting<Float> distance = this.register(new Setting<>("Distance", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(100.0f), v -> {
        if (this.autoOff.getValue() != Mode.DISTANCE) return false;
        return true;
    }));
    private Timer timer = new Timer();
    private Queue<Packet<?>> packets = new ConcurrentLinkedQueue();
    private EntityOtherPlayerMP entity;
    private int packetsCanceled = 0;
    private BlockPos startPos = null;
    private static Blink INSTANCE = new Blink();

    public Blink() {
        super("Blink", "Fakelag.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Blink getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new Blink();
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        if (!Blink.fullNullCheck()) {
            this.entity = new EntityOtherPlayerMP(Blink.mc.world, Blink.mc.getSession().getProfile());
            this.entity.copyLocationAndAnglesFrom(Blink.mc.player);
            this.entity.rotationYaw = Blink.mc.player.rotationYaw;
            this.entity.rotationYawHead = Blink.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(Blink.mc.player.inventory);
            Blink.mc.world.addEntityToWorld(6942069, this.entity);
            this.startPos = Blink.mc.player.getPosition();
        } else {
            this.disable();
        }
        this.packetsCanceled = 0;
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (!(Blink.nullCheck() || this.autoOff.getValue() == Mode.TIME && this.timer.passedS(this.timeLimit.getValue().intValue()) || this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && Blink.mc.player.getDistanceSq(this.startPos) >= MathUtil.square(this.distance.getValue().floatValue()))) {
            if (this.autoOff.getValue() != Mode.PACKETS) return;
            if (this.packetsCanceled < this.packetLimit.getValue()) return;
        }
        this.disable();
    }

    @Override
    public void onLogout() {
        if (!this.isOn()) return;
        this.disable();
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (Blink.mc.world == null) return;
        if (mc.isSingleplayer()) return;
        Object packet = event.getPacket();
        if (this.cPacketPlayer.getValue().booleanValue() && packet instanceof CPacketPlayer) {
            event.setCanceled(true);
            this.packets.add((Packet)packet);
            ++this.packetsCanceled;
        }
        if (this.cPacketPlayer.getValue() != false) return;
        if (packet instanceof CPacketChatMessage) return;
        if (packet instanceof CPacketConfirmTeleport) return;
        if (packet instanceof CPacketKeepAlive) return;
        if (packet instanceof CPacketTabComplete) return;
        if (packet instanceof CPacketClientStatus) {
            return;
        }
        this.packets.add((Packet)packet);
        event.setCanceled(true);
        ++this.packetsCanceled;
    }

    @Override
    public void onDisable() {
        if (!Blink.fullNullCheck()) {
            Blink.mc.world.removeEntity((Entity)this.entity);
            while (!this.packets.isEmpty()) {
                Blink.mc.player.connection.sendPacket(this.packets.poll());
            }
        }
        this.startPos = null;
    }

    @Override
    public String getDisplayInfo() {
        if (this.packets == null) return null;
        return this.packets.size() + "";
    }

    public static enum Mode {
        MANUAL,
        TIME,
        DISTANCE,
        PACKETS;

    }
}

