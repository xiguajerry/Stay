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
import net.minecraft.client.gui.GuiGameOver;

public class AntiDeathScreen extends Module {
    public AntiDeathScreen() {
        super("AntiDeathScreen",  "AntiDeathScreen", Category.MISC,true,false,false);
    }
    public void onUpdate() {
        if (mc.currentScreen instanceof GuiGameOver) {
                mc.player.respawnPlayer();
                mc.displayGuiScreen(null);
        }
    }

}
