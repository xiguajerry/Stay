/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class AutoGG extends Module {
    private static AutoGG INSTANCE = new AutoGG();
    public Setting<String> custom = register(new Setting<>("Custom", "ezzzzz"));
    public Setting<String> test = register(new Setting<>("Test", "null"));
    private ConcurrentHashMap<String, Integer> targetedPlayers = null;

    public AutoGG() {
        super("AutoGG", "Sends msg after you kill someone", Module.Category.MISC, true, false, false);
        setInstance();
    }

    public static AutoGG getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new AutoGG();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        targetedPlayers = new ConcurrentHashMap();
    }

    @Override
    public void onDisable() {
        targetedPlayers = null;
    }

    @Override
    public void onUpdate() {
        if (AutoGG.nullCheck()) {
            return;
        }
        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap();
        }
        for (Entity entity : AutoGG.mc.world.getLoadedEntityList()) {
            String name2;
            EntityPlayer player;
            if (!(entity instanceof EntityPlayer) || (player = (EntityPlayer) entity).getHealth() > 0.0f || !shouldAnnounce(name2 = player.getName()))
                continue;
            doAnnounce(name2);
            break;
        }
        targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                targetedPlayers.remove(name);
            } else {
                targetedPlayers.put(name, timeout - 1);
            }
        });
    }

    @SubscribeEvent
    public void onLeavingDeathEvent(LivingDeathEvent event) {
        EntityLivingBase entity;
        if (AutoGG.mc.player == null) {
            return;
        }
        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap();
        }
        if ((entity = event.getEntityLiving()) == null) {
            return;
        }
        if (!(entity instanceof EntityPlayer)) {
            return;
        }
        EntityPlayer player = (EntityPlayer) entity;
        if (player.getHealth() > 0.0f) {
            return;
        }
        String name = player.getName();
        if (shouldAnnounce(name)) {
            doAnnounce(name);
        }
    }

    private boolean shouldAnnounce(String name) {
        return targetedPlayers.containsKey(name);
    }

    private void doAnnounce(String name) {
        targetedPlayers.remove(name);
        AutoGG.mc.player.connection.sendPacket(new CPacketChatMessage(custom.getValue()));
        int u = 0;
        for (int i = 0; i < 10; ++i) {
            ++u;
        }
        if (!test.getValue().equalsIgnoreCase("null")) {
            AutoGG.mc.player.connection.sendPacket(new CPacketChatMessage(test.getValue()));
        }
    }

    public void addTargetedPlayer(String name) {
        if (Objects.equals(name, AutoGG.mc.player.getName())) {
            return;
        }
        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap();
        }
        targetedPlayers.put(name, 20);
    }
}

