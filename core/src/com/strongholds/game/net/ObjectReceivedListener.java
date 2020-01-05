package com.strongholds.game.net;

import com.strongholds.game.event.ErrorEvent;

import java.util.concurrent.LinkedBlockingQueue;

public interface ObjectReceivedListener {
    void notify(LinkedBlockingQueue<Object> receivedObjects);
    void notifyOnError(ErrorEvent errorEvent);
}
