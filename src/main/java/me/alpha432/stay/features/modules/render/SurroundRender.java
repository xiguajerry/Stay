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
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 */
package me.alpha432.stay.features.modules.render;

import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.Iterator;

public class SurroundRender
extends Module {
    public EntityPlayer target;
    private final Setting<Integer> range = this.register(new Setting<>("Range", 5, 1, 10));

    public SurroundRender() {
        super("CityESP", "CityESP", Module.Category.VISUAL, true, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (SurroundRender.fullNullCheck()) {
            return;
        }
        this.target = this.getTarget(this.range.getValue().intValue());
        this.surroundRender();
    }

    private void surroundRender() {
        if (this.target == null) return;
        Vec3d a = this.target.getPositionVector();
        if (SurroundRender.mc.world.getBlockState(new BlockPos(a)).getBlock() == Blocks.OBSIDIAN || SurroundRender.mc.world.getBlockState(new BlockPos(a)).getBlock() == Blocks.ENDER_CHEST) {
            RenderUtil.drawBoxESP(new BlockPos(a), new Color(255, 255, 0), false, new Color(255, 255, 0), 1.0f, false, true, 42, true);
        }
        if (EntityUtils.getSurroundWeakness(a, -1, 1)) {
            this.surroundRender(a, -1.0, 0.0, 0.0, true);
        }
        if (EntityUtils.getSurroundWeakness(a, -1, 2)) {
            this.surroundRender(a, 1.0, 0.0, 0.0, true);
        }
        if (EntityUtils.getSurroundWeakness(a, -1, 3)) {
            this.surroundRender(a, 0.0, 0.0, -1.0, true);
        }
        if (EntityUtils.getSurroundWeakness(a, -1, 4)) {
            this.surroundRender(a, 0.0, 0.0, 1.0, true);
        }
        if (EntityUtils.getSurroundWeakness(a, -1, 5)) {
            this.surroundRender(a, -1.0, 0.0, 0.0, false);
        }
        if (EntityUtils.getSurroundWeakness(a, -1, 6)) {
            this.surroundRender(a, 1.0, 0.0, 0.0, false);
        }
        if (EntityUtils.getSurroundWeakness(a, -1, 7)) {
            this.surroundRender(a, 0.0, 0.0, -1.0, false);
        }
        if (!EntityUtils.getSurroundWeakness(a, -1, 8)) return;
        this.surroundRender(a, 0.0, 0.0, 1.0, false);
    }

    private void surroundRender(Vec3d pos, double x, double y, double z, boolean red) {
        BlockPos position = new BlockPos(pos).add(x, y, z);
        if (SurroundRender.mc.world.getBlockState(position).getBlock() == Blocks.AIR) return;
        if (SurroundRender.mc.world.getBlockState(position).getBlock() == Blocks.FIRE) {
            return;
        }
        if (red) {
            RenderUtil.drawBoxESP(position, new Color(255, 0, 0), false, new Color(255, 0, 0), 1.0f, false, true, 42, true);
            return;
        }
        RenderUtil.drawBoxESP(position, new Color(0, 0, 255), false, new Color(0, 0, 255), 1.0f, false, true, 42, true);
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        Iterator iterator = SurroundRender.mc.world.playerEntities.iterator();
        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer)iterator.next();
            if (EntityUtils.isntValid((Entity)player, range) || !EntityUtils.isInHole((Entity)player)) continue;
            if (target == null) {
                target = player;
                distance = SurroundRender.mc.player.getDistanceSq((Entity)player);
                continue;
            }
            if (!(SurroundRender.mc.player.getDistanceSq((Entity)player) < distance)) continue;
            target = player;
            distance = SurroundRender.mc.player.getDistanceSq((Entity)player);
        }
        return target;
    }
}

