package middleware_ServidorCent;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Aplicacion_Middleware_ServidorCent {

    
    static String nombre;
    static InetAddress ipBroadcast;
    static String interfaz;
    static int puertoServidor;
    static int puertoCliente;
    static HashMap< String, String> ips;
    static HashMap< Integer, String> h;
    static HashMap< Integer, String> cartasRecibidas;
     static HashMap< Integer, String> cartasSobrantes; 
    static int valor;

    public static void main(String[] args) { 
        ips  = new HashMap<String, String>();
        cartasRecibidas= new HashMap<Integer,String>();
        cartasSobrantes= new HashMap<Integer,String>();
        interfaz="wlp3s0";
        nombre="jose";
        puertoServidor=5000;
        puertoCliente=5000;
        Ordenador o =new Ordenador();
        ipBroadcast= setBroadcastip();
        System.out.println("ipBroadCast\t"+ipBroadcast.getHostAddress());
        if(ipBroadcast == null){
            System.out.println("No se pudo obtener la ipBroadCast de la red");
            System.exit(0);
        }
        
        
        Thread servidor = new Thread(new Mythread_ServidorCent());
        Thread cliente = new Thread(new Mythread_ClienteCent());
        
        servidor.start();
        cliente.start();
                   
        
          
    }
    
    

    private static InetAddress setBroadcastip(){
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
            Logger.getLogger(Aplicacion_Middleware_ServidorCent.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
