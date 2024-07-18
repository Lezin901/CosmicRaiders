package com.cosmicraiders;

import com.badlogic.gdx.utils.TimeUtils;

/**
 * This class handles all power ups in the game.
 * It activates and deactivates them and sets the background color accordingly.
 * There is currently one only power up, it increases the fighter's shooting speed.

 */
public class PowerUpHandler {
    private GameScreen gameScreen;

    public PowerUpHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * The main handling method for this class.
     * It checks if the active power up should still be active using the timer and deactives it if necessary.
     * Then it resets the fighter's shooting speed and the background color accordingly.
     */
    public void handlePowerUps() {
        if (gameScreen.getActivePowerUp() != null) {
            // check if active powerUp has run out
            if (gameScreen.getActivePowerUp().getPickupTime() + gameScreen.getActivePowerUp().getDuration() < TimeUtils.millis()) {
                gameScreen.setActivePowerUp(null);
                gameScreen.getConfigSet().setFighterShotsPerSecond(1.5);
                gameScreen.getConfigSet().setBackgroundColor(new CRColor(0,0,0));

            }
        }

    }

    /**
     * Activates the power up and sets the background color and the fighter's shooting speed accordingly.
     * @param powerUp The power up to be activated.
     */
    public void activatePowerUp(PowerUp powerUp) {
        gameScreen.setActivePowerUp(powerUp);
        gameScreen.getActivePowerUp().setPickupTime(TimeUtils.millis());
        gameScreen.getPowerUps().removeValue(powerUp, true);
        gameScreen.getConfigSet().setFighterShotsPerSecond(7);
        gameScreen.getConfigSet().setBackgroundColor(powerUp.getBackgroundColor());
    }
}
