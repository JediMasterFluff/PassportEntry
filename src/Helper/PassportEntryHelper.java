package Helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import Objects.Passport;
import Objects.Passport.Comment;
import Objects.Restaurant;
import Screen.ToggleButtonPanel;

public class PassportEntryHelper {

	private static final int GAP = 5;
	private static PassportEntryHelper instance = null;

	private Vector<Restaurant<String, Integer, Double>> MasterResList;
	private Vector<Passport> MasterBallotList;
	private Passport current_passport;
	public int TOTAL_TALLY;

	protected PassportEntryHelper() {
		TOTAL_TALLY = 0;
		MasterResList = new Vector<Restaurant<String, Integer, Double>>();
		MasterBallotList = new Vector<Passport>();
		current_passport = new Passport();
	}
	
	public static PassportEntryHelper getInstance() {
		if(instance == null)
			instance = new PassportEntryHelper();
		
		return instance;
	}

	private List<Color> colours = Arrays.asList(new Color(255, 202, 125), new Color(196, 215, 155),
			new Color(146, 205, 220), new Color(177, 160, 199), new Color(255, 153, 204));

	/**
	 * Will create a String array of restaurant names provided by the user via a CSV
	 * file
	 * 
	 * @param f
	 *            - a CSV file given at the start of the application to be used to
	 *            generate the toggle button grid
	 * @return a new ArrayList of restaurant names
	 */
	public Map<String, Path> generateListFromFile(File f) {
		Map<String, Path> map = new LinkedHashMap<String, Path>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = "";
			@SuppressWarnings("unused")
			String headerLine = br.readLine();
			while ((line = br.readLine()) != null) {
				String columns[] = line.split(",");
				String name = columns[0];
				if (!map.containsKey(name)) {
					Path p = Paths.get(columns[1]);
					map.put(name, p);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * Will create a grid of toggle buttons from provided list of participating
	 * restaurants Places toggle buttons will be placed into the provided JFrame and
	 * will be returned.
	 * 
	 * @param parent
	 *            the JPanel to place the grid of toggle buttons
	 * @param map
	 *            and ArrayList of String names of the restaurants
	 * @return the JPanel given but with a grid of toggle buttons
	 */
	public ToggleButtonPanel generateParicipants(ToggleButtonPanel parent, Map<String, Path> map) {

		if (map.size() == 0) {
			return parent;
		}

		JToggleButton tBtn;
		int cols = 5;
		int rows = (int) Math.ceil(map.size() / (float) cols);

		parent.setLayout(new GridLayout(0, cols, GAP, GAP));

		int index = 0;
		int color_index;
		Border border;
		Iterator<Map.Entry<String, Path>> it = map.entrySet().iterator();

		outerloop: for (int r = 0; r <= rows; r++) {
			color_index = 0;
			for (int c = 0; c < cols; c++) {

				if (index >= map.size())
					break outerloop;
				if (it.hasNext()) {
					Map.Entry<String, Path> pair = it.next();
					tBtn = new JToggleButton(pair.getKey());
					BufferedImage image = null;
					try {
						image = ImageIO.read(pair.getValue().toFile());
					} catch (IOException e1) {
						e1.printStackTrace();
					}

					border = new LineBorder(colours.get(color_index), 5);
					tBtn.setIcon(new ImageIcon(ImageHelper.resize(image, 150, 120)));
					tBtn.setBorder(border);
					color_index++;
					parent.add(tBtn);
					index++;
				}
			}
		}

		parent.setPreferredSize(new Dimension(parent.getWidth(), parent.getHeight()));

		return parent;
	}

	/**
	 * All information entered on the screen will be saved to the current passport
	 * object and then will be placed into the master ballot list before creating a
	 * new passport object
	 * 
	 * Every time a ballot is submitted, we will recalculate the voting percentage
	 * records
	 * 
	 * @param age
	 *            The age entered on the ballot
	 * @param gender
	 *            The provided gender on the ballot
	 * @param postal
	 *            The provided postal code on the ballot
	 * @param comments
	 *            The optional comments on the ballot
	 * @param foodie
	 *            The users foodie vote
	 */
	public void enterBallot(String age, String gender, String postal, Comment<String, String> comments, String foodie) {

		current_passport.setAge(age);
		current_passport.setComments(comments);
		current_passport.setFoodie(foodie);
		current_passport.setGender(gender);

		MasterBallotList.addElement(current_passport);
		current_passport = new Passport();
	}

	/**
	 * Will write the current contents of the MasterBallotList to a file, then clear
	 * the list.
	 */
	public void submitCountedBallots() {

	}

	public void addRestaurant(Restaurant<String, Integer, Double> r) {
		if (!MasterResList.contains(r))
			MasterResList.add(r);
	}

	public boolean contains(String s) {
		for (Restaurant<String, Integer, Double> r : MasterResList) {
			if (r.getLeft() == s)
				return true;
		}
		return false;
	}

	public Restaurant<String, Integer, Double> getRestaurant(String name) {
		Iterator<Restaurant<String, Integer, Double>> it = MasterResList.iterator();
		while (it.hasNext()) {
			if (it.next().getLeft().equals(name))
				return it.next();
		}
		return null;
	}

	public int getMasterBallotSize() {
		return MasterBallotList.size();
	}

	public void updateRestaurant(Restaurant<String, Integer, Double> r) {
		for (Restaurant<String, Integer, Double> tmp : MasterResList) {
			if (tmp.getLeft().equals(r.getLeft())) {
				MasterResList.remove(tmp);
				MasterResList.add(r);
				return;
			}
		}
	}
}