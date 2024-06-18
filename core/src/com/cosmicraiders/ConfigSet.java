package com.cosmicraiders;

/**
 * This class saves general game settings as static attributes.
 * It serves to centralize these settings and make them more accessible.
 * These attributes are called upon by game logic to make decisions like "how fast does this laser go".
 */
public class ConfigSet {

    // general game settings
    private float volume = 0.05f;
    private int waitAfterDeath = 4000;
    private boolean godMode = false;


    // resolution
    private int resolutionX = 1920;
    private int resolutionY = 1080;
    private int padding = resolutionX / 100;

    // fighter
    private int fighterSize = 120;
    private int fighterSpeed = 800;
    private int fighterLaserSize = 80;
    private int fighterLaserSpeed = 800;
    private int fighterFireDelay = 500; // Godmode: 4

    // aliens
    private int alienSize = 80;
    private int alienSpeed = 200; // changing difficulty (Godmode: 3)
    private int alienLaserSize = 80;
    private int alienLaserSpeed = 800;
    private int alienFireDelay = 2000; // (Godmode: 2)
    private int alienChangeDirectionTimeMinimum = 3000;
    private int alienChangeDirectionTimeMaximum = 10000;
    private int alienRespawnDelay = 2000;

    // asteroids
    private int asteroidDiameter = 100;
    private int asteroidSpeed = 100; // changing difficulty (Godmode: 1)
    private int asteroidSpawnDelay = 1000;

    /**
     * Increase game difficulty by changing values.
     */
    public void increaseDifficulty() {
        increaseAlienSpeed();
        increaseAlienRateOfFire();
        increaseAsteroidSpeed();
    }

    /**
     * Change asteroid movement speed by 50.
     * @param change the value by which the speed is increased or decreased
     */
    private void changeAsteroidSpeed(int change) {
        if (asteroidSpeed + change >= 0) asteroidSpeed += change;
    }

    /**
     * Add 50 to the speed of all asteroids.
     */
    public void increaseAsteroidSpeed() {
        changeAsteroidSpeed(50);
    }

    /**
     * Subtract 50 from the speed of all asteroids.
     */
    public void decreaseAsteroidSpeed() {
        changeAsteroidSpeed(-50);
    }

    private void changeAlienSpeed(int change) {
        if (alienSpeed + change >= 0) alienSpeed += change;
    }

    public void increaseAlienSpeed() {
        changeAlienSpeed(50);
    }

    public void decreaseAlienSpeed() {
        changeAlienSpeed(-50);
    }

    /**
     * Change fighter's rate of fire by a specified amount.
     * @param change the value by which the rate of fire is increased or decreased
     */
    private void changeAlienRateOfFire(int change) {
        if (alienFireDelay - change >= 0) {
            alienFireDelay -= change;
        }
    }

    /**
     * Increase the rate of fire of the alien.
     */
    public void increaseAlienRateOfFire() {
        changeAlienRateOfFire(50);
    }

    /**
     * Decrease the rate of fire of the alien.
     */
    public void decreaseAlienRateOfFire() {
        changeAlienRateOfFire(-50);
    }

    /**
     * Change fighter's rate of fire by a specified amount.
     * @param change the value by which the rate of fire is increased or decreased
     */
    private void changeFighterRateOfFire(int change) {
        if (fighterFireDelay - change >= 0) {
            fighterFireDelay -= change;
        }
    }

    /**
     * Increase the rate of fire of the fighter.
     */
    public void increaseFighterRateOfFire() {
        changeFighterRateOfFire(50);
    }

