package com.mio.gtfsrt.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mio.gtfsrt.exception.ConfigException;
import com.mio.gtfsrt.exception.UtilException;

public abstract  class ReadConfig {
	private Properties properties;
	private String configFile;
	private static String DEFAULT_CONFIG_FILE = "config.properties";
	public static String CONFIG_FILE = null;
	private static final Logger _log = LoggerFactory.getLogger(ReadConfigExporter.class);
	
	public ReadConfig (){
		this.properties = new Properties();
		if(CONFIG_FILE != null){
			this.configFile = CONFIG_FILE;
		} else {
			this.configFile = DEFAULT_CONFIG_FILE;
		}
	}
	
	public ReadConfig (String configLogFile){
		this.properties = new Properties();
		this.configFile = configLogFile;
	}
	
	/**
	 * Lee el archivo de configuracion, si el archivo de entrada contiene algun error,
	 * se intenta con la ubicacion por defecto.
	 * @param configFile
	 * @throws ConfigException
	 */
	public void readPropertyFile(String configLogFile) throws ConfigException{
		InputStream input = null;
		try {
			input = new FileInputStream(configLogFile);
			// load a properties file
			this.properties.load(input);
		} catch (IOException ex) {
			if(getClass().getResource("/" + DEFAULT_CONFIG_FILE).getFile().isEmpty() && configLogFile.equals(DEFAULT_CONFIG_FILE)) {
				throw new ConfigException(ex, ConfigException.ExceptionTypes.UNABLE_READ_CONFIG_FILE);
			} else {
				UtilException.printWarningException(this, ex, "Error leyendo el configFile: "+ configLogFile);
				_log.info("Se intentara con la opcion por defecto: " + DEFAULT_CONFIG_FILE);
				input = null;
			}
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new ConfigException(e, ConfigException.ExceptionTypes.UNABLE_CLOSE_CONFIG);
				}
			}
		}
		if(input != null || (getClass().getResource("/" + DEFAULT_CONFIG_FILE).getFile().isEmpty() && configLogFile.equals(DEFAULT_CONFIG_FILE))){
			return;
		}
		try {
			input = getClass().getResourceAsStream("/" + DEFAULT_CONFIG_FILE);
			// load a properties file
			this.properties.load(input);
		} catch (IOException ex) {
			throw new ConfigException(ex, ConfigException.ExceptionTypes.UNABLE_READ_CONFIG_FILE);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new ConfigException(e, ConfigException.ExceptionTypes.UNABLE_CLOSE_CONFIG);
				}
			}
		}
	}
	
	public void readPropertyFile() throws ConfigException{
		readPropertyFile(this.configFile);
	}
	
	public boolean existProperty(String prop){
		return properties.containsKey(prop);
	}
	
	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return properties;
	}
	/**
	 * @param properties the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	/**
	 * @return the properties
	 */
	public Object getProperties(String prop) {
		return properties.getProperty(prop);
	}
}
