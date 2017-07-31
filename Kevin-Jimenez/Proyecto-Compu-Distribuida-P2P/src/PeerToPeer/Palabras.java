/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import static PeerToPeer.main.cards;

/**
 *
 * @author kevin
 */
public class Palabras {
        
   
    File texto ;
    BufferedReader in;
    StringTokenizer tokens;
    String Line;
    int lend;
    int llave;

    public Palabras() throws FileNotFoundException {
        cards = new HashMap<>();
        
        texto = new File("src/frases.txt");
        in = new BufferedReader(new FileReader(texto));
    }
    
    
    public void frase() throws IOException {                
        Line = in.readLine();
        tokens = new StringTokenizer(Line," ");        
        int i = 1;
        while (tokens.hasMoreTokens()) {
            String palabra = tokens.nextToken().replaceAll("[,|.|:|;]", "");
            cards.put(i, palabra);            
            i++;
        }      
        for (Map.Entry<Integer, String> entry : cards.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            System.out.println(key+" "+value);
            
        }
 
        
    }
    
    
    
    
    
    
    
}
