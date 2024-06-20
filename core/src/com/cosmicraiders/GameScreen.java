package com.cosmicraiders;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
    private CosmicRaiders game;
    private ConfigSet configSet;
    private ControlSet controlSet;
    private MovementHandler movementHandler;
    private CollisionHandler collisionHandler;
    private Painter painter;
    private Spawner spawner;
    private Debug debug;

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

    private int score;
    private boolean gameOver = false;
    private long exitTime;


    private GlyphLayout scoreText;
    private HorizontalCenteredGlyphLayout gameOverText;

    /**
     * using the constructor instead of the create() method
     * this builds a GameScreen instance using the Game instance
     * it uses an auxiliary method called initialize() to construct objects
     * @param game; the Game instance, here: CosmicRaiders
     */
    public GameScreen(final CosmicRaiders game) {

        // link other program components
        this.game = game;
        initialize();
    }

    /**
     * This auxiliary method constructs objects needed for the game.
     * It is called each time a new round starts.
     * It builds NEW arrays and new auxiliary objects like Painter and Spawner.
     * The old arrays and objects from the last round are not emptied,
     * they are de-referenced and removed by the garbage collector.
     */
    public void initialize() {

        // link other program components
        this.controlSet = new ControlSet(this);
        this.movementHandler = new MovementHandler(this);
        this.collisionHandler = new CollisionHandler(this);
        if (painter == null) {
            this.painter = new Painter(this);
        }

        this.spawner = new Spawner(this);
        this.debug = new Debug(this);

        batch = game.getBatch();
        configSet = game.getConfigSet();

        // construct camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, configSet.getResolutionX(), configSet.getResolutionY());

        // construct fighter
        fighter = new Rectangle();
        fighter.x = configSet.getResolutionX() / 2 - configSet.getFighterSize() / 2;
        fighter.y = configSet.getFighterSize() / 4;
        fighter.width = configSet.getFighterSize();
        fighter.height = configSet.getFighterSize();
        //fighter.height = Configs.fighterSize * (57f / 46f);

        // construct arrays
        fighterLasers = new Array<Rectangle>();
        alienLasers = new Array<Rectangle>();
        asteroids = new Array<Asteroid>();
        explosions = new Array<Explosion>();
        aliens = new Array<Rectangle>();

        // construct GlyphLayouts
        scoreText = new GlyphLayout(game.getFont(), "");
        gameOverText = new HorizontalCenteredGlyphLayout(game.getFont(), "Game Over!", configSet.getResolutionX());

        // initialize score
        setScore(0);
    }

    /**
     * Is called when the game is over because the figher was destroyed.
     * Is called by the destroyFighter() method in CollisionHandler.
     */
    public void checkGameOver() {
       if  (gameOver && exitTime <= TimeUtils.millis() ) {
           Scores.setLastScore(score);
           Scores.increaseRoundsPlayed();
           score = 0;
           game.setScreen(game.getMainMenuScreen());
           this.pause();
       }
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
        checkGameOver();

        // black background
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //SpriteBatch rendering code
        batch.begin();
        debug.showCoordinates();
        painter.renderStarLayers(1);
        painter.renderObjects();
        painter.renderText();


        batch.end();

        // spawning
        spawner.handleSpawning();

        // movements
        movementHandler.moveFighterLasers();
        movementHandler.moveAliens();
        movementHandler.moveAlienLasers();
        movementHandler.moveAsteroids();
        movementHandler.rotateAsteroids();

        // fighter movement
        controlSet.handleControls();


        // collisions
        collisionHandler.handleFighterLaserCollisions();
        collisionHandler.handleFighterCollisions();

        // throws up a "Game Over" screen
        if (gameOver == true) {
            game.getBatch().begin();
            game.getFont().draw(game.getBatch(), gameOverText, gameOverText.getX(), configSet.getResolutionY() / 2 - gameOverText.height / 2);
            game.getBatch().end();
        }


    }

    /**
     * start the playback of the background music when the screen is shown
     */
    @Override
    public void show() {
//        Assets.openUp.play();
        initialize();
        AssetSet.neonNoir.play();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
//        Assets.neonNoir.stop();
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
        painter.setScoreText("Score: " + score);
//        this.scoreText.setText(game.font, "Score: " + score);
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

    public long getExitTime() {
        return exitTime;
    }

    public void setExitTime(long exitTime) {
        this.exitTime = exitTime;
    }

    public Painter getPainter() {
        return this.painter;
    }

    public void setGame(CosmicRaiders game) {
        this.game = game;
    }

    public ConfigSet getConfigSet() {
        return configSet;
    }

    public void setConfigSet(ConfigSet configSet) {
        this.configSet = configSet;
    }
}
