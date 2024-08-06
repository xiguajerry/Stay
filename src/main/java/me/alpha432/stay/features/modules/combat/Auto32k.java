/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.event.Render2DEvent;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.gui.StayGui;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.render.ArrowESP;
import me.alpha432.stay.features.modules.render.BlockHighlight;
import me.alpha432.stay.features.setting.Bind;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.basement.wrapper.Wrapper;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.DamageUtil;
import me.alpha432.stay.util.math.HoleUtil;
import me.alpha432.stay.util.world.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Auto32k extends Module {
    public Auto32k() {
        super("AutoXin32k", "Automatically places 32ks", Module.Category.COMBAT, true, false, false);
    }

    public Setting<Boolean> rotate = register(new Setting<>("Rotate", true));
    public Setting<Boolean> swordOnly = register(new Setting<>("SwordOnly", true));
    public Setting<Boolean> tot = register(new Setting<>("Totem switch", true));
    public Setting<Boolean> criticals = register(new Setting<>("criticals", true));
    public Setting<Boolean> NBT = register(new Setting<>("NBT", true));
    public Setting<Boolean> look = register(new Setting<>("LookMode", true));
    private final Setting<Bind> bind2 = this.register(new Setting<>("LookPlacesBind", new Bind(42), v -> look.getValue()));
    public Setting<Boolean> fuckmom = register(new Setting<>("Armor detection", true, v -> criticals.getValue()));
    public Setting<Boolean> Second = register(new Setting<>("Second knife", true, v -> criticals.getValue()));
    private final Setting<Double> fuck = register(new Setting<>("Execute full tool HP:", Double.valueOf(24.0), Double.valueOf(0.0), Double.valueOf(36.0), v -> Second.getValue()));
    private final Setting<Integer> packets = register(new Setting<>("criticalsPackets", Integer.valueOf(3), Integer.valueOf(1), Integer.valueOf(4), v -> criticals.getValue()));
    public Setting<Boolean> no = register(new Setting<>("Close crystal", false));
    public Setting<Boolean> Killaura = register(new Setting<>("Killaura", true));
    public Setting<Boolean> tps = register(new Setting<>("Killaura.TpsSync", Boolean.TRUE, v -> Killaura.getValue()));
    public Setting<Boolean> packet = register(new Setting<>("Killaura.Packet", Boolean.FALSE, v -> Killaura.getValue()));
    public Setting<Boolean> delay = register(new Setting<>("Killaura.HitDelay", Boolean.FALSE, v -> Killaura.getValue()));
    public Setting<Boolean> auto = register(new Setting<>("Killaura.AutoDelay", Boolean.FALSE, v -> !delay.getValue()));
    private final Setting<Double> fucks = register(new Setting<>("HP:", 13.0, Double.valueOf(0.0), Double.valueOf(36.0), v -> auto.getValue()));
    private final Setting<Integer> delay2 = register(new Setting<>("KILLDelay", 100, 0, 1000, v -> Killaura.getValue()));
    public Setting<Boolean> friends = register(new Setting<>("No friends", true));
    public Setting<Boolean> tarp = register(new Setting<>("Tarpknife", false));
    private final Setting<Double> tarprange = register(new Setting<>("TarpRange", Double.valueOf(10.0), Double.valueOf(1.0), Double.valueOf(25.0), v -> tarp.getValue()));
    public Setting<Boolean> packet2 = register(new Setting<>("Packet", Boolean.valueOf(false), v -> tarp.getValue()));
     private final Setting<Bind> bind = this.register(new Setting<>("PlacesBind", new Bind(-1)));
    private final Setting<Double> range = register(new Setting<>("Place Distance", 5.0, 0.0, 10.0));
    private final Setting<Double> range3 = register(new Setting<>("Detection distance", 10.0, 1.0, 20.0));

    private final Setting<Boolean> Auxiliary = register(new Setting<>("Auxiliary algorithm", true));
    private final Setting<Boolean> Pointer = register(new Setting<>("Pointer", true));
    private final Setting<Integer> red = register(new Setting<>("Red", 255, 0, 255, v -> Pointer.getValue()));
    private final Setting<Integer> green = register(new Setting<>("Green", 255, 0, 255, v -> Pointer.getValue()));
    private final Setting<Integer> blue = register(new Setting<>("Blue", 255, 0, 255, v -> Pointer.getValue()));
    private final Setting<Integer> radius = register(new Setting<>("Placement", 45, 10, 200, v -> Pointer.getValue()));
    private final Setting<Float> size = register(new Setting<>("Size", Float.valueOf(10.0f), Float.valueOf(5.0f), Float.valueOf(25.0f), v -> Pointer.getValue()));
    private final Setting<Boolean> outline = register(new Setting<>("Outline", true, v -> Pointer.getValue()));
    private final Setting<Float> outlineWidth = register(new Setting<>("Outline-Width", Float.valueOf(1.0f), Float.valueOf(0.1f), Float.valueOf(3.0f), v -> Pointer.getValue()));
    private final Setting<Integer> fadeDistance = register(new Setting<>("Range", 100, 10, 200, v -> Pointer.getValue()));


    public static Color getColor(int red, int green, int blue, int alpha) {
        Color color = new Color((float) red / 255.0f, (float) green / 255.0f, (float) blue / 255.0f, (float) alpha / 255.0f);

        return color;
    }

    private int fume=0;
    public int totems = 0;
    private String stagething;
    private int Hopperslot;
    private int ShulkerSlot;
    private int playerHotbarSlot;
    public static BlockPos placeTarget;
    public static BlockPos placeTarget2;
    private boolean active;
    private int beds;
    private int stage;
    private int shulkerSlot;
    private final Timer timer = new Timer();
    private final Timer timer2 = new Timer();
    private int hopperSlot;
    private boolean isSneaking;
    private boolean isAttacking = false;
    private boolean offHand = false;
    private float I;
    private Entity IS;

    public static final List<BlockPos> Trap = Arrays.asList(
            new BlockPos(0, -1, -1),
            new BlockPos(1, -1, 0),
            new BlockPos(0, -1, 1),
            new BlockPos(-1, -1, 0),
            new BlockPos(0, 0, -1),
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, 1),
            new BlockPos(-1, 0, 0),
            new BlockPos(0, 1, -1),
            new BlockPos(0, 1, 1),
            new BlockPos(-1, 1, 0),
            new BlockPos(1, 1, 0),
            new BlockPos(0, 2, -1),
            new BlockPos(0, 2, 1),
            new BlockPos(-1, 2, 0),
            new BlockPos(0, 2, 0),
            new BlockPos(1, 2, 0)

    );

    private void lookmods() {
        placeTarget=null;
        placeTarget2=null;
        this.Hopperslot = -1;
        this.ShulkerSlot = -1;
        RayTraceResult ray = BlockHighlight.mc.objectMouseOver;
        if (ray == null) {
            return;
        }
        BlockPos placePos = ray.getBlockPos();
        if (mc.world.getBlockState(new BlockPos(placePos.getX(), placePos.getY() + 1, placePos.getZ())).getBlock() != Blocks.AIR || mc.world.getBlockState(new BlockPos(placePos.getX(), placePos.getY() + 2, placePos.getZ())).getBlock() != Blocks.AIR) {
            return;
        }
        if (mc.world.getBlockState(new BlockPos(placePos.getX(), placePos.getY(), placePos.getZ())).getBlock() == Blocks.AIR && mc.world.getBlockState(new BlockPos(placePos.getX() + 1, placePos.getY(), placePos.getZ())).getBlock() == Blocks.AIR && mc.world.getBlockState(new BlockPos(placePos.getX() - 1, placePos.getY(), placePos.getZ())).getBlock() == Blocks.AIR && mc.world.getBlockState(new BlockPos(placePos.getX(), placePos.getY(), placePos.getZ() + 1)).getBlock() == Blocks.AIR && mc.world.getBlockState(new BlockPos(placePos.getX(), placePos.getY(), placePos.getZ() - 1)).getBlock() == Blocks.AIR) {
            return;
        }

        if (placePos == null) return;
        placeTarget = placePos.add(0, 1, 0);
        Anti32k.min=placeTarget;
        this.isSneaking = false;


        for (int x = 0; x <= 8; ++x) {
            Item item = Auto32k.mc.player.inventory.getStackInSlot(x).getItem();
            if (item == Item.getItemFromBlock(Blocks.HOPPER)) {
                this.Hopperslot = x;
                continue;
            }
            if (item instanceof ItemShulkerBox) {
                if (NBT.getValue()) {
                    if (mc.player.inventory.getStackInSlot(x).serializeNBT().copy().toString().indexOf("AttributeModifiers:[{UUIDMost:2345838571545327294L,UUIDLeast:-1985342459327194118L,Amount:32767,AttributeName") != -1) {
                        this.ShulkerSlot = x;
                    }
                } else {
                    this.ShulkerSlot = x;
                }

            }
        }

        if (ShulkerSlot == -1 || Hopperslot == -1) {
            Command.sendMessage("Hopper/Shulker No Found!");
            return;
        }
        this.placeBlock(placePos.add(0, 1, 0), Hopperslot);
        this.placeBlock(placePos.add(0, 2, 0), ShulkerSlot);
        Anti32k.min=placeTarget;
        fume=0;
        WorldUtil.openBlock(placePos.add(0, 1, 0));
        Command.sendMessage("[Auto32kHopper] " + ChatFormatting.GREEN + "Succesfully" + ChatFormatting.WHITE + " placed 32k");

    }


    private void placeBlock(BlockPos pos, int slot) {
        if (!TestUtil.emptyBlocks.contains(Auto32k.mc.world.getBlockState(pos).getBlock())) {
            return;
        }
        if (slot != Auto32k.mc.player.inventory.currentItem) {
            Auto32k.mc.player.inventory.currentItem = slot;
        }
        EnumFacing[] enumFacingArray = EnumFacing.values();
        int n = enumFacingArray.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing f = enumFacingArray[n2];
            Block neighborBlock = Auto32k.mc.world.getBlockState(pos.offset(f)).getBlock();
            if (!TestUtil.emptyBlocks.contains(neighborBlock)) {
                Auto32k.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Auto32k.mc.player, CPacketEntityAction.Action.START_SNEAKING));
                Auto32k.mc.playerController.processRightClickBlock(Auto32k.mc.player, Auto32k.mc.world, pos.offset(f), f.getOpposite(), new Vec3d((Vec3i)pos), EnumHand.MAIN_HAND);
                Auto32k.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)Auto32k.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            }
            ++n2;
        }
    }
    //criticals on
    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if(Second.getValue()){

            if(I>=fuck.getValue()){
                return;
            }



        }


            if(fuckmom.getValue()){
                if(IS!=null){
                    for (ItemStack armourStack : IS.getArmorInventoryList()) {

                        if(armourStack.getItem()!=Items.DIAMOND_CHESTPLATE||armourStack.getItem()!=Items.DIAMOND_HELMET||armourStack.getItem()!=Items.DIAMOND_LEGGINGS||armourStack.getItem()!=Items.DIAMOND_BOOTS){
                            CPacketUseEntity packet;
                            if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
                                if (!criticals.getValue()) {
                                    return;
                                }

                                if (!timer.passedMs(0L)) {
                                    return;
                                }
                                if (Criticals.mc.player.onGround && !Criticals.mc.gameSettings.keyBindJump.isKeyDown() && packet.getEntityFromWorld(Criticals.mc.world) instanceof EntityLivingBase && !Criticals.mc.player.isInWater() && !Criticals.mc.player.isInLava()) {
                                    switch (packets.getValue()) {
                                        case 1: {
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + (double) 0.1f, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                                            break;
                                        }
                                        case 2: {
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625101, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.1E-5, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                                            break;
                                        }
                                        case 3: {
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625101, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0125, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                                            break;
                                        }
                                        case 4: {
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1625, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 4.0E-6, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.0E-6, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                                            Criticals.mc.player.connection.sendPacket(new CPacketPlayer());
                                            Criticals.mc.player.onCriticalHit(Objects.requireNonNull(packet.getEntityFromWorld(Criticals.mc.world)));
                                        }
                                    }
                                    timer.reset();
                                }
                            }
                            return;
                        }

                    }

                }


            }



        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity) event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
            if (!criticals.getValue()) {
                return;
            }

            if (!timer.passedMs(0L)) {
                return;
            }
            if (Criticals.mc.player.onGround && !Criticals.mc.gameSettings.keyBindJump.isKeyDown() && packet.getEntityFromWorld(Criticals.mc.world) instanceof EntityLivingBase && !Criticals.mc.player.isInWater() && !Criticals.mc.player.isInLava()) {
                switch (packets.getValue()) {
                    case 1: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + (double) 0.1f, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        break;
                    }
                    case 2: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625101, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.1E-5, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        break;
                    }
                    case 3: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0625101, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.0125, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        break;
                    }
                    case 4: {
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.1625, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 4.0E-6, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 1.0E-6, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                        Criticals.mc.player.connection.sendPacket(new CPacketPlayer());
                        Criticals.mc.player.onCriticalHit(Objects.requireNonNull(packet.getEntityFromWorld(Criticals.mc.world)));
                    }
                }
                timer.reset();
            }
        }
    }

    private void placeBlock(BlockPos pos, EnumFacing side) {
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        if (!this.isSneaking) {
            Auto32k.mc.player.connection.sendPacket(new CPacketEntityAction(Auto32k.mc.player, CPacketEntityAction.Action.START_SNEAKING));
            this.isSneaking = true;
        }
        Vec3d hitVec = new Vec3d(neighbour).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        if (this.rotate.getValue()) {
            BlockInteractionHelper.faceVectorPacketInstant(hitVec);
        }
        Auto32k.mc.playerController.processRightClickBlock(Auto32k.mc.player, Auto32k.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        Auto32k.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState() && !(Auto32k.mc.currentScreen instanceof StayGui) && bind2.getValue().getKey() > -1&&bind2.getValue().getKey() > -1&&look.getValue()) {
            if (Keyboard.isKeyDown(bind2.getValue().getKey()) && mc.currentScreen == null&&Keyboard.isKeyDown(bind.getValue().getKey())) {
                Anti32k.min=null;
                lookmods();
                    return;

            }

        }
        if (Keyboard.getEventKeyState() && !(Auto32k.mc.currentScreen instanceof StayGui) && bind.getValue().getKey() > -1) {
            if (Keyboard.isKeyDown(bind.getValue().getKey()) && mc.currentScreen == null) {
                place32k();
            }
        }
    }

    private BlockPos getNearestHopper2() {
        Double maxDist = this.range.getValue();

        List<BlockPos> Blocksss = new ArrayList<>();
        Double x;
        for (x = maxDist; x >= -maxDist; x--) {
            Double y;
            for (y = maxDist; y >= -maxDist; y--) {

                Double z;
                for (z = maxDist; z >= -maxDist; z--) {
                    BlockPos pos = new BlockPos(Wrapper.getPlayer().posX + x, Wrapper.getPlayer().posY + y, Wrapper.getPlayer().posZ + z);
                    double dist = Wrapper.getPlayer().getDistance(pos.getX(), pos.getY(), pos.getZ());
                    BlockPos pos2 = new BlockPos(pos.getX(),pos.getY()+1,pos.getZ());
                    BlockPos pos3 = new BlockPos(pos.getX(),pos.getY()-1,pos.getZ());

                    if (dist >= maxDist && range3.getValue() >= dist    &&Wrapper.getWorld().getBlockState(pos3).getBlock() != Blocks.HOPPER && !(Wrapper.getWorld().getBlockState(pos3).getBlock() instanceof BlockShulkerBox)&&Wrapper.getWorld().getBlockState(pos3).getBlock() != Blocks.AIR) {
                        if(Wrapper.getWorld().getBlockState(pos).getBlock() != Blocks.WATER&&Wrapper.getWorld().getBlockState(pos).getBlock() != Blocks.LAVA&&Wrapper.getWorld().getBlockState(pos).getBlock() != Blocks.AIR){
                            continue;
                        }
                        if(Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.WATER&&Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.LAVA&&Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.AIR){
                            continue;
                        }
//
                        if(pos.getY()<1||pos.getY()>255){
                            continue;

                        }
                        EntityPlayer   target = getTarget(range3.getValue(), true);
                            double dists = Wrapper.getPlayer().getDistance(pos.getX() , pos.getY(), pos.getZ() );
                            if(dists>range3.getValue()){ continue;}
                            if(dists<range.getValue()){continue;}
                            if(target!=null){
                                if (dist<=Math.sqrt((target.posX - pos.getX()) * (target.posX - pos.getX()) + (target.posY - pos.getY()) * (target.posY - pos.getY()) + (target.posZ - pos.getZ()) * (target.posZ - pos.getZ()))){
                                    continue;
                                }

                            }

                            Blocksss.add(pos);




                    }


                }
            }
        }


        int a = 0;

        for (BlockPos renderBlock : Blocksss) {
            a++;
        }
        if (a == 0) {
            return null;
        }
        double ant =  -1;
        int fomen = -1;
        EntityPlayer target = getTarget(range3.getValue(), true);
        for (int i = 0; i < a; i++) {
            BlockPos pos =Blocksss.get(i);
            double dist = Math.sqrt((target.posX - pos.getX()) * (target.posX - pos.getX()) + (target.posY - pos.getY()) * (target.posY - pos.getY()) + (target.posZ - pos.getZ()) * (target.posZ - pos.getZ()));
            if(dist>ant){
                ant=dist;
                fomen=i;
            }
        }
       if(fomen==-1){
           return null;
       }
        return Blocksss.get(fomen);

//        int a = 0;
//
//        for (BlockPos renderBlock : Blocksss) {
//            a++;
//        }
//        if (a == 0) {
//            return null;
//        }
//        Random random = new Random();
//
//        return Blocksss.get(random.nextInt(a));
    }
