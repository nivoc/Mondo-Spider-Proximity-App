package com.mondospider.spiderlocationapi;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class GetSpiderLocation extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

        Location l = pm.getObjectById(Location.class, "spider");


		resp.setContentType("text/plain");
		resp.getWriter().println(
				"{" + "\n\"latitude\":" + l.getLatitude() + ","
						+ "\n\"longitude\":" + l.getLongitude() + ","
						+ "\n\"datemodified\":" + l.getDateModified() + ","
						+ "\n}");
	}
	
	
}
