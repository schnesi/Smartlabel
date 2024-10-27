package ch.schnes.smartlabel.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger instance;
    private static final String LOG_FILE = "src/main/resources/server.log";
    private PrintWriter writer;

    private Logger() {
        try {
        	File logFile = new File(LOG_FILE);
        	if (!logFile.exists()) logFile.createNewFile();
        	
            FileWriter fileWriter = new FileWriter(LOG_FILE, true); // true -> append to existing file
            writer = new PrintWriter(fileWriter, true); // true -> Auto-Flush for PrintWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Singleton-Instanz abrufen
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // Methode zum Schreiben einer Info-Nachricht
    public void logInfo(String instance, String message) {
        log("INFO", instance, message);
    }

    // Methode zum Schreiben einer Fehler-Nachricht
    public void logError(String instance, String message) {
        log("ERROR", instance, message);
    }

    // Private Methode, die das Log-Format bestimmt
    private void log(String level, String instance, String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        writer.println("[" + timestamp + "] [" + level + "] " + instance + ": " + message);
    }

    // Methode zum Schließen des Writers (optional, wenn die Anwendung beendet wird)
    public void close() {
        if (writer != null) {
        	logInfo("Logger", "Main end");
            writer.close();
        }
    }
}