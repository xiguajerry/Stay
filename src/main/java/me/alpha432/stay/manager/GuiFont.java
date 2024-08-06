/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.manager;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.util.interfaces.Globals;
import me.alpha432.stay.features.gui.font.CustomFont;
import me.alpha432.stay.util.graphics.color.Rainbow;



import java.awt.*;
import java.io.IOException;
import java.util.Locale;

public class GuiFont implements Globals {

    private final String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH);
    public String fontName = "Tahoma";
    public int fontSize = 16;

    private CustomFont font = new CustomFont(Font.createFont(Font.TRUETYPE_FONT, Stay.class.getResourceAsStream("/assets/cuican/LexendDeca-Regular.ttf")), true, false);

    public GuiFont() throws IOException, FontFormatException {
    }

    public void setFont() {
        this.font = new CustomFont(new Font(fontName, Font.PLAIN, fontSize), true, false);
    }

    public void reset() {
        this.setFont("Tahoma");
        this.setFontSize(16);
        this.setFont();
    }

    public boolean setFont(String fontName) {
        for (String font : fonts) {
            if (fontName.equalsIgnoreCase(font)) {
                this.fontName = font;
                this.setFont();
                return true;
            }
        }
        return false;
    }

    public void setFontSize(int size) {
        this.fontSize = size;
        this.setFont();
    }



    public void drawStringWithShadow(String string, float x, float y, int colour) {
        this.drawString(string, x, y, colour, true);
    }

    public float drawString(String string, float x, float y, int colour, boolean shadow) {
        if (shadow) {
            return this.font.drawStringWithShadow(string, x, y, colour);
        } else {
            return this.font.drawString(string, x, y, colour);
        }
    }

    public void drawStringRainbow(String string, float x, float y, boolean shadow) {
        if (shadow) {
            this.font.drawStringWithShadow(string, x, y, Rainbow.getColour().getRGB());
        } else {
            this.font.drawString(string, x, y, Rainbow.getColour().getRGB());
        }
    }

    public int getTextHeight() {
        return this.font.getStringHeight("");
    }

    public int getTextWidth(String string) {
        return this.font.getStringWidth(string);
    }

}
