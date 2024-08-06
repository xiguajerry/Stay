
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
import me.alpha432.stay.event.RenderItemEvent;
import me.alpha432.stay.event.TransformSideFirstPersonEvent;
import me.alpha432.stay.features.modules.render.NoRender;
import me.alpha432.stay.features.modules.render.SmallShield;
import me.alpha432.stay.features.modules.render.ViewModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    private boolean injection = true;

    @Shadow
    public abstract void renderItemInFirstPerson(AbstractClientPlayer var1, float var2, float var3, EnumHand var4, float var5, ItemStack var6, float var7);

    @Inject(method={"renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V"}, at=@At(value="HEAD"), cancellable=true)
    public void renderItemInFirstPersonHook(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_, CallbackInfo info) {
        if (this.injection) {
            info.cancel();
            SmallShield offset = SmallShield.getINSTANCE();
            float xOffset = 0.0f;
            float yOffset = 0.0f;
            this.injection = false;
            if (hand == EnumHand.MAIN_HAND) {
                if (offset.isOn() && player.getHeldItemMainhand() != ItemStack.EMPTY) {
                    xOffset = offset.mainX.getValue();
                    yOffset = offset.mainY.getValue();
                }
            } else if (!offset.normalOffset.getValue() && offset.isOn() && player.getHeldItemOffhand() != ItemStack.EMPTY) {
                xOffset = offset.offX.getValue().floatValue();
                yOffset = offset.offY.getValue().floatValue();
            }
            this.renderItemInFirstPerson(player, p_187457_2_, p_187457_3_, hand, p_187457_5_ + xOffset, stack, p_187457_7_ + yOffset);
            this.injection = true;
        }
    }

    @Redirect(method={"renderArmFirstPerson"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal=0))
    public void translateHook(float x, float y, float z) {
        SmallShield offset = SmallShield.getINSTANCE();
        boolean shiftPos = Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().player.getHeldItemMainhand() != ItemStack.EMPTY && offset.isOn();
        GlStateManager.translate((float)(x + (shiftPos ? offset.mainX.getValue().floatValue() : 0.0f)), (float)(y + (shiftPos ? offset.mainY.getValue().floatValue() : 0.0f)), (float)z);
    }

    @Inject(method={"renderFireInFirstPerson"}, at=@At(value="HEAD"), cancellable=true)
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (NoRender.getInstance().isOn() && NoRender.getInstance().fire.getValue().booleanValue()) {
            info.cancel();
        }
    }

    @Inject(method = "transformEatFirstPerson", at = @At("HEAD"), cancellable = true)
    public void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo callbackInfo) {
        TransformSideFirstPersonEvent event = new TransformSideFirstPersonEvent(hand);
        MinecraftForge.EVENT_BUS.post(event);
        ViewModel viewModel = (ViewModel) Stay.moduleManager.getModuleByDisplayName("ViewModel");

        if (viewModel.isEnabled() && viewModel.noEat.getValue()) {
            callbackInfo.cancel();
        }
    }


    @Redirect(method = "renderItemInFirstPerson(Lnet/minecraft/client/entity/AbstractClientPlayer;FFLnet/minecraft/util/EnumHand;FLnet/minecraft/item/ItemStack;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemRenderer;transformSideFirstPerson(Lnet/minecraft/util/EnumHandSide;F)V"))
    public void transformRedirect(ItemRenderer renderer, EnumHandSide hand, float y) {
        RenderItemEvent event = new RenderItemEvent(0.56F, -0.52F + y * -0.6F, -0.72F,
                -0.56F, -0.52F + y * -0.6F, -0.72F,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                1.0, 1.0, 1.0,
                1.0, 1.0, 1.0
        );
        MinecraftForge.EVENT_BUS.post(event);
        ViewModel viewModel = (ViewModel) Stay.moduleManager.getModuleByDisplayName("ViewModel");

        if (viewModel.isEnabled()) {
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.translate(viewModel.mainX.getValue(), viewModel.mainY.getValue(), viewModel.mainZ.getValue());
                GlStateManager.scale( viewModel.mainScaleX.getValue(), viewModel.mainScaleY.getValue(), viewModel.mainScaleZ.getValue());
                GlStateManager.rotate(viewModel.mainRx.getValue().floatValue(),viewModel.mainRx.getValue().floatValue() ,viewModel.mainRy.getValue().floatValue(), viewModel.mainRz.getValue().floatValue());
            } else {
                GlStateManager.translate(viewModel.offX.getValue(), viewModel.offY.getValue(),viewModel.offZ.getValue());
                GlStateManager.scale( viewModel.offScaleX.getValue(),viewModel.offScaleY.getValue(), viewModel.offScaleZ.getValue());
                GlStateManager.rotate(viewModel.offRx.getValue().floatValue(), viewModel.offRx.getValue().floatValue(), viewModel.offRy.getValue().floatValue(),  viewModel.offRz.getValue().floatValue());
            }
        } else {
            if (hand == EnumHandSide.RIGHT) {
                GlStateManager.translate(0.56F, -0.52F + y * -0.6F, -0.72F);
                GlStateManager.scale(1.0, 1.0, 1.0);
                GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
            } else {
                GlStateManager.translate(-0.56F, -0.52F + y * -0.6F, -0.72F);
                GlStateManager.scale(1.0, 1.0, 1.0);
                GlStateManager.rotate(0.0f, 0.0f, 1.0f, 0.0f);
            }
        }
    }


//    @Inject(method={"renderSuffocationOverlay"}, at={@At(value="HEAD")}, cancellable=true)
//    public void renderSuffocationOverlay(CallbackInfo ci) {
//        if (NoRender.getInstance().isOn() && NoRender.getInstance().blocks.getValue().booleanValue()) {
//            ci.cancel();
//        }
//    }
}

