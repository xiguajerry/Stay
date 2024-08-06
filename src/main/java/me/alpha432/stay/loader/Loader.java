/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.loader;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class Loader {

    public static void load() throws Exception {

//        Socket socket = new Socket("47.106.126.97", 6668);
//        DataInputStream input = new DataInputStream(socket.getInputStream());
//        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
//
        String hardwareID = HWIDUtil.getEncryptedHWID("cuicanIsYourDaddy");
//
//        output.writeUTF("[Check]-" + hardwareID);
//        String result = input.readUTF();
// TODO: 2021/11/9
        String result = "[Passed]";
        if (result.equals("[NotPass]")) {
            copyToClipboard(hardwareID);

            new DialogueWindow(
                    "Not passed verification!",
                    "You are not allowed to use Stay Client!" + "\n" +
                            "HWID(Copied) : " + hardwareID
            ).setVisible(false);
        } else if (!result.equals("[Passed]")) {
            new DialogueWindow(
                    "Time out",
                    "Can't connect to the server.Please try again a later!"
            ).setVisible(false);
        }

        if (result.equals("[Passed]")) {
            ForgeEntry.shouldLoad = true;
//             TODO: 2021/11/23
//            ClassHelper.inject(new BufferedInputStream(input));
        }


//        String hardwareID = HWIDUtil.getEncryptedHWID("NobleSixIsYourDaddy");
//        if (!VerifyManager.getHWIDList().contains(hardwareID)) {
//            copyToClipboard(hardwareID);
//            new DialogueWindow(
//                    "Not passed verification!",
//                    "You are not allowed to use Stay Client!" + "\n" +
//                            "HWID(Copied) : " + hardwareID
//            ).setVisible(true);
//        } else {
//            ClassHelper.inject(new BufferedInputStream(new URL("http://file.hyperlethal.com:39999/client.jar").openStream()));
//            ForgeEntry.shouldLoad = true;
//        }
    }

    private static void copyToClipboard(String s) {
        StringSelection selection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

}
