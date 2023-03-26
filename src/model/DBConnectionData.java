package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DBConnectionData {

	private String url;
	
	private String user;
	
	private String password;
	
	public DBConnectionData(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	private static final String resourcePath = "/META-INF/overrides.out";
	
	public static DBConnectionData loadFromResource() {
		String url = null, user = null, password = null;
		
		try {
			Path overridesPath = Paths.get(DBConnectionData.class.getResource(resourcePath).toURI());
			ObjectInputStream in = new ObjectInputStream(Files.newInputStream(overridesPath, StandardOpenOption.READ));
			
			url = (String)in.readObject();
			user = (String)in.readObject();
			password = (String)in.readObject();
			
			in.close();
		} catch(IOException | URISyntaxException | ClassNotFoundException e) {}
		
		return new DBConnectionData(url, user, password);
	}
	
	public static void saveToResource(DBConnectionData data) throws IOException {
		try {
			Path overridesPath = Paths.get(DBConnectionData.class.getResource(resourcePath).toURI());
	    	
	    	ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(overridesPath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING));
	    	os.writeObject(data.getURL());
	    	os.writeObject(data.getUser());
	    	os.writeObject(data.getPassword());
	    	
	    	os.close();
		} catch(URISyntaxException | IOException e) {
			throw new IOException("Die Autorisierungsdaten konnten nicht abgespeichert werden.");
		}
	}
	
}
