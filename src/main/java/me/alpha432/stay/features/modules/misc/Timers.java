/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;


import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;

/**
 * @author linustouchtips
 * @since 12/17/2020
 */

public class Timers extends Module {
    public Timers() {
        super("Timer",  "Modifies client-side ticks",Category.MISC,false,false,false);
    }
    private final Setting<Double> ticks = register(new Setting<>("Timer", 4.0, 0.0, 10.0));
    public Setting<Boolean> sync = register(new Setting<>("TPS Sync", false));
    public Setting<Boolean> bypass = register(new Setting<>("bypass xin", false));
    private final Setting<Integer> ISFM = register(new Setting<>("bypas sspeed", 50, 0, 100,v->bypass.getValue()));
    public int i=0;
    public int x =0;




    @Override
    public void onUpdate() {
        if (nullCheck())
            return;
        if(bypass.getValue()){
            if(i<=ISFM.getValue()){
                i++;
                mc.timer.tickLength = sync.getValue() ? 50.0f / (Stay.serverManager.getTPS()/ 20) : (float) (50.0f / ticks.getValue());
                x=0;
            }else {
                if(x<= ISFM.getValue()-ISFM.getValue()/2/2){
                    x++;
                    mc.timer.tickLength = 50.0f;

                }else {
                    i=0;
                }
            }

        }else {
            mc.timer.tickLength = sync.getValue() ? 50.0f / (Stay.serverManager.getTPS()/ 20) : (float) (50.0f / ticks.getValue());
        }

    }

    @Override
    public void onDisable() {
        mc.timer.tickLength = 50.0f;

    }
}
