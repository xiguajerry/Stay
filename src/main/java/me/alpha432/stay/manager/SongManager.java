/*
 * Copyright (c) 2021-2021
 * *********** Reversing Team and Stay Development Team.
 * All Rights Reserved.
 *
 * ***********' Github website: https://github.com/ ***********
 * This file was created by SagiriXiguajerry at 2021/11/11 上午1:33
 */

package me.alpha432.stay.manager;

import me.alpha432.stay.util.interfaces.Globals;
import me.alpha432.stay.util.songs.DontStop;
import me.alpha432.stay.util.songs.FireBall;
import me.alpha432.stay.util.songs.HotelRoom;
import net.minecraft.client.audio.ISound;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SongManager implements Globals {

    private final List<ISound> songs = Arrays.asList(
            FireBall.sound,
            HotelRoom.sound,
            DontStop.sound
    );

    private final ISound menuSong;
    private ISound currentSong;

    public SongManager() {
        this.menuSong = this.getRandomSong();
        this.currentSong = this.getRandomSong();
    }

    public ISound getMenuSong() {
        return this.menuSong;
    }

    public void skip() {
        boolean flag = isCurrentSongPlaying();
        if (flag) {
            this.stop();
        }
        this.currentSong = songs.get((songs.indexOf(currentSong) + 1) % songs.size());
        if (flag) {
            this.play();
        }
    }

    public void play() {
        if (!this.isCurrentSongPlaying()) {
            mc.soundHandler.playSound(currentSong);
        }
    }

    public void stop() {
        if (this.isCurrentSongPlaying()) {
            mc.soundHandler.stopSound(currentSong);
        }
    }

    private boolean isCurrentSongPlaying() {
        return mc.soundHandler.isSoundPlaying(currentSong);
    }

    public void shuffle() {
        this.stop();
        Collections.shuffle(this.songs);
    }

    private ISound getRandomSong() {
        return songs.get(random.nextInt(songs.size()));
    }

}
