package com.strongholds.game.event;

public class ErrorEvent {
    private boolean opponentDisconnected;

    public boolean isOpponentDisconnected() {
        return opponentDisconnected;
    }

    public void setOpponentDisconnected(boolean opponentDisconnected) {
        this.opponentDisconnected = opponentDisconnected;
    }
}
