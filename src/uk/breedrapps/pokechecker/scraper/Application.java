/**
 * Application.java
 * PokecheckerScraper
 *
 * Created by Ed George on 29 Apr 2015
 *
 */
package uk.breedrapps.pokechecker.scraper;

import java.awt.EventQueue;

import javax.swing.UIManager;

import uk.breedrapps.pokechecker.scraper.gui.AppWindow;

/**
 * @author edgeorge
 *
 */
public class Application {

	public static void main(String[] args){
//		try {

//			ResultSet result = CommonSQL.getTest();
//			while (result.next())
//			{
//				Log.d(Application.class,result.getString("test_col"));
//			}
//			
//			Log.d(Application.class, "Found: " + CommonSQL.getIdFromField("test", "test_col", "Test"));
//			Log.d(Application.class, "Id of TestMe: " + CommonSQL.getIdFromField("test", "test_col", "HiMum"));
//			Log.d(Application.class, "Id of TestMe: " + CommonSQL.getOrCreateFieldId("test", "test_col", "HiMum"));

//card_type_id,card_rarity_id,card_set_id,type_id,set_raw,card_type_raw,pokemon_type_raw,rarity_raw
//11,4,4,28,"Base Set 2","Pokémon - Stage 2",Psychic,"Rare Holo"

//			PokemonCard.Builder builder = new PokemonCard.Builder();
//			builder = builder.title("Test").set("Base Set 2").cardType("Pokémon - Stage 2").pokemonType("Psychic").rarity("Rare Holo");
//			Log.d(Application.class, builder.build().toCSV());
			
//			CardScraper scraper = new CardScraper();
//			String[] sets = new String[]{"Double Crisis"};
//			scraper.scrapeCardsFromSets(sets, true);
//			FileUtils.generateCSV(sets[0], scraper.scrapeCards(sets[0]));
//			
//			ImageScraper image_scraper = new ImageScraper();
//			image_scraper.getSetCardIcon(new String[]{"PCL"});
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						AppWindow window = new AppWindow();
						window.getFrame().setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			CommonSQL.disconnect();
//		}
	}

}
