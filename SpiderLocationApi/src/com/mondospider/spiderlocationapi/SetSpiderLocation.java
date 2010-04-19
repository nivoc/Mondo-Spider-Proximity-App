package com.mondospider.spiderlocationapi;

import java.io.IOException;
import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("serial")
public class SetSpiderLocation extends HttpServlet {
	private static String secretKey = null;

	final JSONObject responseResp = new JSONObject();

	public SetSpiderLocation() {

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

		// Permission Check
		String key = req.getParameter("pwd");
		if (!isSecretKeyTrue(key)) {
			// accessDenied
			resp.getWriter().println(
					"{\"result:error\", \"reason\":\"Wrong Key\"}");
			return;
		}

		try {

			// Get lat/lng Parameters and validate
			double lat = 0;
			double lng = 0;

			try {
				lat = Double.parseDouble(req.getParameter("lat"));
				lng = Double.parseDouble(req.getParameter("lng"));
			} catch (NumberFormatException e) {
				responseResp.put("result", "error");
				responseResp.put("reason",
						"Parameter lat or log missing or invalid");

				resp.getWriter().println(responseResp.toString(2));

				return;
			}

			// Update Position
			updatePosition("spider_neu", lat, lng);

			responseResp.put("result", "success");
			responseResp.put("updated_on", new Date());

			resp.setContentType("text/plain");

			resp.getWriter().println(responseResp.toString(2));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean isSecretKeyTrue(String key) {
		return key.equals(secretKey);
	}

	public void updatePosition(String username, double latitude,
			double longitude) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Location l = new Location(username, latitude, longitude);

		try {
			pm.makePersistent(l);
		} finally {
			pm.close();
		}
	}

}
