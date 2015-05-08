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

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
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
	}
}
