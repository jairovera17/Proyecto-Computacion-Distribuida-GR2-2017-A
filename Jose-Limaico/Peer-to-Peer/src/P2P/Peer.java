/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P2P;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author joselimaico
 */
public class Peer {

    static String nombre;
    static InetAddress ipBcast;
    static String iface;
    static int portEscucha;
    static int portEnvio;
    static ArrayList<Nodo> lista;
    
    public static TreeMap map;
    static TreeMap<Integer, String> c_utiles, c_repetidas;
    static String path = "src/P2P/Lipsum.txt";

    public static void main(String[] args) throws UnknownHostException, IOException {

    
        c_utiles = new TreeMap<>();
        c_repetidas = new TreeMap<>();
        lista = new ArrayList<>();
        nombre = "peque";
        System.out.println(nombre);
        portEscucha = Integer.parseInt("5000");
        portEnvio = Integer.parseInt("5000");
        Ordenador o = new Ordenador();

        map = o.sacarPalabras(path);
        o.imprimirMap(map);
        
        empezarPeer();
        // o.sacarPalabras();

        
       // randPalabra(10);
        //getRepetidas(cartas);
    }

    static void empezarPeer() throws SocketException {
        ipBcast = getBcastIP();
        System.out.println("ipBroadCast\t" + ipBcast.getHostAddress());
        if (ipBcast == null) {
            System.out.println("No se pudo obtener la ipBroadCast de la red");
            System.exit(0);
        }

        ExecutorService ex = Executors.newCachedThreadPool();

        ex.submit(new SalidaPeer(ipBcast, portEnvio, nombre, lista));
        ex.submit(new IngresoPeer(ipBcast, portEscucha, nombre, lista));

    }

    private static InetAddress getBcastIP() throws SocketException {

        Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
        InetAddress add = null;
        while (en.hasMoreElements()) {

            NetworkInterface ni = en.nextElement();
            if (!ni.getDisplayName().equals("lo")) {
                System.out.println(" Display Name = " + ni.getDisplayName());

                List<InterfaceAddress> list = ni.getInterfaceAddresses();
                Iterator<InterfaceAddress> it = list.iterator();

                while (it.hasNext()) {

                    InterfaceAddress ia = it.next();
                    System.out.println(" Broadcast = " + ia.getBroadcast());
                    //ia.getBroadcast();
                    add = ia.getBroadcast();

                }
            }
        }
        return add;
    }

    private static void randPalabra(int rango) {

        ArrayList<Integer> keys = new ArrayList(map.keySet());

        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int numero = (int) (rnd.nextDouble() * rango + 1);
            System.out.println(numero);
            
            if (!c_utiles.containsKey(numero)) {
                c_utiles.put(numero, (String) map.get(keys.get(numero-1)));
            } else {
                c_repetidas.put(numero, (String) map.get(keys.get(numero-1)));
            }
        }

        for (Entry<Integer, String> object : c_utiles.entrySet()) {
            int key = object.getKey();
            String value = object.getValue();

            System.out.println(key + "=" + value);

        }
        System.out.println("repetidas");
        for (Entry<Integer, String> object : c_repetidas.entrySet()) {
            int key = object.getKey();
            String value = object.getValue();

            System.out.println(key + "=" + value);

        }
        

    }

}
