package controller;

import entity.Developer;
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
        // Implementierung für den "Entwickler entfernen"-Button
    }
}
