package Screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.apache.xmlbeans.impl.jam.JComment;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import Helper.BallotsToFile;
import Helper.PassportEntryHelper;
import Objects.Passport.Comment;

public class PassportEntryScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JFrame frmTasteOfDowntown;
	private JTextField age_field, postal_field;
	private JTextArea comments_area;
	private JComboBox<String> gender_combo, foodie_combo;

	private ToggleButtonPanel participantPanel;

	private File csvFile;
	private static PassportEntryHelper peh;
	private static BallotsToFile btf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PassportEntryScreen window = new PassportEntryScreen();
					window.frmTasteOfDowntown.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PassportEntryScreen() {
		getFile();
		peh = PassportEntryHelper.getInstance();
		btf = new BallotsToFile();
		btf.createFile();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTasteOfDowntown = new JFrame();
		frmTasteOfDowntown.setTitle("Taste of Downtown Passport Entry");
		// frmTasteOfDowntown.setBounds(0, 0, 1863, 1096);
		frmTasteOfDowntown.setMinimumSize(new Dimension(454, 467));
		frmTasteOfDowntown.setExtendedState(Frame.MAXIMIZED_BOTH);
		frmTasteOfDowntown.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frmTasteOfDowntown.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				btf.finish();
				System.exit(0);
			}

		});

		JPanel menuPanel = new JPanel();
		frmTasteOfDowntown.getContentPane().add(menuPanel, BorderLayout.NORTH);
		menuPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JMenuBar menuBar = new JMenuBar();
		menuPanel.add(menuBar, BorderLayout.NORTH);
		{
			JMenu menu_File = new JMenu("File");
			menuBar.add(menu_File);

			JMenuItem menuItem_New = new JMenuItem("New");
			menuItem_New.setIcon(new ImageIcon(
					PassportEntryScreen.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
			menu_File.add(menuItem_New);

			JMenuItem menuItem_Open = new JMenuItem("Open");
			menuItem_Open.setIcon(new ImageIcon(
					PassportEntryScreen.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
			menu_File.add(menuItem_Open);

			JMenuItem menuItem_Save = new JMenuItem("Save");
			menuItem_Save.setIcon(new ImageIcon(
					PassportEntryScreen.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
			menu_File.add(menuItem_Save);

			JMenuItem menuItem_Exit = new JMenuItem("Exit");
			menuItem_Exit.setIcon(new ImageIcon(
					PassportEntryScreen.class.getResource("/javax/swing/plaf/metal/icons/ocean/close.gif")));
			menu_File.add(menuItem_Exit);

			JMenu menu_Settings = new JMenu("Settings");
			menuBar.add(menu_Settings);

			JMenuItem menuItem_Preferences = new JMenuItem("Preferences");
			menu_Settings.add(menuItem_Preferences);

			JMenu menu_Help = new JMenu("Help");
			menuBar.add(menu_Help);

			JMenuItem menuItem_Help = new JMenuItem("Help");
			menu_Help.add(menuItem_Help);

			JMenuItem menuItem_About = new JMenuItem("About");
			menu_Help.add(menuItem_About);
		}

		/*
		 ************************************************
		 * PARTICIPANT PANEL *
		 ************************************************
		 */

		participantPanel = new ToggleButtonPanel();
		participantPanel.setBackground(Color.WHITE);
		participantPanel.setLayout(new GridLayout(1, 0, 0, 0));
		Map<String, Path> tmp = peh.generateListFromFile(csvFile);
		participantPanel = peh.generateParicipants(participantPanel, tmp);
		participantPanel.generateComponents();
		participantPanel.setToggles(peh.getMasterResList());
		frmTasteOfDowntown.getContentPane().add(participantPanel, BorderLayout.CENTER);

		/*
		 ************************************************
		 * BOTTOM PANEL *
		 ************************************************
		 */

		JPanel bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.LIGHT_GRAY);
		bottomPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		frmTasteOfDowntown.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		GridBagLayout gbl_bottomPanel = new GridBagLayout();
		gbl_bottomPanel.columnWidths = new int[] { 434, 0 };
		gbl_bottomPanel.rowHeights = new int[] { 0 };
		gbl_bottomPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_bottomPanel.rowWeights = new double[] { 0.0, 0.0 };
		bottomPanel.setLayout(gbl_bottomPanel);

		JPanel top = new JPanel();
		top.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_top = new GridBagConstraints();
		gbc_top.anchor = GridBagConstraints.NORTH;
		gbc_top.fill = GridBagConstraints.BOTH;
		gbc_top.gridx = 0;
		gbc_top.gridy = 0;
		bottomPanel.add(top, gbc_top);
		FormLayout fl_top = new FormLayout(
				new ColumnSpec[] { ColumnSpec.decode("6dlu"), FormSpecs.DEFAULT_COLSPEC, FormSpecs.RELATED_GAP_COLSPEC,
						ColumnSpec.decode("89px"), FormSpecs.RELATED_GAP_COLSPEC, FormSpecs.DEFAULT_COLSPEC,
						FormSpecs.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_COLSPEC, },
				new RowSpec[] { FormSpecs.RELATED_GAP_ROWSPEC, RowSpec.decode("default:grow"),
						FormSpecs.RELATED_GAP_ROWSPEC, FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC,
						FormSpecs.DEFAULT_ROWSPEC, FormSpecs.RELATED_GAP_ROWSPEC, });
		fl_top.setHonorsVisibility(false);
		top.setLayout(fl_top);

		JLabel lblAge = new JLabel("Age");
		top.add(lblAge, "2, 2, right, default");

		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
		decimalFormat.setGroupingUsed(false);
		age_field = new JFormattedTextField(decimalFormat);
		age_field.setColumns(2);
		top.add(age_field, "4, 2, left, center");

		JLabel lblComments = new JLabel("Comments");
		top.add(lblComments, "6, 2");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		top.add(scrollPane, "8, 2, 1, 3, fill, fill");

		comments_area = new JTextArea();
		comments_area.setWrapStyleWord(true);
		comments_area.setTabSize(4);
		comments_area.setLineWrap(true);
		comments_area.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comments_area.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_TAB) {
					if(e.getModifiers() > 0) {
						comments_area.transferFocusBackward();
					}else {
						comments_area.transferFocus();
					}
					e.consume();
				}
			}
			
		});
		
		scrollPane.setViewportView(comments_area);

		JLabel lblGender = new JLabel("Gender");
		lblGender.setHorizontalAlignment(SwingConstants.RIGHT);
		top.add(lblGender, "2, 4, right, default");

		gender_combo = new JComboBox<>();
		gender_combo.setModel(new DefaultComboBoxModel<String>(new String[] { "BLANK", "F", "M" }));
		gender_combo.setMaximumRowCount(3);
		top.add(gender_combo, "4, 4, fill, default");

		JLabel lblPostalCode = new JLabel("Postal Code");
		lblPostalCode.setHorizontalAlignment(SwingConstants.RIGHT);
		top.add(lblPostalCode, "2, 6, right, default");

		postal_field = new JTextField();
		DocumentFilter filter = new UppercaseDocumentFilter();
		((AbstractDocument) postal_field.getDocument()).setDocumentFilter(filter);
		
		top.add(postal_field, "4, 6, left, default");
		postal_field.setColumns(3);

		JLabel lblFoodieVote = new JLabel("Foodie Vote");
		top.add(lblFoodieVote, "6, 6, right, default");

		foodie_combo = new JComboBox<>();
		foodie_combo.addItem("No Location");

		for (String s : participantPanel.getComponentNames()) {
			foodie_combo.addItem(s);
		}

		top.add(foodie_combo, "8, 6, fill, default");

		JPanel bot = new JPanel();
		bot.setBackground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_bot = new GridBagConstraints();
		gbc_bot.anchor = GridBagConstraints.SOUTH;
		gbc_bot.ipadx = 1;
		gbc_bot.ipady = 1;
		gbc_bot.fill = GridBagConstraints.BOTH;
		gbc_bot.gridx = 0;
		gbc_bot.gridy = 1;
		bottomPanel.add(bot, gbc_bot);

		JButton enter_button = new JButton("Enter");
		enter_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (foodie_combo.getSelectedItem() != null) {
					String age = age_field.getText() != null ? age_field.getText() : "";
					String gender = gender_combo.getSelectedItem() != null ? gender_combo.getSelectedItem().toString()
							: "";
					String postal = postal_field.getText() != null ? postal_field.getText() : "";
					String foodie = foodie_combo.getSelectedItem().toString();
					Comment<String, String> comments = comments_area.getText() != null
							? new Comment<String, String>(comments_area.getText(), "General")
							: new Comment<String, String>("No Comment", "No Comment");

					List<String> res = participantPanel.selectedToggles();

					peh.enterBallot(age, gender, postal, comments, foodie, res);
					resetScreen();
				} else {
					JOptionPane.showMessageDialog(null,
							"Please enter the foodie vote. If no vote was cast, please disregard the ballot.");
				}
			}
		});
		
		AbstractAction action = new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource() instanceof JButton) {
					JButton btn = new JButton();
					btn.doClick();
				}else if(e.getSource() instanceof JComment) {
					JComponent com = (JComponent) e.getSource();
					com.transferFocus();
				}
				
			}
		}; 
		
		enter_button.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "DoClick");
		enter_button.getActionMap().put("DoClick", action);

		JButton finish_button = new JButton("Finish");
		finish_button.setToolTipText(
				"Clicking this will push all recent entries into the selected file and close the application");
		finish_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				peh.submitCountedBallots();
				resetScreen();
			}
		});

		JButton reset_button = new JButton("Reset");
		reset_button.setToolTipText(
				"Return the form to it's default state. No changes to the select file or previous enteries will take place.");
		reset_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resetScreen();
			}
		});

		JButton cancel_button = new JButton("Cancel");
		cancel_button.setToolTipText(
				"All entered information and all previously submitted entires that have not been pushed to the selected file and the application will close.");
		cancel_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (peh.getMasterBallotSize() > 0) {
					if (JOptionPane.showConfirmDialog(null,
							"Are you sure you wish to cancel? All un-submitted ballots will be lost.") == JOptionPane.OK_OPTION) {
						btf.finish();
						System.exit(EXIT_ON_CLOSE);
					}
				} else
					System.exit(EXIT_ON_CLOSE);
			}
		});

		bot.setLayout(new GridLayout(0, 4, 0, 0));
		bot.add(enter_button);
		bot.add(finish_button);
		bot.add(reset_button);
		bot.add(cancel_button);
		frmTasteOfDowntown.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[] { age_field, gender_combo,
				postal_field, foodie_combo, comments_area, enter_button, finish_button, reset_button, cancel_button }));

	}

	private void getFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Please select the restaurant csv file");
		chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		chooser.setFileFilter(new ExtensionFileFilter("CSV Only", "csv"));

		int result = chooser.showOpenDialog(getParent());

		if (result == JFileChooser.APPROVE_OPTION) {
			csvFile = chooser.getSelectedFile();
		} else {
			JOptionPane.showMessageDialog(rootPane, "You have not chosen a file, the program will now exit",
					"No file chosen", JOptionPane.ERROR_MESSAGE);
			System.exit(ABORT);
		}
	}

	private class ExtensionFileFilter extends FileFilter {
		String description;

		String extensions[];

		public ExtensionFileFilter(String description, String extension) {
			this(description, new String[] { extension });
		}

		public ExtensionFileFilter(String description, String extensions[]) {
			if (description == null) {
				this.description = extensions[0];
			} else {
				this.description = description;
			}
			this.extensions = extensions.clone();
			toLower(this.extensions);
		}

		private void toLower(String array[]) {
			for (int i = 0, n = array.length; i < n; i++) {
				array[i] = array[i].toLowerCase();
			}
		}

		@Override
		public String getDescription() {
			return description;
		}

		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) {
				return true;
			} else {
				String path = file.getAbsolutePath().toLowerCase();
				for (int i = 0, n = extensions.length; i < n; i++) {
					String extension = extensions[i];
					if ((path.endsWith(extension) && (path.charAt(path.length() - extension.length() - 1)) == '.')) {
						return true;
					}
				}
			}
			return false;
		}
	}

	public void resetScreen() {
		age_field.setText("");
		postal_field.setText("");
		gender_combo.setSelectedIndex(0);
		foodie_combo.setSelectedIndex(0);
		comments_area.setText("");
		participantPanel.setToggles(peh.getMasterResList());
		age_field.requestFocus();
	}
}

class UppercaseDocumentFilter extends DocumentFilter {
	public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr)
			throws BadLocationException {

		fb.insertString(offset, text.toUpperCase(), attr);
	}

	public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
			throws BadLocationException {

		fb.replace(offset, length, text.toUpperCase(), attrs);
	}
}