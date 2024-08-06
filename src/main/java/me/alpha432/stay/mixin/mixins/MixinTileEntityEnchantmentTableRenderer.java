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
import me.alpha432.stay.features.modules.render.HellgateEsp;
import me.alpha432.stay.features.modules.render.NoRender;
import me.alpha432.stay.manager.ModuleManager;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityEnchantmentTableRenderer.class)
public class MixinTileEntityEnchantmentTableRenderer {

    @Inject(method = "render*", at = @At(value = "INVOKE"), cancellable = true)
    private void renderEnchantingTableBook(TileEntityEnchantmentTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo info) {
        NoRender NoRender = (NoRender) Stay.moduleManager.getModuleByDisplayName("NoRender");
        if (ModuleManager.getModuleByName("NoRender").isEnabled() && NoRender.enchantmentTables.getValue())
            info.cancel();
    }

    @Inject(method = "render*", at = @At(value = "INVOKE"), cancellable = true)
    private void renderEnchantingTableBooks(TileEntityEnchantmentTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha, CallbackInfo info) {
        HellgateEsp HellgateEsp = (HellgateEsp) Stay.moduleManager.getModuleByDisplayName("HellgateEsp");
        if (ModuleManager.getModuleByName("HellgateEsp").isEnabled() && HellgateEsp.norem.getValue())
            info.cancel();
    }
}
