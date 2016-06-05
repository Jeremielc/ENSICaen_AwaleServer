/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ensicaen.awaleserver.main;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 *
 * @author jeremie
 */
public class Player extends Thread {

    private final Socket socket, otherSocket;
    private boolean alive;
    private boolean mustUpdateData;
    private String receivedData, SendData;
    
    public Player() {
        socket = s;
        alive = false;
        mustUpdateData = false;
    }

    @Override
    public void run() {
        while (alive) {
            try {
                if (socket.getInputStream().available() > 0) {
                    //read from other player
                }
                
                if (mustUpdateData) {
                    //write to other player
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }
    
    private void readData() {
        
    }
    
    private void writeData() {
        
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setMustUpdateData(boolean mustUpdateData) {
        this.mustUpdateData = mustUpdateData;
    }
}
