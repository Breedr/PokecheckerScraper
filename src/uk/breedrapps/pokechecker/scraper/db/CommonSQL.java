/**
 * CommonSQL.java
 * PokecheckerScraper
 *
 * Created by Ed George on 29 Apr 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import uk.breedrapps.pokechecker.scraper.model.PokemonCard;
import uk.breedrapps.pokechecker.scraper.utils.Log;
import uk.breedrapps.pokechecker.scraper.utils.Utils;

/**
 * @author edgeorge
 *
 */
public class CommonSQL {

	private CommonSQL(){}

	private static Database instance;

	private static Connection getDatabaseInstance(){
		if(instance == null){
			instance = new Database();
		}

		if(!instance.isConnected()){
			instance.connect();
		}

		return instance.getConnection();
	}

	public static void insertCards(List<PokemonCard> cards) throws SQLException{
		String table = Utils.DEBUG ? "cards_test" : "cards";
		PreparedStatement preparedStatement = 
				getDatabaseInstance().prepareStatement(
						"INSERT INTO "+table+" VALUES "
								+ "(default,?,?,?,?,?,?,?,?,?,?,?)");
		for (int i = 0; i < cards.size(); i++) {
			PokemonCard card = cards.get(i);
			preparedStatement.setString(1, card.getTitle()); //title
			preparedStatement.setString(2, card.getScanUrl()); //scan_url
			preparedStatement.setInt(3, card.getCard_type_id()); //card_type_id
			preparedStatement.setInt(4, card.getCard_rarity_id()); //card_rarity_id
			preparedStatement.setInt(5, card.getCard_set_id()); //card_set_id
			preparedStatement.setInt(6, card.getType_id()); //type_id
			preparedStatement.setInt(7, card.getNumber()); //card_number
			preparedStatement.setString(8, card.getSet()); //set_raw
			preparedStatement.setString(9, card.getCard_type()); //card_type_raw
			preparedStatement.setString(10, card.getPokemon_type()); //pokemon_type_raw
			preparedStatement.setString(11, card.getRarity()); //rarity_raw
			
			//TODO: Re-implement batch transactions
			try{
				preparedStatement.execute();
			}catch(Exception e){
				Log.d(CommonSQL.class, card.toString());
				e.printStackTrace();
			}
			//preparedStatement.addBatch();
		}
		//preparedStatement.executeBatch();
		preparedStatement.close(); 
	}

	public static int getOrCreateFieldId(String table, String column, String param) throws SQLException{
		if(param.equals(null)){
			Log.e(CommonSQL.class, String.format("Cannot create null - %s %s", table, column));
			return PokemonCard.FIELD_NOT_FOUND;
		}
		int existing_id = getIdFromField(table, column, param);
		return existing_id == PokemonCard.FIELD_NOT_FOUND ? createRow(table, param) : existing_id;
	}

	private static int createRow(String table, String param) throws SQLException {

		Log.w(CommonSQL.class, "Inserting \""+ param +"\" into " + table);

		PreparedStatement preparedStatement = 
				getDatabaseInstance().prepareStatement("INSERT INTO " + table + " VALUES (default, ?)",
						Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setString(1, param);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		if(rs.next()){
			int id = rs.getInt(1);
			preparedStatement.close();
			return id;
		}
		preparedStatement.close();
		return PokemonCard.FIELD_NOT_FOUND;
	}

	public static int getIdFromField(String table, String column, String param) throws SQLException{
		PreparedStatement statement = getDatabaseInstance().prepareStatement(
				"SELECT id FROM "+ table +" WHERE "+ column +" = ? ORDER BY id ASC");
		statement.setString(1, param);
		ResultSet result = statement.executeQuery();

		int id = PokemonCard.FIELD_NOT_FOUND;

		if(result.next()){
			id = result.getInt("id");	
		}

		statement.close();
		return id;
	}

//	public static void insertTest(String insert) throws SQLException{
//		PreparedStatement preparedStatement = 
//				getDatabaseInstance().prepareStatement("INSERT INTO test VALUES (default, ?)");
//		preparedStatement.setString(1, insert);
//		preparedStatement.executeUpdate();
//		preparedStatement.close();
//	}
//
//	public static ResultSet getTest() throws SQLException{
//		Statement statement = getDatabaseInstance().createStatement();
//		return statement.executeQuery("SELECT * FROM test");
//	}

	public static boolean disconnect(){
		return instance == null || instance.disconnect();
	}

	/**
	 * @return
	 * @throws SQLException 
	 */
	public static ResultSet getCards() throws SQLException {
		return getDatabaseInstance().createStatement().executeQuery("SELECT * FROM cards");
	}

	/**
	 * @return
	 * @throws SQLException 
	 */
	public static ResultSet getSets() throws SQLException {
		return getDatabaseInstance().createStatement().executeQuery("SELECT * FROM card_set ORDER BY id DESC");
	}

	/**
	 * @return
	 * @throws SQLException 
	 */
	public static ResultSet getSetsWithCardCount() throws SQLException {
		String query = "SELECT cs.set_name, COUNT(ct.card_set_id) AS card_count "
				+"FROM  card_set cs "
				+"LEFT JOIN cards_test ct ON cs.id = ct.card_set_id "
				+"GROUP BY cs.id "
				+"ORDER BY cs.id DESC";
		return getDatabaseInstance().createStatement().executeQuery(query);
	}

}
