/**
 * Utils.java
 * PokecheckerScraper
 *
 * Created by Ed George on 29 Apr 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author edgeorge
 *
 */
public class Utils {
	
	public static final boolean DEBUG = true;
	private static Properties properties;

	private Utils(){}
	
	public static Properties getProperties(){
		if(properties == null){
			setupProperties();
		}
		return properties;
	}

	private static void setupProperties() {
		try(InputStream input = new FileInputStream("config.properties")){
			properties = new Properties();
			properties.load(input);
		} catch (IOException e) {
			Log.e(Utils.class, "Failed to load config.properties");
			e.printStackTrace();
			System.exit(-1);
		}		
	}
	
	public static String formatSetName(String set_name){
		return set_name.toLowerCase().trim().replaceAll(" ", "-");
	}

	/**
	 * 
	 */
	public static void spoofUserAgent() {
		System.setProperty("http.agent", "");	
	}

}
