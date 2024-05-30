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

    final CosmicRaiders game;
    final OrthographicCamera camera;

    HorizontalCenteredGlyphLayout welcomeText;
    HorizontalCenteredGlyphLayout instructionsText;
    HorizontalCenteredGlyphLayout highscoreText;
    HorizontalCenteredGlyphLayout lastScoreText;

    /**
     * Constructs the main menu screen which is the first thing the player sees.
     * Includes a pretty texture of stars.
     * @param game the top layer of the libGDX hierarchy
     */
    public MainMenuScreen(final CosmicRaiders game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        welcomeText = new HorizontalCenteredGlyphLayout(game.font, "Welcome to Cosmic Raiders");
        instructionsText = new HorizontalCenteredGlyphLayout(game.font, "CLICK to begin");
        highscoreText = new HorizontalCenteredGlyphLayout(game.font, "");
        lastScoreText = new HorizontalCenteredGlyphLayout(game.font, "");

        game.font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

    }

    @Override
    public void show() {
        Assets.neonNoir.setVolume(Configs.volume);
        Assets.neonNoir.play();
        Assets.neonNoir.setLooping(true);
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
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        Painter painter = game.getGameScreen().getPainter();
        painter.renderStarLayers();



        game.font.draw(game.batch, welcomeText, welcomeText.getX(), 600);
        game.font.draw(game.batch, instructionsText, instructionsText.getX(), 500);


        if (Scores.getRoundsPlayed() > 0) {
            highscoreText.setText("Highscore: " + Scores.getHighscore());
            lastScoreText.setText("Last Score: " + Scores.getLastScore());
            game.font.draw(game.batch, highscoreText, highscoreText.getX(), 300);
            game.font.draw(game.batch, lastScoreText, lastScoreText.getX(), 200);
//            game.font.draw(game.batch, "Rounds played: " + Scores.getRoundsPlayed(), 450, 100);
        }

        game.batch.end();

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            System.out.println("1");
            game.getGameScreen().setGameOver(false);
            System.out.println("2");
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
