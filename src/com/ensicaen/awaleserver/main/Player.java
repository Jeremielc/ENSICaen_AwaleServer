package com.ensicaen.awaleserver.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Contains information on the players in order to enable communication.
 *
 * @author Pierrick Hue and Jérémie Leclerc
 */
public class Player extends Thread {

    private final Server server;
    private final Socket socket, otherSocket;
    private boolean threadIsAlive, mustUpdateData;

    /**
     * Instanciate a player object.
     *
     * @param s The server object. Used to managed 'quit' request.
     * @param playerSocket Player's socket to receive informations.
     * @param otherPlayerSocket Opponent's socket to send informations.
     */
    public Player(Server s, Socket playerSocket, Socket otherPlayerSocket) {
        server = s;
        socket = playerSocket;
        otherSocket = otherPlayerSocket;
        threadIsAlive = true;
        mustUpdateData = false;
    }

    @Override
    /**
     * Look for information and transfer them.
     */
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

    /**
     * Read string from player's socket.
     *
     * @param playerSocket Player's socket.
     * @return A string representing the informations.
     */
    private String readData(Socket playerSocket) {
        String receivedData = "";

        try (DataInputStream dis = new DataInputStream(playerSocket.getInputStream())) {
            receivedData = dis.readUTF();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

        return receivedData;
    }

    /**
     * Wrtite string to opponent's socket.
     *
     * @param data The informations to send.
     * @param otherPlayerSocket Opponent's socket.
     */
    private void writeData(String data, Socket otherPlayerSocket) {
        try (DataOutputStream dos = new DataOutputStream(otherPlayerSocket.getOutputStream())) {
            dos.writeUTF(data);
            dos.flush();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Use to enable the thread to run and to stop it.
     *
     * @param threadIsAlive A boolean telling if the thread must run or not.
     */
    public void setThreadIsAlive(boolean threadIsAlive) {
        this.threadIsAlive = threadIsAlive;
    }
}
