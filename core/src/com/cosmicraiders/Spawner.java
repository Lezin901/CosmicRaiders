package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * A Spawner creates objects in the game: aliens, alien lasers and fighter lasers.
 * Its attributes also track the timing of past and future spawns.
 */
public class Spawner {
    private final GameScreen gameScreen;
    /**
     * tracks how long ago the alien shot a laser
     */
    private long lastAlienShootTime = TimeUtils.nanoTime();
    /**
     * tracks how long ago the fighter shot a laser
     */
    private long lastFighterShootTime;
    /**
     * tracks how long ago the last asteroid spawned
     */
    private long lastAsteroidTime;
    /**
     * tracks how long ago the last alien was destroyed
     */
    private long lastAlienTime;
    /**
     * is the alien currently dead?
     */
    private boolean alienDead = true;

    /**
     * The constructor gets a reference to the GameScreen in order to refer back to it.
     *
     * @param gameScreen the main game environment
     */
    public Spawner(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Checks the timing and spawns asteroids, aliens, and alien lasers if conditions are met.
     */
    public void handleSpawning() {
        if (TimeUtils.nanoTime() - lastAsteroidTime > 500000000) {
            spawnAsteroid();
        }

        if (alienDead && (TimeUtils.nanoTime() - lastAlienTime > 2000000000)) {
            spawnAlien();
        }

        for (Rectangle alien : gameScreen.getAliens()) {
            if (TimeUtils.nanoTime() - lastAlienShootTime > 1000000000) {
                spawnAlienLaser(alien.x, alien.y);
            }
        }
    }

    /**
     * Spawns an asteroid and adds it to the asteroids array.
     * This method ensures a steady stream of asteroids falling from the top of the screen towards the bottom.
     * Asteroids are Circles, not Rectangles. Most other objects are Rectangles.
     */
    public void spawnAsteroid() {
        Asteroid asteroid = new Asteroid();
        float randomSizeMultiplier = MathUtils.random(0.5f, 1.5f);
        asteroid.radius = ((gameScreen.getConfigSet().getAsteroidDiameter() / 2) * randomSizeMultiplier);
        asteroid.x = MathUtils.random(gameScreen.getConfigSet().getFighterSize(), gameScreen.getConfigSet().getResolutionX() - gameScreen.getConfigSet().getFighterSize());
        asteroid.y = gameScreen.getConfigSet().getResolutionY() + asteroid.radius;
        gameScreen.getAsteroids().add(asteroid);
        lastAsteroidTime = TimeUtils.nanoTime();
    }

    /**
     * Spawns an alien and adds it to the aliens array which moves diagonally.
     * The alien is a Rectangle for collision purposes.
     * It shoots green lasers.
     * This method can be expanded to cause more than one alien to spawn, move and shoot.
     */
    public void spawnAlien() {
        AssetSet.alienSpawn.play(gameScreen.getConfigSet().getVolume());
        Rectangle alien = new Rectangle();
        alien.width = gameScreen.getConfigSet().getAlienSize();
        alien.height = gameScreen.getConfigSet().getAlienSize() * (57f / 46f);
        alien.x = MathUtils.random(gameScreen.getConfigSet().getPadding(), gameScreen.getConfigSet().getResolutionX() - gameScreen.getConfigSet().getAlienSize() - gameScreen.getConfigSet().getPadding());
        alien.y = gameScreen.getConfigSet().getResolutionY() - alien.height - gameScreen.getConfigSet().getPadding();
        gameScreen.getAliens().add(alien);
        alienDead = false;
        gameScreen.getMovementHandler().setAlienChangeDirectionTime(TimeUtils.millis());
    }

    /**
     * Spawns an alien laser starting from the bottom middle of the alien ship sprite.
     * Sets lastAlienShootTime in order to set the interval between shots.
     * Adds the laser to an Array to be rendered.
     *
     * @param x the horizontal coordinate of the alien ship that shoots
     * @param y the vertical coordinate of the alien ship that shoots
     */
    public void spawnAlienLaser(float x, float y) {
        AssetSet.alienLaser.play(gameScreen.getConfigSet().getVolume());
        Rectangle laser = new Rectangle();
        laser.width = gameScreen.getConfigSet().getAlienLaserSize() / 10;
        laser.height = gameScreen.getConfigSet().getAlienLaserSize();
        laser.x = x + gameScreen.getConfigSet().getAlienSize() / 2 - laser.width / 2;
        laser.y = y - gameScreen.getConfigSet().getAlienSize();

        gameScreen.getAlienLasers().add(laser);
        lastAlienShootTime = TimeUtils.nanoTime();
    }

    /**
     * Spawns a fighter laser starting from the bottom middle of the fighter ship sprite.
     * Sets lastFighterShootTime in order to set the interval between shots.
     * Adds the laser to an Array to be rendered.
     */
    public void spawnFighterLaser() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (TimeUtils.millis() - lastFighterShootTime > gameScreen.getConfigSet().getFighterRateOfFire()) {
                AssetSet.fighterLaser.play(gameScreen.getConfigSet().getVolume());
                Rectangle laser = new Rectangle();
                laser.width = gameScreen.getConfigSet().getFighterLaserSize() / 10;
                laser.height = gameScreen.getConfigSet().getFighterLaserSize();
                laser.x = gameScreen.getFighter().x + gameScreen.getConfigSet().getFighterSize() / 2 - laser.width / 2;
                laser.y = gameScreen.getFighter().y + gameScreen.getConfigSet().getFighterSize();

                gameScreen.getFighterLasers().add(laser);
                lastFighterShootTime = TimeUtils.millis();
            }
        }
        ;
    }


    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public boolean isAlienDead() {
        return alienDead;
    }

    public void setAlienDead(boolean alienDead) {
        this.alienDead = alienDead;
    }

    public long getLastAlienShootTime() {
        return lastAlienShootTime;
    }

    public void setLastAlienShootTime(long lastAlienShootTime) {
        this.lastAlienShootTime = lastAlienShootTime;
    }

    public long getLastAsteroidTime() {
        return lastAsteroidTime;
    }

    public void setLastAsteroidTime(long lastAsteroidTime) {
        this.lastAsteroidTime = lastAsteroidTime;
    }

    public long getLastAlienTime() {
        return lastAlienTime;
    }

    public void setLastAlienTime(long lastAlienTime) {
        this.lastAlienTime = lastAlienTime;
    }
}