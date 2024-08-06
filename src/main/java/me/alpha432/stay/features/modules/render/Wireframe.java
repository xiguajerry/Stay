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
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Wireframe extends Module {
    private static Wireframe INSTANCE = new Wireframe();
    public Setting<Boolean> rainbow = register(new Setting<>("Rainbow", Boolean.TRUE));
    public Setting<Integer> rainbowHue = register(new Setting<>("RBrightness", Integer.valueOf(100), Integer.valueOf(0), Integer.valueOf(600), v -> rainbow.getValue()));
    public Setting<Integer> red = register(new Setting<>("PRed", 168, 0, 255));
    public Setting<Integer> green = register(new Setting<>("PGreen", 0, 0, 255));
    public Setting<Integer> blue = register(new Setting<>("PBlue", 232, 0, 255));
    public Setting<Integer> Cred = register(new Setting<>("CRed", 168, 0, 255));
    public Setting<Integer> Cgreen = register(new Setting<>("CGreen", 0, 0, 255));
    public Setting<Integer> Cblue = register(new Setting<>("CBlue", 232, 0, 255));
    public final Setting<Float> alpha = register(new Setting<>("PAlpha", Float.valueOf(255.0f), Float.valueOf(0.1f), Float.valueOf(255.0f)));
    public final Setting<Float> cAlpha = register(new Setting<>("CAlpha", Float.valueOf(255.0f), Float.valueOf(0.1f), Float.valueOf(255.0f)));
    public final Setting<Float> lineWidth = register(new Setting<>("PLineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    public final Setting<Float> crystalLineWidth = register(new Setting<>("CLineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    public Setting<RenderMode> mode = register(new Setting<>("PMode", RenderMode.SOLID));
    public Setting<RenderMode> cMode = register(new Setting<>("CMode", RenderMode.SOLID));
    public Setting<Boolean> players = register(new Setting<>("Players", Boolean.FALSE));
    public Setting<Boolean> playerModel = register(new Setting<>("PlayerModel", Boolean.FALSE));
    public Setting<Boolean> crystals = register(new Setting<>("Crystals", Boolean.FALSE));
    public Setting<Boolean> crystalModel = register(new Setting<>("CrystalModel", Boolean.FALSE));


    public Wireframe() {
        super("Wireframe", "Draws a wireframe esp around other players.", Module.Category.VISUAL, false, false, false);
        setInstance();
    }

    public static Wireframe getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Wireframe();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onRenderPlayerEvent(RenderPlayerEvent.Pre event) {

        event.getEntityPlayer().hurtTime = 0;
    }

    public enum RenderMode {
        SOLID,
        WIREFRAME

    }
}

