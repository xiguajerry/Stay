/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/7 上午1:32
 */

package me.alpha432.stay.mixin.mixins;

import me.alpha432.stay.event.IMixinHelper;
import me.alpha432.stay.features.modules.movement.PlayerTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author linustouchtips
 * @since 01/26/2021
 */

@Mixin(value = MovementInputFromOptions.class, priority = 10001)
public class MixinMovementInputFromOptions extends MovementInput implements IMixinHelper {


    @Redirect(method={"updatePlayerMoveState"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"))
    public boolean isKeyPressed(KeyBinding keyBinding) {
        int keyCode = keyBinding.getKeyCode();
        if (keyCode <= 0) return keyBinding.isKeyDown();
        if (keyCode >= 256) return keyBinding.isKeyDown();
        if (!PlayerTweaks.getInstance().isOn()) return keyBinding.isKeyDown();
        if (PlayerTweaks.getInstance().guiMove.getValue() == false) return keyBinding.isKeyDown();
        if (Minecraft.getMinecraft().currentScreen == null) return keyBinding.isKeyDown();
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) return keyBinding.isKeyDown();
        return Keyboard.isKeyDown((int)keyCode);
    }
}
