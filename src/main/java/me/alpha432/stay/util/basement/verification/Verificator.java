/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午8:17
 */

package me.alpha432.stay.util.basement.verification;

import me.alpha432.stay.util.network.NetworkUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

/**
  * @Author SpartanB312(B_312)
  */

@Mod(modid = Verificator.MODID, name = Verificator.NAME, version = Verificator.VERSION)
public class Verificator {
    public static final String MODID = "cuicanhwid";
    public static final String NAME = "HWID Verificator";
    public static final String VERSION = "1.0";
    public static List<String> hwidList = new ArrayList<>();


    /**
     * The key to encode hwid.Dont let other people know this!
     */
    public static final String KEY = "verify";


    /**
     * We recommend use paste bin https://pastebin.com/
     */
    public static final String HWID_URL = "http://47.106.126.97/HWID.txt";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Verify();
    }

    /**
     * You can use Mixin injector to invoke this method
     */
    public static void Verify(){
        //Here we get the HWID List From URL
        hwidList = NetworkUtil.getHWIDList();

        //Check HWID
        if(!hwidList.contains(HWIDUtil.getEncryptedHWID(KEY))){
            //Shutdown client and display message
            // TODO: 2021/11/9
//            FrameUtil.Display();
//            throw new NoStackTraceThrowable("Verify HWID Failed!");
        }

    }

}
