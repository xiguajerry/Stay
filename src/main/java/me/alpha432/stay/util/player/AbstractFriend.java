/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午4:16
 */

package me.alpha432.stay.util.player;

import me.alpha432.stay.util.interfaces.IFriendable;
import me.alpha432.stay.util.interfaces.INameable;

public abstract class AbstractFriend implements INameable, IFriendable {

    private String name;
    private String alias;

    public AbstractFriend(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.name = name;
    }
}
