package entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
	private int id;
	
	@Column(name = "PLATFORM")
	private Platform platform;
	
	@Column(name = "URL")
	private String url;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.PERSIST)
	@JoinColumn(name = "DEVELOPER")
	private Developer developer;
	
	public ContactOpportunity(Platform platform, String url) {
		this.platform = platform;
		this.url = url;
	}
	
	public ContactOpportunity() {
		
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
	
	public Developer getDeveloper() {
		return this.developer;
	}
	
	public void setDeveloper(Developer developer) {
		this.developer = developer;
	}
	
}
