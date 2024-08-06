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
import me.alpha432.stay.util.graphics.color.Colour;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BreakESP extends Module {
    public BreakESP() {
        super("BreakESP", "BreakESP", Module.Category.VISUAL, false, false, false);
    }

    public Setting<Boolean> ros =  this.register(new Setting<>("No Animation", true));


    public void onRender3D(Render3DEvent event) {
        mc.renderGlobal.damagedBlocks.forEach((integer, destroyBlockProgress) -> {
            if (destroyBlockProgress != null&&integer!=null) {


                BlockPos blockPos = destroyBlockProgress.getPosition();

                if (mc.world.getBlockState(blockPos).getBlock() == Blocks.AIR) return;

                if (blockPos.getDistance((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ) <= 6&&blockPos!=null&&integer!=null) {

                    int progress = destroyBlockProgress.getPartialBlockDamage();
                    String neam = mc.world.getEntityByID(integer).getName();
                    if(!neam.equals("")){
                       return;
                    }
                    AxisAlignedBB axisAlignedBB = mc.world.getBlockState(blockPos).getSelectedBoundingBox(mc.world, blockPos);
                    RenderUtil.drawText(blockPos,  neam+">"+(progress * 10+20) + "%");
                    axisAlignedBB = calcBB(axisAlignedBB, progress);
                    renderESP(axisAlignedBB, progress+2);
                }
            }
        });
    }
    private AxisAlignedBB calcBB(AxisAlignedBB bb, int state){
        AxisAlignedBB rbb = bb;
        switch (state){
            case 0:
                rbb = bb.shrink(0.6);
                break;
            case 1:
                rbb = bb.shrink(0.65);
                break;
            case 2:
                rbb = bb.shrink(0.7);
                break;
            case 3:
                rbb = bb.shrink(0.75);
                break;
            case 4:
                rbb = bb.shrink(0.8);
                break;
            case 5:
                rbb = bb.shrink(0.85);
                break;
            case 6:
                rbb = bb.shrink(0.9);
                break;
            case 7:
                rbb = bb.shrink(0.95);
                break;
            case 8:
                rbb = bb;
                break;
        }
        return rbb;
    }

    private void renderESP(AxisAlignedBB axisAlignedBB, int progress) {


        double centerX = axisAlignedBB.minX + ((axisAlignedBB.maxX - axisAlignedBB.minX) / 2);
        double centerY = axisAlignedBB.minY + ((axisAlignedBB.maxY - axisAlignedBB.minY) / 2);
        double centerZ = axisAlignedBB.minZ + ((axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2);
        double progressValX = progress * ((axisAlignedBB.maxX - centerX) / 10);
        double progressValY = progress * ((axisAlignedBB.maxY - centerY) / 10);
        double progressValZ = progress * ((axisAlignedBB.maxZ - centerZ) / 10);

        AxisAlignedBB axisAlignedBB1 = new AxisAlignedBB(centerX - progressValX, centerY - progressValY, centerZ - progressValZ, centerX + progressValX, centerY + progressValY, centerZ + progressValZ);
    RenderUtil.drawBBBox(axisAlignedBB1,new Colour(255,255,255, 200),new Colour(255,255,255, 200).getAlpha());
//        RenderUtil.drawBoundingBox(axisAlignedBB1, 1, outlineColor);
    }

}
