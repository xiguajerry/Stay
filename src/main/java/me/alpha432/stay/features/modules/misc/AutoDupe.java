/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.misc;


import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.combat.Auto32k;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.world.BlockUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class AutoDupe extends Module {
    public AutoDupe() {
        super("AutoDupe","Automatically places Shulker", Category.MISC, true,false,false);
    }

    public Setting<Boolean> packet = register(new Setting<>("Packet", false));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", false));
    private final Setting<Integer> delay = register(new Setting<>("Delay", 0, 0, 2000));
    private final Timer timer = new Timer();



    private BlockPos pos;
    int Im;

    @Override
    public void onEnable() {

    }
    @Override
    public void onDisable() {

    }

    @Override
    public void onUpdate() {
        if(InstantMine.breakPos==null){
            return;
        }
        pos = InstantMine.breakPos;
        IBlockState blockState = mc.world.getBlockState(pos);
        Im = getItemShulkerBox();
        if(blockState.getBlock()== Blocks.AIR&&Im!=-1){
            mc.player.inventory.currentItem = Im;
            BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue(), false);
            timer.passedDms(delay.getValue());
        }
    }

    public int getItemShulkerBox(){
        int fus = -1;
        for (int x = 0; x <= 8; ++x) {
            Item item = Auto32k.mc.player.inventory.getStackInSlot(x).getItem();
            if (item instanceof ItemShulkerBox) {
                fus= x;
            }
        }
        return fus;
    }

}





