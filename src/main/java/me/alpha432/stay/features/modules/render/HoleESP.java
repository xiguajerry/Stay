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
import me.alpha432.stay.features.modules.client.ClickGui;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.graphics.color.ColorUtil;
import me.alpha432.stay.util.math.HoleUtil;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

import java.awt.*;

/**
 * @Author BrownZombie
 * @Date 2/10/21
 * @Version 1.0.5
 */

public class HoleESP extends Module {
    public Setting<Boolean> renderOwn = register(new Setting<>("RenderOwn", true));
    public Setting<Boolean> fov = register(new Setting<>("InFov", true));
    public Setting<Boolean> rainbow = register(new Setting<>("Rainbow", false));
    private final Setting<Integer> range = register(new Setting<>("RangeX", 0, 0, 10));
    private final Setting<Integer> rangeY = register(new Setting<>("RangeY", 0, 0, 10));
    public Setting<Boolean> box = register(new Setting<>("Box", true));
    public Setting<Boolean> boxs = register(new Setting<>("Double Hole", true));
    public Setting<Boolean> gradientBox = register(new Setting<>("Gradient", Boolean.valueOf(false), v -> box.getValue()));
    public Setting<Boolean> invertGradientBox = register(new Setting<>("ReverseGradient", Boolean.valueOf(false), v -> gradientBox.getValue()));
    public Setting<Boolean> outline = register(new Setting<>("Outline", true));
    public Setting<Boolean> gradientOutline = register(new Setting<>("GradientOutline", Boolean.valueOf(false), v -> outline.getValue()));
    public Setting<Boolean> invertGradientOutline = register(new Setting<>("ReverseOutline", Boolean.valueOf(false), v -> gradientOutline.getValue()));
    public Setting<Double> height = register(new Setting<>("Height", 0.0, -2.0, 2.0));
    private Setting<Integer> red = register(new Setting<>("Red", 0, 0, 255));
    private Setting<Integer> green = register(new Setting<>("Green", 255, 0, 255));
    private Setting<Integer> blue = register(new Setting<>("Blue", 0, 0, 255));
    private Setting<Integer> alpha = register(new Setting<>("Alpha", 255, 0, 255));
    private Setting<Integer> boxAlpha = register(new Setting<>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> box.getValue()));
    private Setting<Float> lineWidth = register(new Setting<>("LineWidth", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(5.0f), v -> outline.getValue()));
    public Setting<Boolean> safeColor = register(new Setting<>("BedrockColor", false));
    private Setting<Integer> safeRed = register(new Setting<>("BedrockRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> safeColor.getValue()));
    private Setting<Integer> safeGreen = register(new Setting<>("BedrockGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> safeColor.getValue()));
    private Setting<Integer> safeBlue = register(new Setting<>("BedrockBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> safeColor.getValue()));
    private Setting<Integer> safeAlpha = register(new Setting<>("BedrockAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> safeColor.getValue()));
    public Setting<Boolean> customOutline = register(new Setting<>("CustomLine", Boolean.valueOf(false), v -> outline.getValue()));
    private Setting<Integer> cRed = register(new Setting<>("OL-Red", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false));
    private Setting<Integer> cGreen = register(new Setting<>("OL-Green", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false));
    private Setting<Integer> cBlue = register(new Setting<>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false));
    private Setting<Integer> cAlpha = register(new Setting<>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false));
    private Setting<Integer> safecRed = register(new Setting<>("OL-SafeRed", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false && safeColor.getValue() != false));
    private Setting<Integer> safecGreen = register(new Setting<>("OL-SafeGreen", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false && safeColor.getValue() != false));
    private Setting<Integer> safecBlue = register(new Setting<>("OL-SafeBlue", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false && safeColor.getValue() != false));
    private Setting<Integer> safecAlpha = register(new Setting<>("OL-SafeAlpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> customOutline.getValue() != false && outline.getValue() != false && safeColor.getValue() != false));
    private static HoleESP INSTANCE = new HoleESP();
    private int currentAlpha = 0;

    public HoleESP() {
        super("HoleESP", "Shows safe spots.", Module.Category.VISUAL, false, false, false);
        setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static HoleESP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleESP();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        assert (HoleESP.mc.renderViewEntity != null);
        Vec3i playerPos = new Vec3i(HoleESP.mc.renderViewEntity.posX, HoleESP.mc.renderViewEntity.posY, HoleESP.mc.renderViewEntity.posZ);
        for (int x = playerPos.getX() - range.getValue(); x < playerPos.getX() + range.getValue(); ++x) {
            for (int z = playerPos.getZ() - range.getValue(); z < playerPos.getZ() + range.getValue(); ++z) {
                for (int y = playerPos.getY() + rangeY.getValue(); y > playerPos.getY() - rangeY.getValue(); --y) {
                    BlockPos pos = new BlockPos(x, y, z);
                   if(boxs.getValue()){
                       if(HoleUtil.is2securityHole(pos)&&( mc.world.getBlockState(new BlockPos(pos.getX(),pos.getY()+1,pos.getZ())).getBlock()==Blocks.AIR||mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(),HoleUtil.is2Hole(pos).getY()+1,HoleUtil.is2Hole(pos).getZ())).getBlock()==Blocks.AIR  )&&mc.world.getBlockState(new BlockPos(pos.getX(),pos.getY()-1,pos.getZ())).getBlock()==Blocks.BEDROCK&&mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(),HoleUtil.is2Hole(pos).getY()-1,HoleUtil.is2Hole(pos).getZ())).getBlock()==Blocks.BEDROCK){
                           RenderUtil.drawBoxESP(pos, rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue(), safeAlpha.getValue()), customOutline.getValue(), new Color(safecRed.getValue(), safecGreen.getValue(), safecBlue.getValue(), safecAlpha.getValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), currentAlpha);
                           RenderUtil.drawBoxESP(HoleUtil.is2Hole(pos), rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue(), safeAlpha.getValue()), customOutline.getValue(), new Color(safecRed.getValue(), safecGreen.getValue(), safecBlue.getValue(), safecAlpha.getValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), currentAlpha);
                           continue;

                       }

                       if(HoleUtil.is2Hole(pos)!=null&&( mc.world.getBlockState(new BlockPos(pos.getX(),pos.getY()+1,pos.getZ())).getBlock()==Blocks.AIR||mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(),HoleUtil.is2Hole(pos).getY()+1,HoleUtil.is2Hole(pos).getZ())).getBlock()==Blocks.AIR  )&&(mc.world.getBlockState(new BlockPos(pos.getX(),pos.getY()-1,pos.getZ())).getBlock()==Blocks.BEDROCK||mc.world.getBlockState(new BlockPos(pos.getX(),pos.getY()-1,pos.getZ())).getBlock()==Blocks.OBSIDIAN) &&(mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(),HoleUtil.is2Hole(pos).getY()-1,HoleUtil.is2Hole(pos).getZ())).getBlock()==Blocks.BEDROCK||mc.world.getBlockState(new BlockPos(HoleUtil.is2Hole(pos).getX(),HoleUtil.is2Hole(pos).getY()-1,HoleUtil.is2Hole(pos).getZ())).getBlock()==Blocks.OBSIDIAN)){
                           RenderUtil.drawBoxESP(pos, rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), customOutline.getValue(), new Color(cRed.getValue(), cGreen.getValue(), cBlue.getValue(), cAlpha.getValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), currentAlpha);
                           RenderUtil.drawBoxESP(HoleUtil.is2Hole(pos), rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), customOutline.getValue(), new Color(cRed.getValue(), cGreen.getValue(), cBlue.getValue(), cAlpha.getValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), currentAlpha);
                           continue;
                       }
                   }

                    if (!HoleESP.mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !HoleESP.mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR) || pos.equals(new BlockPos(HoleESP.mc.player.posX, HoleESP.mc.player.posY, HoleESP.mc.player.posZ)) && !renderOwn.getValue().booleanValue() || !BlockUtil.isPosInFov(pos).booleanValue() && fov.getValue().booleanValue())
                        continue;

                    if (HoleESP.mc.world.getBlockState(pos.north()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.east()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.west()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.south()).getBlock() == Blocks.BEDROCK && HoleESP.mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK) {
                        RenderUtil.drawBoxESP(pos, rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(safeRed.getValue(), safeGreen.getValue(), safeBlue.getValue(), safeAlpha.getValue()), customOutline.getValue(), new Color(safecRed.getValue(), safecGreen.getValue(), safecBlue.getValue(), safecAlpha.getValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), currentAlpha);
                        continue;
                    }
                    if (!BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.down()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.east()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.west()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.south()).getBlock()) || !BlockUtil.isBlockUnSafe(HoleESP.mc.world.getBlockState(pos.north()).getBlock()))
                        continue;

                    RenderUtil.drawBoxESP(pos, rainbow.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(red.getValue(), green.getValue(), blue.getValue(), alpha.getValue()), customOutline.getValue(), new Color(cRed.getValue(), cGreen.getValue(), cBlue.getValue(), cAlpha.getValue()), lineWidth.getValue().floatValue(), outline.getValue(), box.getValue(), boxAlpha.getValue(), true, height.getValue(), gradientBox.getValue(), gradientOutline.getValue(), invertGradientBox.getValue(), invertGradientOutline.getValue(), currentAlpha);
                }
            }
        }
    }
}