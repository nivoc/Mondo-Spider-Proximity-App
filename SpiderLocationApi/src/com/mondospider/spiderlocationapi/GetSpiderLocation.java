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
        	responseResp.put("next_update_in", 5);
        	
        	resp.setContentType("text/plain");
		
			resp.getWriter().println(responseResp.toString(2));
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    
	    
		resp.setContentType("text/plain");
	
	}
	
	
}
