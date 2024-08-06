/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:32
 */
package me.alpha432.stay.util.basement

import me.alpha432.stay.client.Stay
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import org.lwjgl.opengl.Display

object Title {
    var cachedTitle = "Stay " + Stay.VERSION + " | "

    init {
        val titles = arrayOf(
            "Do one thing at a time, and do well.",
            "Keep on going, and never give up.",
            "Whatever is worth doing is worth doing well.",
            "You cannot improve your past, but you can improve your future. Once time is wasted, life is wasted.",
            "Don't aim for success if you want it; just do what you love and believe in, and it will come naturally.",
            "Whether it is the old man, or the young, it is the last time for us to sail.",
            "Is the true wisdom fortitude ambition.",
            "Life is precious, you have to pay no sooner.",
            "Life must be self weight.",
            "Genius out of diligence.",
            "Live a live you will remember.",
            "When your dreams come alive you're unstoppable.",
            "Take the shot, chase the sun, find the beautiful.",
            "We will glow in the dark turning dust to gold.",
            "The past, is everything we were, don't make us who we are.",
            "Yeah you better know where you're going.",
            "Keep goin' and never give up.",
            "Powered by SagiriAntiLeak"
        )
        cachedTitle += titles[(Math.random() * titles.size).toInt()]
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent?) {
        Display.setTitle(cachedTitle)
        //        ++ticks;
//        if (ticks % 17 == 0)
//        {
//            Display.setTitle((bruh1.substring(0, bruh1.length()-bruh)));
//            if ((bruh == bruh1.length() && breakTimer != 2) || (bruh == 0 && breakTimer != 4)) {
//                breakTimer++;
//                return;
//            } else breakTimer = 0;
//            if (bruh == bruh1.length()) qwerty = true;
//            if (qwerty) --bruh;
//            else ++bruh;
//            if (bruh == 0) qwerty = false;
//        }
    }
}