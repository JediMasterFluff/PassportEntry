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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private Map<Integer, Restaurant<String, Integer, Double>> MasterResList;
	private ArrayList<Passport> MasterBallotList;
	private Passport current_passport;

	protected PassportEntryHelper() {
		MasterResList = new LinkedHashMap<Integer, Restaurant<String, Integer, Double>>();
		MasterBallotList = new ArrayList<Passport>();
		current_passport = new Passport();
	}

	public static PassportEntryHelper getInstance() {
		if (instance == null)
			instance = new PassportEntryHelper();

		return instance;
	}

	private List<Color> colours = Arrays.asList(new Color(255, 202, 125), new Color(196, 215, 155),
			new Color(146, 205, 220), new Color(177, 160, 199), new Color(255, 153, 204));

	/**
	 * Will create a String array of restaurant names provided by the user via a CSV
	 * file
	 * 
	 * @param f A CSV file given at the start of the application to be used to
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
	 * @param parent the JPanel to place the grid of toggle buttons
	 * @param map and ArrayList of String names of the restaurants
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

					Restaurant<String, Integer, Double> res = new Restaurant<String, Integer, Double>(pair.getKey());
					addRestaurant(res);

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

		// printRestaurants();

		return parent;
	}

	/**
	 * All information entered on the screen will be saved to the current passport
	 * object and then will be placed into the master ballot list before creating a
	 * new passport object Every time a ballot is submitted, we will recalculate the
	 * voting percentage records
	 * 
	 * @param age The age entered on the ballot
	 * @param gender The provided gender on the ballot
	 * @param postal The provided postal code on the ballot
	 * @param comments The optional comments on the ballot
	 * @param foodie The users foodie vote
	 */
	public void enterBallot(String age, String gender, String postal, Comment<String, String> comments, String foodie,
			List<String> res) {

		current_passport.setAge(age);
		current_passport.setComments(comments);
		current_passport.setFoodie(foodie);
		current_passport.setGender(gender);

		for (String s : res) {
			Restaurant<String, Integer, Double> r = getRestaurant(s);
			r.incrementCount();
			updateRestaurant(r);
		}

		MasterBallotList.add(current_passport);
		RecalculatePercentage();
		printRestaurants();
		printBallotList();
		current_passport = new Passport();
	}

	/**
	 * Will write the current contents of the MasterBallotList to a file, then clear
	 * the list. Should only be called at the end of processing ballots. Cannot be
	 * undone
	 */
	public void submitCountedBallots() {

	}

	/**
	 * Adds a Restaurant to the MasterResList, if doesn't already exist
	 * 
	 * @param r Restaurant to add
	 */
	private void addRestaurant(Restaurant<String, Integer, Double> r) {
		if (!MasterResList.containsValue(r))
			MasterResList.put(MasterResList.size() + 1, r);
	}

	/**
	 * Returns a Restaurant Object from a given name, if it exists
	 * 
	 * @param name The name of the restaurant to return
	 * @return The restaurant object returned from the MasterReslist
	 */
	public Restaurant<String, Integer, Double> getRestaurant(String name) {

		for (Entry<Integer, Restaurant<String, Integer, Double>> e : MasterResList.entrySet()) {
			if (e.getValue().getLeft().equals(name))
				return e.getValue();
		}
		return null;

	}

	public int getMasterBallotSize() {
		return MasterBallotList.size();
	}

	/**
	 * Will update the given Restaurant in the MasterResList of the application
	 * 
	 * @param r The restaurant to update in the list
	 */
	public void updateRestaurant(Restaurant<String, Integer, Double> r) {
		for (Entry<Integer, Restaurant<String, Integer, Double>> e : MasterResList.entrySet()) {
			if (r.equals(e.getValue())) {
				e.setValue(r);
				return;
			}
		}
	}

	/**
	 * Internal method called after every ballot has been entered. Will recalculate
	 * every restaurant's voting percentage in the MasterResList
	 */
	private void RecalculatePercentage() {
		for (Restaurant<String, Integer, Double> res : MasterResList.values()) {
			res.recalculatePercent(MasterBallotList.size());
		}
	}

	public Map<Integer, Restaurant<String, Integer, Double>> getMasterResList() {
		return MasterResList;
	}

	private void printRestaurants() {
		System.out.println("***** PRINTING RESTAURANTS *****");
		for (Entry<Integer, Restaurant<String, Integer, Double>> e : MasterResList.entrySet()) {
			int k = e.getKey();
			Restaurant<String, Integer, Double> res = e.getValue();

			System.out.println("Item " + k + " " + res.toString());
		}
	}

	private void printBallotList() {
		System.out.println("***** PRINTING " + MasterBallotList.size() + " ENTERED BALLOTS *****");
		for (Passport pass : MasterBallotList) {
			System.out.println(pass.toString());
		}
	}
}