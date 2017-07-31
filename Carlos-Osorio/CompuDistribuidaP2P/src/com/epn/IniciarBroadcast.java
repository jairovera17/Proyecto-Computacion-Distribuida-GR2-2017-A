/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn;


import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.epn.P2P.interfaz;

/**
 *
 * @author CARLOS OSORIO
 */
public class IniciarBroadcast {
    
    public static InetAddress setBroadcastip(){
        try {
            
            NetworkInterface network = NetworkInterface.getByName(interfaz);
            for(InterfaceAddress temp : network.getInterfaceAddresses()){
                InetAddress add = temp.getBroadcast();
                if(add ==null)
                    continue;
                else{
                   
                    return add;
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(P2P.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
