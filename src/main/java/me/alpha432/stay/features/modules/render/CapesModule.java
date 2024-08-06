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
import me.alpha432.stay.util.counting.Timer;


public class CapesModule extends Module {
    public CapesModule() {
        super("Capes", "CapesModule", Module.Category.VISUAL, false, false, false);
    }


    public Setting<ModeS> Mode = register(new Setting<>("CapesMod", ModeS.STAY));
    public Setting<String> URL = this.register(new Setting<>("URL", "URL", v->Mode.getValue()==ModeS.URL));
    public Setting<Boolean> ros =  this.register(new Setting<>("Auto", false));
    private Setting<Integer> delay = this.register(new Setting<>("Delay", 100, 0, 10000));
    public enum ModeS {
        STAY,
        Future,
        GS1,
        GS2,
        LJM,
        CAPE1,
        CAPE2,
        CAPE3,
        CAPE4,
        CAPE5,
        CAPE6,
        CAPE7,
        CAPE8,
        CAPE9,
        CAPE10,
        CAPE11,
        CAPE12,
        CAPE13,
        CAPE14,
        CAPE15,
        CAPE16,
        CAPE17,
        CAPE18,
        CAPE19,
        CAPE20,
        CAPE21,
        CAPE22,
        CAPE23,
        CAPE24,
        CAPE25,
        CAPE26,
        WUT1,
        WUT2,
        URL

    }
    private final Timer timer = new Timer();
    @Override
    public void onUpdate() {
        if(fullNullCheck()){
        return;
        }
        if(Mode.getValue() == ModeS.URL){
        return;
        }
        if(!ros.getValue()){
        return;
        }
        if (!this.timer.passedMs(this.delay.getValue().intValue())) return;

       if(Mode.getValue() == ModeS.STAY){
           Mode.setValue(ModeS.Future);
            timer.reset();
        return;
       }
        if(Mode.getValue() == ModeS.Future){
            Mode.setValue(ModeS.GS1);
             timer.reset();
        return;
        }

        if(Mode.getValue() == ModeS.GS1){
            Mode.setValue(ModeS.GS2);
             timer.reset();
        return;
        }

        if(Mode.getValue() == ModeS.GS2){
            Mode.setValue(ModeS.LJM);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.LJM){
            Mode.setValue(ModeS.CAPE1);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE1){
            Mode.setValue(ModeS.CAPE2);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE2){
            Mode.setValue(ModeS.CAPE3);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE3){
            Mode.setValue(ModeS.CAPE4);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE4){
            Mode.setValue(ModeS.CAPE5);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE5){
            Mode.setValue(ModeS.CAPE6);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE6){
            Mode.setValue(ModeS.CAPE7);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE7){
            Mode.setValue(ModeS.CAPE8);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE8){
            Mode.setValue(ModeS.CAPE9);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE9){
            Mode.setValue(ModeS.CAPE10);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE10){
            Mode.setValue(ModeS.CAPE11);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE11){
            Mode.setValue(ModeS.CAPE12);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE12){
            Mode.setValue(ModeS.CAPE13);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE13){
            Mode.setValue(ModeS.CAPE14);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE14){
            Mode.setValue(ModeS.CAPE15);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE15){
            Mode.setValue(ModeS.CAPE16);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE16){
            Mode.setValue(ModeS.CAPE17);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE17){
            Mode.setValue(ModeS.CAPE18);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE18){
            Mode.setValue(ModeS.CAPE19);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE19){
            Mode.setValue(ModeS.CAPE20);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE20){
            Mode.setValue(ModeS.CAPE21);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE21){
            Mode.setValue(ModeS.CAPE22);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE22){
            Mode.setValue(ModeS.CAPE23);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE23){
            Mode.setValue(ModeS.CAPE24);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE24){
            Mode.setValue(ModeS.CAPE25);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE25){
            Mode.setValue(ModeS.CAPE26);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.CAPE26){
            Mode.setValue(ModeS.WUT1);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.WUT1){
            Mode.setValue(ModeS.WUT2);
             timer.reset();
        return;
        }
        if(Mode.getValue() == ModeS.WUT2){
            Mode.setValue(ModeS.STAY);
            timer.reset();
            return;
        }

    }





}