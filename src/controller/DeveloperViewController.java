package controller;

import java.util.Optional;

import entity.Developer;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBInterface;
import view.DetailedDeveloperListCell;
import view.EmptyListViewPlaceholder;

public class DeveloperViewController extends Controller {

    @FXML
    private Button addDeveloperButton;

    @FXML
    private Button changeDeveloperButton;

    @FXML
    private Button deleteDeveloperButton;

    @FXML
    private ListView<Developer> developerListView;
    
    @FXML
    void initialize() {
    	this.developerListView.setPlaceholder(new EmptyListViewPlaceholder(
    						"Noch keine Entwickler hinzugefügt",
    						"Entwickler hinzufügen",
    						ev -> {
    							this.openDeveloperEditView(null);
    						}
    			));
    	
    	this.developerListView.getSelectionModel()
    			.selectionModeProperty().set(SelectionMode.SINGLE);
    	
    	this.developerListView.setCellFactory(val -> new DetailedDeveloperListCell());
    	
    	this.developerListView.setItems(DBInterface.getInstance().getDevelopers());
    }
    
    private void openDeveloperEditView(Developer developer) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DeveloperEditView.fxml"));
			Parent root = loader.load();
			EditController<Developer> controller = loader.getController();
			
			controller.setEntity(developer);
			
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
			stage.showAndWait();
			
			DBInterface.getInstance().refresh(developer);
			this.developerListView.refresh();
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
    }

    @FXML
    private void handleAddDeveloperButtonAction() {
    	this.openDeveloperEditView(null);
    }

    @FXML
    private void handleChangeDeveloperButtonAction() {
    	Developer selectedDeveloper = this.developerListView
    				.getSelectionModel().getSelectedItem();
    	
    	if(selectedDeveloper != null)
    		this.openDeveloperEditView(selectedDeveloper);
    	else {
    		Alert alert = new Alert(AlertType.ERROR,
					"Wählen Sie einen Entwickler aus der Liste aus",
					ButtonType.OK);
			alert.showAndWait();
    	}
    }

    @FXML
    private void handleDeleteDeveloperButtonAction() {
    	Developer selectedDeveloper = this.developerListView
				.getSelectionModel().getSelectedItem();
    	
    	if(selectedDeveloper != null) {
    		Alert warningDialog = new Alert(AlertType.WARNING,
					"Möchten Sie den Entwickler wirklich löschen?\nDiese Aktion ist permanent!",
					ButtonType.NO, ButtonType.YES);
			Optional<ButtonType> result = warningDialog.showAndWait();
			
			if(result.isPresent() && result.get().equals(ButtonType.YES)) {
				try {
					EntityManager em = DBInterface.getInstance().createEntityManager();
					
					Developer d = em.find(Developer.class, selectedDeveloper.id);
					
					em.getTransaction().begin();
					em.remove(d);
					em.getTransaction().commit();
					em.close();
					
					DBInterface.getInstance().getDevelopers().remove(selectedDeveloper);
				} catch(Exception e) {
					Alert alert = new Alert(AlertType.ERROR,
							"Das Löschen des Entwicklers ist fehlgeschlagen",
							ButtonType.OK);
						alert.showAndWait();
				}
			}
    	} else {
    		Alert alert = new Alert(AlertType.ERROR,
    				"Wählen Sie einen Entwickler aus der Liste aus",
    				ButtonType.OK);
    			alert.showAndWait();
    	}
    }
}
