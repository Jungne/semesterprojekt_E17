package client;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class FXMLDocumentController implements Initializable {

    private ClientController clientController;

    @FXML
    private Label label;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
	try {
	    clientController = new ClientController();
	} catch (RemoteException ex) {
	    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
	
	try {
	    System.out.println(clientController.getAllTrips().get(0).getTitle());
	} catch (RemoteException ex) {
	    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
}
