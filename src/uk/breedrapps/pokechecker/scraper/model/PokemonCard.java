/**
 * PokemonCard.java
 * PokecheckerScraper
 *
 * Created by Ed George on 29 Apr 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.breedrapps.pokechecker.scraper.db.CommonSQL;
import uk.breedrapps.pokechecker.scraper.utils.FileUtils;

/**
 * @author edgeorge
 *
 */
public class PokemonCard {
	
	public static final int FIELD_NOT_FOUND = -1;
	private int id;
	private final String title;
	private final int number;
	private final String set;
	private final String card_type;
	private final String pokemon_type;
	private final String rarity;
	private final String scanUrl;
	
	private final int card_type_id;
	private final int card_rarity_id;
	private final int card_set_id;
	private final int type_id;
	
	public static class Builder{
		private static final String DEFAULT = "n/a";
		private String title;
		private int number;
		private String set;
		private String card_type;
		private String pokemon_type;
		private String rarity;
		private String scanUrl;
		
		private int card_type_id = FIELD_NOT_FOUND;
		private int card_rarity_id = FIELD_NOT_FOUND;
		private int card_set_id = FIELD_NOT_FOUND;
		private int type_id = FIELD_NOT_FOUND;
		
		public Builder title(String title){
			this.title = title;
			return this;
		}
		public Builder number(int number){
			this.number = number;
			return this;
		}
		public Builder set(String set){
			this.set = set;
			return this;
		}
		public Builder cardType(String card_type){
			this.card_type = nullCheck(card_type);
			return this;
		}
		public Builder pokemonType(String pokemon_type){
			this.pokemon_type = nullCheck(pokemon_type);
			return this;
		}
		public Builder rarity(String rarity){
			this.rarity = nullCheck(rarity);
			return this;
		}
		public Builder scanUrl(String scanUrl){
			this.scanUrl = scanUrl;
			return this;
		}
		

		private String nullCheck(String string) {
			// TODO Auto-generated method stub
			return string.equals(null) ? DEFAULT : string;
		}
		
		private Builder getIdsFromDatabase(){
			try{
			this.card_rarity_id = CommonSQL.getOrCreateFieldId("card_rarity", "description", rarity);
			this.card_set_id = CommonSQL.getIdFromField("card_set", "set_name", set);
			this.type_id = CommonSQL.getOrCreateFieldId("type", "description", pokemon_type);
			this.card_type_id = CommonSQL.getOrCreateFieldId("card_type", "description", card_type);
			}catch(Exception e){
				e.printStackTrace();
			}
			return this;
		}
		
		private Builder generateScanUrl() {
			this.scanUrl = FileUtils.getGeneratedScanFilePath(set,number);
			return this;
		}
		
		public PokemonCard build(){
			Builder finalBuilder = this.generateScanUrl().getIdsFromDatabase();
			return new PokemonCard(finalBuilder);
		}

	}
	
	private PokemonCard(Builder builder){
		this.card_rarity_id = builder.card_rarity_id;
		this.card_set_id = builder.card_set_id;
		this.card_type = builder.card_type;
		this.card_type_id = builder.card_type_id;
		this.title = builder.title;
		this.number = builder.number;
		this.pokemon_type = builder.pokemon_type;
		this.rarity = builder.rarity;
		this.scanUrl = builder.scanUrl;
		this.set = builder.set;
		this.type_id = builder.type_id;
	}

	public String getTitle() {
		return title;
	}

	public int getNumber() {
		return number;
	}

	public String getSet() {
		return set;
	}

	public String getCard_type() {
		return card_type;
	}

	public String getPokemon_type() {
		return pokemon_type;
	}

	public String getRarity() {
		return rarity;
	}

	public String getScanUrl() {
		return scanUrl;
	}

	public int getCard_type_id() {
		return card_type_id;
	}

	public int getCard_rarity_id() {
		return card_rarity_id;
	}

	public int getCard_set_id() {
		return card_set_id;
	}

	public int getType_id() {
		return type_id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	public String toCSV(){
		return id +","+ title +","+ scanUrl +","+ card_type_id
				+","+ card_rarity_id +","+ card_set_id
				+","+ type_id +","+ number +","+ set
				+","+ card_type +","+ pokemon_type +","+ rarity;
	}
	
}
