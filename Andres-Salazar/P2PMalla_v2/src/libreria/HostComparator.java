/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libreria;

import java.util.Comparator;

/**
 *
 * @author andres
 */
public class HostComparator implements Comparator<Host>{
    @Override
    public int compare(Host h1, Host h2) {
        return h1.getIdentificador().compareToIgnoreCase(h2.getIdentificador());
    }
    
}
