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
    private long alienChangeDirectionTime;


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
        this.controls = new Controls(this);
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
        alienChangeDirectionTime = TimeUtils.millis();
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

        // show score in top left corner
        game.font.draw(game.batch, "Score: " + score, 0, Configs.resolutionY);

        // debug: show coordinates when pressing T
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            batch.draw(Assets.alienImage, touchPos.x, touchPos.y, Configs.fighterSize, Configs.fighterSize);
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
        for(Asteroid asteroid: asteroids) { // asteroids
            // batch.draw(Assets.asteroidImage, asteroid.x - asteroid.radius, asteroid.y - asteroid.radius, asteroid.radius * 2, asteroid.radius * 2);
            batch.draw(Assets.asteroidImage, asteroid.x - asteroid.radius, asteroid.y - asteroid.radius, asteroid.radius, asteroid.radius, asteroid.radius * 2, asteroid.radius * 2, 1, 1, asteroid.getRotation(), 0, 0, 59, 59, false, false);
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
        for(Rectangle alienLaser: alienLasers) { // alien lasers
            batch.draw(Assets.laserGreenImage, alienLaser.x, alienLaser.y, alienLaser.width, alienLaser.height);
        }
        batch.end();

        // movement or restart
        if (!gameOver) {
            controls.moveFighter();
            controls.shootFromFighter();
        } else {
            controls.checkForRestart();
        }

        // fighterLaser movement
        for (Iterator<Rectangle> laserIterator = fighterLasers.iterator(); laserIterator.hasNext();) {
            Rectangle laser = laserIterator.next();
            // move laser
            laser.y += 800 * Gdx.graphics.getDeltaTime(); // laser speed

            if(laser.y >= Configs.resolutionY) laserIterator.remove();
        }

        // check for fighterLaser collision
        for (Iterator<Rectangle> LaserIter = fighterLasers.iterator(); LaserIter.hasNext(); ) {
            Rectangle laser = LaserIter.next();
            // check for fighterLaser-asteroid collision
            for (Iterator<Asteroid> AsteroidIter = asteroids.iterator(); AsteroidIter.hasNext(); ) {
                Asteroid asteroid = AsteroidIter.next();
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
        for (Iterator<Asteroid> asteroidIter = asteroids.iterator(); asteroidIter.hasNext(); ) {
            Circle asteroid = asteroidIter.next(); // asteroids are circles!
            if (Intersector.overlaps(asteroid, fighter) && gameOver == false) {
                gameOver = true;
                Explosion fighterExplosion = new Explosion(fighter.x + fighter.width / 2, fighter.y + fighter.height / 2, 256, 256);
                fighterExplosion.setCreationTime(TimeUtils.nanoTime() + 1000000000);
                explosions.add(fighterExplosion);
            }
        }

        // rotate asteroids
        for (Asteroid asteroid : asteroids) {
            asteroid.updateRotation(Gdx.graphics.getDeltaTime());
        }

        // alienLaser movement
        for (Iterator<Rectangle> alienLaserIterator = alienLasers.iterator(); alienLaserIterator.hasNext();) {
            Rectangle alienLaser = alienLaserIterator.next();
            // move alienLaser
            alienLaser.y -= 800 * Gdx.graphics.getDeltaTime(); // alienLaser speed
            // check for alienLaser-fighter collision
            if(Intersector.overlaps(alienLaser, fighter) && gameOver == false) {
                destroyFighter();
                alienLaserIterator.remove();
            }
            // check if out of bounds
            if(alienLaser.y <= 0 - Configs.alienLaserSize) alienLaserIterator.remove();
        }

        // throws up a "Game Over" screen
        if (gameOver == true) {
            game.batch.begin();
            game.font.draw(game.batch, "Game Over!", Configs.resolutionX*1/5, Configs.resolutionY*3/5);
            game.font.draw(game.batch, "Score: " + score, Configs.resolutionX*1/5, Configs.resolutionY*3/5- Configs.fighterSize);
            game.font.draw(game.batch, "Press ENTER to continue", Configs.resolutionX*1/5, Configs.resolutionY*3/5- Configs.fighterSize*4);
            game.batch.end();
        }

        // spawn asteroids
        if(TimeUtils.nanoTime() - lastAsteroidTime > 500000000) spawnAsteroid();

        // move asteroids
        for (Iterator<Asteroid> iter = asteroids.iterator(); iter.hasNext(); ) {
            Circle asteroid = iter.next();
            asteroid.y -= Configs.asteroidSpeed * Gdx.graphics.getDeltaTime(); // asteroid speed
            if(asteroid.y < -asteroid.radius * 2) iter.remove();
        }

        // spawn alien
        if(alienDead && (TimeUtils.nanoTime() - lastAlienTime > 2000000000)) spawnAlien();
//        System.out.println("Debug: number of aliens: " + aliens.size);

        // move alien
        for (Iterator<Rectangle> iter = aliens.iterator(); iter.hasNext(); ) {
            Rectangle alien = iter.next();
            if ((alien.x <= Configs.padding) || (alien.x >= Configs.resolutionX - Configs.padding - Configs.fighterSize)) {
                aliensMoveToRight = !aliensMoveToRight;
            }
            if(aliensMoveToRight) {
                alien.x -= 200 * Gdx.graphics.getDeltaTime(); // alien speed
            } else {
                alien.x += 200 * Gdx.graphics.getDeltaTime(); // alien speed
            }
        }

        if (alienChangeDirectionTime <= TimeUtils.millis()) {
            alienChangeDirectionTime = TimeUtils.millis() + MathUtils.random(3000, 5000);
            aliensMoveToRight = !aliensMoveToRight;
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
        Assets.explosion.play(Configs.volume/2);
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
        Assets.explosion.play(Configs.volume/2);
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
        Assets.explosion.play(Configs.volume/2);
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

    public boolean isAliensMoveToRight() {
        return aliensMoveToRight;
    }

    public void setAliensMoveToRight(boolean aliensMoveToRight) {
        this.aliensMoveToRight = aliensMoveToRight;
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
