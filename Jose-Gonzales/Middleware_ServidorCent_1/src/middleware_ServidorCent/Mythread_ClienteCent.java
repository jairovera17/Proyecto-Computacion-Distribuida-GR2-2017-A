package middleware_ServidorCent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.h;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.nombre;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.ipBroadcast;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.ips;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.puertoCliente;

public class Mythread_ClienteCent implements Runnable {    


    DatagramSocket socket;
    int puerto;
  
    
    public Mythread_ClienteCent(){
       
      
        this.puerto=puertoCliente;
       
        try {
            this.socket=new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(Mythread_ClienteCent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run() {
     
        byte [] mensaje = new byte[256];
            
        while(true){
            try {
             
                
            
                String temp = null;
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                
                temp = in.readLine();
                if(temp.equals("--listar")){
                    ips.clear();
                   
                }
                   
                
                
                mensaje = temp.getBytes();
               
               DatagramPacket packet = new DatagramPacket(mensaje,mensaje.length,ipBroadcast,puerto);     
                socket.send(packet);
                 if(temp.equals("--listar")){
                   
                   // Iteradores para recorrer todas las ips                
                Set<Map.Entry<String, String>> freq = ips.entrySet();
 
		Iterator<Map.Entry<String, String>> it = freq.iterator();

                                           
                         // Se muestra todas las ips que estan conectadas
		while ( it.hasNext() ) {
                 
                    Map.Entry<String, String> item = it.next();
		        System.out.println("Usuario Connectado: "+item.getKey()+"\t"+item.getValue());
                }
                    
                                
                
              
                }
                 
                 if(temp.compareTo("jugar")==0){
                  
                int valorNodo=0;// Es el valor que le va a corresponder a cada nodo
                String enviando=null;
                byte [] mensaje2 = new byte[256];//Mensaje a enviar
		
                Set<Map.Entry<String, String>> freq = ips.entrySet();
 
		Iterator<Map.Entry<String, String>> it = freq.iterator();

                        while ( it.hasNext() ) {
                            valorNodo++;
                            Map.Entry<String, String> item = it.next();
                            System.out.println("Usuario Connectado: "+item.getKey()+"\t"+item.getValue());
                            String name=item.getKey(); // Nombre al nodo a cual enviamos
                            System.err.println("nombre a quien enviamos "+ips.size());
                //Realiza random para repartir las palabras
                for(int i=valorNodo;i<=ips.size()*10;i=i+ips.size()){                  
                   // Se envia NombredelNodoAEnviar@ClaveDePalabra@Palabra@ValorCorrespondienteADichoNodo
                  enviando=name+"@"+(i)+"@"+h.get(i)+"@"+valorNodo;
              
                  mensaje2 = enviando.getBytes();
                  DatagramPacket packet1 = new DatagramPacket(mensaje2,mensaje2.length,ipBroadcast,puerto);     
                socket.send(packet1);//Se envia a cada uno de los nodos
  
                
                 }
                }
                 }
                 
                 
                 
                 
              
            } catch (IOException ex) {
                Logger.getLogger(Mythread_ClienteCent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
}
