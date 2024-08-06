/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.player;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;

public class TpsSync extends Module {
    private static TpsSync INSTANCE = new TpsSync();
    public Setting<Boolean> attack = register(new Setting<>("Attack", Boolean.FALSE));
    public Setting<Boolean> mining = register(new Setting<>("Mine", Boolean.TRUE));

    public TpsSync() {
        super("TpsSync", "Syncs your client with the TPS.", Module.Category.PLAYER, true, false, false);
        setInstance();
    }

    public static TpsSync getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TpsSync();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }
}

