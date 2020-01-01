package com.strongholds.game.net;

public interface INetworkController extends Runnable{
    void run();
    void addObjectRequest(Object object);
    void registerController(ObjectReceivedListener controller);
}
