package com.strongholds.game.net;

import com.strongholds.game.GameSingleton;
import com.strongholds.game.controller.ViewEvent;
import com.strongholds.game.exception.CannotConnectException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class TcpServer implements INetworkController{
    ServerSocket receivingSocket;
    LinkedBlockingQueue<Object> objectsToSend;
    LinkedBlockingQueue<Object> receivedObjects;
    ObjectReceivedListener controller;

    int inPort = 46000;
    int outPort = 46000;
    String ip = "127.0.0.1";

    public TcpServer(){
        try{
            receivingSocket = new ServerSocket(inPort);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        objectsToSend = new LinkedBlockingQueue<>();
        receivedObjects = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        if (!connect())
            throw new CannotConnectException("can't connect");
        //send objects
        Thread senderThread = new Thread(new ObjectSender());
        senderThread.start();
        Thread receiverThread = new Thread(new ObjectReceiver());
        receiverThread.start();

        while(true){
            if (receivedObjects.size() > 0)
                controller.notify(receivedObjects);
        }
    }

    private boolean connect(){
        return true;
    }


    public void addObjectRequest(Object object) {
        objectsToSend.add(object);
    }

    public void setInPort(int port){
        inPort = port;
    }

    public void setOutPort(int port){
        outPort = port;
    }

    public void setTargetIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void registerController(ObjectReceivedListener controller) {
        this.controller = controller;
    }

    private class ObjectSender implements Runnable{
        @Override
        public void run() {
            while(true){
                if (objectsToSend.size() > 0){
                    System.out.println("objects to send!");

                    Object objToSend = objectsToSend.poll();
                    Socket s = null;
                    try {
                        s = receivingSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ObjectOutputStream out = null;
                    try {
                        out = new ObjectOutputStream(s.getOutputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.writeObject(objToSend);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class ObjectReceiver implements Runnable{
        @Override
        public void run(){
            while(true){
                System.out.println("waiting for new objects");

                Socket s = null;
                try {
                    s = new Socket(ip, outPort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ObjectInputStream in = null;
                try {
                    in = new ObjectInputStream(s.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Object receivedObj = null;
                try {
                    receivedObj = in.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                receivedObjects.add(receivedObj);
            }
        }
    }
}
