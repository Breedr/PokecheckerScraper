/**
 * Database.java
 * PokecheckerScraper
 *
 * Created by Ed George on 29 Apr 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import uk.breedrapps.pokechecker.scraper.utils.Log;
import uk.breedrapps.pokechecker.scraper.utils.Utils;

/**
 * @author edgeorge
 *
 */
public class Database implements DatabaseHandler {

	private final String USERNAME;
	private final String PASSWORD;
	private final String URL;
	private final String DATABASE_NAME;
	
	private Connection connect = null;
	
	public Database(){
		USERNAME = Utils.getProperties().getProperty("username");
		PASSWORD = Utils.getProperties().getProperty("password");
		URL = Utils.getProperties().getProperty("url");
		DATABASE_NAME = Utils.getProperties().getProperty("database_name");
	}

	@Override
	public boolean connect() {

		try{
			if(connect != null && !connect.isClosed()){
				return true;
			}
			
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(generateConnectionURL(), USERNAME, PASSWORD);
			return connect.isValid(0);
		}catch(Exception e){
			Log.e(getClass(), "Error connection to database: " + e.getMessage());
			System.exit(-1);
		}
		return false;
	}

	public boolean isConnected(){
		try {
			return connect != null && !connect.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private String generateConnectionURL() {
		return String.format("jdbc:mysql://%s/%s", URL, DATABASE_NAME);
	}

	@Override
	public boolean disconnect() {
		try {
			connect.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Connection getConnection() {
		return connect;
	}

}
