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
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.world.BlockUtils;
import me.alpha432.stay.util.world.CrystalUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Iterator;

public class AutoCev extends Module {
    public AutoCev() {
        super("AutoCev","AutoCev" ,Module.Category.COMBAT, true, false, false);
    }
    private final Setting<Double> range = register(new Setting<>("Range", 4.9D, 0.0D, 6.0D));
    private final Setting<Boolean> autoToggle = register(new Setting<>("AutoToggle", true));
    Entity currentEntity;
    boolean flag;
    int progress = 0;
    int sleep;
    int civCounter;
    boolean breakFlag;
    private BlockPos breakPos;
    public void onDisable() {
        super.onDisable();
    }

    private int findItem(Item item) {
        if (item == Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return 999;
        } else {
            for(int i = 0; i < 9; ++i) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                    return i;
                }
            }

            return -1;
        }
    }


    public void onEnable() {
        this.findTarget();
        this.progress = 0;
        this.breakFlag = false;
        this.flag = false;
        this.civCounter = 0;
        this.sleep = 0;
        super.onEnable();
    }
    public void onTick() {
        int n = this.findItem(Items.DIAMOND_PICKAXE);
        int n2 = this.findItem(Items.END_CRYSTAL);
        int n3 = this.findMaterials(Blocks.OBSIDIAN);
        BlockPos[] objectArray = new BlockPos[]{new BlockPos(0, 0, 1), new BlockPos(0, 1, 1), new BlockPos(0, 2, 1), new BlockPos(0, 2, 0)};
        int n4 = InventoryUtil.getSlot();
        if (n != -1 && n2 != -1 && n3 != -1) {
            if (this.currentEntity == null || (double)this.currentEntity.getDistance(mc.player) > (Double)this.range.getValue()) {
                this.findTarget();
            }

            if (this.currentEntity != null) {
                if (this.currentEntity.isDead) {
                    disable();
                    return;
                }
                Entity entity = this.currentEntity;
                Stay.targetManager.updateTarget((EntityLivingBase) entity);
                if (entity instanceof EntityPlayer&&!Stay.friendManager.isFriend(entity.getName())) {
//                    if (n2 == -1) {
//                        mc.player.inventory.offHandInventory.get(0).getItem();
//                        Item.getItemById(426);
//                    }

                    if (this.sleep > 0) {
                        --this.sleep;
                    } else {
                        entity.move(MoverType.SELF, 0.0D, -2.0D, 0.0D);
                        switch(this.progress) {
                            case 0:
                                BlockPos blockPos = new BlockPos(entity);
                                BlockPos[] var16 = objectArray;
                                int var17 = objectArray.length;

                                for(int var10 = 0; var10 < var17; ++var10) {
                                    BlockPos blockPos2 = var16[var10];
                                    if (Arrays.asList(objectArray).indexOf(blockPos2) != -1 && this.civCounter < 1) {
                                        this.flag = true;
                                        InventoryUtil.setSlot(n3);
                                    } else {
                                        InventoryUtil.setSlot(n3);
                                    }

                                    BlockUtils blockUtils = BlockUtils.isPlaceable(blockPos.add(blockPos2), 0.0D, true);
                                    if (blockUtils != null) {
                                        blockUtils.doPlace(true);
                                    }
                                }

                                InventoryUtil.setSlot(n2);
                                CrystalUtil.placeCrystal(new BlockPos(entity.posX, entity.posY + 3.0D, entity.posZ));
                                ++this.progress;
                                break;
                            case 1:
                                InventoryUtil.setSlot(n);
                                mc.playerController.onPlayerDamageBlock((new BlockPos(entity)).add(0, 2, 0), EnumFacing.UP);
                                mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, (new BlockPos(entity)).add(0, 2, 0), EnumFacing.UP));
                                if (mc.world.isAirBlock((new BlockPos(entity)).add(0, 2, 0))) {
                                    Iterator var13 = mc.world.loadedEntityList.iterator();

                                    while(var13.hasNext()) {
                                        Entity entity2 = (Entity)var13.next();
                                        if (!((double)entity.getDistance(entity2) > (Double)this.range.getValue()) && entity2 instanceof EntityEnderCrystal) {
                                            mc.playerController.attackEntity(mc.player, entity2);
                                        }
                                    }

                                    this.breakFlag = true;
                                }

                                if (this.civCounter < 1) {
                                    mc.playerController.onPlayerDamageBlock((new BlockPos(entity)).add(0, 2, 0), EnumFacing.UP);
                                    this.sleep += 30;
                                }

                                ++this.progress;
                                break;
                            case 2:
                                int n5 = 0;
                                Iterator var8 = mc.world.loadedEntityList.iterator();

                                while(var8.hasNext()) {
                                    Entity entity3 = (Entity)var8.next();
                                    if (!((double)entity.getDistance(entity3) > (Double)this.range.getValue()) && entity3 instanceof EntityEnderCrystal) {
                                        mc.playerController.attackEntity(mc.player, entity3);
                                        ++n5;
                                    }
                                }

                                if (n5 == 0 || this.flag) {
                                    ++this.progress;
                                }
                                break;
                            case 3:
                                BlockUtils.doPlace(BlockUtils.isPlaceable(new BlockPos(entity.posX, entity.posY + 2.0D, entity.posZ), 0.0D, true), true);
                                InventoryUtil.setSlot(n3);
                                this.progress = 0;
                                ++this.civCounter;
                        }
                    }

                    InventoryUtil.setSlot(n4);
                    return;
                }

                InventoryUtil.setSlot(n4);
            }

        } else {
            Command.sendMessage("Pix or Crystal or Obsidian No Material");
            this.disable();
        }
    }

    private int findMaterials(Block block) {
        for(int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock)mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == block) {
                return i;
            }
        }

        return -1;
    }

    public void findTarget() {
        this.currentEntity = (Entity)mc.world.loadedEntityList.stream().filter((entity) -> {
            return entity != mc.player && entity instanceof EntityLivingBase && (double)entity.getDistance(mc.player) < (Double)this.range.getValue()&&!Stay.friendManager.isFriend(entity.getName());
        }).findFirst().orElse((Entity) null);
    }
}
