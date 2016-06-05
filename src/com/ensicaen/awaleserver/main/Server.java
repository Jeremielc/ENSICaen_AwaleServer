package com.ensicaen.awaleserver.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final Socket[] players;

    public Server() {
        players = new Socket[2];
        
        try {
            Server server = new Server();
            int port = 15425;

            ServerSocket sSocket = new ServerSocket(port);
            players[0] = sSocket.accept();
            
            
            
            players[1] = sSocket.accept();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void main(String args[]) {
        Server server = new Server();
    }
}
