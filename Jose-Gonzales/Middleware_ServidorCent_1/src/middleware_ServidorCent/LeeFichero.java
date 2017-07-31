/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware_ServidorCent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 *
 * @author jose
 */
public class LeeFichero {
    
    public String leer(){
         File archivo = null;
      FileReader fr = null;
      BufferedReader br = null;
    String linea = "";
    String linea1 = ""; 
      try {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File ("Lipsum.txt");
         fr = new FileReader (archivo);
         br = new BufferedReader(fr);

         // Lectura del fichero
         while((linea=br.readLine())!=null){
             
             //System.out.println(linea);
            linea1+=linea;
         }
            //System.out.println("hola soy :"+linea1);
      }
      catch(Exception e){
         e.printStackTrace();
      }finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }
        
    return linea1;    
    
    }
}
