package com.cosmicraiders;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;

/**
 * This class defines an Asteroid as a Circle and gives it a few more attributes.
 */
public class Asteroid extends Circle {
    private float rotation;
    private float rotationSpeed;
    private boolean rotatesClockwise;

    /**
     * Constructs an Asteroid with randomised rotation attributes.
     */
    public Asteroid() {
        rotation = MathUtils.random(0.0f, 360.0f);
        rotationSpeed = MathUtils.random(0.0f, 1.0f);
        rotatesClockwise = MathUtils.randomBoolean();
    }

    /**
     * Which way the Asteroid faces.
     * @return rotation
     */
    public float getRotation() {
        return this.rotation;
    }

    /**
     * How fast the Asteroid rotates.
     * @return rotationSpeed
     */
    public float getRotationSpeed() {
        return this.rotationSpeed;
    }

    /**
     * Set the way the Asteroid faces.
     * @param rotation
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * Turns the asteroid clockwise or counter-clockwise depending on the rotatesClockwise attribute.
     * @param delta a parameter connected to the libGDX graphics renderer
     */
    public void updateRotation(float delta) {
        rotation = rotatesClockwise ? (rotation + delta * 100 * rotationSpeed) : (rotation - delta * 100 * rotationSpeed);
    }
}
