/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P2P;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joselimaico
 */
public class Ordenador {
 
    
   
  
   
   
  public TreeMap<Integer,String> sacarPalabras(String dir) throws IOException{
      BufferedReader in = null;
      TreeMap <Integer,String> arbol= new TreeMap<>();
        int i=1;
        File f = new File(dir);
     in = new BufferedReader(new FileReader(f));
    String line = in.readLine();
    StringTokenizer tokens = new StringTokenizer(line," ");
    while(tokens.hasMoreTokens()){
        
        arbol.put(i,tokens.nextToken());
    i++;
    }
    
    return arbol;
    }
    
    
     
     public static void imprimirMap(TreeMap <Integer,String> nuevoTree){
          for (Entry<Integer, String> entry : nuevoTree.entrySet()) {
             Integer key = entry.getKey();
             String value = entry.getValue();
              System.out.println(key+":"+value);
         }
    }
    
}