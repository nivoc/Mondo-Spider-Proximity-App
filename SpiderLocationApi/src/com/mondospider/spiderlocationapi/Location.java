package com.mondospider.spiderlocationapi;


import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Location represents a location object. Its maked as detachable b/c it's created on 
 * the server side and than transfered to the client.
 * @author matthias
 *
 */
@SuppressWarnings("serial")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable="true")
// Mark as Serializable to tell gwt that its serializable...
public class Location implements Serializable{

    @SuppressWarnings("unused")
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String encodedKey;
    

    @SuppressWarnings("unused")
    @Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
    private String keyName;

    // Necessary for the GWT-Serialization process
    @SuppressWarnings("unused")
	private Location() {
	}
    
    public Location(String username, double latitude,  double longitude) {
    	this.setUsername(username);
    	this.setLatetude(latitude);
    	this.setLongitude(longitude);
    	updateDateModified();
	}
    

	@Persistent
    private String username;

	@Persistent
    private double longitude;

    @Persistent
    private double latitude;
    
    @Persistent
    private Date dateModified;

    
	public void setLatetude(double latitude) {
		this.latitude = latitude;
		updateDateModified();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		updateDateModified();
	}

	public double getLongitude() {
		return longitude;
	}

	public void setUsername(String username) {
		this.username = username;
		this.keyName = username;
		
	}

	public String getUsername() {
		return username;
		
	}


	public Date getDateModified() {
		return dateModified;
	}
    

	private void updateDateModified() {
		dateModified = new Date();
	}
    

	
}
