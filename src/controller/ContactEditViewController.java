package controller;

import entity.ContactOpportunity;
import entity.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ContactEditViewController extends EditController<ContactOpportunity> {

    @FXML
    private ChoiceBox<Platform> platformChoiceBox;

    @FXML
    private TextField urlTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
    	this.platformChoiceBox.getItems().addAll(Platform.values());
    }

    @FXML
    private void handleCancelButtonAction() {
    	this.close();
    }

    @FXML
    private void handleConfirmButtonAction() {
    	this.close();
    }

	@Override
	protected void update() {
		
	}
}
