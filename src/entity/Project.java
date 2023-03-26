package entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Basic;
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
	public Integer id;
	
	@Column(name = "TITLE")
	public String title;
	
	@Column(name = "DESCRIPTION")
	public String description;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "IMAGE")
	public byte[] image;
	
	@Column(name = "SOURCE")
	public String sourceURL;
	
	@Column(name = "DEMO")
	public String demoURL;
	
	@ManyToMany
	@JoinTable(
			name = "PROJECT_DEVELOPER",
			joinColumns = { @JoinColumn(name = "PROJECT", referencedColumnName = "ID") },
			inverseJoinColumns = { @JoinColumn(name = "DEVELOPER", referencedColumnName = "ID") }
	)
	public List<Developer> developers;
	
	public Project(String title, String description, File image, String sourceURL, String demoURL) throws IOException, RuntimeException, IllegalArgumentException {
		if(title == null || title.isBlank())
			throw new IllegalArgumentException("Der Titel darf nicht leer sein.");
		
		if(description == null || description.isBlank())
			throw new IllegalArgumentException("Die Beschreibung darf nicht leer sein.");
		
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
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof Project))
			return false;
		
		Project other = (Project)obj;
		
		if(this.id == null)
			return false;
		
		return this.id.equals(other.id);
	}
	
	public Image getImage(double width, double height) {
		if(this.image == null)
			return null;
		
		return new Image(new ByteArrayInputStream(this.image), width, height, true, true);
	}
	
	public void addDeveloper(Developer developer) {
		this.developers.add(developer);
		developer.getProjects().add(this);
	}
	
	public void removeDeveloper(Developer developer) {
		this.developers.remove(developer);
		developer.getProjects().remove(this);
	}
	
	public void removeAllDevelopers() {
		for(Developer d : new ArrayList<Developer>(this.developers))
			this.removeDeveloper(d);
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		if(title == null || title.isBlank())
			throw new IllegalArgumentException("Der Titel darf nicht leer sein.");
		
		this.title = title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		if(description == null || description.isBlank())
			throw new IllegalArgumentException("Die Beschreibung darf nicht leer sein.");
		
		this.description = description;
	}
	
	public void setImage(byte[] data) {
		this.image = data;
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
