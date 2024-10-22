/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.render;

import com.google.common.collect.Maps;
import me.alpha432.stay.event.Render2DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import me.alpha432.stay.util.basement.wrapper.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;

public class ArrowESP extends Module {
    private final Setting<Integer> red = register(new Setting<>("Red", 255, 0, 255));
    private final Setting<Integer> green = register(new Setting<>("Green", 255, 0, 255));
    private final Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255));
    private final Setting<Integer> radius = register(new Setting<>("Placement", 45, 10, 200));
    private final Setting<Float> size = register(new Setting<>("Size", Float.valueOf(10.0f), Float.valueOf(5.0f), Float.valueOf(25.0f)));
    private final Setting<Boolean> outline = register(new Setting<>("Outline", true));
    private final Setting<Float> outlineWidth = register(new Setting<>("Outline-Width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f)));
    private final Setting<Integer> fadeDistance = register(new Setting<>("Range", 100, 10, 200));
    private final Setting<Boolean> invisibles = register(new Setting<>("Invisibles", false));
    private final EntityListener entityListener = new EntityListener();

    public ArrowESP() {
        super("ArrowESP", "Arrow tracers ", Module.Category.VISUAL, true, false, false);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        entityListener.render();
        ArrowESP.mc.world.loadedEntityList.forEach(o -> {
            if (o instanceof EntityPlayer && isValid((EntityPlayer) o)) {
                EntityPlayer entity = (EntityPlayer) o;
                Vec3d pos = entityListener.getEntityLowerBounds().get(entity);
                if (pos != null && !isOnScreen(pos) && !RenderUtil.isInViewFrustrum(entity)) {
                    Color color = EntityUtils.getColor(entity, red.getValue(), green.getValue(), blue.getValue(), (int) MathHelper.clamp(255.0f - 255.0f / (float) fadeDistance.getValue().intValue() * ArrowESP.mc.player.getDistance(entity), 100.0f, 255.0f), true);
                    int x = Display.getWidth() / 2 / (ArrowESP.mc.gameSettings.guiScale == 0 ? 1 : ArrowESP.mc.gameSettings.guiScale);
                    int y = Display.getHeight() / 2 / (ArrowESP.mc.gameSettings.guiScale == 0 ? 1 : ArrowESP.mc.gameSettings.guiScale);
                    float yaw = getRotations(entity) - ArrowESP.mc.player.rotationYaw;
                    GL11.glTranslatef((float) x, (float) y, 0.0f);
                    GL11.glRotatef(yaw, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef((float) (-x), (float) (-y), 0.0f);
                    RenderUtil.drawTracerPointer(x, y - radius.getValue(), size.getValue().floatValue(), 2.0f, 1.0f, outline.getValue(), outlineWidth.getValue().floatValue(), color.getRGB());
                    GL11.glTranslatef((float) x, (float) y, 0.0f);
                    GL11.glRotatef(-yaw, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef((float) (-x), (float) (-y), 0.0f);
                }
            }
        });
    }


    private boolean isOnScreen(Vec3d pos) {
        if (!(pos.x > -1.0)) return false;
        if (!(pos.y < 1.0)) return false;
        if (!(pos.x > -1.0)) return false;
        if (!(pos.z < 1.0)) return false;
        int n = ArrowESP.mc.gameSettings.guiScale == 0 ? 1 : ArrowESP.mc.gameSettings.guiScale;
        if (!(pos.x / (double) n >= 0.0)) return false;
        int n2 = ArrowESP.mc.gameSettings.guiScale == 0 ? 1 : ArrowESP.mc.gameSettings.guiScale;
        if (!(pos.x / (double) n2 <= (double) Display.getWidth())) return false;
        int n3 = ArrowESP.mc.gameSettings.guiScale == 0 ? 1 : ArrowESP.mc.gameSettings.guiScale;
        if (!(pos.y / (double) n3 >= 0.0)) return false;
        int n4 = ArrowESP.mc.gameSettings.guiScale == 0 ? 1 : ArrowESP.mc.gameSettings.guiScale;
        return pos.y / (double) n4 <= (double) Display.getHeight();
    }

    private boolean isValid(EntityPlayer entity) {
        return entity != ArrowESP.mc.player && (!entity.isInvisible() || invisibles.getValue() != false) && entity.isEntityAlive();
    }

    private float getRotations(EntityLivingBase ent) {
        double x = ent.posX - ArrowESP.mc.player.posX;
        double z = ent.posZ - ArrowESP.mc.player.posZ;
        return (float) (-(Math.atan2(x, z) * 57.29577951308232));
    }

    private static class EntityListener {
        private final Map<Entity, Vec3d> entityUpperBounds = Maps.newHashMap();
        private final Map<Entity, Vec3d> entityLowerBounds = Maps.newHashMap();

        private EntityListener() {
        }

        private void render() {
            if (!entityUpperBounds.isEmpty()) {
                entityUpperBounds.clear();
            }
            if (!entityLowerBounds.isEmpty()) {
                entityLowerBounds.clear();
            }
            for (Entity e : Util.mc.world.loadedEntityList) {
                Vec3d bound = getEntityRenderPosition(e);
                bound.add(new Vec3d(0.0, (double) e.height + 0.2, 0.0));
                Vec3d upperBounds = RenderUtil.to2D(bound.x, bound.y, bound.z);
                Vec3d lowerBounds = RenderUtil.to2D(bound.x, bound.y - 2.0, bound.z);
                if (upperBounds == null || lowerBounds == null) continue;
                entityUpperBounds.put(e, upperBounds);
                entityLowerBounds.put(e, lowerBounds);
            }
        }

        private Vec3d getEntityRenderPosition(Entity entity) {
            double partial = Util.mc.timer.renderPartialTicks;
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial - Util.mc.getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial - Util.mc.getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial - Util.mc.getRenderManager().viewerPosZ;
            return new Vec3d(x, y, z);
        }

        public Map<Entity, Vec3d> getEntityLowerBounds() {
            return entityLowerBounds;
        }
    }
}

