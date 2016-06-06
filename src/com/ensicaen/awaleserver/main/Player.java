/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ensicaen.awaleserver.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author jeremie
 */
public class Player extends Thread {

    private final Server server;
    private final Socket socket, otherSocket;
    private boolean threadIsAlive, mustUpdateData;

    public Player(Server s, Socket playerSocket, Socket otherPlayerSocket) {
        server = s;
        socket = playerSocket;
        otherSocket = otherPlayerSocket;
        threadIsAlive = true;
        mustUpdateData = false;
    }

    @Override
    public void run() {
        while (threadIsAlive) {
            String received = "";
            try {
                if (socket.getInputStream().available() > 0) {
                    received = readData(socket);
                    mustUpdateData = true;
                }
                
                if (received.trim().equalsIgnoreCase("quit")) {
                    server.setReceivedQuitRequest(false);
                }

                if (mustUpdateData) {
                    writeData(received, otherSocket);
                    mustUpdateData = false;
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }

    private String readData(Socket playerSocket) {
        String receivedData = "";

        try (DataInputStream dis = new DataInputStream(playerSocket.getInputStream())) {
            receivedData = dis.readUTF();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        
        return receivedData;
    }

    private void writeData(String data, Socket otherPlayerSocket) {
        try (DataOutputStream dos = new DataOutputStream(otherPlayerSocket.getOutputStream())) {
            dos.writeUTF(data);
            dos.flush();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void setThreadIsAlive(boolean threadIsAlive) {
        this.threadIsAlive = threadIsAlive;
    }
}
