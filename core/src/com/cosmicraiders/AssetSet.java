package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

/**
 * This class saves textures, sounds and music as static attributes.
 */
public class AssetSet {

    public static Texture fighterImage;
    public static Texture fighterRedImage;

    public static Texture alienImage;
    public static Texture laserRedImage;
    public static Texture laserGreenImage;
    public static Texture asteroidImage;
    public static Texture asteroidExplosionImage;
    public static Texture starBackgroundImage;
    public static Texture cosmicRaidersLogoImage;
    public static Texture powerUpImage;

    public static Music neonNoir;

    public static Sound blasterShoot;
    public static Sound explosion;
    public static Sound alienSpawn;
    public static Sound alienLaser;
    public static Sound alienExplosion;
    public static Sound asteroidExplosion;
    public static Sound fighterLaser;
    public static Sound fighterExplosion;



    // more sounds on https://www.weloveindies.com/en/sounds-for-games
    // Neon Noir: Music by Karl Casey @ White Bat Audio

    /**
     * This method converts a file to a Texture object.
     * @param fileName the file to be converted
     * @return the Texture object made from the file
     */
    public static Texture loadTexture(String fileName) {
        return new Texture(Gdx.files.internal(fileName));
    }

    /**
     * Loads the images for the aliens and the fighter using loadTexture method.
     * Load the drop sound effect and the background music.
     */
    public static void load() {

        fighterImage = loadTexture("fighter/starFighter.png");
        fighterRedImage = loadTexture("fighter/starFighterRed.png");

        alienImage = loadTexture("alien.png");
        laserRedImage = loadTexture("laserRed.png");
        laserGreenImage = loadTexture("laserGreen.png");
        asteroidImage = loadTexture("asteroid.png");
        asteroidExplosionImage = loadTexture("asteroidExplosion.png");
        starBackgroundImage = loadTexture("stars.png");
        cosmicRaidersLogoImage = loadTexture("cosmicRaidersLogo.png");
        powerUpImage = loadTexture("powerUp.png");

        blasterShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/blasterShoot.mp3"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));
        neonNoir = Gdx.audio.newMusic(Gdx.files.internal("sounds/NeonNoir.mp3"));

        asteroidExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/asteroidExplosion.wav"));
        alienSpawn = Gdx.audio.newSound(Gdx.files.internal("sounds/alienSpawn.wav"));
        alienLaser = Gdx.audio.newSound(Gdx.files.internal("sounds/alienLaser.wav"));
        alienExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/alienExplosion.wav"));
        fighterLaser = Gdx.audio.newSound(Gdx.files.internal("sounds/fighterLaser.wav"));
        fighterExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/fighterExplosion.wav"));
    }

    /**
     * Disposes of the textures.
     * Disposes of the sounds and music.
     */
    public static void dispose() {
        fighterImage.dispose();
        fighterRedImage.dispose();
        alienImage.dispose();
        laserRedImage.dispose();
        laserGreenImage.dispose();
        asteroidImage.dispose();
        asteroidExplosionImage.dispose();
        starBackgroundImage.dispose();
        cosmicRaidersLogoImage.dispose();
        powerUpImage.dispose();

        blasterShoot.dispose();
        explosion.dispose();
        alienSpawn.dispose();
        alienLaser.dispose();
        alienExplosion.dispose();
        fighterLaser.dispose();
        fighterExplosion.dispose();
    }
}


