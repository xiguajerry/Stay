


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

public class SmallShield
extends Module {
    public Setting<Boolean> normalOffset = this.register(new Setting<>("OffNormal", false));
    public Setting<Float> offset = this.register(new Setting<>("Offset", Float.valueOf(0.7f), Float.valueOf(0.0f), Float.valueOf(1.0f), v -> this.normalOffset.getValue()));
    public Setting<Float> offX = this.register(new Setting<>("OffX", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f), v -> this.normalOffset.getValue() == false));
    public Setting<Float> offY = this.register(new Setting<>("OffY", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f), v -> this.normalOffset.getValue() == false));
    public Setting<Float> mainX = this.register(new Setting<>("MainX", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    public Setting<Float> mainY = this.register(new Setting<>("MainY", Float.valueOf(0.0f), Float.valueOf(-1.0f), Float.valueOf(1.0f)));
    private static SmallShield INSTANCE = new SmallShield();

    public SmallShield() {
        super("SmallShield", "Makes you offhand lower.", Module.Category.VISUAL, false, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.normalOffset.getValue().booleanValue()) {
            SmallShield.mc.entityRenderer.itemRenderer.equippedProgressOffHand = this.offset.getValue().floatValue();
        }
    }

    public static SmallShield getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new SmallShield();
        }
        return INSTANCE;
    }
}

