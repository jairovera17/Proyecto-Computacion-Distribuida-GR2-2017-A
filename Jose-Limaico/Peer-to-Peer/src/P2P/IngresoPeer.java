/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P2P;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static P2P.Peer.c_utiles;
import static P2P.Peer.c_repetidas;
import java.util.Map;


/**
 *
 * @author joselimaico
 */
public class IngresoPeer implements Runnable {

    private InetAddress ipBcast;
    private DatagramSocket socket;
    private String nombre;
    private int port;
    private ArrayList<Nodo> listaNodo;
    
    String palabra = null;
    String pal[] = null;
    String recibido;
    int valor;

    public IngresoPeer(InetAddress ipBroadcast, int port, String nombre, ArrayList<Nodo> listaNodo) {
        this.ipBcast = ipBroadcast;
        this.nombre = nombre;
        this.port = port;
        this.listaNodo = listaNodo;

        try {
            socket = new DatagramSocket(port);
        } catch (SocketException ex) {
            Logger.getLogger(IngresoPeer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        escuchar();
    }

    private void escuchar() {
        byte buf[] = new byte[256];
        while (true) {

            try {

                DatagramPacket pack = new DatagramPacket(buf, buf.length);
                socket.receive(pack);

                recibido = new String(pack.getData(), 0, pack.getLength());
                

                String parse = parsear(recibido, pack.getAddress().getHostAddress());
                if (parse != null) {
                    System.out.println(parse);
                }

            } catch (IOException ex) {
                Logger.getLogger(IngresoPeer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private String parsear(String mensaje, String hostAddress) throws IOException {

        if (mensaje.equals("--listar")) {
            enviarReport();
            
            return null;
        }
        StringTokenizer tokens = new StringTokenizer(mensaje, "@");
        String index = tokens.nextToken();
        if (index.equals(nombre) || index.equals("global")) {
            if (index.equals("global")) {
                for (Nodo nodo : listaNodo) {
                    if (nodo.getDireccion().equals(hostAddress)) {
                        System.out.println(nodo.getId() + " desde " + hostAddress + " dice a todos:");

                        break;

                    }

                }
            }

            if (index.equals(nombre)) {
                for (Nodo nodo : listaNodo) {
                    if (nodo.getDireccion().equals(hostAddress)) {
                        System.out.println(nodo.getId() + " desde " + hostAddress + " dice:");

                        break;
                    }

                }
            }
            return tokens.nextToken();
        }

        if (index.equals("report")) {
            String nodo = tokens.nextToken();

            Nodo nuevoNodo = new Nodo();
            nuevoNodo.setId(nodo);
            nuevoNodo.setDireccion(hostAddress);
            boolean node = true;
            for (Nodo aux : listaNodo) {
                if (aux.getDireccion().equals(nuevoNodo.getDireccion())) {
                    node = false;
                    break;
                }
            }
            if (node) {
                listaNodo.add(nuevoNodo);
                Collections.sort(listaNodo,
                        new Comparator<Nodo>() {
                    @Override
                    public int compare(Nodo s1, Nodo s2) {
                        return s1.getId().compareToIgnoreCase(s2.getId());
                    }
                }
                );
            }

            //    System.out.println("Usuario Connectado: "+nodo+"\t"+hostAddress);
            return null;
        }

        if (index.equals("jugar")) {

            for (Nodo nodo : listaNodo) {

                if (nodo.getDireccion().equals(hostAddress)) {
                    System.out.println(nodo.getId() + " desde " + hostAddress + " manda las siguientes palabras:");
                    break;
                }

            }

            for (int i = 0; i < listaNodo.size() * 10; i++) {
                byte[] mensajejugar = new byte[256];
                DatagramPacket packete1 = new DatagramPacket(mensajejugar, mensajejugar.length);
                socket.receive(packete1);
                this.recibido = new String(packete1.getData(), 0, packete1.getLength());

                if (!this.recibido.equals("jugar")) {
                    StringTokenizer tokensrecibidos = new StringTokenizer(this.recibido, "@");
                    String nombreRecibido = tokensrecibidos.nextToken();
                    int clavePalabra = Integer.parseInt(tokensrecibidos.nextToken());
                    String nombrePalabra = tokensrecibidos.nextToken();

                    if (nombreRecibido.equals(nombre)) {
                        c_utiles.put(clavePalabra, nombrePalabra);
                        valor = Integer.parseInt(tokensrecibidos.nextToken());
                    }
                }

            }
            for (Map.Entry entry : c_utiles.entrySet()) {

                System.out.println("Clave : " + entry.getKey()
                        + " Palabra : " + entry.getValue());
            }
            ordenarPalabras();
            
                //Thread.sleep(1000);
            
            
            intercambiarPalabras();
        }

        if (index.equals("intercambio")) {

            int claveNueva = Integer.parseInt(tokens.nextToken());
            String nuevaPalabra = tokens.nextToken();
            if (((valor - 1) * 10) < claveNueva && (valor) * 10 >= claveNueva) {
                c_utiles.put(claveNueva, nuevaPalabra);
                System.out.println("Palabras mias finales");
                for (Map.Entry entry : c_utiles.entrySet()) {

                    System.out.println("Clave : " + entry.getKey()
                            + " Palabra : " + entry.getValue());
                }

            }

        }

        return null;

    }

    private void enviarReport() {

        try {
            byte[] buf = new byte[256];

            String reporte = "report@" + nombre;
            buf = reporte.getBytes();

            DatagramPacket pack = new DatagramPacket(buf, buf.length, ipBcast, port);
            socket.send(pack);

        } catch (IOException ex) {
            Logger.getLogger(SalidaPeer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void ordenarPalabras() {

        int[] borrar = new int[20];
        int i = 0;
        for (Map.Entry entry : c_utiles.entrySet()) {

            if (((valor - 1) * 10) < Integer.parseInt(entry.getKey().toString()) && (valor) * 10 >= Integer.parseInt(entry.getKey().toString())) {

            } else {
                
                c_repetidas.put(Integer.parseInt(entry.getKey().toString()), entry.getValue().toString());
                borrar[i] = Integer.parseInt(entry.getKey().toString());
                
            }
            i++;
        }
        for (int j = 0; j < borrar.length; j++) {
            c_utiles.remove(borrar[j]);
        }

        System.out.println("Palabras mias");
        for (Map.Entry entry : c_utiles.entrySet()) {

            System.out.println("Clave : " + entry.getKey()
                    + " Palabra : " + entry.getValue());
        }

        System.out.println("Palabras para intercambiar");
        for (Map.Entry entry : c_repetidas.entrySet()) {

            System.out.println("Clave : " + entry.getKey()
                    + " Palabra : " + entry.getValue());
        }

    }

    public void intercambiarPalabras() throws IOException {

        for (Map.Entry entry : c_repetidas.entrySet()) {
            byte[] mensaje = new byte[256];

            String reporte = "intercambio@" + entry.getKey().toString() + "@" + entry.getValue().toString();
            System.out.println(reporte);
            mensaje = reporte.getBytes();

            DatagramPacket packet = new DatagramPacket(mensaje, mensaje.length, ipBcast, port);
            socket.send(packet);

        }

    }

}
