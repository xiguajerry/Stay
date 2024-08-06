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

public class Fov  extends Module {
    public Fov() {
        super("Fov", "tabbott mode", Category.VISUAL, false, false, false);
    }
    public Setting<Integer> fov = register(new Setting<>("Fov", 130, 90, 179));
    float fovOld;
    @Override
    public void onEnable(){
        fovOld = mc.gameSettings.fovSetting;
    }

    @Override
    public void onUpdate() {
        mc.gameSettings.fovSetting = (float) fov.getValue();
    }

    @Override
    public void onDisable(){
        mc.gameSettings.fovSetting = fovOld;
    }
}
