package controller;

import java.util.Optional;

import entity.Demonstration;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBInterface;
import view.DemonstrationListCell;
import view.EmptyViewPlaceholder;

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
		this.demonstrationListView.setPlaceholder(new EmptyViewPlaceholder(
								"Noch keine Demonstrationen hochgeladen",
								"Demonstration hinzufügen",
								ev -> {
									this.openDemonstrationEditView(null);
								}
				));
		
		this.demonstrationListView.getSelectionModel()
				.selectionModeProperty().set(SelectionMode.SINGLE);
		
		this.demonstrationListView.setCellFactory(val -> new DemonstrationListCell());
		
		MenuItem newMenuItem = new MenuItem("Demonstration hinzufügen");
    	newMenuItem.setOnAction(ev -> {
    		this.openDemonstrationEditView(null);
    	});
    	
    	MenuItem editMenuItem = new MenuItem("Demonstration bearbeiten");
    	editMenuItem.setOnAction(ev -> {
    		this.openDemonstrationEditView(this.demonstrationListView.getSelectionModel().getSelectedItem());
    	});
    	editMenuItem.setDisable(true);
    	
    	MenuItem deleteMenuItem = new MenuItem("Demonstration entfernen");
    	deleteMenuItem.setOnAction(ev -> {
    		Demonstration selectedDemo = this.demonstrationListView
    				.getSelectionModel().getSelectedItem();

    		if(selectedDemo != null) {
    			Alert warningDialog = new Alert(AlertType.WARNING,
    					"Möchten Sie die Demonstration wirklich löschen?\nDiese Aktion ist permanent!",
    					ButtonType.NO, ButtonType.YES);
    			Optional<ButtonType> result = warningDialog.showAndWait();
    			
    			if(result.isPresent() && result.get().equals(ButtonType.YES))
    				this.deleteDemonstration(selectedDemo);
    		}
    	});
    	deleteMenuItem.setDisable(true);
    	
    	this.demonstrationListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		editMenuItem.setDisable(newValue == null);
    		deleteMenuItem.setDisable(newValue == null);
    	});
    	
    	this.demonstrationListView.setContextMenu(new ContextMenu(newMenuItem, editMenuItem, deleteMenuItem));
		
		this.demonstrationListView.setItems(DBInterface.getInstance().getDemonstrations());
	}

	@Override
	public void refresh() {
		this.demonstrationListView.setItems(DBInterface.getInstance().getDemonstrations());
	}
	
	private void openDemonstrationEditView(Demonstration demo) {
		try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DemonstrationEditView.fxml"));
			Parent root = loader.load();
			EditController<Demonstration> controller = loader.getController();
			controller.setEntity(demo);
			
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Demonstration bearbeiten - ";
			
			if(demo != null)
				stageTitle += demo.getName();
			else
				stageTitle += "Neue Demonstration";
			
			stage.setTitle(stageTitle);
			stage.showAndWait();
			
			DBInterface.getInstance().refresh(demo);
			this.demonstrationListView.refresh();
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
	}
	
	@FXML
	private void handleNewDemonstrationButtonAction() {
		this.openDemonstrationEditView(null);
	}
	
	@FXML
	private void handleEditDemonstrationButtonAction() {
		Demonstration selectedDemo = this.demonstrationListView
    			.getSelectionModel().getSelectedItem();
    	
    	if(selectedDemo != null)
    		this.openDemonstrationEditView(selectedDemo);
    	else {
    		Alert alert = new Alert(AlertType.ERROR,
					"Wählen Sie eine Demonstration aus der Liste aus",
					ButtonType.OK);
			alert.showAndWait();
    	}
	}
	
	private void deleteDemonstration(Demonstration demonstration) {
		try {
			EntityManager em = DBInterface.getInstance().createEntityManager();
			
			Demonstration d = em.find(Demonstration.class, demonstration.id);
			
			em.getTransaction().begin();
			em.remove(d);
			em.getTransaction().commit();
			em.close();
			
			DBInterface.getInstance().getDemonstrations().remove(demonstration);
		} catch(Exception e) {
			Alert alert = new Alert(AlertType.ERROR,
					"Das Löschen der Demonstration ist fehlgeschlagen",
					ButtonType.OK);
				alert.showAndWait();
		}
	}
	
	@FXML
	private void handleDeleteDemonstrationButtonAction() {
		Demonstration selectedDemo = this.demonstrationListView
					.getSelectionModel().getSelectedItem();
		
		if(selectedDemo != null) {
			Alert warningDialog = new Alert(AlertType.WARNING,
					"Möchten Sie die Demonstration wirklich löschen?\nDiese Aktion ist permanent!",
					ButtonType.NO, ButtonType.YES);
			Optional<ButtonType> result = warningDialog.showAndWait();
			
			if(result.isPresent() && result.get().equals(ButtonType.YES))
				this.deleteDemonstration(selectedDemo);
		} else {
			Alert alert = new Alert(AlertType.ERROR,
    				"Wählen Sie eine Demonstration aus der Liste aus",
    				ButtonType.OK);
    			alert.showAndWait();
		}
	}
	
}
