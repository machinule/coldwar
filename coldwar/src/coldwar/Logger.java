package coldwar;

import java.io.PrintWriter;
import java.sql.Timestamp;

import com.badlogic.gdx.Gdx;

public class Logger {

	public enum LogLvl {
		DBG, INFO, NONE, VRB
	}

	static boolean Console = true;
	static boolean File = true;
	static LogLvl lvl = LogLvl.INFO;
	static PrintWriter writer;

	public static void Close() {
		Logger.writer.flush();
		Logger.writer.close();
	}

	public static void Dbg(final String in) {
		if (Logger.lvl.compareTo(LogLvl.DBG) >= 0) {
			Logger.Log(Logger.TmStmp() + ": DEBUG: " + in);
			Gdx.app.log(ColdWarGame.LOG, Logger.TmStmp() + ": DEBUG: " + in);
		}
	}

	public static void Err(final String in) {
		if (Logger.lvl.compareTo(LogLvl.INFO) >= 0) {
			Logger.Log(Logger.TmStmp() + ": ERROR: " + in);
			Gdx.app.log(ColdWarGame.LOG, Logger.TmStmp() + ": ERROR: " + in);
		}
	}

	public static void Info(final String in) {
		if (Logger.lvl.compareTo(LogLvl.INFO) >= 0) {
			Logger.Log(Logger.TmStmp() + ": INFO: " + in);
			Gdx.app.log(ColdWarGame.LOG, Logger.TmStmp() + ": INFO: " + in);
		}
	}

	private static void Log(final String log) {
		Logger.writer.println(log);
		Logger.writer.flush();
	}

	public static void SetLevel(final String inLvl) {
		if (inLvl == "none") {
			Logger.lvl = LogLvl.NONE;
		} else if (inLvl == "info") {
			Logger.lvl = LogLvl.INFO;
		} else if (inLvl == "debug") {
			Logger.lvl = LogLvl.DBG;
		} else if (inLvl == "verbose") {
			Logger.lvl = LogLvl.VRB;
		}
	}

	public static void Start() {
		Logger.Start(true, true);
	}

	public static void Start(final boolean in_file, final boolean in_console) {
		try {
			Logger.writer = new PrintWriter("log.txt");
		} catch (final Exception e) {
			System.out.println("LOGGER EXCEPTION: " + e.toString());
		}
	}

	private static String TmStmp() {
		final java.util.Date date = new java.util.Date();
		final Timestamp tmpstmp = new Timestamp(date.getTime());
		return tmpstmp.toString();
	}

	public static void Vrb(final String in) {
		if (Logger.lvl.compareTo(LogLvl.VRB) >= 0) {
			Logger.Log(Logger.TmStmp() + ": VERBOSE: " + in);
			Gdx.app.log(ColdWarGame.LOG, Logger.TmStmp() + ": VERBOSE: " + in);
		}
	}

	public static void Warn(final String in) {
		if (Logger.lvl.compareTo(LogLvl.DBG) >= 0) {
			Logger.Log(Logger.TmStmp() + ": WARN: " + in);
			Gdx.app.log(ColdWarGame.LOG, Logger.TmStmp() + ": WARN: " + in);
		}
	}
}
