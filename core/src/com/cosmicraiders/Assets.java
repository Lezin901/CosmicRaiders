package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    public static Texture alienImage;
    public static Texture fighterImage;
    public static Texture laserRedImage;
    public static Texture laserGreenImage;
    public static Texture asteroidImage;
    public static Texture asteroidExplosionImage;
    public static Texture starBackgroundImage;
    public static Texture supernovaImage;
    public static Texture nebulaImage;
    public static Sound blasterShoot;
    public static Sound explosion;
    public static Music beepbop;

    public static Texture loadTexture(String fileName) {
        return new Texture(Gdx.files.internal(fileName));
    }

    public static void load() {
        // load the images for the aliens and the fighter using loadTexture method
        alienImage = loadTexture("alien.png");
        fighterImage = loadTexture("starFighter.png");
        laserRedImage = loadTexture("laserRed.png");
        laserGreenImage = loadTexture("laserGreen.png");
        asteroidImage = loadTexture("asteroid.png");
        asteroidExplosionImage = loadTexture("asteroidExplosion.png");
        starBackgroundImage = loadTexture("stars.png");
        nebulaImage = loadTexture("nebula.png");
        supernovaImage = loadTexture("supernovas.png");

        // load the drop sound effect and the background "music"
        blasterShoot = Gdx.audio.newSound(Gdx.files.internal("blasterShoot.mp3"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
        beepbop = Gdx.audio.newMusic(Gdx.files.internal("beepbop.mp3"));
    }
    public static void dispose() {
        // Dispose of the textures
        alienImage.dispose();
        fighterImage.dispose();
        laserRedImage.dispose();
        laserGreenImage.dispose();
        asteroidImage.dispose();
        asteroidExplosionImage.dispose();
        starBackgroundImage.dispose();
        supernovaImage.dispose();
        nebulaImage.dispose();

        // Dispose of the sounds and music
        blasterShoot.dispose();
        explosion.dispose();
        beepbop.dispose();
    }
}


