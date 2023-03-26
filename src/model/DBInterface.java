package model;

import entity.Developer;
import entity.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DBInterface {

	private static DBInterface instance;
	
	public static DBInterface getInstance() {
		if(instance == null)
			instance = new DBInterface();
		
		return instance;
	}
	
	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	private ObservableList<Project> projects;
	
	private ObservableList<Developer> developers;
	
	private DBInterface() {
		try {
			this.emf = Persistence.createEntityManagerFactory("Database");
			this.em = this.emf.createEntityManager();
		} catch(Exception e) {
			throw new IllegalStateException("Es konnte keine Datenbankverbindung aufgebaut werden.", e);
		}
		
		this.projects = FXCollections.observableArrayList();
		this.developers = FXCollections.observableArrayList();
		
		this.loadFromDB();
	}
	
	private void loadFromDB() {
		TypedQuery<Project> projectQuery = this.em.createQuery("select p from Project p", Project.class);
		this.projects.clear();
		this.projects.addAll(projectQuery.getResultList());
		
		TypedQuery<Developer> developerQuery = this.em.createQuery("select d from Developer d", Developer.class);
		this.developers.clear();
		this.developers.addAll(developerQuery.getResultList());
	}
	
	public void refresh() {
		this.em.close();
		
		this.emf.getCache().evictAll();
		this.em = this.emf.createEntityManager();
		this.loadFromDB();
	}
	
	public void refresh(Object entity) {
		if(entity != null)
			this.em.refresh(entity);
	}
	
	public ObservableList<Project> getProjects() {
		return this.projects;
	}

	public ObservableList<Developer> getDevelopers() {
		return this.developers;
	}
	
	public EntityManager createEntityManager() {
		return this.emf.createEntityManager();
	}
}
