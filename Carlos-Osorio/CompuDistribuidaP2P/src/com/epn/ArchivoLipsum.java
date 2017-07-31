/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class ArchivoLipsum {
    
    String line="";
    String nextline="";
    
    public String leerarchivo() throws FileNotFoundException, IOException{
        File datos=new File("C:\\Users\\CARLOS OSORIO\\Desktop\\Lipsum.txt");
        FileReader fr=new FileReader(datos);
        BufferedReader br=new BufferedReader(fr);
        try {

        while((line=br.readLine())!=null){
             
            nextline+=line;
         } }
      catch(Exception e){
         e.printStackTrace();
      }finally{}
        
        return nextline;
    }
}

