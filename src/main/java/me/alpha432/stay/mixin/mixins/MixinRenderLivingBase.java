/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/7 上午1:32
 */

package me.alpha432.stay.mixin.mixins;

import anonymous.team.eventsystem.impl.RenderEntityEvent;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.render.Wireframe;
import me.alpha432.stay.util.graphics.color.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase> extends Render<T> {
    @Shadow
    private static final Logger LOGGER = LogManager.getLogger();
    @Shadow
    protected ModelBase mainModel;
    @Shadow
    protected boolean renderMarker;

    float red;

    float green;

    float blue;

    protected MixinRenderLivingBase(RenderManager renderManager) {
        super(renderManager);
        this.red = 0.0F;
        this.green = 0.0F;
        this.blue = 0.0F;
    }


    /**
     * @author Fixed by SagiriXiguajerry
     * @reason DO I REALLY NEED TO WRITE SOMETHING HERE?
     */
    @Overwrite
    public void doRender(@NotNull T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(entity, RenderLivingBase.class.cast(this), partialTicks, x, y, z))) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = getSwingProgress(entity, partialTicks);
            boolean shouldSit = (entity.isRiding() && entity.getRidingEntity() != null && entity.getRidingEntity().shouldRiderSit());
            this.mainModel.isRiding = shouldSit;
            this.mainModel.isChild = entity.isChild();
            try {
                float f = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                float f1 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f2 = f1 - f;
                if (shouldSit && entity.getRidingEntity() instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase) entity.getRidingEntity();
                    f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f2 = f1 - f;
                    float f3 = MathHelper.wrapDegrees(f2);
                    if (f3 < -85.0F)
                        f3 = -85.0F;
                    if (f3 >= 85.0F)
                        f3 = 85.0F;
                    f = f1 - f3;
                    if (f3 * f3 > 2500.0F)
                        f += f3 * 0.2F;
                    f2 = f1 - f;
                }
                float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                renderLivingAt(entity, x, y, z);
                float f8 = handleRotationFloat(entity, partialTicks);
                applyRotations(entity, f8, f, partialTicks);
                float f4 = prepareScale(entity, partialTicks);
                float f5 = 0.0F;
                float f6 = 0.0F;
                if (!entity.isRiding()) {
                    f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                    f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
                    if (entity.isChild())
                        f6 *= 3.0F;
                    if (f5 > 1.0F)
                        f5 = 1.0F;
                    f2 = f1 - f;
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
                this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, f4, entity);
                if (this.renderOutlines) {
                    boolean flag1 = setScoreTeamColor(entity);
                    GlStateManager.enableColorMaterial();
                    GlStateManager.enableOutlineMode(getTeamColor(entity));
                    if (!this.renderMarker)
                        renderModel(entity, f6, f5, f8, f2, f7, f4);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator())
                        renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                    GlStateManager.disableOutlineMode();
                    GlStateManager.disableColorMaterial();
                    if (flag1)
                        unsetScoreTeamColor();
                } else {
                    if (Wireframe.getInstance().isOn() && (Wireframe.getInstance()).players.getValue() && entity instanceof EntityPlayer && (Wireframe.getInstance()).mode.getValue().equals(Wireframe.RenderMode.SOLID)) {
                        this.red = (Wireframe.getInstance()).red.getValue() / 255.0F;
                        this.green = (Wireframe.getInstance()).green.getValue() / 255.0F;
                        this.blue = (Wireframe.getInstance()).blue.getValue() / 255.0F;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glDisable(2929);
                        GL11.glDepthMask(false);
                        assert Stay.friendManager != null;
                        if (Stay.friendManager.isFriend(entity.getName()) || entity == (Minecraft.getMinecraft()).player) {
                            GL11.glColor4f(0.0F, 191.0F, 255.0F, (Wireframe.getInstance()).alpha.getValue() / 255.0F);
                        } else {
                            GL11.glColor4f((Wireframe.getInstance()).rainbow.getValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue()).getRed() / 255.0F) : this.red, (Wireframe.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue().intValue()).getGreen() / 255.0F) : this.green, (Wireframe.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue().intValue()).getBlue() / 255.0F) : this.blue, (Wireframe.getInstance()).alpha.getValue().floatValue() / 255.0F);
                        }
                        renderModel(entity, f6, f5, f8, f2, f7, f4);
                        GL11.glDisable(2896);
                        GL11.glEnable(2929);
                        GL11.glDepthMask(true);
                        if (Stay.friendManager.isFriend(entity.getName()) || entity == (Minecraft.getMinecraft()).player) {
                            GL11.glColor4f(0.0F, 191.0F, 255.0F, (Wireframe.getInstance()).alpha.getValue() / 255.0F);
                        } else {
                            GL11.glColor4f((Wireframe.getInstance()).rainbow.getValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue()).getRed() / 255.0F) : this.red, (Wireframe.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue().intValue()).getGreen() / 255.0F) : this.green, (Wireframe.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue().intValue()).getBlue() / 255.0F) : this.blue, (Wireframe.getInstance()).alpha.getValue().floatValue() / 255.0F);
                        }
                        renderModel(entity, f6, f5, f8, f2, f7, f4);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                    boolean flag1 = setDoRenderBrightness(entity, partialTicks);
                    if (!(entity instanceof EntityPlayer) || (Wireframe.getInstance().isOn() && (Wireframe.getInstance()).mode.getValue().equals(Wireframe.RenderMode.WIREFRAME) && (Wireframe.getInstance()).playerModel.getValue().booleanValue()) || Wireframe.getInstance().isOff())
                        renderModel(entity, f6, f5, f8, f2, f7, f4);
                    if (flag1)
                        unsetBrightness();
                    GlStateManager.depthMask(true);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator())
                        renderLayers(entity, f6, f5, partialTicks, f8, f2, f7, f4);
                    if (Wireframe.getInstance().isOn() && (Wireframe.getInstance()).players.getValue().booleanValue() && entity instanceof EntityPlayer && (Wireframe.getInstance()).mode.getValue().equals(Wireframe.RenderMode.WIREFRAME)) {
                        this.red = (Wireframe.getInstance()).red.getValue().intValue() / 255.0F;
                        this.green = (Wireframe.getInstance()).green.getValue().intValue() / 255.0F;
                        this.blue = (Wireframe.getInstance()).blue.getValue().intValue() / 255.0F;
                        GlStateManager.pushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glPolygonMode(1032, 6913);
                        GL11.glDisable(3553);
                        GL11.glDisable(2896);
                        GL11.glDisable(2929);
                        GL11.glEnable(2848);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        if (Stay.friendManager.isFriend(entity.getName()) || entity == (Minecraft.getMinecraft()).player) {
                            GL11.glColor4f(0.0F, 191.0F, 255.0F, (Wireframe.getInstance()).alpha.getValue().floatValue() / 255.0F);
                        } else {
                            GL11.glColor4f((Wireframe.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue().intValue()).getRed() / 255.0F) : this.red, (Wireframe.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue().intValue()).getGreen() / 255.0F) : this.green, (Wireframe.getInstance()).rainbow.getValue().booleanValue() ? (ColorUtil.rainbow((Wireframe.getInstance()).rainbowHue.getValue().intValue()).getBlue() / 255.0F) : this.blue, (Wireframe.getInstance()).alpha.getValue().floatValue() / 255.0F);
                        }
                        GL11.glLineWidth((Wireframe.getInstance()).lineWidth.getValue().floatValue());
                        renderModel(entity, f6, f5, f8, f2, f7, f4);
                        GL11.glEnable(2896);
                        GlStateManager.popAttrib();
                        GlStateManager.popMatrix();
                    }
                }
                GlStateManager.disableRescaleNormal();
            } catch (Exception var20) {
                LOGGER.error("Couldn't render entity", var20);
            }
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(entity, RenderLivingBase.class.cast(this), partialTicks, x, y, z));
        }
    }

    @Shadow
    protected abstract boolean isVisible(EntityLivingBase paramEntityLivingBase);

    @Shadow
    protected abstract float getSwingProgress(T paramT, float paramFloat);

    @Shadow
    protected abstract float interpolateRotation(float paramFloat1, float paramFloat2, float paramFloat3);

    @Shadow
    protected abstract float handleRotationFloat(T paramT, float paramFloat);

    @Shadow
    protected abstract void applyRotations(T paramT, float paramFloat1, float paramFloat2, float paramFloat3);

    @Shadow
    public abstract float prepareScale(T paramT, float paramFloat);

    @Shadow
    protected abstract void unsetScoreTeamColor();

    @Shadow
    protected abstract boolean setScoreTeamColor(T paramT);

    @Shadow
    protected abstract void renderLivingAt(T paramT, double paramDouble1, double paramDouble2, double paramDouble3);

    @Shadow
    protected abstract void unsetBrightness();

    @Shadow
    protected abstract void renderModel(T paramT, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6);

    @Shadow
    protected abstract void renderLayers(T paramT, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7);

    @Shadow
    protected abstract boolean setDoRenderBrightness(T paramT, float paramFloat);
    
    @Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", shift = At.Shift.BEFORE))
    public void renderModelHead(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {
        if (entity == null || !RenderEntityEvent.getRenderingEntities()) return;
        
        RenderEntityEvent.Model.Pre eventModel = RenderEntityEvent.Model.Pre.of(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, mainModel);
        eventModel.post();
    }
    
    @Inject(method = "renderModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", shift = At.Shift.AFTER))
    public void renderEntityReturn(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, CallbackInfo ci) {
        if (entity == null || !RenderEntityEvent.getRenderingEntities()) return;
        
        RenderEntityEvent.Model.Post eventModel = RenderEntityEvent.Model.Post.of(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, mainModel);
        eventModel.post();
    }
}
