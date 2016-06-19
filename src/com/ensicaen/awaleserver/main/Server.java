package com.ensicaen.awaleserver.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A class used to redirect informations between players.
 *
 * @author Pierrick Hue and Jérémie Leclerc
 */
public class Server extends Thread {

    private final Socket[] players;
    private Player player_1, player_2;
    private boolean threadIsAlive = true;
    private boolean hasReceivedQuitRequest = false;

    /**
     * Instanciate the server.
     */
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

    /**
     * Program entry point. Create a new server object.
     *
     * @param args A table containing the arguments passed to the program from
     * command line.
     */
    public static void main(String args[]) {
        Server server = new Server();
    }

    @Override
    /**
     * Instanciate the two players and start their own thread to listen and
     * transimt data.
     */
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

    /**
     * Launch the server thread.
     */
    private void launch() {
        start();
    }

    /**
     * Used to inform the thread that a 'quit' request has been received and
     * that the thread must stop by himself.
     *
     * @param b A boolean telling if a 'quit' request has been received or not.
     */
    void setReceivedQuitRequest(boolean b) {
        hasReceivedQuitRequest = true;
    }
}
