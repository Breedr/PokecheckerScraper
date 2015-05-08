/**
 * HTMLUtils.java
 * PokecheckerScraper
 *
 * Created by Ed George on 1 May 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.utils;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.BaseToken;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import uk.breedrapps.pokechecker.scraper.db.CommonSQL;
import uk.breedrapps.pokechecker.scraper.model.PokemonCard;
import uk.breedrapps.pokechecker.scraper.model.PokemonCard.Builder;


/**
 * @author edgeorge
 *
 */
public class CardScraper {

	public String url = null;
	public String path = null;
	public String xpath = null;

	final CleanerProperties mCleanerProps; 
	final HtmlCleaner mCleaner;

	public CardScraper(){
		Utils.spoofUserAgent();
		mCleanerProps = new CleanerProperties();
		mCleaner = new HtmlCleaner(mCleanerProps);
	}

	public void scrapeCardsFromSets(String[] sets){
		scrapeCardsFromSets(sets, true);
	}

	public void scrapeCardsFromSets(String[] sets, boolean insert){
		for(int i = 0; i < sets.length; i++){
			scrapeCardsFromSet(sets[i], insert);
		}
	}

	private void scrapeCardsFromSet(String set, boolean insert) {
		try{
			List<PokemonCard> cards = scrapeCards(set);
			//Add cards to DB
			if(insert && cards.size() > 0){
				CommonSQL.insertCards(cards);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public List<PokemonCard> scrapeCards(String set){
		url = "http://pkmncards.com/set/";
		xpath = "//div[@class='entry-content']//div//a";
		path = Utils.formatSetName(set);

		try{
			Log.d(getClass(), "Loading cards " + url + path);
			TagNode tagNode = mCleaner.clean(new URL(url + path));

			Object[] link_nodes = tagNode.evaluateXPath(xpath);

			List<String> tmp = new ArrayList<>();

			for (int node = 0; node < link_nodes.length; node++) {
				TagNode link_node = (TagNode) link_nodes[node];
				for(BaseToken b : link_node.getAllChildren()){
					tmp.add(b.toString());
				}	
			}

			xpath = "//div[@class='entry-content']//div";
			link_nodes = tagNode.evaluateXPath(xpath);

			List<PokemonCard> cards = new ArrayList<>();
			
			if(tmp.size() < 1){
				Log.e(getClass(), "No cards found");
				return cards;
			}
			
			PokemonCard.Builder builder = new Builder();
			int index = 0;
			for (int y = 0; y < link_nodes.length; y++) {

				TagNode link_node = (TagNode) link_nodes[y];
				for(int i = 0; i < link_node.getAllChildren().size(); i++){

					BaseToken b = link_node.getAllChildren().get(i);

					switch(y % 6){
					case 0:
						if(y != 0){
							try{
								PokemonCard card = builder.title(tmp.get(index)).build();
								Log.d(getClass(), card.toString());
								cards.add(card);
							}catch(Exception e){
								Log.e(getClass(), "Failed to add scraped card - index " + index);
								e.printStackTrace();
							}finally{
								builder = new Builder();
								index++;
							}
						}
						break;
					case 1:
						int id;
						try{
							id = Integer.parseInt(b.toString());
						}catch (Exception e) {
							id = cards.size() + 1;
							Log.w(getClass(), String.format("Cannot convert card id %s - setting to %d", b.toString(), id));
						}
						builder = builder.number(id);
						break;
					case 2:
						builder = builder.set(b.toString());
						break;
					case 3:
						builder = builder.cardType(b.toString());
						break;
					case 4:
						builder = builder.pokemonType(b.toString());
						break;
					case 5:
						builder = builder.rarity(b.toString());
						break;
					}
				}
			}

			return cards;

		}catch(FileNotFoundException fne){
			Log.e(getClass(), "HTTP 404: Could not find " + url + path);
		}catch(Exception e){
			Log.e(getClass(), "Failed to get cards from " + url + path);
			e.printStackTrace();
		}
		
		return null;
		
	}
}
