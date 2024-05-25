package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

public class Debug {
    GameScreen gameScreen;

    public Debug(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void showCoordinates() {
        // debug: show coordinates when pressing T
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            gameScreen.getCamera().unproject(touchPos);
            gameScreen.getGame().batch.draw(Assets.alienImage, touchPos.x, touchPos.y, Configs.fighterSize, Configs.fighterSize);
            System.out.println(touchPos.x + " " + touchPos.y);
        }
    }
}
