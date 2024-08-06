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
import me.alpha432.stay.loader.ForgeEntry;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class SkyColor extends Module {

    private Setting<Integer> red = register(new Setting<>("Red", 255, 0, 255));
    private Setting<Integer> green = register(new Setting<>("Green", 255, 0, 255));
    private Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255));
    private Setting<Boolean> rainbow = register(new Setting<>("Rainbow", true));

    private static SkyColor INSTANCE = new SkyColor();

    public SkyColor() {
        super("SkyColor", "Changes the color of the fog", Module.Category.VISUAL, false, false, false);
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static SkyColor getInstance() {
        if (INSTANCE == null)
            INSTANCE = new SkyColor();
        return INSTANCE;
    }

    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        event.setRed(red.getValue() / 255f);
        event.setGreen(green.getValue() / 255f);
        event.setBlue(blue.getValue() / 255f);
    }

    @SubscribeEvent
    public void fog_density(final EntityViewRenderEvent.FogDensity event) {
        event.setDensity(0.0f);
        event.setCanceled(true);
    }

    @Override
    public void onEnable() {
        ForgeEntry.register(this);
    }

    @Override
    public void onDisable() {
        ForgeEntry.unregister(this);
    }

    @Override
    public void onUpdate() {
        if (rainbow.getValue()) {
            doRainbow();
        }
    }

    public void doRainbow() {

        float[] tick_color = {
                (System.currentTimeMillis() % (360 * 32)) / (360f * 32)
        };

        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);

        red.setValue((color_rgb_o >> 16) & 0xFF);
        green.setValue((color_rgb_o >> 8) & 0xFF);
        blue.setValue(color_rgb_o & 0xFF);
    }
}