/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/7 上午1:32
 */

package me.alpha432.stay.mixin.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.alpha432.stay.event.PacketEvent;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NetworkManager.class, priority = 312312)
public class MixinNetworkManager {
    @Inject(method = {"sendPacket(Lnet/minecraft/network/Packet;)V"}, at = @At(value = "HEAD"), cancellable = true)
    private void onSendPacketPre(Packet<?> packet, CallbackInfo info) {
        PacketEvent.Send event = new PacketEvent.Send(packet);
        final anonymous.team.eventsystem.impl.PacketEvent.Send send = new anonymous.team.eventsystem.impl.PacketEvent.Send(packet);
        event.post();
        send.post();
        if (event.isCanceled() || send.getCancelled()) {
            info.cancel();
        }
    }
    
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("RETURN"))
    private void sendPacketPost(Packet<?> packet, CallbackInfo callbackInfo) {
        if (packet != null) {
            anonymous.team.eventsystem.impl.PacketEvent.PostSend event = new anonymous.team.eventsystem.impl.PacketEvent.PostSend(packet);
            event.post();
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelReadPre(ChannelHandlerContext context, Packet<?> packet, CallbackInfo info) {
        PacketEvent.Receive event = new PacketEvent.Receive(packet);
        final anonymous.team.eventsystem.impl.PacketEvent.Receive receive = new anonymous.team.eventsystem.impl.PacketEvent.Receive(packet);
        event.post();
        receive.post();
        if (event.isCanceled() || receive.getCancelled()) {
            info.cancel();
        }
    }
    
    @Inject(method = "channelRead0", at = @At("RETURN"))
    private void channelReadPost(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        if (packet != null) {
            anonymous.team.eventsystem.impl.PacketEvent.PostReceive event = new anonymous.team.eventsystem.impl.PacketEvent.PostReceive(packet);
            event.post();
        }
    }

}

