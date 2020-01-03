package com.strongholds.game.controller;

public interface IMenuController {
    void startGame();
    boolean connect();
    void setIp(String ip);
    void setInPort(int port);
    void setOutPort(int port);
    void setUsername(String username);
}
