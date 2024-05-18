package com.cosmicraiders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {

    final CosmicRaiders game;

    OrthographicCamera camera;
    Texture supernovaImage;

    public MainMenuScreen(final CosmicRaiders game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        supernovaImage = new Texture(Gdx.files.internal("supernovas.png"));

        game.font.getData().setScale(5);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.font.draw(game.batch, "Welcome to Cosmic Raiders!!! ", 450, 500);
        game.font.draw(game.batch, "Press TAB to begin!", 450, 400);
        game.batch.draw(supernovaImage, 450, 600, 400, 400);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
            game.setScreen(new GameScreen(game));
            dispose();
        }

        // debug: show coordinates when pressing T
        if(Gdx.input.isKeyPressed(Input.Keys.T)) {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            System.out.println(touchPos.x + " " + touchPos.y);
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
