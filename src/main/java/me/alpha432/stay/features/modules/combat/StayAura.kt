/*
 * Copyright (c) 2021-2022
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/30 下午5:35
 */

@file:Suppress("nothing_to_inline")

package me.alpha432.stay.features.modules.combat

import kotlinx.coroutines.*
import me.alpha432.stay.client.Stay
import me.alpha432.stay.event.safeListener
import me.alpha432.stay.features.command.Command
import me.alpha432.stay.features.modules.ApplyModule
import me.alpha432.stay.features.modules.Module
import me.alpha432.stay.features.modules.client.ClickGui
import me.alpha432.stay.features.modules.combat.StayAura.BreakCalcMode.*
import me.alpha432.stay.features.modules.combat.StayAura.BreakHand.MAIN
import me.alpha432.stay.features.modules.combat.StayAura.BreakHand.OFFHAND
import me.alpha432.stay.features.modules.combat.StayAura.HudInfoMode.*
import me.alpha432.stay.features.modules.combat.StayAura.OverrideMode.*
import me.alpha432.stay.manager.NotificationManager
import me.alpha432.stay.manager.NotificationType
import me.alpha432.stay.util.counting.Timer
import me.alpha432.stay.util.delegate.setting
import me.alpha432.stay.util.extension.limit
import me.alpha432.stay.util.extension.sq
import me.alpha432.stay.util.extension.visible
import me.alpha432.stay.util.graphics.animations.BlockEasingRender
import me.alpha432.stay.util.graphics.color.ColorUtil
import me.alpha432.stay.util.graphics.opengl.RenderUtil
import me.alpha432.stay.util.graphics.opengl.StayTessellator
import me.alpha432.stay.util.inventory.InventoryUtil
import me.alpha432.stay.util.math.MathUtil
import me.alpha432.stay.util.math.vector.distanceSqTo
import me.alpha432.stay.util.math.vector.distanceTo
import me.alpha432.stay.util.player.CombatUtil.canSeeBlock
import me.alpha432.stay.util.player.RotationUtil
import me.alpha432.stay.util.world.BlockInteractionHelper
import me.alpha432.stay.util.world.CrystalUtil
import me.alpha432.stay.util.world.EntityUtils
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityEnderCrystal
import net.minecraft.entity.item.EntityEnderPearl
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityArrow
import net.minecraft.entity.projectile.EntityEgg
import net.minecraft.entity.projectile.EntitySnowball
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemSword
import net.minecraft.network.play.client.CPacketAnimation
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraft.network.play.server.*
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicInteger
import java.util.stream.Collectors
import kotlin.math.floor
import kotlin.math.max
import kotlin.streams.toList


@ApplyModule
object StayAura : Module("StayAura", "WTF lol", Category.COMBAT, true, false, false) {
    private val page by setting("Page", Pages.GENERAL)

    //General
    private val explode by setting("Explode", true).visible { page == Pages.GENERAL }
    private val place by setting("Place", true).visible { page == Pages.GENERAL }
    private val multiPlace by setting("MultiPlace", false).visible { page == Pages.GENERAL }
    private val packetExplode by setting("Packet Break", true).visible { page == Pages.GENERAL }
    private val autoSwitch by setting("Auto Switch", false).visible { page == Pages.GENERAL }
    private val switchingMode by setting(
        "Switch Mode",
        SwitchMode.NORMAL
    ).visible { page == Pages.GENERAL && autoSwitch }
    private val findingMode by setting("Sort By", TargetMode.DISTANCE).visible { page == Pages.GENERAL }
    private val enemyRange by setting("Enemy Range", 10.0, 0.0..20.0).visible { page == Pages.GENERAL }
    private val placeRange by setting("Place Range", 6.0, 0.0..8.0).visible { page == Pages.GENERAL }
        .limit { if (it <= enemyRange) it else enemyRange }
    private val breakRange by setting("Break Range", 6.0, 0.0..8.0).visible { page == Pages.GENERAL }
        .limit { if (it <= enemyRange) it else enemyRange }
    private val placeDelay by setting("Place Delay", 50, 0..200).visible { page == Pages.GENERAL }
    private val breakDelay by setting("Break Delay", 50, 0..200).visible { page == Pages.GENERAL }
    private val players by setting("Players", true).visible { page == Pages.GENERAL }
    private val animals by setting("Animals", false).visible { page == Pages.GENERAL }
    private val mobs by setting("Mobs", false).visible { page == Pages.GENERAL }
    private val rotate by setting("Rotation", true).visible { page == Pages.GENERAL }
    private val pauseWhileEating by setting("PauseWhileEating", false).visible { page == Pages.GENERAL }

