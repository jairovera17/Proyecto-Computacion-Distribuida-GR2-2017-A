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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.epn.P2P.hashinicial;

public class Crearhash {
   
    public Crearhash() throws IOException {
                hashinicial=new HashMap<Integer,String>();
                ArchivoLipsum leer = new ArchivoLipsum();
                String archivo=leer.leerarchivo();
		String [] arreglo = archivo.split("\\s");
               Set<Map.Entry<Integer, String>> numero = hashinicial.entrySet();
		Iterator<Map.Entry< Integer,String>> frecuencia = numero.iterator();
		while ( frecuencia.hasNext() ) {
			Map.Entry< Integer,String> item = frecuencia.next();
			System.out.println ( "Llave: "+item.getKey() + " Palabra: " + item.getValue() );
		}
            }
}
