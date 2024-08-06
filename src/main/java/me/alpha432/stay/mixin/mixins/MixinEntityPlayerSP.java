/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/12/7 上午1:32
 */

/*
 * Decompiled with CFR 0.151.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.entity.MoverType
 *  net.minecraft.stats.RecipeBook
 *  net.minecraft.stats.StatisticsManager
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 */
package me.alpha432.stay.mixin.mixins;


import anonymous.team.eventsystem.impl.PlayerMoveEvent;
import me.alpha432.stay.event.*;
import me.alpha432.stay.features.modules.movement.Sprint;
import me.alpha432.stay.util.basement.wrapper.Util;
import me.alpha432.stay.util.basement.wrapper.Wrapper;
import me.alpha432.stay.util.math.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.MoverType;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.alpha432.stay.util.basement.wrapper.MinecraftInstance.mc;

@Mixin(value={EntityPlayerSP.class}, priority=0x7FFFFFFF)
public abstract class MixinEntityPlayerSP
        extends AbstractClientPlayer {
    public MixinEntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
    }

    @Inject(method={"sendChatMessage"}, at=@At(value="HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        ChatEvent chatEvent = new ChatEvent(message);
        MinecraftForge.EVENT_BUS.post((Event)chatEvent);
    }
    
    private double cachedX;
    private double cachedY;
    private double cachedZ;
    private float cachedRotationPitch;
    private float cachedRotationYaw;
    private boolean cachedMoving;
    private boolean cachedOnGround;
    
    @Inject(method={"onUpdateWalkingPlayer"}, at=@At(value="HEAD"), cancellable=true)
    private void preMotion(CallbackInfo ci) {
        MotionUpdateEvent event = new MotionUpdateEvent(0);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) return;
        ci.cancel();
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at=@At(value="RETURN"), cancellable=true)
    private void postMotion(CallbackInfo ci) {
        MotionUpdateEvent event = new MotionUpdateEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) return;
        ci.cancel();
    }
    

    
    @Inject(method={"pushOutOfBlocks"}, at=@At(value="HEAD"), cancellable=true)
    private void pushOutOfBlocksHook(double x, double y, double z, CallbackInfoReturnable<Boolean> ci) {
        PushEvent event = new PushEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) return;
        ci.setReturnValue(false);
    }

    @Redirect(method={"onLivingUpdate"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/entity/EntityPlayerSP;setSprinting(Z)V", ordinal=2))
    public void onLivingUpdate(EntityPlayerSP entityPlayerSP, boolean sprinting) {
        if (Sprint.getInstance().isOn() && Sprint.getInstance().mode.getValue() == Sprint.Mode.RAGE ? Util.mc.player.moveForward != 0.0f || Util.mc.player.moveStrafing != 0.0f : Util.mc.player.movementInput.moveStrafe != 0.0f) {
            entityPlayerSP.setSprinting(true);
            return;
        }
        entityPlayerSP.setSprinting(sprinting);
    }

    @Inject(method={"move"}, at=@At(value="HEAD"), cancellable=true)
    public void move(MoverType type, double n, double n2, double n3, CallbackInfo ci) {
        MoveEvent event = new MoveEvent( type, n, n2, n3);
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) return;
        super.move(type, event.getX(), event.getY(), event.getZ());
        ci.cancel();
    
        EntityPlayerSP player = Wrapper.getPlayer();
        if (player == null) return;
    
        PlayerMoveEvent.Pre event0 = new PlayerMoveEvent.Pre(player);
    
        if (event0.getCancelled()) ci.cancel();
        
        if (event0.isModified()) {
            super.move(type, event0.getX(), event0.getY(), event0.getZ());
            ci.cancel();
        }
    }
}

