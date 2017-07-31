/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn;

/**
 *
 * @author CARLOS OSORIO
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.epn.P2P.ipBroadcast;
import static com.epn.P2P.puertocli;
import static com.epn.P2P.hashinicial;
import static com.epn.P2P.hash;

public class Cliente implements Runnable {    


    DatagramSocket socketcli;
    int puertocliente;
  
    
    public Cliente(){
        this.puertocliente=puertocli;
       
        try {
            this.socketcli=new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @Override
    public void run() {
     
        byte [] bytes = new byte[256];
            
        while(true){
            try {
                String aux = null;
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                
                aux = in.readLine();
                if(aux.equals("--listar")){
                    hash.clear();
                   
                }
                 
                bytes = aux.getBytes();
               
               DatagramPacket msjenviar = new DatagramPacket(bytes,bytes.length,ipBroadcast,puertocliente);     
                socketcli.send(msjenviar);
                
                 if(aux.equals("--listar")){
                    Thread.sleep(1000);
                    
                
                Set<Map.Entry<String, String>> numero = hash.entrySet();
 
		Iterator<Map.Entry<String, String>> frecuencia = numero.iterator();
                while ( frecuencia.hasNext() ) {
                 
                    Map.Entry<String, String> caracter = frecuencia.next();
		        System.out.println("Usuario en la red: "+caracter.getKey()+"\t"+caracter.getValue());
                }
                 
                }
                 
                 if(aux.compareTo("jugar")==0){
                  
                int numeradorusuario=0;
                String enviarmsj=null;
                byte [] segundomsj = new byte[256];
		
                Set<Map.Entry<String, String>> frecuencia2 = hash.entrySet();
 
		Iterator<Map.Entry<String, String>> iterador2 = frecuencia2.iterator();

                        while ( iterador2.hasNext() ) {
                            numeradorusuario++;
                            Map.Entry<String, String> item = iterador2.next();
                            System.out.println("Usuario en la red: "+item.getKey()+"\t"+item.getValue());
                            String idllaveus=item.getKey(); 
                            System.err.println("Usuario Receptor "+hash.size());
                
                for(int i=numeradorusuario;i<=hash.size()*10;i=i+hash.size()){                  

                  enviarmsj=idllaveus+"@"+(i)+"@"+hashinicial.get(i)+"@"+numeradorusuario;
              
                  segundomsj = enviarmsj.getBytes();
                  DatagramPacket packeteenviado = new DatagramPacket(segundomsj,segundomsj.length,ipBroadcast,puertocliente);     
                  socketcli.send(packeteenviado);
                   }
                }
                 }
              
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
