/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:14
 */

package me.alpha432.stay.util.graphics.animations;

import me.alpha432.stay.util.basement.wrapper.Util;
import me.alpha432.stay.util.counting.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Deprecated
@SuppressWarnings("unused")
public class BlockRenderSmooth implements Util {
    private BlockPos lastPos;
    private BlockPos newPos;
    private final FadeUtils fade;
    public static Timer timer = new Timer();

    public BlockRenderSmooth (BlockPos pos , long smoothLength){
        lastPos = pos;
        newPos = pos;
        fade = new FadeUtils(smoothLength);
    }

    public void setNewPos (BlockPos pos){
        if(!pos.equals(newPos)&&timer.passedMs(200)){
            lastPos = newPos;
            newPos = pos;
            fade.reset();
            timer.reset();
        }
    }

    public Vec3d getRenderPos(){
        return lerp(PosToVec(lastPos),PosToVec(newPos),(float) fade.easeOutQuad());
    }

    public static Vec3d lerp(Vec3d from , Vec3d to, float t){
        return new Vec3d((t * to.x) + ((1 - t) * from.x), (t * to.y) + ((1 - t) * from.y), (t * to.z) + ((1 - t) * from.z));
    }

    private static Vec3d PosToVec(BlockPos pos){
        return new Vec3d(pos.getX(),pos.getY(),pos.getZ());
    }

}
