/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:25
 */

package me.alpha432.stay.util.interfaces;

import net.minecraft.client.Minecraft;

import java.util.Random;

public interface Globals {
    Minecraft mc = Minecraft.getMinecraft();
    Random random = new Random();
    char SECTIONSIGN = '\u00A7';

    default boolean nullCheck(){
        return mc.player == null || mc.world == null;
    }

}
