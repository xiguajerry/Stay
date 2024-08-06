/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.math.MathUtil;
import me.alpha432.stay.util.player.PlayerUtil;
import me.alpha432.stay.util.world.BlockUtil;
import me.alpha432.stay.util.world.PlacementUtil;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.Objects;

public class PistonAura  extends Module {
    public PistonAura() {
        super("PistonCrystal", "PistonCrystal", Module.Category.COMBAT, true, false, false);
    }
    private final Setting<Double> range = register(new Setting<>("Range",  4.9D, 0.0D, 6.0D));
    private final Setting<Boolean> rotate = this.register(new Setting<>("Rotate", false));
    private BlockPos breakPos = null;
    private EntityPlayer _target;
    private BlockPos breakPos1;
    private boolean placedCrystal = false;



    public void onTick() {
        int n = this.findItem(Items.DIAMOND_PICKAXE);
        int n2 = this.findItem(Items.END_CRYSTAL);
        int n3 = this.findMaterials(Blocks.PISTON);
        int n4 = this.findMaterials(Blocks.REDSTONE_BLOCK);
        int n5 = this.findMaterials(Blocks.OBSIDIAN);
        if (n == -1) {
            Command.sendMessage("No Pix");
            this.disable();
        } else if (n2 == -1) {
            Command.sendMessage("No crystal");
            this.disable();
        } else if (n3 == -1) {
            Command.sendMessage("No piston");
            this.disable();
        } else if (n4 == -1) {
            Command.sendMessage("No redstoneblock");
            this.disable();
        } else if (n5 == -1) {
            Command.sendMessage("No OBSIDIAN");
            this.disable();
        } else {
            this._target = PlayerUtil.findClosestTarget((Double)this.range.getValue(),_target);
            if (this._target != null &&   this._target != null || this._target != null) {
                if (mc.world == null) {
                    Command.sendMessage("ByWorld");
                    this.disable();
                }

                if ((double)mc.player.getDistance(this._target) <= (Double)this.range.getValue()) {
                    this.place();
                }

            }
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

    public void place() {
        int n = this.findItem(Items.END_CRYSTAL);
        int n2 = this.findMaterials(Blocks.PISTON);
        int n3 = this.findMaterials(Blocks.REDSTONE_BLOCK);
        int n4 = this.findMaterials(Blocks.OBSIDIAN);
        BlockPos blockPos = new BlockPos(this._target.posX, this._target.posY, this._target.posZ);
        int n5 = this.findItem(Items.DIAMOND_PICKAXE);
        Entity entity2 = (Entity)mc.world.loadedEntityList.stream().filter((entity) -> {
            return entity instanceof EntityEnderCrystal;
        }).min(Comparator.comparing((entity) -> {
            return entity.getDistance(this._target);
        })).orElse((Entity) null);
        int n11;
        float[] fArray;
        if (BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockAir && BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockAir && BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockAir) {
            if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 0.0D, this._target.posZ + 1.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n4 != 999) {
                    mc.player.inventory.currentItem = n4;
                }

                this.breakPos = blockPos.add(1, 0, 1);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 0.0D, this._target.posZ + 1.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n4 != 999) {
                    mc.player.inventory.currentItem = n4;
                }

                this.breakPos = blockPos.add(2, 0, 1);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ + 1.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n4 != 999) {
                    mc.player.inventory.currentItem = n4;
                }

                this.breakPos = blockPos.add(2, 1, 1);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ + 1.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n4 != 999) {
                    mc.player.inventory.currentItem = n4;
                }

