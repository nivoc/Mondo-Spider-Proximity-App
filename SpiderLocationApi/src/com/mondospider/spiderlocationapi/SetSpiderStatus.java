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

import java.io.IOException;
import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.naming.ConfigurationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class SetSpiderStatus extends HttpServlet {
	private static String secretKey = null;

	final JSONObject responseResp = new JSONObject();

	public SetSpiderStatus() {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Config c;
		try {
			c = pm.getObjectById(Config.class, "pref");

		} catch (JDOObjectNotFoundException e) {
			c = new Config();
			c.setSecretKey("defaultSecretKey");
			pm.makePersistent(c);
		} finally {
			pm.close();
		}

		secretKey = c.getSecretKey();

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		if(!req.isSecure()) {
			resp.getWriter().println(
			"{\"result:error\", \"reason\":\"SSL secured connection required for updates.\"}");
			return;
		}
		
		if (req.getParameter("status")==null) {
			//Display Error&Help
			try {
				req.getRequestDispatcher("/index.html").forward(req,resp);
			} catch (ServletException e) {
				resp.getWriter().println(
				"{\"result:error\", \"reason\":\"Forward failed\"}");
				e.printStackTrace();
				return;
			}
		}
		
		// Permission Check
		String key = req.getParameter("pwd");
		if (!isSecretKeyTrue(key)) {
			// accessDenied
			resp.getWriter().println(
					"{\"result:error\", \"reason\":\"Wrong Key\"}");
			return;
		}

		try {

			
			String status = req.getParameter("status");
			
			
			// Update Position
			updateStatus(status);

			responseResp.put("result", "success");
			responseResp.put("updated_on", new Date());

			resp.setContentType("text/plain");

			resp.getWriter().println(responseResp.toString(2));
			return;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		resp.sendRedirect("/index.html");

	}

	private boolean isSecretKeyTrue(String key) {
		return key.equals(secretKey);
	}

	public void updateStatus(String status)  {

		PersistenceManager pm = PMF.get().getPersistenceManager();

        Location l = pm.getObjectById(Location.class, "spider");
       
        if (l==null){
        	 throw new IllegalStateException("spider-object not found");
        }
        
        if (status != null && status.length() > 0) {
        	l.setStatus(status);
        }
        
		try {
			pm.makePersistent(l);
		} finally {
			pm.close();
		}
	}

}
