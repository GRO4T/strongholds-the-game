package com.strongholds.game.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class TcpServer implements INetworkController{
    private ServerSocket opponentClientSocket;
    private LinkedBlockingQueue<Object> objectsToSend;
    private LinkedBlockingQueue<Object> receivedObjects;
    private ObjectReceivedListener controller;

    private int inPort = 46000;
    private int outPort = 46000;
    private String ip = "127.0.0.1";

    public TcpServer(){
        objectsToSend = new LinkedBlockingQueue<>();
        receivedObjects = new LinkedBlockingQueue<>();
    }

    @Override
    public void run() {
        Thread senderThread = new Thread(new ObjectSender());
        senderThread.start();
        Thread receiverThread = new Thread(new ObjectReceiver());
        receiverThread.start();

        while(true){
            if (receivedObjects.size() > 0)
                controller.notify(receivedObjects);
        }
    }

    public boolean connect(){
        System.out.println(ip);
        System.out.println(outPort);
        System.out.println(inPort);
        try {
            if (opponentClientSocket != null)
                opponentClientSocket.close();
            opponentClientSocket = new ServerSocket(outPort);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        ConnectionEstablisher connectionEstablisher = new ConnectionEstablisher();
        Thread connectionEstablisherThread = new Thread(connectionEstablisher);
        connectionEstablisherThread.start();

        try{
            Thread.sleep(2000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
            connectionEstablisherThread.interrupt();
            return false;
        }

        connectionEstablisher.stop();
        connectionEstablisherThread.interrupt();
        return connectionEstablisher.isConnected();
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

    private class ConnectionEstablisher implements Runnable{
        private volatile boolean connected = false;
        private Thread helloSender = null;
        private Thread helloReceiver = null;

        @Override
        public void run() {
            connect();
        }

        private void connect(){
            helloSender = new Thread(new Runnable() {
                Socket sender = null;
                @Override
                public void run() {
                        try{
                            //if (sender != null)
                             //   sender.close();
                            sender = opponentClientSocket.accept();
                            DataOutputStream outputStream = new DataOutputStream(sender.getOutputStream());
                            outputStream.writeBytes("hello");
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println("sender error");
                            return;
                        } catch (Exception e){
                            e.printStackTrace();
                            return;
                        }
                    }
            });

            helloReceiver = new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true){
                        Socket receiver = null;
                        try{
                            receiver = new Socket(ip, inPort);
                            DataInputStream inputStream = new DataInputStream(receiver.getInputStream());
                            String bytes = new String(inputStream.readNBytes(5));
                            System.out.println("received bytes = " + bytes);
                            connected = true;
                            receiver.close();
                            return;
                        }
                        catch(IOException e){
                            e.printStackTrace();
                            System.out.println("receiver error");
                            if (receiver != null) {
                                try {
                                    receiver.close();
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                }
            });

            helloSender.start();
            helloReceiver.start();

            while(!connected){

            }
        }

        public boolean isConnected(){
            return connected;
        }

        public void stop(){
                if (helloReceiver != null)
                    helloReceiver.interrupt();
                if (helloSender != null)
                    helloSender.interrupt();
        }
    }


    private class ObjectSender implements Runnable{
        @Override
        public void run() {
            while(true){
                if (objectsToSend.size() > 0){
                    System.out.println("objects to send!");

                    Object objToSend = objectsToSend.poll();
                    ObjectOutputStream out;
                    Socket s;
                    try {
                        s = opponentClientSocket.accept();
                        out = new ObjectOutputStream(s.getOutputStream());
                        out.writeObject(objToSend);
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

                Socket s;
                ObjectInputStream in;
                Object receivedObj = null;
                try {
                    s = new Socket(ip, inPort);
                    in = new ObjectInputStream(s.getInputStream());
                    receivedObj = in.readObject();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                receivedObjects.add(receivedObj);
            }
        }
    }
}
