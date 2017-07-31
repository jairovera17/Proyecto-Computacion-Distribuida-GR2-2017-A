/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P2P;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static P2P.Peer.map;


/**
 *
 * @author joselimaico
 */
public class SalidaPeer implements Runnable {

    String nombre;
    InetAddress ipBcast;
    DatagramSocket socket;
    int port;
    ArrayList<Nodo> listaNodo;
   
    
    public SalidaPeer(InetAddress ipBroadcast,int port,String name,ArrayList<Nodo> lista){
        this.nombre=name;
        this.ipBcast=ipBroadcast;
        this.port=port;
        this.listaNodo=lista;
   
        try {
            this.socket=new DatagramSocket();
        } catch (SocketException ex) {
            Logger.getLogger(SalidaPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run() {
       
            enviar();
           
    }
    
    
    
    
    
    
    private void enviar(){
           
        byte [] buf = new byte[256];
       
          
        while(true){
            try {
             
                
            
                String aux = null;
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                
                aux = in.readLine();
                if(aux.equals("--listar")){
                    listaNodo.clear(); 
                   
                }
                   
                
                
                buf = aux.getBytes();
               
               DatagramPacket packet = new DatagramPacket(buf,buf.length,ipBcast,port);     
                socket.send(packet);
                 if(aux.equals("--listar")){
                    Thread.sleep(1000);
                    for(Nodo nodo: listaNodo){
                        System.out.println("Usuario Connectado: "+nodo.getId()+"\t"+nodo.getDireccion());
                    }
                     
                    
                }
                 
              
                       if(aux.compareTo("jugar")==0){
                       int x=0;
                String enviando=null;
                byte [] mensaje2 = new byte[256];
                for(Nodo nodo: listaNodo){
                 x++;
                 System.out.println("Usuario Connectado: "+nodo.getId()+"\t"+nodo.getDireccion());
                 String name = nodo.getId();
                for(int i=x;i<=listaNodo.size()*10;i=i+listaNodo.size()){                  

                  enviando=name+"@"+(i)+"@"+map.get(i)+"@"+x;
                    
                  mensaje2 = enviando.getBytes();
                  DatagramPacket packet1 = new DatagramPacket(mensaje2,mensaje2.length,ipBcast,port);     
                socket.send(packet1);
  
                
                 }
                }
                       
                       
                       }
            } catch (IOException ex) {
                Logger.getLogger(SalidaPeer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(SalidaPeer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
     
    
}
