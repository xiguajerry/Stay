/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;

import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoContainer extends Module {
    public NoContainer() {
        super("AntiContainer", "Do not display containers", Module.Category.PLAYER, true, false, false);
    }
    public Setting<Boolean> Chest = register(new Setting<>("Chest", true));

      public Setting<Boolean> EnderChest =  register(new Setting<>("EnderChest",true));
      public Setting<Boolean> Trapped_Chest =  register(new Setting<>("Trapped_Chest",true));
      public Setting<Boolean> Hopper =  register(new Setting<>("Hopper",true));
      public Setting<Boolean> Dispenser =  register(new Setting<>("Dispenser",true));
      public Setting<Boolean> Furnace =  register(new Setting<>("Furnace",true));
      public Setting<Boolean> Beacon =  register(new Setting<>("Beacon",true));
      public Setting<Boolean> Crafting_Table =  register(new Setting<>("Crafting_Table",true));
      public Setting<Boolean> Anvil =  register(new Setting<>("Anvil",true));
      public Setting<Boolean> Enchanting_table =  register(new Setting<>("Enchanting_table",true));
      public Setting<Boolean> Brewing_Stand =  register(new Setting<>("Brewing_Stand",true));
      public Setting<Boolean> ShulkerBox =  register(new Setting<>("ShulkerBox",true));

    @SubscribeEvent
    public void onCheck(PacketEvent.Send packet){
        if(packet.getPacket() instanceof CPacketPlayerTryUseItemOnBlock){
            BlockPos pos = ((CPacketPlayerTryUseItemOnBlock) packet.getPacket()).getPos();
            if(check(pos)) packet.setCanceled(true);
        }
    }

    public boolean check(BlockPos pos){
        return ((Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CHEST && Chest.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST && EnderChest.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.TRAPPED_CHEST && Trapped_Chest.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.HOPPER && Hopper.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.DISPENSER && Dispenser.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.FURNACE && Furnace.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BEACON && Beacon.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CRAFTING_TABLE && Crafting_Table.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ANVIL && Anvil.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE && Enchanting_table.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BREWING_STAND && Brewing_Stand.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) && ShulkerBox.getValue());
    }
}

