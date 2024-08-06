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

public class DialogueWindow extends JFrame {

    public DialogueWindow(String title, String message) {
        this.setTitle(title);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"));
    }

}