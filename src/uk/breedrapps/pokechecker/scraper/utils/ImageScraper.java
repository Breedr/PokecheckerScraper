/**
 * ImageScraper.java
 * PokecheckerScraper
 *
 * Created by Ed George on 2 May 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.utils;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * @author edgeorge
 *
 */
public class ImageScraper {

	final String EXPANSION_URL = "http://static.pokemontcg.eu/expansion/";
	
	public static final String EXPANSION_FILE_PATH = "/Users/edgeorge/projs/sandbox/img_temp/";

	public void getSetCardIcon(String[] setShortCodes){
		//"PCL", "DCR"
		for(String expansion : setShortCodes){
			try {
				URL full_url = new URL(EXPANSION_URL + expansion + ".png");
				BufferedImage img = ImageIO.read(full_url);
				FileUtils.writeExpansionImage(img, expansion);
			} catch (Exception e) {
				Log.e(getClass(), "Failed to write expansion image for " + expansion);
				e.printStackTrace();
			}
		}
	}
}
