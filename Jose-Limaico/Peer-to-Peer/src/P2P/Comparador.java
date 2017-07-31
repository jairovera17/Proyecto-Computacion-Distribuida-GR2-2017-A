/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package P2P;

import java.util.Comparator;

/**
 *
 * @author joselimaico
 */
public class Comparador implements Comparator{

    

    @Override
    public int compare(Object o1, Object o2) {
     Nodo nodo1 = (Nodo)o1;
     Nodo nodo2 = (Nodo)o2;
     return nodo1.id.compareToIgnoreCase(nodo2.id);
        
    }

   
    
}
