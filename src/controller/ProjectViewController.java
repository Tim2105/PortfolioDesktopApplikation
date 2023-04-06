package controller;

import java.util.Optional;

import entity.Project;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBInterface;
import view.DetailedProjectListCell;
import view.EmptyViewPlaceholder;

public class ProjectViewController extends Controller implements Refreshable {
    
    @FXML
    private Button newProjectButton;
    
    @FXML
    private Button editProjectButton;
    
    @FXML
    private Button deleteProjectButton;
    
    @FXML
    private ListView<Project> projectsListView;

    @FXML
    void initialize() {
    	this.projectsListView.setPlaceholder(new EmptyViewPlaceholder(
    						"Noch keine Projekte erstellt",
    						"Projekt hinzufügen",
    						ev -> {
    							this.openProjectEditView(null);
    						}
    			));
    	
    	this.projectsListView.getSelectionModel()
    			.selectionModeProperty().set(SelectionMode.SINGLE);
    	
    	this.projectsListView.setCellFactory(val -> new DetailedProjectListCell());
    	
    	MenuItem newMenuItem = new MenuItem("Projekt hinzufügen");
    	newMenuItem.setOnAction(ev -> {
    		this.openProjectEditView(null);
    	});
    	
    	MenuItem editMenuItem = new MenuItem("Projekt bearbeiten");
    	editMenuItem.setOnAction(ev -> {
    		this.openProjectEditView(this.projectsListView.getSelectionModel().getSelectedItem());
    	});
    	editMenuItem.setDisable(true);
    	
    	MenuItem deleteMenuItem = new MenuItem("Projekt entfernen");
    	deleteMenuItem.setOnAction(ev -> {
    		Project selectedProject = this.projectsListView
    				.getSelectionModel().getSelectedItem();

    		if(selectedProject != null) {
    			Alert warningDialog = new Alert(AlertType.WARNING,
    					"Möchten Sie das Projekt wirklich löschen?\nDiese Aktion ist permanent!",
    					ButtonType.NO, ButtonType.YES);
    			Optional<ButtonType> result = warningDialog.showAndWait();
    			
    			if(result.isPresent() && result.get().equals(ButtonType.YES))
    				this.deleteProject(selectedProject);
    		}
    	});
    	deleteMenuItem.setDisable(true);
    	
    	this.projectsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		editMenuItem.setDisable(newValue == null);
    		deleteMenuItem.setDisable(newValue == null);
    	});
    	
    	this.projectsListView.setContextMenu(new ContextMenu(newMenuItem, editMenuItem, deleteMenuItem));
    	
    	this.projectsListView.setItems(DBInterface.getInstance().getProjects());
    }
    
    @Override
    public void refresh() {
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
			
			DBInterface.getInstance().refresh(project);
			this.projectsListView.refresh();
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
    
    private void deleteProject(Project project) {
    	try {
			EntityManager em = DBInterface.getInstance().createEntityManager();
			
			Project p = em.find(Project.class, project.id);
			
			em.getTransaction().begin();
			em.remove(p);
			em.getTransaction().commit();
			em.close();
			
			DBInterface.getInstance().getProjects().remove(project);
		} catch(Exception e) {
			Alert alert = new Alert(AlertType.ERROR,
					"Das Löschen des Projektes ist fehlgeschlagen",
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
			
			if(result.isPresent() && result.get().equals(ButtonType.YES))
				this.deleteProject(selectedProject);
		} else {
			Alert alert = new Alert(AlertType.ERROR,
				"Wählen Sie ein Projekt aus der Liste aus",
				ButtonType.OK);
			alert.showAndWait();
		}
    }
    
}
