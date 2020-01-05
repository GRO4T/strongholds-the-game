package com.strongholds.game.net;

public interface INetworkController extends Runnable{
    void run();
    void addObjectRequest(Object object);
    void registerController(ObjectReceivedListener controller);
    void setInPort(int port);
    void setOutPort(int port);
    void setTargetIp(String ip);
    boolean connect();
}
