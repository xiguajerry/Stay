/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.event.TotemPopEvent;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.NotificationManager;
import me.alpha432.stay.manager.NotificationType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;

public class PopCounter extends Module {
    public static HashMap<String, Integer> TotemPopContainer = new HashMap();
    public static PopCounter INSTANCE = new PopCounter();

    public PopCounter() {
        super("PopCounter", "Counts other players totem pops.", Module.Category.MISC, true, false, false);
        setInstance();
    }

    public final Setting<String> clientname = register(new Setting<>("Name", "onpoint.ie"));

    public static PopCounter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PopCounter();
        }
        return INSTANCE;
    }
    public void onDisable() {
        super.onDisable();}

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        TotemPopContainer.clear();
    }

    public void onDeath(EntityPlayer player) {
        if (TotemPopContainer.containsKey(player.getName())) {
            int l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.remove(player.getName());
            if (l_Count == 1) {
                Command.sendSilentMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GRAY + l_Count + ChatFormatting.RED + ChatFormatting.RED + " totem, thanks to " + clientname.getValueAsString());
            } else {
                Command.sendSilentMessage(ChatFormatting.RED + player.getName() + " died after popping " + ChatFormatting.GRAY + l_Count + ChatFormatting.RED + ChatFormatting.RED + " totems, " + "thanks to " + clientname.getValueAsString());
            }
        }
    }
    
    @SubscribeEvent
    public void onTotemPopEvent(TotemPopEvent event) {
        onTotemPop(event.getPlayer());
    }

    public void onTotemPop(EntityPlayer player) {
        if (PopCounter.fullNullCheck()) {
            return;
        }
        if (PopCounter.mc.player.equals(player)) {
            return;
        }
        int l_Count = 1;
        if (TotemPopContainer.containsKey(player.getName())) {
            l_Count = TotemPopContainer.get(player.getName());
            TotemPopContainer.put(player.getName(), ++l_Count);
        } else {
            TotemPopContainer.put(player.getName(), l_Count);
        }
        if (l_Count == 1) {
            NotificationManager.INSTANCE.push(player.getName() + " popped " + ChatFormatting.GRAY + l_Count + ChatFormatting.RED + " totem", NotificationType.INFO, 1500);
        } else {
            NotificationManager.INSTANCE.push(player.getName() + " popped " + ChatFormatting.GRAY + l_Count + ChatFormatting.RED + " totems", NotificationType.INFO, 1500);
        }
    }
}