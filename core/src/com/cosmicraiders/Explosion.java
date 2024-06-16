package com.cosmicraiders;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * This class serves to define what an explosion is, mainly its size, position, and lifespan.
 */
public class Explosion {
    private float x;
    private float y;
    private float height;
    private float width;
    /**
     * This attribute documents when the explosion was created, it helps time its later deletion.
     */
    private long creationTime;

    /**
     * This constructor gets most  attributes to be filled as parameters.
     * It also saves the moment of the creation in order to time later deletion.
     * @param x the horizontal coordinate
     * @param y the vertical coordinate
     * @param height how high is the explosion
     * @param width how wide is the explosion
     */
    public Explosion(float x, float y, float height, float width) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.creationTime = TimeUtils.millis();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
