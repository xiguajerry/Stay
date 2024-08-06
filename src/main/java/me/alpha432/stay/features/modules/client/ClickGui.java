/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.ClientEvent;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.gui.StayGui;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.client.settings.GameSettings;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<String> prefix = register(new Setting<>("Prefix", "."));
    public Setting<Boolean> customFov = register(new Setting<>("CustomFov", true));
    public Setting<Boolean> gui = register(new Setting<>("CustomMainMenu", true));
    public Setting<Float> fov = register(new Setting<>("Fov", 150.0f, -180.0f, 180.0f));
    public Setting<Integer> red = register(new Setting<>("Red", 0, 0, 255));
    public Setting<Integer> green = register(new Setting<>("Green", 0, 0, 255));
    public Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255));
    public Setting<Integer> hoverAlpha = register(new Setting<>("Alpha", 180, 0, 255));
    public Setting<Integer> topRed = register(new Setting<>("SecondRed", 0, 0, 255));
    public Setting<Integer> topGreen = register(new Setting<>("SecondGreen", 0, 0, 255));
    public Setting<Integer> topBlue = register(new Setting<>("SecondBlue", 150, 0, 255));
    public Setting<Integer> alpha = register(new Setting<>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = register(new Setting<>("HRainbowMode", rainbowMode.Static, v -> rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = register(new Setting<>("ARainbowMode", rainbowModeArray.Static, v -> rainbow.getValue()));
    public Setting<Integer> rainbowHue = register(new Setting<>("Delay", 240, 0, 600, v -> rainbow.getValue()));
    public Setting<Float> rainbowBrightness = register(new Setting<>("Brightness ", 150.0f, 1.0f, Float.valueOf(255.0f), v -> rainbow.getValue()));
    public Setting<Float> rainbowSaturation = register(new Setting<>("Saturation", 150.0f, 1.0f, Float.valueOf(255.0f), v -> rainbow.getValue()));
    public Setting<Integer> fadingLength = register(new Setting<>("Fading Time", 500, 20, 5000));
    private StayGui click;

    public enum ModeS {
        GUI1,
        GUI2,
        GUI3,
        GUI4,
        GUI5,
        GUI6,
        GUI7,
        GUI8,
        GUI9,
        GUI10,
        URL
    }

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui, color by SexyMemory", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (customFov.getValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, fov.getValue().floatValue());
        }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(prefix)) {
                Stay.commandManager.setPrefix(prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + Stay.commandManager.getPrefix());
            }
            Stay.colorManager.setColor(red.getPlannedValue(), green.getPlannedValue(), blue.getPlannedValue(), hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(StayGui.getClickGui());
    }

    @Override
    public void onLoad() {
        Stay.colorManager.setColor(red.getValue(), green.getValue(), blue.getValue(), hoverAlpha.getValue());
        Stay.commandManager.setPrefix(prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof StayGui)) {
            disable();
        }
    }

    public enum rainbowModeArray {
        Static,
        Up

    }

    public enum rainbowMode {
        Static,
        Sideway

    }
}

