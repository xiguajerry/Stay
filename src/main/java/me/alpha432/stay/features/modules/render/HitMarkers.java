/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.render;

import me.alpha432.stay.event.Render2DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.loader.ForgeEntry;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import net.minecraft.util.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.gui.*;
import java.awt.*;

public final class HitMarkers extends Module
{

    public final ResourceLocation image;
    private int renderTicks;

    public HitMarkers() {
        super("HitMarkers", "hitmarker thingys", Module.Category.VISUAL, false, false, false);
        this.image = new ResourceLocation("hitmarker.png");
        renderTicks = 100;
    }

    public Setting<Integer> red = this.register(new Setting<>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<>("Green", 255, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<>("Blue", 255, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<>("Alpha", 255, 0, 255));
    public Setting<Integer> thickness = this.register(new Setting<>("Thickness", 2, 1, 6));
    public Setting<Double> time = this.register(new Setting<>("Time", 20D, 1D, 50D));

    @Override
    public void onRender2D(Render2DEvent event) {
        if (this.renderTicks < time.getValue()) {
            final ScaledResolution resolution = new ScaledResolution(HitMarkers.mc);
            this.drawHitMarkers();
        }
    }

    public void onEnable() {
        ForgeEntry.register(this);
    }

    public void onDisable() {
        ForgeEntry.unregister(this);
    }

    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (!event.getEntity().equals(HitMarkers.mc.player)) {
            return;
        }
        this.renderTicks = 0;
    }

    @SubscribeEvent
    public void onTickClientTick(final TickEvent event) {
        ++renderTicks;
    }

    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(HitMarkers.mc);
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f - 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f - 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f - 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f - 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
        RenderUtil.drawLine(resolution.getScaledWidth() / 2.0f + 4.0f, resolution.getScaledHeight() / 2.0f + 4.0f, resolution.getScaledWidth() / 2.0f + 8.0f, resolution.getScaledHeight() / 2.0f + 8.0f, this.thickness.getValue(), new Color(red.getValue(), green.getValue(), blue.getValue()).getRGB());
    }
}