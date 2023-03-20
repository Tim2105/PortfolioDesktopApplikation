package dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	private int id;
	
	@Column(name = "FIRSTNAME")
	private String firstname;
	
	@Column(name = "LASTNAME")
	private String lastname;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@OneToMany(mappedBy = "developer", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<ContactOpportunity> contactOpportunities;
	
	@ManyToMany(mappedBy = "developers", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private List<Project> projects;
	
	public Developer(String firstname, String lastname, String description) {
		if(firstname == null)
			throw new IllegalArgumentException("Der Vorname darf nicht null sein.");
		
		if(lastname == null)
			throw new IllegalArgumentException("Der Nachname darf nicht null sein.");
		
		if(description == null)
			throw new IllegalArgumentException("Die Beschreibung darf nicht null sein.");
		
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
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Developer))
			return false;
		
		return ((Developer)other).id == this.id;
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
