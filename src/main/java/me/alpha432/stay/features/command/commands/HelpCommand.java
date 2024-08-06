/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : Stay.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + Stay.commandManager.getPrefix() + command.getName());
        }
    }
}

