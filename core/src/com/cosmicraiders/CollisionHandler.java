package com.cosmicraiders;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * This class handles all collisions of objects which are rendered.
 * It mostly deletes them upon collision and creates a new Explosion there.
 * It also ends the game if the fighter collides with an asteroid or a laser.
 */
public class CollisionHandler {
    private final GameScreen gameScreen;

    public CollisionHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Checks if an asteroid, power up or alien laser hits the fighter and ends the game.
     */
    public void handleFighterCollisions() {
        Rectangle fighter = gameScreen.getFighter();
        if (gameScreen.isGameOver()) return;
        for (Asteroid asteroid : gameScreen.getAsteroids()) {
            if (Intersector.overlaps(asteroid, fighter)) {
                destroyFighter();
            }
        }
        for (PowerUp powerUp : gameScreen.getPowerUps()) {
            if (Intersector.overlaps(powerUp, fighter)) {
                gameScreen.getPowerUpHandler().activatePowerUp(powerUp);
            }
        }
        for (Rectangle alienLaser : gameScreen.getAlienLasers()) {
            if (Intersector.overlaps(alienLaser, fighter)) {
                destroyFighter();
                gameScreen.getAlienLasers().removeValue(alienLaser, true);
            }
        }
    }

    /**
     * Checks if fighter lasers hit an alien or an asteroid and handle the collision accordingly.
     */
    public void handleFighterLaserCollisions() {
        for (Rectangle fighterLaser : gameScreen.getFighterLasers()) {
            for (Asteroid asteroid : gameScreen.getAsteroids()) {
                if (Intersector.overlaps(asteroid, fighterLaser)) {
                    laserHitsAsteroid(asteroid, fighterLaser);
                }
            }
            for (Rectangle alien : gameScreen.getAliens()) {
                if (Intersector.overlaps(alien, fighterLaser)) {
                    laserHitsAlien(alien, fighterLaser);
                }
            }
        }
    }

    /**
     * Auxiliary method.
     * This method is called when a laser hits an asteroid.
     * It plays an explosion sound and adds an Explosion object to the explosions array to be rendered.
     *
     * @param asteroid the asteroid which has just been hit
     */
    private void laserHitsAsteroid(Asteroid asteroid, Rectangle fighterLaser) {
        AssetSet.asteroidExplosion.play(gameScreen.getConfigSet().getVolume());
        gameScreen.getExplosions().add(new Explosion(asteroid.x, asteroid.y, asteroid.radius * 2, asteroid.radius * 2));

        gameScreen.getSpawner().attemptPowerUpSpawn(asteroid.x, asteroid.y);

        gameScreen.getAsteroids().removeValue(asteroid, true);
        gameScreen.getFighterLasers().removeValue(fighterLaser, true);

    }

    /**
     * Auxiliary method.
     * This method is called when a laser hits the alien ship.
     * It plays an explosion sound, increases difficulty (when score is low) and adds an Explosion object to the explosions array to be rendered.
     * It also sets the alienDead, lastAlienTime and aliensMovetoRight attributes.
     *
     * @param alien        the alien ship which has just been hit
     * @param fighterLaser
     */
    private void laserHitsAlien(Rectangle alien, Rectangle fighterLaser) {
        gameScreen.setScore(gameScreen.getScore() + 1);

        // don't change difficulty after score of 14
        if (gameScreen.getScore() <= 14) {
            gameScreen.getConfigSet().increaseDifficulty();
        }

        AssetSet.alienExplosion.play(gameScreen.getConfigSet().getVolume());
        gameScreen.getExplosions().add(new Explosion(alien.x + alien.width / 2, alien.y + alien.height / 2, alien.height, alien.width));
        gameScreen.getSpawner().setAlienDead(true);
        gameScreen.getSpawner().setLastAlienTime(TimeUtils.millis());
        gameScreen.getAliens().removeValue(alien, true);
        gameScreen.getFighterLasers().removeValue(fighterLaser, true);
    }

    /**
     * Auxiliary method.
     * This method is called when the fighter is hit by an asteroid or a laser.
     * It plays an explosion sound and adds an Explosion object to the explosions array to be rendered.
     */
    private void destroyFighter() {
        if (gameScreen.getConfigSet().isGodMode() == false) {
            gameScreen.setGameOver(true);
            gameScreen.setExitTime(TimeUtils.millis() + gameScreen.getConfigSet().getWaitAfterDeath());
            AssetSet.fighterExplosion.play(gameScreen.getConfigSet().getVolume() * 3);
            Explosion fighterExplosion = new Explosion(gameScreen.getFighter().x + gameScreen.getFighter().width / 2, gameScreen.getFighter().y + gameScreen.getFighter().height / 2, 256, 256);
            fighterExplosion.setCreationTime(TimeUtils.millis() + 5000);
            gameScreen.getExplosions().add(fighterExplosion);
            gameScreen.setFighter(null);
            gameScreen.getConfigSet().resetDifficulty();
        }

    }
}
