/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

/*
 * Decompiled with CFR 0.151.
 */
package me.alpha432.stay.features.modules.misc;


import me.alpha432.stay.features.modules.Module;

public class PacketEat
extends Module {
    private static PacketEat INSTANCE = new PacketEat();

    public PacketEat() {
        super("PacketEat", "PacketEat", Module.Category.MISC, true, false, false);
        this.setInstance();
    }

    public static PacketEat getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new PacketEat();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

