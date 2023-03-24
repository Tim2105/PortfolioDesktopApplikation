package controller;

import entity.ContactOpportunity;
import entity.Developer;
import entity.Project;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.EmptyListViewPlaceholder;

public class DeveloperEditViewController extends EditController<Developer> {

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ListView<ContactOpportunity> contactListView;

    @FXML
    private ListView<Project> projectListView;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button addContactButton;

    @FXML
    private Button editContactButton;

    @FXML
    private Button removeContactButton;
    
    @FXML
    private Button addProjectButton;
    
    @FXML
    private Button removeProjectButton;
    
    @FXML
    void initialize() {
    	this.contactListView.setPlaceholder(new EmptyListViewPlaceholder(
    						"Erzählen Sie uns,\nwie man Sie erreichen kann",
    						"Kontaktmöglichkeit hinzufügen",
    						ev -> {
    							this.openContactEditView(null);
    						}
    			));
    	
    	this.contactListView.getSelectionModel()
    			.selectionModeProperty().set(SelectionMode.SINGLE);
    	
    	this.projectListView.setPlaceholder(new Label("Noch keine Projekte hinzugefügt"));
    	
    	this.projectListView.getSelectionModel()
    			.selectionModeProperty().set(SelectionMode.SINGLE);
    }

    private void openContactEditView(ContactOpportunity contact) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContactEditView.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Kontakt bearbeiten - ";
			
			if(contact != null)
				stageTitle += contact.getPlatform().toString();
			else
				stageTitle += "Neue Kontaktmöglichkeit";
			
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
    private void handleCancelButtonAction() {
    	this.close();
    }

    @FXML
    private void handleConfirmButtonAction() {
    	this.close();
    }

    @FXML
    private void handleAddContactButtonAction() {
    	this.openContactEditView(null);
    }

    @FXML
    private void handleEditContactButtonAction() {
    	ContactOpportunity selectedContact = this.contactListView
				.getSelectionModel().getSelectedItem();

		if(selectedContact != null)
			this.openContactEditView(selectedContact);
		else {
			Alert alert = new Alert(AlertType.ERROR,
					"Wählen Sie eine Kontaktmöglichkeit aus der Liste aus",
					ButtonType.OK);
			alert.showAndWait();
		}
    }

    @FXML
    private void handleRemoveContactButtonAction() {
    	
    }
    
    @FXML
    private void handleAddProjectButtonAction() {
    	
    }
    
    @FXML
    private void handleRemoveProjectButtonAction() {
    	
    }

	@Override
	protected void update() {
		
	}
}
