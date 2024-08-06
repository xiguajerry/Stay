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
import me.alpha432.stay.event.PacketEvent;
import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.command.Command;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.client.ClickGui;
import me.alpha432.stay.features.modules.misc.AutoGG;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.basement.wrapper.Wrapper;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import me.alpha432.stay.util.graphics.animations.BlockEasingRender;
import me.alpha432.stay.util.graphics.animations.Easing;
import me.alpha432.stay.util.graphics.color.ColorUtil;
import me.alpha432.stay.util.graphics.color.Colour;
import me.alpha432.stay.util.inventory.item.ItemUtil;
import me.alpha432.stay.util.player.RotationUtil;
import me.alpha432.stay.util.world.BlockInteractionHelper;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class StormCrystal extends Module {

    public static double yaw;
    public static double pitch;
    public static double renderPitch;
    public static boolean isSpoofingAngles;
    public static Entity target;
    public EntityEnderCrystal c;
    private int Placements;
    private int oldSlot;
    private int PrevSlot;
    private boolean switchCoolDown;
    boolean togglePitch;
    private BlockPos renderBlock;
    boolean shouldIgnoreEntity;
    private static boolean rotating = false;
    private int lastEntityId;
    private BlockPos pos1;
    Timer placeTimer;
    Timer breakTimer;
    Timer updateTimer;
    private final HashMap<BlockPos, Double> renderBlockDmg;
    List<BlockPos> PlacePosList;

    CrystalTarget crystalTarget;

    static class CrystalTarget {
        public BlockPos blockPos;
        public Entity target;

        public CrystalTarget(BlockPos block, Entity target) {
            this.blockPos = block;
            this.target = target;
        }
    }

    public Setting<ThreadMode> ExplodeHand_value = this.register(new Setting<>("HitHand", ThreadMode.Off));
    public Setting<ThreadMode2> AttackMode_value = this.register(new Setting<>("HitMode", ThreadMode2.Smart));
    public Setting<ThreadMode3> RenderMode_value = this.register(new Setting<>("RenderPattern", ThreadMode3.Dynamic));
    private final Setting<Boolean> AutoSwitch_value = this.register(new Setting<>("AutoSwitch", false));
    private final Setting<Boolean> TargetPlayer_value = this.register(new Setting<>("Players", true));
    private final Setting<Boolean> TargetMobs_value = this.register(new Setting<>("Mobs", false));
    private final Setting<Boolean> TargetAnimals_value = this.register(new Setting<>("Animals", false));
    private final Setting<Boolean> Place_value = this.register(new Setting<>("Place", true));
    private final Setting<Boolean> legitplace_value = this.register(new Setting<>("Legitplace", true));
    private final Setting<Boolean> Explode_value = this.register(new Setting<>("Explode", true));
    private final Setting<Boolean> EntityIgnore_value = this.register(new Setting<>("EntityIgnore", true));
    private final Setting<Boolean> MultiPlace_value = this.register(new Setting<>("MultiPlace", false));
    private final Setting<Double> AttackSpeed_value = this.register(new Setting<>("AttackSpeed", 35.0D, 0.0D, 50.0D));
    private final Setting<Double> PlaceSpeed_value = this.register(new Setting<>("PlaceSpeed", 35.0D, 0.0D, 50.0D));
    private final Setting<Double> Distance_value = this.register(new Setting<>("Distance", 11.0D, 0.0D, 15.0D));
    private final Setting<Double> PlaceRange_value = this.register(new Setting<>("PlaceRange", 6.5D, 0.0D, 8.0D));
    private final Setting<Double> HitRange_value = this.register(new Setting<>("HitRange", 5.5D, 0.0D, 8.0D));
    private final Setting<Double> Damage_value = this.register(new Setting<>("Damage", 0.3D, 0.0D, 5.0D));
    private final Setting<Boolean> Rotate_value = this.register(new Setting<>("Rotate", true));
    private final Setting<Boolean> RayTrace_value = this.register(new Setting<>("RayTrace", false));
    private final Setting<Boolean> NewPlace_value = this.register(new Setting<>("1.13Place", false));
    private final Setting<Boolean> Wall_value = this.register(new Setting<>("Wall", true));
    private final Setting<Double> WallRange_value = this.register(new Setting<>("WallRange", 3.5D, 0.0D, 20.0D));
    private final Setting<Boolean> NoSuicide_value = this.register(new Setting<>("NoSuicide", true));
    private final Setting<Boolean> FacePlace_value = this.register(new Setting<>("FacePlace", true));
    private final Setting<Boolean> armorPlace_value = this.register(new Setting<>("ArmorPlace", true));
    private final Setting<Double> armorPlaceDmg_value = this.register(new Setting<>("Armor%", 15.0D, 0.0D, 50.0D));
    private final Setting<Boolean> Predite_value = this.register(new Setting<>("Predite", true));
    private final Setting<Integer> PrediteNumber_value = this.register(new Setting<>("EnemyPlacePredite", 2, 0, 10));
    private final Setting<Double> BlastHealth_value = this.register(new Setting<>("MinHealthFace", 10.0D, 0.0D, 20.0D));
    private final Setting<Double> MinDmg_value = this.register(new Setting<>("PlaceMinDamage", 4.5D, 0.0D, 20.0D));
    private final Setting<Double> MaxSelf_value = this.register(new Setting<>("PlaceMaxSelf", 10.0D, 0.0D, 36.0D));
    private final Setting<Double> BMinDmg_value = this.register(new Setting<>("BreakMinDmg", 4.5D, 0.0D, 36.0D));
    private final Setting<Double> BMaxSelf_value = this.register(new Setting<>("BreakMaxSelf", 12.0D, 0.0D, 36.0D));
    private final Setting<Boolean> PopTotemTry = this.register(new Setting<>("PopTotemTry", true));
    final Setting<Boolean> GhostHand_value = this.register(new Setting<>("GhostHand", false));

    private final Setting<Boolean> PlacebyPass_value = this.register(new Setting<>("ByPass", false));
    final Setting<Boolean> weak = this.register(new Setting<>("antiWeakness", false));
    private final Setting<Boolean> PauseEating_value = this.register(new Setting<>("PauseWhileEating", false));
    private final Setting<Boolean> RenderDmg_value = this.register(new Setting<>("RenderDamage", false));
    private final Setting<Boolean> render = this.register(new Setting<>("Silky rendering", true));
    public Setting<Boolean> colorSync = this.register(new Setting<>("ColorSync", Boolean.valueOf(false)));
    private final Setting<Integer> Red_value = this.register(new Setting<>("Red", 255, 0, 255));
    private final Setting<Integer> Green_value = this.register(new Setting<>("Green", 0, 0, 255));
    private final Setting<Integer> Blue_value = this.register(new Setting<>("Blue", 0, 0, 255));
    private final Setting<Integer> Alpha_value = this.register(new Setting<>("Alpha", 70, 0, 255));
    private final Setting<Integer> Width_value = this.register(new Setting<>("Width", 2, 0, 5));
    private final Setting<Boolean> Rainbow_value = this.register(new Setting<>("Rainbow", false));
    public Setting<Boolean> box = this.register(new Setting<>("Box", Boolean.valueOf(true), v -> this.render.getValue() != false));
    public Setting<Boolean> outline = this.register(new Setting<>("Outline", Boolean.valueOf(true), v -> this.render.getValue() != false));
    private final Setting<Integer> boxAlpha = this.register(new Setting<>("BoxAlpha", Integer.valueOf(125), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue() != false && this.box.getValue() != false));
    public Setting<Boolean> customOutline = this.register(new Setting<>("CustomLine", Boolean.valueOf(false), String.valueOf(this.render.getValue() != false && this.outline.getValue() != false)));
    private final Setting<Integer> cRed = this.register(new Setting<>("OL-Red", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cGreen = this.register(new Setting<>("OL-Green", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cBlue = this.register(new Setting<>("OL-Blue", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));
    private final Setting<Integer> cAlpha = this.register(new Setting<>("OL-Alpha", Integer.valueOf(255), Integer.valueOf(0), Integer.valueOf(255), v -> this.render.getValue() != false && this.customOutline.getValue() != false && this.outline.getValue() != false));

    private final Setting<Float> RGBSpeed_value = this.register(new Setting<>("RGB Speed", 1.0F, 0.0F, 10.0F));
    private final Setting<Float> Saturation_value = this.register(new Setting<>("Saturation", 0.65F, 0.0F, 1.0F));
    private final Setting<Float> Brightness_value = this.register(new Setting<>("Brightness", 1.0F, 0.0F, 1.0F));
    private final Setting<Easing> movingEasingType = this.register(new Setting<>("MoveType", Easing.OUT_CUBIC));
    private final Setting<Easing> fadingEasingType = this.register(new Setting<>("FadingType", Easing.OUT_CUBIC));

    private final Setting<Integer> moveTime = this.register(new Setting<>("MoveTimeMS", 1000, 10, 20000));
    private final Setting<Integer> fadeTime = this.register(new Setting<>("FadeTimeMS", 1000, 10, 20000));

    public enum ThreadMode3 {
        Dynamic,
        Classic,
    }

    public enum ThreadMode {
        Off,
        Main,
        Packet,


    }

    public enum ThreadMode2 {
        Smart,

    }

    public StormCrystal() {
        super("StormCrystal", "Automatically place crystal to kill enemy", Module.Category.COMBAT, true, false, false);
        this.Placements = 0;
        this.oldSlot = -1;
        this.togglePitch = false;
        this.shouldIgnoreEntity = false;
        this.lastEntityId = -1;
        this.placeTimer = new Timer();
        this.breakTimer = new Timer();
        this.updateTimer = new Timer();
        this.renderBlockDmg = new HashMap<>();
        this.PlacePosList = new ArrayList<>();
    }

    public BlockEasingRender blockRenderSmooth = new BlockEasingRender(new BlockPos(0, 0, 0), moveTime.getValue().floatValue(), fadeTime.getValue().floatValue());

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player != null) {
            if (event.getPacket() instanceof SPacketSoundEffect) {
                SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
                if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {

                    for (Entity e : Minecraft.getMinecraft().world.loadedEntityList) {
                        if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0D) {
                            e.setDead();
                        }
                    }
                }
            }

            BlockPos pos;
            if (event.getPacket() instanceof SPacketSpawnObject && ((SPacketSpawnObject) event.getPacket()).getType() == 51) {
                SPacketSpawnObject sPacketSpawnObject = (SPacketSpawnObject) event.getPacket();
                pos = new BlockPos(sPacketSpawnObject.getX(), sPacketSpawnObject.getY(), sPacketSpawnObject.getZ());
                float selfDamage = calculateDamage(pos.getX(), pos.getY(), pos.getZ(), mc.player, mc.player.getPositionVector());
                float playerHealthy = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                if (!(mc.world.getEntityByID(((SPacketSpawnObject) event.getPacket()).getEntityID()) instanceof EntityEnderCrystal)) {
                    return;
                }

                if (this.PlacePosList.contains(pos.down()) && mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()) <= this.HitRange_value.getValue()) {
                    if (this.PopTotemTry.getValue()) {
                        if (selfDamage - 2.0F > playerHealthy) {
                            return;
                        }
                    } else if ((double) selfDamage - 0.5D > (double) playerHealthy) {
                        return;
                    }

                    if (this.Rotate_value.getValue()) {
                        this.lookAtPos(pos, enumFacing(pos));
                    }

                    this.packetBreakCrystal(sPacketSpawnObject.getEntityID());
                } else if (this.Predite_value.getValue() && (double) selfDamage <= this.MaxSelf_value.getValue() && (double) selfDamage + 0.5D < (double) playerHealthy) {
                    if (this.Rotate_value.getValue()) {
                        this.lookAtPos(pos, enumFacing(pos));
                    }

                    this.packetBreakCrystal(sPacketSpawnObject.getEntityID());
                }
            } else if (event.getPacket() instanceof SPacketExplosion) {
                SPacketExplosion sPacketExplosion = (SPacketExplosion) event.getPacket();
                pos = (new BlockPos(sPacketExplosion.getX(), sPacketExplosion.getY(), sPacketExplosion.getZ())).down();
                this.PlacePosList.remove(pos);
            }

            if (event.getPacket() instanceof SPacketEntityStatus && this.PlacebyPass_value.getValue()) {
                this.lastEntityId = ((SPacketEntityStatus) event.getPacket()).getEntity(mc.world).getEntityId();
            }

        }
    }

    public void onRender3D(Render3DEvent event) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if (renderBlock == null) blockRenderSmooth.end(); else blockRenderSmooth.begin();
        if (render.getValue() && this.RenderMode_value.getValue().equals(ThreadMode3.Dynamic)) {
            AxisAlignedBB bb = blockRenderSmooth.getFullUpdate();
            Color color = this.colorSync.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.Red_value.getValue(), this.Green_value.getValue(), this.Blue_value.getValue(), this.Alpha_value.getValue());
            RenderUtil.drawBBBox(bb, new Colour(color.getRed(), color.getGreen(), color.getBlue(), boxAlpha.getValue()), boxAlpha.getValue());
//          Vec3d renders = blockRenderSmooth.getUpdate(renderBlock);
//            RenderUtil.drawBoxESP(renders, this.colorSync.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.Red_value.getValue(), this.Green_value.getValue(), this.Blue_value.getValue(), this.Alpha_value.getValue()), this.customOutline.getValue(), this.colorSync.getValue() ? getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.Width_value.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue());
//            if (this.RenderDmg_value.getValue()) {
//                if (!renderBlockDmg.isEmpty()) {
//                    double renderDamage = this.renderBlockDmg.get(renderBlock);
//                    RenderUtil.drawText(this.renderBlock, (Math.floor(renderDamage) == renderDamage ? Integer.valueOf((int) renderDamage) : String.format("%.1f", renderDamage)) + "");
//                }
//            }
        } else {
            if (render.getValue() && this.renderBlock != null) {
                RenderUtil.drawBoxESP(this.renderBlock, this.colorSync.getValue() ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(this.Red_value.getValue(), this.Green_value.getValue(), this.Blue_value.getValue(), this.Alpha_value.getValue()), this.customOutline.getValue(), this.colorSync.getValue() ? getCurrentColor() : new Color(this.cRed.getValue(), this.cGreen.getValue(), this.cBlue.getValue(), this.cAlpha.getValue()), this.Width_value.getValue().floatValue(), this.outline.getValue(), this.box.getValue(), this.boxAlpha.getValue(), false);
                if (this.RenderDmg_value.getValue()) {
                    double renderDamage = this.renderBlockDmg.get(renderBlock);
                    RenderUtil.drawText(this.renderBlock, (Math.floor(renderDamage) == renderDamage ? Integer.valueOf((int) renderDamage) : String.format("%.1f", renderDamage)) + "");
                }
            }
        }

    }

    public Color getCurrentColor() {
        return new Color(this.Red_value.getValue(), this.Green_value.getValue(), this.Blue_value.getValue(), this.Alpha_value.getValue());
    }
