package bilanpack.net.vkscaner;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
	private String fileName = "";
	private FileOutputStream fileStream = null;
	private String logPath = "";

	public Logger(String Path) {
		logPath = Path;
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
		fileName = String.valueOf(String.valueOf((new StringBuffer()).append(
				formatter.format(today)).append(".log")));

		try {
			boolean isFolderExists = (new File(logPath)).exists();
			if (!isFolderExists) {
				File folder = new File(logPath);
				folder.mkdirs();
			}
			fileStream = new FileOutputStream(logPath + "/" + fileName, true);

		} catch (Exception ne) {
			System.err.print(ne.getMessage());
		}

	}

	public Logger(String Path, String fName) {
		logPath = Path;

		fileName = fName + ".log";

		try {
			boolean isFolderExists = (new File(logPath)).exists();
			if (!isFolderExists) {
				File folder = new File(logPath);
				folder.mkdirs();
			}
			fileStream = new FileOutputStream(logPath + "/" + fileName, true);

		} catch (Exception ne) {
			System.err.print(ne.getMessage());
		}

	}

	public synchronized void log(String message) {
		PrintWriter printWriter = null;
		Date today = new Date();
		message = String.valueOf((new StringBuffer(String.valueOf(DateFormat.getDateTimeInstance(2, 2,
						new Locale("uk", "UA")).format(today)))).append("\t").append(message).append(
						message.length() != 0 ? message.charAt(message.length() - 1) != '\n' ? "\n"
								: "" : "\n"));
		try {
			fileStream = new FileOutputStream(logPath + "/" + fileName, true);
			printWriter = new PrintWriter(fileStream);
			printWriter.write(message);
			printWriter.flush();
			if (fileStream != null)
				fileStream.close();
		} catch (Exception ex) {
			System.err.print(ex.toString());
		}
	}

	static class test {
		public static void main(String[] args) {
//			new Logger("./", "NewLOg").log("new write");
			Logger n = new Logger("./", "NewLOg");
			System.out.println(n.getClass().getSimpleName());
		}
	}
}