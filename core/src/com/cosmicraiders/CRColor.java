package com.cosmicraiders;

public class CRColor {
    private int r = 0;
    private int g = 0;
    private int b = 0;

    public CRColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }


    public int getRed() {
        return r;
    }

    public void setRed(int r) {
        this.r = r;
    }

    public int getGreen() {
        return g;
    }

    public void setGreen(int g) {
        this.g = g;
    }

    public int getBlue() {
        return b;
    }

    public void setBlue(int b) {
        this.b = b;
    }

    public float getRedFloat() {
        return (r / 255.0f);
    }

    public float getGreenFloat() {
        return (g / 255.0f);
    }
    public float getBlueFloat() {
        return (b / 255.0f);
    }
}
