package dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "DEVELOPER")
@SequenceGenerator(name="GEN_DEVELOPER_ID", initialValue = 1, allocationSize = 1)
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
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "DEVELOPER", referencedColumnName = "ID")
	private List<ContactOpportunity> contactOpportunities;
	
	@ManyToMany(mappedBy = "developers", fetch = FetchType.EAGER)
	private List<Project> projects;
	
	public Developer(String firstname, String lastname, String description) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.description = description;
		this.contactOpportunities = new ArrayList<ContactOpportunity>();
		this.projects = new ArrayList<Project>();
	}
	
	public void addContactOpportunity(ContactOpportunity contactOpportunity) {
		if(!this.contactOpportunities.contains(contactOpportunity))
			this.contactOpportunities.add(contactOpportunity);
	}
	
	public void removeContactOpportunity(ContactOpportunity contactOpportunity) {
		this.contactOpportunities.remove(contactOpportunity);
	}
	
	public void addProject(Project project) {
		if(!this.projects.contains(project))
			this.projects.add(project);
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
