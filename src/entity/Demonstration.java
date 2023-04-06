package entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "DEMONSTRATION")
@SequenceGenerator(name = "GEN_DEMONSTRATION_ID", initialValue = 0, allocationSize = 1)
public class Demonstration {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator = "GEN_DEMONSTRATION_ID")
	@Column(name = "ID")
	public Integer id;
	
	@Column(name = "NAME")
	public String name;
	
	@Column(name = "WELCOME_PAGE")
	public String welcomePage;
	
	@OneToMany(mappedBy = "demonstration", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<DemonstrationFile> files;
	
	public Demonstration(String name, String welcomePage) {
		if(name == null || name.isBlank())
			throw new IllegalArgumentException("Der Name darf nicht leer sein.");
		
		if(welcomePage == null || welcomePage.isBlank())
			throw new IllegalArgumentException("Die Eintrittseite darf nicht leer sein.");
		
		this.name = name;
		this.welcomePage = welcomePage;
		this.files = new ArrayList<DemonstrationFile>();
	}
	
	public Demonstration(String name, DemonstrationFile welcomePage) {
		if(name == null || name.isBlank())
			throw new IllegalArgumentException("Der Name darf nicht leer sein.");
		
		if(welcomePage == null)
			throw new IllegalArgumentException("Geben Sie eine Eintrittsseite an.");
		
		if(welcomePage.getFilename() == null || welcomePage.getFilename().isBlank())
			throw new IllegalArgumentException("Die Eintrittseite darf nicht leer sein.");
		
		this.name = name;
		this.welcomePage = welcomePage.getFilename();
		this.files = new ArrayList<DemonstrationFile>();
	}
	
	public Demonstration() {
		this.files = new ArrayList<DemonstrationFile>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof Demonstration))
			return false;
		
		Demonstration other = (Demonstration)obj;
		
		if(this.id == null)
			return false;
		
		return this.id.equals(other.id);
	}
	
	public void addFile(DemonstrationFile file) {
		files.add(file);
		file.setDemonstration(this);
	}
	
	public void removeFile(DemonstrationFile file) {
		this.files.remove(file);
	}
	
	public void removeAllFiles() {
		for(DemonstrationFile file : new ArrayList<DemonstrationFile>(this.files))
			this.removeFile(file);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name == null || name.isBlank())
			throw new IllegalArgumentException("Der Name darf nicht leer sein.");
		
		this.name = name;
	}

	public String getWelcomePage() {
		return welcomePage;
	}

	public void setWelcomePage(String welcomePage) {
		if(welcomePage == null || welcomePage.isBlank())
			throw new IllegalArgumentException("Die Eintrittseite darf nicht leer sein.");
		
		this.welcomePage = welcomePage;
	}
	
	public void setWelcomePage(DemonstrationFile welcomePage) {
		if(welcomePage == null)
			throw new IllegalArgumentException("Geben Sie eine Eintrittsseite an.");
		
		if(welcomePage.getFilename() == null || welcomePage.getFilename().isBlank())
			throw new IllegalArgumentException("Die Eintrittseite darf nicht leer sein.");
		
		this.welcomePage = welcomePage.getFilename();
	}
	
	public List<DemonstrationFile> getFiles() {
		return this.files;
	}
	
}
