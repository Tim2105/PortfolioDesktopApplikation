package controller;

import entity.ContactOpportunity;
import entity.Developer;
import entity.Project;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    						"Kontaktmöglichkeit hinzufügen"
    			));
    	
    	this.projectListView.setPlaceholder(new Label("Noch keine Projekte hinzugefügt"));
    }

    @FXML
    private void handleCancelButtonAction() {
    	((Stage)cancelButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleConfirmButtonAction() {
    	((Stage)confirmButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleAddContactButtonAction() {
    	
    }

    @FXML
    private void handleEditContactButtonAction() {
    	
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
}
