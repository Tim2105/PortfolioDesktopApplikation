package main;

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

public class App extends Application {
	
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
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
		
		boolean connected = false;
		
		try {connected = DBInterface.getInstance() != null;}
		catch(Exception e) {}
		
		if(connected) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
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
