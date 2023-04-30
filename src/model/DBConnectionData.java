package model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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
	
	private static String resourcePath = "";
	
	static {
		String fileSep = FileSystems.getDefault().getSeparator();
		String homeDir = System.getProperty("user.home");
		String dataDir = homeDir;
		
		if(!dataDir.endsWith(fileSep))
			dataDir += fileSep;
		
		dataDir += ".ProjektClient";
		
		File dataDirFile = new File(dataDir);
		if(!dataDirFile.exists())
			dataDirFile.mkdirs();
		
		resourcePath += dataDir + fileSep + "data.out";
	}
	
	public static DBConnectionData loadFromResource() {
		String url = null, user = null, password = null;
		
		try {
			Path overridesPath = new File(resourcePath).toPath();
			ObjectInputStream in = new ObjectInputStream(Files.newInputStream(overridesPath, StandardOpenOption.READ));
			
			url = (String)in.readObject();
			user = (String)in.readObject();
			
			in.close();
		} catch(IOException | ClassNotFoundException e) {}
		
		return new DBConnectionData(url, user, password);
	}
	
	public static void saveToResource(DBConnectionData data) throws IOException {
		try {
			Path overridesPath = new File(resourcePath).toPath();
	    	
	    	ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(overridesPath, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE));
	    	os.writeObject(data.getURL());
	    	os.writeObject(data.getUser());
	    	
	    	os.close();
		} catch(IOException e) {
			throw new IOException("Die Autorisierungsdaten konnten nicht lokal abgespeichert werden.");
		}
	}
	
}
