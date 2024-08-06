/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午8:17
 */

package me.alpha432.stay.util.basement.verification;

import me.alpha432.stay.util.basement.NoStackTraceThrowable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class FrameUtil {

    public static void Display() {
        Frame frame = new Frame();
        frame.setVisible(false);
        throw new NoStackTraceThrowable("Verify HWID Failed!");
    }

    public static class Frame extends JFrame {

        public Frame() {
            this.setTitle("Verify Failed");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
            copyToClipboard(HWIDUtil.getEncryptedHWID(Verificator.KEY));
            String message = "You are not allowed to use this" + "\n" + "HWID: " + HWIDUtil.getEncryptedHWID(Verificator.KEY) + "\n(Copied to clipboard)";
            JOptionPane.showMessageDialog(this, message, "[STAY] Verify Failed", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"));
            try {
                Runtime.getRuntime().exec("cmd.exe /c taskkill /F /PID " + ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        public static void copyToClipboard(String s) {
            StringSelection selection = new StringSelection(s);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }
}
