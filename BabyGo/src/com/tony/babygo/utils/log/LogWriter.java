package com.tony.babygo.utils.log;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {

	private static LogWriter mLogWriter;

	private static String mPath;

	private static Writer mWriter;

	private static SimpleDateFormat df;

	private LogWriter(String file_path) {
		this.setmPath(file_path);
		LogWriter.setmWriter(null);
	}

	public static LogWriter open(String file_path) throws IOException {
		if (mLogWriter == null) {
			mLogWriter = new LogWriter(file_path);
		}
		setmWriter(new BufferedWriter(new FileWriter(getmPath())));
		df = new SimpleDateFormat("[yy-MM-dd hh:mm:ss]: ");

		return mLogWriter;
	}

	public void close() throws IOException {
		getmWriter().close();
	}

	public void writeFile(String log) throws IOException {
		getmWriter().write(df.format(new Date()));
		getmWriter().write(log);
		getmWriter().write("\n");
		getmWriter().flush();
	}

	public void appendFile(String log) throws IOException {
		getmWriter().append(df.format(new Date()));
		getmWriter().append(log);
		getmWriter().append("\n");
		getmWriter().flush();
	}

	public static String getmPath() {
		return mPath;
	}

	public void setmPath(String mPath) {
		LogWriter.mPath = mPath;
	}

	public static Writer getmWriter() {
		return mWriter;
	}

	public static void setmWriter(Writer mWriter) {
		LogWriter.mWriter = mWriter;
	}

}