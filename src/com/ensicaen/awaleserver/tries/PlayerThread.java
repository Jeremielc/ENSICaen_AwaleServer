package com.ensicaen.awaleserver.tries;

import java.net.*;
import java.io.*;

class PlayerThread implements Runnable {

    private final Thread thread;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Server server;
    private int clientNum = 0;

    PlayerThread(Socket socket, Server server) {
        this.server = server;
        this.socket = socket;

        try {
            out = new PrintWriter(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            clientNum = server.addClient(out);
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        String message = "";
        System.out.println("Un nouveau client s'est connecte, no " + clientNum);
        try {
            char charCur[] = new char[1];

            while (in.read(charCur, 0, 1) != -1) {
                if (charCur[0] != '\u0000' && charCur[0] != '\n' && charCur[0] != '\r') { //\u0000 char de terminaison nulle
                    message += charCur[0];
                } else if (!message.equalsIgnoreCase("")) {
                    if (charCur[0] == '\u0000') {
                        server.sendAll(message, "" + charCur[0]);
                    } else {
                        server.sendAll(message, "");
                    }

                    message = "";
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        } finally {
            try {
                System.out.println("Le client no " + clientNum + " s'est deconnecte");
                server.delClient(clientNum);
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
}