    //Place
    private val newPlace by setting("1.13+", false).visible { page == Pages.PLACE }
    private val legitPlace by setting("LegitPlace", false).visible { page == Pages.PLACE }
    private val wallPlace by setting("Ignore Wall", true).visible { page == Pages.PLACE }
    private val wallPlaceRange by setting("Wall Range", 3.5, 0.0..6.0).visible { page == Pages.PLACE && wallPlace }
        .limit { if (it <= placeRange) it else placeRange }
    private val placeCalcMode by setting("Calc Mode", PlaceCalcMode.DEFAULT).visible { page == Pages.PLACE }
    private val minPlaceDmg by setting(
        "Min Dmg",
        4.0,
        0.0..20.0
    ).visible { page == Pages.PLACE && placeCalcMode != PlaceCalcMode.DYNAMIC }
    private val maxPlaceSelf by setting(
        "Max Self",
        10.0,
        0.0..20.0
    ).visible { page == Pages.PLACE && placeCalcMode != PlaceCalcMode.DYNAMIC }
    private val placeBalance by setting(
        "Balance",
        0.0,
        -10.0..10.0
    ).visible { page == Pages.PLACE && placeCalcMode != PlaceCalcMode.DEFAULT }
    private val motionPredict by setting("Predict", true).visible { page == Pages.PLACE }
    private val predictTicks by setting("Predict Ticks", 8, 1..20).visible { page == Pages.PLACE && motionPredict }
    private val facePlace by setting("Face Place", true).visible { page == Pages.PLACE }
    private val facePlaceHealth by setting("Min Health", 10.0, 0.0..36.0).visible { page == Pages.PLACE && facePlace }
    private val stopFpWhileSwording by setting("StopWhileSwording", false).visible { page == Pages.PLACE && facePlace }

    //Break
    private val breakWhen by setting("Break Mode", BreakMode.CALCULATION).visible { page == Pages.BREAK }
    private val predictHit by setting("Predict Hit", false).visible { page == Pages.BREAK }
    private val predictHitSize by setting("Predict Size", 0, 0..20).visible { page == Pages.BREAK && predictHit }
    private val breakHand by setting("Hand", MAIN).visible { page == Pages.BREAK }
    private val wallBreak by setting("Ignore Wall", true).visible { page == Pages.BREAK }
    private val wallBreakRange by setting("Wall Range", 3.5, 0.0..6.0).visible { page == Pages.BREAK && wallBreak }
        .limit { if (it <= breakRange) it else breakRange }
    private val breakCalcMode by setting("Calc Mode", DEFAULT).visible { page == Pages.BREAK }
    private val minBreakDmg by setting(
        "Min Dmg",
        4.0,
        0.0..20.0
    ).visible { page == Pages.BREAK && breakCalcMode != DYNAMIC }
    private val maxBreakSelf by setting(
        "Max Self",
        12.0,
        0.0..20.0
    ).visible { page == Pages.BREAK && breakCalcMode != DYNAMIC }
    private val breakBalance by setting(
        "Balance",
        0.0,
        -10.0..10.0
    ).visible { page == Pages.BREAK && breakCalcMode != DEFAULT }

    //Bypass
    private val bypass by setting("Bypass", false).visible { page == Pages.BYPASS }
    private val bypassMode by setting("Mode", BypassMode.ENTITYIGNORE).visible { page == Pages.BYPASS && bypass }

    //Modification
    private val actionPriority by setting("Action", ActionPriority.BREAK_PLACE).visible { page == Pages.Modification }
    private val popTotemTry by setting("TryPopTotem", false).visible { page == Pages.Modification }
    private val popTotemTryingProtection by setting(
        "TryPopProtection",
        false
    ).visible { page == Pages.Modification && popTotemTry }
    private val protectionMode by setting(
        "ProtectMode",
        PopTotemTryingProtection.CHANCE
    ).visible { page == Pages.Modification && popTotemTry && popTotemTryingProtection }
    private val trialFactor by setting(
        "TrialChance",
        100,
        0..100
    ).visible { page == Pages.Modification && popTotemTry && popTotemTryingProtection && protectionMode == PopTotemTryingProtection.CHANCE }
    private val trialMaxSelf by setting(
        "TrialMaxSelf",
        10,
        0..36
    ).visible { page == Pages.Modification && popTotemTry && popTotemTryingProtection && protectionMode == PopTotemTryingProtection.SELFDAMAGE }
    private val trialSelfHealth by setting(
        "TrialSelfHealth",
        10,
        0..36
    ).visible { page == Pages.Modification && popTotemTry && popTotemTryingProtection && protectionMode == PopTotemTryingProtection.SELFHEALTH }
    private val chase by setting("Chasing", false).visible { page == Pages.Modification }
    private val chasingFactor by setting("ChasingFactor", 20, 0..100).visible { page == Pages.Modification && chase }
    private val chasingMaxSelf by setting(
        "ChasingMaxSelf",
        30.0,
        0.0..36.0
    ).visible { page == Pages.Modification && chase }
    private val noSuicide by setting("NoSuicide", true).visible { page == Pages.Modification }
    private val damagePriority by setting(
        "DamagePriority",
        100,
        0..200
    ).visible { page == Pages.Modification && noSuicide }
    private val balanceProtection by setting("BalanceProtection", false).visible { page == Pages.Modification }
    private val globalBalance by setting(
        "GlobalBalance",
        0.0,
        -10.0..10.0
    ).visible { page == Pages.Modification && balanceProtection }
    private val balanceProtectionFactor by setting(
        "ProtectFactor",
        50,
        0..100
    ).visible { page == Pages.Modification && balanceProtection }
    private val facing by setting("Facing", EnumFacing.UP).visible { page == Pages.Modification }

    //Unsafe
    private val threads by setting("Threads", 10, 1..100).visible { page == Pages.UNSAFE }
    private val heavyCalc by setting("Heavy Calc", false).visible { page == Pages.UNSAFE }
    private val overrideMode by setting("Override Mode", TARGETDAMAGE).visible { page == Pages.UNSAFE && heavyCalc }
    private val nullCheckedReturn by setting(
        "Null Checked",
        NullChecked.FIRST
    ).visible { page == Pages.UNSAFE && heavyCalc }
//    private val pBConsecutive by setting("P-B Consecutive", false).visible { page == Pages.UNSAFE }

