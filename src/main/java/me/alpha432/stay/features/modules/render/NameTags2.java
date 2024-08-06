/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemShield
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 */
package me.alpha432.stay.features.modules.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;

import java.util.Iterator;
import java.util.Objects;

public class NameTags2
extends Module {
    private final Setting<Boolean> invisibles = this.register(new Setting<>("Invisibles", true));

    public NameTags2() {
        super("NameTags2", "Better Nametags", Module.Category.VISUAL, false, false, false);
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (NameTags2.fullNullCheck()) return;
        Iterator iterator = NameTags2.mc.world.playerEntities.iterator();
        while (iterator.hasNext()) {
            EntityPlayer player = (EntityPlayer)iterator.next();
            if ((double)player.getDistance((Entity) NameTags2.mc.player) > 30.0 || player == null || player.equals((Object) NameTags2.mc.player) || !player.isEntityAlive() || player.isInvisible() && !this.invisibles.getValue().booleanValue()) continue;
            double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - NameTags2.mc.getRenderManager().viewerPosX;
            double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - NameTags2.mc.getRenderManager().viewerPosY;
            double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - NameTags2.mc.getRenderManager().viewerPosZ;
            this.renderNameTag(player, x, y, z, event.getPartialTicks());
        }
    }

    private void renderNameTag(EntityPlayer player, double x, double y, double z, float delta) {
        double tempY = y;
        tempY += player.isSneaking() ? 0.5 : 0.7;
        Entity camera = mc.getRenderViewEntity();
        assert (camera != null);
        double originalPositionX = camera.posX;
        double originalPositionY = camera.posY;
        double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        String displayTag = this.getDisplayTag(player);
        double distance = camera.getDistance(x + NameTags2.mc.getRenderManager().viewerPosX, y + NameTags2.mc.getRenderManager().viewerPosY, z + NameTags2.mc.getRenderManager().viewerPosZ);
        int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = distance <= 6.0 ? 0.0245 : (0.0018 + 4.5 * (distance * 1.0)) / 1000.0;
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset((float)1.0f, (float)-1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)((float)x), (float)((float)tempY + 1.4f), (float)((float)z));
        GlStateManager.rotate((float)(-NameTags2.mc.getRenderManager().playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float) NameTags2.mc.getRenderManager().playerViewX, (float)(NameTags2.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f), (float)0.0f, (float)0.0f);
        GlStateManager.scale((double)(-scale), (double)(-scale), (double)scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        RenderUtil.drawRect(-width - 2, -(this.renderer.getFontHeight() + 1), (float)width + 2.0f, 1.5f, 0x55000000);
        GlStateManager.disableBlend();
        ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        GlStateManager.pushMatrix();
        int xOffset = -8;
        for (ItemStack stack : player.inventory.armorInventory) {
            if (stack == null) continue;
            xOffset -= 8;
        }
        ItemStack renderOffhand = player.getHeldItemOffhand().copy();
        this.renderItemStack(renderOffhand, xOffset -= 8, -26);
        xOffset += 16;
        Iterator iterator = player.inventory.armorInventory.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.renderItemStack(renderMainHand, xOffset, -26);
                GlStateManager.popMatrix();
                this.renderer.drawStringWithShadow(displayTag, -width, -(this.renderer.getFontHeight() - 1), this.getDisplayColour(player));
                camera.posX = originalPositionX;
                camera.posY = originalPositionY;
                camera.posZ = originalPositionZ;
                GlStateManager.enableDepth();
                GlStateManager.disableBlend();
                GlStateManager.disablePolygonOffset();
                GlStateManager.doPolygonOffset((float)1.0f, (float)1500000.0f);
                GlStateManager.popMatrix();
                return;
            }
            ItemStack stack2 = (ItemStack)iterator.next();
            if (stack2 == null) continue;
            ItemStack armourStack = stack2.copy();
            this.renderItemStack(armourStack, xOffset, -26);
            xOffset += 16;
        }
    }

    private void renderItemStack(ItemStack stack, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        NameTags2.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(NameTags2.mc.fontRenderer, stack, x, y);
        NameTags2.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale((float)0.5f, (float)0.5f, (float)0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x, y);
        GlStateManager.enableDepth();
        GlStateManager.scale((float)2.0f, (float)2.0f, (float)2.0f);
        GlStateManager.popMatrix();
    }

    private void renderEnchantmentText(ItemStack stack, int x, int y) {
        int enchantmentY = y - 8;
        if (stack.getItem() == Items.GOLDEN_APPLE && stack.hasEffect()) {
            this.renderer.drawStringWithShadow("God", x * 2, enchantmentY, -3977919);
            enchantmentY -= 8;
        }
        if (!(stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool)) {
            if (!(stack.getItem() instanceof ItemShield)) return;
        }
        int dmg = 0;
        float green = ((float)stack.getMaxDamage() - (float)stack.getItemDamage()) / (float)stack.getMaxDamage();
        float red = 1.0f - green;
        dmg = 100 - (int)(red * 100.0f);
        ChatFormatting color = dmg >= 60 ? ChatFormatting.GREEN : (dmg >= 25 ? ChatFormatting.YELLOW : ChatFormatting.RED);
        this.renderer.drawStringWithShadow(color + "" + dmg + "%", x * 2, enchantmentY, -1);
    }

    private String getDisplayTag(EntityPlayer player) {
        float health;
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(mc.getSession().getUsername())) {
            name = "You";
        }
        ChatFormatting color = (health = player.getHealth() + player.getAbsorptionAmount()) > 18.0f ? ChatFormatting.GREEN : (health > 16.0f ? ChatFormatting.DARK_GREEN : (health > 12.0f ? ChatFormatting.YELLOW : (health > 8.0f ? ChatFormatting.GOLD : (health > 5.0f ? ChatFormatting.RED : ChatFormatting.DARK_RED))));
        String pingStr = "";
        try {
            int responseTime = Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
            pingStr = pingStr + responseTime + "ms ";
        }
        catch (Exception exception) {
            // empty catch block
        }
        if (Math.floor(health) == (double)health) {
            name = name + color + ' ' + (health > 0.0f ? Integer.valueOf((int)Math.floor(health)) : "dead");
            return pingStr + name;
        }
        name = name + color + ' ' + (health > 0.0f ? Integer.valueOf((int)health) : "dead");
        return pingStr + name;
    }

    private int getDisplayColour(EntityPlayer player) {
        int colour = -1;
        if (Stay.friendManager.isFriend(player)) {
            return -11157267;
        }
        if (player.isInvisible()) {
            return -1113785;
        }
        if (!player.isSneaking()) return colour;
        return -6481515;
    }

    private double interpolate(double previous, double current, float delta) {
        return previous + (current - previous) * (double)delta;
    }
}