//    public void onWorldRender(RenderEvent event) {
//        int color = (Boolean)this.SyncGui_value.getValue() ? (new Color(GuiManager.getINSTANCE().getRed(), GuiManager.getINSTANCE().getGreen(), GuiManager.getINSTANCE().getBlue(), (Integer)this.Alpha_value.getValue())).getRGB() : this.getColor();
//        if (this.renderBlock != null) {
//            this.drawBlock(this.renderBlock, color);
//        }
//
//    }

//    @SubscribeEvent
//    public void renderModelRotation(RenderModelEvent event) {
//        if ((Boolean)this.Rotate_value.getValue()) {
//            if (rotating) {
//                event.rotating = true;
//                event.pitch = (float)renderPitch;
//            }
//
//        }
//    }

    @SubscribeEvent
    public void updateRotation(RenderWorldLastEvent event) {
        if (this.Rotate_value.getValue()) {
            if (rotating) {
                mc.player.rotationYawHead = (float) yaw;
                mc.player.renderYawOffset = (float) yaw;
            }

        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (mc.player != null) {
            Packet<?> packet = event.getPacket();
            if (this.Rotate_value.getValue()) {
                if (packet instanceof CPacketPlayer && isSpoofingAngles) {
                    ((CPacketPlayer) packet).yaw = (float) yaw;
                    ((CPacketPlayer) packet).pitch = (float) pitch;
                    isSpoofingAngles = false;
                }

//                if (packet instanceof CPacketUseEntity && ((CPacketUseEntity)packet).getAction() == CPacketUseEntity.Action.ATTACK && ((CPacketUseEntity)packet).getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) {
//                    mc.world.getEntityByID(((CPacketUseEntity)event.packet).entityId).setDead();
//                    mc.world.removeEntityFromWorld(((CPacketUseEntity)packet).entityId);
//                    Vec3d vec3d = ((CPacketUseEntity)event.packet).getHitVec();
//                    this.PlacePosList.add(new BlockPos(vec3d.x, vec3d.y, vec3d.z));
//                }

                if (this.legitplace_value.getValue() && mc.playerController.isHittingBlock) {
                    Vec3d[] var8 = new Vec3d[]{new Vec3d(1.0D, 0.0D, 0.0D), new Vec3d(-1.0D, 0.0D, 0.0D), new Vec3d(0.0D, 0.0D, 1.0D), new Vec3d(0.0D, 0.0D, -1.0D)};
                    int var4 = var8.length;

                    for (int var5 = 0; var5 < var4; ++var5) {
                        Vec3d v = var8[var5];
                        if (v != null && target != null && pos1 != null) {
                            if (this.pos1.add(v.x, v.y, v.z) == target.getPosition()) {
                                return;
                            }
                            BlockPos pos2 = (new BlockPos(target.getPositionVector())).add(v.x, v.y, v.z);
                            if (mc.world.getBlockState(pos2).getBlock() != Blocks.AIR || mc.world.getBlockState(pos2.down()).getBlock() != Blocks.OBSIDIAN || mc.world.getBlockState(pos2.down()).getBlock() != Blocks.BEDROCK) {
                                return;
                            }

                            this.PlaceCrystal(pos2, EnumHand.OFF_HAND);
                        }


                    }
                }

            }
        }
    }

    @Contract("null -> false")
    private boolean canHitCrystal(Entity crystal) {
        if (!(crystal instanceof EntityEnderCrystal)) {
            return false;
        } else {
            Vec3d vec3d = crystal.getPositionVector();
            if (mc.player.getDistance(vec3d.x, vec3d.y, vec3d.z) > this.HitRange_value.getValue()) {
                return false;

            } else if (this.AttackMode_value.getValue().toString().equals("Smart")) {
                float selfDamage = calculateDamage(vec3d.x, vec3d.y, vec3d.z, mc.player, mc.player.getPositionVector());
                if (selfDamage >= mc.player.getHealth() + mc.player.getAbsorptionAmount()) {
                    return false;
                } else {
                    List<EntityPlayer> entities = new ArrayList(mc.world.playerEntities);
                    List<EntityPlayer> entitiess = entities.stream()
                            .filter((e) -> (double) mc.player.getDistance(e) <= this.Distance_value.getValue())
                            .filter((e) -> mc.player != e).filter((e) -> !Stay.friendManager.isFriend(e))
                            .sorted(Comparator.comparing((e) -> mc.player.getDistance(e)))
                            .collect(Collectors.toList());

                    for (EntityPlayer player : entities) {
                        if (!player.isDead && !(player.getHealth() + player.getAbsorptionAmount() <= 0.0F)) {
                            double minDamage = this.BMinDmg_value.getValue();
                            double maxSelf = this.BMaxSelf_value.getValue();
                            if (this.FacePlace_value.getValue() && this.canFacePlace(player, this.BlastHealth_value.getValue())) {
                                minDamage = 1.0D;
                            }

                            if (this.armorPlace_value.getValue() && this.canFaceArmorPlace(player, this.armorPlaceDmg_value.getValue())) {
                                minDamage = 1.0D;
                            }

                            double target = calculateDamage(vec3d.x, vec3d.y, vec3d.z, player, player.getPositionVector());
                            if (target > (double) (player.getHealth() + player.getAbsorptionAmount()) + minDamage - 2.0D && selfDamage < mc.player.getHealth() + mc.player.getAbsorptionAmount() && this.PopTotemTry.getValue()) {
                                return true;
                            }

                            if (!((double) selfDamage > maxSelf) && !(target < minDamage) && !((double) selfDamage > target)) {
                                return true;
                            }
                        }
                    }

                    return false;
                }
            } else {
                return true;
            }
        }
    }

    private void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
            rotating = false;
        }

    }

    private void ExplodeCrystal(EntityEnderCrystal crystal) {
        if (this.Rotate_value.getValue()) {
            this.lookAtCrystal(crystal);
        }

        if (this.breakTimer.passedMs(this.AttackSpeed_value.getValue())) {
            if (crystal != null) {
                String var2 = this.ExplodeHand_value.getValue().toString();
                byte var3 = -1;
                switch (var2.hashCode()) {
                    case -1911998296:
                        if (var2.equals("Packet")) {
                            var3 = 2;
                        }
                        break;
                    case 79183:
                        if (var2.equals("Off")) {
                            var3 = 0;
                        }
                        break;
                    case 2390489:
                        if (var2.equals("Main")) {
                            var3 = 1;
                        }
                }
                int newSlots = Wrapper.getPlayer().inventory.currentItem;
                if (weak.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                    int newSlot = -1;
                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = Wrapper.getPlayer().inventory.getStackInSlot(i);
                        if (stack == ItemStack.EMPTY) {
                            continue;
                        }
                        if ((stack.getItem() instanceof ItemSword)) {
                            newSlot = i;
                            break;
                        }
                        if ((stack.getItem() instanceof ItemTool)) {
                            newSlot = i;
                            break;
                        }
                    }
                    if (newSlot != -1) {
                        Wrapper.getPlayer().inventory.currentItem = newSlot;
                    }
                }
                switch (var3) {
                    case 0:
                        mc.playerController.attackEntity(mc.player, crystal);
                        mc.player.swingArm(EnumHand.OFF_HAND);
                        break;
                    case 1:
                        mc.playerController.attackEntity(mc.player, crystal);
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        break;
                    case 2:
                        this.packetBreakCrystal(crystal.entityId);

                }

                if (weak.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS) && newSlots != -1) {
                    Wrapper.getPlayer().inventory.currentItem = newSlots;
                }


                mc.player.resetCooldown();
                this.breakTimer.reset();
            }

            if (this.lastEntityId != -1 && this.canHitCrystal(mc.world.getEntityByID(this.lastEntityId))) {
                this.packetBreakCrystal(this.lastEntityId);
            }
        }

    }

    private void PlaceCrystal(BlockPos targetBlock, EnumHand enumHand) {
        EnumFacing facing;
        if (this.RayTrace_value.getValue()) {
            facing = enumFacing(targetBlock);
        } else {
            facing = EnumFacing.UP;
        }

        if (this.Rotate_value.getValue()) {
            this.lookAtPos(targetBlock, facing);
        }

        if (this.placeDelayRun(this.PlaceSpeed_value.getValue())) {

            blockRenderSmooth.updatePos(targetBlock);
            if (this.legitplace_value.getValue()) {
                mc.playerController.processRightClickBlock(mc.player, mc.world, targetBlock, EnumFacing.UP, new Vec3d(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ()), enumHand);
            } else {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetBlock, facing, enumHand, 0.0F, 0.0F, 0.0F));
            }
