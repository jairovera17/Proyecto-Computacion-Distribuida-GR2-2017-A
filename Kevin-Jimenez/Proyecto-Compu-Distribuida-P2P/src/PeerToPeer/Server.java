/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kevin
 */
public class Server implements Runnable{

    Juego game;
    InetAddress broadcastAddr;
    DatagramSocket ds;
    DatagramPacket dp;
    StringTokenizer tokens;
    String usrName,msjRecibido,addrDestino,parser,str,reporte,userName;    
    int port,valor;    
    HashMap<String , String> map;
    HashMap<Integer, String> listCards;
    HashMap<Integer, String> sobras;
    byte [] buffer;
    Palabras cartas;
    
   
    public Server(InetAddress ipBroadcast,int port,String name,HashMap<String,String> map) throws SocketException, FileNotFoundException{
        this.broadcastAddr = ipBroadcast;
        this.usrName=name;
        this.port=port;
        this.map=map;       
        ds = new DatagramSocket(port);                
        game=new Juego(this.map);
        cartas = new Palabras();
        listCards= new HashMap<>();
        sobras=new HashMap<>();
    }
    @Override
    public void run() {
        try {
            listener();
        } catch (FileNotFoundException | InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void listener() throws FileNotFoundException, InterruptedException{
        buffer = new byte[1024];
        while(true){            
            try {               
                dp = new DatagramPacket(buffer, buffer.length);
                ds.receive(dp);                
                msjRecibido = new String(dp.getData(), 0, dp.getLength());
                addrDestino=dp.getAddress().getHostAddress();
                parser=parsear(msjRecibido,addrDestino);
                if(parser!=null){
                    System.out.println(parser);   
                }                
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    private String parsear(String mensaje, String hostAddress) throws FileNotFoundException, IOException, InterruptedException{       
        if(mensaje.compareTo("--listar")==0){                        
            try {
                buffer = new byte[1024];                
                reporte="report@"+usrName;
                buffer = reporte.getBytes();               
                dp = new DatagramPacket(buffer,buffer.length,broadcastAddr,port);     
                ds.send(dp);              
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }            
        return null;
        }
        tokens = new StringTokenizer(mensaje,"@");        
        str=tokens.nextToken();
        if(str.equals(usrName)||str.equals("global")){                        
            if(str.equals("global")){
                for (Map.Entry<String, String> entry : map.entrySet()) {                                      
                    if(entry.getValue().equals(hostAddress)){
                        System.out.println(entry.getKey()+" desde "+hostAddress+" les dice a todos:");              
                        break;
                    }                
                }   
            }              
            if(str.equals(usrName)){
                for (Map.Entry<String, String> entry : map.entrySet()) {                                
                    if(entry.getValue().equals(hostAddress)){
                        System.out.println(entry.getKey()+" desde "+hostAddress+" dice:");              
                        break;
                    }
                }
            }
            String next = tokens.nextToken();        
        return next;
        }
        if (str.compareTo("jugar")==0) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getValue().equals(hostAddress)) {
                        System.out.println(entry.getKey()+" desde "+hostAddress+" manda las siguientes palabras:");
                        break;                        
                    }                    
                }
                
                //game.play(ds, mensaje, usrName,reporte);                
                //game.play(ds, mensaje, usrName);
                
        }
        if (str.compareTo("intercambio")==0) {
                //System.out.println("khe");
                for (Map.Entry<String, String> entry : map.entrySet()) {                    
                    if (map.containsValue(hostAddress)) {
                        break;
                    }
                }
                //game.sendCads(tokens.nextToken());                
        }
        if(str.equals("report")){
            userName=tokens.nextToken();
            map.put(userName, hostAddress);            
        return null;
        }                  
    return null;                        
    }
    
    
    
     
}
