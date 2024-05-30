package com.cosmicraiders;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class HorizontalCenteredGlyphLayout extends GlyphLayout {
    private String text;
    private BitmapFont font;
    private float centeredX;

    public HorizontalCenteredGlyphLayout(BitmapFont font, String text) {
        super();
        this.font = font;
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        this.setText(font, text);
        centeredX = (Configs.resolutionX - this.width) / 2;
    }

    public float getX() {
        return this.centeredX;
    }
}