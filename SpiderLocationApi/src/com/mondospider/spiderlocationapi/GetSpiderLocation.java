package com.mondospider.spiderlocationapi;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class GetSpiderLocation extends HttpServlet {

    final JSONObject responseResp = new JSONObject(); 
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

        Location l = pm.getObjectById(Location.class, "spider");

        
        
        try {       
        	responseResp.put("latitude", l.getLatitude() );
        	responseResp.put("longitude", l.getLongitude());
        	responseResp.put("datemodified",l.getDateModified());
		
        	resp.setContentType("text/plain");
		
			resp.getWriter().println(responseResp.toString(2));
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	    
		resp.setContentType("text/plain");
	
	}
	
	
}
