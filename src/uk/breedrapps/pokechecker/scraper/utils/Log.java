/**
 * Log.java
 * PokecheckerScraper
 *
 * Created by Ed George on 29 Apr 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.utils;

import java.awt.Color;
import java.io.PrintStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * @author edgeorge
 *
 */
public class Log {

	public enum Level{
		DEBUG, INFO, WARNING, ERROR;

		public String toString() {
			return super.toString().substring(0, 1);
		};

	}

	private static Style style;
	private static StyledDocument document;

	private Log(){}

	public static void setConsole(Style style, StyledDocument doc){
		Log.document = doc;
		Log.style = style;
	}

	private static void print(Level level, Class<?> clazz, String message){
		if(document != null){
			try {
				setStyle(level);
				String line = "> " + level.toString() + ": " + message + "\n";
				document.insertString(document.getLength(), line, style);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		PrintStream stream = (level == Level.ERROR) ? System.err : System.out;
		stream.println(clazz.getCanonicalName() + " " + level.toString() + ": " + message);
	}

	/**
	 * @param level
	 */
	private static void setStyle(Level level) {
		switch(level){
		case ERROR:
			StyleConstants.setForeground(style, Color.decode("#B03D4B"));
			StyleConstants.setBackground(style, Color.decode("#FF394C"));
			break;
		case DEBUG:
			StyleConstants.setForeground(style, Color.decode("#294A69"));
			StyleConstants.setBackground(style, Color.decode("#63B0FA"));
			break;
		case WARNING:
			StyleConstants.setForeground(style, Color.decode("#ABA000"));
			StyleConstants.setBackground(style, Color.decode("#FAEB00"));
			break;
		case INFO:
			StyleConstants.setForeground(style, Color.decode("#6AAB80"));
			StyleConstants.setBackground(style, Color.decode("#9BFABC"));
			break;
		}

	}

	public static void d(Class<?> clazz, String message){
		print(Level.DEBUG, clazz, message);
	}

	public static void i(Class<?> clazz, String message){
		print(Level.INFO, clazz, message);
	}

	public static void w(Class<?> clazz, String message){
		print(Level.WARNING, clazz, message);
	}

	public static void e(Class<?> clazz, String message){
		print(Level.ERROR, clazz, message);
	}

	public static String line() {
		int level = 3;
		StackTraceElement[] traces;
		traces = Thread.currentThread().getStackTrace();
		return traces[level].toString();
	}

}
