package com.cosmicraiders;

/**
 * This class keeps track of player scores.
 * It uses static attributes, it is not instantiated.
 */
public class Scores {
    private static int highScore;
    private static int lastScore;
    private static int roundsPlayed = 0;


    /**
     * This method returns the highScore that is tracked in Scores.
     * @return the highest score attained up to now
     */
    public static int getHighscore() {
        return highScore;
    }

    /**
     * This method returns the lastScore that is tracked in Scores.
     * @return the last score attained in the last round
     */
    public static int getLastScore() {
        return lastScore;
    }

    /**
     * This method saves that last score attained.
     * @param lastScore score from the last round
     */
    public static void setLastScore(int lastScore) {
        Scores.lastScore = lastScore;
        if (lastScore > highScore) {
            Scores.highScore = lastScore;
        }
    }

    /**
     * This method increments the roundsPlayed attribute by 1.
     */
    public static void increaseRoundsPlayed() {
        roundsPlayed++;
    }

    /**
     * This method returns the amount of rounds played.
     * @return how many rounds were played since the game was started
     */
    public static int getRoundsPlayed() {
        return Scores.roundsPlayed;
    }
}
