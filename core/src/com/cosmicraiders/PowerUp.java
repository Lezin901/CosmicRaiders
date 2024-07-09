package com.cosmicraiders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;

/**
 * This class defines powerups. Subsequent powerup variants should inherit from this class.
 * There is currently one only power up, it increases the fighter's shooting speed.
 */
public class PowerUp extends Circle {
    private long pickupTime;
    private long duration = 5000;
    private Color backgroundColor = new Color(20, 0, 0);
    private Texture fighterImage;

    public PowerUp() {
        this.fighterImage = AssetSet.fighterRedImage;
    }

    public Texture getFighterImage() {
        return fighterImage;
    }

    public void setFighterImage(Texture fighterImage) {
        this.fighterImage = fighterImage;
    }

    public long getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(long pickupTime) {
        this.pickupTime = pickupTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
