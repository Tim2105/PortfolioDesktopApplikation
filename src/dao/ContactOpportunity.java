package dao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "CONTACT_OPPORTUNITY")
@SequenceGenerator(name="GEN_CONTACT_OPPORTUNITY_ID", initialValue = 1, allocationSize = 1)
public class ContactOpportunity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator = "GEN_CONTACT_OPPORTUNITY_ID")
	@Column(name = "ID")
	private int id;
	
	@Column(name = "PLATFORM")
	private Platform platform;
	
	@Column(name = "URL")
	private String url;
	
	public ContactOpportunity(Platform platform, String url) {
		this.platform = platform;
		this.url = url;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof ContactOpportunity))
			return false;
		
		return ((ContactOpportunity)other).id == this.id;
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
	
}