//    private BlockPos getNearestHopper() {
//        Double maxDist = this.range.getValue();
//        BlockPos ret = null;
//        List<BlockPos> Blocksss = new ArrayList<>();
//        Double x;
//        for (x = maxDist; x >= -maxDist; x--) {
//            Double y;
//            for (y = maxDist; y >= -maxDist; y--) {
//
//                Double z;
//                for (z = maxDist; z >= -maxDist; z--) {
//                    BlockPos pos = new BlockPos(Wrapper.getPlayer().posX + x+0.5D, Wrapper.getPlayer().posY + y+0.5D, Wrapper.getPlayer().posZ + z+0.5D);
//                    double dist = Wrapper.getPlayer().getDistance(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D);
//                    BlockPos pos2 = new BlockPos(pos.getX()+0.5D,pos.getY()+1,pos.getZ()+0.5D);
//                    BlockPos pos3 = new BlockPos(pos.getX()+0.5D,pos.getY()-1,pos.getZ()+0.5D);
//
//                        if (dist >= maxDist && range3.getValue() >= dist    &&Wrapper.getWorld().getBlockState(pos3).getBlock() != Blocks.HOPPER && !(Wrapper.getWorld().getBlockState(pos3).getBlock() instanceof BlockShulkerBox)&&Wrapper.getWorld().getBlockState(pos3).getBlock() != Blocks.AIR) {
//                            if(Wrapper.getWorld().getBlockState(pos).getBlock() != Blocks.WATER&&Wrapper.getWorld().getBlockState(pos).getBlock() != Blocks.LAVA&&Wrapper.getWorld().getBlockState(pos).getBlock() != Blocks.AIR){
//                                continue;
//                            }
//                            if(Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.WATER&&Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.LAVA&&Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.AIR){
//                                continue;
//                            }
////
//                            if(pos.getY()<1||pos.getY()>255){
//                                continue;
//
//                            }
//                            if (anti.getValue()) {
//
//                                EntityPlayer   target = getTarget(range4.getValue(), true);
//                                if (target != null) {
//
//                                        if(range2.getValue()>range3.getValue()&&Wrapper.getPlayer().getDistance(pos.getX() , pos.getY(), pos.getZ())<Wrapper.getPlayer().getDistance(target.posX , target.posY, target.posZ )){
//                                            double dists = Wrapper.getPlayer().getDistance(target.posX , target.posY, target.posZ );
//                                            if(Wrapper.getPlayer().getDistance(pos.getX() , pos.getY(), pos.getZ() )>4.5||Wrapper.getPlayer().getDistance(pos.getX() , pos.getY()+1, pos.getZ() )>4.5){
//                                                continue;
//                                            }
//                                            if (range2.getValue()-range3.getValue()>=dists){
//                                                continue;
//                                            }
//                                        }
//
//
//
//                                    double distance = Math.sqrt((target.posX - pos.getX()) * (target.posX - pos.getX()) + (target.posY - pos.getY()) * (target.posY - pos.getY()) + (target.posZ - pos.getZ()) * (target.posZ - pos.getZ()));
//                                    if (distance >= range2.getValue()) {
//                                        double dists = Wrapper.getPlayer().getDistance(pos.getX() , pos.getY(), pos.getZ() );
//                                        if(dists>6){
//                                            continue;
//                                        }
//                                        maxDist = dist;
//                                        ret = pos;
//                                        if (dists>=Math.sqrt((target.posX - pos.getX()) * (target.posX - pos.getX()) + (target.posY - pos.getY()) * (target.posY - pos.getY()) + (target.posZ - pos.getZ()) * (target.posZ - pos.getZ()))){
//                                            continue;
//                                        }
//                                        if(BlockUtil.isPositionPlaceable(pos, true)!=3&&dists>3){
//                                            continue;
//                                        }
//                                        if(Auxiliary.getValue()){
//                                            int bet =   BlockUtil.getenum(new BlockPos(target.posX,target.posY,target.posZ),new BlockPos(mc.player.posX,mc.player.posY,mc.player.posZ));
//                                            if(BlockUtil.getenum(new BlockPos(mc.player.posX,mc.player.posY,mc.player.posZ),pos)==bet||bet==5){
//                                                Blocksss.add(pos);
//                                            }
//                                        }else {
//                                            Blocksss.add(pos);
//
//                                        }
//
//
//
//                                    }
//                                } else {
//                                    double dists = Wrapper.getPlayer().getDistance(pos.getX() , pos.getY(), pos.getZ() );
//                                    if(dists>6){
//                                        continue;
//                                    }
//                                    maxDist = dist;
//                                    ret = pos;
//                                    Blocksss.add(pos);
//                                }
//
//                            }else {
//                                double dists = Wrapper.getPlayer().getDistance(pos.getX() , pos.getY(), pos.getZ() );
//                                if(dists>6){
//                                    continue;
//                                }
//                                maxDist = dist;
//                                ret = pos;
//                                Blocksss.add(pos);
//                            }
//
//                        }
//
//
//                }
//            }
//        }
//
//
//        int a = 0;
//
//        for (BlockPos renderBlock : Blocksss) {
//            a++;
//        }
//        if (a == 0) {
//            return null;
//        }
//        Random random = new Random();
//
//        return Blocksss.get(random.nextInt(a));
//    }
    private BlockPos getNearestHopper3() {
        Double maxDist = this.range.getValue();
        BlockPos ret = null;
        List<BlockPos> Blocksss = new ArrayList<>();
        Double x;
        for (x = maxDist; x >= -maxDist; x--) {
            Double y;
            for (y = maxDist; y >= -maxDist; y--) {

                Double z;
                for (z = maxDist; z >= -maxDist; z--) {
                    BlockPos pos = new BlockPos(Wrapper.getPlayer().posX + x, Wrapper.getPlayer().posY + y, Wrapper.getPlayer().posZ + z);
                    double dist = Wrapper.getPlayer().getDistance(pos.getX(), pos.getY(), pos.getZ());
                    BlockPos pos2 = new BlockPos(pos.getX(),pos.getY()+1,pos.getZ());
                    BlockPos pos3 = new BlockPos(pos.getX(),pos.getY()-1,pos.getZ());

                    if (range.getValue() >= dist&&Wrapper.getWorld().getBlockState(pos3).getBlock() != Blocks.AIR&&(Wrapper.getWorld().getBlockState(pos).getBlock() == Blocks.AIR||Wrapper.getWorld().getBlockState(pos).getBlock() == Blocks.LAVA||Wrapper.getWorld().getBlockState(pos).getBlock() == Blocks.WATER)) {

                        if(Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.WATER&&Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.LAVA&&Wrapper.getWorld().getBlockState(pos2).getBlock() != Blocks.AIR){
                            continue;
                        }
                        Blocksss.add(pos);

                    }


                }
            }
        }

        EntityPlayer   target = getTarget(range3.getValue(), true);
        int a = 0;

        for (BlockPos renderBlock : Blocksss) {
            a++;
        }
        if (a == 0) {
            return null;
        }
        BlockPos  pp= null;
        if (target!=null){

            for (int i = 1; i < a; i++) {
            BlockPos pos2 =     Blocksss.get(i);

           if (pp==null){
               pp = pos2;
               continue;
           }
                double distance = Math.sqrt((target.posX - pp.getX()) * (target.posX - pp.getX()) + (target.posY - pp.getY()) * (target.posY - pp.getY()) + (target.posZ - pp.getZ()) * (target.posZ - pp.getZ()));
                double distance2 = Math.sqrt((target.posX - pos2.getX()) * (target.posX - pos2.getX()) + (target.posY - pos2.getY()) * (target.posY - pos2.getY()) + (target.posZ - pos2.getZ()) * (target.posZ - pos2.getZ()));

                if(distance2>distance){
                    pp = pos2;
                    continue;
                }

            }
        }
        if(pp==null){
            Random random = new Random();
       return Blocksss.get(random.nextInt(a));
        }

        return pp;
    }
    public void fck() {

        int i;
        int t;
        this.totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++this.totems;
        } else {


            if (mc.player.inventory.getItemStack().isEmpty()) {
                if (this.totems == 0) {
                    return;
                }

                t = -1;
                for (i = 0; i < 45; ++i) {
                    if (mc.player.inventory.getStackInSlot(i).getItem() != Items.TOTEM_OF_UNDYING) continue;
                    t = i;
                    break;
                }
                if (t == -1) {
                    return;
                }
                mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
                this.moving = true;
                if (this.moving) {
                    mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
                    this.moving = false;
                    if (!mc.player.inventory.getItemStack().isEmpty()) {
                        this.returnI = true;
                    }
                    return;
                }
            }
        }





        if (this.returnI) {
            t = -1;
            for (i = 0; i < 45; ++i) {
                if (!mc.player.inventory.getStackInSlot((int)i).isEmpty) continue;
                t = i;
                break;
            }
            if (t == -1) {
                return;
            }
            mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
            this.returnI = false;
        }
    }
    private EntityPlayer getTarget(double range, boolean trapped) {
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
    public  void place32k() {
        this.Hopperslot = -1;
        this.ShulkerSlot = -1;
        this.isSneaking = false;
        this.placeTarget = null;
        this.placeTarget2 = null;

        for (int x = 0; x <= 8; ++x) {
            Item item = Auto32k.mc.player.inventory.getStackInSlot(x).getItem();
            if (item == Item.getItemFromBlock(Blocks.HOPPER)) {
                this.Hopperslot = x;
                continue;
            }
            if (item instanceof ItemShulkerBox) {
                if(NBT.getValue()){
                    if(mc.player.inventory.getStackInSlot(x).serializeNBT().copy().toString().indexOf("AttributeModifiers:[{UUIDMost:2345838571545327294L,UUIDLeast:-1985342459327194118L,Amount:32767,AttributeName")!=-1){
                        this.ShulkerSlot = x;
                    }
                }else {
                    this.ShulkerSlot = x;
                }

            }
        }

        if (ShulkerSlot == -1 || Hopperslot == -1) {
            Command.sendMessage("Hopper/Shulker No Found!");
            return;
        }
        this.placeTarget = getNearestHopper3();
//        if(this.placeTarget==null){
//            this.placeTarget = getNearestHopper2();
//        }



        if (placeTarget != null) {


//            if(Wrapper.getPlayer().getDistance(placeTarget.getX() , placeTarget.getY(), placeTarget.getZ() )>4.5){
//                placeTarget = null;
//                int swordIndex;
//                int i;
//                for(swordIndex = -6; swordIndex <= 6; ++swordIndex) {
//                    for(i = -2; i <= 2; ++i) {
//                        for(int z = -6; z <= 6; ++z) {
//                            BlockPos autoPos = EntityUtils.getPlayerPos(Auto32k.mc.player).add(swordIndex, i, z);
//
//                            if (!TestUtil.emptyBlocks.contains(Auto32k.mc.world.getBlockState(autoPos).getBlock()) && TestUtil.emptyBlocks.contains(Auto32k.mc.world.getBlockState(autoPos.add(0, 1, 0)).getBlock()) && TestUtil.emptyBlocks.contains(Auto32k.mc.world.getBlockState(autoPos.add(0, 2, 0)).getBlock()) && Auto32k.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(autoPos.add(0, 1, 0))).isEmpty() && Auto32k.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(autoPos.add(0, 2, 0))).isEmpty() && (BlockUtil.getDistance32k(target, autoPos.add(0, 1, 0)) <= 6.0 ) && (BlockUtil.getDistance32k(target, autoPos.add(0, 2, 0)) <= 6.0) && (BlockUtil.getDistance32k((Entity)Auto32k.mc.player, autoPos) > 4.5) && (BlockUtil.getDistance32k((Entity)Auto32k.mc.player, autoPos.add(0, 1, 0)) > 4.5) && (BlockUtil.getDistance32k((Entity)Auto32k.mc.player, autoPos.add(0, 2, 0)) > 4.5)) {
//                                this.placeTarget = new BlockPos(autoPos.getX(),autoPos.getY()+1,autoPos.getZ());
//                            }
//                        }
//                    }
//                }
//            }
//            if(placeTarget==null){return;}
            if(tarp.getValue()){
                if(InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass())!=-1){
                    EntityPlayer on =  getTarget(tarprange.getValue(),true);
                    if(on!=null){
                        if(HoleUtil.isHole(new BlockPos(on.posX,on.posY,on.posZ))){
                            BlockPos pos = new BlockPos(on.posX,on.posY,on.posZ);
                            for(BlockPos bPoss : Trap) {
                                mc.player.inventory.currentItem=InventoryUtil.findHotbarBlock(Blocks.OBSIDIAN.getClass());
                                BlockUtil.placeBlock(pos.add(bPoss), offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, rotate.getValue(), packet2.getValue(), isSneaking);

                            }
                        }
                    }
                }else {
                    Command.sendMessage("No Obsidian! Tarp execution failed");
                }
            }


            Auto32k.mc.player.inventory.currentItem = this.Hopperslot;
            this.stagething = "HOPPER";
            placeBlock(new BlockPos(this.placeTarget), EnumFacing.DOWN);
            Auto32k.mc.player.inventory.currentItem = this.ShulkerSlot;
            this.stagething = "SHULKER";
            placeBlock(new BlockPos(this.placeTarget.getX(),this.placeTarget.getY()+1,this.placeTarget.getZ()), EnumFacing.DOWN);
            Auto32k.mc.player.connection.sendPacket(new CPacketEntityAction(Auto32k.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            this.isSneaking = false;
            this.stagething = "OPENING";
            if(Offhand.mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING&&tot.getValue()){
                OffHandCrystal.dev=true;
                tis.reset();
                fck();
            }
            mc.player.connection.sendPacket(new CPacketHeldItemChange(ShulkerSlot));
            fume=0;
            WorldUtil.openBlock(this.placeTarget);




            Command.sendMessage("[Auto32kHopper] " + ChatFormatting.GREEN + "Succesfully" + ChatFormatting.WHITE + " placed 32k");

        } else {
            Command.sendMessage("[Auto32kHopper] " + ChatFormatting.RED + "FAILED" + ChatFormatting.WHITE + " because your dumbass thought you could place there");


        }
        return;

    }

    boolean moving = false;
    boolean returnI = false;

    private Timer tis = new Timer();
    @Override
    public void onUpdate() {

        if (mc.player == null || mc.player.isDead) return;
        List<Entity> targets = mc.world.loadedEntityList.stream()
                .filter(entity -> entity != mc.player)
                .filter(entity -> mc.player.getDistance(entity) <= range.getValue())
                .filter(entity -> !entity.isDead)
                .filter(entity -> entity instanceof EntityPlayer)
                .filter(entity -> ((EntityPlayer) entity).getHealth() > 0)
                .sorted(Comparator.comparing(e -> mc.player.getDistance(e)))
                .collect(Collectors.toList());

        targets.forEach(target -> {

            IS=target;
            if (mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD && Auto32k.mc.currentScreen instanceof GuiHopper && Killaura.getValue()) {
                if (friends.getValue()) {
                    if (!Stay.friendManager.isFriend(target.getName())) {


                        I = EntityUtils.getHealth(target);
                        attack( target);
                        timer2.reset();
                    }
                } else {

                    I = EntityUtils.getHealth(target);
                    attack( target);


                }
            }

        });
        int w = 0;

        if (Auto32k.mc.currentScreen instanceof GuiHopper) {
            Anti32k.min=placeTarget;
            GuiHopper gui = (GuiHopper) Auto32k.mc.currentScreen;
            this.active = true;
            for (int i = 0; i <= 4; i++) {
                if (gui.inventorySlots.inventorySlots.get(i).getStack().getItem() == Items.DIAMOND_SWORD) {

                    if (swordOnly.getValue())
                        if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemAir)) {
                            int ss = InventoryUtil.findHotbarBlock(Blocks.AIR);
                            if (ss != -1) {
                                mc.player.inventory.currentItem = ss;
                            } else {

                                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.DROP_ITEM,new BlockPos(1, 0, 0), mc.player.getHorizontalFacing()));

                            }


                        }

                    if (mc.player.getHeldItemMainhand().getItem() instanceof ItemAir) {
                        mc.playerController.windowClick(mc.player.openContainer.windowId, i, mc.player.inventory.currentItem, ClickType.SWAP, mc.player);
                        break;
                    }


                }
            }

            Anti32k.min=placeTarget2;
            placeTarget2 = placeTarget;

        }else {

//            if(mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING&&placeTarget!=null){
//                WorldUtil.openBlock(this.placeTarget);
//            }
            fume++;
            if(fume>=10){
                placeTarget2 = null;
                Anti32k.min=placeTarget2;
                fume=0;
            }

        }

        if(tot.getValue()){
            if(  tis.passedDms(100)) {
                OffHandCrystal.dev=false;
            }else {
                if(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem()!=Items.TOTEM_OF_UNDYING){
                    fck();
                }
            }

        }

    }

    @Override
    public void onRender2D(Render2DEvent event) {


                if (placeTarget2 != null &&Pointer.getValue()) {
                    Color color = getColor( red.getValue(), green.getValue(), blue.getValue(), (int) MathHelper.clamp(255.0f - 255.0f / (float) fadeDistance.getValue().intValue() * Wrapper.getPlayer().getDistance(placeTarget2.getX() + 0.5D, placeTarget2.getY() + 0.5D, placeTarget2.getZ() + 0.5D), 100.0f, 255.0f));
                    int x = Display.getWidth() / 2 / (ArrowESP.mc.gameSettings.guiScale == 0 ? 1 : ArrowESP.mc.gameSettings.guiScale);
                    int y = Display.getHeight() / 2 / (ArrowESP.mc.gameSettings.guiScale == 0 ? 1 : ArrowESP.mc.gameSettings.guiScale);
                    float yaw = getRotations(placeTarget2) - ArrowESP.mc.player.rotationYaw;
                    GL11.glTranslatef((float) x, (float) y, 0.0f);
                    GL11.glRotatef(yaw, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef((float) (-x), (float) (-y), 0.0f);
                    RenderUtil.drawTracerPointer(x, y - radius.getValue(), size.getValue().floatValue(), 2.0f, 1.0f, outline.getValue(), outlineWidth.getValue().floatValue(),color.getRGB());
                    GL11.glTranslatef((float) x, (float) y, 0.0f);
                    GL11.glRotatef(-yaw, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef((float) (-x), (float) (-y), 0.0f);
                }
            }


    private float getRotations(BlockPos ent) {
        double x = ent.getX() - ArrowESP.mc.player.posX;
        double z = ent.getZ() - ArrowESP.mc.player.posZ;
        return (float) (-(Math.atan2(x, z) * 57.29577951308232));
    }
    private void attack(Entity target) {
        if(NBT.getValue()&&mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem()==Items.DIAMOND_SWORD){
            if(mc.player.getHeldItem(EnumHand.MAIN_HAND).serializeNBT().copy().toString().indexOf("AttributeModifiers:[{UUIDMost:2345838571545327294L,UUIDLeast:-1985342459327194118L,Amount:32767,AttributeName")==-1){
                timer.reset();
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.DROP_ITEM,new BlockPos(1, 0, 0), mc.player.getHorizontalFacing()));
            return;
            }
        }
        if(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem()!=Items.DIAMOND_SWORD){
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.DROP_ITEM,new BlockPos(1, 0, 0), mc.player.getHorizontalFacing()));
            return;
        }

        int wait = delay.getValue().booleanValue() ? delay2.getValue() : (int) (DamageUtil.getCooldownByWeapon(mc.player) * (tps.getValue().booleanValue() ? Stay.serverManager.getTpsFactor() : 1.0F));
        if(auto.getValue()&&!delay.getValue()){
            EntityPlayer targets = getTarget(range3.getValue(), true);
            if(targets!=null){
                Stay.targetManager.updateTarget((EntityLivingBase) target);
                if(targets.getHeldItem(EnumHand.OFF_HAND).getItem()==Items.TOTEM_OF_UNDYING&&targets.getHealth()<=fucks.getValue()){
                    wait = delay2.getValue();
                }
            }

        }

        if (!timer.passedMs(wait))
            return;

        if (target == null)
            return;
        Stay.rotationManager.lookAtEntity(target);
        EntityUtils.attackEntity(target, packet.getValue().booleanValue(), true);
        timer.reset();
    }


}

