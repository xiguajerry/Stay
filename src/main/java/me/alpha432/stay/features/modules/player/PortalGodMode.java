/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.player;


import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.modules.Module;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**
 * Created by GlowskiBroski on 10/14/2018.
 */

public class PortalGodMode extends Module {
    public PortalGodMode() {
        super("PortalGodMode", "PortalGodMode", Category.PLAYER, false, false, false);
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (isEnabled() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.setCancelled(true);
        }
    }
}
