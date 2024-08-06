/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;

import me.alpha432.stay.event.DeathEvent;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.client.HUD;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.text.TextUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Tracker extends Module {
    private static Tracker instance;
    private EntityPlayer trackedPlayer;
    private int usedExp = 0;
    private int usedStacks = 0;

    public Tracker() {
        super("Tracker", "Tracks players in 1v1s.", Module.Category.MISC, true, false, false);
        instance = this;
    }

    public static Tracker getInstance() {
        if (instance == null) {
            instance = new Tracker();
        }
        return instance;
    }

    @Override
    public void onUpdate() {
        if (trackedPlayer == null) {
            trackedPlayer = EntityUtils.getClosestEnemy(1000.0);
        } else if (usedStacks != usedExp / 64) {
            usedStacks = usedExp / 64;
            Command.sendMessage(TextUtil.coloredString(trackedPlayer.getName() + " has used " + usedStacks + " stacks of XP!", HUD.getInstance().commandColor.getValue()));
        }
    }

    public void onSpawnEntity(Entity entity) {
        if (entity instanceof EntityExpBottle && Objects.equals(Tracker.mc.world.getClosestPlayerToEntity(entity, 3.0), trackedPlayer)) {
            ++usedExp;
        }
    }

    @Override
    public void onDisable() {
        trackedPlayer = null;
        usedExp = 0;
        usedStacks = 0;
    }

    @SubscribeEvent
    public void onDeath(DeathEvent event) {
        if (event.getPlayer().equals(trackedPlayer)) {
            usedExp = 0;
            usedStacks = 0;
        }
    }

    @Override
    public String getDisplayInfo() {
        if (trackedPlayer != null) {
            return trackedPlayer.getName();
        }
        return null;
    }
}

