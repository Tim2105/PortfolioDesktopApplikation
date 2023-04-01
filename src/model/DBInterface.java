package model;

import java.util.HashMap;
import java.util.Map;

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
	
	public static void reloadConnection(DBConnectionData data) {
		instance = new DBInterface(data);
	}
	
	private EntityManagerFactory emf;
	
	private EntityManager em;
	
	private ObservableList<Project> projects;
	
	private ObservableList<Developer> developers;
	
	private DBInterface() {
		this(DBConnectionData.loadFromResource());
	}
	
	private DBInterface(DBConnectionData data) {
		this.connect(data.getURL(), data.getUser(), data.getPassword());
	}
	
	private void connect(String url, String user, String password) {
		try {
			Map<String, String> overrides = new HashMap<String, String>();
			if(url != null)
				overrides.put("jakarta.persistence.jdbc.url", url + "?useUnicode=true&encoding=utf8");
			
			if(user != null)
				overrides.put("jakarta.persistence.jdbc.user", user);
			
			if(password != null)
				overrides.put("jakarta.persistence.jdbc.password", password);
			
			this.emf = Persistence.createEntityManagerFactory("Database", overrides);
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
		if(entity != null) {
			if(!this.em.contains(entity))
				this.em.persist(entity);
			
			this.em.refresh(entity);
		}
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
	
	public DBConnectionData getConnectionData() {
		return new DBConnectionData(
					(String)this.emf.getProperties().get("jakarta.persistence.jdbc.url"),
					(String)this.emf.getProperties().get("jakarta.persistence.jdbc.user"),
					(String)this.emf.getProperties().get("jakarta.persistence.jdbc.password")
				);
	}
}
