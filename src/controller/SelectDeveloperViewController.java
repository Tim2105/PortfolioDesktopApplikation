package controller;

import entity.Developer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import view.DetailedDeveloperListCell;

public class SelectDeveloperViewController extends SelectController<Developer> {

    @FXML
    private ListView<Developer> developerListView;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;
    
    @FXML
    void initialize() {
    	this.developerListView.setPlaceholder(new Label("Es können keine weiteren Entwickler hinzugefügt werden"));
    	
    	this.developerListView.getSelectionModel()
    			.selectionModeProperty().set(SelectionMode.MULTIPLE);
    	
    	this.developerListView.setCellFactory(val -> new DetailedDeveloperListCell());
    }

    @FXML
    private void handleCancelButtonAction() {
    	this.close();
    }

    @FXML
    private void handleConfirmButtonAction() {
    	this.getSelectedItems().clear();
    	this.getSelectedItems().addAll(this.developerListView.getSelectionModel().getSelectedItems());
    	
    	this.close();
    }

	@Override
	protected void update() {
		this.developerListView.getItems().clear();
		this.developerListView.getItems().addAll(this.getShownItems());
	}
}
