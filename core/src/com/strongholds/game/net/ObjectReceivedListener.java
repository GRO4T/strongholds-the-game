package com.strongholds.game.net;

import java.util.concurrent.LinkedBlockingQueue;

public interface ObjectReceivedListener {
    void notify(LinkedBlockingQueue<Object> receivedObjects);
}
