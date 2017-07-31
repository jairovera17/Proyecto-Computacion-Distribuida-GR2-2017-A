/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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
public class ThreadS extends Thread{
    
    
    private DatagramSocket S;
    private final int MyPort = 5000;
    public InetAddress MyBroadcastIP;
    ArrayList<Host>HostList;
    
    TreeMap<Integer,String> EntireWords;
    TreeMap<Integer,String> MyWords;
    TreeMap<Integer,String> NoNeededWords;
    TreeMap<Integer,String> PossibleNewWords;
    
    protected boolean listening = true;
    
    
    public ThreadS(ArrayList<Host> lista, InetAddress direccionB, 
            TreeMap<Integer,String> accept,TreeMap<Integer,String> ignored, TreeMap<Integer,String> possible, 
            TreeMap<Integer,String> totalpalabras) throws SocketException 
    {
        MyBroadcastIP = direccionB;
        HostList=lista;
        MyWords=accept;
        NoNeededWords=ignored;
        PossibleNewWords = possible;
        EntireWords=totalpalabras;
        S = new DatagramSocket();
    }
        
    private void SendDatagram()
    {
        while(listening)
        {
            try
            {                         
                byte [] buf = new byte [256];
                BufferedReader StdIn = new BufferedReader(new InputStreamReader(System.in));
                String message=StdIn.readLine();
                
                buf = message.getBytes();

                DatagramPacket packet = new DatagramPacket(buf, buf.length, MyBroadcastIP, MyPort);
                S.send(packet);
                
                if(message.equals("--enviar"))
                    SendUnnecesaryWords(NoNeededWords);
                
                if(message.equals("--palabras nuevas"))
                    DisplayTable(PossibleNewWords);
                
                if(message.equals("--armar"))
                    BuildPhrase(PossibleNewWords, MyWords,NoNeededWords);
                
                if(message.equals("--nuevo mazo"))
                    PossibleNewWords=RandomWords(EntireWords);
                
                if(message.equals("jugar"))
                    DistributeWords(HostList, EntireWords);
                
                if(message.equals("--listar"))
                {
                    HostList.clear();
                    Thread.sleep(1000);
                    for(Host host:HostList)
                    {
                        System.out.println("Usuario conectado ==> "+host.getIdentificador()+" - "+host.getDireccion());
                    }
                    System.out.println("Total de hosts conectados a la red: "+HostList.size());
                }
                
                
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
                listening = false;
            }
        }
    }  
    
    public void SendUnnecesaryWords(TreeMap<Integer,String> InterchangableWords)
    {
        try
        {
            byte buf []= new byte[256];
            for(Map.Entry<Integer, String> temp:InterchangableWords.entrySet())
            {
                String pck="palabra@"+temp.getKey()+"/"+temp.getValue();
                buf=pck.getBytes();
                DatagramPacket packet = new DatagramPacket(buf,buf.length, MyBroadcastIP, MyPort);
                S.send(packet);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void DisplayTable(TreeMap <Integer,String> tabla)
   {
       for(Map.Entry<Integer,String> aux:tabla.entrySet())
       {
           int indice = aux.getKey();
           String palabra = aux.getValue();
           
           System.out.println(indice+".- "+palabra);
       }
   }
    
    public static void BuildPhrase(TreeMap<Integer,String> nuevatabla, TreeMap<Integer,String> mitabla, TreeMap<Integer,String> tablaenvio)
   {
       ArrayList<Integer> potenciales = new ArrayList<>();
       for (Map.Entry<Integer,String> temp:nuevatabla.entrySet())
       {
           int k=temp.getKey();
           potenciales.add(k);
       }       
       
       ArrayList<String> noEscogido = new ArrayList<>();
       for (Map.Entry<Integer,String> temp: nuevatabla.entrySet())
       {
           int numero=temp.getKey();
           String valor = temp.getValue();
           if(mitabla.size()!=10)
           {
               if((numero>=maxRange && numero<maxRange+10))
               {
                    mitabla.put(numero, valor);
               }
               else
               {
                    noEscogido.add(numero+"-"+valor);
               }
           }else
           {
               System.out.println("Mi lista está completa");
               break;
           }
       }
      
       tablaenvio.clear();
       System.out.println("\n Lista de elementos actual");
       for (Map.Entry<Integer,String> myW: mitabla.entrySet())
       {
           int k = myW.getKey();
           String v = myW.getValue();
           
           System.out.println(k+"-"+v);
       }
       
       System.out.println("\n Lista de elementos que no me sirven");
       for (int i=0;i<noEscogido.size();i++)
       {
           StringTokenizer tokens = new StringTokenizer(noEscogido.get(i),"-");
           int k=Integer.parseInt(tokens.nextToken());
           String v = tokens.nextToken();
           String elemento = k+".- "+v;
           System.out.println(elemento);
           if(!tablaenvio.containsKey(i))
           {
               tablaenvio.put(k,v);
           }
       }    
   }
    
    public static TreeMap<Integer,String> RandomWords (TreeMap <Integer,String> tabla)
   {
       TreeMap<Integer,String> aux = new TreeMap<>();
       int i=0;
       int max=0;
       int min=0;
       for (Map.Entry<Integer,String> entry: tabla.entrySet())
       {
               max=entry.getKey();
       }
       int range = max - min + 1;
       while (i<10)
       {
           
           Random rn = new Random();
           int n =  rn.nextInt(range) + min;
           try
           {
               aux.put(n, tabla.get(n));
               i++;
           }
           catch(Exception e)
           {
               System.out.println("reintentando...");
               i--;
           }
       }
       System.out.println("Nuevo mazo de palabras!");
       return aux;
   }
    
    public void DistributeWords(ArrayList<Host> listahosts, TreeMap<Integer,String> listaCompleta)
    {
        int cont=0;
        String phrase;
        try
        {
            byte [] buf = new byte[256];
            for(int i=0;i<listahosts.size();i++)
            {
                cont++;
                String name=listahosts.get(i).getIdentificador();
                System.out.println("Enviando al host "+name);
                for (int j=cont;j<listahosts.size()*10;j=j+listahosts.size())
                {
                    phrase=name+"@"+j+"@"+listaCompleta.get(j)+"@"+cont;
                    buf=phrase.getBytes();
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, MyBroadcastIP, MyPort);
                    S.send(packet);
                }
            }   
        }
        catch(Exception e)
        {
            System.out.println("something gone wrong");
            e.printStackTrace();
        }
    }
     
    public void run()
    {
        SendDatagram();        
    }
}
