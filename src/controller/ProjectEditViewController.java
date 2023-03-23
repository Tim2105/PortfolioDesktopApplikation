package controller;

import entity.Developer;
import entity.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import view.EmptyListViewPlaceholder;

public class ProjectEditViewController extends EditController<Project> {

    @FXML
    private Label imagePreviewLabel;

    @FXML
    private TextField titleTextField;

    @FXML
    private TextArea descriptionTextField;

    @FXML
    private Button chooseFileButton;

    @FXML
    private TextField downloadTextField;

    @FXML
    private TextField demoTextField;

    @FXML
    private ListView<Developer> developerListView;

    @FXML
    private Button addDeveloperButton;

    @FXML
    private Button removeDeveloperButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;
    
    @FXML
    void initialize() {
    	this.developerListView.setPlaceholder(new EmptyListViewPlaceholder(
    						"Noch keine Entwickler hinzugefügt",
    						"Entwickler hinzufügen"
    			));
    }

    @FXML
    private void handleChooseFileButtonAction() {
        // handle choose file button action
    }

    @FXML
    private void handleAddDeveloperButtonAction() {
        // handle add developer button action
    }

    @FXML
    private void handleRemoveDeveloperButtonAction() {
        // handle remove developer button action
    }

    @FXML
    private void handleCancelButtonAction() {
    	((Stage)cancelButton.getScene().getWindow()).close();
    }

    @FXML
    private void handleConfirmButtonAction() {
    	((Stage)confirmButton.getScene().getWindow()).close();
    }
}
