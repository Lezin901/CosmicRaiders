package com.cosmicraiders;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CosmicRaiders extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;

    public void create() {
        Assets.load();
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font

        // initialize screens
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);

        this.setScreen(mainMenuScreen);
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
        Assets.dispose();
    }

    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

}
