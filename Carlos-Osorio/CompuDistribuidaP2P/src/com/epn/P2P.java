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
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

public class P2P {

    static HashMap< String, String> hash= new HashMap<String, String>();
    static String nombre="carlos";
    static HashMap< Integer, String> hashinicial;
    static InetAddress ipBroadcast;
    static String interfaz="eth0";
    static HashMap< Integer, String> hashrecibido= new HashMap<Integer,String>();
    static int puertoser=5000;
    static int puertocli=5000;
    static HashMap< Integer, String> hashaenviar= new HashMap<Integer,String>(); 
    static int num;

    public static void main(String[] args) throws IOException { 
        
        InetAddress address = InetAddress.getByName( "192.168.43.255" );
        
        IniciarBroadcast broad=new IniciarBroadcast();
        
        Crearhash o =new Crearhash();
        ipBroadcast= address;
        
//        System.out.println("ver lista");

//System.out.println(ipBroadcast);
        
        if(ipBroadcast == null){
            System.out.println("No se ha podido conectar...");
            System.exit(0);
        }
        
        
        Thread servidor = new Thread(new Runnable() {
                @Override
                public void run() {

                    Servidor servidor=new Servidor();
                    while(true){
                    
                        servidor.run();
                    }
                }
            });

            Thread cliente = new Thread(new Runnable() {
                @Override
                public void run() {

                    Cliente cliente=new Cliente();
                    while(true){
                    
                        cliente.run();
                    }
                }
            });        
        servidor.start();
        cliente.start();
          
    }
   
}
