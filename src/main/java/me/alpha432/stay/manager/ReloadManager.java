/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.Feature;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.loader.ForgeEntry;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReloadManager
        extends Feature {
    public String prefix;

    public void init(String prefix) {
        this.prefix = prefix;
        ForgeEntry.register(this);
        if (!ReloadManager.fullNullCheck()) {
            Command.sendMessage(ChatFormatting.RED + "stay has been unloaded. Type " + prefix + "reload to reload.");
        }
    }

    public void unload() {
        ForgeEntry.unregister(this);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketChatMessage packet;
        if (event.getPacket() instanceof CPacketChatMessage && (packet = (CPacketChatMessage) event.getPacket()).getMessage().startsWith(this.prefix) && packet.getMessage().contains("reload")) {
            Stay.load();
            event.setCanceled(true);
        }
    }
}

