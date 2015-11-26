/**
 * Copyright (C) 2011 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.onebusway.gtfsrealtime.exporter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onebusaway.guice.jetty_exporter.ServletSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;
import com.mio.gtfsrt.config.ReadConfigExporter;
import com.mio.gtfsrt.exception.ConfigException;
import com.mio.secure.httpd.Htpasswd;

abstract class AbstractGtfsRealtimeServlet extends HttpServlet implements
    ServletSource {

  private static final long serialVersionUID = 1L;

  private static final String CONTENT_TYPE = "application/x-google-protobuf";

  protected GtfsRealtimeProvider _provider;
  
  private boolean _useHttpBasicAuth = false;
  
  private URL _url;
  
  private static final Logger _log = LoggerFactory.getLogger(AbstractGtfsRealtimeServlet.class);

  public void setProvider(GtfsRealtimeProvider provider) {
    _provider = provider;
  }

  public void setUrl(URL url) {
    _url = url;
  }

  /****
   * {@link HttpServlet} Interface
   ****/

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
	try{
		if(!authUser(req)){
			// Not allowed, so report he's unauthorized
	        resp.setHeader("WWW-Authenticate", "BASIC realm=\"Auth (" + req.getSession().getCreationTime() + ")\"");
	        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	        req.getSession().setAttribute("auth",Boolean.TRUE);
	        return;
		}
	} catch(ConfigException e){
		//dont data for use auth
		_log.warn("On load config", e);
	}
    boolean debug = req.getParameter("debug") != null;
    Message message = getMessage();
    if (debug) {
      resp.getWriter().print(message);
    } else {
      resp.setContentType(CONTENT_TYPE);
      message.writeTo(resp.getOutputStream());
    }
  }
  
	private boolean authUser(HttpServletRequest req) throws ConfigException {
		boolean valid = false;
		String resource = req.getRequestURI();
		ReadConfigExporter configExporter = new ReadConfigExporter();
		if (configExporter.isExporterBasicAuth()) {
			this._useHttpBasicAuth = configExporter.getExporterBasicAuth();
		}
		
		if (!this._useHttpBasicAuth ) {
			// dont use basic auth
			return true;
		}
		
		String htpasswdFile = null;
		
		if(configExporter.isFileAuth()){
			htpasswdFile = configExporter.getFileAuth();
		}
		
		if(htpasswdFile == null || !new File(htpasswdFile).canRead()){
			//no file to compare credential
			return true;
		}

		String folder =  null;
		
		if(configExporter.isURLPatternAuth()){
			folder = configExporter.getURLPatternAuth();
		}
		
		//evaluet pattern
		if(folder != null && !resource.startsWith(folder)){
			// the path dont match, dont use basic auth
			return true;
		}

		// Get Authorization header
		String auth = req.getHeader("Authorization");
		if (auth == null || !auth.toUpperCase().startsWith("BASIC ")) {
			// only basic auth
			return false;
		}

		final String encodedUserPassword = auth.replaceFirst("Basic" + " ", "");
		String usernameAndPassword = null;
		byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
		usernameAndPassword = new String(decodedBytes, StandardCharsets.UTF_8);
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		
		if(tokenizer.countTokens() != 2){
			return false;
		}
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();
				
		Htpasswd htpasswd = new Htpasswd(htpasswdFile);
		try {
			valid = htpasswd.authorized(username, password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			_log.warn("On authorized", e);
		}
		
		return valid;
	}

  /****
   * {@link ServletSource} Interface
   ****/

  @Override
  public URL getUrl() {
    return _url;
  }

  @Override
  public Servlet getServlet() {
    return this;
  }

  /****
   * Protected Methods
   ****/

  /**
   * Override this method to return the protocol buffer
   * 
   * @return
   */
  protected abstract Message getMessage();
}
