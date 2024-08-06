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

/**
 * Created by 086 on 8/04/2018.
 */

public class AntiWeather extends Module {

    public AntiWeather() {
        super("AntiWeather", "ARemoves rain from your world ", Module.Category.VISUAL, true, false, false);
    }

    @Override
    public void onTick() {
        if (isDisabled()) return;
        if (mc.world.isRaining())
            mc.world.setRainStrength(0);
    }
}
