package com.cosmicraiders;

/**
 * This class saves general game settings as static attributes.
 * It serves to centralize these settings and make them more accessible.
 * These attributes are called upon by game logic to make decisions like "how fast does this laser go".
 */
public class Configs {

    // general game settings
    public static float volume = 0.05f;
    public static int waitAfterDeath = 4000;
    public static boolean godMode = false;

    // resolution
    public static final int resolutionX = 1920;
    public static final int resolutionY = 1080;
    public static final int padding = resolutionX / 100;

    // fighter
    public static final int fighterSize = 120;
    public static final int fighterSpeed = 800;
    public static final int fighterLaserSize = 80;
    public static final int fighterLaserSpeed = 800;
    public static final int fighterRateOfFire = 500;

    // aliens
    public static final int alienSize = 80;
    public static int alienSpeed = 200; // changing difficulty
    public static final int alienLaserSize = 80;
    public static final int alienLaserSpeed = 800;
    public static final int alienRateOfFire = 500;
    public static int alienChangeDirectionTimeMinimum = 3000;
    public static int alienChangeDirectionTimeMaximum = 10000;

    // asteroids
    public static final int asteroidDiameter = 100;
    public static int asteroidSpeed = 200; // changing difficulty

}
