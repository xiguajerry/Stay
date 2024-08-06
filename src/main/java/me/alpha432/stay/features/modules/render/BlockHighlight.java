/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.render;

import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.graphics.color.ColorUtil;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

public class BlockHighlight extends Module {
    private final Setting<Float> lineWidth = register(new Setting<>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f)));
    private final Setting<Integer> alpha = register(new Setting<>("Alpha", 255, 0, 255));
    private final Setting<Integer> red = register(new Setting<>("Red", 255, 0, 255));
    private final Setting<Integer> green = register(new Setting<>("Green", 255, 0, 255));
    private final Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255));
    private final Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    private final Setting<Integer> rainbowhue = register(new Setting<>("RainbowHue", 255, 0, 255, v -> rainbow.getValue()));


    public BlockHighlight() {
        super("BlockHighlight", "Highlights the block u look at.", Module.Category.VISUAL, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ray.getBlockPos();
            RenderUtil.drawBlockOutline(blockpos, rainbow.getValue() != false ? ColorUtil.rainbow(rainbowhue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), lineWidth.getValue().floatValue(), false);
        }
    }
}

