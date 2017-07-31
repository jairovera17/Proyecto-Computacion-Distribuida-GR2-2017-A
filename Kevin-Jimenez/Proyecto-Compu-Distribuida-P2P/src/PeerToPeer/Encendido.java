/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author kevin
 */
public class Encendido {
    InetAddress broadcastAddr;
    HashMap<String,String> map = new HashMap<>();
    String nombreInterfaz="wlp7s0";
    String usrNamr;
    int port;

    public Encendido(String usrNamr, int port) {
        this.usrNamr = usrNamr;
        this.port = port;
    }        
    
    public void turnUP() throws SocketException, FileNotFoundException, IOException{
        NetworkInterface netInt = NetworkInterface.getByName(nombreInterfaz);
        for (InterfaceAddress intAddr : netInt.getInterfaceAddresses()) {
            InetAddress addr = intAddr.getBroadcast();
            if (addr==null) {                
            }else{
                broadcastAddr = addr;
            }
        }
        Palabras listaPalabras = new Palabras();
        listaPalabras.frase();
        System.out.println("Direccion de BroadCast\t"+broadcastAddr.getHostAddress());
        ExecutorService ex = Executors.newCachedThreadPool();     
        ex.submit(new Client(broadcastAddr, port, usrNamr,map));
        ex.submit(new Server(broadcastAddr, port,usrNamr,map));
        
    }
    
}
