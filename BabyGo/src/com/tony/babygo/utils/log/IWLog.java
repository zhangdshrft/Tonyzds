package com.tony.babygo.utils.log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

/*
 * LOG
 *       
 * author: tony.zhang
 * */
public final class IWLog {

	private final static String baseTag = "";

	private static boolean logToFile = false; 

	private static LogWriter mLogWriter;

	private IWLog() {

	}

	public static void d(String tag, String msg) {
		String t = baseTag + tag;
		print('d', t, msg);
	}

	public static void i(String tag, String msg) {
		String t = baseTag + tag;
		print('i', t, msg);
	}

	public static void e(String tag, String msg) {
		String t = baseTag + tag;
		print('e', t, msg);
	}

	public static void v(String tag, String msg) {
		String t = baseTag + tag;
		print('v', t, msg);
	}

	public static void w(String tag, String msg) {
		String t = baseTag + tag;
		print('w', t, msg);
	}

	private static void print(char methodName, String tag, String msg) {
		if (logToFile) {
			writeToFile(methodName, tag, msg);

		} else {
			switch (methodName) {
			case 'd':
				Log.d(tag, msg);
				break;
			case 'e':
				Log.e(tag, msg);
				break;
			case 'i':
				Log.i(tag, msg);
				break;
			case 'v':
				Log.v(tag, msg);
				break;
			case 'w':
				Log.w(tag, msg);
				break;

			}
		}
	}

	private static void writeToFile(char methodName, String tag, String msg) {
		if (mLogWriter != null) {
			String log = tag + ":" + msg;
			try {
				mLogWriter.appendFile(log);
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(baseTag, "error!!! write log file fail");
			}

		} else {   
			SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd");
			String fileName = "lottery-" + df.format(new Date()) + ".txt";
			try {
				File logf = new File(Environment.getExternalStorageDirectory()
						+ File.separator + fileName);
				mLogWriter = LogWriter.open(logf.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				Log.e(baseTag, "error!!! create log file fail");
			}
		}

	}

}