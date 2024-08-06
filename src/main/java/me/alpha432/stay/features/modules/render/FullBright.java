/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.render;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

/**
 * @author linustouchtips
 * @since 11/27/2020
 */

public class FullBright extends Module {
    public FullBright() {
        super("FullBright", "FullBright", Module.Category.VISUAL, false, false, false);
    }
    public Setting<SwingMode> mode = register(new Setting<>("Swing", SwingMode.Gamma));

    public enum SwingMode {

        Gamma,
        Potion

    }
    float oldBright;

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (mode.getValue() == SwingMode.Potion)
            mc.player.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 80950, 1, false, false)));
    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        oldBright = mc.gameSettings.gammaSetting;

        if (mode.getValue() == SwingMode.Gamma)
            mc.gameSettings.gammaSetting = +100;
        return;
    }

    @Override
    public void onDisable() {
        mc.player.removePotionEffect(MobEffects.NIGHT_VISION);

        if (mode.getValue() ==SwingMode.Gamma)
            mc.gameSettings.gammaSetting = oldBright;
    }
}
