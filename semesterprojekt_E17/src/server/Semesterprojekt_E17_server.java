/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jungn
 */
public class Semesterprojekt_E17_server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
	try {
	    ServerControllerImpl serverController = new ServerControllerImpl();
	} catch (RemoteException ex) {
	    Logger.getLogger(Semesterprojekt_E17_server.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
}
