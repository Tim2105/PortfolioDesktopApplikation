package dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import javafx.scene.image.Image;

@Entity
@Table(name = "PROJECT")
@SequenceGenerator(name="GEN_PROJECT_ID", initialValue = 0, allocationSize = 1)
public class Project {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator = "GEN_PROJECT_ID")
	@Column(name = "ID")
	private int id;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "IMAGE")
	private byte[] image;
	
	@Column(name = "SOURCE")
	private String sourceURL;
	
	@Column(name = "DEMO")
	private String demoURL;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(
			name = "PROJECT_DEVELOPER",
			joinColumns = { @JoinColumn(name = "PROJECT", referencedColumnName = "ID") },
			inverseJoinColumns = { @JoinColumn(name = "DEVELOPER", referencedColumnName = "ID") }
	)
	private List<Developer> developers;
	
	public Project(String title, String description, File image, String sourceURL, String demoURL) throws IOException, RuntimeException, IllegalArgumentException {
		if(title == null)
			throw new IllegalArgumentException("Der Titel darf nicht null sein.");
		
		if(description == null)
			throw new IllegalArgumentException("Die Beschreibung darf nicht null sein.");
		
		this.title = title;
		this.description = description;
		this.sourceURL = sourceURL;
		this.demoURL = demoURL;
		this.developers = new ArrayList<Developer>();
		
		
		if(image != null ) {
			if(!image.canRead())
				throw new IOException("Für diese Datei besitzt das Programm keine Leseberechtigungen.");
			
			if(!image.isFile())
				throw new IllegalArgumentException("Der angegebene Pfad führt zu keiner Datei.");
			
			String fileExtension = "";
			String filePath = image.getAbsolutePath();
			
			if(filePath.contains("."))
				fileExtension = filePath.substring(filePath.lastIndexOf(".") + 1);
			
			if(!(fileExtension.equals("png") ||
					fileExtension.equals("jpeg") ||
					fileExtension.equals("jpg")))
				throw new IllegalArgumentException("Das Bild muss eine .png- oder eine .jpeg/.jpg-Datei sein.");
			
			try { this.image = Files.readAllBytes(image.toPath()); }
			catch(IOException e) {
				throw new IOException("Beim Lesen der Datei ist ein Fehler aufgetreten. Stellen Sie sicher, dass die Datei noch existiert und Sie Leseberechtigungen besitzen.");
			} catch(Exception e) {
				throw new RuntimeException("Beim Lesen der Datei ist ein unbekannter Fehler aufgetreten.");
			}
		} else {
			this.image = null;
		}
	}
	
	public Project() {
		this.developers = new ArrayList<Developer>();
	}
	
	public Image getImage() {
		return new Image(new ByteArrayInputStream(this.image));
	}
	
	public void addDeveloper(Developer developer) {
		this.developers.add(developer);
		developer.getProjects().add(this);
	}
	
	public void removeDeveloper(Developer developer) {
		this.developers.remove(developer);
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Project))
			return false;
		
		return ((Project)other).id == this.id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getSourceURL() {
		return this.sourceURL;
	}
	
	public void setSourceURL(String sourceURL) {
		this.sourceURL = sourceURL;
	}
	
	public String getDemoURL() {
		return this.demoURL;
	}
	
	public void setDemoURL(String demoURL) {
		this.demoURL = demoURL;
	}
	
	public List<Developer> getDevelopers() {
		return this.developers;
	}

}
