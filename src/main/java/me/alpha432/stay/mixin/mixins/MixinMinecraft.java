/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/7 上午1:32
 */

package me.alpha432.stay.mixin.mixins;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.KeyEvent;
import me.alpha432.stay.event.WorldEvent;
import me.alpha432.stay.features.gui.CustomSplashScreen;
import me.alpha432.stay.features.modules.client.ClickGui;
import me.alpha432.stay.features.modules.player.MultiTask;
import me.alpha432.stay.util.basement.wrapper.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import anonymous.team.eventsystem.impl.RunGameLoopEvent;
import anonymous.team.eventsystem.impl.TickEvent;

import javax.swing.*;

@Mixin(value = {Minecraft.class})
public abstract class MixinMinecraft {

    @Inject(method = {"shutdownMinecraftApplet"}, at = @At(value = "HEAD"))
    public void injectShutDownMA(CallbackInfo ci) {
        this.unload(false);
    }

    @Inject(method = "shutdown", at = @At(value = "HEAD"))
    public void handleShutDown(CallbackInfo ci) {
        this.unload(false);
    }

//    @Redirect(method = {"run"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
//    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
//        this.unload(true);
//    }

    @Inject(method = {"runTickKeyboard"}, at = @At(value = "INVOKE", remap = false, target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 0, shift = At.Shift.BEFORE))
    private void onKeyboard(CallbackInfo ci) {
        int i;
        int n = i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();
        if (Keyboard.getEventKeyState()) {
            KeyEvent event = new KeyEvent(i);
            MinecraftForge.EVENT_BUS.post(event);
        }
    }
    @Shadow
    public abstract void displayGuiScreen( GuiScreen var1);

    @Shadow public abstract void crashed(CrashReport crash);

    @Inject(method={"displayGuiScreen"}, at=@At(value="HEAD"))
    private void displayGuiScreen(GuiScreen screen, CallbackInfo ci) {
        assert Stay.moduleManager != null;
        ClickGui ClickGui = (ClickGui) Stay.moduleManager.getModuleByDisplayName("ClickGui");
        if (screen instanceof GuiMainMenu&& ClickGui.gui.getValue()) {
            //&& Gui.INSTANCE.customScreen.getValue()
            this.displayGuiScreen(new CustomSplashScreen());
        }
    }

    @Inject(method={"runTick()V"}, at=@At(value="RETURN"))
    private void runTick(CallbackInfo callbackInfo) {
        ClickGui ClickGui = (ClickGui) Stay.moduleManager.getModuleByDisplayName("ClickGui");
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu && ClickGui.gui.getValue()) {
            //&& Gui.INSTANCE.customScreen.getValue()
            Minecraft.getMinecraft().displayGuiScreen(new CustomSplashScreen());
        }
    }

    private void unload(boolean crash) {
        Stay.LOGGER.info("Stopping Stay...");
        Stay.onUnload();
        Stay.LOGGER.info("Stay Client stopped.");
        if (crash) {
            JOptionPane.showConfirmDialog(null, "Client Crashed! Please send this message's screenshot & game logs to Stay Dev.\nConfigs is saved.Open the TaskManager and close the game then.\nCaused by " + Minecraft.getMinecraft().crashReporter.getCrashCause(), "Crash Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    private void loadWorld(WorldClient p_loadWorld_1_, String p_loadWorld_2_, final CallbackInfo callbackInfo) {
        MinecraftForge.EVENT_BUS.post(new WorldEvent(p_loadWorld_1_));
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    public void runTick$Inject$HEAD(CallbackInfo ci) {
        TickEvent.Pre.INSTANCE.post();
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    public void runTick$Inject$RETURN(CallbackInfo ci) {
        TickEvent.Post.INSTANCE.post();
    }

    @Redirect(method = {"sendClickBlockToController"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;isHandActive()Z"))
    private boolean isHandActiveWrapper(EntityPlayerSP playerSP) {
        return !MultiTask.getInstance().isOn() && playerSP.isHandActive();
    }

    @Redirect(method = {"rightClickMouse"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerControllerMP;getIsHittingBlock()Z", ordinal = 0))
    private boolean isHittingBlockHook(PlayerControllerMP playerControllerMP) {
        return !MultiTask.getInstance().isOn() && playerControllerMP.getIsHittingBlock();
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Timer;updateTimer()V", shift = At.Shift.BEFORE))
    public void runGameLoop$Inject$INVOKE$updateTimer(CallbackInfo ci) {
        Wrapper.getMinecraft().profiler.startSection("trollRunGameLoop");
        RunGameLoopEvent.Start.INSTANCE.post();
        Wrapper.getMinecraft().profiler.endSection();
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endSection()V", ordinal = 0, shift = At.Shift.AFTER))
    public void runGameLoop$INVOKE$endSection(CallbackInfo ci) {
        Wrapper.getMinecraft().profiler.startSection("trollRunGameLoop");
        RunGameLoopEvent.Tick.INSTANCE.post();
        Wrapper.getMinecraft().profiler.endSection();
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.BEFORE))
    public void runGameLoop$Inject$INVOKE$endStartSection(CallbackInfo ci) {
        Wrapper.getMinecraft().profiler.endStartSection("trollRunGameLoop");
        RunGameLoopEvent.Render.INSTANCE.post();
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isFramerateLimitBelowMax()Z", shift = At.Shift.BEFORE))
    public void runGameLoop$Inject$INVOKE$isFramerateLimitBelowMax(CallbackInfo ci) {
        Wrapper.getMinecraft().profiler.startSection("trollRunGameLoop");
        RunGameLoopEvent.End.INSTANCE.post();
        Wrapper.getMinecraft().profiler.endSection();
    }
}

