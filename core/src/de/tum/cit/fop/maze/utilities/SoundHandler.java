package de.tum.cit.fop.maze.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.time.Duration;
import java.time.Instant;

public class SoundHandler {

    public boolean swordSlashSoundPlayed;
    public Sound swordSlash;
    public Instant startedPlaying;

    public Sound enemyDeath;
    public Sound keyPickup;
    public Sound healthRestore;

    int counter = 0;

    public SoundHandler(){
        this.swordSlash = Gdx.audio.newSound(Gdx.files.internal("sounds/sword-swing.mp3"));
        this.enemyDeath =  Gdx.audio.newSound(Gdx.files.internal("sounds/enemy-death.mp3"));
        this.keyPickup = Gdx.audio.newSound(Gdx.files.internal("sounds/item-pickup.mp3"));
        this.healthRestore = Gdx.audio.newSound(Gdx.files.internal("sounds/health-restore.mp3"));
        this.swordSlashSoundPlayed = false;
    }


    public void playSwordSlash() {
        counter++;
        System.out.println(counter);
        this.swordSlash.play();
        startedPlaying = Instant.now();
        this.swordSlashSoundPlayed = true;
    }

    public boolean canSlash(){

        if (!this.swordSlashSoundPlayed){
            return true;
        }
        Instant now = Instant.now();
        long milliSecondsBetween = Duration.between(this.startedPlaying, now).toMillis();
        if (milliSecondsBetween > 500 ){
            this.swordSlashSoundPlayed = false;
            this.startedPlaying = null;
            return true;
        }
        return false;
    }
}
