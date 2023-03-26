package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
    		
    		DBInterface.reloadConnection(url, user, password);
    		this.saveOverrides(url, user, password);
    		this.close();
    	} catch(IllegalStateException e) {
    		Alert alert = new Alert(AlertType.ERROR,
					"Es konnte keine Datenbankverbindung aufgebaut werden!\nÜberprüfen Sie die URL und Autorisationsdaten.",
					ButtonType.OK);
			alert.show();
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler beim Speichern der neuen Autorisationsdaten ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
    }
    
    private void saveOverrides(String url, String user, String password) throws URISyntaxException, IOException {
    	Path overridesPath = Paths.get(this.getClass().getResource("/META-INF/overrides.out").toURI());
    	
    	ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(overridesPath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
    	os.writeObject(url);
    	os.writeObject(user);
    	os.writeObject(password);
    	
    	os.close();
    }
    
    private void loadOverrides() {
    	String url = null, user = null, password = null;
    	
    	try {
			Path overridesPath = Paths.get(this.getClass().getResource("/META-INF/overrides.out").toURI());
			ObjectInputStream in = new ObjectInputStream(Files.newInputStream(overridesPath, StandardOpenOption.READ));
			
			url = (String)in.readObject();
			user = (String)in.readObject();
			password = (String)in.readObject();
			
			in.close();
		} catch(IOException | URISyntaxException | ClassNotFoundException e) {}
    	
    	this.urlTextField.setText(this.urlToHostname(url));
    	this.userTextField.setText(user);
    	this.passwordTextField.setText(password);
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
    			"/C:/Users/timpl/Desktop/Informatikstudium/Sommersemester 2023/Projekt/Datenbank/DATABASE.FDB";
    }

	@Override
	protected void update() {
		if(this.entity != null) {
			this.urlTextField.setText(this.urlToHostname(this.entity.getURL()));
			this.userTextField.setText(this.entity.getUser());
			this.passwordTextField.setText(this.entity.getPassword());
		} else {
			this.loadOverrides();
		}
	}
}
