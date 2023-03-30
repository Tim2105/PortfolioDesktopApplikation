package main;

import java.util.Optional;

import controller.EditController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.DBConnectionData;
import model.DBInterface;

public class Main extends Application {
	
	private static Application applicationInstance = null;
	
	public static Application getApplication() {
		return applicationInstance;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		applicationInstance = this;
		
		// Datenbankverbindung aufbauen
		boolean connected = false, closed = false;
		
		while(!connected && !closed) {
			try {
				if(DBInterface.getInstance() != null)
					connected = true;
			}
			catch(Exception e) {
				Alert dialog;
				
				DBConnectionData savedData = DBConnectionData.loadFromResource();
				
				if(savedData.getURL() == null || savedData.getUser() == null || savedData.getPassword() == null) {
					dialog = new Alert(AlertType.INFORMATION,
							"Bevor wir loslegen können, müssen Sie die URL und Autorisationsdaten der Datenbank eingeben.",
							ButtonType.CLOSE, ButtonType.NEXT);
					
					dialog.setTitle("Einrichtung");
				} else {
					dialog = new Alert(AlertType.ERROR,
						"Es konnte keine Datenbankverbindung aufgebaut werden!\nÜberprüfen Sie die URL und Autorisationsdaten.",
						ButtonType.CLOSE, ButtonType.NEXT);
				}
				
				Optional<ButtonType> result = dialog.showAndWait();
				
				if(result.isPresent() && result.get().equals(ButtonType.NEXT)) {
					try {
			    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DBConnectionView.fxml"));
						Parent root = loader.load();
						
						EditController<DBConnectionData> controller = loader.getController();
						controller.setEntity(null);
						
						Scene scene = new Scene(root);
						
						Stage stage = new Stage();
						stage.setScene(scene);
						
						String stageTitle = "Neue Verbindung";
						
						stage.setTitle(stageTitle);
						stage.showAndWait();
			    	} catch(Exception ex) {
						Alert alert = new Alert(AlertType.ERROR,
								"Ein unerwarteter Fehler ist aufgetreten:\n" + ex.getMessage(),
								ButtonType.OK);
						alert.show();
			    	}
				} else {
					closed = true;
				}
			}
		}
		
		if(!closed) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProjectView.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				
				primaryStage.setScene(scene);
				primaryStage.setTitle("Projektverwaltung");
				primaryStage.show();
			} catch(Exception e) {
				e.printStackTrace();
				Alert alert = new Alert(AlertType.ERROR,
						"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
						ButtonType.OK);
				alert.show();
			}
		}
	}
	
}
