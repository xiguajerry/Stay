/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.loader;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.mixin.ModHook;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = Stay.MOD_ID,
        name = Stay.MOD_NAME,
        version = Stay.VERSION
)
public class ForgeEntry {
    // TODO: 2021/11/11
    public static boolean shouldLoad = true;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if (shouldLoad) ModHook.preInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (shouldLoad) ModHook.postInit();
    }

    public static void register(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }

    public static void unregister(Object obj) {
        MinecraftForge.EVENT_BUS.unregister(obj);
    }

}
