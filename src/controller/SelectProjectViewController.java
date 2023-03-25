package controller;

import entity.Project;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import view.DetailedProjectListCell;

public class SelectProjectViewController extends SelectController<Project> {

	@FXML
    private ListView<Project> projectListView;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;
    
    @FXML
    void initialize() {
    	this.projectListView.setPlaceholder(new Label("Es können keine weiteren Projekte hinzugefügt werden"));
    	
    	this.projectListView.getSelectionModel()
    			.selectionModeProperty().set(SelectionMode.MULTIPLE);
    	
    	this.projectListView.setCellFactory(val -> new DetailedProjectListCell());
    }
    
    @FXML
    private void handleCancelButtonAction() {
    	this.close();
    }

    @FXML
    private void handleConfirmButtonAction() {
    	this.getSelectedItems().clear();
    	this.getSelectedItems().addAll(this.projectListView.getSelectionModel().getSelectedItems());
    	
    	this.close();
    }
	
	@Override
	protected void update() {
		this.projectListView.getItems().clear();
		this.projectListView.getItems().addAll(this.getShownItems());
	}

}
