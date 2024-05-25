package com.cosmicraiders;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

public class Asteroid extends Circle {
    private float rotation;
    private float rotationSpeed;
    private boolean rotatesClockwise;


    public Asteroid() {
        rotation = MathUtils.random(0.0f, 360.0f);
        rotationSpeed = MathUtils.random(0.0f, 1.0f);
        rotatesClockwise = MathUtils.randomBoolean();
    }

    public float getRotation() {
        return this.rotation;
    }
    public float getRotationSpeed() {
        return this.rotationSpeed;
    }
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
    public void updateRotation(float delta) {
        rotation = rotatesClockwise ? (rotation + delta * 100 * rotationSpeed) : (rotation - delta * 100 * rotationSpeed);
    }
}
