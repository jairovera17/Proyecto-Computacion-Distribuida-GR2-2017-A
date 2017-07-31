/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import static libreria.Aplicacion.maxRange;

/**
 *
 * @author andres
 */

public class ThreadC extends Thread
{

    public static String MyIdentification;
    private DatagramSocket MyChannel;
    private int MyPort= 5000;
    private InetAddress MyBroadcastIP;
    private ArrayList <Host> HostList; 
    
    private TreeMap<Integer,String> MyWords;
    private TreeMap<Integer,String> EntireWords;
    private TreeMap<Integer,String> NoNeedWords;
    
    private final boolean listening = true;
    
    
    private TreeMap <Integer,String> PossibleNewWords;
    public ThreadC(String identificador, InetAddress IPBroadcast, ArrayList<Host> listado, TreeMap<Integer,String> possibles,
            TreeMap<Integer,String> mispalabras, TreeMap<Integer,String> nonecesitadas, TreeMap<Integer,String> palabrascompletas)
    {
        MyIdentification = identificador;
        MyBroadcastIP=IPBroadcast;
        HostList=listado;
        PossibleNewWords = possibles;
        MyWords=mispalabras;
        NoNeedWords=nonecesitadas;
        EntireWords=palabrascompletas;
        
        try
        {
            MyChannel = new DatagramSocket(MyPort);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    
    public void ListeningIncomingDatagrams()
    {
        byte buf[] = new byte[256];
        while(listening)
        {
            String message;
            try
            {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                MyChannel.receive(packet);
                message = new String(packet.getData(),0,packet.getLength());
                String typeofMessage = AnalyzeMessage(message, packet.getAddress().getHostAddress());
                if(typeofMessage!=null)
                    System.out.println(typeofMessage);
                try
                {
                    StringTokenizer tokens = new StringTokenizer(typeofMessage, "/");
                    int index = Integer.parseInt(tokens.nextToken());
                    String word = tokens.nextToken();
                    if(!PossibleNewWords.containsKey(index))
                    {
                        PossibleNewWords.put(index, word);
                    }
                    
                }
                catch(Exception ex)
                {
                }
            }catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
        
    public String AnalyzeMessage(String msg, String hostAddress)
    {
        String temp = msg;
        
        if(temp.equals("--listar"))
        {
            try
            {
                byte buf []= new byte[256];
                String message = "report@"+MyIdentification;
                buf = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buf,buf.length,MyBroadcastIP, MyPort);
                MyChannel.send(packet);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }
        
        if(temp.equals("--enviar"))
            return null;
        
        if(temp.equals("--palabras nuevas"))
            return null;
        
        if(temp.equals("--armar"))
            return null;
            
        if (temp.equals("--nuevo mazo"))
            return null;
                
        
        StringTokenizer arroba = new StringTokenizer(temp,"@");
        String id = arroba.nextToken();
       
        
        if (temp.equals("jugar"))
        {
            for(int i=0;i<HostList.size();i++)
            {
                if(HostList.get(i).getDireccion().equals(hostAddress))
                {
                    System.out.println("El usuario "+HostList.get(i).getIdentificador()+" con direccion "+HostList.get(i).getDireccion()+" envia la palabra");
                    break;
                }   
            }
            
            try
            {
                MyWords.clear();
                for(int i=0; i<HostList.size()*10;i++)
                {
                    byte [] buf = new byte[256];
                    DatagramPacket incomingMessage = new DatagramPacket(buf,buf.length);
                    MyChannel.receive(incomingMessage);
                    String message = new String(incomingMessage.getData(), 0, incomingMessage.getLength());
                    
                    if(!(message.equals("jugar")))
                    {
                        StringTokenizer token = new StringTokenizer(message, "@");
                        String HostId = token.nextToken();
                        int WordIndex = Integer.parseInt(token.nextToken());
                        String Word = token.nextToken();
                        System.out.println(HostId+"=> "+WordIndex+" "+Word);
                        
                        if(HostId.equals(MyIdentification))
                        {
                            MyWords.put(WordIndex, Word);
                            maxRange=Integer.parseInt(token.nextToken());
                        }
                        
                        for (Map.Entry<Integer,String> E:MyWords.entrySet())
                        {
                            System.out.println("Id > "+E.getKey()+" Palabra > "+E.getValue());
                        }
                        
                        SortTree();
                        Thread.sleep(1000);
                        Interchange();
                    }  
                }                                
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        
        if(id.equals(MyIdentification)||id.equals("global"))
        {
            if(id.equals(MyIdentification))
            {
                for (Host host:HostList)
                {
                    if(host.getDireccion().equals(hostAddress))
                    {
                        System.out.println(host.getIdentificador()+" desde "+host.getDireccion()+" dice: ");
                        break;
                    }
                }
            }
     
            if (id.equals("global"))
            {
                for (Host host:HostList)
                {
                    if(host.getDireccion().equals(hostAddress))
                    {
                        System.out.println(host.getIdentificador()+" desde "+host.getDireccion()+" dice a "
                                + "todos los host conectados: ");
                        break;
                    }
                }            
            }
            
            return arroba.nextToken();
        }
        
        if(id.equals("palabra"))
            {
                StringTokenizer slash = new StringTokenizer(arroba.nextToken(),"/");
                int index = Integer.parseInt(slash.nextToken());
                String word = slash.nextToken();
                String message = index +" - "+word;
                for (Host host:HostList)
                {
                    if(host.getDireccion().equals(hostAddress))
                    {
                        System.out.println(host.getIdentificador()+" desde "+host.getDireccion()+" mando su palabra: ");
                        if(!PossibleNewWords.containsKey(index))
                            PossibleNewWords.put(index, word);
                        break;
                    }
                }
                return message;
            }
        
        
        if(id.equals("intercambio"))
        {
            System.err.println(temp);
            int key = Integer.parseInt(arroba.nextToken());
            String value = arroba.nextToken();
            if(((maxRange -1)*10)<key&&(maxRange)*10>=key)
            {
                MyWords.put(key, value);
            }
            System.out.println("Mias finales");
            for(Map.Entry<Integer,String> aux: MyWords.entrySet())
            {
                System.out.println("indice ="+aux.getKey()+" palabra="+aux.getValue());
            }
            return null;
        }
        
        
        
        if(id.equals("report"))
        {
            
            Host newHost = new Host(arroba.nextToken(), hostAddress);
            boolean IsNew = true;
            
            for (Host host:HostList)
            {
                if((host.getDireccion().equals(newHost.getDireccion())))
                {
                    IsNew = false;
                    break;
                }
            }
            
            if(IsNew)
            {
                HostList.add(newHost);
                Collections.sort(HostList, new HostComparator());
            }
            return null;
        }
        return null;
    }   
    
    
    public void SortTree()
    {    
        NoNeedWords.clear();
        int []NotUsedId= new int[20];
        int cont=0;
        for (Map.Entry entry : MyWords.entrySet()) 
        {
            if(((maxRange-1)*10)<Integer.parseInt(entry.getKey().toString())&&(maxRange)*10>=Integer.parseInt(entry.getKey().toString()))
            {    
                continue;
            }
            else
            {
                NoNeedWords.put(Integer.parseInt(entry.getKey().toString()),entry.getValue().toString());
                NotUsedId[cont]=Integer.parseInt(entry.getKey().toString());
            }
            
            cont++;
        }
        
        for(int j =0;j<NotUsedId.length;j++)
        {
            MyWords.remove(NotUsedId[j]);
        }
        
        System.out.println("Mis palabras");  
        
        for (Map.Entry entry : MyWords.entrySet()) 
        {
               
            System.out.println("Indice: " + entry.getKey()+ " Palabra: " + entry.getValue());
        }
        
        System.out.println("Palabras que no utilizo");
        
        for (Map.Entry entry : NoNeedWords.entrySet())
        {
            System.out.println("Clave : " + entry.getKey()+ " Palabra : " + entry.getValue());
        }   
    }
    
    public void Interchange() throws IOException 
    {
        
        for (Map.Entry entry : NoNeedWords.entrySet()) 
        {
            byte [] mensaje = new byte[256];   
            String reporte="intercambio@"+entry.getKey().toString()+"@"+entry.getValue().toString();
            System.out.println(reporte); 
            mensaje = reporte.getBytes();
               
            DatagramPacket packet = new DatagramPacket(mensaje,mensaje.length,MyBroadcastIP,MyPort);     
            MyChannel.send(packet);
        }
    }
    
    public void run()
    {
        ListeningIncomingDatagrams(); 
    }
    
}
