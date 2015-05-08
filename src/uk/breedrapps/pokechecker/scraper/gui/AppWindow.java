/**
 * AppWindow.java
 * PokecheckerScraper
 *
 * Created by Ed George on 3 May 2015
 *
 */
package uk.breedrapps.pokechecker.scraper.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

import uk.breedrapps.pokechecker.scraper.db.CommonSQL;
import uk.breedrapps.pokechecker.scraper.utils.CardScraper;
import uk.breedrapps.pokechecker.scraper.utils.FileUtils;
import uk.breedrapps.pokechecker.scraper.utils.Log;

/**
 * @author edgeorge
 *
 */
public class AppWindow {

	private static final int APP_HEIGHT = 700;
	private static final int APP_WIDTH = 800;

	private JFrame frame;
	private JTable table;
	private JButton btnExport;
	private JButton btnScans;
	private JButton btnImport;
	private JComboBox setComboBox;
	private final DefaultTableModel tableModel = new DefaultTableModel();


	/**	
	 * Create the application.
	 */
	public AppWindow() {
		initialize();
		setup();
	}


	/**
	 * 
	 */
	private void setup() {
		populateComboBox();
		try {
			GuiUtils.populateTableFromResultSet(tableModel, CommonSQL.getCards());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("Pokéchecker Scraper");
		frame.setBounds(100, 100, APP_WIDTH, APP_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				//Ensure connection to Database is terminated
				boolean success = CommonSQL.disconnect();
				if(!success){
					JOptionPane.showMessageDialog(frame,
							"Could not disconnect from database",
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});


		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('f');
		menuBar.add(mnFile);

		final JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("CSV File", "csv");
		fileChooser.setFileFilter(filter);
		JMenuItem mntmExport = new JMenuItem("Export");
		mntmExport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		mntmExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(FileUtils.CSV)){
						//FileUtils.generateCSV(set, cards, file.getAbsolutePath());
					}
				}
			}
		});
		mnFile.add(mntmExport);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JTextPane textPane = new JTextPane();
		frame.getContentPane().add(textPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frame.getContentPane().add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("List", null, panel, null);
		panel.setLayout(new GridLayout(0, 1, 100, 0));

		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{86, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{43, 29, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		btnImport = new JButton("Import");
		btnImport.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				CardScraper scraper = new CardScraper();
				String set = setComboBox.getSelectedItem().toString().replaceAll("\\(.*\\)", "");
				scraper.scrapeCardsFromSets(new String[]{set});
			}
		});
		btnImport.setEnabled(false);
		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnImport.gridwidth = 2;
		gbc_btnImport.insets = new Insets(0, 0, 5, 5);
		gbc_btnImport.gridx = 0;
		gbc_btnImport.gridy = 0;
		panel_1.add(btnImport, gbc_btnImport);
		
		setComboBox = new JComboBox();
		setComboBox.setEnabled(false);

		GridBagConstraints gbc_setComboBox = new GridBagConstraints();
		gbc_setComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_setComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_setComboBox.gridx = 2;
		gbc_setComboBox.gridy = 0;
		panel_1.add(setComboBox, gbc_setComboBox);
		btnScans = new JButton("Get Scans");
		btnScans.setEnabled(false);
		GridBagConstraints gbc_btnScans = new GridBagConstraints();
		gbc_btnScans.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnScans.gridwidth = 2;
		gbc_btnScans.insets = new Insets(0, 0, 5, 5);
		gbc_btnScans.gridx = 0;
		gbc_btnScans.gridy = 1;
		panel_1.add(btnScans, gbc_btnScans);

		btnExport = new JButton("Export");
		btnExport.setEnabled(false);
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.anchor = GridBagConstraints.SOUTH;
		gbc_btnExport.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnExport.gridwidth = 2;
		gbc_btnExport.insets = new Insets(0, 0, 0, 5);
		gbc_btnExport.gridx = 0;
		gbc_btnExport.gridy = 2;
		panel_1.add(btnExport, gbc_btnExport);

		JLabel lblReady = new JLabel("Ready");
		GridBagConstraints gbc_lblReady = new GridBagConstraints();
		gbc_lblReady.gridx = 2;
		gbc_lblReady.gridy = 2;
		panel_1.add(lblReady, gbc_lblReady);

		final JTextPane lblNewLabel = new JTextPane();
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		StyledDocument doc = lblNewLabel.getStyledDocument();
		Style style = lblNewLabel.addStyle("ConsoleStyle", null);
		Log.setConsole(style, doc);
		
		lblNewLabel.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(lblNewLabel);
		panel.add(scrollPane);
		table = new JTable(tableModel);
		table.setCellSelectionEnabled(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		panel.add(new JScrollPane(table));

		//				try{
		//					table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
		//						public void valueChanged(ListSelectionEvent event) {
		//							lblNewLabel.setText(tableModel.getValueAt(table.getSelectedRow(), table.getSelectedColumn()).toString());
		//						}
		//					});
		//					table.addMouseListener(new MouseAdapter() {
		//						public void mouseClicked(MouseEvent evt) {
		//							JTable list = (JTable) evt.getSource();
		//							if (evt.getClickCount() == 2) {
		//								if(Desktop.isDesktopSupported()){
		//									try {
		//										Desktop.getDesktop().browse(model.getTradeAtRow(list.getSelectedRow()).getUrl().toURI());
		//									} catch (IOException | URISyntaxException e) {
		//										e.printStackTrace();
		//									}
		//								}
		//							}
		//						}
		//					});
		//				}catch(Exception e){
		//					e.printStackTrace();
		//				}

		JPanel consolePanel = new JPanel();
		tabbedPane.addTab("Console", null, consolePanel, null);
		consolePanel.setLayout(new BoxLayout(consolePanel, BoxLayout.PAGE_AXIS));

		//		console = new Console();
		//		logger = new Logger(console);
		//		console.setFont(new Font("Courier", Font.PLAIN, 13));
		//		console.setEditable(false);
		//		consolePanel.add(new JScrollPane(console));


		JButton btnClearConsole = new JButton("Clear Log");
		btnClearConsole.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnClearConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//				console.clear();
			}
		});

		Component verticalStrut = Box.createVerticalStrut(2);
		consolePanel.add(verticalStrut);
		btnClearConsole.setMnemonic('c');
		consolePanel.add(btnClearConsole);
	}



	/**
	 * @param setComboBox
	 */
	private void populateComboBox() {
		//		new SwingWorker<Void, Void>() {
		//
		//			@Override
		//			protected Void doInBackground() throws Exception {
		try{
			ResultSet sets = CommonSQL.getSetsWithCardCount();
			List<String> results = new ArrayList<String>();
			while(sets.next()) {
				String result = String.format("%s (%d)", sets.getString(1), sets.getInt(2));
				results.add(result);
			}
			setComboBox.setModel(new DefaultComboBoxModel(results.toArray()));
			setToolboxEnabled(true);
		}catch(Exception e){
			e.printStackTrace();
		}
		//enableForm();
		//				return null;
		//			}
		//		};

	}


	/**
	 * 
	 */
	private void setToolboxEnabled(boolean enable) {
		setComboBox.setEnabled(enable);
		btnExport.setEnabled(enable);
		btnImport.setEnabled(enable);
		btnScans.setEnabled(enable);
	}


	public JFrame getFrame() {
		return frame;
	}

}
