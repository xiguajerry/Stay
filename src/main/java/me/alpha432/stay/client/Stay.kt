/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */
package me.alpha432.stay.client

import kotlinx.coroutines.launch
import me.alpha432.stay.event.ListenerProcessor
import me.alpha432.stay.event.Render3DEvent
import me.alpha432.stay.features.modules.client.FontMod
import me.alpha432.stay.loader.ForgeEntry
import me.alpha432.stay.manager.*
import me.alpha432.stay.util.basement.Title
import me.alpha432.stay.util.graphics.image.IconUtil
import me.alpha432.stay.util.player.Enemy
import me.alpha432.stay.util.thread.loadingScope
import me.alpha432.stay.util.thread.mainScope
import net.minecraft.util.Util
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.eventhandler.EventBus
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.opengl.Display
import java.awt.*
import javax.swing.JOptionPane

class Stay {
    companion object {
        const val MOD_ID = "stay"
        const val MOD_NAME = "Stay"
        const val VERSION = "1.1 beta+b13.fk5jcKwJ5iK9k12c"
//        const val VERSION = "1.0 release+b1"

        @JvmField
        val LOGGER: Logger = LogManager.getLogger(MOD_NAME)

        @JvmField
        var timerManager: TimerManager? = null

        @JvmField
        var commandManager: CommandManager? = null

        @JvmField
        var friendManager: FriendManager? = null

        @JvmField
        var moduleManager: ModuleManager? = null
        var packetManager: PacketManager? = null

        @JvmField
        var colorManager: ColorManager? = null

        @JvmField
        var POP_MANAGER: TotemPopManager? = null
        var holeManager: HoleManager? = null

        @JvmField
        var inventoryManager: InventoryManager? = null

        @JvmField
        var potionManager: PotionManager? = null

        @JvmField
        var rotationManager: RotationManager? = null

        @JvmField
        var positionManager: PositionManager? = null

        @JvmField
        var speedManager: SpeedManager? = null
        var reloadManager: ReloadManager? = null
        var fileManager: FileManager? = null

        @JvmField
        var configManager: ConfigManager? = null

        @JvmField
        var serverManager: ServerManager? = null

        @JvmField
        var eventManager: EventManager? = null

        @JvmField
        var textManager: TextManager? = null

        @JvmField
        var italicTextManager: TextManager? = null

        @JvmField
        var targetManager: TargetManager? = null

        @JvmField
        var fontRenderer = textManager
        var render3DEvent: Render3DEvent? = null

        @JvmField
        var enemy: Enemy? = null
        val EVENT_BUS = EventBus()

        @JvmField
        var MENU_FONT_MANAGER = MenuFont()

        @JvmField
        var GUI_FONT_MANAGER = GuiFont()

        @JvmField
        var DONATOR_FONT_MANAGER = DonatorFont()

        @JvmField
        var notificationManager: NotificationManager = NotificationManager
        var SONG_MANAGER = SongManager()
        private var unloaded = false

        @JvmStatic
        fun preInit() {
            trayIcon.isImageAutoSize = true
            trayIcon.toolTip = "Hello User, thanks for using Stay $VERSION!"
            try {
                INSTANCE.tray.add(trayIcon)
            } catch (e: AWTException) {
                e.printStackTrace()
            }
            trayIcon.displayMessage("Stay", "Hello User, thanks for using Stay $VERSION!", TrayIcon.MessageType.NONE)
            LOGGER.info("Preparing to initialize Stay $VERSION")
            ForgeEntry.register(Title)
            textManager = TextManager()
            textManager!!.init(true)
            italicTextManager = TextManager().apply { setFontRenderer(
                Font.createFont(
                    Font.TRUETYPE_FONT,
                    Stay::class.java.getResourceAsStream("/assets/cuican/LexendDeca-Regular.ttf")
                ).deriveFont(FontMod.getInstance().fontSize.value.toFloat()), true, true) }
            italicTextManager!!.init(true)
            fontRenderer = textManager
            LOGGER.info("TextRender loaded.")
            mainScope.launch {
                load()
            }
            setWindowIcon()
        }

        @JvmStatic
        fun postInit() {
            loadingScope.launch {
                if (Loader.instance().modList.any { "uwu" in it.name.lowercase() }) {
                    JOptionPane.showConfirmDialog(null, "[AntiUwU]请卸载你的UwWGod。", "UwWGod Detection", JOptionPane.YES_OPTION)
                    throw object : VirtualMachineError("EZPZ") { /* LOL */ }
                }
                LOGGER.info("Stay successfully loaded!\n")
            }
        }

        @JvmStatic
        fun load() {
            LOGGER.info("loading stay")
            unloaded = false
            if (reloadManager != null) {
                reloadManager!!.unload()
                reloadManager = null
            }
            loadingScope.launch {
                commandManager = CommandManager()
                friendManager = FriendManager()
                moduleManager = ModuleManager()
                rotationManager = RotationManager()
                packetManager = PacketManager()
                eventManager = EventManager()
                speedManager = SpeedManager()
                POP_MANAGER = TotemPopManager()
                potionManager = PotionManager()
                inventoryManager = InventoryManager()
                serverManager = ServerManager()
                fileManager = FileManager()
                colorManager = ColorManager()
                positionManager = PositionManager()
                configManager = ConfigManager()
                holeManager = HoleManager()
                targetManager = TargetManager()
                LOGGER.info("Managers loaded.")
                launch {
                    moduleManager!!.init()
                    LOGGER.info("Modules loaded.")
                    configManager!!.init()
                    eventManager!!.init()
                    MinecraftForge.EVENT_BUS.register(ListenerProcessor)
                    LOGGER.info("EventManager loaded.")
                    moduleManager!!.onLoad()
                }
            }
//            textManager = TextManager()
//            commandManager = CommandManager()
//            friendManager = FriendManager()
//            moduleManager = ModuleManager()
//            rotationManager = RotationManager()
//            packetManager = PacketManager()
//            eventManager = EventManager()
//            speedManager = SpeedManager()
//            POP_MANAGER = TotemPopManager()
//            potionManager = PotionManager()
//            inventoryManager = InventoryManager()
//            serverManager = ServerManager()
//            fileManager = FileManager()
//            colorManager = ColorManager()
//            positionManager = PositionManager()
//            configManager = ConfigManager()
//            holeManager = HoleManager()
//            targetManager = TargetManager()
//            LOGGER.info("Managers loaded.")
//            moduleManager!!.init()
//            LOGGER.info("Modules loaded.")
//            configManager!!.init()
//            eventManager!!.init()
//            MinecraftForge.EVENT_BUS.register(ListenerProcessor)
//            LOGGER.info("EventManager loaded.")
//            textManager!!.init(true)
//            moduleManager!!.onLoad()
//            LOGGER.info("stay successfully loaded!\n")
        }

        @JvmStatic
        fun unload(unload: Boolean) {
            LOGGER.info("unloading stay")
            if (unload) {
                reloadManager = ReloadManager()
                reloadManager!!.init(if (commandManager != null) commandManager!!.prefix else ".")
            }
            onUnload()
            eventManager = null
            friendManager = null
            speedManager = null
            holeManager = null
            positionManager = null
            rotationManager = null
            configManager = null
            commandManager = null
            colorManager = null
            serverManager = null
            fileManager = null
            potionManager = null
            inventoryManager = null
            moduleManager = null
            textManager = null
            LOGGER.info("stay unloaded!\n")
        }

        @JvmStatic
        fun reload() {
            unload(false)
            load()
        }

        @JvmStatic
        fun onUnload() {
            if (!unloaded) {
                eventManager?.onUnload()
                moduleManager?.onUnload()
                configManager?.saveConfig(configManager!!.config.replaceFirst("Stay/".toRegex(), ""))
                moduleManager?.onUnloadPost()
                unloaded = true
            }
        }

        @JvmField
        var image =
            Toolkit.getDefaultToolkit().createImage(Stay::class.java.getResource("/assets/cuican/icons/icon-32x.png"))

        @JvmField
        var trayIcon = TrayIcon(image, "Auto Queue")

        private fun setWindowIcon() {
            if (Util.getOSType() != Util.EnumOS.OSX) {
                try {
                    Stay::class.java.getResourceAsStream("/assets/cuican/icons/icon-16x.png").use { inputStream16x ->
                        Stay::class.java.getResourceAsStream("/assets/cuican/icons/icon-32x.png")
                            .use { inputStream32x ->
                                val icons = arrayOf(
                                    IconUtil.INSTANCE.readImageToBuffer(inputStream16x),
                                    IconUtil.INSTANCE.readImageToBuffer(inputStream32x)
                                )
                                Display.setIcon(icons)
                            }
                    }
                } catch (e: Exception) {
                    LOGGER.error("Couldn't set Windows Icon", e)
                }
            }
        }

        var INSTANCE = Stay()

        init {
            unloaded = false
        }
    }

    var tray = SystemTray.getSystemTray()
}