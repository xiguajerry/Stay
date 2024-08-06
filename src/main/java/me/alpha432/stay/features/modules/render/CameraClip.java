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

public class CameraClip  extends Module {
    public CameraClip() {
        super("Camera Clip", "CameraClip", Module.Category.VISUAL, false, false, false);
    }
    public Setting<Double> distance = register(new Setting<>("Distance", 10.0, -10.0, 50.0));
}
