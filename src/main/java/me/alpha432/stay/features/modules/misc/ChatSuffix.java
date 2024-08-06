/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix extends Module {
    public ChatSuffix() {
        super("ChatSuffix", "Appends your message", Module.Category.MISC, true, false, false);
    }
    public Setting<Boolean> prefix = register(new Setting<>("prefix", true));
    public Setting<String> custom = register(new Setting<>("Custom", ">",v ->prefix.getValue()));
    public Setting<Boolean> bb = register(new Setting<>("edition", true));
    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() != 0) return;
        if(ModuleManager.getModuleByName("AutoQueue").isEnabled()) return;
        if (!(event.getPacket() instanceof CPacketChatMessage)) return;
        CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
        String message = packet.getMessage();
        if (message.startsWith("/")) {
            return;
        }

        if(prefix.getValue()){
            if ((message = custom.getValue()+message + "  >  s\u1d1b\u1d00\u028f").length() >= 256) {
                message = message.substring(0, 256);
            }
        }else {
            if ((message = message + "  >  s\u1d1b\u1d00\u028f").length() >= 256) {
                message = message.substring(0, 256);
            }
        }
        if(bb.getValue()){
            if ((message = message +"-"+ Stay.VERSION).length() >= 256) {
                message = message.substring(0, 256);
            }
        }
        packet.message = message;
    }
}