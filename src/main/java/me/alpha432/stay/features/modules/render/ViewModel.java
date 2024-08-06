/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.render;


import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;

public class ViewModel extends Module {
    public float defaultFov;
    public Setting<Integer> viewmodelDistance = register(new Setting<>("ViewmodelDistance", 125, 0, 170, "Changes the distance of the Viewmodel"));
    public ViewModel() {
        super("ViewModel", "Changes viewmodel of items", Category.VISUAL, false, false, false);
    }
    public Setting<Boolean> noEat = this.register(new Setting<>("No Eat", false));
    public final Setting<Double> mainX = register(new Setting<>("mainX", 1.2, 0.0, 6.0));
    public final Setting<Double> mainY = register(new Setting<>("mainY", -0.95, -3.0, 3.0));
    public final Setting<Double> mainZ = register(new Setting<>("mainZ", -1.45, -5.0, 5.0));
    public final Setting<Double> offX = register(new Setting<>("offX", -1.2, -6.0, 0.0));
    public final Setting<Double> offY = register(new Setting<>("offY", -0.95, -3.0, 3.0));
    public final Setting<Double> offZ = register(new Setting<>("offZ", -1.45, -5.0, 5.0));
    public final Setting<Double> mainAngel =register(new Setting<>("mainAngle", 0.0, 0.0, 360.0));
    public final Setting<Double> mainRx = register(new Setting<>("mainRotationPointX", 0.0, -1.0, 1.0));
    public final Setting<Double> mainRy = register(new Setting<>("mainRotationPointY", 0.0, -1.0, 1.0));
    public final Setting<Double> mainRz = register(new Setting<>("mainRotationPointZ", 0.0, -1.0, 1.0));
    public final Setting<Double> offAngel = register(new Setting<>("offAngle", 0.0, 0.0, 360.0));
    public final Setting<Double> offRx = register(new Setting<>("offRotationPointX", 0.0, -1.0, 1.0));
    public final Setting<Double> offRy = register(new Setting<>("offRotationPointY", 0.0, -1.0, 1.0));
    public final Setting<Double> offRz = register(new Setting<>("offRotationPointZ", 0.0, -1.0, 1.0));
    public final Setting<Double> mainScaleX = register(new Setting<>("mainScaleX", 1.0, -5.0, 10.0));
    public final Setting<Double> mainScaleY = register(new Setting<>("mainScaleY", 1.0, -5.0, 10.0));
    public final Setting<Double> mainScaleZ =register(new Setting<>("mainScaleZ", 1.0, -5.0, 10.0));
    public final Setting<Double> offScaleX = register(new Setting<>("offScaleX", 1.0, -5.0, 10.0));
    public final Setting<Double> offScaleY =register(new Setting<>("offScaleY", 1.0, -5.0, 10.0));
    public final Setting<Double> offScaleZ = register(new Setting<>("offScaleZ", 1.0, -5.0, 10.0));








}
