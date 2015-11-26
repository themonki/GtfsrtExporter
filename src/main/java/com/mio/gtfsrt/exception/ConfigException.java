package com.mio.gtfsrt.exception;

public class ConfigException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8133587976879648061L;
	String customMessage = "";

	public enum ExceptionTypes {
		UNABLE_READ_CONFIG_FILE, UNABLE_CLOSE_CONFIG, PARAMETER_REQUIRED_DONT_EXIST, INVALID_VALUE_FOR_PARAMETER;
	}

	public ConfigException() {
		// TODO Auto-generated constructor stub
	}

	public ConfigException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ConfigException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ConfigException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public ConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public ConfigException(String message, ExceptionTypes type) {
		super(message);
		// TODO Auto-generated constructor stub
		this.customMessage = processExpetion(type);
	}

	public ConfigException(Throwable cause, ExceptionTypes type) {
		super(cause);
		// TODO Auto-generated constructor stub
		this.customMessage = processExpetion(type);
	}

	public ConfigException(String message, Throwable cause, ExceptionTypes type) {
		super(message, cause);
		// TODO Auto-generated constructor stub
		this.customMessage = processExpetion(type);
	}

	public ConfigException(ExceptionTypes type) {
		super();
		// TODO Auto-generated constructor stub
		this.customMessage = processExpetion(type);
	}

	public String getCustomMessage() {
		return this.customMessage;
	}

	private String processExpetion(ExceptionTypes type) {
		String mensaje = "";

		switch (type) {
		case UNABLE_READ_CONFIG_FILE:
			mensaje = "Hubo un error al cargar el archivo de configuracion";
			break;
		case UNABLE_CLOSE_CONFIG:
			mensaje = "Hubo un error al cerrar el archivo de configuracion";
			break;
		case PARAMETER_REQUIRED_DONT_EXIST:
			mensaje = "Se requiere un parametro y no fue encontrado en el archivo config.properties o referenciado";
			break;
		case INVALID_VALUE_FOR_PARAMETER:
			mensaje = "El parametro tiene un valor invalido o no es aceptable";
			break;
		default:
			break;
		}
		return mensaje;
	}
}
