package de.tum.cit.fop.maze.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundHandler {

    public Sound swordSlash;
    public Sound enemyDeath;
    public Sound keyPickup;
    public Sound healthRestore;
    public Sound playerMovement;

    public SoundHandler(){
        this.swordSlash = Gdx.audio.newSound(Gdx.files.internal("sounds/sword-swing.mp3"));
        this.enemyDeath =  Gdx.audio.newSound(Gdx.files.internal("sounds/enemy-death.mp3"));
        this.keyPickup = Gdx.audio.newSound(Gdx.files.internal("sounds/item-pickup.mp3"));
        this.healthRestore = Gdx.audio.newSound(Gdx.files.internal("sounds/health-restore.mp3"));
        this.playerMovement = Gdx.audio.newSound(Gdx.files.internal("sounds/player-movement.mp3"));

    }
}
