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
		if(lvl.compareTo(LogLvl.DBG) >= 0) {
			Log(TmStmp() + ": DEBUG: " + in);
			Gdx.app.log(ColdWarGame.LOG, TmStmp() + ": DEBUG: " + in);
		}
	}
	public static void Info(String in) {
		if(lvl.compareTo(LogLvl.INFO) >= 0) {
			Log(TmStmp() + ": INFO: " + in);
			Gdx.app.log(ColdWarGame.LOG, TmStmp() + ": INFO: " + in);
		}
	}
	public static void Warn(String in) {
		if(lvl.compareTo(LogLvl.DBG) >= 0) {
			Log(TmStmp() + ": WARN: " + in);
			Gdx.app.log(ColdWarGame.LOG, TmStmp() + ": WARN: " + in);
		}
	}
	public static void Err(String in) {
		if(lvl.compareTo(LogLvl.INFO) >= 0) {
			Log(TmStmp() + ": ERROR: " + in);
			Gdx.app.log(ColdWarGame.LOG, TmStmp() + ": ERROR: " + in);
		}
	}
	public static void Vrb(String in) {
		if(lvl.compareTo(LogLvl.VRB) >= 0) {
			Log(TmStmp() + ": VERBOSE: " + in);
			Gdx.app.log(ColdWarGame.LOG, TmStmp() + ": VERBOSE: " + in);
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
