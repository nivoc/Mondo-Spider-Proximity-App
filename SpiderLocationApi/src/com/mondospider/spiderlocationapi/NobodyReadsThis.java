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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class NobodyReadsThis extends HttpServlet {
	private static String secretKey = null;

	final JSONObject responseResp = new JSONObject();

	public NobodyReadsThis() {

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {


		

		PersistenceManager pm = PMF.get().getPersistenceManager();
		NobodyReaderCount c;
		try {
			c = pm.getObjectById(NobodyReaderCount.class, "readerCount");
		} catch (JDOObjectNotFoundException e) {
        	c = new NobodyReaderCount();
		}
			
        
        c.increase();
        
        resp.getWriter().println("<html><body><b>:) You won - I was wrong. You are the number <span style='color:red'>"+ c.getCount() +"</span> that clicked this link </b><h1 style='text-transform:uppercase'>&gt;:)</h1></b></body>");
		
		try {
			pm.makePersistent(c);
		} finally {
			pm.close();
		}

	}

}
