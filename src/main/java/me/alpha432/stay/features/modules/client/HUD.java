/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.ClientEvent;
import me.alpha432.stay.event.Render2DEvent;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.setting.Setting;
import me.alpha432.stay.manager.ModuleManager;
import me.alpha432.stay.util.counting.Timer;
import me.alpha432.stay.util.graphics.opengl.RenderUtil;
import me.alpha432.stay.util.graphics.color.ColorUtil;
import me.alpha432.stay.util.math.MathUtil;
import me.alpha432.stay.util.text.TextUtil;
import me.alpha432.stay.util.world.EntityUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class HUD extends Module {
    private static final ResourceLocation box = new ResourceLocation("textures/gui/container/shulker_box.png");
    private static final ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);
    private static final ItemStack Crystal = new ItemStack(Items.END_CRYSTAL);
    private static final ItemStack xp = new ItemStack(Items.EXPERIENCE_BOTTLE);
    private static final ItemStack ap = new ItemStack(Items.GOLDEN_APPLE);
    private static HUD INSTANCE = new HUD();
    private final Setting<Boolean> grayNess = register(new Setting<>("Gray", Boolean.TRUE));
    private final Setting<Boolean> renderingUp = register(new Setting<>("RenderingUp", Boolean.TRUE, "Orientation of the HUD-Elements."));
    private final Setting<Boolean> waterMark = register(new Setting<>("Watermark", Boolean.TRUE, "displays watermark"));
    private final Setting<Boolean> waterMark2 = register(new Setting<>("Watermark2", Boolean.FALSE, "displays watermark"));
    private final Setting<Boolean> waterMark3 = register(new Setting<>("slolwatermark", Boolean.FALSE, "displays watermark"));
    public Setting<Integer> waterMarkY = register(new Setting<>("WatermarkPosY", 2, 0, 20, v -> waterMark.getValue()));
    public Setting<Integer> waterMark2Y = register(new Setting<>("Watermark2PosY", 2, 0, 100, v -> waterMark2.getValue()));
    public Setting<Integer> waterMark3Y = register(new Setting<>("slolwatermarkY", 2, 0, 100, v -> waterMark3.getValue()));
    private final Setting<Boolean> arrayList = register(new Setting<>("ActiveModules", Boolean.TRUE, "Lists the active modules."));
    private final Setting<Integer> arrayListBackGroundAlpha = register(new Setting<>("BackGroundAlpha", 127, 0, 255));
    private final Setting<Boolean> pvp = register(new Setting<>("PvpInfo", true));
    private final Setting<Boolean> hitMarkers = this.register(new Setting<>("HitMarkers", true));
    private final Setting<Boolean> coords = register(new Setting<>("Coords", Boolean.TRUE, "Your current coordinates"));
    private final Setting<Boolean> direction = register(new Setting<>("Direction", Boolean.TRUE, "The Direction you are facing."));
    private final Setting<Boolean> armor = register(new Setting<>("Armor", Boolean.TRUE, "ArmorHUD"));
    private final Setting<Boolean> totems = register(new Setting<>("Totems", Boolean.TRUE, "TotemHUD"));
    private final Setting<Boolean> Crystalsss = register(new Setting<>("Crystal", Boolean.TRUE, "CrystalHUD"));
    private final Setting<Boolean> APSSS = register(new Setting<>("APPLE", Boolean.TRUE, "APPLEHUD"));
    private final Setting<Boolean> XPSSS = register(new Setting<>("XP", Boolean.TRUE, "XPHUD"));
    private final Setting<Integer> insss = register(new Setting<>("distance", 16, 0, 100));
    private final Setting<Boolean> greeter = register(new Setting<>("Welcomer", Boolean.TRUE, "The time"));
    private final Setting<Boolean> speed = register(new Setting<>("Speed", Boolean.TRUE, "Your Speed"));
    private final Setting<Boolean> potions = register(new Setting<>("Potions", Boolean.TRUE, "Your Speed"));
    private final Setting<Boolean> ping = register(new Setting<>("Ping", Boolean.TRUE, "Your response time to the server."));
    private final Setting<Boolean> tps = register(new Setting<>("TPS", Boolean.TRUE, "Ticks per second of the server."));
    private final Setting<Boolean> fps = register(new Setting<>("FPS", Boolean.TRUE, "Your frames per second."));
    private final Setting<Boolean> lag = register(new Setting<>("LagNotifier", Boolean.TRUE, "The time"));
    private final Setting<Boolean> Friends = register(new Setting<>("Friends", Boolean.FALSE, "Friends"));
    private final Setting<Boolean> customFont = register(new Setting<>("Custom Font", Boolean.FALSE,  v -> Friends.getValue()));
    public Setting<Integer> Friendsx = register(new Setting<>("Friendsx", 10, 0, 1000, v -> Friends.getValue()));
    public Setting<Integer> Friendsy = register(new Setting<>("Friendsy", 72, 0, 1000, v -> Friends.getValue()));
    private final Timer timer = new Timer();
    private final Map<String, Integer> players = new HashMap<>();
    public Setting<String> command = register(new Setting<>("Command", "Stay"));
    public Setting<TextUtil.Color> bracketColor = register(new Setting<>("BracketColor", TextUtil.Color.RED));
    public Setting<TextUtil.Color> commandColor = register(new Setting<>("NameColor", TextUtil.Color.GRAY));
    public Setting<Boolean> rainbowPrefix = this.register(new Setting<>("RainbowPrefix", true));
    public Setting<Integer> rainbowSpeed = this.register(new Setting<>("PrefixSpeed", 20, 0, 100, v -> this.rainbowPrefix.getValue()));
    public Setting<String> commandBracket = register(new Setting<>("Bracket", "["));
    public Setting<String> commandBracket2 = register(new Setting<>("Bracket2", "]"));
    public Setting<Boolean> notifyToggles = register(new Setting<>("ChatNotify", Boolean.TRUE, "notifys in chat"));
    public Setting<Boolean> magenDavid = register(new Setting<>("MagenDavid", Boolean.TRUE, "draws magen david"));
    public Setting<Integer> animationHorizontalTime = register(new Setting<>("AnimationHTime", 500, 1, Integer.valueOf(1000), v -> arrayList.getValue().booleanValue()));
    public Setting<Integer> animationVerticalTime = register(new Setting<>("AnimationVTime", Integer.valueOf(50), Integer.valueOf(1), Integer.valueOf(500), v -> arrayList.getValue().booleanValue()));
    public Setting<RenderingMode> renderingMode = register(new Setting<>("Ordering", RenderingMode.Length));
    public Setting<Boolean> time = register(new Setting<>("Time", Boolean.TRUE, "The time"));
    public Setting<Integer> lagTime = register(new Setting<>("LagTime", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(2000)));

    private int color;
    public float hue;
    private boolean shouldIncrement;
    private int hitMarkerTimer;

    public HUD() {
        super("HUD", "HUD Elements rendered on your screen", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static HUD getInstance() {
        if (INSTANCE == null)
            INSTANCE = new HUD();
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public void onUpdate() {
        if (shouldIncrement)
            hitMarkerTimer++;
        if (hitMarkerTimer == 10) {
            hitMarkerTimer = 0;
            shouldIncrement = false;
        }
    }
    private void drawString(String string, int x, int y) {
        if (this.customFont.getValue()) {
           Stay.GUI_FONT_MANAGER.drawStringWithShadow(string, x, y, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB());
        } else {
            mc.fontRenderer.drawStringWithShadow(string, x, y, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB());
        }
    }


    private void renderFriends() {
        List<String> friends = new ArrayList<>();
        for (EntityPlayer player : mc.world.playerEntities) {
            if(Stay.friendManager.isFriend(player.getName())) {
                friends.add(player.getName());
            }
        }
        int y = Friendsy.getValue();
        if (friends.isEmpty()) {
            drawString(ChatFormatting.BOLD + "You have no friends on this server", Friendsx.getValue(), y);
        } else {
            drawString(ChatFormatting.BOLD + "The_Friends_list", Friendsx.getValue(), y);
            y += 12;
            for (String friend : friends) {
                drawString(friend, Friendsx.getValue(), y);
                y += 12;
            }
        }
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        if (fullNullCheck())
            return;
        betaText();
        int width = renderer.scaledWidth;
        int height = renderer.scaledHeight;
        color = ColorUtil.toRGBA((ClickGui.getInstance()).red.getValue().intValue(), (ClickGui.getInstance()).green.getValue().intValue(), (ClickGui.getInstance()).blue.getValue().intValue());
        if (waterMark.getValue().booleanValue()) {
            String string = command.getPlannedValue() + " "+ Stay.VERSION;
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(string, 2.0F, waterMarkY.getValue().intValue(), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = { 1 };
                    char[] stringToCharArray = string.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, waterMarkY.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(string, 2.0F, waterMarkY.getValue().intValue(), color, true);
            }
        }
        if(pvp.getValue()) {
            renderPvpInfo();
        }
        if (Friends.getValue()) {
            renderFriends();
        }
        if (waterMark2.getValue().booleanValue()) {
            String string = "Stay cool person edition";
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(string, 2.0F, waterMark2Y.getValue().intValue(), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = { 1 };
                    char[] stringToCharArray = string.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, waterMark2Y.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(string, 2.0F, waterMark2Y.getValue().intValue(), color, true);
            }
        }

        if (waterMark3.getValue().booleanValue()) {
            String string = "Bright in China";
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(string, 2.0F, waterMark3Y.getValue().intValue(), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = { 1 };
                    char[] stringToCharArray = string.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, waterMark3Y.getValue().intValue(), ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(string, 2.0F, waterMark3Y.getValue().intValue(), color, true);
            }
        }

        int[] counter1 = {1};
        int j = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && !renderingUp.getValue().booleanValue()) ? 14 : 0;
        if (arrayList.getValue().booleanValue())
            if (renderingUp.getValue()) {
                if (renderingMode.getValue() == RenderingMode.ABC) {
                    for (int k = 0; k < Stay.moduleManager.sortedModulesABC.size(); k++) {
                        String str = Stay.moduleManager.sortedModulesABC.get(k);
                        Gui.drawRect((width - 6 - renderer.getStringWidth(str)), (2 + j * 10), width, (2 + j * 10 + 1) + renderer.getFontHeight(), new Color(0, 0, 0, arrayListBackGroundAlpha.getValue()).getRGB());
                        renderer.drawString(str, (width - 4 - renderer.getStringWidth(str)), (2 + j * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue()).getRGB()) : color, true);
                        Gui.drawRect(
                                width - 2,
                                (2 + j * 10 - 1),
                                width,
                                (2 + j * 10 + 1) + renderer.getFontHeight(),
                                (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue()).getRGB()) : color);
                        j++;
                        counter1[0] = counter1[0] + 1;
                    }
                } else {
                    for (int k = 0; k < Stay.moduleManager.sortedModules.size(); k++) {
                        Module module = Stay.moduleManager.sortedModules.get(k);
                        String str = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                        Gui.drawRect((width - 6 - renderer.getStringWidth(str)), (2 + j * 10), width, (2 + j * 10 + 1) + renderer.getFontHeight(), new Color(0, 0, 0, arrayListBackGroundAlpha.getValue()).getRGB());
                        renderer.drawString(str, (width - 4 - renderer.getStringWidth(str)), (2 + j * 10), (ClickGui.getInstance()).rainbow.getValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                        Gui.drawRect(
                                width - 2,
                                (2 + j * 10 - 1),
                                width,
                                (2 + j * 10 + 1) + renderer.getFontHeight(),
                                (ClickGui.getInstance()).rainbow.getValue()
                                        ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up)
                                        ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB()
                                        : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue()).getRGB())
                                        : color);
                        j++;
                        counter1[0] = counter1[0] + 1;
                    }
                }
            } else if (renderingMode.getValue() == RenderingMode.ABC) {
                for (int k = 0; k < Stay.moduleManager.sortedModulesABC.size(); k++) {
                    String str = Stay.moduleManager.sortedModulesABC.get(k);
                    j += 10;
                    Gui.drawRect((width - 6 - renderer.getStringWidth(str)), (2 + j * 10), width, (2 + j * 10) + renderer.getFontHeight(), new Color(0, 0, 0, arrayListBackGroundAlpha.getValue()).getRGB());
                    renderer.drawString(str, (width - 4 - renderer.getStringWidth(str)), (height - j), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    Gui.drawRect(
                            width - 2,
                            (2 + j * 10),
                            width,
                            (2 + j * 10) + renderer.getFontHeight(),
                            (ClickGui.getInstance()).rainbow.getValue()
                                    ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up)
                                    ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB()
                                    : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue()).getRGB())
                                    : color);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                for (int k = 0; k < Stay.moduleManager.sortedModules.size(); k++) {
                    Module module = Stay.moduleManager.sortedModules.get(k);
                    String str = module.getDisplayName() + ChatFormatting.GRAY + ((module.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
                    j += 10;
                    Gui.drawRect((width - 6 - renderer.getStringWidth(str)), (2 + j * 10), width, (2 + j * 10) + renderer.getFontHeight(), new Color(0, 0, 0, arrayListBackGroundAlpha.getValue()).getRGB());
                    renderer.drawString(str, (width - 4 - renderer.getStringWidth(str)), (height - j), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    Gui.drawRect(
                            width - 2,
                            (2 + j * 10),
                            width,
                            (2 + j * 10) + renderer.getFontHeight(),
                            (ClickGui.getInstance()).rainbow.getValue()
                                    ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up)
                                    ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue()).getRGB()
                                    : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue()).getRGB())
                                    : color);
                    counter1[0] = counter1[0] + 1;
                }
            }
        String grayString = grayNess.getValue().booleanValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat && renderingUp.getValue().booleanValue()) ? 13 : (renderingUp.getValue().booleanValue() ? -2 : 0);
        if (renderingUp.getValue().booleanValue()) {
            if (potions.getValue().booleanValue()) {
                List<PotionEffect> effects = new ArrayList<>((Minecraft.getMinecraft()).player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    String str = Stay.potionManager.getColoredPotionString(potionEffect);
                    i += 10;
                    renderer.drawString(str, (width - renderer.getStringWidth(str) - 2), (height - 2 - i), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (speed.getValue().booleanValue()) {
                String str = grayString + "Speed " + ChatFormatting.WHITE + Stay.speedManager.getSpeedKpH() + " km/h";
                i += 10;
                renderer.drawString(str, (width - renderer.getStringWidth(str) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (time.getValue().booleanValue()) {
                String str = grayString + "Time " + ChatFormatting.WHITE + (new SimpleDateFormat("h:mm a")).format(new Date());
                i += 10;
                renderer.drawString(str, (width - renderer.getStringWidth(str) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (tps.getValue().booleanValue()) {
                String str = grayString + "TPS " + ChatFormatting.WHITE + Stay.serverManager.getTPS();
                i += 10;
                renderer.drawString(str, (width - renderer.getStringWidth(str) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                counter1[0] = counter1[0] + 1;
            }
            String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            String str1 = grayString + "Ping " + ChatFormatting.WHITE + Stay.serverManager.getPing();
            if (renderer.getStringWidth(str1) > renderer.getStringWidth(fpsText)) {
                if (ping.getValue().booleanValue()) {
                    i += 10;
                    renderer.drawString(str1, (width - renderer.getStringWidth(str1) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (fps.getValue().booleanValue()) {
                    i += 10;
                    renderer.drawString(fpsText, (width - renderer.getStringWidth(fpsText) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (fps.getValue().booleanValue()) {
                    i += 10;
                    renderer.drawString(fpsText, (width - renderer.getStringWidth(fpsText) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (ping.getValue().booleanValue()) {
                    i += 10;
                    renderer.drawString(str1, (width - renderer.getStringWidth(str1) - 2), (height - 2 - i), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            if (potions.getValue().booleanValue()) {
                List<PotionEffect> effects = new ArrayList<>((Minecraft.getMinecraft()).player.getActivePotionEffects());
                for (PotionEffect potionEffect : effects) {
                    String str = Stay.potionManager.getColoredPotionString(potionEffect);
                    renderer.drawString(str, (width - renderer.getStringWidth(str) - 2), (2 + i++ * 10), potionEffect.getPotion().getLiquidColor(), true);
                }
            }
            if (speed.getValue().booleanValue()) {
                String str = grayString + "Speed " + ChatFormatting.WHITE + Stay.speedManager.getSpeedKpH() + " km/h";
                renderer.drawString(str, (width - renderer.getStringWidth(str) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (time.getValue().booleanValue()) {
                String str = grayString + "Time " + ChatFormatting.WHITE + (new SimpleDateFormat("h:mm a")).format(new Date());
                renderer.drawString(str, (width - renderer.getStringWidth(str) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                counter1[0] = counter1[0] + 1;
            }
            if (tps.getValue().booleanValue()) {
                String str = grayString + "TPS " + ChatFormatting.WHITE + Stay.serverManager.getTPS();
                renderer.drawString(str, (width - renderer.getStringWidth(str) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                counter1[0] = counter1[0] + 1;
            }
            String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.debugFPS;
            String str1 = grayString + "Ping " + ChatFormatting.WHITE + Stay.serverManager.getPing();
            if (renderer.getStringWidth(str1) > renderer.getStringWidth(fpsText)) {
                if (ping.getValue().booleanValue()) {
                    renderer.drawString(str1, (width - renderer.getStringWidth(str1) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (fps.getValue().booleanValue()) {
                    renderer.drawString(fpsText, (width - renderer.getStringWidth(fpsText) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    counter1[0] = counter1[0] + 1;
                }
            } else {
                if (fps.getValue().booleanValue()) {
                    renderer.drawString(fpsText, (width - renderer.getStringWidth(fpsText) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    counter1[0] = counter1[0] + 1;
                }
                if (ping.getValue().booleanValue()) {
                    renderer.drawString(str1, (width - renderer.getStringWidth(str1) - 2), (2 + i++ * 10), (ClickGui.getInstance()).rainbow.getValue().booleanValue() ? (((ClickGui.getInstance()).rainbowModeA.getValue() == ClickGui.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB() : ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB()) : color, true);
                    counter1[0] = counter1[0] + 1;
                }

            }
        }
        boolean inHell = mc.world.getBiome(mc.player.getPosition()).getBiomeName().equals("Hell");
        int posX = (int) mc.player.posX;
        int posY = (int) mc.player.posY;
        int posZ = (int) mc.player.posZ;
        float nether = !inHell ? 0.125F : 8.0F;
        int hposX = (int) (mc.player.posX * nether);
        int hposZ = (int) (mc.player.posZ * nether);
        i = (mc.currentScreen instanceof net.minecraft.client.gui.GuiChat) ? 14 : 0;
        String coordinates = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]"));
        String direction = this.direction.getValue().booleanValue() ? Stay.rotationManager.getDirection4D(false) : "";
        String coords = this.coords.getValue().booleanValue() ? coordinates : "";
        i += 10;
        if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
            String rainbowCoords = this.coords.getValue().booleanValue() ? ("XYZ " + ((posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]"))) : "";
            if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                renderer.drawString(direction, 2.0F, (height - i - 11), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                renderer.drawString(rainbowCoords, 2.0F, (height - i), ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
            } else {
                int[] counter2 = {1};
                char[] stringToCharArray = direction.toCharArray();
                float s = 0.0F;
                for (char c : stringToCharArray) {
                    renderer.drawString(String.valueOf(c), 2.0F + s, (height - i - 11), ColorUtil.rainbow(counter2[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                    s += renderer.getStringWidth(String.valueOf(c));
                    counter2[0] = counter2[0] + 1;
                }
                int[] counter3 = {1};
                char[] stringToCharArray2 = rainbowCoords.toCharArray();
                float u = 0.0F;
                for (char c : stringToCharArray2) {
                    renderer.drawString(String.valueOf(c), 2.0F + u, (height - i), ColorUtil.rainbow(counter3[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                    u += renderer.getStringWidth(String.valueOf(c));
                    counter3[0] = counter3[0] + 1;
                }
            }
        } else {
            renderer.drawString(direction, 2.0F, (height - i - 11), color, true);
            renderer.drawString(coords, 2.0F, (height - i), color, true);
        }
        if (armor.getValue().booleanValue())
            renderArmorHUD(true);
        if (totems.getValue().booleanValue())
            renderTotemHUD();
        if (greeter.getValue().booleanValue())
            renderGreeter();
        if (lag.getValue().booleanValue())
            renderLag();
        if (Crystalsss.getValue().booleanValue())
             renderCrystalHUD();
        if (XPSSS.getValue().booleanValue())
            renderxpHUD();
        if (APSSS.getValue().booleanValue())
            renderAPHUD();
        if (this.hitMarkers.getValue() == false) return;
        if (this.hitMarkerTimer <= 0) return;
        this.drawHitMarkers();
    }

    public Map<String, Integer> getTextRadarPlayers() {
        return EntityUtils.getTextRadarPlayers();
    }
    public void drawHitMarkers() {
        ScaledResolution resolution = new ScaledResolution(mc);
        RenderUtil.drawLine((float)resolution.getScaledWidth() / 2.0f - 4.0f, (float)resolution.getScaledHeight() / 2.0f - 4.0f, (float)resolution.getScaledWidth() / 2.0f - 8.0f, (float)resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine((float)resolution.getScaledWidth() / 2.0f + 4.0f, (float)resolution.getScaledHeight() / 2.0f - 4.0f, (float)resolution.getScaledWidth() / 2.0f + 8.0f, (float)resolution.getScaledHeight() / 2.0f - 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine((float)resolution.getScaledWidth() / 2.0f - 4.0f, (float)resolution.getScaledHeight() / 2.0f + 4.0f, (float)resolution.getScaledWidth() / 2.0f - 8.0f, (float)resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
        RenderUtil.drawLine((float)resolution.getScaledWidth() / 2.0f + 4.0f, (float)resolution.getScaledHeight() / 2.0f + 4.0f, (float)resolution.getScaledWidth() / 2.0f + 8.0f, (float)resolution.getScaledHeight() / 2.0f + 8.0f, 1.0f, ColorUtil.toRGBA(255, 255, 255, 255));
    }
    public void betaText() {
        int width = renderer.scaledWidth;
        String text = "Developing version,doesn't stand for final quality.\n测试版本，不代表最终品质";
        if ((ClickGui.getInstance()).rainbow.getValue()) {
            if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                renderer.drawString(text, width / 2.0F - renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
            } else {
                int[] counter1 = {1};
                char[] stringToCharArray = text.toCharArray();
                float i = 0.0F;
                for (char c : stringToCharArray) {
                    renderer.drawString(String.valueOf(c), width / 2.0F - renderer.getStringWidth(text) / 2.0F + 2.0F + i, 2.0F, ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                    i += renderer.getStringWidth(String.valueOf(c));
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            renderer.drawString(text, width / 2.0F - renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, color, true);
        }
    }
    public void renderGreeter() {
        int width = renderer.scaledWidth;
        String text = "Welcome, ";
        if (greeter.getValue().booleanValue())
            text = text + mc.player.getDisplayNameString();
        if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
            if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                renderer.drawString(text, width / 2.0F - renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
            } else {
                int[] counter1 = {1};
                char[] stringToCharArray = text.toCharArray();
                float i = 0.0F;
                for (char c : stringToCharArray) {
                    renderer.drawString(String.valueOf(c), width / 2.0F - renderer.getStringWidth(text) / 2.0F + 2.0F + i, 2.0F, ColorUtil.rainbow(counter1[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                    i += renderer.getStringWidth(String.valueOf(c));
                    counter1[0] = counter1[0] + 1;
                }
            }
        } else {
            renderer.drawString(text, width / 2.0F - renderer.getStringWidth(text) / 2.0F + 2.0F, 2.0F, color, true);
        }
    }

    public void renderLag() {
        int width = renderer.scaledWidth;
        if (Stay.serverManager.isServerNotResponding()) {
            String text = ChatFormatting.RED + "Server lagging for " + MathUtil.round((float) Stay.serverManager.serverRespondingTime() / 1000.0F, 1) + "s.";
            renderer.drawString(text, width / 2.0F - renderer.getStringWidth(text) / 2.0F + 2.0F, 20.0F, color, true);
        }
    }

    public void renderTotemHUD() {
        int width = renderer.scaledWidth;
        int height = renderer.scaledHeight;
        int totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.TOTEM_OF_UNDYING)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING)
            totems += mc.player.getHeldItemOffhand().getCount();
        if (totems > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int iteration = 0;
            int y = height - 55 - ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(totem, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, totem, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(totems + "", (x + 19 - 2 - renderer.getStringWidth(totems + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    public void renderCrystalHUD() {
        int width = renderer.scaledWidth;
        int height = renderer.scaledHeight;
        int Crystals = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.END_CRYSTAL)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)
            Crystals += mc.player.getHeldItemOffhand().getCount();
        if (Crystals > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int iteration = 0;
            int y = height - 55 - ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            if(totems.getValue()){
                y=y-insss.getValue();
            }
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(Crystal, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, Crystal, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(Crystals + "", (x + 19 - 2 - renderer.getStringWidth(Crystals + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    public void renderxpHUD() {
        int width = renderer.scaledWidth;
        int height = renderer.scaledHeight;
        int Crystals = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.EXPERIENCE_BOTTLE)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE)
            Crystals += mc.player.getHeldItemOffhand().getCount();
        if (Crystals > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int iteration = 0;
            int y = height - 55 - ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            if(totems.getValue()){
                y=y-insss.getValue();
            }
            if(Crystalsss.getValue()){
                y=y-insss.getValue();
            }
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(xp, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, xp, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(Crystals + "", (x + 19 - 2 - renderer.getStringWidth(Crystals + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    public void renderAPHUD() {
        int width = renderer.scaledWidth;
        int height = renderer.scaledHeight;
        int Crystals = mc.player.inventory.mainInventory.stream().filter(itemStack -> (itemStack.getItem() == Items.GOLDEN_APPLE)).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE)
            Crystals += mc.player.getHeldItemOffhand().getCount();
        if (Crystals > 0) {
            GlStateManager.enableTexture2D();
            int i = width / 2;
            int iteration = 0;
            int y = height - 55 - ((mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure()) ? 10 : 0);
            int x = i - 189 + 180 + 2;
            if(totems.getValue()){
                y=y-insss.getValue();
            }
            if(Crystalsss.getValue()){
                y=y-insss.getValue();
            }
            if(XPSSS.getValue()){
                y=y-insss.getValue();
            }

            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200.0F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(ap, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, ap, x, y, "");
            RenderUtil.itemRender.zLevel = 0.0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            renderer.drawStringWithShadow(Crystals + "", (x + 19 - 2 - renderer.getStringWidth(Crystals + "")), (y + 9), 16777215);
            GlStateManager.enableDepth();
            GlStateManager.disableLighting();
        }
    }
    public void renderArmorHUD(boolean percent) {
        int width = renderer.scaledWidth;
        int height = renderer.scaledHeight;
        GlStateManager.enableTexture2D();
        int i = width / 2;
        int iteration = 0;
        int y = height - 55 - (mc.player.isInWater() && mc.playerController.gameIsSurvivalOrAdventure() ? 10 : 0);
        for (ItemStack is : mc.player.inventory.armorInventory) {
            iteration++;
            if (is.isEmpty()) continue;
            int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.enableDepth();
            RenderUtil.itemRender.zLevel = 200F;
            RenderUtil.itemRender.renderItemAndEffectIntoGUI(is, x, y);
            RenderUtil.itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, is, x, y, "");
            RenderUtil.itemRender.zLevel = 0F;
            GlStateManager.enableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            String s = is.getCount() > 1 ? is.getCount() + "" : "";
            renderer.drawStringWithShadow(s, x + 19 - 2 - renderer.getStringWidth(s), y + 9, 0xffffff);
            //mc.fontRenderer.drawStringWithShadow(s, x + 19 - 2 - mc.fontRenderer.getStringWidth(s), y + 9, 0xffffff);

            if (percent) {
                int dmg = 0;
                int itemDurability = is.getMaxDamage() - is.getItemDamage();
                float green = ((float) is.getMaxDamage() - (float) is.getItemDamage()) / (float) is.getMaxDamage();
                float red = 1 - green;
                if (percent) {
                    dmg = 100 - (int) (red * 100);
                } else {
                    dmg = itemDurability;
                }
                renderer.drawStringWithShadow(dmg + "", x + 8 - renderer.getStringWidth(dmg + "") / 2, y - 11, ColorUtil.toRGBA((int) (red * 255), (int) (green * 255), 0));
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
    }

    public void renderPvpInfo() {
        String caOn = "CA:" + ChatFormatting.GREEN + " TRUE";
        String caOff = "CA:" + ChatFormatting.DARK_RED + " FALSE";
        String atOn = "AT:" + ChatFormatting.GREEN + " TRUE";
        String atOff = "AT:" + ChatFormatting.DARK_RED + " FALSE";
        String suOn = "SU:" + ChatFormatting.GREEN + " TRUE";
        String suOff = "SU:" + ChatFormatting.DARK_RED + " FALSE";
        String hfOn = "HF:" + ChatFormatting.GREEN + " TRUE";
        String hfOff = "HF:" + ChatFormatting.DARK_RED + " FALSE";
        String kOn = "32k:" + ChatFormatting.GREEN + " TRUE";
        String kOff = "32k:" + ChatFormatting.DARK_RED + " FALSE";
        if (ModuleManager.getModuleByName("AutoXin32k").isEnabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(kOn, 2.0F, 50.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = kOn.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 50.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(kOn, 2.0F, 50.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("AutoCrystal").isEnabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(caOn, 2.0F, 10.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = caOn.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(caOn, 2.0F, 10.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("AutoTrap").isEnabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(atOn, 2.0F, 20.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = atOn.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 20.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(atOn, 2.0F, 20.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("Surround").isEnabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(suOn, 2.0F, 30.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = suOn.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 30.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(suOn, 2.0F, 30.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("HoleFill").isEnabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(hfOn, 2.0F, 40.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = hfOn.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 40.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(hfOn, 2.0F, 40.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("AutoCrystal").isDisabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(caOff, 2.0F, 10.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = caOff.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(caOff, 2.0F, 10.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("AutoTrap").isDisabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(atOff, 2.0F, 20.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = atOff.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 20.0F, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(atOff, 2.0F, 20.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("Surround").isDisabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(suOff, 2.0F, 30.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = suOff.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 30.0F, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(suOff, 2.0F, 30.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("HoleFill").isDisabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(hfOff, 2.0F, 40.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = hfOff.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 40.0F, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(hfOff, 2.0F, 40.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("AutoXin32k").isDisabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(kOff, 2.0F, 50.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = kOff.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 50.0F, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(kOff, 2.0F, 50.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("AutoCrystal+").isEnabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(caOn, 2.0F, 10.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = caOn.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(caOn, 2.0F, 10.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("AutoCrystal+").isDisabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(caOff, 2.0F, 10.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = caOff.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(caOff, 2.0F, 10.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("StormCrystal").isEnabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(caOn, 2.0F, 10.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = caOn.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(caOn, 2.0F, 10.0f, color, true);
            }
        }
        if (ModuleManager.getModuleByName("StormCrystal").isDisabled()) {
            if ((ClickGui.getInstance()).rainbow.getValue().booleanValue()) {
                if ((ClickGui.getInstance()).rainbowModeHud.getValue() == ClickGui.rainbowMode.Static) {
                    renderer.drawString(caOff, 2.0F, 10.0f, ColorUtil.rainbow((ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                } else {
                    int[] arrayOfInt = {1};
                    char[] stringToCharArray = caOff.toCharArray();
                    float f = 0.0F;
                    for (char c : stringToCharArray) {
                        renderer.drawString(String.valueOf(c), 2.0F + f, 10.0f, ColorUtil.rainbow(arrayOfInt[0] * (ClickGui.getInstance()).rainbowHue.getValue().intValue()).getRGB(), true);
                        f += renderer.getStringWidth(String.valueOf(c));
                        arrayOfInt[0] = arrayOfInt[0] + 1;
                    }
                }
            } else {
                renderer.drawString(caOff, 2.0F, 10.0f, color, true);
            }
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(AttackEntityEvent event) {
        shouldIncrement = true;
    }

    public void onLoad() {
        Stay.commandManager.setClientMessage(getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 &&
                equals(event.getSetting().getFeature()))
            Stay.commandManager.setClientMessage(getCommandMessage());
    }


    public String getCommandMessage() {
        if (this.rainbowPrefix.getPlannedValue().booleanValue()) {
            StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
            stringBuilder.insert(0, "\u00a7+");
            stringBuilder.append("\u00a7r");
            return stringBuilder.toString();
        }
        return TextUtil.coloredString(this.commandBracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command.getPlannedValue(), this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2.getPlannedValue(), this.bracketColor.getPlannedValue());
    }

    public String getRainbowCommandMessage() {
        StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
        stringBuilder.insert(0, "\u00a7+");
        stringBuilder.append("\u00a7r");
        return stringBuilder.toString();
    }

    public String getRawCommandMessage() {
        return this.commandBracket.getValue() + this.command.getValue() + this.commandBracket2.getValue();
    }

    public void drawTextRadar(int yOffset) {
        if (!players.isEmpty()) {
            int y = renderer.getFontHeight() + 7 + yOffset;
            for (Map.Entry<String, Integer> player : players.entrySet()) {
                String text = player.getKey() + " ";
                int textheight = renderer.getFontHeight() + 1;
                renderer.drawString(text, 2.0F, y, color, true);
                y += textheight;
            }
        }
    }

    public enum RenderingMode {
        Length, ABC
    }
}
