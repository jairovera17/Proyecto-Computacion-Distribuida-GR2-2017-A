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
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;

import static com.epn.P2P.ipBroadcast;
import static com.epn.P2P.puertoser;



/**
 *
 * @author CARLOS OSORIO
 */
public class HacerIntercambio {

public void intercambiar(HashMap< Integer, String> hash1, DatagramPacket socket) throws IOException {
        
        for (Map.Entry entry : hash1.entrySet()) {
        byte [] mensaje = new byte[256];
               String reporte="intercambio@"+entry.getKey().toString()+"@"+entry.getValue().toString();
               System.out.println(reporte); 
               mensaje = reporte.getBytes();
               
               DatagramPacket packet = new DatagramPacket(mensaje,mensaje.length,ipBroadcast,puertoser);     
     
        }
         
    }    
    
    
}