                this.breakPos = blockPos.add(1, 1, 1);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n2 != 999) {
                    mc.player.inventory.currentItem = n2;
                }

                this.breakPos = blockPos.add(2, 2, 1);
                this.breakPos1 = blockPos.add(0, 2, 0);
                fArray = MathUtil.calcAngle(new Vec3d(this.breakPos), new Vec3d(this.breakPos1));
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0] + 180.0F, fArray[1], true));
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n != 999) {
                    mc.player.inventory.currentItem = n;
                }

                this.breakPos = blockPos.add(1, 2, 1);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
                this.placedCrystal = true;
            }

            if (BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n3 != 999) {
                    mc.player.inventory.currentItem = n3;
                }

                this.breakPos = blockPos.add(3, 2, 1);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }
        } else if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockPistonMoving) {
            this.breakPos = blockPos.add(3, 2, 1);
            mc.player.inventory.currentItem = n5;
            mc.playerController.updateController();
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.playerController.onPlayerDamageBlock(this.breakPos, EnumFacing.DOWN);
            if (entity2 != null) {
                mc.player.connection.sendPacket(new CPacketUseEntity(entity2));
            }
        } else if ((!(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ + 0.0D) instanceof BlockAir) || !(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ + 0.0D) instanceof BlockAir) || !(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ + 0.0D) instanceof BlockAir)) && !((ResourceLocation) Objects.requireNonNull(BlockUtil.getBlock(this._target.posX, this._target.posY + 1.0D, this._target.posZ + 1.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") && !(BlockUtil.getBlock(this._target.posX, this._target.posY + 1.0D, this._target.posZ + 1.0D) instanceof BlockObsidian)) {
            if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockPistonMoving) {
                this.breakPos = blockPos.add(3, 2, 0);
                mc.player.inventory.currentItem = n5;
                mc.playerController.updateController();
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(this.breakPos, EnumFacing.DOWN);
                if (entity2 != null) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(entity2));
                }
            } else if (BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 3.0D, this._target.posZ) instanceof BlockAir && BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockAir && BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                if (((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian) {
                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 0.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(1, 0, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 0.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(2, 0, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(2, 1, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(1, 1, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(1, 2, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(2, 2, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n2 != 999) {
                            mc.player.inventory.currentItem = n2;
                        }

                        this.breakPos = blockPos.add(2, 3, 0);
                        this.breakPos1 = blockPos.add(0, 3, 0);
                        fArray = MathUtil.calcAngle(new Vec3d(this.breakPos), new Vec3d(this.breakPos1));
                        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0] + 180.0F, fArray[1], true));
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n != 999) {
                            mc.player.inventory.currentItem = n;
                        }

                        this.breakPos = blockPos.add(1, 3, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                        this.placedCrystal = true;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n3 != 999) {
                            mc.player.inventory.currentItem = n3;
                        }

                        this.breakPos = blockPos.add(3, 3, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }
                }
            } else if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockPistonMoving) {
                this.breakPos = blockPos.add(3, 3, 0);
                mc.player.inventory.currentItem = n5;
                mc.playerController.updateController();
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(this.breakPos, EnumFacing.DOWN);
                if (entity2 != null) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(entity2));
                }
            } else if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir && BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir && BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                if (((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian) {
                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 0.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(1, 0, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 0.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(2, 0, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n2 != 999) {
                            mc.player.inventory.currentItem = n2;
                        }

                        this.breakPos1 = blockPos.add(1, 1, 0);
                        this.breakPos = blockPos.add(2, 1, 0);
                        this.breakPos1 = blockPos.add(0, 1, 0);
                        fArray = MathUtil.calcAngle(new Vec3d(this.breakPos), new Vec3d(this.breakPos1));
                        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0] + 180.0F, fArray[1], true));
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n != 999) {
                            mc.player.inventory.currentItem = n;
                        }

                        this.breakPos = blockPos.add(1, 1, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                        this.placedCrystal = true;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n3 != 999) {
                            mc.player.inventory.currentItem = n3;
                        }

                        this.breakPos = blockPos.add(3, 1, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }
                }
            } else if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockPistonMoving) {
                this.breakPos = blockPos.add(3, 1, 0);
                mc.player.inventory.currentItem = n5;
                mc.playerController.updateController();
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(this.breakPos, EnumFacing.DOWN);
                if (entity2 != null) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(entity2));
                }
            } else if (BlockUtil.getBlock(this._target.posX - 2.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir && BlockUtil.getBlock(this._target.posX - 3.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir && BlockUtil.getBlock(this._target.posX - 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                if ((((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian) && (((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian) && (((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian)) {
                    if (BlockUtil.getBlock(this._target.posX - 1.0D, this._target.posY + 0.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(-1, 0, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX - 2.0D, this._target.posY + 0.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(-2, 0, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX - 2.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        this.breakPos = blockPos.add(-2, 1, 0);
                        this.breakPos1 = blockPos.add(0, 1, 0);
                        fArray = MathUtil.calcAngle(new Vec3d(this.breakPos), new Vec3d(this.breakPos1));
                        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0] + 180.0F, fArray[1], true));
                        if (n2 != 999) {
                            mc.player.inventory.currentItem = n2;
                        }

                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX - 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n != 999) {
                            mc.player.inventory.currentItem = n;
                        }

                        this.breakPos = blockPos.add(-1, 1, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                        this.placedCrystal = true;
                    }

                    if (BlockUtil.getBlock(this._target.posX - 3.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n3 != 999) {
                            mc.player.inventory.currentItem = n3;
                        }

                        this.breakPos = blockPos.add(-3, 1, 0);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }
                }
            } else if (BlockUtil.getBlock(this._target.posX - 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockPistonMoving) {
                this.breakPos = blockPos.add(-1, 1, 0);
                mc.player.inventory.currentItem = n5;
                mc.playerController.updateController();
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.playerController.onPlayerDamageBlock(this.breakPos, EnumFacing.DOWN);
                if (entity2 != null) {
                    mc.player.connection.sendPacket(new CPacketUseEntity(entity2));
                }
            } else if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ - 1.0D) instanceof BlockAir && BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ - 1.0D) instanceof BlockAir && BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ - 1.0D) instanceof BlockAir && (((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian) && (((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 3.0D, this._target.posZ + 0.0D) instanceof BlockObsidian)) {
                if (!((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") && !(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian) && !((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") && !(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian) && !((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") && !(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockObsidian)) {
                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ - 1.0D) instanceof BlockPistonMoving) {
                        this.breakPos = blockPos.add(3, 2, -1);
                        mc.player.inventory.currentItem = n5;
                        mc.playerController.updateController();
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.playerController.onPlayerDamageBlock(this.breakPos, EnumFacing.DOWN);
                        if (entity2 != null) {
                            mc.player.connection.sendPacket(new CPacketUseEntity(entity2));
                        }
                    }
                } else if (((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 1.0D, this._target.posZ).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockObsidian) {
                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 0.0D, this._target.posZ - 1.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(1, 0, -1);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 0.0D, this._target.posZ - 1.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(2, 0, -1);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ - 1.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(1, 1, -1);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ - 1.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n4 != 999) {
                            mc.player.inventory.currentItem = n4;
                        }

                        this.breakPos = blockPos.add(2, 1, -1);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ - 1.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n2 != 999) {
                            mc.player.inventory.currentItem = n2;
                        }

                        this.breakPos = blockPos.add(2, 2, -1);
                        this.breakPos1 = blockPos.add(0, 1, 0);
                        fArray = MathUtil.calcAngle(new Vec3d(this.breakPos), new Vec3d(this.breakPos1));
                        mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0] + 180.0F, fArray[1], true));
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ - 1.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n != 999) {
                            mc.player.inventory.currentItem = n;
                        }

                        this.breakPos = blockPos.add(1, 2, -1);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                        this.placedCrystal = true;
                    }

                    if (BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ - 1.0D) instanceof BlockAir) {
                        n11 = mc.player.inventory.currentItem;
                        if (n3 != 999) {
                            mc.player.inventory.currentItem = n3;
                        }

                        this.breakPos = blockPos.add(3, 2, -1);
                        mc.playerController.updateController();
                        PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                        mc.player.inventory.currentItem = n11;
                    }
                }
            }
        } else if (((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockObsidian || ((ResourceLocation)Objects.requireNonNull(BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D).getRegistryName())).toString().toLowerCase().contains("bedrock") || BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ + 1.0D) instanceof BlockObsidian) {
            if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 0.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n4 != 999) {
                    mc.player.inventory.currentItem = n4;
                }

                this.breakPos = blockPos.add(1, 0, 0);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 0.0D, this._target.posZ + 0.0D) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n4 != 999) {
                    mc.player.inventory.currentItem = n4;
                }

                this.breakPos = blockPos.add(2, 0, 0);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n4 != 999) {
                    mc.player.inventory.currentItem = n4;
                }

                this.breakPos = blockPos.add(2, 1, 0);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 1.0D, this._target.posZ) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n4 != 999) {
                    mc.player.inventory.currentItem = n4;
                }

                this.breakPos = blockPos.add(1, 1, 0);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 2.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n2 != 999) {
                    mc.player.inventory.currentItem = n2;
                }

                this.breakPos = blockPos.add(2, 2, 0);
                this.breakPos1 = blockPos.add(0, 2, 0);
                fArray = MathUtil.calcAngle(new Vec3d(this.breakPos), new Vec3d(this.breakPos1));
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(fArray[0] + 180.0F, fArray[1], true));
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }

            if (BlockUtil.getBlock(this._target.posX + 1.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n != 999) {
                    mc.player.inventory.currentItem = n;
                }

                this.breakPos = blockPos.add(1, 2, 0);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
                this.placedCrystal = true;
            }

            if (BlockUtil.getBlock(this._target.posX + 3.0D, this._target.posY + 2.0D, this._target.posZ) instanceof BlockAir) {
                n11 = mc.player.inventory.currentItem;
                if (n3 != 999) {
                    mc.player.inventory.currentItem = n3;
                }

                this.breakPos = blockPos.add(3, 2, 0);
                mc.playerController.updateController();
                PlacementUtil.place(this.breakPos, EnumHand.MAIN_HAND, (Boolean)this.rotate.getValue());
                mc.player.inventory.currentItem = n11;
            }
        }

    }
}
