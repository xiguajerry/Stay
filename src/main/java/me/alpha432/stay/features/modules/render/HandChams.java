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

public class HandChams extends Module {
    private static HandChams INSTANCE = new HandChams();
    public Setting<RenderMode> mode = register(new Setting<>("Mode", RenderMode.SOLID));
    public Setting<Integer> red = register(new Setting<>("Red", 255, 0, 255));
    public Setting<Integer> green = register(new Setting<>("Green", 0, 0, 255));
    public Setting<Integer> blue = register(new Setting<>("Blue", 0, 0, 255));
    public Setting<Integer> alpha = register(new Setting<>("Alpha", 240, 0, 255));

    public HandChams() {
        super("HandChams", "Changes your hand color.", Module.Category.VISUAL, false, false, false);
        setInstance();
    }

    public static HandChams getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new HandChams();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public enum RenderMode {
        SOLID,
        WIREFRAME

    }
}

