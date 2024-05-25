package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

/**
 * Includes the main game logic as well as most GUI elements of the game.
 * Includes game avatars, background, movement, shooting, collision, and more.
 */
public class GameScreen implements Screen {

    /**
     * The Game instance which should be "CosmicRaiders" as a reference
     */
    final CosmicRaiders game;
    final Controls controls;
    final MovementHandler movementHandler;
    final CollisionHandler collisionHandler;
    final Painter painter;
    final Debug debug;

    /**
     * This batch includes all the textures / sprites to be rendered
     */
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Rectangle fighter;

    private Array<Rectangle> fighterLasers;
    private Array<Rectangle> alienLasers;
    private Array<Asteroid> asteroids;
    private Array<Explosion> explosions;
    private Array<Rectangle> aliens;

    private long lastAlienShootTime = TimeUtils.nanoTime();
    private long lastAsteroidTime;
    private long lastAlienTime;


    private int score = 0;
    private boolean alienDead = true;
    private boolean gameOver = false;

    /**
     * using the constructor instead of the create() method
     * this builds a GameScreen instance using the Game instance
     * @param game; the Game instance, here: CosmicRaiders
     */
    public GameScreen(final CosmicRaiders game) {

        // link other program components
        this.game = game;
        this.controls = new Controls(this);
        this.movementHandler = new MovementHandler(this);
        this.collisionHandler = new CollisionHandler(this);
        this.painter = new Painter(this);
        this.debug = new Debug(this);

        // start the playback of the background music immediately
        Assets.beepbop.setLooping(true);
        Assets.beepbop.setVolume(Configs.volume);

        // construct camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Configs.resolutionX, Configs.resolutionY);

        batch = game.batch; // this ignores encapsulation as intended by LibGDX

        // construct fighter
        fighter = new Rectangle();
        fighter.x = Configs.resolutionX / 2 - Configs.fighterSize / 2;
        fighter.y = Configs.fighterSize / 4;
        fighter.width = Configs.fighterSize;
        fighter.height = Configs.fighterSize * (57f / 46f);

        // construct arrays
        fighterLasers = new Array<Rectangle>();
        alienLasers = new Array<Rectangle>();
        asteroids = new Array<Asteroid>();
        explosions = new Array<Explosion>();
        aliens = new Array<Rectangle>();
    }

    /**
     * This method ensures a steady stream of asteroids falling from the top of the screen towards the bottom.
     * The spawned asteroids are added to an "Array" List which is used in render().
     * Asteroids are Circles, not Rectangles. Most other objects are Rectangles.
     */
    private void spawnAsteroid() {
        Asteroid asteroid = new Asteroid();
        float randomSizeMultiplier = MathUtils.random(0.5f, 1.5f);
        asteroid.radius = ((Configs.asteroidDiameter / 2) * randomSizeMultiplier);
        asteroid.x = MathUtils.random(Configs.fighterSize, Configs.resolutionX - Configs.fighterSize);
        asteroid.y = Configs.resolutionY + asteroid.radius;
        asteroids.add(asteroid);
        lastAsteroidTime = TimeUtils.nanoTime();
    }

    /**
     * This method spawns one alien ship at the top of the screen which moves diagonally.
     * The alien is a Rectangle for collision purposes.
     * It shoots green lasers.
     * This method can be expanded to cause more than one alien to spawn, move and shoot.
     */
    private void spawnAlien() {
        Rectangle alien = new Rectangle();
        alien.width = Configs.alienSize;
        alien.height = Configs.alienSize * (57f / 46f);
        alien.x = MathUtils.random(Configs.padding, Configs.resolutionX - Configs.alienSize - Configs.padding);
        alien.y = Configs.resolutionY - alien.height - Configs.padding;
        aliens.add(alien);
        alienDead = false;
        movementHandler.setAlienChangeDirectionTime(TimeUtils.millis());
    }

