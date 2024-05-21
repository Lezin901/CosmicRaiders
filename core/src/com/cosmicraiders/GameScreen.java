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

    /**
     * This batch includes all the textures / sprites to be rendered
     */
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Rectangle fighter;
    private Array<Rectangle> fighterLasers;
    private Array<Rectangle> alienLasers;
    private Array<Circle> asteroids;
    private Array<Explosion> explosions;
    private Array<Rectangle> aliens;

    private long lastFighterShootTime;
    private long lastAlienShootTime = TimeUtils.nanoTime();
    private long lastAsteroidTime;
    private long lastAlienTime;


    public int score = 0;
    public boolean aliensMoveToRight = false;
    public boolean alienDead = true;
    public boolean gameOver = false;

    /**
     * using the constructor instead of the create() method
     * this builds a GameScreen instance using the Game instance
     * @param game; the Game instance, here: CosmicRaiders
     */
    public GameScreen(final CosmicRaiders game) {
        this.game = game;

        // start the playback of the background music immediately
        Assets.beepbop.setLooping(true);
        Assets.beepbop.setVolume(Config.volume);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.resolutionX, Config.resolutionY);

        batch = game.batch; // this ignores encapsulation as intended by LibGDX

        fighter = new Rectangle();
        fighter.x = Config.resolutionX / 2 - Config.fighterSize / 2;
        fighter.y = Config.fighterSize / 4;
        fighter.width = Config.fighterSize;
        fighter.height = Config.fighterSize * (57f / 46f);

        fighterLasers = new Array<Rectangle>();
        alienLasers = new Array<Rectangle>();
        asteroids = new Array<Circle>();
        explosions = new Array<Explosion>();
        aliens = new Array<Rectangle>();
    }

    /**
     * This method ensures a steady stream of asteroids falling from the top of the screen towards the bottom.
     * The spawned asteroids are added to an "Array" List which is used in render().
     * Asteroids are Circles, not Rectangles. Most other objects are Rectangles.
     */
    private void spawnAsteroid() {
        Circle asteroid = new Circle();
        float randomSizeMultiplier = MathUtils.random(0.5f, 1.5f);
        asteroid.radius = ((Config.asteroidDiameter / 2) * randomSizeMultiplier);
        asteroid.x = MathUtils.random(Config.fighterSize, Config.resolutionX - Config.fighterSize);
        asteroid.y = Config.resolutionY + asteroid.radius;
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
        alien.width = Config.alienSize;
        alien.height = Config.alienSize * (57f / 46f);
        alien.x = MathUtils.random(Config.padding, Config.resolutionX - Config.alienSize - Config.padding);
        alien.y = Config.resolutionY - alien.height - Config.padding;
        aliens.add(alien);
        alienDead = false;
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
        laser.width = Config.alienLaserSize / 10;
        laser.height = Config.alienLaserSize;
        laser.x = x + Config.alienSize / 2 - laser.width / 2;
        laser.y = y - Config.alienSize;

        alienLasers.add(laser);
        Assets.blasterShoot.play(Config.volume);
        lastAlienShootTime = TimeUtils.nanoTime();
    }

    /**
     * Spawns a fighter laser starting from the bottom middle of the fighter ship sprite.
     * Sets lastFighterShootTime in order to set the interval between shots.
     * Adds the laser to an Array to be rendered.
     */
    private void spawnFighterLaser() {
        if (TimeUtils.nanoTime() - lastFighterShootTime > 150000000) {
            Rectangle laser = new Rectangle();
            laser.width = Config.fighterLaserSize / 10;
            laser.height = Config.fighterLaserSize;
            laser.x = fighter.x + Config.fighterSize / 2 - laser.width / 2;
            laser.y = fighter.y + Config.fighterSize;

            fighterLasers.add(laser);
            Assets.blasterShoot.play(Config.volume);
            lastFighterShootTime = TimeUtils.nanoTime();
        }
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

        // show score in top left corner
        game.font.draw(game.batch, "Score: " + score, 0, Config.resolutionY);

        // debug: show coordinates when pressing T
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            batch.draw(Assets.alienImage, touchPos.x, touchPos.y, Config.fighterSize, Config.fighterSize);
            System.out.println(touchPos.x + " " + touchPos.y);
        }

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

        // render all objects from Arrays into batch
        if (gameOver == false) { // fighter
            batch.draw(Assets.fighterImage, fighter.x, fighter.y, fighter.width, fighter.height);
        }
        for(Rectangle laser: fighterLasers) { // fighter lasers
            batch.draw(Assets.laserRedImage, laser.x, laser.y, laser.width, laser.height);
        }
        for(Rectangle alienLaser: alienLasers) { // alien lasers
            batch.draw(Assets.laserGreenImage, alienLaser.x, alienLaser.y, alienLaser.width, alienLaser.height);
        }
        for(Circle asteroid: asteroids) { // asteroids
            batch.draw(Assets.asteroidImage, asteroid.x - asteroid.radius, asteroid.y - asteroid.radius, asteroid.radius * 2, asteroid.radius * 2);
        }
        for(Explosion explosion: explosions) { // explosions
            batch.draw(Assets.asteroidExplosionImage, explosion.getX() - explosion.getWidth() / 2, explosion.getY() - explosion.getHeight() / 2, explosion.getWidth(), explosion.getHeight());
            if(explosion.getCreationTime() < TimeUtils.nanoTime() - 500000000) {
                explosions.removeValue(explosion, true);
            }
        }
        for(Rectangle alien: aliens) { // alien ships
            batch.draw(Assets.alienImage, alien.x, alien.y, alien.width, alien.height);
        }
        batch.end();

        // movement controls by player inputs
        if (gameOver == false) {
            // WASD left right movement: fighter speed
            if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                fighter.x -= 1000 * Gdx.graphics.getDeltaTime();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                fighter.x += 1000 * Gdx.graphics.getDeltaTime();
            }

            // WASD up down movement: fighter speed
            if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                fighter.y -= 1000 * Gdx.graphics.getDeltaTime();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                fighter.y += 1000 * Gdx.graphics.getDeltaTime();
            }

            // setting movement boundaries
            if(fighter.x < 0) fighter.x = 0;
            if(fighter.x > Config.resolutionX - Config.fighterSize) fighter.x = Config.resolutionX - Config.fighterSize;

            // setting movement boundaries
            if(fighter.y < 0) fighter.y = 0;
            if(fighter.y > Config.resolutionY *2/4 - Config.fighterSize) fighter.y = Config.resolutionY *2/4 - Config.fighterSize;

            // shoot
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) spawnFighterLaser();
        }

        // restart the game by pressing ENTER
        if(gameOver && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            gameOver = false;

            fighter.x = Config.resolutionX / 2 - Config.fighterSize / 2;
            fighter.y = Config.fighterSize / 4;

            score = 0;

            System.out.println("Game restarted. Score has been reset. ");
        }

        // fighterLaser movement
        for (Iterator<Rectangle> laserIterator = fighterLasers.iterator(); laserIterator.hasNext();) {
            Rectangle laser = laserIterator.next();
            // move laser
            laser.y += 800 * Gdx.graphics.getDeltaTime(); // laser speed

            if(laser.y >= Config.resolutionY) laserIterator.remove();
        }

        // check for fighterLaser collision
        for (Iterator<Rectangle> LaserIter = fighterLasers.iterator(); LaserIter.hasNext(); ) {
            Rectangle laser = LaserIter.next();
            // check for fighterLaser-asteroid collision
            for (Iterator<Circle> AsteroidIter = asteroids.iterator(); AsteroidIter.hasNext(); ) {
                Circle asteroid = AsteroidIter.next();
                if(Intersector.overlaps(asteroid,laser)) {
                    laserHitsAsteroid(asteroid);
                    AsteroidIter.remove();
                    LaserIter.remove();
                }
            }
            // check for fighterLaser-alien collision
            for (Iterator<Rectangle> AlienIter = aliens.iterator(); AlienIter.hasNext(); ) {
                Rectangle alien = AlienIter.next();
                if(Intersector.overlaps(alien,laser)) {
                    laserHitsAlien(alien);
                    AlienIter.remove();
                    LaserIter.remove();
                }
            }
        }

        // check for asteroid-fighter collision
        for (Iterator<Circle> asteroidIter = asteroids.iterator(); asteroidIter.hasNext(); ) {
            Circle asteroid = asteroidIter.next(); // asteroids are circles!
            if (Intersector.overlaps(asteroid, fighter) && gameOver == false) {
                gameOver = true;
                Explosion fighterExplosion = new Explosion(fighter.x + fighter.width / 2, fighter.y + fighter.height / 2, 256, 256);
                fighterExplosion.setCreationTime(TimeUtils.nanoTime() + 1000000000);
                explosions.add(fighterExplosion);
            }
        }

        // alienLaser movement
        for (Iterator<Rectangle> alienLaserIterator = alienLasers.iterator(); alienLaserIterator.hasNext();) {
            Rectangle alienLaser = alienLaserIterator.next();
            // move alienLaser
            alienLaser.y -= 800 * Gdx.graphics.getDeltaTime(); // alienLaser speed
            // check for alienLaser-fighter collision
            if(Intersector.overlaps(alienLaser,fighter) && gameOver == false) {
                destroyFighter();
                alienLaserIterator.remove();
            }
            // check if out of bounds
            if(alienLaser.y <= 0 - Config.alienLaserSize) alienLaserIterator.remove();
        }

        // throws up a "Game Over" screen
        if (gameOver == true) {
            game.batch.begin();
            game.font.draw(game.batch, "Game Over!", Config.resolutionX/2-Config.fighterSize, Config.resolutionY/2-Config.fighterSize);
            game.font.draw(game.batch, "Score: " + score, Config.resolutionX/2-Config.fighterSize, Config.resolutionY/2-Config.fighterSize*2);
            game.batch.end();
        }

        // spawn asteroids
        if(TimeUtils.nanoTime() - lastAsteroidTime > 500000000) spawnAsteroid();

        // move asteroids
        for (Iterator<Circle> iter = asteroids.iterator(); iter.hasNext(); ) {
            Circle asteroid = iter.next();
            asteroid.y -= 400 * Gdx.graphics.getDeltaTime(); // asteroid speed
            if(asteroid.y < -asteroid.radius * 2) iter.remove();
        }

        // spawn alien
        if(alienDead && (TimeUtils.nanoTime() - lastAlienTime > 2000000000)) spawnAlien();
