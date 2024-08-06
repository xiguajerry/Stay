/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.render;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.event.WorldEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.client.HUD;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class Breadcrumbs extends Module {
    private final Setting<Integer> colorRed = register(new Setting<>("Red", 255, 0, 255));
    private final Setting<Integer> colorGreen = register(new Setting<>("Green", 255, 0, 255));
    private final Setting<Integer> colorBlue = register(new Setting<>("Blue", 255, 0, 255));
    private final Setting<Boolean> colorRainbow = register(new Setting<>("Rainbow", false));
    private final Setting<Boolean> fade = register(new Setting<>("Fade", false));
    private final Setting<Integer> fadeTime = register(new Setting<>("FadeTime", 5, 1, 100, _hidden -> fade.getValue()));

    private final List<BreadcrumbPoint> points = new ArrayList<>();
    private final long startTime = System.currentTimeMillis();

    public Breadcrumbs() {
        super("Breadcrumbs", "This is a description", Category.VISUAL, true, false, false);
    }

    private Color getColor() {
        if (colorRainbow.getValue()) return Color.getHSBColor((Math.abs(((((System.currentTimeMillis() - startTime) + 300L) / Stay.moduleManager.getModuleT(HUD.class).rainbowSpeed.getValue()) % 2) - 1) * (0.58f - 0.41f)) + 0.41f, 0.7f, 1f);
        else return new Color(colorRed.getValue(), colorGreen.getValue(), colorBlue.getValue());
    }

    @Override
    public void onRender3D(Render3DEvent event) {

        int fTime = fadeTime.getValue() * 100;
        long fadeSec = System.currentTimeMillis() - fTime;

        synchronized (points) {
            glPushMatrix();
            glDisable(GL_TEXTURE_2D);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_LINE_SMOOTH);
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            mc.entityRenderer.disableLightmap();
            glLineWidth(2f);
            glBegin(GL_LINE_STRIP);
            double renderPosX = mc.renderManager.viewerPosX;
            double renderPosY = mc.renderManager.viewerPosY;
            double renderPosZ = mc.renderManager.viewerPosZ;
            if (fade.getValue()) {
                points.removeIf(breadcrumbPoint -> {
                    float pct = (float) (breadcrumbPoint.time - fadeSec) / fTime;
                    if (pct < 0 || pct > 1) {
                        return true;
                    }
                    return false;
                });
            }
            points.forEach(breadcrumbPoint -> {
                glColor(breadcrumbPoint.color, 1f);
                glVertex3d(breadcrumbPoint.x - renderPosX, breadcrumbPoint.y - renderPosY, breadcrumbPoint.z - renderPosZ);
            });
            glColor4d(1.0, 1.0, 1.0, 1.0);
            glEnd();
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_LINE_SMOOTH);
            glDisable(GL_BLEND);
            glEnable(GL_TEXTURE_2D);
            glPopMatrix();
        }
    }

    @Override
    public void onUpdate() {
        synchronized (points) {
            points.add(new BreadcrumbPoint(mc.player.posX, mc.player.getEntityBoundingBox().minY, mc.player.posZ, System.currentTimeMillis(), getColor().getRGB()));
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent event) {
        synchronized (points) {
            points.clear();
        }
    }

    @Override
    public void onDisable() {
        synchronized (points) {
            points.clear();
        }
    }

    private static void glColor(final int hex, final float alpha) {
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }

    public static class BreadcrumbPoint {
        private double x;
        private double y;
        private double z;
        private long time;
        private int color;

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
        }

        public double getZ() {
            return z;
        }

        public void setZ(double z) {
            this.z = z;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public BreadcrumbPoint(double x, double y, double z, long time, int color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.time = time;
            this.color = color;
        }
    }
}
