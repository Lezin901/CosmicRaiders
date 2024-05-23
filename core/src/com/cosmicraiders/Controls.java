package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * This class manages the movement inputs for the fighter (player avatar).
 * It also restarts the game if the fighter has exploded.
 * It is currently implemented in a static way, since there is only one fighter.
 */
public class Controls {

    private final GameScreen gameScreen;

    /**
     * This constructor gets a reference to the GameScreen instance.
     */
    public Controls(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }


    /**
     * Controls the movement of the fighter.
     * Allows keyboard inputs.
     */
    public void moveFighter() {

        // WASD left right movement: fighter speed
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || isTouchLeftOfShip()) {
            gameScreen.getFighter().x -= Config.fighterSpeed * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || isTouchRightOfShip()) {
            gameScreen.getFighter().x += Config.fighterSpeed * Gdx.graphics.getDeltaTime();
        }

        // WASD up down movement: fighter speed
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || isTouchBelowShip()) {
            gameScreen.getFighter().y -= Config.fighterSpeed * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || isTouchAboveShip()) {
            gameScreen.getFighter().y += Config.fighterSpeed * Gdx.graphics.getDeltaTime();
        }

        // setting movement boundaries
        if(gameScreen.getFighter().x < 0) gameScreen.getFighter().x = 0;
        if(gameScreen.getFighter().x > Config.resolutionX - Config.fighterSize) gameScreen.getFighter().x = Config.resolutionX - Config.fighterSize;

        // setting movement boundaries
        if(gameScreen.getFighter().y < 0) gameScreen.getFighter().y = 0;
        if(gameScreen.getFighter().y > Config.resolutionY *2/4 - Config.fighterSize) gameScreen.getFighter().y = Config.resolutionY *2/4 - Config.fighterSize;
    }

    private boolean isTouchLeftOfShip() {
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.getCamera().unproject(touchPos);
            return touchPos.x < gameScreen.getFighter().x + gameScreen.getFighter().width / 2;
        } else {
            return false;
        }
    }

    private boolean isTouchRightOfShip() {
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.getCamera().unproject(touchPos);
            return touchPos.x > gameScreen.getFighter().x + gameScreen.getFighter().width / 2;
        } else {
            return false;
        }
    }

    private boolean isTouchAboveShip() {
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.getCamera().unproject(touchPos);
            return touchPos.y > gameScreen.getFighter().y + gameScreen.getFighter().height / 2;
        } else {
            return false;
        }
    }

    private boolean isTouchBelowShip() {
        if(Gdx.input.isTouched()) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.getCamera().unproject(touchPos);
            return touchPos.y < gameScreen.getFighter().y + gameScreen.getFighter().height / 2;
        } else {
            return false;
        }
    }


    /**
     * Spawns a fighter laser starting from the bottom middle of the fighter ship sprite.
     * Sets lastFighterShootTime in order to set the interval between shots.
     * Adds the laser to an Array to be rendered.
     */
    public void shootFromFighter() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (TimeUtils.nanoTime() - gameScreen.getLastFighterShootTime() > 150000000) {
                Rectangle laser = new Rectangle();
                laser.width = Config.fighterLaserSize / 10;
                laser.height = Config.fighterLaserSize;
                laser.x = gameScreen.getFighter().x + Config.fighterSize / 2 - laser.width / 2;
                laser.y = gameScreen.getFighter().y + Config.fighterSize;

                gameScreen.getFighterLasers().add(laser);
                Assets.blasterShoot.play(Config.volume);
                gameScreen.setLastFighterShootTime(TimeUtils.nanoTime());
            }
        };
    }

    /**
     * Restart the game by pressing ENTER.
     */
    public void checkForRestart() {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            gameScreen.setGameOver(false);
            gameScreen.setScore(0);
            gameScreen.getFighter().x = Config.resolutionX / 2 - Config.fighterSize / 2;
            gameScreen.getFighter().y = Config.fighterSize / 4;
        }
    }


}