/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.misc.InstantMine;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.inventory.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Iterator;
import java.util.Objects;

public class AutoCity extends Module {
    public AutoCity() {
        super("AutoCity", "AutoCity", Category.COMBAT, false, false, false);
    }
    private final Setting<Boolean> disable = register(new Setting<>("AutoDisable", true));

    public EntityPlayer target;
    @Override
    public void onTick() {
        if (fullNullCheck()) {
            return;
        }
        if(Objects.requireNonNull(ModuleManager.getModuleByName("AutoCev")).isEnabled()) return;
        if(disable.getValue()){
            disable();
        }
        if (InventoryUtil.getItemHotbar(Items.DIAMOND_PICKAXE) == -1) {
            return;
        }
        this.target = this.getTarget(6.0);
        this.surroundMine();
    }

    @Override
    public String getDisplayInfo() {
        if (this.target == null) return null;
        return this.target.getName();
    }

    private void surroundMine() {
        if (this.target == null) return;
        Vec3d a = this.target.getPositionVector();
        if (EntityUtils.getSurroundWeakness(a, 1, -1)) {
            this.surroundMine(a, -1.0, 0.0, 0.0);
            return;
        }
        if (EntityUtils.getSurroundWeakness(a, 2, -1)) {
            this.surroundMine(a, 1.0, 0.0, 0.0);
            return;
        }
        if (EntityUtils.getSurroundWeakness(a, 3, -1)) {
            this.surroundMine(a, 0.0, 0.0, -1.0);
            return;
        }
        if (EntityUtils.getSurroundWeakness(a, 4, -1)) {
            this.surroundMine(a, 0.0, 0.0, 1.0);
            return;
        }
        if (EntityUtils.getSurroundWeakness(a, 5, -1)) {
            this.surroundMine(a, -1.0, 0.0, 0.0);
            return;
        }
        if (EntityUtils.getSurroundWeakness(a, 6, -1)) {
            this.surroundMine(a, 1.0, 0.0, 0.0);
            return;
        }
        if (EntityUtils.getSurroundWeakness(a, 7, -1)) {
            this.surroundMine(a, 0.0, 0.0, -1.0);
            return;
        }
        if (!EntityUtils.getSurroundWeakness(a, 8, -1)) return;
        this.surroundMine(a, 0.0, 0.0, 1.0);
    }

    private void surroundMine(Vec3d pos, double x, double y, double z) {
        BlockPos position = new BlockPos(pos).add(x, y, z);
        if (InstantMine.getInstance().isOff()) {
            InstantMine.getInstance().enable();
            return;
        }
        if (!InstantMine.getInstance().isOn()) return;
        if(InstantMine.breakPos!=null){
            if(InstantMine.breakPos.equals(position)){
                return;
            }
            if(InstantMine.breakPos.equals(new BlockPos(target.posX,target.posY,target.posZ))&&mc.world.getBlockState(new BlockPos(target.posX,target.posY,target.posZ)).getBlock()!=Blocks.AIR){
                return;
            }
        }

       mc.playerController.onPlayerDamageBlock(position, BlockUtil.getRayTraceFacing(position));
    }

    private EntityPlayer getTarget(double range) {
        EntityPlayer target = null;
        double distance = range;
        Iterator<EntityPlayer> iterator = mc.world.playerEntities.iterator();
        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer)iterator.next();
            if (EntityUtils.isntValid(player, range) || !EntityUtils.isInHole((Entity)player)) continue;
            if (target == null) {
                target = player;
                distance = mc.player.getDistanceSq(player);
                continue;
            }
            if (!(mc.player.getDistanceSq(player) < distance)) continue;
            target = player;
            distance = mc.player.getDistanceSq(player);
        }
        return target;
    }

}
