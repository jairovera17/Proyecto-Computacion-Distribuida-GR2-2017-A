/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;
import static libreria.Aplicacion.maxRange;
/**
 *
 * @author andres
 */

public class NodoPrincipal 
{
    public static String nombre = "Andres";
    public static InetAddress BroadcastIP;
    public static ArrayList <Host> nodos = new ArrayList<>();
    public static TreeMap<Integer,String> EntireWords = new TreeMap<>();
    public static TreeMap<Integer,String> MyWords = new TreeMap<>();
    public static TreeMap<Integer,String> NoNeededWords = new TreeMap<>();
    public static TreeMap<Integer,String> PossibleNewWords = new TreeMap<>();
    public static ArrayList<String> Words = new ArrayList<>();
    

    public static void main (String args[]) throws IOException, InterruptedException
    {

            BroadcastIP=myBroadCast();
            FillTable(EntireWords);
            DealingWords(EntireWords, Words);
            DisplayList(Words);
            NoNeededWords=DefinePhrase(Words, MyWords);            
            new ThreadC(nombre, BroadcastIP, nodos, PossibleNewWords, MyWords, NoNeededWords, EntireWords).start();
            new ThreadS(nodos, BroadcastIP, MyWords, NoNeededWords, PossibleNewWords, EntireWords).start();
    }
    
    public static InetAddress myBroadCast()
    {
        try
        {
            NetworkInterface network = NetworkInterface.getByName("wlp2s0");
            for(InterfaceAddress interfaz:network.getInterfaceAddresses())
            {
                InetAddress broadcast=interfaz.getBroadcast();
                if(broadcast==null)
                {
                    continue;
                }else
                {
                    return broadcast;
                }
            }
            
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
     public static void FillTable(TreeMap <Integer,String> Tabla)
   {
       int i=1;
       try
       {
           BufferedReader bfr = new BufferedReader(new FileReader("Lipsum.txt"));
           String input;
           while((input=bfr.readLine())!=null)
           {
               StringTokenizer punto = new StringTokenizer(input,".");
               while(punto.hasMoreTokens())
               {
                   String input1 = punto.nextToken();
                   StringTokenizer coma = new StringTokenizer(input1,",");
                   while(coma.hasMoreTokens())
                   {
                       String input2 = coma.nextToken();
                       StringTokenizer espacio = new StringTokenizer(input2," ");
                       while(espacio.hasMoreTokens())
                       {
                           String input3 = espacio.nextToken();
                           if (!Tabla.containsValue(input3))
                           {
                           Tabla.put(i, input3);
                           i++;
                           }
                       }
                   }
               }
           }
           
       }catch(Exception e)
       {
           System.out.println("Something's went wrong");
           e.printStackTrace();
       }
   }
     
   public static void DealingWords (TreeMap <Integer,String> tabla,ArrayList<String>lista)
   {
       int i=0;
       int max=0;
       int min=0;
       for (Map.Entry<Integer,String> entry: tabla.entrySet())
       {
               max=entry.getKey();
       }
       System.out.println("Aux:"+max);
       int range = max - min + 1;
       while (i<10)
       {
           Random rn = new Random();
           int n =  rn.nextInt(range) + min;
           try
           {
               lista.add(n+"/"+tabla.get(n));
               i++;
           }
           catch(Exception e)
           {
               System.out.println("reintentando...");
               i--;
           }
       }
   }
   
   public static void DisplayList (ArrayList<String> lista)
   {
       for (int i=0;i<lista.size();i++)
       {
           System.out.println("Palabra "+i+": "+lista.get(i));
       }
   }
   
   public static TreeMap <Integer,String> DefinePhrase(ArrayList<String> lista, TreeMap<Integer,String> myWords)
   {
       
       ArrayList <Integer> indices = new ArrayList<>();
       ArrayList<String> ran = new ArrayList<>();
       TreeMap <Integer,String> rechazados = new TreeMap<>();
       for (int i=0;i<lista.size();i++)
       {
           String elemento=lista.get(i);
           StringTokenizer tokens = new StringTokenizer(elemento,"/");
           while(tokens.hasMoreTokens())
           {
               int indice = Integer.parseInt(tokens.nextToken());
               String valor =tokens.nextToken();
               myWords.put(indice,valor);
           }
       }
       
       for(Map.Entry<Integer,String> entry: myWords.entrySet())
       {
           indices.add(entry.getKey());
       }
       
       
       for (int i=0;i<indices.size();i++)
       {
           String numero = "";
           int cont=1;
           int aux=indices.get(i);
           numero = numero+aux;
           int range = aux/10;
           range=range*10;
           for (int j=0;j<indices.size();j++)
           {
               int temp = indices.get(j);
               if(temp>=range && temp<range+10)
               {
                   if(temp!=aux)
                   {
                       numero= numero +"|"+temp;
                       cont++;
                   }
               }
           }
           if(!ran.contains(cont))
           {
               ran.add(cont+"/"+range+"/"+numero);
           }
       }
       
       int maximum=0;
       maxRange=0;
       String maxliners="";
       
       for (int i=0;i<ran.size();i++)
       {
           String temp=ran.get(i);
           StringTokenizer tokens = new StringTokenizer(temp,"/");
           while(tokens.hasMoreTokens())
           {
               int num = Integer.parseInt(tokens.nextToken());
               int rango = Integer.parseInt(tokens.nextToken());
               String liners=tokens.nextToken();
               
               if(num>maximum)
               {
                   maximum=num;
                   maxRange=rango;
                   maxliners=liners;
               }
           }
       }
       
       //System.out.println("el rango que mas elementos tiene es "+maxRange+" con un total de "+maximum+" elementos y los numeros dentro de ese rango son "+maxliners);
       
       ArrayList<Integer> noEscogido = new ArrayList<>();
       for (Map.Entry<Integer,String> temp: myWords.entrySet())
       {
           int numero=temp.getKey();
           String valor = temp.getValue();
           if(!(numero>=maxRange && numero<maxRange+10))
           {
               rechazados.put(numero, valor);
               noEscogido.add(numero);
           }
       }
       
       for (int i=0;i<noEscogido.size();i++)
       {
           myWords.remove(noEscogido.get(i));
       }
       
       System.out.println("\n Lista de elementos que me sirven");
       for (Map.Entry<Integer,String> myW: myWords.entrySet())
       {
           int k = myW.getKey();
           String v = myW.getValue();
           
           System.out.println(k+"-"+v);
       }
       
       System.out.println("\n Lista de elementos que me estoy deshaciendo");
       for (Map.Entry<Integer,String> rechazo: rechazados.entrySet())
       {
           int k = rechazo.getKey();
           String v = rechazo.getValue();
           
           System.out.println(k+"-"+v);
       }
       return rechazados;
   }
}
