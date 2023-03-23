package entity;

public enum Platform {

	Twitter,
	Facebook,
	LinkedIn,
	Github,
	Email,
	Phone,
	Other;
	
	@Override
	public String toString() {
		return this.name();
	}
	
}
