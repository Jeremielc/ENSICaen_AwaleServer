package com.ensicaen.awaleserver.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

    private final Socket[] players;
    private Player player_1, player_2;
    private boolean threadIsAlive = true;

    public Server() {
        players = new Socket[2];
        
        try {
            int port = 15425;

            ServerSocket sSocket = new ServerSocket(port);
            players[0] = sSocket.accept();
            players[1] = sSocket.accept();
            
            player_1 = new Player(players[0], players[1]);
            player_2 = new Player(players[1], players[0]);
            
            start();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void main(String args[]) {
        Server server = new Server();
    }
    
    @Override
    public void run() {
        while (threadIsAlive) {
            player_1.start();
            player_2.start();
        }
    }

    public Player getPlayer_1() {
        return player_1;
    }

    public void setPlayer_1(Player player_1) {
        this.player_1 = player_1;
    }

    public Player getPlayer_2() {
        return player_2;
    }

    public void setPlayer_2(Player player_2) {
        this.player_2 = player_2;
    }

    public boolean isThreadIsAlive() {
        return threadIsAlive;
    }

    public void setAlive(boolean alive) {
        this.threadIsAlive = alive;
    }
}
