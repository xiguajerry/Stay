/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.util.songs;

import me.alpha432.stay.util.interfaces.Globals;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import org.jetbrains.annotations.NotNull;

public class DontStop implements Globals {

    public static final ISound sound;
    private static final String song = "dontstop";
    private static final ResourceLocation loc = new ResourceLocation("sounds/" + song + ".ogg");

    static {
        sound = new ISound() {

            private final int pitch = 1;
            private final int volume = 1;

            @NotNull
            @Override
            public ResourceLocation getSoundLocation() {
                return loc;
            }

            @NotNull
            @Override
            public SoundEventAccessor createAccessor(@NotNull SoundHandler soundHandler) {
                return new SoundEventAccessor(loc, "Pitbull");
            }

            @NotNull
            @Override
            public Sound getSound() {
                return new Sound(song, volume, pitch, 1, Sound.Type.SOUND_EVENT, false);
            }

            @NotNull
            @Override
            public SoundCategory getCategory() {
                return SoundCategory.VOICE;
            }

            @Override
            public boolean canRepeat() {
                return true;
            }

            @Override
            public int getRepeatDelay() {
                return 2;
            }

            @Override
            public float getVolume() {
                return volume;
            }

            @Override
            public float getPitch() {
                return pitch;
            }

            @Override
            public float getXPosF() {
                return 1;
            }

            @Override
            public float getYPosF() {
                return 0;
            }

            @Override
            public float getZPosF() {
                return 0;
            }

            @NotNull
            @Override
            public AttenuationType getAttenuationType() {
                return AttenuationType.LINEAR;
            }
        };
    }

}
