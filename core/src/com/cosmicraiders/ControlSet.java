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
public class ControlSet {

    private final GameScreen gameScreen;
    private long lastGodModeInputTime = TimeUtils.millis();

    /**
     * This constructor gets a reference to the GameScreen instance.
     */
    public ControlSet(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Controls the movement of the fighter.
     * Allows WASD inputs.
     * Allows UP DOWN LEFT RIGHT inputs.
     * Allows mouse inputs.
     * Allows touch inputs.
     * Uses the fighter object from gamescreen and the fighterSpeed attribute from Config.
     */
    public void handleFighterControls() {
        int speed = gameScreen.getConfigSet().getFighterSpeed();
        Rectangle fighter = gameScreen.getFighter();

        // left right movement
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || isTouchLeftOfShip()) {
            fighter.x -= speed * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || isTouchRightOfShip()) {
            fighter.x += speed * Gdx.graphics.getDeltaTime();
        }

        // up down movement
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || isTouchBelowShip()) {
            fighter.y -= speed * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || isTouchAboveShip()) {
            fighter.y += speed * Gdx.graphics.getDeltaTime();
        }

        // setting movement boundaries
        if(fighter.x < 0) fighter.x = 0;
        if(fighter.x > gameScreen.getConfigSet().getResolutionX() - gameScreen.getConfigSet().getFighterSize())
            fighter.x = gameScreen.getConfigSet().getResolutionX() - gameScreen.getConfigSet().getFighterSize();

        // setting movement boundaries
        if(fighter.y < 0) fighter.y = 0;
        if(fighter.y > gameScreen.getConfigSet().getResolutionY() *2/4 - gameScreen.getConfigSet().getFighterSize()) fighter.y = gameScreen.getConfigSet().getResolutionY() *2/4 - gameScreen.getConfigSet().getFighterSize();
    }

    /**
     * checks if mouse or touch was left of the fighter
     * @return boolean true if left
     */
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

    /**
     * checks if mouse or touch was right of the fighter
     * @return boolean true if right
     */
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

    /**
     * checks if mouse or touch was above the fighter
     * @return boolean true if above
     */
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

    /**
     * checks if mouse or touch was below the fighter
     * @return boolean true if below
     */
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
     * This method handles all inputs from the player and calls the required methods.
     */
    public void handleControls() {
        if (!gameScreen.isGameOver()) {
            handleGodmodeControls();
            handleFighterControls();
            gameScreen.getSpawner().spawnFighterLaser();
        }
    }

    /**
     * This is for developers and cheaters. It allows invulnerability and difficulty changes.
     */
    private void handleGodmodeControls() {
        if (TimeUtils.millis() > lastGodModeInputTime + 200) {
            if (Gdx.input.isKeyPressed(Input.Keys.O)) {
                gameScreen.getConfigSet().setGodMode(!gameScreen.getConfigSet().isGodMode());
                lastGodModeInputTime = TimeUtils.millis();
            }
            if (gameScreen.getConfigSet().isGodMode()) {
                handleAsteroidControls();
                handleAlienControls();
                handleFighterGodmodeControls();
            }
        }
    }

    private void handleAlienControls() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_2) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            gameScreen.getConfigSet().decreaseAlienSpeed();
            lastGodModeInputTime = TimeUtils.millis();
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            gameScreen.getConfigSet().increaseAlienSpeed();
            lastGodModeInputTime = TimeUtils.millis();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.NUM_3) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            gameScreen.getConfigSet().decreaseAlienRateOfFire();
            lastGodModeInputTime = TimeUtils.millis();
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)) {
            gameScreen.getConfigSet().increaseAlienRateOfFire();
            lastGodModeInputTime = TimeUtils.millis();
        }
    }

    private void handleAsteroidControls() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1) && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            gameScreen.getConfigSet().increaseAsteroidSpeed();
            lastGodModeInputTime = TimeUtils.millis();
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.NUM_1) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            gameScreen.getConfigSet().decreaseAsteroidSpeed();
            lastGodModeInputTime = TimeUtils.millis();
        }
    }

    private void handleFighterGodmodeControls() {
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_4) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            gameScreen.getConfigSet().decreaseFighterShotsPerSecond();
            lastGodModeInputTime = TimeUtils.millis();
        } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)) {
            gameScreen.getConfigSet().increaseFighterShotsPerSecond();
            lastGodModeInputTime = TimeUtils.millis();
        }
    }

    // old code: the game can be restarted now from the main menu after death
//    /**
//     * Restart the game by pressing ENTER.
//     */
//    public void checkForRestart() {
//        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
//            gameScreen.setGameOver(false);
//            gameScreen.setScore(0);
//            gameScreen.getFighter().x = Configs.resolutionX / 2 - Configs.fighterSize / 2;
//            gameScreen.getFighter().y = Configs.fighterSize / 4;
//        }
//    }


}