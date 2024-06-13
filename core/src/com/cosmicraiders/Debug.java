package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

/**
 * This is a helper class which contains a method to show the coordinates of the mouse / touch pointer for debugging.
 */
public class Debug {
    GameScreen gameScreen;

    public Debug(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Debug: show coordinates when pressing T, also display an alien at the coordinates pointed to.
     */
    public void showCoordinates() {
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.getCamera().unproject(touchPos);
            gameScreen.getGame().getBatch().draw(Assets.alienImage, touchPos.x, touchPos.y, gameScreen.getConfigSet().getFighterSize(), gameScreen.getConfigSet().getFighterSize());
            System.out.println(touchPos.x + " " + touchPos.y);
        }
    }
}
