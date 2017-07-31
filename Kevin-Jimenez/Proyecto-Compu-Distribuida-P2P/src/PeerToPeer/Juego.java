/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PeerToPeer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author kevin
 */
public class Juego {
    HashMap<String, String> map;
    HashMap<Integer, String> listCards;
    HashMap<Integer, String> sobras;
    byte [] buffer;
    int [] noSriven;
    DatagramPacket dp;
    //DatagramSocket ds;
    String usrNameRecivido,word;
    StringTokenizer tokens;
    Integer key;
    int temp;
    
    public Juego(HashMap<String, String> map) {
        this.map=map;
        //this.valor=valor;
        listCards=new HashMap<>();
        
        
        
    }
    
    
    public void play(DatagramSocket ds, String msjRecive, String usrName) throws IOException, InterruptedException{
                
        for (int i = 0; i < map.size()*10; i++) {
            buffer=new byte[1024];
            dp=new DatagramPacket(buffer, buffer.length);
            ds.receive(dp);
            msjRecive=new String(dp.getData(),0,dp.getLength());            
            if(msjRecive.compareTo("jugar")!=0){
                tokens = new StringTokenizer(msjRecive,"@");
                usrNameRecivido=tokens.nextToken();
                word = tokens.nextToken();
                key = Integer.parseInt(tokens.nextToken());                
                System.err.println(usrNameRecivido+" name "+usrName+" "+key+" "+word);
                if (usrNameRecivido.equals(usrName)) {
                    listCards.put(key, word);
                    temp = Integer.parseInt(tokens.nextToken());
                }
            }
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());            
        }
        sortCards();
        Thread.sleep(1000);
        intercambioCards(ds);
        
    }
    public void sendCads(String str){ //intercambio        
        int newKey=Integer.parseInt(str);
        String neWord=str;
        if (((temp-1)*10)<newKey&&(temp)*10>=newKey) {
            listCards.put(newKey, neWord);
            System.out.println("cartas finales");
            for (Map.Entry<Integer, String> entry : listCards.entrySet()) {                
                System.out.println(entry.getKey()+" "+entry.getValue());
            }
        }
    }
    
    public void sortCards(){
        noSriven = new int[20];
        int index=0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String llave = entry.getKey();
            String value = entry.getValue();
            if (((temp-1)*10)<Integer.parseInt(llave)&&(temp)*10>=Integer.parseInt(llave)) {                
            }else{
                sobras.put(Integer.parseInt(llave), value);
                noSriven[index]=Integer.parseInt(llave);
            }
            index++;
        }
        for (int i = 0; i < noSriven.length; i++) {
            listCards.remove(noSriven[i]);            
        }
        System.out.println("Mias");
        for (Map.Entry<Integer, String> entry : listCards.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());            
        }
        System.out.println("NO sirven");
        for (Map.Entry<Integer, String> entry : sobras.entrySet()) {
            System.out.println(entry.getKey()+" "+entry.getValue());            
        }
    }
    
    public void intercambioCards(DatagramSocket ds) throws IOException{
        for (Map.Entry<Integer, String> entry : sobras.entrySet()) {
            buffer = new byte[1024];
            String reporte1="intercambio@"+entry.getKey()+"@"+entry.getValue();
            System.out.println(reporte1);
            buffer=reporte1.getBytes();
            dp=new DatagramPacket(buffer, buffer.length);
            ds.send(dp);
        }
    }

    
    
    
}
