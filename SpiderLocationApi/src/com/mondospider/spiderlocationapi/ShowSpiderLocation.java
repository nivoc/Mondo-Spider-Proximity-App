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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ShowSpiderLocation extends HttpServlet {
	private static final String SECRET_KEY = "spiders_secret_key";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		resp.setContentType("text/html");
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Location l = pm.getObjectById(Location.class, "spider");

		resp.getWriter().println(
						"<html><head><meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />"
								+ "<script type=\"text/javascript\" src=\"http://maps.google.com/maps/api/js?sensor=false\"></script>"
								+ "<script type=\"text/javascript\">"
								+ "	function initialize() {"
								+ "		var latlng = new google.maps.LatLng("+l.getLatitude()+", "+l.getLongitude()+");"
								+ "		var myOptions = {"
								+ "		zoom: 8,"
								+ " 		center: latlng,"
								+ " 		mapTypeId: google.maps.MapTypeId.ROADMAP"
								+ "		};"
								+ "		var map = new google.maps.Map(document.getElementById(\"map_canvas\"), myOptions);"
								+ "     var marker = new google.maps.Marker({"
								+ " 		position: latlng, "
								+ "			map: map,"
								+ " 		title:\"Spiders current location!\""
								+ "		}); "
								+ " };"
								+ "</script>"
								+ "</head>"
								+ "<body onload=\"initialize()\"><h1 style='text-align:center;font-family:arial'>Spider's current location</h1><div id=\"map_canvas\" style=\"width:80%; height:80%; margin:auto\"></div></body></html>");

	}
}
