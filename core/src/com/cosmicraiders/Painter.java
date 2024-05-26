package com.cosmicraiders;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Painter {
    private GameScreen gameScreen;
    private Batch batch;

    public Painter(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.batch = gameScreen.getGame().batch;
    }

    /**
     * Draws the two star layers as a background for the game.
     */
    public void renderStarLayers() {
        // first star layer rendering
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                batch.draw(Assets.starBackgroundImage, i * 512, j * 512  - ((TimeUtils.nanoTime() / 10000000) % 512), 512, 512);
            }
        }
        // second star layer rendering
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                batch.draw(Assets.starBackgroundImage, i * 1024, j * 1024  - ((TimeUtils.nanoTime() / 5000000) % 1024), 1024, 1024);
            }
        }
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
            batch.draw(Assets.fighterImage, gameScreen.getFighter().x, gameScreen.getFighter().y, gameScreen.getFighter().width, gameScreen.getFighter().height);
        }
        for(Rectangle laser: gameScreen.getFighterLasers()) { // fighter lasers
            batch.draw(Assets.laserRedImage, laser.x, laser.y, laser.width, laser.height);
        }
        for(Asteroid asteroid: gameScreen.getAsteroids()) { // asteroids
            // batch.draw(Assets.asteroidImage, asteroid.x - asteroid.radius, asteroid.y - asteroid.radius, asteroid.radius * 2, asteroid.radius * 2);
            batch.draw(Assets.asteroidImage, asteroid.x - asteroid.radius, asteroid.y - asteroid.radius, asteroid.radius, asteroid.radius, asteroid.radius * 2, asteroid.radius * 2, 1, 1, asteroid.getRotation(), 0, 0, 59, 59, false, false);
        }
        for(Explosion explosion: gameScreen.getExplosions()) { // explosions
            batch.draw(Assets.asteroidExplosionImage, explosion.getX() - explosion.getWidth() / 2, explosion.getY() - explosion.getHeight() / 2, explosion.getWidth(), explosion.getHeight());
            if(explosion.getCreationTime() < TimeUtils.nanoTime() - 500000000) {
                gameScreen.getExplosions().removeValue(explosion, true);
            }
        }
        for(Rectangle alien: gameScreen.getAliens()) { // alien ships
            batch.draw(Assets.alienImage, alien.x, alien.y, alien.width, alien.height);
        }
        for(Rectangle alienLaser: gameScreen.getAlienLasers()) { // alien lasers
            batch.draw(Assets.laserGreenImage, alienLaser.x, alienLaser.y, alienLaser.width, alienLaser.height);
        }

    }



}
