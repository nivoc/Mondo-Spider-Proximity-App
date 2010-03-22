package com.mondospider.spiderlocationapi;

import java.io.IOException;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.sun.servicetag.UnauthorizedAccessException;

@SuppressWarnings("serial")
public class SetSpiderLocation extends HttpServlet {
	private static final String SECRET_KEY = "spiders_secret_key";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		
		
		// Permission Check
		String key = req.getParameter("pwd");
		if ( !isSecretKeyTrue(key) )  {
			//accessDenied		
			resp.getWriter().println("{\"result:error\", \"reason\":\"Wrong Key\"}");
			return;
		}
		

		// Get lat/lng Parameters and validate
		double lat = 0;
		double lng = 0;
		
		try {
			lat = Double.parseDouble(req.getParameter("lat"));	
			lng = Double.parseDouble(req.getParameter("lng"));
		} catch (NumberFormatException e) {
			resp.getWriter().println(" {\"result:error\", \"reason\":\"Parameter lat or log missing or invalid\" }");
			return;
		}

		//Update Position
		updatePosition("spider", lat, lng);
		resp.getWriter().println("{\"result:success\", \"note\":\"Updated on "+new Date()+"\" }");
		
	}
	

	private boolean isSecretKeyTrue(String key) {
		return key.equals(SECRET_KEY);
	}
	
	

	public void updatePosition(String username, double latitude,  double longitude) {
		
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    Location l = new Location(username, latitude, longitude);
	    
	    
	    try {
	    	pm.makePersistent(l);
	    } finally {
	        pm.close();
	    }
	}
	
}
