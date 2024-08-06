
/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import java.util.Comparator;
import java.util.HashMap;

import me.alpha432.stay.client.Stay;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockInteractionHelper;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.inventory.InventoryUtil;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class Anti32k
        extends Module {
    private static final Anti32k INSTANCE = new Anti32k();
    private final Setting<Integer> range = this.register(new Setting<>("Range", 5, 3, 8));
    private final Setting<Boolean> packetMine = this.register(new Setting<>("PacketMine", false));
    public static BlockPos min = null;
    public Anti32k() {
        super("Anti32k", "Anti32k", Category.COMBAT, true, false, false);

    }
    int oldSlot = -1;
    int shulkerInt;
    HashMap<BlockPos, Integer> openedShulker = new HashMap<>();
    public void onDisable() {
        this.oldSlot = -1;
        this.shulkerInt = 0;
        this.openedShulker.clear();
    }


    @Override
    public void onTick() {
        if (Anti32k.fullNullCheck()) {
            return;
        }
        Auto32k Auto32k = (Auto32k) Stay.moduleManager.getModuleByDisplayName("AutoXin32k");

        BlockPos hopperPos = BlockInteractionHelper.getSphere(EntityUtils.getLocalPlayerPosFloored(), (float) this.range.getValue(), this.range.getValue(), false, true, 0).stream().filter((e) -> mc.world.getBlockState(e).getBlock() instanceof BlockHopper && mc.world.getBlockState(new BlockPos(e.getX(),e.getY()+1,e.getZ())).getBlock() instanceof BlockShulkerBox&&!e.equals(me.alpha432.stay.features.modules.combat.Auto32k.placeTarget)).min(Comparator.comparing((e) -> {
            return mc.player.getDistanceSq(e);
        })).orElse(null);
        int slot = InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) ;
        if (slot != -1&&hopperPos!=null) {

                if (mc.player.getDistance(hopperPos.getX(), hopperPos.getY(), hopperPos.getZ()) > (double) this.range.getValue()) {
                    return;
                }

                if (mc.player.inventory.currentItem != slot) {
                    this.oldSlot = mc.player.inventory.currentItem;
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
                }

                if (this.packetMine.getValue()) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, hopperPos, EnumFacing.UP));
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, hopperPos, EnumFacing.UP));
                } else {
                    mc.playerController.onPlayerDamageBlock(hopperPos, EnumFacing.UP);
                    mc.playerController.onPlayerDestroyBlock(hopperPos);
                }

                mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
                if (this.oldSlot != -1) {
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(this.oldSlot));
                }
        }
    }
}

