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
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.client.ClickGui;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.graphics.color.ColorUtil;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.awt.*;

public class Burrowesp extends Module {

    public Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    private final Setting<Integer> range = register(new Setting<>("Range", 10, 0, 20));
    public Setting<Boolean> box = register(new Setting<>("Box", true));
    public Setting<Boolean> gradientBox = register(new Setting<>("Gradient", Boolean.valueOf(false), v -> box.getValue()));
    public Setting<Boolean> invertGradientBox = register(new Setting<>("ReverseGradient", Boolean.valueOf(false), v -> gradientBox.getValue()));
    public Setting<Boolean> outline = register(new Setting<>("Outline", true));
    public Setting<Boolean> gradientOutline = register(new Setting<>("GradientOutline", Boolean.valueOf(false), v -> outline.getValue()));
    public Setting<Boolean> invertGradientOutline = register(new Setting<>("ReverseOutline", Boolean.valueOf(false), v -> gradientOutline.getValue()));
    public Setting<Double> height = register(new Setting<>("Height", 0.0, -2.0, 2.0));

    private Setting<Integer> boxAlpha = register(new Setting<>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> box.getValue()));
    private Setting<Float> lineWidth = register(new Setting<>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> outline.getValue()));
    public Setting<Boolean> safeColor = register(new Setting<>("BedrockColor", false));
    private Setting<Integer> safeRed = register(new Setting<>("BedrockRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> safeColor.getValue()));
    private Setting<Integer> safeGreen = register(new Setting<>("BedrockGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> safeColor.getValue()));
    private Setting<Integer> safeBlue = register(new Setting<>("BedrockBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> safeColor.getValue()));
    private Setting<Integer> safeAlpha = register(new Setting<>("BedrockAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> safeColor.getValue()));
    public Setting<Boolean> customOutline = register(new Setting<>("CustomLine", Boolean.valueOf(false), v -> outline.getValue()));
    private Setting<Integer> safecRed = register(new Setting<>("OL-SafeRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false && safeColor.getValue() != false));
    private Setting<Integer> safecGreen = register(new Setting<>("OL-SafeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false && safeColor.getValue() != false));
    private Setting<Integer> safecBlue = register(new Setting<>("OL-SafeBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false && safeColor.getValue() != false));
    private Setting<Integer> safecAlpha = register(new Setting<>("OL-SafeAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false && safeColor.getValue() != false));
    public Burrowesp() {
        super("Burrowesp", "Burrowesp", Module.Category.VISUAL, false, false, false);
    }
    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : mc.world.playerEntities) {
            if (EntityUtils.isntValid(player, range) || Stay.speedManager.getPlayerSpeed(player) > 10.0)
                continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }
            if (!(mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = mc.player.getDistanceSq(player);

        }
        return target;
    }
    @Override
    public void onRender3D(Render3DEvent event) {
        EntityPlayer pss=  getTarget(range.getValue());
        if(pss!=null){

            if(mc.world.getBlockState(new BlockPos(pss.posX,pss.posY,pss.posZ)).getBlock()== Blocks.AIR||mc.world.getBlockState(new BlockPos(pss.posX,pss.posY,pss.posZ)).getBlock()== Blocks.WATER||mc.world.getBlockState(new BlockPos(pss.posX,pss.posY,pss.posZ)).getBlock()== Blocks.LAVA){
                pss = null;
                return;
            }

            RenderUtil.drawBoxESP(new BlockPos(pss.posX,pss.posY,pss.posZ), rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue(), safeAlpha.getValue()), customOutline.getValue(), new Color(safecRed.getValue(), safecGreen.getValue(), safecBlue.getValue(), safecAlpha.getValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), 0);
        }

    }
}
