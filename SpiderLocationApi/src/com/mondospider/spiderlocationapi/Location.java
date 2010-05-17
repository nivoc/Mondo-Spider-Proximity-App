/*
 * Copyright (C) 2010 The Mondospider Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    
    public Location(String username, double latitude,  double longitude, String status) {
    	this.setUsername(username);
    	this.setLatetude(latitude);
    	this.setLongitude(longitude);
    	this.setStatus(status);
    	updateDateModified();
	}
    

	@Persistent
    private String username;

	@Persistent
    private double longitude;

    @Persistent
    private double latitude;
    
    @Persistent
    private String status;
    
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

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	
}
