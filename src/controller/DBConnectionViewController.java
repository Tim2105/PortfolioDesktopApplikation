package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.DBConnectionData;
import model.DBInterface;

public class DBConnectionViewController extends EditController<DBConnectionData> {

    @FXML
    private TextField urlTextField;

    @FXML
    private TextField userTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    void handleCancelButtonAction() {
    	this.close();
    }

    @FXML
    void handleConfirmButtonAction() {
    	try {
    		String url = this.hostnameToUrl(this.urlTextField.getText());
    		String user = this.userTextField.getText();
    		String password = this.passwordTextField.getText();
    		
    		DBConnectionData newData = new DBConnectionData(url, user, password);
    		
    		DBInterface.reloadConnection(newData);
    		this.entity = newData;
    		this.saveOverrides(newData);
    		this.close();
    	} catch(IllegalStateException e) {
    		Alert alert = new Alert(AlertType.ERROR,
					"Es konnte keine Datenbankverbindung aufgebaut werden!\nÜberprüfen Sie die URL und Autorisierungsdaten.",
					ButtonType.OK);
			alert.show();
    	} catch(IOException e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler beim Speichern der neuen Autorisierungsdaten ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
    }
    
    private void saveOverrides(DBConnectionData data) throws IOException {
    	DBConnectionData.saveToResource(data);
    }
    
    private void loadOverrides() {
    	DBConnectionData data = DBConnectionData.loadFromResource();
    	
    	this.setEntity(data);
    }
    
    private String urlToHostname(String url) {
    	if(url == null)
    		return "";
    	
    	String result = url;
    	
    	result = result.substring(19);
    	result = result.substring(0, result.indexOf('/'));
    	
    	return result;
    }
    
    private String hostnameToUrl(String hostname) {
    	if(hostname == null)
    		return "";
    	
    	return "jdbc:firebirdsql://" +
    			hostname +
    			"/projectDatabase";
    }

	@Override
	protected void update() {
		if(this.entity != null) {
			if(this.entity.getURL() != null)
				this.urlTextField.setText(this.urlToHostname(this.entity.getURL()));
			else
				this.urlTextField.setText("localhost:3050");
			
			this.userTextField.setText(this.entity.getUser());
			this.passwordTextField.setText(this.entity.getPassword());
		} else {
			this.loadOverrides();
		}
	}
}
