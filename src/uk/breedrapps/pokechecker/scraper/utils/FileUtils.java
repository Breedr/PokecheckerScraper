/**
 * FileUtils.java
 * PokecheckerScraper
 *
 * Created by Ed George on 30 Apr 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.imageio.ImageIO;

import uk.breedrapps.pokechecker.scraper.model.PokemonCard;

/**
 * @author edgeorge
 *
 */
public class FileUtils {

	private static final String SCAN_FILE_EXTENSION = ".jpg";
	private static final String BASE_SCAN_FOLDER = "scans";
	private static final String BASE_CSV_DIR = "csv";
	private static final String UNKNOWN_FOLDER = "unknown";
	private static final String CSV_HEADER = "id,title,scan_url,card_type_id,card_rarity_id,card_set_id,type_id,card_number,set_raw,card_type_raw,"
			+ "pokemon_type_raw,rarity_raw\n";
	public static final String CSV = ".csv";

	private FileUtils(){}

	public static String getGeneratedScanFilePath(String set_name, int number) {

		if(set_name.equals(null)){
			set_name = UNKNOWN_FOLDER;
			Log.w(FileUtils.class, "Generated filepath " + BASE_SCAN_FOLDER + File.separator + UNKNOWN_FOLDER);
		}

		return File.separator + BASE_SCAN_FOLDER 
				+ File.separator + Utils.formatSetName(set_name)
				+ File.separator + Integer.toString(number)
				+ SCAN_FILE_EXTENSION;
	}

	public static void writeExpansionImage(BufferedImage img, String filename) throws IOException {
		String filepath = ImageScraper.EXPANSION_FILE_PATH + filename + ".png";
		ImageIO.write(img, "png", new File(filepath));
		Log.d(FileUtils.class, "Wrote " + filepath );
	}
	
	public static boolean generateCSV(String set, List<PokemonCard> cards){
		return generateCSV(set, cards, BASE_CSV_DIR + File.separator + Utils.formatSetName(set) + ".csv");
	}

	public static boolean generateCSV(String set, List<PokemonCard> cards, String fileName){

		if(set.equals(null) || cards == null){
			Log.e(FileUtils.class, "Failed to create CSV file - null param");
			return false;
		}

		StringBuilder builder = new StringBuilder(CSV_HEADER);
		for(PokemonCard card : cards){
			builder.append(card.toCSV()).append("\n");
		}

		File file = new File(fileName);
		file.getParentFile().mkdirs();

		try(PrintWriter write = new PrintWriter(file)){
			write.print(builder.toString());
			write.flush();
			Log.d(FileUtils.class, "Created CSV " + file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			Log.e(FileUtils.class, "Failed to create CSV file - File not found");
			e.printStackTrace();
			return false;
		}

		return true;

	}

}
