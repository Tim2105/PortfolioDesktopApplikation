package controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import entity.Developer;
import entity.Project;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBInterface;
import view.DeveloperListCell;
import view.EmptyViewPlaceholder;

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
    private Button removeImageButton;

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
    
    private File selectedImage;
    
    private boolean imageRemoved = false;
    
    @FXML
    void initialize() {
    	this.developerListView.setPlaceholder(new EmptyViewPlaceholder(
    						"Noch keine Entwickler hinzugefügt",
    						"Entwickler hinzufügen",
    						ev -> {
    							this.addDevelopers();
    						}
    			));
    	
    	this.developerListView.setCellFactory(val -> new DeveloperListCell());
    	
    	MenuItem newMenuItem = new MenuItem("Entwickler hinzufügen");
    	newMenuItem.setOnAction(ev -> {
    		this.addDevelopers();
    	});
    	
    	MenuItem deleteMenuItem = new MenuItem("Entwickler entfernen");
    	deleteMenuItem.setOnAction(ev -> {
    		Developer selectedDeveloper = this.developerListView
    				.getSelectionModel().getSelectedItem();
    		
    		this.developerListView.getItems().remove(selectedDeveloper);
    	});
    	deleteMenuItem.setDisable(true);
    	
    	this.developerListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		deleteMenuItem.setDisable(newValue == null);
    	});
    	
    	this.developerListView.setContextMenu(new ContextMenu(newMenuItem, deleteMenuItem));
    }

    @FXML
    private void handleChooseFileButtonAction() {
    	Stage fcStage = new Stage();
    	fcStage.initModality(Modality.APPLICATION_MODAL);
    	fcStage.initOwner(this.root.getScene().getWindow());
    	
    	FileChooser fc = new FileChooser();
    	fc.setTitle("Datei öffnen");
    	fc.getExtensionFilters().addAll(
    			new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpeg", "*.jpg"),
    			new FileChooser.ExtensionFilter("PNG", "*.png"),
    			new FileChooser.ExtensionFilter("JPEG", "*.jpeg", "*.jpg")
    	);
    	
    	selectedImage = fc.showOpenDialog(fcStage);
    	
    	if(selectedImage != null) {
	    	try {
				byte[] imageBuffer = Files.readAllBytes(selectedImage.toPath());
				
				Image image = new Image(new ByteArrayInputStream(imageBuffer),
											this.imagePreviewLabel.getMaxWidth(),
											this.imagePreviewLabel.getMaxHeight(),
											true, true);
				
				this.imagePreviewLabel.setGraphic(new ImageView(image));
				this.imagePreviewLabel.setText(null);
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR,
						"Beim Lesen der Datei ist ein Fehler aufgetreten:\n" + e.getMessage(),
						ButtonType.OK);
				alert.show();
			}
    	}
    }
    
    @FXML
    private void handleRemoveImageButtonAction() {
    	this.selectedImage = null;
    	this.imageRemoved = true;
    	this.imagePreviewLabel.setGraphic(null);
    	this.imagePreviewLabel.setText("Vorschau des Bildes");
    }
    
    private void addDevelopers() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectDeveloperView.fxml"));
			Parent root = loader.load();
			SelectController<Developer> controller = loader.getController();
			
			List<Developer> selectableItems = new ArrayList<Developer>(DBInterface.getInstance().getDevelopers());
			
			for(Developer d : this.developerListView.getItems())
				selectableItems.remove(d);
			
			controller.showItems(selectableItems);
			
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Entwickler hinzufügen";
			
			stage.setTitle(stageTitle);
			stage.showAndWait();
			
			List<Developer> selectedDevelopers = controller.getSelectedItems();
			this.developerListView.getItems().addAll(selectedDevelopers);
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
    	this.addDevelopers();
    }

    @FXML
    private void handleRemoveDeveloperButtonAction() {
    	Developer selectedDeveloper = this.developerListView.getSelectionModel().getSelectedItem();
    	
    	if(selectedDeveloper != null) {
    		this.developerListView.getItems().remove(selectedDeveloper);
    	} else {
			Alert alert = new Alert(AlertType.ERROR,
					"Wählen Sie einen Entwickler aus der Liste aus",
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
    	try {
	    	if(this.entity == null) {
	    		// Erstelle ein neues Projekt und persistiere es in der Datenbank
	    		
	    		Project p = new Project(
	    				this.titleTextField.getText(),
	    				this.descriptionTextField.getText(),
	    				this.selectedImage,
	    				this.downloadTextField.getText(),
	    				this.demoTextField.getText()
	    		);
	    		
	    		for(Developer d : this.developerListView.getItems())
	    			p.addDeveloper(d);
	    		
	    		EntityManager em = DBInterface.getInstance().createEntityManager();
	    		
	    		em.getTransaction().begin();
	    		em.persist(p);
	    		em.getTransaction().commit();
	    		
	    		em.close();
	    		
	    		DBInterface.getInstance().getProjects().add(p);
	    	} else {
	    		// Verändere das Projekt und persistiere die Änderungen
    			this.entity.setTitle(this.titleTextField.getText());
    			this.entity.setDescription(this.descriptionTextField.getText());
    			
    			if(this.selectedImage != null)
    				this.entity.setImage(Files.readAllBytes(this.selectedImage.toPath()));
    			else if(this.imageRemoved)
    				this.entity.setImage(null);
    			
    			this.entity.setSourceURL(this.downloadTextField.getText());
    			this.entity.setDemoURL(this.demoTextField.getText());
    			
    			this.entity.removeAllDevelopers();
    			for(Developer d : this.developerListView.getItems())
	    			this.entity.addDeveloper(d);
    			
    			EntityManager em = DBInterface.getInstance().createEntityManager();
	    		em.getTransaction().begin();
	    		em.merge(this.entity);
    			em.getTransaction().commit();
    			em.close();
	    	}
	    	
	    	this.close();
    	} catch(IllegalArgumentException e) {
    		Alert alert = new Alert(AlertType.ERROR,
					e.getMessage(),
					ButtonType.OK);
			alert.show();
    	} catch(Exception e) {
    		e.printStackTrace();
    		
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
		}
    }

	@Override
	protected void update() {
		if(this.entity == null)
			return;
		
		this.titleTextField.setText(this.entity.getTitle());
		this.descriptionTextField.setText(this.entity.getDescription());
		
		Image img = this.entity.getImage(this.imagePreviewLabel.getMaxWidth(), this.imagePreviewLabel.getMaxHeight());
		if(img != null) {
			this.imagePreviewLabel.setGraphic(new ImageView(img));
			this.imagePreviewLabel.setText(null);
		}
		
		this.downloadTextField.setText(this.entity.getSourceURL());
		this.demoTextField.setText(this.entity.getDemoURL());
		
		this.developerListView.getItems().addAll(this.entity.getDevelopers());
	}
}
