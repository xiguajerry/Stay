/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.gui;


import me.alpha432.stay.client.Stay;
import me.alpha432.stay.util.graphics.image.DynamicTextureWrapper;
import me.alpha432.stay.util.graphics.color.Rainbow;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CustomSplashScreen extends GuiScreen {
    private String URLS = "http://47.106.126.97/assets/minecraft/textures/";
    private ResourceLocation background;

    {
        try {
            background = DynamicTextureWrapper.getTexture(new URL(URLS + "gui.jpg"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private final List<DonatorItem> donatorItems = new ArrayList<>();
    private int y;
    private int x;
    private float watermarkX;

    public static void drawCompleteImage(float posX, float posY, float width, float height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f(width, height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f(width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }

    public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
    }


    public void initGui() {
        mc.gameSettings.enableVsync = false;
        mc.gameSettings.limitFramerate = 200;
        this.x = this.width / 4;
        this.y = this.height / 4 + 48;
        this.watermarkX = this.width + 80;
        this.buttonList.add(new TextButton(0, this.x, this.y + 22, "Single"));
        this.buttonList.add(new TextButton(1, this.x, this.y + 44, "Online"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 66, "Setting"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 88, "Alts"));
        this.buttonList.add(new TextButton(2, this.x, this.y + 110, "Quit"));
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public void updateScreen() {
        super.updateScreen();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (CustomSplashScreen.isHovered(this.x, this.y + 22, Stay.MENU_FONT_MANAGER.getTextWidth("Single"), Stay.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        } else if (CustomSplashScreen.isHovered(this.x, this.y + 44, Stay.MENU_FONT_MANAGER.getTextWidth("Online"), Stay.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        } else if (CustomSplashScreen.isHovered(this.x, this.y + 66, Stay.MENU_FONT_MANAGER.getTextWidth("Setting"), Stay.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }  else if (CustomSplashScreen.isHovered(this.x, this.y + 88, Stay.MENU_FONT_MANAGER.getTextWidth("Alts"), Stay.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.displayGuiScreen(new GuiAccountSelector());
        } else if (CustomSplashScreen.isHovered(this.x, this.y + 110, Stay.MENU_FONT_MANAGER.getTextWidth("Quit"), Stay.MENU_FONT_MANAGER.getTextHeight(), mouseX, mouseY)) {
            this.mc.shutdown();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {
            background = new ResourceLocation("cuican/background/background.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        float xOffset = -1.0f * (((float) mouseX - (float) this.width / 2.0f) / ((float) this.width / 32.0f));
        float yOffset = -1.0f * (((float) mouseY - (float) this.height / 2.0f) / ((float) this.height / 18.0f));
        this.x = this.width / 4;
        this.y = this.height / 4 + 48;
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        this.mc.getTextureManager().bindTexture(this.background);
        CustomSplashScreen.drawCompleteImage(-16.0f + xOffset, -9.0f + yOffset, this.width + 32, this.height + 18);
        for (DonatorItem item : this.donatorItems) {
            item.updatePos();
            switch (item.getSize()) {
                case 1:
                    Stay.DONATOR_FONT_MANAGER.drawSmallStringRainbow(item.getName(), (float) item.getX(), (float) item.getY(), item.getRgb());
                    break;
                case 2:
                    Stay.DONATOR_FONT_MANAGER.drawMediumStringRainbow(item.getName(), (float) item.getX(), (float) item.getY(), item.getRgb());
                    break;
                case 3:
                    Stay.DONATOR_FONT_MANAGER.drawLargeStringRainbow(item.getName(), (float) item.getX(), (float) item.getY(), item.getRgb());
                    break;
            }

        }

        Stay.MENU_FONT_MANAGER.drawStringBig("STAY Client", (float) this.x, (float) this.y - 20, Color.white.getRGB(), true);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private static class TextButton extends GuiButton {

        public TextButton(int buttonId, int x, int y, String buttonText) {
            super(buttonId, x, y, Stay.MENU_FONT_MANAGER.getTextWidth(buttonText), Stay.MENU_FONT_MANAGER.getTextHeight(), buttonText);
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                this.enabled = true;
                this.hovered = (float) mouseX >= (float) this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
                Stay.MENU_FONT_MANAGER.drawStringWithShadow(this.displayString, (float) this.x + 1f, this.y, Color.WHITE.getRGB());
                if (this.hovered) {
                    RenderUtil.drawLine(this.x - 5f, this.y + 2 + Stay.MENU_FONT_MANAGER.getTextHeight(), this.x - 5f, this.y - 2, 2f, Rainbow.getColour().getRGB());
                }
            }
        }

        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            return this.enabled && this.visible && (float) mouseX >= (float) this.x - (float) Stay.MENU_FONT_MANAGER.getTextWidth(this.displayString) / 2.0f && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        }
    }

}
