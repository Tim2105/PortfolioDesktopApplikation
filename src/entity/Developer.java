package entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "DEVELOPER")
@SequenceGenerator(name="GEN_DEVELOPER_ID", initialValue = 0, allocationSize = 1)
public class Developer {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator = "GEN_DEVELOPER_ID")
	@Column(name = "ID")
	public Integer id;
	
	@Column(name = "FIRSTNAME")
	public String firstname;
	
	@Column(name = "LASTNAME")
	public String lastname;
	
	@Column(name = "DESCRIPTION")
	public String description;
	
	@OneToMany(mappedBy = "developer", cascade = CascadeType.ALL)
	public List<ContactOpportunity> contactOpportunities;
	
	@ManyToMany(mappedBy = "developers")
	public List<Project> projects;
	
	public Developer(String firstname, String lastname, String description) {
		if(firstname == null || firstname.isBlank())
			throw new IllegalArgumentException("Der Vorname darf nicht leer sein.");
		
		if(lastname == null || lastname.isBlank())
			throw new IllegalArgumentException("Der Nachname darf nicht leer sein.");
		
		if(description == null || description.isBlank())
			throw new IllegalArgumentException("Die Beschreibung darf nicht leer sein.");
		
		this.firstname = firstname;
		this.lastname = lastname;
		this.description = description;
		this.contactOpportunities = new ArrayList<ContactOpportunity>();
		this.projects = new ArrayList<Project>();
	}
	
	public Developer() {
		this.contactOpportunities = new ArrayList<ContactOpportunity>();
		this.projects = new ArrayList<Project>();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof Developer))
			return false;
		
		Developer other = (Developer)obj;
		
		if(this.id == null)
			return false;
		
		return this.id.equals(other.id);
	}
	
	public void addContactOpportunity(ContactOpportunity contactOpportunity) {
		this.contactOpportunities.add(contactOpportunity);
		contactOpportunity.setDeveloper(this);
	}
	
	public void removeContactOpportunity(ContactOpportunity contactOpportunity) {
		if(this.contactOpportunities.remove(contactOpportunity))
			contactOpportunity.setDeveloper(null);
	}
	
	public void addProject(Project project) {
		this.projects.add(project);
		project.getDevelopers().add(this);
	}
	
	public void removeProject(Project project) {
		this.projects.remove(project);
	}
	
	public String getFirstname() {
		return this.firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<ContactOpportunity> getContactOpportunities() {
		return this.contactOpportunities;
	}
	
	public List<Project> getProjects() {
		return this.projects;
	}
	
}
