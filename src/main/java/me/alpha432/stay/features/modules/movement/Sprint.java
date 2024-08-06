/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

/*
 * Decompiled with CFR 0.151.
 */
package me.alpha432.stay.features.modules.movement;


import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;

public class Sprint
        extends Module {


    public static enum Mode {
        LEGIT,
        RAGE

    }
    public Setting<Mode> mode = this.register(new Setting<>("Mode", Mode.LEGIT));
    private static Sprint INSTANCE = new Sprint();

    public Sprint() {
        super("Sprint", "Modifies sprinting", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Sprint getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new Sprint();
        return INSTANCE;
    }

    @Override
    public void onUpdate() {
//        if(ModuleManager.getModuleByName("HoleSnap").isEnabled()){
//            return;
//        }
        switch (this.mode.getValue().ordinal()) {
            case 1: {
                if (!(Sprint.mc.gameSettings.keyBindForward.isKeyDown() || Sprint.mc.gameSettings.keyBindBack.isKeyDown() || Sprint.mc.gameSettings.keyBindLeft.isKeyDown())) {
                    if (!Sprint.mc.gameSettings.keyBindRight.isKeyDown()) return;
                }
                if (Sprint.mc.player.isSneaking()) return;
                if (Sprint.mc.player.collidedHorizontally) return;
                if ((float)Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f) {
                    return;
                }
                Sprint.mc.player.setSprinting(true);
                return;
            }
            case 2: {
                if (!Sprint.mc.gameSettings.keyBindForward.isKeyDown()) return;
                if (Sprint.mc.player.isSneaking()) return;
                if (Sprint.mc.player.isHandActive()) return;
                if (Sprint.mc.player.collidedHorizontally) return;
                if ((float)Sprint.mc.player.getFoodStats().getFoodLevel() <= 6.0f) return;
                if (Sprint.mc.currentScreen != null) {
                    return;
                }
                Sprint.mc.player.setSprinting(true);
                return;
            }
        }
    }

    @Override
    public void onDisable() {
        if (Sprint.nullCheck()) return;
        Sprint.mc.player.setSprinting(false);
    }

    @Override
    public String getDisplayInfo() {
        return this.mode.currentEnumName();
    }


}

