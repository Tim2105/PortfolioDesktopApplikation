package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import entity.Demonstration;
import entity.DemonstrationFile;
import jakarta.persistence.EntityManager;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBInterface;
import view.EmptyViewPlaceholder;
import view.FilenameTableCell;
import view.WelcomeFileTableCell;

public class DemonstrationEditViewController extends EditController<Demonstration> {

	@FXML
	private TextField nameTextField;
	
    @FXML
    private Button addFolderButton;

    @FXML
    private Button addFilesButton;

    @FXML
    private Button removeFilesButton;

    @FXML
    private TableView<DemonstrationFile> fileTableView;

    @FXML
    private TableColumn<DemonstrationFile, String> filenameColumn;

    @FXML
    private TableColumn<DemonstrationFile, Boolean> welcomePageColumn;

    @FXML
    private Button cancelButton;

    @FXML
    private Button confirmButton;
    
    private Map<String, SimpleBooleanProperty> welcomeFileProperties;
    
    private DemonstrationFile welcomeFile = null;
    
    @FXML
    void initialize() {
    	this.welcomeFileProperties = new HashMap<String, SimpleBooleanProperty>();
    	
    	this.filenameColumn.setCellValueFactory(val -> val.getValue().getFilenameProperty());
    	this.filenameColumn.setCellFactory(val -> new FilenameTableCell());
    	
    	Comparator<String> filenameComparator = (s1, s2) -> {	
    		long numSlashesS1 = s1.chars().filter(c -> c == '/').count();
    		long numSlashesS2 = s2.chars().filter(c -> c == '/').count();
    		
    		if(numSlashesS1 != numSlashesS2)
    			return (int)(numSlashesS1 - numSlashesS2);
    		
    		return s1.compareToIgnoreCase(s2);
    	};
    	
    	this.filenameColumn.setComparator(filenameComparator);
    	
    	this.welcomePageColumn.setCellValueFactory(val -> welcomeFileProperties.get(val.getValue().getFilename()));
    	this.welcomePageColumn.setCellFactory(val -> {
    		WelcomeFileTableCell cell = new WelcomeFileTableCell();
    		
    		cell.addChangeListener((observable, oldValue, newValue) -> {
    			if(newValue && welcomeFile != null)
    				welcomeFileProperties.get(welcomeFile.getFilename()).set(false);
    			
    			welcomeFileProperties.get(cell.getDemonstrationFile().getFilename()).set(newValue);
    			
    			if(newValue)
    				welcomeFile = cell.getDemonstrationFile();
    			else
    				welcomeFile = null;
    		});
    		
    		return cell;
    	});
    	
    	this.fileTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	this.fileTableView.setPlaceholder(new EmptyViewPlaceholder(
    					"Noch keine Dateien hinzugefügt",
    					"Dateien hinzufügen",
    					ev -> {
    						this.chooseAndAddFiles();
    					}
    			));
    	
    	MenuItem addFilesMenuItem = new MenuItem("Dateien hinzufügen");
    	addFilesMenuItem.setOnAction(ev -> {
    		this.chooseAndAddFiles();
    	});
    	
    	MenuItem addFolderMenuItem = new MenuItem("Ordner hinzufügen");
    	addFolderMenuItem.setOnAction(ev -> {
    		this.chooseAndAddFolder();
    	});
    	
    	MenuItem deleteFilesMenuItem = new MenuItem("Dateien entfernen");
    	deleteFilesMenuItem.setOnAction(ev -> {
    		List<DemonstrationFile> selectedFiles = new ArrayList<DemonstrationFile>(this.fileTableView.getSelectionModel().getSelectedItems());
    		
    		this.deleteFiles(selectedFiles);
    	});
    	deleteFilesMenuItem.setDisable(true);
    	
    	this.fileTableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<DemonstrationFile>() {
			@Override
			public void onChanged(Change<? extends DemonstrationFile> c) {
				deleteFilesMenuItem.setDisable(fileTableView.getItems().size() == 0);
			}
    	});
    	
