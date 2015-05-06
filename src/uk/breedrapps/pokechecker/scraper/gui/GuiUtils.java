/**
 * GuiUtils.java
 * PokecheckerScraper
 *
 * Created by Ed George on 6 May 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.gui;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import uk.breedrapps.pokechecker.scraper.utils.Log;

/**
 * @author edgeorge
 *
 */
public class GuiUtils {
	
	private GuiUtils(){}
	
	public static void populateTableFromResultSet(final DefaultTableModel tableModel, final ResultSet resultSet){
		new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				loadData(resultSet);
				return null;
			}

			private void loadData(ResultSet resultSet) {

				try{
					ResultSetMetaData metaData = resultSet.getMetaData();

					// Names of columns
					Vector<String> columnNames = new Vector<String>();
					int columnCount = metaData.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						columnNames.add(metaData.getColumnName(i));
					}

					// Data of the table
					Vector<Vector<Object>> data = new Vector<Vector<Object>>();
					while (resultSet.next()) {
						Vector<Object> vector = new Vector<Object>();
						for (int i = 1; i <= columnCount; i++) {
							vector.add(resultSet.getObject(i));
						}
						data.add(vector);
					}

					tableModel.setDataVector(data, columnNames);
				} catch (Exception e) {
					Log.e(getClass(), e.getMessage());
				}
			}
		}.execute();
	}

}
