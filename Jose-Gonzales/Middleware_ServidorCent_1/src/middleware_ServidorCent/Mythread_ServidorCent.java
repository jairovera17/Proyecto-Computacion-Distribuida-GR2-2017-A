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
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.ipBroadcast;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.nombre;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.puertoServidor;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.ips;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.cartasRecibidas;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.cartasSobrantes;
import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.valor;

public class Mythread_ServidorCent implements Runnable {



    DatagramSocket socket;

    
    String mensajeRecibido;
    int puerto;

    byte[] mensaje = new byte[256];
    
    
    public Mythread_ServidorCent(){
  
     
        this.puerto=puertoServidor;

       
        try {
            socket = new DatagramSocket(puerto);
        } catch (SocketException ex) {
            Logger.getLogger(Mythread_ServidorCent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @Override
    public void run() {
      while(true){
            
            try {
               
                DatagramPacket packete = new DatagramPacket(mensaje, mensaje.length);
                socket.receive(packete);
                this.mensajeRecibido = new String(packete.getData(), 0, packete.getLength());
                
                String direccionIP=packete.getAddress().getHostAddress();
                
                //Cuando se recibe un --listar el programa reenvia un report con 
                //el nombre del nodo
                
                if(this.mensajeRecibido.equals("--listar")){
            
            try {
                byte [] mensaje = new byte[256];
                
               String reporte="report@"+nombre;
                mensaje = reporte.getBytes();
               
               DatagramPacket packet = new DatagramPacket(mensaje,mensaje.length,ipBroadcast,puerto);     
                socket.send(packet);
              
            } catch (IOException ex) {
                Logger.getLogger(Mythread_ClienteCent.class.getName()).log(Level.SEVERE, null, ex);
            }
      
       
        }
        StringTokenizer tokens = new StringTokenizer(this.mensajeRecibido,"@");
        String comparar=tokens.nextToken();
            //Cuando se recibe global
            if(comparar.compareTo("global")==0){
                
                Set<Map.Entry<String, String>> freq = ips.entrySet();
 
		Iterator<Map.Entry<String, String>> it = freq.iterator();
 
		// Mostramos el resultado en la pantalla
                
		while ( it.hasNext() ) {
			
                    Map.Entry<String, String> item = it.next();
			//System.out.println ( item.getKey() + ": " + item.getValue() );
                          if(item.getValue().compareTo(direccionIP)==0){
                         System.out.println(item.getKey()+" desde "+direccionIP+" les dice a todos:");
              
                break;
                
                }
              
		}
          System.out.println(tokens.nextToken());
                
            }
            
            //Cuando recibimos la palabra jugar
            if(comparar.compareTo("jugar")==0){
                //Imprimir quien comenzo el juego
                Set<Map.Entry<String, String>> freq = ips.entrySet(); 
		Iterator<Map.Entry<String, String>> it = freq.iterator();
		while ( it.hasNext() ) {
			Map.Entry<String, String> item = it.next();
			//System.out.println ( item.getKey() + ": " + item.getValue() );
                          if(item.getValue().compareTo(direccionIP)==0){
                         System.out.println(item.getKey()+" desde "+direccionIP+" manda las siguientes palabras:");
              break;
		}}
 
          
        //Se reciben todas las palabras y solo se guardan la que le 
        // corresponden a este nodo por medio de la variable valor 
        //clasificamos
          for(int i=0;i<ips.size()*10;i++){
                    byte [] mensaje1 = new byte[256];
                  DatagramPacket packete1 = new DatagramPacket(mensaje1, mensaje1.length);
            socket.receive(packete1);
            this.mensajeRecibido = new String(packete1.getData(), 0, packete1.getLength());
            System.err.println(this.mensajeRecibido);
            if(this.mensajeRecibido.compareTo("jugar")!=0){
            StringTokenizer tokens1 = new StringTokenizer(this.mensajeRecibido,"@");
            String nombreRecibido=tokens1.nextToken();
            int clavePalabra=Integer.parseInt(tokens1.nextToken());
            String nombrePalabra= tokens1.nextToken();
            
              System.err.println(nombreRecibido+" nombre original " +nombre+" "+clavePalabra+" "+nombrePalabra);
              
            if(nombreRecibido.compareTo(nombre)==0){    
            cartasRecibidas.put(clavePalabra,nombrePalabra);
            valor=Integer.parseInt(tokens1.nextToken());}
          }}
          
          
          for (Map.Entry entry : cartasRecibidas.entrySet()) {
                   
            System.out.println("Clave : " + entry.getKey()
                    + " Palabra : " + entry.getValue());
        }
        ordenar();//Separa las palabras propias de este nodo de las que no lo son
        Thread.sleep(1000);
        intercambiar();//Envia a todos los nodos las palabras que no pertencen a este nodo
            
            }
              //Cuando llega nombre comapara si es el nombre del nodo lo guarda caso contrario 
              //lo ignora
            if(comparar.compareTo(nombre)==0){
                Set<Map.Entry<String, String>> freq = ips.entrySet();
 
		Iterator<Map.Entry<String, String>> it = freq.iterator();
 
		// Mostramos el resultado en la pantalla
		while ( it.hasNext() ) {
			Map.Entry<String, String> item = it.next();
			//System.out.println ( item.getKey() + ": " + item.getValue() );
                          if(item.getValue().compareTo(direccionIP)==0){
                         System.out.println(item.getKey()+" desde "+direccionIP+" te dice a ti:");
              break;
		}
              
		}
                  System.out.println(tokens.nextToken());
            }
            //Cuando llega la palabra intercambio
            //Guarda las palabras que le pertenecen a este nodo 
            //Las demas las ignora
            
            
      if(comparar.compareTo("intercambio")==0){
        
              
		
        int claveNueva=Integer.parseInt(tokens.nextToken());
        String nuevaPalabra=tokens.nextToken();
         if(((valor-1)*10)<claveNueva&&(valor)*10>=claveNueva){
           cartasRecibidas.put(claveNueva,nuevaPalabra);
              System.out.println("Palabras mias finales");  
        for (Map.Entry entry : cartasRecibidas.entrySet()) {
               
            System.out.println("Clave : " + entry.getKey()
                    + " Palabra : " + entry.getValue());
        }  
                
            }
              
            }
            
       
           
        if(comparar.compareTo("report")==0){
            //String nodo =tokens.nextToken();
            
            ips.put(tokens.nextToken(), direccionIP);
                
            
            //    return null;
            }
                  
                
                
                
            } catch (IOException ex) {
                Logger.getLogger(Mythread_ServidorCent.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
              Logger.getLogger(Mythread_ServidorCent.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
       
    }

    public void ordenar() {
        
        int []borrar= new int[20];
        int i=0;
        for (Map.Entry entry : cartasRecibidas.entrySet()) {
                   
            if(((valor-1)*10)<Integer.parseInt(entry.getKey().toString())&&(valor)*10>=Integer.parseInt(entry.getKey().toString())){
                
                
                
            }else{
                
                cartasSobrantes.put(Integer.parseInt(entry.getKey().toString()),entry.getValue().toString());
                borrar[i]=Integer.parseInt(entry.getKey().toString());
                //cartasRecibidas.remove(entry.getKey());
            }
            i++;
        }
        for(int j =0;j<borrar.length;j++){
            cartasRecibidas.remove(borrar[j]);
        }
        
        
          System.out.println("Palabras mias");  
        for (Map.Entry entry : cartasRecibidas.entrySet()) {
               
            System.out.println("Clave : " + entry.getKey()
                    + " Palabra : " + entry.getValue());
        }
        
                      System.out.println("Palabras para intercambiar");  
        for (Map.Entry entry : cartasSobrantes.entrySet()) {
   
            System.out.println("Clave : " + entry.getKey()
                    + " Palabra : " + entry.getValue());
        }
        
        
        
    }

    public void intercambiar() throws IOException {
        
        for (Map.Entry entry : cartasSobrantes.entrySet()) {
        byte [] mensaje = new byte[256];
                
               String reporte="intercambio@"+entry.getKey().toString()+"@"+entry.getValue().toString();
               System.out.println(reporte); 
               mensaje = reporte.getBytes();
               
               DatagramPacket packet = new DatagramPacket(mensaje,mensaje.length,ipBroadcast,puerto);     
                socket.send(packet);
     
        }
    
        
        
        
        
        
    }
    
    
}
