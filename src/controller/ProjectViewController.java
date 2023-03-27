package controller;

import java.util.Optional;

import entity.Developer;
import entity.Project;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBConnectionData;
import model.DBInterface;
import view.DetailedProjectListCell;
import view.EmptyListViewPlaceholder;

public class ProjectViewController extends Controller {
	
    @FXML
    private MenuItem newProjectMenuItem;
    
    @FXML
    private MenuItem newDeveloperMenuItem;
    
    @FXML
    private MenuItem newConnectionMenuItem;
    
    @FXML
    private MenuItem refreshMenuItem;
    
    @FXML
    private MenuItem guideMenuItem;
    
    @FXML
    private MenuItem aboutMenuItem;
    
    @FXML
    private Button newProjectButton;
    
    @FXML
    private Button editProjectButton;
    
    @FXML
    private Button deleteProjectButton;
    
    @FXML
    private Button showDeveloperButton;
    
    @FXML
    private ListView<Project> projectsListView;

    @FXML
    void initialize() {
    	this.projectsListView.setPlaceholder(new EmptyListViewPlaceholder(
    						"Noch keine Projekte erstellt",
    						"Projekt hinzufügen",
    						ev -> {
    							this.openProjectEditView(null);
    						}
    			));
    	
    	this.projectsListView.getSelectionModel()
    			.selectionModeProperty().set(SelectionMode.SINGLE);
    	
    	this.projectsListView.setCellFactory(val -> new DetailedProjectListCell());
    	
    	this.projectsListView.setItems(DBInterface.getInstance().getProjects());
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
			
			this.projectsListView.refresh();
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
			
			this.projectsListView.setItems(DBInterface.getInstance().getProjects());
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
    	this.projectsListView.refresh();
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
    
    @FXML
    private void handleNewProjectButtonAction() {
        this.openProjectEditView(null);
    }
    
    @FXML
    private void handleEditProjectButtonAction() {
    	Project selectedProject = this.projectsListView
    			.getSelectionModel().getSelectedItem();
    	
    	if(selectedProject != null)
    		this.openProjectEditView(selectedProject);
    	else {
    		Alert alert = new Alert(AlertType.ERROR,
					"Wählen Sie ein Projekt aus der Liste aus",
					ButtonType.OK);
			alert.showAndWait();
    	}
    }
    
    @FXML
    private void handleDeleteProjectButtonAction() {
    	Project selectedProject = this.projectsListView
				.getSelectionModel().getSelectedItem();

		if(selectedProject != null) {
			Alert warningDialog = new Alert(AlertType.WARNING,
					"Möchten Sie das Projekt wirklich löschen?\nDiese Aktion ist permanent!",
					ButtonType.NO, ButtonType.YES);
			Optional<ButtonType> result = warningDialog.showAndWait();
			
			if(result.isPresent() && result.get().equals(ButtonType.YES)) {
				try {
					EntityManager em = DBInterface.getInstance().createEntityManager();
					
					Project p = em.find(Project.class, selectedProject.id);
					
					em.getTransaction().begin();
					em.remove(p);
					em.getTransaction().commit();
					em.close();
					
					DBInterface.getInstance().getProjects().remove(selectedProject);
				} catch(Exception e) {
					Alert alert = new Alert(AlertType.ERROR,
							"Das Löschen des Projektes ist fehlgeschlagen",
							ButtonType.OK);
						alert.showAndWait();
				}
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR,
				"Wählen Sie ein Projekt aus der Liste aus",
				ButtonType.OK);
			alert.showAndWait();
		}
    }
    
    @FXML
    private void handleShowDeveloperButtonAction() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DeveloperView.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Entwickler verwalten";
			
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
