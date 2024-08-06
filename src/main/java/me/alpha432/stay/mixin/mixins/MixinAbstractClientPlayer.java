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
import me.alpha432.stay.features.modules.render.CapesModule;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.MalformedURLException;
import java.net.URL;

import static me.alpha432.stay.util.graphics.image.DynamicTextureWrapper.getTexture;

@Mixin(value={AbstractClientPlayer.class})
public abstract class MixinAbstractClientPlayer {

    @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
    public void getLocationCape(CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        CapesModule capesModule = (CapesModule) Stay.moduleManager.getModuleByDisplayName("Capes");
        String URLS = "http://47.106.126.97/assets/cape/";
        try {
        if (capesModule.isEnabled()) {
            if(capesModule.Mode.getValue()==CapesModule.ModeS.STAY){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"capestay.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.Future){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"capefuture.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.GS1){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"capeblack.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.GS2){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"capewhite.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE1){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape1.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE2){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape2.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE3){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape3.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE4){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape4.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE5){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape5.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE6){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape6.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE7){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape7.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE8){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape8.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE9){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape9.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE10){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape10.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE11){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape11.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE12){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape12.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE13){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape13.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE14){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape14.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE15){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape15.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE16){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape16.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE17){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape17.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE18){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape18.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE19){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape19.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE20){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape20.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE21){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape21.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE22){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape22.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE23){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape23.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE24){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape24.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE25){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape25.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.CAPE26){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"cape26.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.LJM){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"ljm.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.WUT1){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"wu1.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.WUT2){
                callbackInfoReturnable.setReturnValue( getTexture(new URL(URLS+"wu2.png")));
            }
            if(capesModule.Mode.getValue()==CapesModule.ModeS.URL){
                try {
                    callbackInfoReturnable.setReturnValue(getTexture(new URL(capesModule.URL.getValue())));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }

        }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}