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
import me.alpha432.stay.event.BlockEvent;
import me.alpha432.stay.event.PlayerDamageBlockEvent;
import me.alpha432.stay.event.ProcessRightClickBlockEvent;
import me.alpha432.stay.features.modules.misc.PacketEat;
import me.alpha432.stay.features.modules.player.TpsSync;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {PlayerControllerMP.class})
public abstract class MixinPlayerControllerMP {
    @Redirect(method = {"onPlayerDamageBlock"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/state/IBlockState;getPlayerRelativeBlockHardness(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)F"))
    public float getPlayerRelativeBlockHardnessHook(IBlockState state, EntityPlayer player, World worldIn, BlockPos pos) {
        return state.getPlayerRelativeBlockHardness(player, worldIn, pos) * (TpsSync.getInstance().isOn() && TpsSync.getInstance().mining.getValue() != false ? 1.0f / Stay.serverManager.getTpsFactor() : 1.0f);
    }
    @Inject(method={"onPlayerDamageBlock"}, at=@At(value="HEAD"), cancellable=true)
    private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> ci) {
        PlayerDamageBlockEvent event = new PlayerDamageBlockEvent(0, pos, face);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }

    @Inject(method = {"clickBlock"}, at = @At(value = "HEAD"), cancellable = true)
    private void clickBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
        BlockEvent event = new BlockEvent(3, pos, face);
        event.post();
    }

//    @Inject(method = "onPlayerDamageBlock(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", at = @At("HEAD"), cancellable = true)
//    private void onPlayerDamageBlock1(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
//        DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
//        MinecraftForge.EVENT_BUS.post(event);
//        if (event.isCanceled()) {
//            callbackInfoReturnable.setReturnValue(false);
//        }
//    }
    @Shadow
    public abstract void syncCurrentPlayItem();

//    @Inject(method = {"onPlayerDamageBlock"}, at = {@At(value = "HEAD")}, cancellable = true)
//    private void onPlayerDamageBlockHook(BlockPos pos, EnumFacing face, CallbackInfoReturnable<Boolean> info) {
//        BlockEvent event = new BlockEvent(0, pos, face);
//        MinecraftForge.EVENT_BUS.post(event);
//    }

    @Inject(method = {"processRightClickBlock"}, at = @At(value = "HEAD"), cancellable = true)
    public void processRightClickBlock(EntityPlayerSP player, WorldClient worldIn, BlockPos pos, EnumFacing direction, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> cir) {
        ProcessRightClickBlockEvent event = new ProcessRightClickBlockEvent(pos, hand, Minecraft.instance.player.getHeldItem(hand));
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            cir.cancel();
        }
    }
    @Inject(method={"onStoppedUsingItem"}, at=@At(value="HEAD"), cancellable=true)
    private void onStoppedUsingItem(EntityPlayer playerIn, CallbackInfo ci) {
        if (!PacketEat.getInstance().isOn()) return;
        if (!(playerIn.getHeldItem(playerIn.getActiveHand()).getItem() instanceof ItemFood)) return;
        this.syncCurrentPlayItem();
        playerIn.stopActiveHand();
        ci.cancel();
    }
}

