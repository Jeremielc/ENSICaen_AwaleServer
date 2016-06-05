package com.ensicaen.awaleserver.tries;

import java.net.*;
import java.io.*;
import java.util.*;

//** Classe principale du serveur, gère les infos globales **
public class Server {

    private Vector tabClients = new Vector();
    private int nbClients = 0;

    public Server() {
        Server server = new Server();
        int port = 15425;

        try {
            Commands commands = new Commands();
            commands.start();
            ServerSocket sSocket = new ServerSocket(port);
            printWelcome(port);

            PlayerThread pt;
            while (nbClients < 2) {
                pt = new PlayerThread(sSocket.accept(), server);
                // un client se connecte, un nouveau thread client est lancé
                nbClients++;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void main(String args[]) {
        Server server = new Server();
    }

    static private void printWelcome(int port) {
        System.out.println("--------");
        System.out.println("Awale server : By Pierrick HUE & Jérémie LECLERC.");
        System.out.println("--------");
        System.out.println("Started on port " + port + ".");
        System.out.println("--------");
        System.out.println("Enter \"quit\" in order to close the server program.");
        System.out.println("--------");
    }

    synchronized public void sendAll(String message, String sLast) {
        PrintWriter out;
        for (int i = 0; i < tabClients.size(); i++) {
            out = (PrintWriter) tabClients.elementAt(i);
            if (out != null) {
                out.print(message + sLast);
                out.flush();
            }
        }
    }

    synchronized public void delClient(int i) {
        nbClients--; // un client en moins ! snif
        if (tabClients.elementAt(i) != null) // l'élément existe ...
        {
            tabClients.removeElementAt(i); // ... on le supprime
        }
    }

    synchronized public int addClient(PrintWriter out) {
        nbClients++; // un client en plus ! ouaaaih
        tabClients.addElement(out); // on ajoute le nouveau flux de sortie au tableau
        return tabClients.size() - 1; // on retourne le numéro du client ajouté (size-1)
    }

    synchronized public int getNbClients() {
        return nbClients; // retourne le nombre de clients connectés
    }

}
