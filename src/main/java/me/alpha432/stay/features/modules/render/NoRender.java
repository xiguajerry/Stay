/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  net.minecraft.init.MobEffects
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent
 *  net.minecraftforge.client.event.RenderBlockOverlayEvent$OverlayType
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.alpha432.stay.features.modules.render;


import me.alpha432.stay.event.NoRenderEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoRender
extends Module {

    public static NoRender INSTANCE = new NoRender();
    public Setting<Boolean> armor = this.register(new Setting<>("Armor", true));
    public Setting<Boolean> fire = this.register(new Setting<>("Frie", true));
    public Setting<Boolean> blind = this.register(new Setting<>("Blind", true));
    public Setting<Boolean> nausea = this.register(new Setting<>("Nausea", true));
    public Setting<Boolean> hurtCam = this.register(new Setting<>("HurtCam", true));
    public Setting<Boolean> enchantmentTables = this.register(new Setting<>("enchantmentTables", true));
    public NoRender() {
        super("NoRender", "No Render", Module.Category.VISUAL, true, false, false);
        this.setInstance();
    }

    public static NoRender getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new NoRender();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (this.blind.getValue().booleanValue() && NoRender.mc.player.isPotionActive(MobEffects.BLINDNESS)) {
            NoRender.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (this.nausea.getValue() == false) return;
        if (!NoRender.mc.player.isPotionActive(MobEffects.NAUSEA)) return;
        NoRender.mc.player.removePotionEffect(MobEffects.NAUSEA);
    }

    @SubscribeEvent
    public void NoRenderEventListener(NoRenderEvent event) {
        if (event.getStage() == 0 && this.armor.getValue().booleanValue()) {
            event.setCanceled(true);
            return;
        }
        if (event.getStage() != 1) return;
        if (this.hurtCam.getValue() == false) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void blockOverlayEventListener(RenderBlockOverlayEvent event) {
        if (this.fire.getValue() == false) return;
        if (event.getOverlayType() != RenderBlockOverlayEvent.OverlayType.FIRE) return;
        event.setCanceled(true);
    }
}

