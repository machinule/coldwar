package com.berserkbentobox.coldwar;

import com.badlogic.gdx.Gdx;

/**
 * Wrapper around the libGDX application logger for easy access.
 * @see com.badlogic.gdx.Application
 */
public class Logger {

	public static void setLogLevel(int level) {
		Gdx.app.setLogLevel(level);
	}

	private static String getClassName() {
		return String.format("%1$-" + 25 + "s", Thread.currentThread().getStackTrace()[3].getFileName());		
	}
	
	private static String getTimestamp() {
		return new java.util.Date().toString();
	}
	
	private static String formatMessage(final String msg) {
		StringBuilder b = new StringBuilder(Logger.getTimestamp());
		b.append(" : ");
		b.append(msg);
		return b.toString();
	}
	
	public static void Dbg(final String msg) {
		Gdx.app.debug(Logger.getClassName(), Logger.formatMessage(msg));
	}

	public static void Err(final String msg) {
		Gdx.app.error(Logger.getClassName(), Logger.formatMessage(msg));
	}

	public static void Info(final String msg) {
		Gdx.app.log(Logger.getClassName(), Logger.formatMessage(msg));
	}

	public static void Vrb(final String msg) {
		Gdx.app.debug(Logger.getClassName(), Logger.formatMessage(msg));
	}

	public static void Warn(final String msg) {
		Gdx.app.log(Logger.getClassName(), Logger.formatMessage(msg));
	}
}
