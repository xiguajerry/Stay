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
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketChatMessage
 */
package me.alpha432.stay.features.modules.misc;


import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import org.apache.commons.lang3.RandomStringUtils;

public class Message
extends Module {
    private final Timer timer = new Timer();
    private Setting<String> custom = this.register(new Setting<>("Custom", "/kit create "));
    private Setting<Integer> random = this.register(new Setting<>("Random", 1, 1, 20));
    private Setting<Integer> delay = this.register(new Setting<>("Delay", 100, 0, 10000));

    public Message() {
        super("Message", "Message", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onTick() {
        if (Message.fullNullCheck()) {
            return;
        }
        if (!this.timer.passedMs(this.delay.getValue().intValue())) return;
        Message.mc.player.connection.sendPacket((Packet)new CPacketChatMessage(this.custom.getValue() + RandomStringUtils.randomAlphanumeric(this.random.getValue())));
        this.timer.reset();
    }
}

