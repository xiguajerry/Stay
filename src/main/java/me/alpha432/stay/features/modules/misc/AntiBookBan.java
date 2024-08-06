/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;

import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.modules.Module;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiBookBan extends Module {
    public AntiBookBan() {
        super("AntiBookBan",  "AntiBookBan", Category.MISC,true,false,false);
    }

    @SubscribeEvent
    public void onLeavingDeathEvent(PacketEvent.Receive event) {
        if (!(event.getPacket() instanceof CPacketClickWindow)) return;
        final CPacketClickWindow packet = (CPacketClickWindow) event.getPacket();

        if (!(packet.getClickedItem().getItem() instanceof ItemWrittenBook)) return;

        event.setCancelled(true);

        mc.player.openContainer.slotClick(packet.getSlotId(), packet.getUsedButton(), packet.getClickType(), mc.player);

    }


}
