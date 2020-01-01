package com.strongholds.game.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpClient{
    public Object receive() throws IOException, ClassNotFoundException {
        Socket s = new Socket("localhost", 1035);
        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
        Object receivedObject = in.readObject();
        s.close();
        return receivedObject;
    }

    public void run() {

    }
}
