package controller;

import entity.Developer;
import entity.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBConnectionData;
import model.DBInterface;

public class MainViewController extends Controller {
	
	@FXML
    private MenuItem newProjectMenuItem;
    
    @FXML
    private MenuItem newDeveloperMenuItem;
    
    @FXML
    private MenuItem newConnectionMenuItem;
    
    @FXML
    private MenuItem refreshMenuItem;
    
    @FXML
    private MenuItem aboutMenuItem;

	@FXML
	private Tab projectTab;
	
	@FXML
	private Tab developerTab;
	
	private ProjectViewController projectViewController = null;
	
	private DeveloperViewController developerViewController = null;
	
	@FXML
	void initialize() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProjectView.fxml"));
			Parent root = loader.load();
			this.projectViewController = loader.getController();
			
			this.projectTab.setContent(root);
			
		} catch(Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler beim Laden des Projekttabs ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
		}
		
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DeveloperView.fxml"));
			Parent root = loader.load();
			this.developerViewController = loader.getController();
			
			this.developerTab.setContent(root);
			
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter beim Laden des Entwicklertabs Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
	}
	
	private void refreshLists() {
		if(this.projectViewController != null)
			this.projectViewController.refresh();
		
		if(this.developerViewController != null)
			this.developerViewController.refresh();
	}
	
	private void openProjectEditView(Project project) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProjectEditView.fxml"));
			Parent root = loader.load();
			EditController<Project> controller = loader.getController();
			controller.setEntity(project);
			
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Projekt bearbeiten - ";
			
			if(project != null)
				stageTitle += project.getTitle();
			else
				stageTitle += "Neues Projekt";
			
			stage.setTitle(stageTitle);
			stage.showAndWait();
			
			this.refreshLists();
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
    }
    
    private void openDeveloperEditView(Developer developer) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DeveloperEditView.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Entwickler bearbeiten - ";
			
			if(developer != null)
				stageTitle += developer.getFirstname() + " " + developer.getLastname();
			else
				stageTitle += "Neuer Entwickler";
			
			stage.setTitle(stageTitle);
			stage.show();
			
			this.refreshLists();
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
    }
    
    @FXML
    private void handleNewProjectMenuItemAction() {
    	this.openProjectEditView(null);
    }
    
    @FXML
    private void handleNewDeveloperMenuItemAction() {
    	this.openDeveloperEditView(null);
    }
    
    @FXML
    private void handleNewConnectionMenuItemAction() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DBConnectionView.fxml"));
			Parent root = loader.load();
			
			EditController<DBConnectionData> controller = loader.getController();
			controller.setEntity(DBInterface.getInstance().getConnectionData());
			
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Neue Verbindung";
			
			stage.setTitle(stageTitle);
			stage.showAndWait();
			
			this.refreshLists();
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
    }
    
    @FXML
    private void handleRefreshMenuItemAction() {
    	DBInterface.getInstance().refresh();
    	this.refreshLists();
    }
    
    @FXML
    private void handleAboutMenuItemAction() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AboutView.fxml"));
			Parent root = loader.load();
			
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Über";
			
			stage.setTitle(stageTitle);
			stage.show();
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
    }
	
}
