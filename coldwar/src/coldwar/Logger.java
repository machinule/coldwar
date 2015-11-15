package coldwar;

import java.io.PrintWriter;
import java.sql.Timestamp;

import com.badlogic.gdx.Gdx;

public class Logger {
	
	public enum LogLvl {
	    NONE, INFO, DBG, VRB
	}
	
	static PrintWriter writer; 
	static boolean Console = true;
	static boolean File = true;
	static LogLvl lvl = LogLvl.INFO;

	public static void Start() {
		Start(true, true);
	}
	
	public static void SetLevel(String inLvl) {
		if(inLvl == "none") {
			lvl = LogLvl.NONE;
		} else if(inLvl == "info") {
			lvl = LogLvl.INFO;
		} else if(inLvl == "debug") {
			lvl = LogLvl.DBG;
		} else if(inLvl == "verbose") {
			lvl = LogLvl.VRB;
		}
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
		if(lvl.compareTo(LogLvl.DBG) <= 0) {
			Gdx.app.log(ColdWarGame.LOG, in);
		}
	}
	public static void Info(String in) {
		Log(TmStmp() + ": INFO: " + in);
		if(lvl.compareTo(LogLvl.INFO) <= 0) {
			Gdx.app.log(ColdWarGame.LOG, in);
		}
	}
	public static void Warn(String in) {
		Log(TmStmp() + ": WARN: " + in);
		if(lvl.compareTo(LogLvl.DBG) <= 0) {
			Gdx.app.log(ColdWarGame.LOG, in);
		}
	}
	public static void Err(String in) {
		Log(TmStmp() + ": ERROR: " + in);
		if(lvl.compareTo(LogLvl.INFO) <= 0) {
			Gdx.app.log(ColdWarGame.LOG, in);
		}
	}
	public static void Vrb(String in) {
		Log(TmStmp() + ": VERBOSE: " + in);
		if(lvl.compareTo(LogLvl.VRB) <= 0) {
			Gdx.app.log(ColdWarGame.LOG, in);
		}
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