    /**
     * Decrease the rate of fire of the fighter.
     */
    public void decreaseFighterRateOfFire() {
        changeFighterRateOfFire(-50);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public int getWaitAfterDeath() {
        return waitAfterDeath;
    }

    public void setWaitAfterDeath(int waitAfterDeath) {
        this.waitAfterDeath = waitAfterDeath;
    }

    public boolean isGodMode() {
        return godMode;
    }

    public void setGodMode(boolean godMode) {
        this.godMode = godMode;
    }

    public int getResolutionX() {
        return resolutionX;
    }

    public void setResolutionX(int resolutionX) {
        this.resolutionX = resolutionX;
    }

    public int getResolutionY() {
        return resolutionY;
    }

    public void setResolutionY(int resolutionY) {
        this.resolutionY = resolutionY;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getFighterSize() {
        return fighterSize;
    }

    public void setFighterSize(int fighterSize) {
        this.fighterSize = fighterSize;
    }

    public int getFighterSpeed() {
        return fighterSpeed;
    }

    public void setFighterSpeed(int fighterSpeed) {
        this.fighterSpeed = fighterSpeed;
    }

    public int getFighterLaserSize() {
        return fighterLaserSize;
    }

    public void setFighterLaserSize(int fighterLaserSize) {
        this.fighterLaserSize = fighterLaserSize;
    }

    public int getFighterLaserSpeed() {
        return fighterLaserSpeed;
    }

    public void setFighterLaserSpeed(int fighterLaserSpeed) {
        this.fighterLaserSpeed = fighterLaserSpeed;
    }

    public int getFighterFireDelay() {
        return fighterFireDelay;
    }

    public void setFighterFireDelay(int fighterFireDelay) {
        this.fighterFireDelay = fighterFireDelay;
    }

    public int getAlienSize() {
        return alienSize;
    }

    public void setAlienSize(int alienSize) {
        this.alienSize = alienSize;
    }

    public int getAlienSpeed() {
        return alienSpeed;
    }

    public void setAlienSpeed(int alienSpeed) {
        this.alienSpeed = alienSpeed;
    }

    public int getAlienLaserSize() {
        return alienLaserSize;
    }

    public void setAlienLaserSize(int alienLaserSize) {
        this.alienLaserSize = alienLaserSize;
    }

    public int getAlienLaserSpeed() {
        return alienLaserSpeed;
    }

    public void setAlienLaserSpeed(int alienLaserSpeed) {
        this.alienLaserSpeed = alienLaserSpeed;
    }

    public int getAlienFireDelay() {
        return alienFireDelay;
    }

    public void setAlienFireDelay(int alienFireDelay) {
        this.alienFireDelay = alienFireDelay;
    }

    public int getAlienChangeDirectionTimeMinimum() {
        return alienChangeDirectionTimeMinimum;
    }

    public void setAlienChangeDirectionTimeMinimum(int alienChangeDirectionTimeMinimum) {
        this.alienChangeDirectionTimeMinimum = alienChangeDirectionTimeMinimum;
    }

    public int getAlienChangeDirectionTimeMaximum() {
        return alienChangeDirectionTimeMaximum;
    }

    public void setAlienChangeDirectionTimeMaximum(int alienChangeDirectionTimeMaximum) {
        this.alienChangeDirectionTimeMaximum = alienChangeDirectionTimeMaximum;
    }

    public int getAsteroidDiameter() {
        return asteroidDiameter;
    }

    public void setAsteroidDiameter(int asteroidDiameter) {
        this.asteroidDiameter = asteroidDiameter;
    }

    public int getAsteroidSpeed() {
        return asteroidSpeed;
    }

    public void setAsteroidSpeed(int asteroidSpeed) {
        this.asteroidSpeed = asteroidSpeed;
    }

    public void setAlienRespawnDelay(int alienRespawnDelay) {
        this.alienRespawnDelay = alienRespawnDelay;
    }

    public int getAlienRespawnDelay() {
        return alienRespawnDelay;
    }

    public int getAsteroidSpawnDelay() {
        return asteroidSpawnDelay;
    }

    public void setAsteroidSpawnDelay(int asteroidSpawnDelay) {
        this.asteroidSpawnDelay = asteroidSpawnDelay;
    }
}
