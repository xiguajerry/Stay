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

public class TexturedChams extends Module {

    public static Setting<Integer> red;
    public static Setting<Integer> green;
    public static Setting<Integer> blue;
    public static Setting<Integer> alpha;

    public TexturedChams() {
        super("TexturedChams", "hi yes", Category.VISUAL, true, false, true);

        red = register(new Setting<>("Red", 168, 0, 255));
        green =  register(new Setting<>("Green", 0, 0, 255));
        blue =  register(new Setting<>("Blue", 232, 0, 255));
        alpha =  register(new Setting<>("Alpha", 150, 0, 255));
    }
}
