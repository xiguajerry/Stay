/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AutoQueue extends Module {

    public AutoQueue() {
        super("AutoQueue", "Automatically queue in 2b2t.xin", Category.MISC, true, false, false);

    }
    private boolean firstrun = true;
 ;
    private static Image image = Stay.image;
    private static TrayIcon trayIcon = Stay.trayIcon;

    Map<String,String>  QA = new HashMap<String, String>(){{
        put("\u9f99\u86cb","B");
        put("\u4f20\u9001","B");
        put("\u5927\u7bb1\u5b50","C");
        put("\u5c0f\u7bb1\u5b50","B");
        put("HIM","A");
        put("\u95ea\u7535\u51fb\u4e2d","B");
        put("\u5b98\u65b9\u8bd1\u540d","C");
        put("\u94bb\u77f3","C");
        put("\u706b\u7130\u5f39","B");
        put("\u5357\u74dc","B");
        put("\u4EC0\u4E48\u52A8\u7269","B");
        put("\u7f8a\u9a7c","B");
        put("\u6316\u6398","C");
        put("\u51CB\u96F6","C");
        put("\u5708\u5730","B");
        put("\u65E0\u9650\u6C34","C");
        put("\u672B\u5F71\u4E4B\u773C","A");
        put("\u7EA2\u77F3\u706B\u628A","B");
        put("\u51E0\u9875","A");
        put("\u933E","B");
        put("\u9644\u9B54\u91D1","B");
    }};

    @Override
    public void onEnable(){
        if (nullCheck()){
            return;
        }
        if (mc.getCurrentServerData().serverIP==null&&!mc.getCurrentServerData().serverIP.equals("2b2t.xin")){
            Command.sendMessage("Only support 2b2t.xin!");
            disable();
            return;
        }
        if (firstrun) {
            if (SystemTray.isSupported()) {
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Start Queueing");

            } else {
                System.err.println("System tray not supported!");
            }
            firstrun = false;
        }
    }

    @SubscribeEvent
    public void onGuiUpdate(GuiScreenEvent event){
        if (fullNullCheck()){
            return;
        }
        if (event.getGui() instanceof GuiDownloadTerrain){
            if (SystemTray.isSupported()) {
                displayTray();
                trayIcon.setToolTip("Welcome " +mc.player.getName());
                disable();
            } else {
                System.err.println("System tray not supported!");
                disable();
            }
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        if (!mc.getCurrentServerData().serverIP.equals("2b2t.xin")){
            return;
        }
        if (event.getMessage().getUnformattedText().contains("\u00A7")){
            String s=event.getMessage().getUnformattedText().substring(15,17);
            int sec;
            if (s.contains(" ")){
                sec=Integer.parseInt(s.substring(0,1));
            }else{
                sec=Integer.parseInt(s);
            }
            if (SystemTray.isSupported()){
                trayIcon.setToolTip("Queueing: "+sec);
            }
            if (sec<=2) return;
        }
        String msg=event.getMessage().getUnformattedText();
        Map.Entry<String, String> Answer = QA.entrySet().stream().filter(p -> msg.contains(p.getKey())).findFirst().orElse(null);
        if (Answer !=null){
            mc.player.connection.sendPacket(new CPacketChatMessage(Answer.getValue()));
        }
    }

    public static void displayTray() {
        trayIcon.displayMessage("Auto Queue", "\u6392\u5B8C\u961F\u8FA3"+" "+mc.player.getName(), TrayIcon.MessageType.NONE);
    }

}
