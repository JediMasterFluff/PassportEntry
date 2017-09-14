package Helper;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class P_Log {

	private static P_Log instance = null;
	private Logger log;
	private ConsoleHandler handler;

	protected P_Log() {
		log = Logger.getLogger("Passport Entry Log");
		log.setLevel(Level.ALL);
		handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		log.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
	}

	public static P_Log getLog() {

		if (instance == null)
			instance = new P_Log();

		return instance;
	}

	public void writeLog(String msg, Level lvl) {
		log.setLevel(lvl);
		log.log(lvl, msg);
	}

}
