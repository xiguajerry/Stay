/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.manager;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.alpha432.stay.client.Stay;
import me.alpha432.stay.event.Render2DEvent;
import me.alpha432.stay.event.Render3DEvent;
import me.alpha432.stay.features.Feature;
import me.alpha432.stay.features.gui.StayGui;
import me.alpha432.stay.features.modules.Module;
import me.alpha432.stay.features.modules.ApplyModule;
import me.alpha432.stay.features.modules.client.*;
import me.alpha432.stay.features.modules.combat.*;
import me.alpha432.stay.features.modules.misc.*;
import me.alpha432.stay.features.modules.movement.*;
import me.alpha432.stay.features.modules.player.*;
import me.alpha432.stay.features.modules.render.*;
import me.alpha432.stay.features.modules.render.Swing;
import me.alpha432.stay.features.modules.unstable.GUIBlur;
import me.alpha432.stay.loader.ForgeEntry;
import me.alpha432.stay.util.basement.wrapper.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;
import org.reflections.Reflections;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ModuleManager
        extends Feature {
    public static ArrayList<Module> modules = new ArrayList();
    public List<Module> sortedModules = new ArrayList<Module>();
    public List<String> sortedModulesABC = new ArrayList<String>();
    public Animation animationThread;

    public void init() {
        //CLIENT
        this.modules.add(new ClickGui());
        this.modules.add(new FontMod());
        this.modules.add(new GUIBlur());
        this.modules.add(new HUD());
        this.modules.add(new HudComponents());
        this.modules.add(new NickHider());
        //RENDER
        this.modules.add(new Breadcrumbs());
        this.modules.add(new HoleESP2());
        this.modules.add(new LogSpots());
        this.modules.add(new AntiWeather());
        this.modules.add(new Tracer());
        this.modules.add(new CameraClip());
        this.modules.add(new VoidESP());
        this.modules.add(new BreakESP());
        this.modules.add(new CapesModule());
        this.modules.add(new Shaders());
        this.modules.add(new BlockHighlight());
        this.modules.add(new HellgateEsp());
        this.modules.add(new HoleESP());
        this.modules.add(new Skeleton());
        this.modules.add(new Wireframe());
        this.modules.add(new ViewModel());
        this.modules.add(new HandChams());
        this.modules.add(new NoRender());
        this.modules.add(new Trajectories());
        this.modules.add(new Swing());
        this.modules.add(new ArrowESP());
        this.modules.add(new GlintModify());
        this.modules.add(new FullBright());
        this.modules.add(new SkyColor());
        this.modules.add(new ESP());
        this.modules.add(new Burrowesp());
        this.modules.add(new NameTags());
        this.modules.add(new NameTags2());
        this.modules.add(new Fov());
        this.modules.add(new TexturedChams());
        this.modules.add(new SurroundRender());
        this.modules.add(new SmallShield());
        this.modules.add(new HitMarkers());
        //COMBAT
        //fuck zopac
        this.modules.add(new AutoQuiver());
        this.modules.add(new StormCrystal());
        this.modules.add(new InfiniteDive());
        this.modules.add(new PistonAura());
        this.modules.add(new AntiCrystal());
        this.modules.add(new Anti32KTotem());
        this.modules.add(new AutoCrystal2());
        this.modules.add(new Surround());
        this.modules.add(new AutoTrap());
        this.modules.add(new AutoTotem());
        this.modules.add(new OffHandCrystal());
        this.modules.add(new GodModule());
        this.modules.add(new AutoWeb());
        this.modules.add(new Killaura());
        this.modules.add(new AutoHoleFiller());
        this.modules.add(new NewBuorrw());
        this.modules.add(new AntiCevTrap());
        this.modules.add(new AutoCrystal());
        this.modules.add(new AutoCev());
        this.modules.add(new Auto32k());
        this.modules.add(new Criticals());
        this.modules.add(new HoleFiller());
        this.modules.add(new Burrow());
        this.modules.add(new AutoArmor());
        this.modules.add(new Selftrap());

        this.modules.add(new SelfWeb());
        this.modules.add(new Quiver());
        this.modules.add(new AutoMinecart());
        this.modules.add(new AutoAntiBurrow());
        this.modules.add(new NoContainer());
        this.modules.add(new AntiBurrow());
        this.modules.add(new AutoCity());
        //PLAYER
        this.modules.add(new PortalGodMode());
        this.modules.add(new Yaw());
        this.modules.add(new Freecam());
        this.modules.add(new FastPlace());
        this.modules.add(new TpsSync());
        this.modules.add(new Replenish());
        this.modules.add(new FakePlayer());
        this.modules.add(new Blink());
        this.modules.add(new MultiTask());
        this.modules.add(new MCP());
        this.modules.add(new LiquidInteract());
        this.modules.add(new Speedmine());
        this.modules.add(new Announcer());
        //MISC
        this.modules.add(new AntiBookBan());
        this.modules.add(new ExtraTab());
        this.modules.add(new AutoPlatform());
        this.modules.add(new NoHitBox());
        this.modules.add(new PacketEat());
        this.modules.add(new Timestamps());
        this.modules.add(new ChatSuffix());
        this.modules.add(new AntiDeathScreen());
        this.modules.add(new AtinAFK());
        this.modules.add(new NoHandShake());
        this.modules.add(new BuildHeight());
        this.modules.add(new AutoSignin());
        this.modules.add(new ChatModifier());
        this.modules.add(new MCF());
        this.modules.add(new Message());
        this.modules.add(new PearlNotify());
        this.modules.add(new AutoQueue());
        this.modules.add(new AutoGG());
        this.modules.add(new ToolTips());
        this.modules.add(new InstantMine());
        this.modules.add(new RPC());
        this.modules.add(new AutoDupe());
        this.modules.add(new Tracker());
        this.modules.add(new Timers());
        this.modules.add(new Anti32k());
        this.modules.add(new Burrow2());
        this.modules.add(new PopCounter());
        this.modules.add(new AutoAntiCity());
        this.modules.add(new NarratorTweaks());
        this.modules.add(new PacketXP());
        this.modules.add(new GhastNotifier());
        this.modules.add(new HotbarRefill());
        this.modules.add(new XCarry());
        this.modules.add(new TestPhase());
        this.modules.add(new PingSpoof());
        this.modules.add(new AutoLog());
        this.modules.add(new AutoReconnect());
        //MOVEMENT
        this.modules.add(new AutoWalk());
        this.modules.add(new Phase());
        this.modules.add(new Sprint());
        this.modules.add(new Step());
        this.modules.add(new Strafe());
        this.modules.add(new NoFall());
        this.modules.add(new BoatFly());
        this.modules.add(new ElytraFlight());
        this.modules.add(new ReverseStep());
        this.modules.add(new PlayerTweaks());
        this.modules.add(new AntiVoid());
        this.modules.add(new Anchor());
        this.modules.add(new Flight());
        this.modules.add(new HoleIntercept());
        this.modules.add(new Scaffold());
        Reflections reflections = new Reflections();
        Set<Class<?>> modules = reflections.getTypesAnnotatedWith(ApplyModule.class);
        modules.stream()
                .filter(it ->
                        Arrays
                                .stream(it.getFields())
                                .noneMatch(field -> field.getName()
                                        .equals("INSTANCE")
                                )
                )
                .forEach(aClass -> {
                    try {
                        Module module = (Module) aClass.newInstance();
                        if (ModuleManager.modules.stream().noneMatch(module::equals)) ModuleManager.modules.add(module);
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
        modules.stream()
                .filter(it ->
                        Arrays
                                .stream(it.getFields())
                                .anyMatch(field -> field.getName()
                                        .equals("INSTANCE")
                                )
                )
                .forEach(aClass -> {
                    try {
                        Module module = (Module) aClass.getField("INSTANCE").get(null);
                        if (ModuleManager.modules.stream().noneMatch(module::equals)) ModuleManager.modules.add(module);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    @Nullable
    public static Module getModuleByName(String name) {
        for (Module module : modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    @Nullable
    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <T extends Module> T getModuleByClass(@NotNull Class<T> clazz) {
        for (Module module : modules) {
            if (!clazz.isInstance(module)) continue;
            return (T) module;
        }
        return null;
    }

    public void enableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(Class<Module> clazz) {
        Module module = this.getModuleByClass(clazz);
        if (module != null) {
            module.disable();
        }
    }

    public void enableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.enable();
        }
    }

    public void disableModule(String name) {
        Module module = this.getModuleByName(name);
        if (module != null) {
            module.disable();
        }
    }

    public boolean isModuleEnabled(String name) {
        Module module = this.getModuleByName(name);
        return module != null && module.isOn();
    }

    public <T extends Module> boolean isModuleEnabled(Class<T> clazz) {
        Module module = this.getModuleByClass(clazz);
        return module != null && module.isOn();
    }

    public Module getModuleByDisplayName(String displayName) {
        for (Module module : this.modules) {
            if (!module.getDisplayName().equalsIgnoreCase(displayName)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getEnabledModules() {
        ArrayList<Module> enabledModules = new ArrayList<Module>();
        for (Module module : this.modules) {
            if (!module.isEnabled()) continue;
            enabledModules.add(module);
        }
        return enabledModules;
    }

    public ArrayList<String> getEnabledModulesName() {
        ArrayList<String> enabledModules = new ArrayList<String>();
        for (Module module : this.modules) {
            if (!module.isEnabled() || !module.isDrawn()) continue;
            enabledModules.add(module.getFullArrayString());
        }
        return enabledModules;
    }

    public ArrayList<Module> getModulesByCategory(Module.Category category) {
        ArrayList<Module> modulesCategory = new ArrayList<Module>();
        modules.forEach(module -> {
            if (module.getCategory() == category) {
                modulesCategory.add(module);
            }
        });
        return modulesCategory;
    }

    public List<Module.Category> getCategories() {
        return Arrays.asList(Module.Category.values());
    }

    public void onLoad() {
        modules.stream().filter(Module::listening).forEach(ForgeEntry::register);
        modules.forEach(Module::onLoad);
    }

    public void onUpdate() {
        modules.stream().filter(Feature::isEnabled).forEach(module -> {
            module.onUpdate0();
            module.onUpdate();
        });
    }

    public void onTick() {
        modules.stream().filter(Feature::isEnabled).forEach(module -> {
            module.onTick();
            module.onTick0();
        });
    }

    public void onRender2D(Render2DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> {
            module.onRender2D(event);
            module.onRender2D0(event);
        });
    }

    public void onRender3D(Render3DEvent event) {
        modules.stream().filter(Feature::isEnabled).forEach(module -> {
            module.onRender3D(event);
            module.onRender3D0(event);
        });
    }

    public <T extends Module> T getModuleT(Class<T> clazz) {
        return modules.stream().filter(module -> module.getClass() == clazz).map(module -> (T) module).findFirst().orElse(null);
    }

    public void sortModules(boolean reverse) {
        this.sortedModules = this.getEnabledModules().stream().filter(Module::isDrawn).sorted(Comparator.comparing(module -> this.renderer.getStringWidth(module.getFullArrayString()) * (reverse ? -1 : 1))).collect(Collectors.toList());
    }

    public void sortModulesABC() {
        this.sortedModulesABC = new ArrayList<String>(this.getEnabledModulesName());
        this.sortedModulesABC.sort(String.CASE_INSENSITIVE_ORDER);
    }

    public void onLogout() {
        this.modules.forEach(Module::onLogout);
    }

    public void onLogin() {
        this.modules.forEach(Module::onLogin);
    }

    public void onUnload() {
        modules.forEach(ForgeEntry::unregister);
        modules.forEach(Module::onUnload);
    }

    public void onUnloadPost() {
        for (Module module : this.modules) {
            module.enabled.setValue(false);
        }
    }

    public void onKeyPressed(int eventKey) {
        
        if (eventKey == 0 || !Keyboard.getEventKeyState() || ModuleManager.mc.currentScreen instanceof StayGui) {
            return;
        }
        this.modules.forEach(module -> {
            if (module.getBind().getKey() == eventKey) {
                module.toggle();
            }
        });
    }

    private class Animation
            extends Thread {
        public Module module;
        public float offset;
        public float vOffset;
        ScheduledExecutorService service;

        public Animation() {
            super("Animation");
            this.service = Executors.newSingleThreadScheduledExecutor();
        }

        @Override
        public void run() {
            if (HUD.getInstance().renderingMode.getValue() == HUD.RenderingMode.Length) {
                for (Module module : ModuleManager.this.sortedModules) {
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            } else {
                for (String e : ModuleManager.this.sortedModulesABC) {
                    Module module = Stay.moduleManager.getModuleByName(e);
                    String text = module.getDisplayName() + ChatFormatting.GRAY + (module.getDisplayInfo() != null ? " [" + ChatFormatting.WHITE + module.getDisplayInfo() + ChatFormatting.GRAY + "]" : "");
                    module.offset = (float) ModuleManager.this.renderer.getStringWidth(text) / HUD.getInstance().animationHorizontalTime.getValue().floatValue();
                    module.vOffset = (float) ModuleManager.this.renderer.getFontHeight() / HUD.getInstance().animationVerticalTime.getValue().floatValue();
                    if (module.isEnabled() && HUD.getInstance().animationHorizontalTime.getValue() != 1) {
                        if (!(module.arrayListOffset > module.offset) || Util.mc.world == null) continue;
                        module.arrayListOffset -= module.offset;
                        module.sliding = true;
                        continue;
                    }
                    if (!module.isDisabled() || HUD.getInstance().animationHorizontalTime.getValue() == 1) continue;
                    if (module.arrayListOffset < (float) ModuleManager.this.renderer.getStringWidth(text) && Util.mc.world != null) {
                        module.arrayListOffset += module.offset;
                        module.sliding = true;
                        continue;
                    }
                    module.sliding = false;
                }
            }
        }

        @Override
        public void start() {
            System.out.println("Starting animation thread.");
            this.service.scheduleAtFixedRate(this, 0L, 1L, TimeUnit.MILLISECONDS);
        }
    }

    public static ArrayList<Module> nigger;

    @Contract(pure = true)
    public static ArrayList<Module> getModules() {
        return nigger;
    }

    public static boolean isModuleEnablednigger(String name) {
        Module modulenigger = getModules().stream().filter(mm->mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
        return modulenigger.isEnabled();
    }

    public static boolean isModuleEnablednigger(Module modulenigger) {
        return modulenigger.isEnabled();
    }

}

