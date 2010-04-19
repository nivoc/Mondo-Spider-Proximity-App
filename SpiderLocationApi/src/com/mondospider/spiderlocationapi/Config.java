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
@PersistenceCapable(identityType = IdentityType.APPLICATION)
// Mark as Serializable to tell gwt that its serializable...
public class Config implements Serializable{

    @SuppressWarnings("unused")
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String encodedKey;
    

    @SuppressWarnings("unused")
    @Persistent
    @Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
    private String keyName;

    public Config() {
    	keyName="pref";
	}
    

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}


	public String getSecretKey() {
		return secretKey;
	}


	@Persistent
    private String secretKey;


	
}
