package com.cosmicraiders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;


/**
 * This class defines powerups. Subsequent powerup variants should inherit from this class.
 * There is currently one only power up, it increases the fighter's shooting speed.
 */
public class PowerUp extends Circle {
    private long pickupTime;
    private long duration = 4000;
    private CRColor backgroundColor = new CRColor(20, 0, 0);
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

    public CRColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(CRColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
