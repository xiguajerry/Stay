/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.movement;

import io.netty.util.internal.ConcurrentSet;

import me.alpha432.stay.event.MoveEvent;
import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.event.PushEvent;
import me.alpha432.stay.event.MotionUpdateEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

public class Phase
        extends Module {
    private static Phase INSTANCE = new Phase();
    public Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.PACKETFLY));
    public Setting<PacketFlyMode> type = this.register(new Setting<>("Type", PacketFlyMode.SETBACK, v -> this.mode.getValue() == Mode.PACKETFLY));
    public Setting<Integer> xMove = this.register(new Setting<>("HMove", 625, 1, 1000, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK, "XMovement speed."));
    public Setting<Integer> yMove = this.register(new Setting<>("YMove", 625, 1, 1000, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK, "YMovement speed."));
    public Setting<Boolean> extra = this.register(new Setting<>("ExtraPacket", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Integer> offset = this.register(new Setting<>("Offset", 1337, -1337, 1337, v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.extra.getValue() != false, "Up speed."));
    public Setting<Boolean> fallPacket = this.register(new Setting<>("FallPacket", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> teleporter = this.register(new Setting<>("Teleport", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> boundingBox = this.register(new Setting<>("BoundingBox", Boolean.valueOf(true), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Integer> teleportConfirm = this.register(new Setting<>("Confirm", Integer.valueOf(2), Integer.valueOf(0), Integer.valueOf(4), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> ultraPacket = this.register(new Setting<>("DoublePacket", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> updates = this.register(new Setting<>("Update", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> setOnMove = this.register(new Setting<>("SetMove", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> cliperino = this.register(new Setting<>("NoClip", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.setOnMove.getValue() != false));
    public Setting<Boolean> scanPackets = this.register(new Setting<>("ScanPackets", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> resetConfirm = this.register(new Setting<>("Reset", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> posLook = this.register(new Setting<>("PosLook", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK));
    public Setting<Boolean> cancel = this.register(new Setting<>("Cancel", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.posLook.getValue() != false));
    public Setting<Boolean> cancelType = this.register(new Setting<>("SetYaw", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.posLook.getValue() != false && this.cancel.getValue() != false));
    public Setting<Boolean> onlyY = this.register(new Setting<>("OnlyY", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.posLook.getValue() != false));
    public Setting<Integer> cancelPacket = this.register(new Setting<>("Packets", Integer.valueOf(20), Integer.valueOf(0), Integer.valueOf(20), v -> this.mode.getValue() == Mode.PACKETFLY && this.type.getValue() == PacketFlyMode.SETBACK && this.posLook.getValue() != false));
    private final Set<CPacketPlayer> packets = new ConcurrentSet();
    private boolean teleport = true;
    private int teleportIds = 0;
    private int posLookPackets;

    public Phase() {
        super("Phase", "Makes you able to phase through blocks.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }
    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
disable();
            return;
        }}
    public static Phase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Phase();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onDisable() {
        this.packets.clear();
        this.posLookPackets = 0;
        if (Phase.mc.player != null) {
            if (this.resetConfirm.getValue().booleanValue()) {
                this.teleportIds = 0;
            }
            Phase.mc.player.noClip = false;
        }
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (this.setOnMove.getValue().booleanValue() && this.type.getValue() == PacketFlyMode.SETBACK && event.getStage() == 0 && !mc.isSingleplayer() && this.mode.getValue() == Mode.PACKETFLY) {
            event.setX(Phase.mc.player.motionX);
            event.setY(Phase.mc.player.motionY);
            event.setZ(Phase.mc.player.motionZ);
            if (this.cliperino.getValue().booleanValue()) {
                Phase.mc.player.noClip = true;
            }
        }
        if (this.type.getValue() == PacketFlyMode.NONE || event.getStage() != 0 || mc.isSingleplayer() || this.mode.getValue() != Mode.PACKETFLY) {
            return;
        }
        if (!this.boundingBox.getValue().booleanValue() && !this.updates.getValue().booleanValue()) {
            this.doPhase(event);
        }
    }

    @SubscribeEvent
    public void onPush(PushEvent event) {
        if (event.getStage() == 1 && this.type.getValue() != PacketFlyMode.NONE) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onMove(MotionUpdateEvent event) {
        if (Phase.fullNullCheck() || event.getStage() != 0 || this.type.getValue() != PacketFlyMode.SETBACK || this.mode.getValue() != Mode.PACKETFLY) {
            return;
        }
        if (this.boundingBox.getValue().booleanValue()) {
            this.doBoundingBox();
        } else if (this.updates.getValue().booleanValue()) {
            this.doPhase(null);
        }
    }

    private void doPhase(MoveEvent event) {
        if (this.type.getValue() == PacketFlyMode.SETBACK && !this.boundingBox.getValue().booleanValue()) {
            double[] dirSpeed = this.getMotion(this.teleport ? (double) this.yMove.getValue().intValue() / 10000.0 : (double) (this.yMove.getValue() - 1) / 10000.0);
            double posX = Phase.mc.player.posX + dirSpeed[0];
            double posY = Phase.mc.player.posY + (Phase.mc.gameSettings.keyBindJump.isKeyDown() ? (this.teleport ? (double) this.yMove.getValue().intValue() / 10000.0 : (double) (this.yMove.getValue() - 1) / 10000.0) : 1.0E-8) - (Phase.mc.gameSettings.keyBindSneak.isKeyDown() ? (this.teleport ? (double) this.yMove.getValue().intValue() / 10000.0 : (double) (this.yMove.getValue() - 1) / 10000.0) : 2.0E-8);
            double posZ = Phase.mc.player.posZ + dirSpeed[1];
            CPacketPlayer.PositionRotation packetPlayer = new CPacketPlayer.PositionRotation(posX, posY, posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, false);
            this.packets.add(packetPlayer);
            Phase.mc.player.connection.sendPacket(packetPlayer);
            if (this.teleportConfirm.getValue() != 3) {
                Phase.mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportIds - 1));
                ++this.teleportIds;
            }
            if (this.extra.getValue().booleanValue()) {
                CPacketPlayer.PositionRotation packet = new CPacketPlayer.PositionRotation(Phase.mc.player.posX, (double) this.offset.getValue().intValue() + Phase.mc.player.posY, Phase.mc.player.posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, true);
                this.packets.add(packet);
                Phase.mc.player.connection.sendPacket(packet);
            }
            if (this.teleportConfirm.getValue() != 1) {
                Phase.mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportIds + 1));
                ++this.teleportIds;
            }
            if (this.ultraPacket.getValue().booleanValue()) {
                CPacketPlayer.PositionRotation packet2 = new CPacketPlayer.PositionRotation(posX, posY, posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, false);
                this.packets.add(packet2);
                Phase.mc.player.connection.sendPacket(packet2);
            }
            if (this.teleportConfirm.getValue() == 4) {
                Phase.mc.player.connection.sendPacket(new CPacketConfirmTeleport(this.teleportIds));
                ++this.teleportIds;
            }
            if (this.fallPacket.getValue().booleanValue()) {
                Phase.mc.player.connection.sendPacket(new CPacketEntityAction(Phase.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            Phase.mc.player.setPosition(posX, posY, posZ);
            boolean bl = this.teleport = this.teleporter.getValue() == false || !this.teleport;
            if (event != null) {
                event.setX(0.0);
                event.setY(0.0);
                event.setX(0.0);
            } else {
                Phase.mc.player.motionX = 0.0;
                Phase.mc.player.motionY = 0.0;
                Phase.mc.player.motionZ = 0.0;
            }
        }
    }

    private void doBoundingBox() {
        double[] dirSpeed = this.getMotion(this.teleport ? (double) 0.0225f : (double) 0.0224f);
        Phase.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Phase.mc.player.posX + dirSpeed[0], Phase.mc.player.posY + (Phase.mc.gameSettings.keyBindJump.isKeyDown() ? (this.teleport ? 0.0625 : 0.0624) : 1.0E-8) - (Phase.mc.gameSettings.keyBindSneak.isKeyDown() ? (this.teleport ? 0.0625 : 0.0624) : 2.0E-8), Phase.mc.player.posZ + dirSpeed[1], Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, false));
        Phase.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(Phase.mc.player.posX, -1337.0, Phase.mc.player.posZ, Phase.mc.player.rotationYaw, Phase.mc.player.rotationPitch, true));
        Phase.mc.player.connection.sendPacket(new CPacketEntityAction(Phase.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        Phase.mc.player.setPosition(Phase.mc.player.posX + dirSpeed[0], Phase.mc.player.posY + (Phase.mc.gameSettings.keyBindJump.isKeyDown() ? (this.teleport ? 0.0625 : 0.0624) : 1.0E-8) - (Phase.mc.gameSettings.keyBindSneak.isKeyDown() ? (this.teleport ? 0.0625 : 0.0624) : 2.0E-8), Phase.mc.player.posZ + dirSpeed[1]);
        this.teleport = !this.teleport;
        Phase.mc.player.motionZ = 0.0;
        Phase.mc.player.motionY = 0.0;
        Phase.mc.player.motionX = 0.0;
        Phase.mc.player.noClip = this.teleport;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (this.posLook.getValue().booleanValue() && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            if (Phase.mc.player.isEntityAlive() && Phase.mc.world.isBlockLoaded(new BlockPos(Phase.mc.player.posX, Phase.mc.player.posY, Phase.mc.player.posZ)) && !(Phase.mc.currentScreen instanceof GuiDownloadTerrain)) {
                if (this.teleportIds <= 0) {
                    this.teleportIds = packet.getTeleportId();
                }
                if (this.cancel.getValue().booleanValue() && this.cancelType.getValue().booleanValue()) {
                    packet.yaw = Phase.mc.player.rotationYaw;
                    packet.pitch = Phase.mc.player.rotationPitch;
                    return;
                }
                if (!(!this.cancel.getValue().booleanValue() || this.posLookPackets < this.cancelPacket.getValue() || this.onlyY.getValue().booleanValue() && (Phase.mc.gameSettings.keyBindForward.isKeyDown() || Phase.mc.gameSettings.keyBindRight.isKeyDown() || Phase.mc.gameSettings.keyBindLeft.isKeyDown() || Phase.mc.gameSettings.keyBindBack.isKeyDown()))) {
                    this.posLookPackets = 0;
                    event.setCanceled(true);
                }
                ++this.posLookPackets;
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Send event) {
        if (this.scanPackets.getValue().booleanValue() && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packetPlayer = (CPacketPlayer) event.getPacket();
            if (this.packets.contains(packetPlayer)) {
                this.packets.remove(packetPlayer);
            } else {
                event.setCanceled(true);
            }
        }
    }

    private double[] getMotion(double speed) {
        float moveForward = Phase.mc.player.movementInput.moveForward;
        float moveStrafe = Phase.mc.player.movementInput.moveStrafe;
        float rotationYaw = Phase.mc.player.prevRotationYaw + (Phase.mc.player.rotationYaw - Phase.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double) moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double) moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double) moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double) moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    public enum PacketFlyMode {
        NONE,
        SETBACK

    }

    public enum Mode {
        PACKETFLY

    }
}

