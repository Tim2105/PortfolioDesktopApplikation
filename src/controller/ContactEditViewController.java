package controller;

import entity.ContactOpportunity;
import entity.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

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
    	if(this.entity == null) {
    		try {
	    		this.entity = new ContactOpportunity(
	    				this.platformChoiceBox.getValue(),
	    				this.urlTextField.getText()
	    			);
    		} catch(IllegalArgumentException e) {
    			this.entity = null;
    			e.printStackTrace();
    			Alert alert = new Alert(AlertType.ERROR,
    					e.getMessage(),
    					ButtonType.OK);
    			alert.showAndWait();
    		}
    	} else {
    		this.entity.setPlatform(this.platformChoiceBox.getValue());
    		
    		if(this.urlTextField.getText() != null && !this.urlTextField.getText().isBlank()) {
    			this.entity.setURL(this.urlTextField.getText());
    		}
    	}
    	
    	this.close();
    }

	@Override
	protected void update() {
		if(this.entity == null)
			return;
		
		this.platformChoiceBox.setValue(this.entity.getPlatform());
		this.urlTextField.setText(this.entity.getURL());
	}
}
