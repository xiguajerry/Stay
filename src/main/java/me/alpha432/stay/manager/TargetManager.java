/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.manager;

import me.alpha432.stay.util.interfaces.Globals;
import net.minecraft.entity.EntityLivingBase;

public class TargetManager implements Globals {

    private EntityLivingBase currentTarget = null;

    public void updateTarget(EntityLivingBase targetIn) {
        currentTarget = targetIn;
    }

    public EntityLivingBase getTarget() {
        return currentTarget;
    }
}
