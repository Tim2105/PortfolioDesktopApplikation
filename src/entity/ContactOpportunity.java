package entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTACT_OPPORTUNITY")
@SequenceGenerator(name="GEN_CONTACT_OPPORTUNITY_ID", initialValue = 0, allocationSize = 1)
public class ContactOpportunity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator = "GEN_CONTACT_OPPORTUNITY_ID")
	@Column(name = "ID")
	public Integer id;
	
	@Column(name = "PLATFORM")
	public Platform platform;
	
	@Column(name = "URL")
	public String url;
	
	@ManyToOne(cascade= CascadeType.ALL)
	@JoinColumn(name = "DEVELOPER")
	public Developer developer;
	
	public ContactOpportunity(Platform platform, String url) {
		if(platform == null)
			throw new IllegalArgumentException("Die Plattform darf nicht leer sein");
		
		if(url == null || url.isBlank())
			throw new IllegalArgumentException("Die URL darf nicht leer sein");
		
		this.platform = platform;
		this.url = url;
	}
	
	public ContactOpportunity() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof ContactOpportunity))
			return false;
		
		ContactOpportunity other = (ContactOpportunity)obj;
		
		if(this.id == null)
			return false;
		
		return this.id.equals(other.id);
	}
	
	public Platform getPlatform() {
		return this.platform;
	}
	
	public void setPlatform(Platform platform) {
		this.platform = platform;
	}
	
	public String getURL() {
		return this.url;
	}
	
	public void setURL(String url) {
		this.url = url;
	}
	
	public Developer getDeveloper() {
		return this.developer;
	}
	
	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}
	
}
