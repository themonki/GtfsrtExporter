package com.mio.gtfsrt.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilException {

	public UtilException() {
		// TODO Auto-generated constructor stub
	}
	
	private static final Logger _log = LoggerFactory.getLogger(UtilException.class);
	
	private static void printException(Object o, Throwable cause, String mensaje, Level level){
		
		String stack = "";
		StringWriter errors = new StringWriter();
		cause.printStackTrace(new PrintWriter(errors));
		stack += "" + errors.toString();
		if(level == Level.INFO) {
			_log.info(mensaje + "\r\n" + stack);
		} else if(level == Level.WARNING){
			_log.warn(mensaje + "\r\n" + stack);
		} else if(level == Level.SEVERE){
			_log.error(mensaje + "\r\n" + stack);
		} else if(level == Level.CONFIG){
			_log.debug(mensaje + "\r\n" + stack);
		} else {
			_log.info(mensaje + "\r\n" + stack);
		}
		
	}
	
	public static void printSevereException(Object o, Throwable cause, String mensaje){
		printException(o, cause, mensaje, Level.SEVERE);
	}
	
	public static void printSevereException(Object o, Throwable cause){
		printSevereException(o, cause, "");
	}
	
	public static void printWarningException(Object o, Throwable cause, String mensaje){
		printException(o, cause, mensaje, Level.WARNING);
	}
	
	public static void printWarningException(Object o, Throwable cause){
		printWarningException(o, cause, "");
	}

}