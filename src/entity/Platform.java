package entity;

public enum Platform {

	Twitter,
	YouTube,
	Facebook,
	LinkedIn,
	Github,
	Email,
	Other;
	
	@Override
	public String toString() {
		return this.name();
	}
	
}
