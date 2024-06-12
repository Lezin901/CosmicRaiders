package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import java.io.ObjectInputFilter;


/**
 * This class handles all movement for aliens, asteroids and lasers.
 */
public class MovementHandler {
    private final GameScreen gameScreen;
    public boolean aliensMoveToRight = false;
    private long alienChangeDirectionTime;

    /**
     * This constructor gets a reference to the GameScreen instance.
     */
    public MovementHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Moves Asteroids downwards and deletes them when they leave the screen.
     */
    public void moveAsteroids() {
        for(Asteroid asteroid: gameScreen.getAsteroids()) {
            asteroid.y -= Configs.asteroidSpeed * Gdx.graphics.getDeltaTime(); // asteroid speed
            if(asteroid.y < -asteroid.radius * 2) gameScreen.getAsteroids().removeValue(asteroid, true);
        }
    }

    /**
     * Rotates asteroids.
     */
    public void rotateAsteroids() {
        for (Asteroid asteroid : gameScreen.getAsteroids()) {
            asteroid.updateRotation(Gdx.graphics.getDeltaTime());
        }

    }

    /**
     * Moves alien lasers downwards and deletes them when they leave the screen.
     */
    public void moveAlienLasers() {
        for(Rectangle alienLaser: gameScreen.getAlienLasers()) {
            alienLaser.y -= Configs.alienLaserSpeed * Gdx.graphics.getDeltaTime();
            if(alienLaser.y <= 0 - Configs.alienLaserSize) gameScreen.getAlienLasers().removeValue(alienLaser, true);
        }
    }

    /**
     * Moves fighter lasers upwards and deletes them when they leave the screen.
     */
    public void moveFighterLasers() {
        for(Rectangle laser: gameScreen.getFighterLasers()) {
            laser.y += Configs.fighterLaserSpeed * Gdx.graphics.getDeltaTime();
            if(laser.y >= Configs.resolutionY) gameScreen.getFighterLasers().removeValue(laser, true);
        }
    }

    /**
     * Moves aliens from side to side, with a random factor for changing direction.
     */
    public void moveAliens() {
        for (Rectangle alien: gameScreen.getAliens()) {
            if ((alien.x <= Configs.padding) || (alien.x >= Configs.resolutionX - Configs.padding - Configs.fighterSize)) {
                aliensMoveToRight = !aliensMoveToRight;
            }
            if (aliensMoveToRight) {
                alien.x -= Configs.alienSpeed * Gdx.graphics.getDeltaTime(); // alien speed
            } else {
                alien.x += Configs.alienSpeed * Gdx.graphics.getDeltaTime(); // alien speed
            }
        }
        if (alienChangeDirectionTime <= TimeUtils.millis()) {
            alienChangeDirectionTime = TimeUtils.millis() + MathUtils.random(Configs.alienChangeDirectionTimeMinimum, Configs.alienChangeDirectionTimeMaximum);
            aliensMoveToRight = !aliensMoveToRight;
        }
    }

    /**
     * Auxiliary method for moveAliens()
     * @return the time after which the alien will change direction
     */
    public long getAlienChangeDirectionTime() {
        return alienChangeDirectionTime;
    }

    /**
     * Auxiliary method for moveAliens()
     */
    public void setAlienChangeDirectionTime(long alienChangeDirectionTime) {
        this.alienChangeDirectionTime = alienChangeDirectionTime;
    }
}
