/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static PeerToPeer.main.cards;
import static PeerToPeer.main.cards;


/**
 *
 * @author kevin
 */
public class Client implements Runnable {
    
    DatagramSocket ds;
    DatagramPacket dp;
    InetAddress broadcastAddr;    
    HashMap<String,String> map;    
    BufferedReader in;
    byte [] buffer;
    int port;
    String usrName; 
    String Line=null;
    Palabras cards;
            
    public Client(InetAddress broadcastAddr,int port,String name,HashMap<String,String> map) throws SocketException, FileNotFoundException{
        this.usrName=name;
        this.broadcastAddr=broadcastAddr;
        this.port=port;
        this.map=map;
        this.ds=new DatagramSocket();        
        cards=new Palabras();
    }
    @Override
    public void run() {       
        buffer = new byte[256];            
        while(true){
            try {                                                        
                in = new BufferedReader(new InputStreamReader(System.in));                
                Line = in.readLine();
                if(Line.compareTo("--listar")==0){
                    map.clear();                    
                }                                                   
                buffer = Line.getBytes();               
                dp = new DatagramPacket(buffer,buffer.length,broadcastAddr,port);     
                ds.send(dp);
                if(Line.compareTo("--listar")==0){
                    Thread.sleep(1000);                    
                    for (Map.Entry<String, String> entry : map.entrySet()) {                        
                        System.out.println("Usuario Connectado: "+entry.getKey()+"\t"+entry.getValue());
                    }                   
                }
                if (Line.compareTo("jugar")==0) {
                    int index=0;
                    String sending=null;
                    byte []buffer1=new byte[256];
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        index++;
                        System.out.println("Usuario Connectado: "+entry.getKey()+"\t"+entry.getValue());
                        System.err.println("nombre a quien enviamos "+map.size());
//                        for (int i = index; i <= map.size()*10; i=i+map.size()) {
//                            sending=entry.getKey()+"@"+(i)+"@"+h.get(i)+"@"+index;
//                            buffer1=sending.getBytes();
//                            dp=new DatagramPacket(buffer1, buffer1.length,broadcastAddr,port);
//                            ds.send(dp);
//                        }
                        
                    }
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
