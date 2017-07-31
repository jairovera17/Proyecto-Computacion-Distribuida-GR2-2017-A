/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn;

import java.util.HashMap;
import java.util.Map;
import static com.epn.P2P.hashrecibido;

/**
 *
 * @author CARLOS OSORIO
 */
public class Ordenarhash {
    static HashMap< Integer, String> hashrecibido;
    int valor;
    static HashMap< Integer, String> hashaenviar;
    
    public void ordenar() {
        
        int []borrar= new int[20];
        int i=0;
        for (Map.Entry entry : hashrecibido.entrySet()) {
                   
            if(((valor-1)*10)<Integer.parseInt(entry.getKey().toString())&&(valor)*10>=Integer.parseInt(entry.getKey().toString())){
                
                
                
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
    
}
