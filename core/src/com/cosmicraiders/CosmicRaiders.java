package com.cosmicraiders;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * This instance of Game is the "root" of the libGDX class hierarchy.
 * It is created by the Launcher.
 * It creates instances of MainMenuScreen and GameScreen, a SpriteBatch for textures, etc.
 */
public class CosmicRaiders extends Game {

    /**
     * This batch contains the textures (sprites) which are rendered each frame. An important libGDX concept.
     */
    private SpriteBatch batch;
    private BitmapFont font;
    private MainMenuScreen mainMenuScreen;
    private GameScreen gameScreen;
    private ConfigSet configSet;

    /**
     * This method constructs the necessary objects to start the game.
     */
    public void create() {
        Assets.load();
        batch = new SpriteBatch();
        configSet = new ConfigSet();
        font = new BitmapFont(Gdx.files.internal("fonts/CaveStoryGreen.fnt"), Gdx.files.internal("fonts/CaveStoryGreen.png"), false); // use libGDX's default Arial font
        font.getData().setScale(2);


        // initialize screens
        mainMenuScreen = new MainMenuScreen(this);
        gameScreen = new GameScreen(this);

        this.setScreen(mainMenuScreen);
    }

    public void render() {
        super.render(); // important!
    }

    /**
     * This frees up resources.
     */
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

    public ConfigSet getConfigSet() {
        return configSet;
    }

    public void setConfigSet(ConfigSet configSet) {
        this.configSet = configSet;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }
}