//            if (this.PrevSlot != -1 && (Boolean)this.GhostHand_value.getValue()) {
//                mc.player.inventory.currentItem = this.PrevSlot;
//                mc.playerController.updateController();
//            }
            this.placeTimer.reset();
            ++this.Placements;
        }

    }

    private boolean placeDelayRun(double speed) {
        return this.placeTimer.passedMs(1000.0D / speed);
    }

    private void packetBreakCrystal(int crystalEntityID) {
        CPacketUseEntity cPacketUseEntity = new CPacketUseEntity();
        cPacketUseEntity.entityId = crystalEntityID;
        cPacketUseEntity.action = CPacketUseEntity.Action.ATTACK;
        mc.player.connection.sendPacket(cPacketUseEntity);
        this.breakTimer.reset();
    }

    private List<BlockPos> findCrystalBlocks(double range) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(BlockInteractionHelper.getSphere(this.getPlayerPos(), (float) range, (int) range, false, true, 0).stream().filter((v) -> {
            return this.canPlaceCrystal(v, this.NewPlace_value.getValue());
        }).collect(Collectors.toList()));
        return positions;
    }

    private void drawBlock(BlockPos blockPos, int color) {
//        RenderUtil.prepare(7);
//        if (!this.RenderMode_value.getMode("NoRender").isToggled()) {
//            if (!this.RenderMode_value.getMode("Solid").isToggled() && !this.RenderMode_value.getMode("Up").isToggled()) {
//                if (this.RenderMode_value.getMode("Full").isToggled()) {
//                    RenderUtil.drawFullBox(blockPos, (float)(Integer)this.Width_value.getValue(), color);
//                } else if (this.RenderMode_value.getMode("Outline").isToggled()) {
//                    RenderUtil.drawBoundingBox(blockPos, 2.0F, color);
//                } else {
//                    RenderUtil.drawBoundingBox(blockPos.add(0, 1, 0), 2.0F, color);
//                }
//            } else if (this.RenderMode_value.getMode("Up").isToggled()) {
//                RenderUtil.drawBox(blockPos, color, 2);
//            } else {
//                RenderUtil.drawBox(blockPos, color, 63);
//            }
//        }
//
//        RenderUtil.release();
//        if ((Boolean)this.RenderDmg_value.getValue() && this.renderBlockDmg.containsKey(blockPos)) {
//            GlStateManager.pushMatrix();
//            this.glBillboardDistanceScaled((float)blockPos.getX() + 0.5F, (float)blockPos.getY() + 0.5F, (float)blockPos.getZ() + 0.5F, mc.player);
//            double damage = (Double)this.renderBlockDmg.get(blockPos);
//            String damageText = " " + (Math.floor(damage) == damage ? (int)damage : String.format("%.1f", damage)) + ((Boolean)this.Percen_value.getValue() ? " %" : "");
//            GlStateManager.disableDepth();
//            GlStateManager.translate(-((double)fontRenderer.getStringWidth(damageText) / 2.0D), 0.0D, 0.0D);
//            this.font.drawStringWithShadow(damageText, 0.0D, 0.0D, -1);
//            GlStateManager.popMatrix();
//        }

    }

    public void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player) {
        this.glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = (float) distance / 2.0F / 3.0F;
        if (scaleDistance < 1.0F) {
            scaleDistance = 1.0F;
        }

        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public void glBillboard(float x, float y, float z) {
        float scale = 0.02666667F;
        GlStateManager.translate((double) x - Minecraft.getMinecraft().getRenderManager().renderPosX, (double) y - Minecraft.getMinecraft().getRenderManager().renderPosY, (double) z - Minecraft.getMinecraft().getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-Minecraft.getMinecraft().player.rotationYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(Minecraft.getMinecraft().player.rotationPitch, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-scale, -scale, scale);
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public boolean canFacePlace(EntityLivingBase target, double blast) {
        float healthTarget = target.getHealth() + target.getAbsorptionAmount();
        return (double) healthTarget <= blast;
    }

    public boolean canFaceArmorPlace(EntityPlayer target, double blast) {
        Iterator var4 = target.inventory.armorInventory.iterator();

        ItemStack piece;
        do {
            if (!var4.hasNext()) {
                return false;
            }

            piece = (ItemStack) var4.next();
            if (piece == null) {
                return true;
            }
        } while ((double) ItemUtil.getItemDamage(piece) >= blast);

        return true;
    }

    public boolean canPlaceCrystal(BlockPos blockPos, boolean newPlace) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
            return false;
        } else {
            if (!newPlace) {
                if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR) {
                    return false;
                }
            } else if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR) {
                return false;
            }

            AxisAlignedBB b1 = new AxisAlignedBB(boost);
            AxisAlignedBB b2 = new AxisAlignedBB(boost2);
            if (this.shouldIgnoreEntity && !(Boolean) this.MultiPlace_value.getValue()) {
                return mc.world.getEntitiesWithinAABB(EntityItem.class, b1).isEmpty() && mc.world.getEntitiesWithinAABB(EntityPlayer.class, b1).isEmpty() && mc.world.getEntitiesWithinAABB(EntityPlayer.class, b2).isEmpty() && mc.world.getEntitiesWithinAABB(EntityArrow.class, b1).isEmpty();
            } else {
                return mc.world.getEntitiesWithinAABB(Entity.class, b1).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, b2).isEmpty();
            }
        }
    }

    public static double getRange(Vec3d a, double x, double y, double z) {
        double xl = a.x - x;
        double yl = a.y - y;
        double zl = a.z - z;
        return Math.sqrt(xl * xl + yl * yl + zl * zl);
    }

    public void lookAtPos(BlockPos block, EnumFacing face) {
        float[] v = RotationUtil.getRotationsBlock(block, face, false);
        float[] v2 = RotationUtil.getRotationsBlock(block.add(0.0D, 0.5D, 0.0D), face, false);
        this.setYawAndPitch(v[0], v[1], v2[1]);
    }

    public void lookAtCrystal(EntityEnderCrystal ent) {
        float[] v = RotationUtil.getRotations(mc.player.getPositionEyes(mc.getRenderPartialTicks()), ent.getPositionVector());
        float[] v2 = RotationUtil.getRotations(mc.player.getPositionEyes(mc.getRenderPartialTicks()), ent.getPositionVector().add(0.0D, -0.5D, 0.0D));
        this.setYawAndPitch(v[0], v[1], v2[1]);

    }

    public void setYawAndPitch(float yaw1, float pitch1, float renderPitch1) {
        yaw = yaw1;
        pitch = pitch1;
        renderPitch = renderPitch1;
        isSpoofingAngles = true;
        rotating = true;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity, Vec3d vec) {
        float doubleExplosionSize = 12.0F;
        double distanceSize = getRange(vec, posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distanceSize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));
        double finalValue = 1.0D;
        if (entity instanceof EntityLivingBase) {
            finalValue = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finalValue;
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        Vec3d offset = new Vec3d(entity.posX, entity.posY, entity.posZ);
        return calculateDamage(posX, posY, posZ, entity, offset);
    }

    private static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        try {
            if (entity instanceof EntityPlayer) {
                EntityPlayer ep = (EntityPlayer) entity;
                DamageSource ds = DamageSource.causeExplosionDamage(explosion);
                damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
                float f = MathHelper.clamp((float) k, 0.0F, 20.0F);
                damage *= 1.0F - f / 25.0F;
                if (entity.isPotionActive(MobEffects.RESISTANCE)) {
                    damage -= damage / 5.0F;
                }

                damage = Math.max(damage, 0.0F);
                return damage;
            } else {
                damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                return damage;
            }
        } catch (Exception var7) {
            return getBlastReduction(entity, damage, explosion);
        }
    }

    private static float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0.0F : (diff == 2 ? 1.0F : (diff == 1 ? 0.5F : 1.5F)));
    }

    public static EnumFacing enumFacing(BlockPos blockPos) {
        EnumFacing[] values;
        int length = (values = EnumFacing.values()).length;

        for (int i = 0; i < length; ++i) {
            EnumFacing enumFacing = values[i];
            Vec3d vec3d = new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ);
            Vec3d vec3d2 = new Vec3d(blockPos.getX() + enumFacing.getDirectionVec().getX(), blockPos.getY() + enumFacing.getDirectionVec().getY(), blockPos.getZ() + enumFacing.getDirectionVec().getZ());
            RayTraceResult rayTraceBlocks;
            if ((rayTraceBlocks = mc.world.rayTraceBlocks(vec3d, vec3d2, false, true, false)) != null && rayTraceBlocks.typeOfHit.equals(RayTraceResult.Type.BLOCK) && rayTraceBlocks.getBlockPos().equals(blockPos)) {
                return enumFacing;
            }
        }

        if ((double) blockPos.getY() > mc.player.posY + (double) mc.player.getEyeHeight()) {
            return EnumFacing.DOWN;
        } else {
            return EnumFacing.UP;
        }
    }

    public static boolean IsEating() {
        return mc.player != null && (mc.player.getHeldItemMainhand().getItem() instanceof ItemFood || mc.player.getHeldItemOffhand().getItem() instanceof ItemFood) && mc.player.isHandActive();
    }

    public static boolean CanSeeBlock(BlockPos p_Pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(p_Pos.getX(), (double) p_Pos.getY() + 1.7D, p_Pos.getZ()), false, true, false) == null;
    }

    public static double getVecDistance(BlockPos a, double posX, double posY, double posZ) {
        double x1 = (double) a.getX() - posX;
        double y1 = (double) a.getY() - posY;
        double z1 = (double) a.getZ() - posZ;
        return Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
    }

    @SubscribeEvent
    public void doAutoCrystal(TickEvent.RenderTickEvent event) {
        if (mc.player != null && mc.world != null) {
            if (this.placeTimer.passedMs(1050.0D) || this.breakTimer.passedMs(1050.0D)) {
                rotating = false;
            }

            if (!(Boolean) this.PauseEating_value.getValue() || !IsEating()) {
                this.shouldIgnoreEntity = false;
                this.c = mc.world.loadedEntityList.stream().filter(this::canHitCrystal).map((e) -> (EntityEnderCrystal) e).min(Comparator.comparing((e) -> mc.player.getDistance(e))).orElse(null);
                if (this.Explode_value.getValue() && this.c != null) {
                    if (mc.player.canEntityBeSeen(this.c) || (double) mc.player.getDistance(this.c) < this.WallRange_value.getValue() && this.Wall_value.getValue() || !(Boolean) this.Wall_value.getValue()) {
                        this.ExplodeCrystal(this.c);
                        this.shouldIgnoreEntity = !(Boolean) this.MultiPlace_value.getValue() && (this.shouldIgnoreEntity = this.EntityIgnore_value.getValue());
                        if (this.Placements >= 2) {
                            this.Placements = 0;
                            if (this.shouldIgnoreEntity == this.EntityIgnore_value.getValue()) {
                                return;
                            }

                            this.shouldIgnoreEntity = this.EntityIgnore_value.getValue();
                        }
                    }
                } else {
                    this.resetRotation();
                    if (this.oldSlot != -1) {
                        mc.player.inventory.currentItem = this.oldSlot;
                        this.oldSlot = -1;
                    }
                }


                int crystalSlot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : -1;
                if (crystalSlot == -1) {
                    for (int l = 0; l < 9; ++l) {
                        if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                            crystalSlot = l;
                            break;
                        }
                    }
                }

                boolean offhand = false;
                if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                    offhand = true;
                } else if (crystalSlot == -1 && !GhostHand_value.getValue()) {
                    return;
                }
                if (updateTimer.passedMs(10)) {
                    try {
                        this.crystalTarget = this.Calculator();
                    } catch (Exception e) {
                        Command.sendMessage("[AntiCrash] I've prevented you from a crash!");
                    }

                }
                if (crystalTarget != null) {
                    target = crystalTarget.target;
                    BlockPos targetBlock = crystalTarget.blockPos;
                    if (target == null) {
                        this.renderBlock = null;
                        this.resetRotation();
                    } else {
                        this.renderBlock = targetBlock;
                        
                        handleAutoGG: {
                            AutoGG autoGG = ModuleManager.getModuleByClass(AutoGG.class);
                            if (autoGG == null) break handleAutoGG;
                            if (autoGG.isEnabled()) {
                                autoGG.addTargetedPlayer(target.getName());
                            }
                        }

                        if (this.Place_value.getValue()) {
                            EnumHand enumHand = offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
                            if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                                if (this.AutoSwitch_value.getValue()) {
                                    mc.player.inventory.currentItem = crystalSlot;
                                    this.resetRotation();
                                    this.switchCoolDown = true;
                                }
                                if (!GhostHand_value.getValue()) {
                                    return;
                                }
                            }
                            if (this.switchCoolDown) {
                                this.switchCoolDown = false;
                                return;
                            }

                            int inft = -1;
                            if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && GhostHand_value.getValue() && !AutoSwitch_value.getValue() && findCrystal() != -1) {
                                inft = mc.player.inventory.currentItem;
                                mc.player.inventory.currentItem = findCrystal();
                                mc.playerController.updateController();
                            }
                            this.PlaceCrystal(targetBlock, enumHand);
                            if (inft != -1 && GhostHand_value.getValue()) {
                                mc.player.inventory.currentItem = inft;
                                mc.playerController.updateController();
                            }
                        }
                        if (this.Rotate_value.getValue() && isSpoofingAngles) {
                            EntityPlayerSP sp = mc.player;
                            sp.rotationPitch = (float) ((double) sp.rotationPitch - 4.0E-4D);
                            this.togglePitch = !this.togglePitch;
                        }
                    }
                }
            }
        }
    }

    private static int findCrystal() {
        for (int l = 0; l < 9; ++l) {
            if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                return l;
            }
        }
        return -1;
    }

    
    private CrystalTarget Calculator() {
        if (mc.player == null || mc.world == null) {
            return null;
        }
        double damage = 0.5D;
        BlockPos tempBlock = null;
        Entity target = null;
        this.PlacePosList = this.findCrystalBlocks(this.PlaceRange_value.getValue());
        List<Entity> entities = this.getEntities();
        this.PrevSlot = -1;
        Iterator<Entity> var6 = entities.iterator();

        label106:
        while (true) {
            Entity entity2;
            do {
                do {
                    if (!var6.hasNext()) {
                        return new CrystalTarget(tempBlock, target);
                    }
                    entity2 = var6.next();
                } while (entity2 == mc.player);
            } while (((EntityLivingBase) entity2).getHealth() <= 0.0F);

            Vec3d targetVec = new Vec3d(entity2.posX, entity2.posY, entity2.posZ);
            Vec3d interpolatedPos = EntityUtils.getInterpolatedPos(entity2, (float) this.PrediteNumber_value.getValue());
            BlockPos pos = new BlockPos(interpolatedPos.x, interpolatedPos.y, interpolatedPos.z);
            if (pos != null && this.canPlaceCrystal(pos, false)) {
                this.PlacePosList.add(pos);
            }

            Iterator<BlockPos> var11 = this.PlacePosList.iterator();

            while (true) {
                BlockPos blockPos;
                double targetDamage;
                double selfDamage;
                do {
                    float healthSelf;
                    do {
                        float healthTarget;
                        do {
                            do {
                                do {
                                    do {
                                        do {
                                            do {
                                                if (!var11.hasNext()) {
                                                    continue label106;
                                                } else {
                                                    blockPos = var11.next();
                                                }

                                            } while (blockPos != null && entity2.getDistanceSq(blockPos) >= this.Distance_value.getValue() * this.Distance_value.getValue());
                                        } while (blockPos != null && mc.player.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > this.PlaceRange_value.getValue());
                                        targetDamage = calculateDamage(Math.floor(blockPos.getX()) + 0.5D, Math.floor(blockPos.getY()) + 1.0D, Math.floor(blockPos.getZ()) + 0.5D, entity2, targetVec);
                                    } while (targetDamage < damage);
                                } while (blockPos != null && targetDamage < (this.FacePlace_value.getValue() ? (this.canFacePlace((EntityLivingBase) entity2, this.BlastHealth_value.getValue()) ? 1.0D : this.MinDmg_value.getValue()) : this.MinDmg_value.getValue()));

                                healthTarget = ((EntityLivingBase) entity2).getHealth() + ((EntityLivingBase) entity2).getAbsorptionAmount();
                                healthSelf = mc.player.getHealth() + mc.player.getAbsorptionAmount();
                                selfDamage = calculateDamage(Math.floor(blockPos.getX()) + 0.5D, Math.floor(blockPos.getY()) + 1.0D, Math.floor(blockPos.getZ()) + 0.5D, mc.player, mc.player.getPositionVector());
                            } while (blockPos != null && selfDamage > targetDamage && targetDamage < (double) healthTarget);
                        } while (blockPos != null && selfDamage - 2.0D > (double) healthSelf);
                    } while (blockPos != null && selfDamage > this.MaxSelf_value.getValue());
                } while (blockPos != null && !(Boolean) this.Wall_value.getValue() && this.WallRange_value.getValue() > 0.0D && !CanSeeBlock(blockPos) && getVecDistance(blockPos, mc.player.posX, mc.player.posY, mc.player.posZ) >= this.WallRange_value.getValue());

                damage = targetDamage;
                tempBlock = new BlockPos(Math.floor(blockPos.getX()), blockPos.getY(), Math.floor(blockPos.getZ()));
                target = entity2;
                if (this.RenderDmg_value.getValue()) {
                    this.renderBlockDmg.put(tempBlock, targetDamage);
                }
            }
        }
    }

    private void GhostHand() {
        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = mc.player.inventory.getStackInSlot(i);
                if (stack != ItemStack.EMPTY && stack.getItem() == Items.END_CRYSTAL) {
                    this.PrevSlot = mc.player.inventory.currentItem;
                    mc.player.inventory.currentItem = i;
                    mc.playerController.updateController();
                }
            }
        } else {
            PrevSlot = -1;
        }

    }

    @NotNull
    private List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        if (this.TargetPlayer_value.getValue()) {
            entities.addAll(mc.world.playerEntities.stream().filter((entityPlayer) -> {
                return !Stay.friendManager.isFriend(entityPlayer.getName());
            }).filter((entity) -> {
                return (double) mc.player.getDistance(entity) < this.Distance_value.getValue();
            }).collect(Collectors.toList()));
        }

        entities.addAll(mc.world.loadedEntityList.stream().filter((entity) -> {
            return EntityUtils.isLiving(entity) && EntityUtils.isPassive(entity) ? this.TargetAnimals_value.getValue() : this.TargetMobs_value.getValue();
        }).filter((entity) -> {
            return !(entity instanceof EntityPlayer);
        }).filter((entity) -> {
            return (double) mc.player.getDistance(entity) < this.Distance_value.getValue();
        }).collect(Collectors.toList()));

        for (Entity ite2 : new ArrayList<>(entities)) {
            if ((double) mc.player.getDistance(ite2) > this.Distance_value.getValue()) {
                entities.remove(ite2);
            }

            if (ite2 == mc.player) {
                entities.remove(ite2);
            }
        }

        entities.sort(Comparator.comparingDouble((entity) -> {
            return entity.getDistance(mc.player);
        }));
        return entities;
    }

    public int getColor() {
        float[] tick_color = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0F * this.RGBSpeed_value.getValue()};
        int color_rgb = Color.HSBtoRGB(tick_color[0], this.Saturation_value.getValue(), this.Brightness_value.getValue());
        return this.Rainbow_value.getValue() ? (new Color(color_rgb >> 16 & 255, color_rgb >> 8 & 255, color_rgb & 255, this.Alpha_value.getValue())).getRGB() : (new Color(this.Red_value.getValue(), this.Green_value.getValue(), this.Blue_value.getValue(), this.Alpha_value.getValue())).getRGB();
    }

    public void onEnable() {
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.updateTimer.reset();
        this.renderBlockDmg.clear();
        blockRenderSmooth = new BlockEasingRender(new BlockPos(0, 0, 0),
                moveTime.getValue().floatValue(),
                fadeTime.getValue().floatValue(),
                movingEasingType.getValue(),
                fadingEasingType.getValue());
        blockRenderSmooth.reset();
    }

    public void onDisable() {
        rotating = false;
        this.resetRotation();
        this.renderBlock = null;
        this.pos1 = null;
        target = null;
        yaw = mc.player.rotationYaw;
        pitch = mc.player.rotationPitch;
        this.PlacePosList.clear();
    }

    @NotNull
    @Override
    public String getDisplayInfo() {
        return target == null ? "Finding...." : target.getName();
    }
}
