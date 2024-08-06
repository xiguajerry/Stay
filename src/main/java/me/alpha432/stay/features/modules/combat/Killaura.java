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
import me.alpha432.stay.event.MotionUpdateEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.inventory.InventoryUtil;
import me.alpha432.stay.util.math.DamageUtil;
import me.alpha432.stay.util.math.MathUtil;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Killaura extends Module {
    private static Killaura INSTANCE = new Killaura();
    public static Entity target;
    private final Timer timer = new Timer();
    public Setting<Float> range = register(new Setting<>("Range", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F)));
    public Setting<Boolean> delay = register(new Setting<>("HitDelay", Boolean.valueOf(true)));
    public Setting<Boolean> rotate = register(new Setting<>("Rotate", Boolean.valueOf(true)));
    public Setting<Boolean> onlySharp = register(new Setting<>("SwordOnly", Boolean.valueOf(true)));
    public Setting<Float> raytrace = register(new Setting<>("Raytrace", Float.valueOf(6.0F), Float.valueOf(0.1F), Float.valueOf(7.0F), "Wall Range."));
    public Setting<Boolean> players = register(new Setting<>("Players", Boolean.valueOf(true)));
    public Setting<Boolean> mobs = register(new Setting<>("Mobs", Boolean.valueOf(false)));
    public Setting<Boolean> animals = register(new Setting<>("Animals", Boolean.valueOf(false)));
    public Setting<Boolean> vehicles = register(new Setting<>("Entities", Boolean.valueOf(false)));
    public Setting<Boolean> projectiles = register(new Setting<>("Projectiles", Boolean.valueOf(false)));
    public Setting<Boolean> tps = register(new Setting<>("TpsSync", Boolean.valueOf(true)));
    public Setting<Boolean> packet = register(new Setting<>("Packet", Boolean.valueOf(false)));
    public Setting<Boolean> Ghosthand = register(new Setting<>("Ghosthand", Boolean.valueOf(false)));

    public Killaura() {
        super("KillAura", "Kills aura.", Module.Category.COMBAT, true, false, false);
    }

    public void onTick() {
        if (!rotate.getValue().booleanValue())
            doKillaura();
    }


    public static Killaura getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Killaura();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void onMotionUpdateEvent(MotionUpdateEvent event) {
        if (event.getStage() == 0 && rotate.getValue().booleanValue())
            doKillaura();
    }

    private void doKillaura() {

        target = getTarget();
        if (target == null){
            return;}
        int wait = !delay.getValue().booleanValue() ? 0 : (int) (DamageUtil.getCooldownByWeapon(mc.player) * (tps.getValue().booleanValue() ? Stay.serverManager.getTpsFactor() : 1.0F));

        Stay.targetManager.updateTarget((EntityLivingBase) target);
        if(Ghosthand.getValue()){
            int sol = mc.player.inventory.currentItem;
            int det = InventoryUtil.getItemHotbar(Items.DIAMOND_SWORD);
            if(det==-1){
                return;
            }


            wait = !delay.getValue().booleanValue() ? 0 : (int) (600 * (tps.getValue().booleanValue() ? Stay.serverManager.getTpsFactor() : 1.0F));
            if (!timer.passedMs(wait)){
                return;}
            if (rotate.getValue().booleanValue()){
                Stay.rotationManager.lookAtEntity(target);}

            mc.player.inventory.currentItem=det;

            if (packet.getValue()) {
                mc.player.connection.sendPacket(new CPacketPlayer());
                EntityUtils.mc.player.connection.sendPacket(new CPacketUseEntity(target));
            } else {
                EntityUtils.mc.playerController.attackEntity(EntityUtils.mc.player, target);
            }
            EntityUtils.mc.player.swingArm(EnumHand.MAIN_HAND);

            mc.player.inventory.currentItem=sol;

            timer.reset();
            return;
        }
        if (onlySharp.getValue().booleanValue() && !EntityUtils.holdingWeapon(mc.player)) {
            target = null;
            return;
        }
         if (!timer.passedMs(wait))
            return;
        if (rotate.getValue().booleanValue())
            Stay.rotationManager.lookAtEntity(target);

            EntityUtils.attackEntity(target, packet.getValue().booleanValue(), true);



        timer.reset();
        return;
    }

    private Entity getTarget() {
        Entity target = null;
        double distance = range.getValue().floatValue();
        double maxHealth = 36.0D;
        for (Entity entity : mc.world.playerEntities) {
            if (((!players.getValue().booleanValue() || !(entity instanceof EntityPlayer)) && (!animals.getValue().booleanValue() || !EntityUtils.isPassive(entity)) && (!mobs.getValue().booleanValue() || !EntityUtils.isMobAggressive(entity)) && (!vehicles.getValue().booleanValue() || !EntityUtils.isVehicle(entity)) && (!projectiles.getValue().booleanValue() || !EntityUtils.isProjectile(entity))) || (entity instanceof net.minecraft.entity.EntityLivingBase &&
                    EntityUtils.isntValid(entity, distance)))
                continue;
            if (!mc.player.canEntityBeSeen(entity) && !EntityUtils.canEntityFeetBeSeen(entity) && mc.player.getDistanceSq(entity) > MathUtil.square(raytrace.getValue().floatValue()))
                continue;
            if (target == null) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtils.getHealth(entity);
                continue;
            }
            if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer) entity, 18)) {
                target = entity;
                break;
            }
            if (mc.player.getDistanceSq(entity) < distance) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtils.getHealth(entity);
            }
            if (EntityUtils.getHealth(entity) < maxHealth) {
                target = entity;
                distance = mc.player.getDistanceSq(entity);
                maxHealth = EntityUtils.getHealth(entity);
            }
        }
        return target;
    }

    public String getDisplayInfo() {
        if (target instanceof EntityPlayer)
            return target.getName();
        return null;
    }
}
