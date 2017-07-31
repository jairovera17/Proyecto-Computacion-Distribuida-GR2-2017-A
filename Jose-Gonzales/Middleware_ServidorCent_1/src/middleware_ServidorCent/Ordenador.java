/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package middleware_ServidorCent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static middleware_ServidorCent.Aplicacion_Middleware_ServidorCent.h;
/**
 *
 * @author jose
 */
public class Ordenador {
   
    public Ordenador() {
                h=new HashMap<Integer,String>();
                LeeFichero l = new LeeFichero();
                String archivo=l.leer();
                System.out.println(archivo);
		String [] cadena = archivo.split("\\s");
                
		for ( int i = 1; i < cadena.length; i++ ) {
			//System.out.println ( cadena[i]);
				h.put (  i,cadena[i]);
			
		}
                
		Set<Map.Entry<Integer, String>> freq = h.entrySet();
 
		Iterator<Map.Entry< Integer,String>> it = freq.iterator();
 
		// Mostramos el resultado en la pantalla
		while ( it.hasNext() ) {
			Map.Entry< Integer,String> item = it.next();
			System.out.println ( item.getKey() + ": " + item.getValue() );
		}
        
    
    }
    
    
    public void repartir(int n){
        
        
        
        
    }
    
    
    
}
