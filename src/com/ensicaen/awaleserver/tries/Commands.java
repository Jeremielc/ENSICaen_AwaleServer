package com.ensicaen.awaleserver.tries;

import java.io.*;

class Commands extends Thread {

    private final BufferedReader br;
    private String command;

    public Commands() {
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        try {
            while ((command = br.readLine()) != null) {
                if (command.equalsIgnoreCase("quit")) {
                    System.exit(0);
                } else {
                    System.out.println("Unsupported command");
                    System.out.println("Enter \"quit\" in order to close the server program.");
                }
                
                System.out.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