    	this.fileTableView.setContextMenu(new ContextMenu(addFilesMenuItem, addFolderMenuItem, deleteFilesMenuItem));
    }
    
    private void addFiles(List<DemonstrationFile> files) {
    	List<DemonstrationFile> duplicateFiles = new ArrayList<DemonstrationFile>();
    	
    	for(DemonstrationFile demoFile : files) {
			if(this.welcomeFileProperties.containsKey(demoFile.getFilename())) {
				duplicateFiles.add(demoFile);
			} else {
				this.welcomeFileProperties.put(demoFile.getFilename(), new SimpleBooleanProperty(false));
				this.fileTableView.getItems().add(demoFile);
			}
		}
		
		if(duplicateFiles.size() > 0) {
			String errorString = String.valueOf(duplicateFiles.size());
			errorString += duplicateFiles.size() > 1 ? " Dateien sind" : " Datei ist";
			errorString += " bereits vorhanden:\n";
			for(DemonstrationFile file : duplicateFiles)
				errorString += file.getFilename() + "\n";
			
			errorString += "\nÜberschreiben?";
			
			Alert alert = new Alert(AlertType.WARNING,
					errorString,
					ButtonType.NO, ButtonType.YES);
			Optional<ButtonType> result = alert.showAndWait();
			
			if(result.isPresent() && result.get().equals(ButtonType.YES)) {
				for(DemonstrationFile demoFile : duplicateFiles) {
					this.fileTableView.getItems().removeIf((file) -> {
						return file.getFilename().equals(demoFile.getFilename());
					});
					
					this.fileTableView.getItems().add(demoFile);
					this.welcomeFileProperties.get(demoFile.getFilename()).set(false);
				}
			}
		}
    }
    
    private void chooseAndAddFolder() {
    	Stage dcStage = new Stage();
    	dcStage.initModality(Modality.APPLICATION_MODAL);
    	dcStage.initOwner(this.root.getScene().getWindow());
    	
    	DirectoryChooser dc = new DirectoryChooser();
    	dc.setTitle("Ordner auswählen");
    	
    	File directory = dc.showDialog(dcStage);
    	
    	if(directory != null) {
    		try {
    			List<DemonstrationFile> demoFiles = new ArrayList<DemonstrationFile>();
    			List<File> filesNotRead = new ArrayList<File>();
    			
    			// Ersetze das Dateitrennzeichen durch ein '/'
    			FileSystem defaultFileSystem = FileSystems.getDefault();
    			String fileSep = defaultFileSystem.getSeparator();
    			
    			Files.find(directory.toPath(), Integer.MAX_VALUE,
    					(p, bfa) -> bfa.isRegularFile()).forEach(path -> {
    						try {
	    						DemonstrationFile demoFile =
	    								new DemonstrationFile(directory.toPath().relativize(path)
	    													.toFile().getPath().replace(fileSep, "/"),
	    													path.toFile());
	        					
	        					demoFiles.add(demoFile);
        					} catch(IOException e) {
        						filesNotRead.add(path.toFile());
        					}
    					});
    			
    			this.addFiles(demoFiles);
    			
    			if(filesNotRead.size() > 0) {
    				String errorString = String.valueOf(filesNotRead.size()) + " Dateien konnten nicht gelesen werden:\n";
    				for(File file : filesNotRead)
    					errorString += file.getPath() + "\n";
    				
    				Alert alert = new Alert(AlertType.ERROR,
    						errorString,
    						ButtonType.OK);
    				alert.showAndWait();
    			}
    			
    		} catch(IOException e) {
    			Alert alert = new Alert(AlertType.ERROR,
						"Auf den Ordner kann nicht mehr zugegriffen werden:\n" + e.getMessage(),
						ButtonType.OK);
				alert.show();
    		}
    	}
    }

    @FXML
    private void handleAddFolderButtonAction() {
    	this.chooseAndAddFolder();
    }
    
    private void chooseAndAddFiles() {
    	Stage fcStage = new Stage();
    	fcStage.initModality(Modality.APPLICATION_MODAL);
    	fcStage.initOwner(this.root.getScene().getWindow());
    	
    	FileChooser fc = new FileChooser();
    	fc.setTitle("Dateien auswählen");
    	fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Alle Dateien", "*"));
    	
    	List<File> files = fc.showOpenMultipleDialog(fcStage);
    	
    	if(files != null) {
	    	List<DemonstrationFile> demoFiles = new ArrayList<DemonstrationFile>();
	    	List<File> filesNotRead = new ArrayList<File>();
	    	
	    	for(File file : files) {
	    		try {
					DemonstrationFile demoFile = new DemonstrationFile(file.getName(), file);
					demoFiles.add(demoFile);
				} catch (IOException e) {
					filesNotRead.add(file);
				}
	    	}
	    	
	    	this.addFiles(demoFiles);
	    	
	    	if(filesNotRead.size() > 0) {
				String errorString = String.valueOf(filesNotRead.size()) + " Dateien konnten nicht gelesen werden:\n";
				for(File file : filesNotRead)
					errorString += file.getPath() + "\n";
				
				Alert alert = new Alert(AlertType.ERROR,
						errorString,
						ButtonType.OK);
				alert.showAndWait();
			}
    	}
    }

    @FXML
    private void handleAddFilesButtonAction() {
    	this.chooseAndAddFiles();
    }
    
    private void deleteFiles(List<DemonstrationFile> files) {
    	if(files.size() > 0) {
    		String warningText = "Möchten Sie die ";
    		warningText += files.size() > 1 ? "Dateien" : "Datei";
    		warningText += " wirklich entfernen?\nDiese Aktion ist permanent!";
    		
    		Alert warningDialog = new Alert(AlertType.WARNING,
					warningText,
					ButtonType.NO, ButtonType.YES);
			Optional<ButtonType> result = warningDialog.showAndWait();
			
			if(result.isPresent() && result.get().equals(ButtonType.YES)) {
				this.fileTableView.getItems().removeAll(files);
				
	    		for(DemonstrationFile selectedFile : files) {
		    		this.welcomeFileProperties.remove(selectedFile.getFilename());
		    		
		    		if(this.welcomeFile == selectedFile)
		    			this.welcomeFile = null;
	    		}
			}
    	} else {
    		Alert alert = new Alert(AlertType.ERROR,
					"Wählen Sie eine oder mehrere Dateien aus der Tabelle aus",
					ButtonType.OK);
			alert.showAndWait();
    	}
    }

    @FXML
    private void handleRemoveFilesButtonAction() {
    	List<DemonstrationFile> selectedFiles = new ArrayList<DemonstrationFile>(this.fileTableView.getSelectionModel().getSelectedItems());
    	
    	this.deleteFiles(selectedFiles);
    }

    @FXML
    private void handleCancelButtonAction() {
    	this.close();
    }

    @FXML
    private void handleConfirmButtonAction() {
    	if(this.fileTableView.getEditingCell() != null)
    		this.fileTableView.edit(-1, null); // Editieren beenden
    	
    	try {
    		if(this.entity == null) {
    			Demonstration d = new Demonstration(
    					this.nameTextField.getText(),
    					this.welcomeFile
    			);
    			
    			for(DemonstrationFile dFile : this.fileTableView.getItems())
    				d.addFile(dFile);
    			
    			EntityManager em = DBInterface.getInstance().createEntityManager();
    			
    			em.getTransaction().begin();
    			em.persist(d);
    			em.getTransaction().commit();
    			
    			em.close();
    			
    			DBInterface.getInstance().getDemonstrations().add(d);
    		} else {
    			
    			this.entity.setName(this.nameTextField.getText());
    			this.entity.setWelcomePage(this.welcomeFile);
    			
    			List<DemonstrationFile> oldFiles = new ArrayList<DemonstrationFile>(this.entity.getFiles());
    			this.entity.removeAllFiles();
    			
    			for(DemonstrationFile file : this.fileTableView.getItems())
    				this.entity.addFile(file);
    			
    			EntityManager em = DBInterface.getInstance().createEntityManager();
    			
    			em.getTransaction().begin();
    			em.merge(this.entity);
    			
    			for(DemonstrationFile file : oldFiles) {
    				file = em.merge(file);
    				
    				if(!this.entity.getFiles().contains(file))
    					em.remove(file);
    			}
    			
    			for(DemonstrationFile file : this.entity.getFiles())
    				em.merge(file);
    			
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
		this.fileTableView.getItems().clear();
		this.welcomeFileProperties.clear();
		this.welcomeFile = null;
		
		if(this.entity == null)
			return;
		
		this.nameTextField.setText(this.entity.getName());
		
		for(DemonstrationFile file : this.entity.getFiles()) {
			if(this.entity.getWelcomePage() != null) {
				boolean isWelcomeFile = this.entity.getWelcomePage().equals(file.getFilename());
				this.welcomeFileProperties.put(file.getFilename(), new SimpleBooleanProperty(isWelcomeFile));
				if(isWelcomeFile)
					this.welcomeFile = file;
			} else
				this.welcomeFileProperties.put(file.getFilename(), new SimpleBooleanProperty(false));
			
			this.fileTableView.getItems().add(file);
		}
	}
}
