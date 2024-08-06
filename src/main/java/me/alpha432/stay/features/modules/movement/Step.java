/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.movement;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayer;

public class Step extends Module {
    public Step() {
        super("Step", "placeholder", Category.MOVEMENT, false, false, false);
    }

    public Setting<Mode> mode = register(new Setting<>("Mode", Mode.Teleport));
    private final Setting<Double> height = register(new Setting<>("Height", 2.0, 0.0, 10.0));
    private final Setting<Double> timerTicks = register(new Setting<>("Timer Speed", 0.5D, 0.1D, 2.0D));
    public Setting<Boolean> useTimer = register(new Setting<>("use Timer", false));
    public Setting<Boolean> entityStep = register(new Setting<>("Entity Step", false));
    public Setting<Boolean> sneakPause = register(new Setting<>("When Sneaking", false));
    public Setting<Boolean> waterPause = register(new Setting<>("When in Liquid", true));
    public Setting<Modes> disable = register(new Setting<>("Disable", Modes.Never));
    double[] forwardStep;
    double originalHeight;

    public enum Mode {
        Teleport,
        Spider,
        Vanilla

    }

    public enum Modes {
        Never,
        Completion


    }

    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        originalHeight = mc.player.posY;
        return;
    }

    @Override
    public void onUpdate() {
        if (nullCheck())
            return;

        if (!mc.player.collidedHorizontally)
            return;

        if (mc.player.isOnLadder() || mc.player.movementInput.jump)
            return;

        if ((mc.player.isInWater() || mc.player.isInLava()) && waterPause.getValue())
            return;

        if (!mc.player.onGround)
            return;

        if (useTimer.getValue())
            mc.timer.tickLength = (float) (50.0f / timerTicks.getValue());

        if (entityStep.getValue() && mc.player.isRiding())
            mc.player.ridingEntity.stepHeight = (float) (height.getValue() / 1);

        if (mc.player.isSneaking() && sneakPause.getValue())
            return;

        forwardStep = getMoveSpeed(0.1);

        if (getStepHeight().equals(StepHeight.Unsafe)) {
            if (disable.getValue() == Modes.Never) {
                this.disable();
            }

            return;
        }
        if (mode.getValue() == Mode.Teleport) {
            stepTeleport();
        }
        if (mode.getValue() == Mode.Spider) {
            stepSpider();
        }
        if (mode.getValue() == Mode.Vanilla) {
            stepVanilla();
        }




        if (mc.player.posY > originalHeight + getStepHeight().height && disable.getValue() ==Modes.Completion)
            this.disable();
    }

    public static double[] getMoveSpeed(double speed) {
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;

        double motionX;
        double motionZ;

        if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += (float) (forward > 0.0f ? -45 : 45);
                strafe = 0.0f;
            } else if (strafe <= -1.0f) {
                yaw += (float) (forward > 0.0f ? 45 : -45);
                strafe = 0.0f;
            }

            if (forward > 0.0f)
                forward = 1.0f;

            else if (forward < 0.0f)
                forward = -1.0f;
        }

        double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        double cos = Math.cos(Math.toRadians(yaw + 90.0f));

        motionX = (double) forward * speed * cos + (double) strafe * speed * sin;
        motionZ = (double) forward * speed * sin - (double) strafe * speed * cos;

        if (!isMoving()) {
            motionX = 0;
            motionZ = 0;
        }

        return new double[]{
                motionX, motionZ
        };
    }

    public static boolean isMoving() {
        return (mc.player.moveForward != 0.0D || mc.player.moveStrafing != 0.0D);
    }

    public void stepTeleport() {
        updateStepPackets(getStepHeight().stepArray);
        mc.player.setPosition(mc.player.posX, mc.player.posY + getStepHeight().height, mc.player.posZ);
    }

    public void stepSpider() {
        updateStepPackets(getStepHeight().stepArray);

        mc.player.motionY = 0.2;
        mc.player.fallDistance = 0;
    }

    public void stepVanilla() {
        mc.player.setPosition(mc.player.posX, mc.player.posY + getStepHeight().height, mc.player.posZ);
    }

    public void updateStepPackets(double[] stepArray) {
        for (double v : stepArray) {
            mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + v, mc.player.posZ, mc.player.onGround));
        }
    }

    public StepHeight getStepHeight() {
        if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.0, forwardStep[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 0.6, forwardStep[1])).isEmpty())
            return StepHeight.One;
        else if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.6, forwardStep[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.4, forwardStep[1])).isEmpty())
            return StepHeight.OneHalf;
        else if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.1, forwardStep[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 1.9, forwardStep[1])).isEmpty())
            return StepHeight.Two;
        else if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.6, forwardStep[1])).isEmpty() && !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(forwardStep[0], 2.4, forwardStep[1])).isEmpty())
            return StepHeight.TwoHalf;
        else
            return StepHeight.Unsafe;
    }

    public enum StepHeight {
        One(1, new double[]{0.42, 0.753}),
        OneHalf(1.5, new double[]{0.42, 0.75, 1.0, 1.16, 1.23, 1.2}),
        Two(2, new double[]{0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43}),
        TwoHalf(2.5, new double[]{0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907}),
        Unsafe(3, new double[]{0});

        double[] stepArray;
        double height;

        StepHeight(double height, double[] stepArray) {
            this.height = height;
            this.stepArray = stepArray;
        }
    }


}