    //Render
    private val render by setting("Render", true).visible { page == Pages.RENDER }
    private val renderMode by setting("RenderMode", RenderMode.DYNAMIC).visible { page == Pages.RENDER && render }
    private val colorSync by setting("Sync", false).visible { page == Pages.RENDER && render }
    private val renderR by setting("Red", 255, 0..255).visible { page == Pages.RENDER && render && !colorSync }
    private val renderG by setting("Green", 255, 0..255).visible { page == Pages.RENDER && render && !colorSync }
    private val renderB by setting("Blue", 255, 0..255).visible { page == Pages.RENDER && render && !colorSync }
    private val renderA by setting("Alpha", 70, 0..255).visible { page == Pages.RENDER }
    private val outline by setting("Outline", true).visible { page == Pages.RENDER && render }
    private val width by setting("Line Width", 3.0, 0.0..5.0).visible { page == Pages.RENDER && render && outline }
    private val outlineA by setting("OutlineAlpha", 255, 0..255).visible { page == Pages.RENDER && render && outline }
    private val renderTR by setting("Text Red", 255, 0..255).visible { page == Pages.RENDER && render }
    private val renderTG by setting("Text Green", 255, 0..255).visible { page == Pages.RENDER && render }
    private val renderTB by setting("Text Blue", 255, 0..255).visible { page == Pages.RENDER && render }
    private val hudMode by setting("Hud Info", DELAY).visible { page == Pages.RENDER }

    private val isEating: Boolean
        get() = CrystalUtil.isEating()

    private val shouldProtectBalance: Boolean
        get() = ((0..100).random() in 0 until balanceProtectionFactor) && balanceProtection

    private val handledEntities: List<EntityLivingBase>
        get() {
            if (mc.world == null) return listOf()
            val entities: MutableList<Entity> = ArrayList()
            if (this.players) {
                entities.addAll(
                    mc.world.playerEntities.stream().filter { entityPlayer: EntityPlayer ->
                        !Stay.friendManager!!.isFriend(
                            entityPlayer.name
                        )
                    }.filter { entity: EntityPlayer ->
                        mc.player.getDistance(
                            entity
                        ).toDouble() < this.placeRange
                    }.collect(Collectors.toList())
                )
            }

            entities.addAll(
                mc.world.loadedEntityList.filter { entity: Entity? ->
                    if (EntityUtils.isLiving(
                            entity
                        ) && EntityUtils.isPassive(entity)
                    ) this.animals else this.mobs
                }.filter { entity: Entity -> entity !is EntityPlayer }.filter { entity: Entity ->
                    mc.player.getDistance(
                        entity
                    ).toDouble() < placeRange
                }
            )

            for (ite2 in ArrayList(entities)) {
                if (mc.player.getDistance(ite2).toDouble() > this.placeRange) {
                    entities.remove(ite2)
                }
                if (ite2 == mc.player) {
                    entities.remove(ite2)
                }
            }
            return entities.map { it as EntityLivingBase }.also { findingMode.handler(it) }.toList()
        }

