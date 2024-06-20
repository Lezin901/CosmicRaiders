package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Date;

/**
 * A Painter draws all the assets in the batch.
 */
public class Painter {
    private GameScreen gameScreen;
    private Batch batch;
    private String scoreText;
    private Rectangle[][] backgroundStarLayerImages = new Rectangle[4][3];
    private Rectangle[][] middlegroundStarLayerImages = new Rectangle[4][3];

    private int backgroundStarLayerImageSize = 512;
    private int middlegroundStarLayerImageSize = 700;

    public Painter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.batch = gameScreen.getGame().getBatch();
        System.out.println("Konstruktor von Painter aufgerufen ");
        // Initialize positions of the backgroundStarLayerImages
        for (int i = 0; i < backgroundStarLayerImages.length; i++) {
            for (int j = 0; j < backgroundStarLayerImages[i].length; j++) {
                backgroundStarLayerImages[i][j] = new Rectangle();
                backgroundStarLayerImages[i][j].x = i * backgroundStarLayerImageSize;
                backgroundStarLayerImages[i][j].y = j * backgroundStarLayerImageSize;
            }
        }
        // Initialize positions of the middlegroundStarLayerImages
        for (int i = 0; i < middlegroundStarLayerImages.length; i++) {
            for (int j = 0; j < middlegroundStarLayerImages[i].length; j++) {
                middlegroundStarLayerImages[i][j] = new Rectangle();
                middlegroundStarLayerImages[i][j].x = i * middlegroundStarLayerImageSize;
                middlegroundStarLayerImages[i][j].y = j * middlegroundStarLayerImageSize;
            }
        }
    }

    /**
     * Draws the two star layers as a background for the game.
     */
    public void renderStarLayers(double speedMultiplier) {

        for (int i = 0; i < backgroundStarLayerImages.length; i++) {
            for (int j = 0; j < backgroundStarLayerImages[i].length; j++) {
                batch.draw(AssetSet.starBackgroundImage, backgroundStarLayerImages[i][j].x, backgroundStarLayerImages[i][j].y, backgroundStarLayerImageSize, backgroundStarLayerImageSize);

                // set movement speed of the backgroundStarLayerImages
                // using Math.min() to prevent stuttering due to delta time
                backgroundStarLayerImages[i][j].y -= gameScreen.getConfigSet().getAsteroidSpeed() * Math.min(Gdx.graphics.getDeltaTime(), 1.0 / 30.0 ) * 0.4d * speedMultiplier; // asteroid speed


                if (backgroundStarLayerImages[i][j].y <= (j-1) * backgroundStarLayerImageSize) {
                    backgroundStarLayerImages[i][j].y = j * backgroundStarLayerImageSize;
                }
            }
        }
        for (int i = 0; i < middlegroundStarLayerImages.length; i++) {
            for (int j = 0; j < middlegroundStarLayerImages[i].length; j++) {
                batch.draw(AssetSet.starBackgroundImage, middlegroundStarLayerImages[i][j].x, middlegroundStarLayerImages[i][j].y, middlegroundStarLayerImageSize, middlegroundStarLayerImageSize);

                // set movement speed of the middlegroundStarLayer
                middlegroundStarLayerImages[i][j].y -= gameScreen.getConfigSet().getAsteroidSpeed() * Math.min(Gdx.graphics.getDeltaTime(), 1.0 / 30.0 ) * 0.5f * speedMultiplier;


                if (middlegroundStarLayerImages[i][j].y <= (j-1) * middlegroundStarLayerImageSize) {
                    middlegroundStarLayerImages[i][j].y = j * middlegroundStarLayerImageSize;
                }
            }
        }






//        // first star layer rendering
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++) {
//                batch.draw(AssetSet.starBackgroundImage, i * 512,
//                        j * 512 - (((gameScreen.getConfigSet().getAsteroidSpeed() * TimeUtils.millis() / 2000)) % 512 ),
//                        512, 512);
//            }
//        }
//        // second star layer rendering
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 3; j++) {
//                batch.draw(AssetSet.starBackgroundImage, i * 1024,
//                        j * 1024  - (((gameScreen.getConfigSet().getAsteroidSpeed() * TimeUtils.millis() / 1600)) % 1024),
//                        1024, 1024);
//            }
//        }
    }

    /**
     * Draws all objects which are used in the game:
     * fighter
     * fighter lasers
     * asteroids
     * explosions
     * aliens
     * alien lasers
     */
    public void renderObjects() {
        // render all objects from Arrays into batch
        if (gameScreen.getFighter() != null) { // fighter
            batch.draw(AssetSet.fighterImage, gameScreen.getFighter().x, gameScreen.getFighter().y, gameScreen.getFighter().width, gameScreen.getFighter().height);
        }
        for(Rectangle laser: gameScreen.getFighterLasers()) { // fighter lasers
            batch.draw(AssetSet.laserRedImage, laser.x, laser.y, laser.width, laser.height);
        }
        for(Asteroid asteroid: gameScreen.getAsteroids()) { // asteroids
            // batch.draw(Assets.asteroidImage, asteroid.x - asteroid.radius, asteroid.y - asteroid.radius, asteroid.radius * 2, asteroid.radius * 2);
            batch.draw(AssetSet.asteroidImage, asteroid.x - asteroid.radius, asteroid.y - asteroid.radius, asteroid.radius, asteroid.radius, asteroid.radius * 2, asteroid.radius * 2, 1, 1, asteroid.getRotation(), 0, 0, 59, 59, false, false);
        }
        for(Explosion explosion: gameScreen.getExplosions()) { // explosions
            batch.draw(AssetSet.asteroidExplosionImage, explosion.getX() - explosion.getWidth() / 2, explosion.getY() - explosion.getHeight() / 2, explosion.getWidth(), explosion.getHeight());
            if(explosion.getCreationTime() < TimeUtils.millis() - 500) {
                gameScreen.getExplosions().removeValue(explosion, true);
            }
        }
        for(Rectangle alien: gameScreen.getAliens()) { // alien ships
            batch.draw(AssetSet.alienImage, alien.x, alien.y, alien.width, alien.height);
        }
        for(Rectangle alienLaser: gameScreen.getAlienLasers()) { // alien lasers
            batch.draw(AssetSet.laserGreenImage, alienLaser.x, alienLaser.y, alienLaser.width, alienLaser.height);
        }

    }

    public void renderText() {
        gameScreen.getGame().getFont().draw(gameScreen.getGame().getBatch(), scoreText, 60, 80);
        if (gameScreen.getConfigSet().isGodMode() == true) {
            gameScreen.getGame().getFont().draw(gameScreen.getGame().getBatch(), "Godmode activated!!!", 60, gameScreen.getConfigSet().getResolutionY() - 60);
        }
    }

    public void setScoreText(String scoreText) {
        this.scoreText = scoreText;
    }
}
