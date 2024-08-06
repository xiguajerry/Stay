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
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

import java.util.Random;

public class AtinAFK extends Module {
    public AtinAFK() {
        super("AtinAFK",  "AtinAFK", Category.MISC,true,false,false);
    }


    private final Setting<Boolean> swing = register(new Setting<>("Swing", true));
    private final Setting<Boolean> turn = register(new Setting<>("Turn", true));
    private Random random = new Random();
    public void onUpdate() {
        if (!mc.playerController.getIsHittingBlock()) {
            if (mc.player.ticksExisted % 40 == 0 && (Boolean)this.swing.getValue()) {
                mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
            }

            if (mc.player.ticksExisted % 15 == 0 && (Boolean)this.turn.getValue()) {
                mc.player.rotationYaw = (float)(this.random.nextInt(360) - 180);
            }

            if (!(Boolean)this.swing.getValue() && !(Boolean)this.turn.getValue() && mc.player.ticksExisted % 80 == 0) {
                mc.player.jump();
            }

        }
    }
}
