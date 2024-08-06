/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.render;

import com.google.common.collect.Sets;
import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import me.alpha432.stay.util.math.HoleUtil2;
import me.alpha432.stay.util.graphics.color.GSColor;
import me.alpha432.stay.util.math.GeometryMasks;
import me.alpha432.stay.util.player.PlayerUtil;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HoleESP2 extends Module {
    public HoleESP2() {
        super("HoleESP2", "Shows safe spots.", Module.Category.VISUAL, false, false, false);
    }

    private Setting<Integer> range = this.register(new Setting<>("Range", 10, 1, 20));

    public Setting<customHolesMode> customHoles = register(new Setting<>("Show", customHolesMode.Single));
    public Setting<typeMode> type = register(new Setting<>("Render", typeMode.Both));
    public Setting<modesss> mode = register(new Setting<>("Mode", modesss.Air));
    private Setting<Boolean> hideOwn = this.register(new Setting<>("Hide Own", false));
    private Setting<Boolean> flatOwn = this.register(new Setting<>("Flat Own", false));
    private Setting<Double> slabHeight = this.register(new Setting<>("Slab Height", 0.5, 0.1, 1.5));
    private Setting<Integer> width = this.register(new Setting<>("Width", 1, 1, 10));

    private Setting<Integer> bedrockColor1 = this.register(new Setting<>("bedrockColor.Red", 0, 0, 255));
    private Setting<Integer> bedrockColor2 = this.register(new Setting<>("bedrockColor.Green", 255, 0, 255));
    private Setting<Integer> bedrockColor3 = this.register(new Setting<>("bedrockColor.Blue", 0, 0, 255));

    private Setting<Integer> obsidianColor1 = this.register(new Setting<>("obsidianColor.Red", 255, 0, 255));
    private Setting<Integer> obsidianColor2 = this.register(new Setting<>("obsidianColor.Green", 0, 0, 255));
    private Setting<Integer> obsidianColor3 = this.register(new Setting<>("obsidianColor.Blue", 0, 0, 255));

    private Setting<Integer> customColor1 = this.register(new Setting<>("customColor.Red", 255, 0, 255));
    private Setting<Integer> customColor2 = this.register(new Setting<>("customColor.Green", 0, 0, 255));
    private Setting<Integer> customColor3 = this.register(new Setting<>("customColor.Blue", 255, 0, 255));

    private Setting<Integer> ufoAlpha = this.register(new Setting<>("UFOAlpha", 255, 0, 255));
    public enum customHolesMode {
        Single,
        Double,
        Custom

    }
    public enum modesss {
        Air,
        Ground,
        Flat,
        Slab,
        Double

    }
    public enum typeMode {
        Outline,
        Fill,
        Both

    }
    private ConcurrentHashMap<AxisAlignedBB, GSColor> holes;

    public void onUpdate() {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (holes == null) {
            holes = new ConcurrentHashMap<>();
        } else {
            holes.clear();
        }

        int range = (int) Math.ceil(this.range.getValue());

        HashSet<BlockPos> possibleHoles = Sets.newHashSet();
        List<BlockPos> blockPosList = EntityUtils.getSphere(PlayerUtil.getPlayerPos(), range, range, false, true, 0);

        for (BlockPos pos : blockPosList) {

            if (!mc.world.getBlockState(pos).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }
            if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().equals(Blocks.AIR)) {
                continue;
            }

            if (mc.world.getBlockState(pos.add(0, 2, 0)).getBlock().equals(Blocks.AIR)) {
                possibleHoles.add(pos);
            }
        }

        possibleHoles.forEach(pos -> {
            HoleUtil2.HoleInfo holeInfo = HoleUtil2.isHole(pos, false, false);
            HoleUtil2.HoleType holeType = holeInfo.getType();

            if (holeType != HoleUtil2.HoleType.NONE) {

                HoleUtil2.BlockSafety holeSafety = holeInfo.getSafety();
                AxisAlignedBB centreBlocks = holeInfo.getCentre();

                if (centreBlocks == null)
                    return;

                GSColor colour;

                if (holeSafety == HoleUtil2.BlockSafety.UNBREAKABLE) {
                    colour = new GSColor(new GSColor(new GSColor(bedrockColor1.getValue(),bedrockColor2.getValue(),bedrockColor3.getValue()) ), 255);
                } else {
                    colour = new GSColor(new GSColor(new GSColor(obsidianColor1.getValue(),obsidianColor2.getValue(),obsidianColor3.getValue()) ), 255);
                }
                if (holeType == HoleUtil2.HoleType.CUSTOM) {
                    colour =new GSColor(new GSColor(new GSColor(customColor1.getValue(),customColor2.getValue(),customColor3.getValue()) ), 255);
                }

                String mode = customHoles.getValue().toString();
                if (mode.equalsIgnoreCase("Custom") && (holeType == HoleUtil2.HoleType.CUSTOM || holeType == HoleUtil2.HoleType.DOUBLE)) {
                    holes.put(centreBlocks, colour);
                } else if (mode.equalsIgnoreCase("Double") && holeType == HoleUtil2.HoleType.DOUBLE) {
                    holes.put(centreBlocks, colour);
                } else if (holeType == HoleUtil2.HoleType.SINGLE) {
                    holes.put(centreBlocks, colour);
                }
            }
        });
    }
    @Override
    public void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.world == null || holes == null || holes.isEmpty()) {
            return;
        }

        holes.forEach(this::renderHoles);
    }

    private void renderHoles(AxisAlignedBB hole, GSColor color) {
        switch (type.getValue().toString()) {
            case "Outline": {
                renderOutline(hole, color);
                break;
            }
            case "Fill": {
                renderFill(hole, color);
                break;
            }
            case "Both": {
                renderOutline(hole, color);
                renderFill(hole, color);
                break;
            }
        }
    }

    private void renderFill(AxisAlignedBB hole, GSColor color) {
        GSColor fillColor = new GSColor(color, 50);
        int ufoAlpha = (this.ufoAlpha.getValue() * 50) / 255;

        if (hideOwn.getValue() && hole.intersects(mc.player.getEntityBoundingBox())) return;

        switch (mode.getValue().toString()) {
            case "Air": {
                if (flatOwn.getValue() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.DOWN);
                } else {
                    RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.ALL);
                }
                break;
            }
            case "Ground": {
                RenderUtil.drawBox(hole.offset(0, -1, 0), true, 1, new GSColor(fillColor, ufoAlpha), fillColor.getAlpha(), GeometryMasks.Quad.ALL);
                break;
            }
            case "Flat": {
                RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.DOWN);
                break;
            }
            case "Slab": {
                if (flatOwn.getValue() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.DOWN);
                } else {
                    RenderUtil.drawBox(hole, false, slabHeight.getValue(), fillColor, ufoAlpha, GeometryMasks.Quad.ALL);
                }
                break;
            }
            case "Double": {
                if (flatOwn.getValue() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBox(hole, true, 1, fillColor, ufoAlpha, GeometryMasks.Quad.DOWN);
                } else {
                    RenderUtil.drawBox(hole.setMaxY(hole.maxY + 1), true, 2, fillColor, ufoAlpha, GeometryMasks.Quad.ALL);
                }
                break;
            }
        }
    }

    private void renderOutline(AxisAlignedBB hole, GSColor color) {
        GSColor outlineColor = new GSColor(color, 255);

        if (hideOwn.getValue() && hole.intersects(mc.player.getEntityBoundingBox())) return;

        switch (mode.getValue().toString()) {
            case "Air": {
                if (flatOwn.getValue() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, width.getValue(), outlineColor, ufoAlpha.getValue(), GeometryMasks.Quad.DOWN);
                } else {
                    RenderUtil.drawBoundingBox(hole, width.getValue(), outlineColor, ufoAlpha.getValue());
                }
                break;
            }
            case "Ground": {
                RenderUtil.drawBoundingBox(hole.offset(0, -1, 0), width.getValue(), new GSColor(outlineColor, ufoAlpha.getValue()), outlineColor.getAlpha());
                break;
            }
            case "Flat": {
                RenderUtil.drawBoundingBoxWithSides(hole, width.getValue(), outlineColor, ufoAlpha.getValue(), GeometryMasks.Quad.DOWN);
                break;
            }
            case "Slab": {
                if (this.flatOwn.getValue() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, width.getValue(), outlineColor, ufoAlpha.getValue(), GeometryMasks.Quad.DOWN);
                } else {
                    RenderUtil.drawBoundingBox(hole.setMaxY(hole.minY + slabHeight.getValue()), width.getValue(), outlineColor, ufoAlpha.getValue());
                }
                break;
            }
            case "Double": {
                if (this.flatOwn.getValue() && hole.intersects(mc.player.getEntityBoundingBox())) {
                    RenderUtil.drawBoundingBoxWithSides(hole, width.getValue(), outlineColor, ufoAlpha.getValue(), GeometryMasks.Quad.DOWN);
                } else {
                    RenderUtil.drawBoundingBox(hole.setMaxY(hole.maxY + 1), width.getValue(), outlineColor, ufoAlpha.getValue());
                }
                break;
            }
        }
    }



}
