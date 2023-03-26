package controller;

import java.util.ArrayList;
import java.util.List;

import entity.ContactOpportunity;
import entity.Developer;
import entity.Project;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBInterface;
import view.ContactOpportunityListCell;
import view.EmptyListViewPlaceholder;
import view.ProjectListCell;

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
    	
    	this.contactListView.setCellFactory(val -> new ContactOpportunityListCell());
    	
    	this.projectListView.setPlaceholder(new Label("Noch keine Projekte hinzugefügt"));
    	
    	this.projectListView.getSelectionModel()
    			.selectionModeProperty().set(SelectionMode.SINGLE);
    	
    	this.projectListView.setCellFactory(val -> new ProjectListCell());
    }

    private void openContactEditView(ContactOpportunity contact) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ContactEditView.fxml"));
			Parent root = loader.load();
			EditController<ContactOpportunity> controller = loader.getController();
			controller.setEntity(contact);
			
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
			stage.showAndWait();
			
			if(contact == null && controller.getEntity() != null) {
				// Füge neue Kontaktmöglichkeit hinzu
				this.contactListView.getItems().add(controller.getEntity());
			} else {
				// Liste neu zeichnen
				this.contactListView.refresh();
			}
			
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
    	try {
    		if(this.entity == null) {
    			// Erstelle einen neuen Entwickler und persistiere ihn in der Datenbank
    			
    			Developer d = new Developer(
    					this.firstNameTextField.getText(),
    					this.lastNameTextField.getText(),
    					this.descriptionTextArea.getText()
    			);
    			
    			for(ContactOpportunity c : this.contactListView.getItems())
    				d.addContactOpportunity(c);
    			
    			for(Project p : this.projectListView.getItems())
    				d.addProject(p);
    			
    			EntityManager em = DBInterface.getInstance().createEntityManager();
	    		
	    		em.getTransaction().begin();
	    		em.persist(d);
	    		
	    		// Die Klasse Project ist der Besitzer der n-m-Beziehung.
	    		// Deshalb müssen alle Änderungen in der Assoziationstabelle über Projekte passieren
	    		for(Project p : d.getProjects())
	    			em.merge(p);
	    		
	    		em.getTransaction().commit();
	    		
	    		em.close();
	    		
	    		DBInterface.getInstance().getDevelopers().add(d);
    		} else {
    			// Verändere den Entwickler und persistiere alle Änderungen
    			
    			this.entity.setFirstname(this.firstNameTextField.getText());
    			this.entity.setLastname(this.lastNameTextField.getText());
    			this.entity.setDescription(this.descriptionTextArea.getText());
    			
    			List<ContactOpportunity> oldContacts = new ArrayList<ContactOpportunity>(this.entity.getContactOpportunities());
    			this.entity.removeAllContactOpportunites();
    			
    			for(ContactOpportunity c : this.contactListView.getItems())
    				this.entity.addContactOpportunity(c);
    			
    			List<Project> oldProjects = new ArrayList<Project>(this.entity.getProjects());
    			this.entity.removeAllProjects();
    			
    			for(Project p : this.projectListView.getItems())
    				this.entity.addProject(p);
    			
    			EntityManager em = DBInterface.getInstance().createEntityManager();
    			
	    		em.getTransaction().begin();
	    		em.merge(this.entity);
	    		
	    		for(ContactOpportunity c : oldContacts) {
	    			c = em.merge(c);
	    			
	    			if(!this.entity.getContactOpportunities().contains(c))
	    				em.remove(c);
	    		}
	    		
	    		for(ContactOpportunity c : this.entity.getContactOpportunities())
	    			em.merge(c);
	    		
	    		// Die Klasse Project ist der Besitzer der n-m-Beziehung.
	    		// Deshalb müssen alle Änderungen in der Assoziationstabelle über Projekte passieren
	    		
	    		// evtl. alte Projekte entfernen
	    		for(Project p : oldProjects)
	    			em.merge(p);
	    		
	    		// evtl. neue Projekte hinzufügen
	    		for(Project p : this.entity.getProjects())
	    			em.merge(p);
	    		
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
    	ContactOpportunity selectedContact = this.contactListView.getSelectionModel().getSelectedItem();
    	
    	if(selectedContact != null) {
    		this.contactListView.getItems().remove(selectedContact);
    	} else {
    		Alert alert = new Alert(AlertType.ERROR,
					"Wählen Sie eine Kontaktmöglichkeit aus der Liste aus",
					ButtonType.OK);
			alert.show();
    	}
    }
    
    private void addProjects() {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SelectProjectView.fxml"));
			Parent root = loader.load();
			SelectController<Project> controller = loader.getController();
			
			List<Project> selectableItems = new ArrayList<Project>(DBInterface.getInstance().getProjects());
			
			for(Project d : this.projectListView.getItems())
				selectableItems.remove(d);
			
			controller.showItems(selectableItems);
			
			Scene scene = new Scene(root);
			
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(root.getScene().getWindow());
			stage.setScene(scene);
			
			String stageTitle = "Projekte hinzufügen";
			
			stage.setTitle(stageTitle);
			stage.showAndWait();
			
			List<Project> selectedProjects = controller.getSelectedItems();
			this.projectListView.getItems().addAll(selectedProjects);
    	} catch(Exception e) {
    		e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR,
					"Ein unerwarteter Fehler ist aufgetreten:\n" + e.getMessage(),
					ButtonType.OK);
			alert.show();
    	}
    }
    
    @FXML
    private void handleAddProjectButtonAction() {
    	this.addProjects();
    }
    
    @FXML
    private void handleRemoveProjectButtonAction() {
    	Project selectedProject = this.projectListView.getSelectionModel().getSelectedItem();
    	
    	if(selectedProject != null) {
    		this.projectListView.getItems().remove(selectedProject);
    	} else {
    		Alert alert = new Alert(AlertType.ERROR,
					"Wählen Sie ein Projekt aus der Liste aus",
					ButtonType.OK);
			alert.show();
    	}
    }

	@Override
	protected void update() {
		if(this.entity == null)
			return;
		
		this.firstNameTextField.setText(this.entity.getFirstname());
		this.lastNameTextField.setText(this.entity.getLastname());
		this.descriptionTextArea.setText(this.entity.getDescription());
		
//		List<ContactOpportunity> contactCopies = new ArrayList<ContactOpportunity>();
//		
//		for(ContactOpportunity c : this.entity.getContactOpportunities()) {
//			ContactOpportunity copy = new ContactOpportunity(c.getPlatform(), c.getURL());
//			copy.id = c.id;
//			
//			contactCopies.add(copy);
//		}
		
		this.contactListView.getItems().addAll(this.entity.getContactOpportunities());
		
		this.projectListView.getItems().addAll(this.entity.getProjects());
	}
}
