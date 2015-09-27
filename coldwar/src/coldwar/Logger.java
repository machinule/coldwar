package coldwar;

import java.io.PrintWriter;
import java.sql.Timestamp;

public class Logger {
	
	static PrintWriter writer; 
	static boolean Console = true;
	static boolean File = true;

	public static void Start() {
		Start(true, true);
	}
	
	public static void Start(boolean in_file, boolean in_console) {
		try {
			writer = new PrintWriter("log.txt");
		} catch(Exception e) {
			System.out.println("LOGGER EXCEPTION: " + e.toString());
		}
	}
	
	public static void Close() {
		writer.flush();
		writer.close();
	}
	
	public static void Dbg(String in) {
		Log(TmStmp() + ": DEBUG: " + in);
	}
	public static void Info(String in) {
		Log(TmStmp() + ": INFO: " + in);
	}
	public static void Warn(String in) {
		Log(TmStmp() + ": WARN: " + in);
	}
	public static void Err(String in) {
		Log(TmStmp() + ": ERROR: " + in);
	}
	public static void Vrb(String in) {
		Log(TmStmp() + ": VERBOSE: " + in);
	}
	
	private static void Log(String log) {
		writer.println(log);
		writer.flush();
	}
	
	private static String TmStmp() {
		java.util.Date date= new java.util.Date();
		Timestamp tmpstmp = new Timestamp(date.getTime());
		return tmpstmp.toString();
	}
}
