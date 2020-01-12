package com.strongholds.game.event;

import java.io.Serializable;

public class ErrorEvent{
    private boolean opponentDisconnected;

    public boolean isOpponentDisconnected() {
        return opponentDisconnected;
    }

    public void setOpponentDisconnected(boolean opponentDisconnected) {
        this.opponentDisconnected = opponentDisconnected;
    }
}
