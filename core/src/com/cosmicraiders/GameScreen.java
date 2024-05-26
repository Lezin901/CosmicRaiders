package com.cosmicraiders;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

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
    final Spawner spawner;
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

    private int score = 0;
    private boolean gameOver = false;
    private long exitTime;

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
        this.spawner = new Spawner(this);
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
     * Is called when the game is over because the figher was destroyed.
     * Is called by the destroyFighter() method in CollisionHandler.
     */
    public void finalizeGame() {
        gameOver = true;

        this.exitTime = TimeUtils.millis() + Configs.waitAfterDeath;

    }

    /**
     * Includes the main game logic as well as most GUI elements of the game.
     * Includes game avatars, background, movement, shooting, collision, and more.
     * LibGDX tutorial recommends not creating objects here - this method is called many times per second.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render (float delta) {

        // go to MainMenu screen if game is over
        if (exitTime <= TimeUtils.millis() && gameOver) {
            Scores.setLastScore(score);
            game.setScreen(game.getMainMenuScreen());
            this.pause();

        }

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

        // spawning
        spawner.handleSpawning();

        // movements
        movementHandler.moveFighterLasers();
        movementHandler.moveAliens();
        movementHandler.moveAlienLasers();
        movementHandler.moveAsteroids();
        movementHandler.rotateAsteroids();

        // collisions
        collisionHandler.handleFighterLaserCollisions();
        collisionHandler.handleFighterCollisions();

        // throws up a "Game Over" screen
        if (gameOver == true) {
            game.batch.begin();
            game.font.draw(game.batch, "Game Over!", Configs.resolutionX*1/5, Configs.resolutionY*3/5);
            game.batch.end();
        }

        // movement or restart
        if (!gameOver) {
            controls.moveFighter();
            spawner.spawnFighterLaser();
        }
//        else {
//            controls.checkForRestart();
//        }
    }

    /**
     * start the playback of the background music when the screen is shown
     */
    @Override
    public void show() {
        Assets.beepbop.play();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        Assets.beepbop.stop();
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public MovementHandler getMovementHandler() {
        return movementHandler;
    }

    public Spawner getSpawner() {
        return spawner;
    }
}
