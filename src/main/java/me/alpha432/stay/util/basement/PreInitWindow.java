/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/9 下午10:42
 */

package me.alpha432.stay.util.basement;

import net.minecraft.client.Minecraft;

import javax.swing.*;

public class PreInitWindow extends JFrame {
    public PreInitWindow() {
        setTitle("Initializing AntiLeak");
        setSize(400, 300);
        //JOptionPane.showConfirmDialog(null, Minecraft.getMinecraft().gameDir);
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
    }
}
