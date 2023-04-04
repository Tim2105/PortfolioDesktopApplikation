package entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DEMONSTRATION_FILE")
public class DemonstrationFile {
	
	@Id
	@ManyToOne
	@JoinColumn(name = "ID", referencedColumnName = "ID")
	public Demonstration demonstration;
	
	@Id
	@Column(name = "FILENAME")
	public String filename;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "FILE")
	public byte[] file;
	
	public DemonstrationFile(String filename, File file) throws IOException {
		if(filename == null || filename.isBlank())
			throw new IllegalArgumentException("Der Dateiname darf nicht leer sein.");
		
		if(file == null)
			throw new IllegalArgumentException("Geben Sie eine Datei an");
		
		this.filename = filename;
		
		if(!file.canRead())
			throw new IOException("Für diese Datei besitzt das Programm keine Leseberechtigungen.");
		
		if(!file.isFile())
			throw new IllegalArgumentException("Der angegebene Pfad führt zu keiner Datei.");
		
		try { this.file = Files.readAllBytes( file.toPath()); }
		catch(IOException e) {
			throw new IOException("Beim Lesen der Datei ist ein Fehler aufgetreten. Stellen Sie sicher, dass die Datei noch existiert und Sie Leseberechtigungen besitzen.");
		} catch(Exception e) {
			throw new RuntimeException("Beim Lesen der Datei ist ein unbekannter Fehler aufgetreten.");
		}
	}
	
	public DemonstrationFile() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof DemonstrationFile))
			return false;
		
		DemonstrationFile other = (DemonstrationFile)obj;
		
		if(this.demonstration == null ||
				this.demonstration.id == null ||
				other.demonstration == null)
			return false;
		
		return this.demonstration.id.equals(other.demonstration.id);
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Demonstration getDemonstration() {
		return demonstration;
	}

	public void setDemonstration(Demonstration demonstration) {
		this.demonstration = demonstration;
	}
	
	public byte[] getFile() {
		return file;
	}
	
}
