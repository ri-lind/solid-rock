package de.tum.cit.fop.maze.objects.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.tum.cit.fop.maze.objects.Player;

public class Heart {

    public Sprite sprite;
    public int remaining_life;
    private static final String filePath = "objects.png";

    public Heart(Player player){
        Texture texture = new Texture(Gdx.files.internal(filePath));
        TextureRegion textureRegion = new TextureRegion(texture, 4* 16, 0,16, 16);
        this.sprite = new Sprite(textureRegion);
        this.sprite.setSize(16, 16);
        this.sprite.setX(player.sprite.getX());
        this.sprite.translateX(-(this.sprite.getWidth()));
        this.sprite.setY(player.sprite.getY());
        this.sprite.translateY(player.sprite.getHeight());

        this.remaining_life = 4;
    }

    public void sustainsDamage(){
        remaining_life--;
        Texture texture = new Texture(Gdx.files.internal(filePath));
        TextureRegion textureRegion;
        if(remaining_life == 3){
            textureRegion = new TextureRegion(texture, 5*16, 0, 16, 16);
        } else if(remaining_life == 2){
            textureRegion = new TextureRegion(texture, 6*16, 0, 16, 16);
        } else if (remaining_life == 1){
            textureRegion = new TextureRegion(texture, 7*16, 0, 16, 16);
        } else {
            textureRegion = new TextureRegion(texture, 8*16, 0, 16 ,16);
        }
        Sprite newHeart = new Sprite(textureRegion);
        newHeart.setPosition(sprite.getX(), sprite.getY());
        newHeart.setSize(sprite.getWidth(), sprite.getHeight());
        this.sprite = newHeart;
    }

    public void restoreHealth(){
        remaining_life++;
        Texture texture = new Texture(Gdx.files.internal(filePath));
        TextureRegion textureRegion;
        if (remaining_life == 4){
            textureRegion = new TextureRegion(texture, 4*16, 0, 16, 16);
        } else if(remaining_life == 3){
            textureRegion = new TextureRegion(texture, 5*16, 0, 16, 16);
        } else if(remaining_life == 2){
            textureRegion = new TextureRegion(texture, 6*16, 0, 16, 16);
        } else if (remaining_life == 1){
            textureRegion = new TextureRegion(texture, 7*16, 0, 16, 16);
        } else {
            textureRegion = new TextureRegion(texture, 8*16, 0, 16 ,16);
        }
        Sprite newHeart = new Sprite(textureRegion);
        newHeart.setPosition(sprite.getX(), sprite.getY());
        newHeart.setSize(sprite.getWidth(), sprite.getHeight());
        this.sprite = newHeart;
    }
}
