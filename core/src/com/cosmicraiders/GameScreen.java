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

    private float volume = 0;
    private int ResolutionX = 1920;
    private int ResolutionY = 1080;
    private int fighterSize = 80;
    private int alienSize = 80;
    private int fighterLaserSize = 80;
    private int alienLaserSize = 80;
    private int asteroidDiameter = 100;
    private boolean gameOver = false;
    private int score = 0;
    private int padding = ResolutionX / 100;
    private boolean aliensMoveToRight = true;
    private boolean alienDead = true;

    /**
     * using the constructor instead of the create() method
     * this builds a GameScreen instance using the Game instance
     * @param game; the Game instance, here: CosmicRaiders
     */
    public GameScreen(final CosmicRaiders game) {
        this.game = game;

        // start the playback of the background music immediately
        Assets.beepbop.setLooping(true);
        Assets.beepbop.setVolume(volume);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, ResolutionX, ResolutionY);

        batch = game.batch; // this ignores encapsulation as intended by LibGDX

        fighter = new Rectangle();
        fighter.x = ResolutionX / 2 - fighterSize / 2;
        fighter.y = fighterSize / 4;
        fighter.width = fighterSize;
        fighter.height = fighterSize * (57f / 46f);

        fighterLasers = new Array<Rectangle>();
        alienLasers = new Array<Rectangle>();
        asteroids = new Array<Circle>();
        explosions = new Array<Explosion>();
        aliens = new Array<Rectangle>();

        spawnAsteroid();
    }

    /**
     * This method ensures a steady stream of asteroids falling from the top of the screen towards the bottom.
     * The spawned asteroids are added to an "Array" List which is used in render().
     * Asteroids are Circles, not Rectangles. Most other objects are Rectangles.
     */
    private void spawnAsteroid() {
        Circle asteroid = new Circle();
        float randomSizeMultiplier = MathUtils.random(0.5f, 1.5f);
        asteroid.radius = ((asteroidDiameter / 2) * randomSizeMultiplier);
        asteroid.x = MathUtils.random(fighterSize, ResolutionX - fighterSize);
        asteroid.y = ResolutionY + asteroid.radius;
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
        alien.width = alienSize;
        alien.height = alienSize * (57f / 46f);
        alien.x = MathUtils.random(padding, ResolutionX - alienSize - padding);
        alien.y = ResolutionY - alien.height - padding;
        aliens.add(alien);
        alienDead = false;
    }

    /**
     *
     * @param x
     * @param y
     */
    private void spawnAlienLaser(float x, float y) {
        Rectangle laser = new Rectangle();
        laser.width = alienLaserSize / 10;
        laser.height = alienLaserSize;
        laser.x = x + alienSize / 2 - laser.width / 2;
        laser.y = y - alienSize;

        alienLasers.add(laser);
        Assets.blasterShoot.play(volume);
        lastAlienShootTime = TimeUtils.nanoTime();
    }

    private void spawnFighterLaser() {
        if (TimeUtils.nanoTime() - lastFighterShootTime > 150000000) {
            Rectangle laser = new Rectangle();
            laser.width = fighterLaserSize / 10;
            laser.height = fighterLaserSize;
            laser.x = fighter.x + fighterSize / 2 - laser.width / 2;
            laser.y = fighter.y + fighterSize;

            fighterLasers.add(laser);
            Assets.blasterShoot.play(volume);
            lastFighterShootTime = TimeUtils.nanoTime();
        }
    }





    @Override
    public void render (float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //SpriteBatch rendering code
        batch.begin();

        // show score in top left corner
        game.font.draw(game.batch, "Score: " + score, 0, ResolutionY);

        // debug: show coordinates when pressing T
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            batch.draw(Assets.alienImage, touchPos.x, touchPos.y, fighterSize, fighterSize);
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

        // render all objects
        if (gameOver == false) {
            batch.draw(Assets.fighterImage, fighter.x, fighter.y, fighter.width, fighter.height);
        }
        for(Rectangle laser: fighterLasers) {
            batch.draw(Assets.laserRedImage, laser.x, laser.y, laser.width, laser.height);
        }
        for(Rectangle alienLaser: alienLasers) {
            batch.draw(Assets.laserGreenImage, alienLaser.x, alienLaser.y, alienLaser.width, alienLaser.height);
        }
        for(Circle asteroid: asteroids) {
            batch.draw(Assets.asteroidImage, asteroid.x - asteroid.radius, asteroid.y - asteroid.radius, asteroid.radius * 2, asteroid.radius * 2);
        }
        for(Explosion explosion: explosions) {
            batch.draw(Assets.asteroidExplosionImage, explosion.getX() - explosion.getWidth() / 2, explosion.getY() - explosion.getHeight() / 2, explosion.getWidth(), explosion.getHeight());
            if(explosion.getCreationTime() < TimeUtils.nanoTime() - 500000000) {
                explosions.removeValue(explosion, true);
            }
        }
        for(Rectangle alien: aliens) {
            batch.draw(Assets.alienImage, alien.x, alien.y, alien.width, alien.height);
        }
        batch.end();

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
            if(fighter.x > ResolutionX - fighterSize) fighter.x = ResolutionX - fighterSize;

            // setting movement boundaries
            if(fighter.y < 0) fighter.y = 0;
            if(fighter.y > ResolutionY *2/4 - fighterSize) fighter.y = ResolutionY *2/4 - fighterSize;

            // shoot
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) spawnFighterLaser();
        }

        // restart the game by pressing tab
        if(gameOver && Gdx.input.isKeyPressed(Input.Keys.TAB)) {
            gameOver = false;

            fighter.x = ResolutionX / 2 - fighterSize / 2;
            fighter.y = fighterSize / 4;

            score = 0;

            System.out.println("Game restarted. Score has been reset. ");
        }

        // fighterLaser movement
        for (Iterator<Rectangle> laserIterator = fighterLasers.iterator(); laserIterator.hasNext();) {
            Rectangle laser = laserIterator.next();
            // move laser
            laser.y += 800 * Gdx.graphics.getDeltaTime(); // laser speed

            if(laser.y >= ResolutionY) laserIterator.remove();
        }

        // check for fighterLaser collision
        for (Iterator<Rectangle> LaserIter = fighterLasers.iterator(); LaserIter.hasNext(); ) {
            Rectangle laser = LaserIter.next();
            // check for fighterLaser-asteroid collision
            for (Iterator<Circle> AsteroidIter = asteroids.iterator(); AsteroidIter.hasNext(); ) {
                Circle asteroid = AsteroidIter.next();
                if(Intersector.overlaps(asteroid,laser)) {
//                    score++;
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
            Circle asteroid = asteroidIter.next();

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
            if(alienLaser.y <= 0 - alienLaserSize) alienLaserIterator.remove();
        }

        if (gameOver == true) {
            game.batch.begin();
            game.font.draw(game.batch, "Game Over!", ResolutionX/2-fighterSize, ResolutionY/2-fighterSize);
            game.font.draw(game.batch, "Score: " + score, ResolutionX/2-fighterSize, ResolutionY/2-fighterSize*2);
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
            if ((alien.x <= padding) || (alien.x >= ResolutionX - padding - fighterSize)) {
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

    private void laserHitsAsteroid(Circle asteroid) {
        Assets.explosion.play(volume/2);
        explosions.add(new Explosion(asteroid.x, asteroid.y, asteroid.radius * 2, asteroid.radius * 2));
    }

    private void laserHitsAlien(Rectangle alien) {
        score++;
        Assets.explosion.play(volume/2);
        explosions.add(new Explosion(alien.x + alien.width/2 , alien.y + alien.height/2, alien.height, alien.width));
        alienDead = true;
        lastAlienTime = TimeUtils.nanoTime();
        aliensMoveToRight = MathUtils.randomBoolean();
    }

    private void destroyFighter() {
        gameOver = true;
        Explosion fighterExplosion = new Explosion(fighter.x + fighter.width / 2, fighter.y + fighter.height / 2, 256, 256);
        fighterExplosion.setCreationTime(TimeUtils.nanoTime() + 1000000000);
        explosions.add(fighterExplosion);
    }

    @Override
    public void show() {
        // start the playback of the background music
        // when the screen is shown
        Assets.beepbop.play();
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
