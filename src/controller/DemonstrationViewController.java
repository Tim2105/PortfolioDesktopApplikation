package controller;

import entity.Demonstration;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import model.DBInterface;
import view.DemonstrationListCell;
import view.EmptyListViewPlaceholder;

public class DemonstrationViewController extends Controller implements Refreshable {

	@FXML
	private Button newDemonstrationButton;
	
	@FXML
	private Button editDemonstrationButton;
	
	@FXML
	private Button deleteDemonstrationButton;
	
	@FXML
	private ListView<Demonstration> demonstrationListView;
	
	@FXML
	void initialize() {
		this.demonstrationListView.setPlaceholder(new EmptyListViewPlaceholder(
								"Noch keine Demonstrationen hochgeladen",
								"Demonstration hinzufügen",
								ev -> {
									
								}
				));
		
		this.demonstrationListView.getSelectionModel()
				.selectionModeProperty().set(SelectionMode.SINGLE);
		
		this.demonstrationListView.setCellFactory(val -> new DemonstrationListCell());
		
		this.demonstrationListView.setItems(DBInterface.getInstance().getDemonstrations());
	}

	@Override
	public void refresh() {
		this.demonstrationListView.setItems(DBInterface.getInstance().getDemonstrations());
	}
	
	@FXML
	private void handleNewDemonstrationButtonAction() {
		
	}
	
	@FXML
	private void handleEditDemonstrationButtonAction() {
		
	}
	
	@FXML
	private void handleDeleteDemonstrationButtonAction() {
		
	}
	
}
