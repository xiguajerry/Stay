/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules;

import anonymous.team.eventsystem.impl.ClientChangeEvent;
import com.mojang.realmsclient.gui.ChatFormatting;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.ClientEvent;
import me.alpha432.stay.event.Render2DEvent;
import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.Feature;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.client.HUD;
import me.alpha432.stay.features.setting.Bind;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.loader.ForgeEntry;
import me.alpha432.stay.manager.NotificationType;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Module
        extends Feature {
    private final String description;
    private final Category category;
    public Setting<Boolean> enabled = this.register(new Setting<>("Enabled", false));
    public Setting<Boolean> drawn = this.register(new Setting<>("Drawn", true));
    public Setting<Bind> bind = this.register(new Setting<>("Keybind", new Bind(-1)));
    public Setting<String> displayName;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset = 0.0f;
    public float arrayListVOffset = 0.0f;
    public float offset;
    public float vOffset;
    public boolean sliding;

    private final List<Function0<kotlin.Unit>> onEnableListener = new CopyOnWriteArrayList<>();
    private final List<Function0<kotlin.Unit>> onDisableListener = new CopyOnWriteArrayList<>();

    private final List<Function0<kotlin.Unit>> onTickListener = new CopyOnWriteArrayList<>();
    private final List<Function1<TickEvent.RenderTickEvent, kotlin.Unit>> onRenderTickListener = new CopyOnWriteArrayList<>();
    private final List<Function0<kotlin.Unit>> onUpdateListener = new CopyOnWriteArrayList<>();
    private final List<Function1<Render3DEvent, kotlin.Unit>> onRender3DListener = new CopyOnWriteArrayList<>();
    private final List<Function1<Render2DEvent, kotlin.Unit>> onRender2DListener = new CopyOnWriteArrayList<>();

    public Module(String name, String description, Category category, boolean hasListener, boolean hidden, boolean alwaysListening) {
        super(name);
        this.displayName = this.register(new Setting<>("DisplayName", name));
        this.description = description;
        this.category = category;
        this.hasListener = hasListener;
        this.hidden = hidden;
        this.alwaysListening = alwaysListening;
    }

    public boolean isSliding() {
        return this.sliding;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public final void onEnable(Function0<kotlin.Unit> blockFunction) {
        onEnableListener.add(blockFunction);
    }

    public final void onDisable(Function0<Unit> blockFunction) {
        onDisableListener.add(blockFunction);
    }

    private void onEnable0() {
        onEnableListener.forEach(Function0::invoke);
    }

    private void onDisable0() {
        onDisableListener.forEach(Function0::invoke);
    }

    public void onToggle() {
    }

    public void onLoad() {
    }

    public void onTick() {
    }

    public final void onTick(Function0<kotlin.Unit> func) {
        onTickListener.add(func);
    }

    public final void onTick0() {
        onTickListener.forEach(Function0::invoke);
    }

    public final void onRenderTick(Function1<TickEvent.RenderTickEvent, kotlin.Unit> func) {
        onRenderTickListener.add(func);
    }

    public final void onRenderTick0(TickEvent.RenderTickEvent event) {
        onRenderTickListener.forEach(func -> func.invoke(event));
    }

    public void onLogin() {
    }

    public void onLogout() {
    }

    public void onUpdate() {
    }

    public final void onUpdate(Function0<kotlin.Unit> func) {
        onUpdateListener.add(func);
    }

    public final void onUpdate0() {
        onUpdateListener.forEach(Function0::invoke);
    }

    public void onRender2D(Render2DEvent event) {
    }

    public final void onRender2D(Function1<Render2DEvent, kotlin.Unit> func) {
        onRender2DListener.add(func);
    }

    public final void onRender2D0(Render2DEvent event) {
        this.onRender2DListener.forEach(func -> func.invoke(event));
    }

    public void onRender3D(Render3DEvent event) {
    }

    public final void onRender3D(Function1<Render3DEvent, kotlin.Unit> func) {
        this.onRender3DListener.add(func);
    }

    public final void onRender3D0(Render3DEvent event) {
        this.onRender3DListener.forEach(func -> func.invoke(event));
    }

    public void onUnload() {
    }

    public String getDisplayInfo() {
        return null;
    }

    public boolean isOn() {
        return this.enabled.getValue();
    }

    public boolean isOff() {
        return this.enabled.getValue() == false;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enable();
        } else {
            this.disable();
        }
    }

    public final void enable() {
        this.enabled.setValue(Boolean.TRUE);
        this.onToggle();
        this.onEnable();
        this.onEnable0();
        if (HUD.getInstance().notifyToggles.getValue().booleanValue()) {
            TextComponentString text = new TextComponentString(Stay.commandManager.getClientMessage() + " " + ChatFormatting.GREEN + this.getDisplayName() + " toggled on.");
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
        }
        assert Stay.notificationManager != null;
        Stay.notificationManager.push(this.getDisplayName() + " toggled on.", NotificationType.INFO, 1000);
        this.subscribe();
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            ForgeEntry.register(this);
        }
    }

    public final void disable() {
        this.unsubscribe();
        if (this.hasListener && !this.alwaysListening) {
            ForgeEntry.unregister(this);
        }
        this.enabled.setValue(false);
        if (HUD.getInstance().notifyToggles.getValue().booleanValue()) {
            TextComponentString text = new TextComponentString(Stay.commandManager.getClientMessage() + " " + ChatFormatting.RED + this.getDisplayName() + " toggled off.");
            Module.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(text, 1);
        }
        assert Stay.notificationManager != null;
        Stay.notificationManager.push(this.getDisplayName() + " toggled off.", NotificationType.WARNING, 1000);
        this.onToggle();
        this.onDisable();
        this.onDisable0();
    }

    public void toggle() {
        ClientEvent event = new ClientEvent(!this.isEnabled() ? 1 : 0, this);
        ClientChangeEvent.ModuleToggle moduleToggle = new ClientChangeEvent.ModuleToggle(this, this.isEnabled());
        moduleToggle.post();
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled() && !moduleToggle.getCancelled()) {
            this.setEnabled(!this.isEnabled());
        }
    }

    public String getDisplayName() {
        return this.displayName.getValue();
    }

    public void setDisplayName(String name) {
        Module module = Stay.moduleManager.getModuleByDisplayName(name);
        Module originalModule = Stay.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage(ChatFormatting.RED + "A module of this name already exists.");
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isDrawn() {
        return this.drawn.getValue();
    }

    public void setDrawn(boolean drawn) {
        this.drawn.setValue(drawn);
    }

    public Category getCategory() {
        return this.category;
    }

    public String getInfo() {
        return null;
    }

    public Bind getBind() {
        return this.bind.getValue();
    }

    public void setBind(int key) {
        this.bind.setValue(new Bind(key));
    }

    public boolean listening() {
        return this.hasListener && this.isOn() || this.alwaysListening;
    }

    public String getFullArrayString() {
        return this.getDisplayName() + ChatFormatting.GRAY + (this.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
    }

    public enum Category {
        COMBAT("Combat"),
        MISC("Misc"),
        VISUAL("Visual"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        CLIENT("Global"),
        UNSTABLE("Unstable");
        
        private final String name;

        Category(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

