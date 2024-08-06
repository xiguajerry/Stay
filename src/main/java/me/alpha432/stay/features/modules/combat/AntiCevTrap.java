/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.alpha432.stay.features.modules.combat;

import java.util.Iterator;

import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.EntityUtils;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.MathUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiCevTrap extends Module {
    public Setting<Boolean> rotate = this.register(new Setting<>("Rotate", true));
    public Setting<Boolean> packet = this.register(new Setting<>("Packet", true));
    private float yaw = 0.0F;
    private float pitch = 0.0F;
    private boolean rotating = false;
    private boolean isSneaking;

    public AntiCevTrap() {
        super("AntiCev", "AntiCev", Category.COMBAT, true, false, false);
    }

    public void onTick() {
        if (!fullNullCheck()) {
            if (InventoryUtil.findHotbarBlock(BlockObsidian.class) != -1) {
                this.main();
            }
        }
    }

    public void onDisable() {
        this.rotating = false;
        this.isSneaking = EntityUtils.stopSneaking(this.isSneaking);
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getStage() == 0 && (Boolean)this.rotate.getValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            this.rotating = false;
        }

    }

    private void main() {
        Vec3d a = mc.player.getPositionVector();
        if (this.checkTrap(a, EntityUtils.getOffsets(2, false)) && this.checkTrap(a, EntityUtils.getVarOffsets(0, 2, 0)) && this.checkCrystal(a, EntityUtils.getVarOffsets(0, 3, 0)) != null) {
            ++mc.player.motionY;
            if ((Boolean)this.packet.getValue()) {
                this.rotateTo(this.checkCrystal(a, EntityUtils.getVarOffsets(0, 3, 0)));
                EntityUtils.attackEntity(this.checkCrystal(a, EntityUtils.getVarOffsets(0, 3, 0)), true);
            } else {
                this.rotateTo(this.checkCrystal(a, EntityUtils.getVarOffsets(0, 3, 0)));
                EntityUtils.attackEntity(this.checkCrystal(a, EntityUtils.getVarOffsets(0, 3, 0)), false);
            }

            this.rotateToPos(a, EntityUtils.getVarOffsets(0, 3, 0));
            this.place(a, EntityUtils.getVarOffsets(0, 3, 0));
        }

    }

    private void rotateTo(Entity entity) {
        if ((Boolean)this.rotate.getValue()) {
            float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }

    }

    private void rotateToPos(Vec3d pos, Vec3d[] list) {
        if ((Boolean)this.rotate.getValue()) {
            Vec3d[] var3 = list;
            int var4 = list.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Vec3d vec3d = var3[var5];
                BlockPos position = (new BlockPos(pos)).add(vec3d.x, vec3d.y, vec3d.z);
                float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)position.getX() + 0.5F), (double)((float)position.getY() - 0.5F), (double)((float)position.getZ() + 0.5F)));
                this.yaw = angle[0];
                this.pitch = angle[1];
                this.rotating = true;
            }
        }

    }

    private void place(Vec3d pos, Vec3d[] list) {
        Vec3d[] var3 = list;
        int var4 = list.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Vec3d vec3d = var3[var5];
            BlockPos position = (new BlockPos(pos)).add(vec3d.x, vec3d.y, vec3d.z);
            int a = mc.player.inventory.currentItem;
            mc.player.inventory.currentItem = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            mc.playerController.updateController();
            this.isSneaking = BlockUtil.placeBlock(position, EnumHand.MAIN_HAND, false, (Boolean)this.packet.getValue(), true);
            mc.player.inventory.currentItem = a;
            mc.playerController.updateController();
        }

    }

    Entity checkCrystal(Vec3d pos, Vec3d[] list) {
        Entity crystal = null;
        Vec3d[] var4 = list;
        int var5 = list.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Vec3d vec3d = var4[var6];
            BlockPos position = (new BlockPos(pos)).add(vec3d.x, vec3d.y, vec3d.z);
            Iterator var9 = mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position)).iterator();

            while(var9.hasNext()) {
                Entity entity = (Entity)var9.next();
                if (entity instanceof EntityEnderCrystal && crystal == null) {
                    crystal = entity;
                }
            }
        }

        return crystal;
    }

    private boolean checkTrap(Vec3d pos, Vec3d[] list) {
        Vec3d[] var3 = list;
        int var4 = list.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Vec3d vec3d = var3[var5];
            BlockPos position = (new BlockPos(pos)).add(vec3d.x, vec3d.y, vec3d.z);
            Block block = EntityUtils.mc.world.getBlockState(position).getBlock();
            if (block == Blocks.OBSIDIAN) {
                return true;
            }
        }

        return false;
    }
}