    /**
     * Spawns an alien laser starting from the bottom middle of the alien ship sprite.
     * Sets lastAlienShootTime in order to set the interval between shots.
     * Adds the laser to an Array to be rendered.
     * @param x the horizontal coordinate of the alien ship that shoots
     * @param y the vertical coordinate of the alien ship that shoots
     */
    private void spawnAlienLaser(float x, float y) {
        Rectangle laser = new Rectangle();
        laser.width = Configs.alienLaserSize / 10;
        laser.height = Configs.alienLaserSize;
        laser.x = x + Configs.alienSize / 2 - laser.width / 2;
        laser.y = y - Configs.alienSize;

        alienLasers.add(laser);
        Assets.blasterShoot.play(Configs.volume);
        lastAlienShootTime = TimeUtils.nanoTime();
    }

    /**
     * Renders the game. Includes movement and collision detection.
     * LibGDX tutorial recommends not creating objects here - this method is called many times per second.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render (float delta) {


        // black background
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //SpriteBatch rendering code
        batch.begin();
        debug.showCoordinates();
        game.font.draw(game.batch, "Score: " + score, 0, Configs.resolutionY);
        painter.renderStarLayers();
        painter.renderObjects();
        batch.end();

        // movements
        movementHandler.moveFighterLasers();
        movementHandler.moveAsteroids();
        movementHandler.moveAliens();
        movementHandler.moveAlienLasers();

        // collisions
        collisionHandler.handleFighterLaserCollisions();
        collisionHandler.handleFighterCollisions();

        // rotate asteroids
        for (Asteroid asteroid : asteroids) {
            asteroid.updateRotation(Gdx.graphics.getDeltaTime());
        }

        // throws up a "Game Over" screen
        if (gameOver == true) {
            game.batch.begin();
            game.font.draw(game.batch, "Game Over!", Configs.resolutionX*1/5, Configs.resolutionY*3/5);
            game.font.draw(game.batch, "Score: " + score, Configs.resolutionX*1/5, Configs.resolutionY*3/5- Configs.fighterSize);
            game.font.draw(game.batch, "Press ENTER to continue", Configs.resolutionX*1/5, Configs.resolutionY*3/5- Configs.fighterSize*4);
            game.batch.end();
        }

        // movement or restart
        if (!gameOver) {
            controls.moveFighter();
            controls.shootFromFighter();
        } else {
            controls.checkForRestart();
        }

        // spawn block
        // spawn asteroids
        if(TimeUtils.nanoTime() - lastAsteroidTime > 500000000) spawnAsteroid();

        // spawn alien
        if(alienDead && (TimeUtils.nanoTime() - lastAlienTime > 2000000000)) spawnAlien();

        // spawn alien lasers (aliens shoots laser)
        for (Iterator<Rectangle> iter = aliens.iterator(); iter.hasNext(); ) {
            Rectangle alien = iter.next();
            if (TimeUtils.nanoTime() - lastAlienShootTime > 1000000000) {
                spawnAlienLaser(alien.x, alien.y);
            }
        }
    }

    /**
     * start the playback of the background music when the screen is shown
     */
    @Override
    public void show() {
        Assets.beepbop.play();
        spawnAsteroid();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {

    }

    public CosmicRaiders getGame() {
        return game;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Rectangle getFighter() {
        return fighter;
    }

    public void setFighter(Rectangle fighter) {
        this.fighter = fighter;
    }

    public Array<Rectangle> getFighterLasers() {
        return fighterLasers;
    }

    public void setFighterLasers(Array<Rectangle> fighterLasers) {
        this.fighterLasers = fighterLasers;
    }

    public Array<Rectangle> getAlienLasers() {
        return alienLasers;
    }

    public void setAlienLasers(Array<Rectangle> alienLasers) {
        this.alienLasers = alienLasers;
    }

    public Array<Asteroid> getAsteroids() {
        return asteroids;
    }

    public void setAsteroids(Array<Asteroid> asteroids) {
        this.asteroids = asteroids;
    }

    public Array<Explosion> getExplosions() {
        return explosions;
    }

    public void setExplosions(Array<Explosion> explosions) {
        this.explosions = explosions;
    }

    public Array<Rectangle> getAliens() {
        return aliens;
    }

    public void setAliens(Array<Rectangle> aliens) {
        this.aliens = aliens;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isAlienDead() {
        return alienDead;
    }

    public void setAlienDead(boolean alienDead) {
        this.alienDead = alienDead;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

}
