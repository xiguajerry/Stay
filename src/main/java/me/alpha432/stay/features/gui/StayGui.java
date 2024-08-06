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
import me.alpha432.stay.features.Feature;
import me.alpha432.stay.features.gui.components.Component;
import me.alpha432.stay.features.gui.components.items.Item;
import me.alpha432.stay.features.gui.components.items.buttons.Button;
import me.alpha432.stay.features.gui.components.items.buttons.ModuleButton;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.client.ClickGui;
import me.alpha432.stay.util.graphics.animations.AnimationFlag;
import me.alpha432.stay.util.graphics.animations.Easing;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class StayGui
        extends GuiScreen {
    private static StayGui INSTANCE;

    private AnimationFlag animationFlag = new AnimationFlag(Easing.OUT_QUINT, ClickGui.getInstance().fadingLength.getValue());

    static {
        INSTANCE = new StayGui();
    }

    private final ArrayList<Component> components = new ArrayList<>();

    public StayGui() {
        this.setInstance();
        this.load();
    }

    public static StayGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StayGui();
        }
        return INSTANCE;
    }

    public static StayGui getClickGui() {
        return StayGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void initGui() {
        animationFlag = new AnimationFlag(Easing.OUT_CUBIC, ClickGui.getInstance().fadingLength.getValue());
        animationFlag.forceUpdate(0f, 0f);
    }

    private void load() {
        int x = -84;
        assert Stay.moduleManager != null;
        for (final Module.Category category : Stay.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), x += 95, 4, true) {
                @Override
                public void setupItems() {
                    counter1 = new int[]{1};
                    Stay.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton(new ModuleButton(module));
                        }
                    });
                }
            }.setWidth(90));
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    public void updateModule(Module module) {
        for (Component component : this.components) {
            for (Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) continue;
                ModuleButton button = (ModuleButton) item;
                Module mod = button.getModule();
                if (module == null || !module.equals(mod)) continue;
                button.initSettings();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glPushMatrix();
        double scala = animationFlag.getAndUpdate(1f);
        GL11.glTranslated(Math.cos(width * scala * 2f), Math.cos(height), Math.cos(width));
        GL11.glScaled(scala, scala, animationFlag.getAndUpdate(1f));
//        GL11.glTranslated(0, animationFlag.getAndUpdate(4f), 0);
        this.checkMouseWheel();
        this.drawDefaultBackground();
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        GL11.glPopMatrix();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Contract(pure = true)
    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public void checkMouseWheel() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
    }

    public int getTextOffset() {
        return -6;
    }

    
    public Component getComponentByName(String name) {
        for (Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) continue;
            return component;
        }
        return null;
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
    }
}

