package com.cosmicraiders;

public class Scores {
    private static int highScore;
    private static int lastScore;
    private static int roundsPlayed = 0;


    public static int getHighscore() {
        return highScore;
    }

    public static int getLastScore() {
        return lastScore;
    }

    public static void setLastScore(int lastScore) {
        Scores.lastScore = lastScore;
        if (lastScore > highScore) {
            Scores.highScore = lastScore;
        }
    }
    public static void increaseRoundsPlayed() {
        roundsPlayed++;
    }

    public static int getRoundsPlayed() {
        return Scores.roundsPlayed;
    }
}