//        System.out.println("Debug: number of aliens: " + aliens.size);

        // move alien
        for (Iterator<Rectangle> iter = aliens.iterator(); iter.hasNext(); ) {
            Rectangle alien = iter.next();
            if ((alien.x <= Config.padding) || (alien.x >= Config.resolutionX - Config.padding - Config.fighterSize)) {
                aliensMoveToRight = !aliensMoveToRight;
            }
            if(aliensMoveToRight) {
                alien.x -= 200 * Gdx.graphics.getDeltaTime(); // alien speed
            } else {
                alien.x += 200 * Gdx.graphics.getDeltaTime(); // alien speed
            }
        }

        // aliens shoots laser
        for (Iterator<Rectangle> iter = aliens.iterator(); iter.hasNext(); ) {
            Rectangle alien = iter.next();
            if (TimeUtils.nanoTime() - lastAlienShootTime > 1000000000) {
                spawnAlienLaser(alien.x, alien.y);
            }

        }

    }

    /**
     * This method is called when a laser hits an asteroid.
     * It plays an explosion sound and adds an Explosion object to the explosions array to be rendered.
     * @param asteroid the asteroid which has just been hit
     */
    private void laserHitsAsteroid(Circle asteroid) {
        Assets.explosion.play(Config.volume/2);
        explosions.add(new Explosion(asteroid.x, asteroid.y, asteroid.radius * 2, asteroid.radius * 2));
    }

    /**
     * This method is called when a laser hits the alien ship.
     * It plays an explosion sound and adds an Explosion object to the explosions array to be rendered.
     * It also sets the alienDead, lastAlienTime and aliensMovetoRight attributes.
     * @param alien the alien ship which has just been hit
     */
    private void laserHitsAlien(Rectangle alien) {
        score++;
        Assets.explosion.play(Config.volume/2);
        explosions.add(new Explosion(alien.x + alien.width/2 , alien.y + alien.height/2, alien.height, alien.width));
        alienDead = true;
        lastAlienTime = TimeUtils.nanoTime();
        aliensMoveToRight = MathUtils.randomBoolean();
    }

    /**
     * This method is called when the fighter is hit by an asteroid or a laser.
     * It plays an explosion sound and adds an Explosion object to the explosions array to be rendered.
     */
    private void destroyFighter() {
        gameOver = true;
        Assets.explosion.play(Config.volume/2);
        Explosion fighterExplosion = new Explosion(fighter.x + fighter.width / 2, fighter.y + fighter.height / 2, 256, 256);
        fighterExplosion.setCreationTime(TimeUtils.nanoTime() + 1000000000);
        explosions.add(fighterExplosion);
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
}
