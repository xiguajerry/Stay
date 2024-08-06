/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.mixin;

import me.alpha432.stay.client.Stay;

/**
 * 此Class不能混淆，用于加载MOD
 */
public class ModHook {

    public static void preInit(){
        Stay.preInit();
    }

    public static void postInit(){
        Stay.postInit();
    }

}