    private val color: Color
        get() {
            val color = if (colorSync) ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.value) else Color(
                this.renderR,
                this.renderG,
                this.renderB,
                this.renderA
            )
            return Color(color.rgb)
        }

    private val EntityEnderCrystal.canBreak: Boolean
        get() {
            if (breakWhen == BreakMode.ALWAYS) return true
            synchronized(lock) {
                val targetDamage = CrystalUtil.calculateDamage(
                    this.posX,
                    this.posY,
                    this.posZ,
                    target ?: return false,
                    target?.positionVector ?: return false
                )
                val selfDamage = CrystalUtil.calculateDamage(
                    this.posX,
                    this.posY,
                    this.posZ,
                    mc.player ?: return false,
                    mc.player.positionVector ?: return false
                )
                val healthTarget = target!!.health + target!!.absorptionAmount
                val healthSelf = mc.player.health + mc.player.absorptionAmount
                var maxSelf = maxBreakSelf
                var minDamage = minBreakDmg
                var balance = breakBalance
                if (chase && healthSelf <= chasingMaxSelf) {
                    if ((0..100).random() in 0 until chasingFactor) {
                        maxSelf = Double.MAX_VALUE
                        balance = Double.MAX_VALUE
                    }
                }
                if (popTotemTry) {
                    if (!popTotemTryingProtection) {
                        if (healthTarget < targetDamage) {
                            maxSelf = Double.MAX_VALUE
                            balance = Double.MAX_VALUE
                        }
                    }
                    if (popTotemTryingProtection && protectionMode == PopTotemTryingProtection.CHANCE) {
                        if ((0..100).random() in (0 until trialFactor)) {
                            if (healthTarget < targetDamage) {
                                maxSelf = Double.MAX_VALUE
                                balance = Double.MAX_VALUE
                            }
                        }
                    }
                    if (popTotemTryingProtection && protectionMode == PopTotemTryingProtection.SELFDAMAGE) {
                        if (selfDamage <= trialMaxSelf) {
                            if (healthTarget < targetDamage) {
                                maxSelf = Double.MAX_VALUE
                                balance = Double.MAX_VALUE
                            }
                        }
                    }
                    if (popTotemTryingProtection && protectionMode == PopTotemTryingProtection.SELFHEALTH) {
                        if (healthSelf >= trialSelfHealth) {
                            if (healthTarget < targetDamage) {
                                maxSelf = Double.MAX_VALUE
                                balance = Double.MAX_VALUE
                            }
                        }
                    }
                }
                if (!wallBreak
                    && wallBreakRange > 0
                    && !CrystalUtil.canSeeBlock(this.position)
                    && CrystalUtil.getVecDistance(
                        this.position,
                        mc.player.posX,
                        mc.player.posY,
                        mc.player.posZ
                    ) >= wallBreakRange
                ) return false
                if (facePlace && healthTarget <= facePlaceHealth && !((mc.player.heldItemMainhand.item is ItemSword) && (mc.player.heldItemOffhand.item is ItemSword) && stopFpWhileSwording)) {
                    minDamage = 1.0
                }
                if (noSuicide) {
                    maxSelf *= (damagePriority.toDouble() / 100.0)
                }
                if (shouldProtectBalance) balance = globalBalance
                when (breakCalcMode) {
                    DEFAULT -> {
                        if (targetDamage < minDamage) return false
                        if (selfDamage > maxSelf) return false
                    }
                    DYNAMIC -> {
                        if ((targetDamage - selfDamage) < balance) return false
                    }
                    COMPLETELY -> {
                        if (targetDamage < minDamage
                            || selfDamage > maxSelf
                            || (targetDamage - selfDamage) < balance
                        ) return false
                    }
                }
            }
            return true
        }

    private val BlockPos.canPlaceCrystal: Boolean
        get() {
            val boost = this.add(0, 1, 0)
            val boost2 = this.add(0, 2, 0)
            if (mc.world.getBlockState(this).block !== Blocks.BEDROCK && mc.world.getBlockState(this).block !== Blocks.OBSIDIAN) return false
            if (!newPlace) {
                if (mc.world.getBlockState(boost).block !== Blocks.AIR || mc.world.getBlockState(boost2).block !== Blocks.AIR) return false
            } else if (mc.world.getBlockState(boost).block !== Blocks.AIR) return false
            if ((bypass && bypassMode == BypassMode.ENTITYIGNORE) || !multiPlace) {
                //Entity ignore would make the AutoCrystal faster
                return !(mc.world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB(boost)).isNotEmpty()
                        || mc.world.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB(boost)).isNotEmpty()
                        || mc.world.getEntitiesWithinAABB(EntityPlayer::class.java, AxisAlignedBB(boost2)).isNotEmpty()
                        || mc.world.getEntitiesWithinAABB(EntityArrow::class.java, AxisAlignedBB(boost)).isNotEmpty())
            }
            return (mc.world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(boost)).isEmpty()
                    && mc.world.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB(boost2)).isEmpty())
        }

    private val Entity.offset: Vec3d
        get() = Vec3d(this.posX - lastTickPosX, this.posY - lastTickPosY, this.posZ - lastTickPosZ)

    private val Entity.offsetX: Double
        get() = this.offset.x

    private val Entity.offsetY: Double
        get() = this.offset.y

    private val Entity.offsetZ: Double
        get() = this.offset.z

    private val placeTimer = Timer()
    private val breakTimer = Timer()
    private val packetTimer = Timer()
    private val predictBreakTimer = Timer()
    private var canPredict = false

    @Transient
    private val crystalID = AtomicInteger(-1)

    private var placement = 0
    private val smoothRender = BlockEasingRender(BlockPos(0, 0, 0), 500f, 500f)

    @Suppress("EXPERIMENTAL_API_USAGE")
    private var auraScope = CoroutineScope(newFixedThreadPoolContext(10, "StayAura"))

    private var renderPos: BlockPos? = null
    private var target: EntityLivingBase? = null
    private var selfD: Double? = null
    private var targetD: Double? = null

    private val lock = Any()

    private var rotating = false
    private var yaw = 0.0
    private var pitch = 0.0
    private var renderPitch = 0.0
    private var isSpoofingAngles = false

    private var lastPlaceTime = System.currentTimeMillis()
    private var currentPlaceTime = System.currentTimeMillis()
    private var currentAction = ""
    private var crystals = 0
    private var displayCount = 0
    private var update0 = 0
    private var updateAmount = 0

    private var countTimer = Timer()

    private var lastCrystal: EntityEnderCrystal? = null
    private val crystalLock = Any()

    private enum class Pages {
        GENERAL, PLACE, BREAK, BYPASS, Modification, UNSAFE, RENDER
    }

    private enum class TargetMode(val handler: (List<EntityLivingBase>) -> EntityLivingBase?) {
        DISTANCE({ list: List<EntityLivingBase> -> list.minByOrNull { it.getDistance(mc.player) } })
    }

    @Suppress("unused")
    private enum class SwitchMode {
        NORMAL, GHOSTHAND
    }

    private enum class PlaceCalcMode {
        DEFAULT, DYNAMIC, COMPLETELY
    }

    private enum class BreakMode {
        ALWAYS, CALCULATION
    }

    private enum class BreakHand {
        MAIN, OFFHAND, NONE
    }

    private enum class BreakCalcMode {
        DEFAULT, DYNAMIC, COMPLETELY
    }

    private enum class BypassMode {
        ENTITYIGNORE
    }

    @Suppress("unused")
    private enum class ActionPriority {
        PLACE_BREAK, BREAK_PLACE
    }

    private enum class PopTotemTryingProtection {
        CHANCE, SELFDAMAGE, SELFHEALTH
    }

    private enum class OverrideMode {
        TARGETDAMAGE, SELFDAMAGE, DISTANCE
    }

    @Suppress("unused")
    private enum class NullChecked {
        FIRST, SECOND
    }

    @Suppress("unused")
    private enum class RenderMode {
        CLASSIC, DYNAMIC
    }

    private enum class HudInfoMode {
        TARGET, DELAY, ACTION, SPEED, DMG, DEBUG, NONE
    }

    init {

        onEnable {
            update0 = 0
            updateAmount = 0
            placeTimer.reset()
            breakTimer.reset()
            packetTimer.reset()
            predictBreakTimer.reset()
            crystalID.set(-1)
            crystals = 0
            lastCrystal = null
            canPredict = false
            displayCount = 0
            placement = 0
            @Suppress("EXPERIMENTAL_API_USAGE")
            // FIXME: 2021/12/4 Exceptions in coroutines
            auraScope = CoroutineScope(newFixedThreadPoolContext(threads, "StayAura"))
            smoothRender.reset()
        }

        onDisable {
            rotating = false
            resetRotation()
        }

        onTick {
            if (countTimer.passedMs(1000)) {
                displayCount = crystals
                crystals = 0
                updateAmount = update0
                update0 = 0
                countTimer.reset()
            }
        }

        onRenderTick {
            try {
                if (fullNullCheck()) return@onRenderTick
                update0++
                if (placeTimer.passedMs(1050.0) || breakTimer.passedMs(1050.0)) {
                    rotating = false
                }

                if (actionPriority == ActionPriority.BREAK_PLACE) {
                    try {
                        doBreak()
                        doPlace()
                    } catch (_: Exception) {
                        NotificationManager.push(
                            "[ThreadSafety] I've prevent you from a crash!",
                            NotificationType.INFO,
                            800
                        )
                    }
                } else {
                    try {
                        doPlace()
                        doBreak()
                    } catch (_: Exception) {
                        NotificationManager.push(
                            "[ThreadSafety] I've prevent you from a crash!",
                            NotificationType.INFO,
                            800
                        )
                    }
                }
            } catch (e: Exception) {
                Command.sendMessage("Error while updating StayAura!")
                Stay.LOGGER.error("Error while updating StayAura!", e)
            }
        }

        onRender3D {
            if (render) {
                synchronized(lock) {
                    if (renderPos != null) {
                        smoothRender.begin()
                    } else {
                        smoothRender.end()
                    }
                    if (renderMode == RenderMode.CLASSIC) {
                        if (renderPos != null) {
                            RenderUtil.drawBoxESP(
                                renderPos,
                                if (colorSync) ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.value) else Color(
                                    this.renderR,
                                    this.renderG,
                                    this.renderB,
                                    this.renderA
                                ),
                                true,
                                if (colorSync) color else Color(
                                    this.renderR,
                                    this.renderG,
                                    this.renderB,
                                    outlineA
                                ),
                                width.toFloat(),
                                outline,
                                true,
                                renderA,
                                false
                            )
                        }
                    } else {
                        var (bb, vec3d) = smoothRender.getBBAndVec3dUpdate()
                        val interpolateEntity: Vec3d = MathUtil.interpolateEntity(mc.player, mc.renderPartialTicks)
                        bb = bb.offset(-interpolateEntity.x, -interpolateEntity.y, -interpolateEntity.z)
//                        RenderUtil.drawBBBox(bb, color, renderA)
                        GlStateManager.pushMatrix()
                        StayTessellator.drawBoxTest(
                            bb.minX.toFloat(),
                            bb.minY.toFloat(),
                            bb.minZ.toFloat(),
                            bb.maxX.toFloat() - bb.minX.toFloat(),
                            bb.maxY.toFloat() - bb.minY.toFloat(),
                            bb.maxZ.toFloat() - bb.minZ.toFloat(),
                            color.red,
                            color.green,
                            color.blue,
                            this.renderA,
                            63
                        )
                        if (outline) {
                            StayTessellator.drawBoundingBox(
                                bb,
                                width.toFloat(),
                                color.red,
                                color.green,
                                color.blue,
                                outlineA
                            )
                        }
                        GlStateManager.popMatrix()
                        if (!smoothRender.isEnded) {
                            GlStateManager.pushMatrix()
                            GlStateManager.shadeModel(GL11.GL_SMOOTH)
                            StayTessellator.glBillboardDistanceScaled(
                                vec3d.x.toFloat() + 0.5f,
                                vec3d.y.toFloat() + 0.5f,
                                vec3d.z.toFloat() + 0.5f,
                                mc.player,
                                1f
                            )
                            val damage = targetD
                            val damageText = if (damage != null) {
                                (if (floor(damage.toDouble()) == damage.toDouble()) damage else String.format(
                                    "%.1f",
                                    damage
                                )).toString() + ""
                            } else "NaN"

                            GlStateManager.disableDepth()
                            GlStateManager.translate(-(mc.fontRenderer.getStringWidth(damageText) / 2.0), 0.0, 0.0)
                            GlStateManager.scale(1f, 1f, 1f)
                            Stay.textManager!!.drawString(
                                damageText,
                                1F,
                                1F,
                                Color(renderTR, renderTG, renderTB).rgb,
                                true
                            )
//                            RenderUtil.drawText(bb, damageText)
                            GlStateManager.popMatrix()
                        }
                    }
                }
            }
        }

        safeListener<RenderWorldLastEvent> {
            if (rotate) {
                if (rotating) {
                    mc.player.rotationYawHead = this.yaw.toFloat()
                    mc.player.renderYawOffset = this.yaw.toFloat()
                }
            }
        }
    }

    @SubscribeEvent
    fun onPacketReceive(event: me.alpha432.stay.event.PacketEvent.Receive) {
        when (event.packet) {
            is SPacketSpawnExperienceOrb -> {
                crystalID.set(-1)
                return
            }
            is SPacketSpawnGlobalEntity -> {
                crystalID.set(-1)
                return
            }
            is SPacketSpawnMob -> {
                crystalID.getAndUpdate {
                    max(it, event.packet.entityID)
                }
                return
            }
            is SPacketSoundEffect -> {
                val packet = event.packet
                if (packet.category == SoundCategory.BLOCKS && packet.sound == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    CopyOnWriteArrayList(mc.world.loadedEntityList.stream()
                        .filter { it is EntityEnderCrystal }
                        .filter {
                            it.getDistance(
                                packet.x,
                                packet.y,
                                packet.z
                            ) <= 6.0f
                        }.toList()).forEach { obj: Entity? -> obj?.setDead() }
                }
                if (packet.sound == SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW || packet.sound == SoundEvents.ENTITY_ARROW_SHOOT || packet.sound == SoundEvents.ENTITY_ITEM_BREAK) {
                    canPredict = false
                }
                return
            }
            is SPacketSpawnObject -> {
                if (predictHit) {
                    mc.world.loadedEntityList.forEach { e ->
                        if (e is EntityItem || e is EntityArrow || e is EntityEnderPearl || e is EntitySnowball || e is EntityEgg) {
                            if (e.getDistance(event.packet.x, event.packet.y, event.packet.z) <= 6) {
                                crystalID.set(-1)
                                canPredict = false
                                event.isCanceled = true
                                return
                            }
                        }
                    }
                }

                if (event.packet.type == 51) {
                    crystals++
                    crystalID.getAndUpdate {
                        max(
                            it,
                            event.packet.entityID
                        )
                    }
                    if (packetExplode) packetBreak(mc.player, event.packet)
                } else {
                    crystalID.set(-1)
                }
                return
            }
            is SPacketSpawnPainting -> {
                crystalID.set(-1)
            }
            is SPacketSpawnPlayer -> {
                crystalID.getAndUpdate {
                    max(it, event.packet.entityID)
                }
            }
            else -> return
        }
    }

    @SubscribeEvent
    fun onPacketSend(event: me.alpha432.stay.event.PacketEvent.Send) {
        if (event.packet is CPacketPlayer && this@StayAura.isSpoofingAngles) {
            event.packet.yaw = this@StayAura.yaw.toFloat()
            event.packet.pitch = this@StayAura.pitch.toFloat()
            this@StayAura.isSpoofingAngles = false
        }
        if (event.packet is CPacketAnimation && breakHand == BreakHand.NONE) {
            event.isCanceled = true
        }
    }

    //region Rotation
    private inline fun lookAtPos(block: BlockPos, face: EnumFacing) {
        val v = RotationUtil.getRotationsBlock(block, face, false)
        val v2 = RotationUtil.getRotationsBlock(block.add(0.0, 0.5, 0.0), face, false)
        setYawAndPitch(v[0], v[1], v2[1])
    }

    private suspend inline fun getPlaceBlocks(): List<BlockPos> {
        if (mc.player == null) return mutableListOf()
        val handler: Deferred<List<BlockPos>>
        @Suppress("BlockingMethodInNonBlockingContext") runBlocking {
            handler = async {
                val positions = mutableListOf<BlockPos>()
                positions.addAll(
                    BlockInteractionHelper.getSphere(
                        mc.player.position, placeRange.toFloat(), placeRange.toInt(),
                        false,
                        true,
                        0
                    ).stream()
                        .filter { it.canPlaceCrystal }.collect(Collectors.toList())
                )
                positions
            }
        }
        return handler.await()
    }

    private inline fun lookAtCrystal(ent: EntityEnderCrystal) {
        val v = RotationUtil.getRotations(mc.player.getPositionEyes(mc.renderPartialTicks), ent.positionVector)
        val v2 = RotationUtil.getRotations(
            mc.player.getPositionEyes(mc.renderPartialTicks),
            ent.positionVector.add(0.0, -0.5, 0.0)
        )
        setYawAndPitch(v[0], v[1], v2[1])
    }

    private inline fun setYawAndPitch(yaw1: Float, pitch1: Float, renderPitch1: Float) {
        yaw = yaw1.toDouble()
        this.pitch = pitch1.toDouble()
        this.renderPitch = renderPitch1.toDouble()
        this.isSpoofingAngles = true
        this.rotating = true
    }

    private inline fun resetRotation() {
        if (this.isSpoofingAngles) {
            this.yaw = mc.player.rotationYaw.toDouble()
            this.pitch = mc.player.rotationPitch.toDouble()
            this.isSpoofingAngles = false
            this.rotating = false
        }
    }
    //endregion

    //region Main
    private inline fun calculate(): CrystalTarget {
        return if (heavyCalc) doubleCalculate() else calculate0()
    }

    private inline fun doubleCalculate(): CrystalTarget {
        val result1 = calculate0()
        val result2 = calculate0()
        when (overrideMode) {
            TARGETDAMAGE -> {
                return if (result1.targetDamage > result2.targetDamage) {
                    result1
                } else {
                    result2
                }
            }
            SELFDAMAGE -> {
                return if (result1.selfDamage < result2.selfDamage) {
                    result1
                } else {
                    result2
                }
            }
            DISTANCE -> {
                if (result1.blockPos == null || result2.blockPos == null) {
                    return if (nullCheckedReturn == NullChecked.FIRST) result1 else result2
                }
                return if (result1.blockPos.distanceTo(mc.player.positionVector) > result2.blockPos.distanceSqTo(mc.player.positionVector)) {
                    result2
                } else result1
            }
        }
    }

    private inline fun calculate0(): CrystalTarget {
        val handler: CrystalTarget
        @Suppress("BlockingMethodInNonBlockingContext") runBlocking {
            handler = withContext(Dispatchers.Default) {
                var tempPos: BlockPos? = null
                var target: EntityLivingBase? = null
                var selfdmg = 0.0
                var damage = 0.5
                var placeMinDmg = minPlaceDmg
                var maxSelf = maxPlaceSelf
                var balance = placeBalance
                val crystalBlocks = getPlaceBlocks()
                val entities = mutableListOf<EntityLivingBase>()
                try {
                    entities.addAll(handledEntities
                        .filter { it != mc.player }
                        .filter { it.health >= 0.0 }
                    )
                } catch (e: Exception) {
                    NotificationManager.push(
                        "[ThreadSafety] I've prevent you from a crash!",
                        NotificationType.INFO,
                        800
                    )
                }
                val entity = when (findingMode) {
                    TargetMode.DISTANCE -> findingMode.handler(entities)
                }
                if (entity == null) canPredict = false
                entity?.let { e ->
                    canPredict = (predictHit || !e.heldItemMainhand.getItem().equals(Items.EXPERIENCE_BOTTLE))
                            && !e.heldItemOffhand.getItem().equals(Items.EXPERIENCE_BOTTLE)
                    val targetVec = if (motionPredict) Vec3d(
                        e.offsetX * predictTicks + e.posX,
                        e.offsetY * predictTicks + e.posY,
                        e.offsetZ * predictTicks + e.posZ
                    ) else e.positionVector
                    for (blockPos in crystalBlocks.filter { e.getDistanceSq(it) < placeRange.sq }) {
                        if (!wallPlace
                            && wallPlaceRange > 0 && !CrystalUtil.canSeeBlock(blockPos)
                            && CrystalUtil.getVecDistance(
                                blockPos,
                                mc.player.posX,
                                mc.player.posY,
                                mc.player.posZ
                            ) >= wallPlaceRange
                        ) continue
                        val targetDmg = CrystalUtil.calculateDamage(
                            blockPos.x + 0.5,
                            blockPos.y + 1.0,
                            blockPos.z + 0.5,
                            e,
                            targetVec
                        ).toDouble()
                        if (targetDmg < damage) continue
                        val healthTarget = e.health + e.absorptionAmount
                        val healthSelf = mc.player.health + mc.player.absorptionAmount
                        val selfDamage = CrystalUtil.calculateDamage(
                            blockPos.x + 0.5,
                            (blockPos.y + 1).toDouble(),
                            blockPos.z + 0.5,
                            mc.player
                        ).toDouble()
                        if (facePlace && (healthTarget <= facePlaceHealth) && !((mc.player.heldItemMainhand.item is ItemSword) && (mc.player.heldItemOffhand.item is ItemSword) && stopFpWhileSwording)) {
                            placeMinDmg = 1.0
                        }
                        if (popTotemTry && (targetDmg > healthTarget)) {
                            maxSelf = Double.MAX_VALUE
                            balance = Double.MAX_VALUE
                        }
                        if (chase && healthSelf <= chasingMaxSelf) {
                            if ((0..100).random() in 0 until chasingFactor) {
                                maxSelf = Double.MAX_VALUE
                                balance = Double.MAX_VALUE
                            }
                        }
                        if (noSuicide) {
                            maxSelf *= (damagePriority / 100.0)
                        }
                        if (shouldProtectBalance) balance = globalBalance
                        when (placeCalcMode) {
                            PlaceCalcMode.DEFAULT -> {
                                if (targetDmg < placeMinDmg
                                    || selfDamage > maxSelf
                                ) continue
                            }
                            PlaceCalcMode.DYNAMIC -> {
                                if ((targetDmg - selfDamage) > balance) continue
                            }
                            PlaceCalcMode.COMPLETELY -> {
                                if (targetDmg < placeMinDmg
                                    || selfDamage > maxSelf
                                    || (targetDmg - selfDamage) > balance
                                ) continue
                            }
                        }
                        damage = targetDmg
                        selfdmg = selfDamage
                        tempPos = blockPos
                        target = e
                    }
                }
                CrystalTarget(tempPos, target, damage, selfdmg)
            }
        }
        return handler
    }

    private fun packetBreak(player: EntityPlayerSP, packet: SPacketSpawnObject) {
        if (!packetTimer.passedMs(breakDelay.toLong())) return
        val distance = player.getDistance(packet.x, packet.y, packet.z)
        if (distance > breakRange) return
        val pos = Vec3d(packet.x, packet.y, packet.z)
        if (wallBreak && distance > wallBreakRange && !canSeeBlock(BlockPos(pos))) return
        val attackPacket = CPacketUseEntity()
        attackPacket.entityId = packet.entityID
        attackPacket.action = CPacketUseEntity.Action.ATTACK
        mc.player.connection.sendPacket(attackPacket)
        if (rotate) lookAtPos(BlockPos(pos), facing)
        packetTimer.reset()
    }

    private inline fun doPlace() {
        if (!place || !placeTimer.passedMs(placeDelay.toLong())) return
        if (placement > 1 && !multiPlace) return
        if (isEating && pauseWhileEating) return
        val (pos, target, targetD, selfD) = calculate()
        synchronized(lock) {
            this.renderPos = pos
            this.target = target
            this.targetD = targetD
            this.selfD = selfD
        }
        var lastHotbar = mc.player.inventory.currentItem
        val hand: EnumHand = if (!autoSwitch) {
            if (mc.player.heldItemMainhand.item == Items.END_CRYSTAL)
                EnumHand.MAIN_HAND
            else EnumHand.OFF_HAND
        } else {
            if (mc.player.heldItemMainhand.item != Items.END_CRYSTAL && mc.player.heldItemOffhand.item != Items.END_CRYSTAL) {
                if (switchingMode == SwitchMode.NORMAL) {
                    switch()
                } else {
                    lastHotbar = mc.player.inventory.currentItem
                    mc.player.inventory.currentItem = InventoryUtil.findItemInHotbar(Items.END_CRYSTAL)
                    mc.playerController.updateController()
                }
            }
            EnumHand.MAIN_HAND
        }
        resetRotation()
        if (pos != null && target != null) {
            val placePos: BlockPos = pos
//            Stay.LOGGER.info("Trying placing Post")
            if (rotate) lookAtPos(placePos, facing)
            smoothRender.updatePos(placePos)
            lastPlaceTime = currentPlaceTime
            currentPlaceTime = System.currentTimeMillis()
            currentAction = "Place"
            if (legitPlace) {
                mc.playerController.processRightClickBlock(
                    mc.player,
                    mc.world,
                    placePos,
                    facing,
                    Vec3d(placePos.x.toDouble(), placePos.y.toDouble(), placePos.z.toDouble()),
                    hand
                )
            } else {
                mc.player.connection.sendPacket(
                    CPacketPlayerTryUseItemOnBlock(
                        placePos,
                        facing,
                        hand,
                        0f,
                        0f,
                        0f
                    )
                )
            }
            if (predictHit && predictHitSize > 0 && canPredict) {
                val syncedId: Int = crystalID.get()
                val count = AtomicInteger(0)
                repeat(predictHitSize) {
                    if (syncedId != -1) {
                        val attackPacket = CPacketUseEntity()
                        val id = syncedId + count.getAndIncrement() + 1
//                        if (mc.world.getEntityByID(id) !is EntityEnderCrystal) return
                        attackPacket.entityId = id
                        attackPacket.action = CPacketUseEntity.Action.ATTACK
                        mc.player.connection.sendPacket(attackPacket)
                    }
                }
            }
            placement++
        }
        if (lastHotbar != -1 && switchingMode == SwitchMode.GHOSTHAND) {
            mc.player.inventory.currentItem = lastHotbar
            mc.playerController.updateController()
        }
        placeTimer.reset()
    }

    private inline fun doBreak() {
        if (!explode || !breakTimer.passedMs(breakDelay.toLong())) return
        resetRotation()
        currentAction = "Break"
        breakTimer.reset()
        mc.world.loadedEntityList
            .filterIsInstance<EntityEnderCrystal>()
            .filter { it.canBreak }
            .minByOrNull { it.getDistance(mc.player) }
            ?.let { crystal ->
                synchronized(crystalLock) {
                    lastCrystal = crystal
                }
                if (this.rotate) {
                    lookAtCrystal(crystal)
                }
                explode(crystal)
            }
        placement = 0
    }

    private inline fun explode(entity: EntityEnderCrystal) {
        mc.playerController.attackEntity(mc.player, entity)
        when (breakHand) {
            MAIN -> mc.player.swingArm(EnumHand.MAIN_HAND)
            OFFHAND -> mc.player.swingArm(EnumHand.OFF_HAND)
            BreakHand.NONE -> return
        }
    }

    //endregion

    private inline fun switch() {
        if (switchingMode == SwitchMode.NORMAL) {
            InventoryUtil.switchToSlot(Items.END_CRYSTAL)
        } else {
            Command.sendMessage("GhostHand is not supported!")
        }
    }

    override fun getDisplayInfo(): String? {
        return when (hudMode) {
            TARGET -> target?.name ?: "Finding..."
            DELAY -> if (target == null) "NaN" else (currentPlaceTime - lastPlaceTime).toString()
            ACTION -> if (target == null) "Waiting" else currentAction
            SPEED -> if (target == null) "NaN" else displayCount.toString()
            NONE -> null
            DMG -> if (target == null) "0.0/0.0" else "${String.format("%.1f", targetD)}/${
                String.format(
                    "%.1f",
                    selfD
                )
            }"
            DEBUG -> "${target?.name} $updateAmount $displayCount ${Stay.serverManager!!.ping}ms ${Stay.serverManager!!.tps} ${String.format("%.1f", targetD)}/${String.format("%.1f", selfD)}"
        }
    }

    private data class CrystalTarget(
        val blockPos: BlockPos?,
        val target: EntityLivingBase?,
        val targetDamage: Double,
        val selfDamage: Double
    )
}