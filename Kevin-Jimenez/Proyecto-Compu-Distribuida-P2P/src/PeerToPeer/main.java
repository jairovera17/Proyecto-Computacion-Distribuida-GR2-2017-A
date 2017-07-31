/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;


/**
 *
 * @author kevin
 */
public class main {
    static String usrName;
    static int port;
    static HashMap< Integer, String> cards;
    public static void main(String[] args) throws SocketException, FileNotFoundException, IOException {
        usrName="kevin";
        System.out.println(usrName);
        port=5000;
        new Encendido(usrName, port).turnUP();
        
    }
    
}
