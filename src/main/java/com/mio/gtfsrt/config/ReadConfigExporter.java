package com.mio.gtfsrt.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mio.gtfsrt.exception.ConfigException;

public class ReadConfigExporter extends ReadConfig {
	
	public static final String EXPORTER_BASIC_AUTH= "basic.auth.use";
	public static final String FILE_AUTH = "basic.auth.file";
	public static final String URL_PATH_PATTERN_AUTH = "basic.auth.path.pattern";
	
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(ReadConfigExporter.class);
	
	public ReadConfigExporter() throws ConfigException{
		super();
		readPropertyFile();		
	}
	
	public boolean getExporterBasicAuth(){
		return Boolean.parseBoolean((String)getProperties(EXPORTER_BASIC_AUTH));
	}
	
	public boolean isExporterBasicAuth(){
		return existProperty(EXPORTER_BASIC_AUTH);
	}
	
	public String getFileAuth(){
		return (String)getProperties(FILE_AUTH);
	}
	
	public boolean isFileAuth(){
		return existProperty(FILE_AUTH);
	}
	
	public String getURLPatternAuth(){
		return (String)getProperties(URL_PATH_PATTERN_AUTH);
	}
	
	public boolean isURLPatternAuth(){
		return existProperty(URL_PATH_PATTERN_AUTH);
	}
}
