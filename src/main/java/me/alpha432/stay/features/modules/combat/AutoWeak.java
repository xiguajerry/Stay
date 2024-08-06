/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.util.basement.wrapper.Wrapper;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.HoleUtil;
import me.alpha432.stay.util.player.PlayerUtil;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoWeak extends Module {
    public AutoWeak() {
        super("AutoWeak", "AutoWeak", Module.Category.COMBAT, true, false, false);
    }


    private final me.alpha432.stay.util.counting.Timer Timer = new Timer();
    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = Math.pow(range, 2.0) + 1.0;
        for (EntityPlayer player : AutoTrap.mc.world.playerEntities) {
            if (EntityUtils.isntValid(player, range) || Stay.speedManager.getPlayerSpeed(player) > 10.0)
                continue;
            if (target == null) {
                target = player;
                distance = AutoTrap.mc.player.getDistanceSq(player);
                continue;
            }
            if (!(AutoTrap.mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = AutoTrap.mc.player.getDistanceSq(player);
        }
        return target;
    }
    private boolean fuck=false;
    private int getweakIteam(){
        for (int i = 0; i <=36 ; i++) {
            if(!PotionUtils.getPotionFromItem(mc.player.inventory.getStackInSlot(i)).getRegistryName().getPath().contains("weakness")){
                continue;
            }else {
                return i;
            }

        }
       return -1;
    }


boolean bs = true;
int i=0;
    @Override
    public void onUpdate() {
        if(mc.player != null) {
            int weak=getweakIteam();
            if(weak==-1){
                return;
            }
            if(InventoryUtil.findItemInHotbar(Items.BOW) == -1){
                return;
            }


            if ( !mc.player.isHandActive()) {
                if(HoleUtil.isHole(new BlockPos( Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ))){
                    BlockPos pos2= PlayerUtil.getPlayerPos();
//                    double dist = Wrapper.getPlayer().getDistance(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D);
                    EntityPlayer pos = getTarget(2);

                    if( pos!=null &&  Wrapper.getPlayer().getDistance(pos.posX, pos.posY, pos.posZ)<=1&&mc.world.getBlockState(new BlockPos(pos2.getX(),pos2.getY(),pos2.getZ())).getBlock()== Blocks.AIR){
                        mc.playerController.windowClick(0, weak, 9, ClickType.SWAP, mc.player);

                            int of = mc.player.inventory.currentItem;
                        System.out.println( pos.getEntityData());

                        if (mc.player.getItemInUseMaxCount() >= 3) {
                            mc.player.setActiveHand(EnumHand.MAIN_HAND);
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));


                        }else if (mc.player.getItemInUseMaxCount() <=3) {
                            mc.player.stopActiveHand();
                        }



//                            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.cameraYaw, -90f, mc.player.onGround));
//                            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
//                           mc.playerController.onStoppedUsingItem(mc.player);



//                        mc.player.connection.sendPacket(new CPacketHeldItemChange(of));
//                            mc.playerController.windowClick(0, weak, 9, ClickType.SWAP, mc.player);



                    }



                }




            }


        }


//    @Override
//    public void onEnable() {
//        if (nullCheck())
//            return;
//        int of = mc.player.inventory.currentItem;
////        mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtil.findItemInHotbar(Items.BOW)));
////        mc.player.stopActiveHand();;
//
//
//            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
//
//
//
//        Timer.passedS(2);
//        System.out.println( Timer.passedS(2));
//if(mc.player.getItemInUseMaxCount()>3){
//    mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.cameraYaw, -90f, mc.player.onGround));
//    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
//    mc.playerController.onStoppedUsingItem(mc.player);
//}
//          System.out.println(mc.player.getItemInUseMaxCount());
//
//
//
//
//
//
//
//
//
//
////        mc.player.connection.sendPacket(new CPacketHeldItemChange(of));
//        return;
//    }


}
}











