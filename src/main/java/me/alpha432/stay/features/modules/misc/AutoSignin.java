/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;


import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.basement.wrapper.Util;
import net.minecraft.network.play.client.CPacketChatMessage;

public class AutoSignin extends Module {
    public AutoSignin() {
        super("AutoSignin", "Automatically send specified information", Module.Category.MISC, true, false, false);

    }

    public Setting<String> ID = register(new Setting<>("ID", "you id"));
    public Setting<String> custom = register(new Setting<>("password", "you password"));
    public Setting<String> ip = register(new Setting<>("IP", "serverIP"));
    private Boolean six=false;
    @Override
    public void onEnable() {
        if (nullCheck())
            return;

        disable();


        if(mc.player.getName().equals(ID.getValue())){
            if(!custom.getValue().equals("/l you password")&&!custom.getValue().equals("")){
               if( mc.getCurrentServerData().serverIP.equals(ip.getValue())){

                   Util.mc.player.connection.sendPacket(new CPacketChatMessage("/l "+custom.getValue()));
               }else {
                   Command.sendMessage("Incorrect IP address");
               }

            }else {
                Command.sendMessage("Please enter your password");
            }

        }else {
            Command.sendMessage("The set ID does not match");

        }
    }



}



