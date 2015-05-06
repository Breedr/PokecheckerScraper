/**
 * DatabaseHandler.java
 * PokecheckerScraper
 *
 * Created by Ed George on 29 Apr 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.db;

import java.sql.Connection;


/**
 * @author edgeorge
 *
 */
public interface DatabaseHandler {
	
	boolean connect();
	boolean disconnect();
	Connection getConnection();
	
}
