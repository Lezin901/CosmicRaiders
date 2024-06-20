package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * This is the screen that is displayed when the game starts and between rounds.
 * If at least one round was played, it also displays scores.
 */
public class MainMenuScreen implements Screen {

    private final CosmicRaiders game;
    private final OrthographicCamera camera;
    private ConfigSet configSet;

    private HorizontalCenteredGlyphLayout welcomeText;
    private HorizontalCenteredGlyphLayout instructionsText;
    private HorizontalCenteredGlyphLayout highscoreText;
    private HorizontalCenteredGlyphLayout lastScoreText;

    /**
     * Constructs the main menu screen which is the first thing the player sees.
     * Includes a pretty texture of stars.
     * @param game the top layer of the libGDX hierarchy
     */
    public MainMenuScreen(final CosmicRaiders game) {
        this.game = game;
        configSet = game.getConfigSet();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        welcomeText = new HorizontalCenteredGlyphLayout(game.getFont(), "Welcome to Cosmic Raiders", configSet.getResolutionX());
        instructionsText = new HorizontalCenteredGlyphLayout(game.getFont(), "CLICK to begin", configSet.getResolutionX());
        highscoreText = new HorizontalCenteredGlyphLayout(game.getFont(), "", configSet.getResolutionX());
        lastScoreText = new HorizontalCenteredGlyphLayout(game.getFont(), "", configSet.getResolutionX());

        game.getFont().getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

    }

    @Override
    public void show() {
        AssetSet.neonNoir.setVolume(configSet.getVolume());
        AssetSet.neonNoir.play();
        AssetSet.neonNoir.setLooping(true);
    }

    /**
     * The render() method checks for player input to start game.
     * It also checks the Scores class for scores to display.
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        Painter painter = game.getGameScreen().getPainter();
        painter.renderStarLayers(0.3f);


        float logoWidth = 700;
        game.getBatch().draw(AssetSet.cosmicRaidersLogoImage, configSet.getResolutionX() / 2 - logoWidth/2, 600, logoWidth, 300);

        //game.font.draw(game.batch, welcomeText, welcomeText.getX(), 600);
        game.getFont().draw(game.getBatch(), instructionsText, instructionsText.getX(), 500);


        if (Scores.getRoundsPlayed() > 0) {
            highscoreText.setText("Highscore: " + Scores.getHighscore());
            lastScoreText.setText("Last Score: " + Scores.getLastScore());
            game.getFont().draw(game.getBatch(), highscoreText, highscoreText.getX(), 300);
            game.getFont().draw(game.getBatch(), lastScoreText, lastScoreText.getX(), 200);
//            game.font.draw(game.batch, "Rounds played: " + Scores.getRoundsPlayed(), 450, 100);
        }

        game.getBatch().end();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.getGameScreen().setGameOver(false);
            game.setScreen(game.getGameScreen());
        }
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
    public void dispose() {

    }
}
