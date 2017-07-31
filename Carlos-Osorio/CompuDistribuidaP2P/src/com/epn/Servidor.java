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
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.epn.P2P.ipBroadcast;
import static com.epn.P2P.nombre;
import static com.epn.P2P.hashrecibido;
import static com.epn.P2P.hashaenviar;
import static com.epn.P2P.puertoser;
import static com.epn.P2P.hash;
import static com.epn.P2P.num;

public class Servidor implements Runnable {

    
    String separar;
    String msjreci;
    

    byte[] msj = new byte[256];
    int puertoservidor;
    DatagramSocket socketser;
    public Servidor(){
          this.puertoservidor=puertoser;
      
        try {
            socketser = new DatagramSocket(puertoservidor);
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    @Override
    public void run() {
      while(true){
            
            try {
               
                DatagramPacket packeterecibido = new DatagramPacket(msj, msj.length);
                socketser.receive(packeterecibido);
                this.msjreci = new String(packeterecibido.getData(), 0, packeterecibido.getLength());
                String direccionIP=packeterecibido.getAddress().getHostAddress();
                if(this.msjreci.equals("--listar")){
            
                try {
                    byte [] mensaje = new byte[256];
                
                    String reporte="report@"+nombre;
                    mensaje = reporte.getBytes();
               
                    DatagramPacket packet = new DatagramPacket(mensaje,mensaje.length,ipBroadcast,puertoservidor);     
                    socketser.send(packet);
              
                    } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                    }
      
                }
                
                StringTokenizer tokens = new StringTokenizer(this.msjreci,"@");
                String comparar=tokens.nextToken();

                    if(comparar.compareTo("global")==0){
                
                    Set<Map.Entry<String, String>> freq = hash.entrySet();
 
                    Iterator<Map.Entry<String, String>> it = freq.iterator();
 
                    while ( it.hasNext() ) {
			
                        Map.Entry<String, String> item = it.next();
                          if(item.getValue().compareTo(direccionIP)==0){
                         System.out.println("El usuario: "+item.getKey()+" con la ip "+direccionIP+" dice:");
              
                        break;
                
                        }
                    }
                    
                    System.out.println(tokens.nextToken());
                
                    }
              
                    if(comparar.compareTo("jugar")==0){
                
                         Set<Map.Entry<String, String>> frecuenciaus = hash.entrySet(); 
                         Iterator<Map.Entry<String, String>> frecuenc = frecuenciaus.iterator();
                        while ( frecuenc.hasNext() ) {
			Map.Entry<String, String> caracter1 = frecuenc.next();
                          if(caracter1.getValue().compareTo(direccionIP)==0){
                         System.out.println("El usuario: "+caracter1.getKey()+" con la dirección ip "+direccionIP+" envió un arreglo:");
                        break;
                            }
                            }
 
                    for(int i=0;i<hash.size()*10;i++){
                      byte [] mensaje1 = new byte[256];
                        DatagramPacket packete1 = new DatagramPacket(mensaje1, mensaje1.length);
                        socketser.receive(packete1);
                        this.msjreci = new String(packete1.getData(), 0, packete1.getLength());
                        System.err.println(this.msjreci);
                        if(this.msjreci.compareTo("jugar")!=0){
                        StringTokenizer tokens1 = new StringTokenizer(this.msjreci,"@");
                        String nombreRecibido=tokens1.nextToken();
                        int clavePalabra=Integer.parseInt(tokens1.nextToken());
                        String nombrePalabra= tokens1.nextToken();
            
              
                         if(nombreRecibido.compareTo(nombre)==0){    
                            hashrecibido.put(clavePalabra,nombrePalabra);
                            num=Integer.parseInt(tokens1.nextToken());}
                        }}
          
                        for (Map.Entry entry : hashrecibido.entrySet()) {
                   
                        System.out.println("Clave : " + entry.getKey()
                        + " Palabra : " + entry.getValue());
                        }
                        
                        ordenarhashutil();
                        Thread.sleep(1000);
                        intercambiarhashinnecesario();
            
                        }
                            if(comparar.compareTo(nombre)==0){
                            Set<Map.Entry<String, String>> frecuencia2 = hash.entrySet();
                            Iterator<Map.Entry<String, String>> frecuencia3 = frecuencia2.iterator();
                            while ( frecuencia3.hasNext() ) {
                                    Map.Entry<String, String> caractera = frecuencia3.next();
                                      if(caractera.getValue().compareTo(direccionIP)==0){
                                     System.out.println("El usuario"+caractera.getKey()+" con la ip "+direccionIP+" dice:");
                          break;
                            }

                            }
                              System.out.println(tokens.nextToken());
                        }
            
                        if(comparar.compareTo("intercambio")==0){
                            Set<Map.Entry<String, String>> nuevafrecuencia = hash.entrySet();
                            Iterator<Map.Entry<String, String>> nuevoiterador = nuevafrecuencia.iterator();
                            while ( nuevoiterador.hasNext() ) {
                            Map.Entry<String, String> item = nuevoiterador.next();
                            if(hash.containsValue(direccionIP)){
                            break;
                
                            }
              
                            }
                            int claveNueva=Integer.parseInt(tokens.nextToken());
                            String nuevaPalabra=tokens.nextToken();
                            if(((num-1)*10)<claveNueva&&(num)*10>=claveNueva){
                            hashrecibido.put(claveNueva,nuevaPalabra);
                            System.out.println("Arreglo Completo");  
                            for (Map.Entry entry : hashrecibido.entrySet()) {
               
                            System.out.println("Clave : " + entry.getKey()
                            + " Palabra : " + entry.getValue());
                            }  
                
                            }
              
                    }
                    if(comparar.compareTo("report")==0){
          
                    hash.put(tokens.nextToken(), direccionIP);

                    }
                
                    } catch (IOException ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                      Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                  }
                }
       
    }

    
    
    
    
    
    public void ordenarhashutil() {
        
        int []borrar= new int[20];
        int i=0;
        for (Map.Entry entry : hashrecibido.entrySet()) {
                   
            if(((num-1)*10)<Integer.parseInt(entry.getKey().toString())&&(num)*10>=Integer.parseInt(entry.getKey().toString())){                
            }else{    
                hashaenviar.put(Integer.parseInt(entry.getKey().toString()),entry.getValue().toString());
                borrar[i]=Integer.parseInt(entry.getKey().toString());
            }
            i++;
        }
        for(int j =0;j<borrar.length;j++){
            hashrecibido.remove(borrar[j]);
        }
        
          System.out.println("Mi arreglo");  
        for (Map.Entry entry : hashrecibido.entrySet()) {
               
            System.out.println("Clave : " + entry.getKey()
                    + " Palabra : " + entry.getValue());
        }
        
                      System.out.println("Arreglo innecesario");  
        for (Map.Entry entry : hashaenviar.entrySet()) {
   
            System.out.println("Clave : " + entry.getKey()
                    + " Palabra : " + entry.getValue());
        }
         
    }

    public void intercambiarhashinnecesario() throws IOException {
        
        for (Map.Entry entry : hashaenviar.entrySet()) {
        byte [] mensaje = new byte[256];
                
               String reporte="intercambio@"+entry.getKey().toString()+"@"+entry.getValue().toString();
               System.out.println(reporte); 
               mensaje = reporte.getBytes();
               
               DatagramPacket packet = new DatagramPacket(mensaje,mensaje.length,ipBroadcast,puertoservidor);     
                socketser.send(packet);
     
        }
         
    }
    
    
}
