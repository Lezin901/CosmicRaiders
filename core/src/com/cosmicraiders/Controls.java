package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

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
     * Allows WASD inputs.
     * Allows UP DOWN LEFT RIGHT inputs.
     * Allows mouse inputs.
     * Allows touch inputs.
     * Uses the fighter object from gamescreen and the fighterSpeed attribute from Config.
     */
    public void handleFighterControls() {
        // left right movement
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) || isTouchLeftOfShip()) {
            gameScreen.getFighter().x -= Configs.fighterSpeed * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) || isTouchRightOfShip()) {
            gameScreen.getFighter().x += Configs.fighterSpeed * Gdx.graphics.getDeltaTime();
        }

        // up down movement
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN) || isTouchBelowShip()) {
            gameScreen.getFighter().y -= Configs.fighterSpeed * Gdx.graphics.getDeltaTime();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP) || isTouchAboveShip()) {
            gameScreen.getFighter().y += Configs.fighterSpeed * Gdx.graphics.getDeltaTime();
        }

        // setting movement boundaries
        if(gameScreen.getFighter().x < 0) gameScreen.getFighter().x = 0;
        if(gameScreen.getFighter().x > Configs.resolutionX - Configs.fighterSize) gameScreen.getFighter().x = Configs.resolutionX - Configs.fighterSize;

        // setting movement boundaries
        if(gameScreen.getFighter().y < 0) gameScreen.getFighter().y = 0;
        if(gameScreen.getFighter().y > Configs.resolutionY *2/4 - Configs.fighterSize) gameScreen.getFighter().y = Configs.resolutionY *2/4 - Configs.fighterSize;
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

    public void handleControls() {
        if (!gameScreen.isGameOver()) {
            handleGodmodeControls();
            handleFighterControls();
            gameScreen.getSpawner().spawnFighterLaser();
        }
    }

    private void handleGodmodeControls() {
        if (Gdx.input.isKeyPressed(Input.Keys.O)) {
            Configs.godMode = true;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            Configs.godMode = false;
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