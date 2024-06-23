package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;


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
            // using Math.min() to prevent stuttering due to delta time
            asteroid.y -= gameScreen.getConfigSet().getAsteroidSpeed() * Math.min(Gdx.graphics.getDeltaTime(), 1.0 / 30.0 ); // asteroid speed
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
            alienLaser.y -= gameScreen.getConfigSet().getAlienLaserSpeed() * Gdx.graphics.getDeltaTime();
            if(alienLaser.y <= 0 - gameScreen.getConfigSet().getAlienLaserSize()) gameScreen.getAlienLasers().removeValue(alienLaser, true);
        }
    }

    /**
     * Moves fighter lasers upwards and deletes them when they leave the screen.
     */
    public void moveFighterLasers() {
        for(Rectangle laser: gameScreen.getFighterLasers()) {
            laser.y += gameScreen.getConfigSet().getFighterLaserSpeed() * Gdx.graphics.getDeltaTime();
            if(laser.y >= gameScreen.getConfigSet().getResolutionY()) gameScreen.getFighterLasers().removeValue(laser, true);
        }
    }

    /**
     * Moves aliens from side to side, with a random factor for changing direction.
     */
    public void moveAliens() {
        for (Rectangle alien: gameScreen.getAliens()) {
            //check if alien has arrived at the LEFT border of the screen
            if (alien.x <= gameScreen.getConfigSet().getPadding()) {
                aliensMoveToRight = true;
            }
            //check if the alien has arrived at the RIGHT border of screen
            if (alien.x >= gameScreen.getConfigSet().getResolutionX() - gameScreen.getConfigSet().getPadding() - gameScreen.getConfigSet().getFighterSize()) {
                aliensMoveToRight = false;
            }
            //move right or left
            if (aliensMoveToRight) {
                alien.x += gameScreen.getConfigSet().getAlienSpeed() * Gdx.graphics.getDeltaTime(); // TODO alien speed - is deltaTime a Problem???
            } else {
                alien.x -= gameScreen.getConfigSet().getAlienSpeed() * Gdx.graphics.getDeltaTime(); // TODO alien speed - is deltaTime a Problem???
            }
        }
        //randomly change direction
        if (alienChangeDirectionTime <= TimeUtils.millis()) {
            alienChangeDirectionTime = TimeUtils.millis() + MathUtils.random(gameScreen.getConfigSet().getAlienChangeDirectionTimeMinimum(), gameScreen.getConfigSet().getAlienChangeDirectionTimeMaximum());
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
