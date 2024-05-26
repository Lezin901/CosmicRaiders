package com.cosmicraiders;

public class Scores {
    private static int highscore;
    private static int lastScore;


    public static int getHighscore() {
        return highscore;
    }

    public static void setHighscore(int highscore) {
        Scores.highscore = highscore;
    }

    public static int getLastScore() {
        return lastScore;
    }

    public static void setLastScore(int lastScore) {
        Scores.lastScore = lastScore;
    }
}
