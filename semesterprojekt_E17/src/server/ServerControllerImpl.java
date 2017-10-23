/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import interfaces.IServerController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author jungn
 */
public class ServerControllerImpl extends UnicastRemoteObject implements IServerController {

    public ServerControllerImpl() throws RemoteException{
	
    }
     
    @Override
    public void test() {
	
    }
}
