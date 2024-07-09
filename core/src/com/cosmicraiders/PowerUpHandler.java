package com.cosmicraiders;

import com.badlogic.gdx.utils.TimeUtils;

import java.awt.*;

public class PowerUpHandler {
    private GameScreen gameScreen;

    public PowerUpHandler(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void handlePowerUps() {
        if (gameScreen.getActivePowerUp() != null) {
            if (gameScreen.getActivePowerUp().getPickupTime() + gameScreen.getActivePowerUp().getDuration() < TimeUtils.millis()) {
                gameScreen.setActivePowerUp(null);
                gameScreen.getConfigSet().setFighterShotsPerSecond(1);
                gameScreen.getConfigSet().setBackgroundColor(Color.BLACK);

            }
        }

    }

    public void activatePowerUp(PowerUp powerUp) {
        gameScreen.setActivePowerUp(powerUp);
        gameScreen.getActivePowerUp().setPickupTime(TimeUtils.millis());
        gameScreen.getPowerUps().removeValue(powerUp, true);
        gameScreen.getConfigSet().setFighterShotsPerSecond(5);
        gameScreen.getConfigSet().setBackgroundColor(powerUp.getBackgroundColor());
    }
}
