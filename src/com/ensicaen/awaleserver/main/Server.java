package com.ensicaen.awaleserver.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private final Socket[] players;
    private Player player_1, player_2;
    private boolean threadIsAlive = true;
    private boolean hasReceivedQuitRequest = false;

    public Server() {
        players = new Socket[2];

        try {
            int port = 15425;

            ServerSocket sSocket = new ServerSocket(port);

            System.out.println("Waiting for the first player...");
            players[0] = sSocket.accept();
            System.out.println("Player 1 is connected.");

            System.out.println("Waiting for player 2...");
            players[1] = sSocket.accept();
            System.out.println("Player 2 is connected.");

            player_1 = new Player(this, players[0], players[1]);
            player_2 = new Player(this, players[1], players[0]);

            launch();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void main(String args[]) {
        Server server = new Server();
    }

    @Override
    public void run() {
        player_1.start();
        player_2.start();

        while (threadIsAlive) {
            if (hasReceivedQuitRequest) {
                player_1.setThreadIsAlive(false);
                player_2.setThreadIsAlive(false);

                try {
                    players[0].close();
                    players[1].close();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }

                threadIsAlive = false;
            }
        }
    }

    private void launch() {
        start();
    }

    void setReceivedQuitRequest(boolean b) {
        hasReceivedQuitRequest = true;
    }
}
