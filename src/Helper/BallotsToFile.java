package Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Objects.Passport;
import Objects.Restaurant;

/**
 * This class is used to write a Passport Object to an already defined file. If
 * the file exists it will append, otherwise a new file will be created. The
 * final output of the file with display the [Age, Gender, Postal Code, Foodie
 * Vote] of each passport and a separate table showing the total tally of every
 * restaurant from all passed passports.
 * 
 * @author Data
 */
public class BallotsToFile {

	private LinkedHashMap<Restaurant<String, Integer, Double>, Integer> map; // Map to hold all passed passport
																				// restaurant votes

	private static final String FILE_NAME = "/output/BalloutCounts_"
			+ new SimpleDateFormat("dd_MM_yyyy").format(new Date()) + ".xlsx";
	private File file;
	private XSSFWorkbook workbook;

	private int rowNum; // The running count of the number of rows entered into the current workbook

	public BallotsToFile() {

		file = new File(FILE_NAME);
		workbook = new XSSFWorkbook();
	}

	/**
	 * Creates the working Passport Entry workbook for all the processing of the
	 * current session. The file will remain open until the finish() method is
	 * called.
	 */
	public void createFile() {

		XSSFSheet passport_sheet = workbook.createSheet("Passport Counts");

		Object[][] passports = { { "Age", "Gender", "Postal Code", "Foodie Vote" } };
		rowNum = 0;
		int colnum;
		for (Object[] rows : passports) {
			Row row = passport_sheet.createRow(rowNum++);
			colnum = 0;
			for (Object field : rows) {
				Cell cell = row.createCell(colnum++);
				if (field instanceof String)
					cell.setCellValue((String) field);
				else if (field instanceof Integer)
					cell.setCellValue((Integer) field);
			}

			try {
				FileOutputStream fos = new FileOutputStream(file);
				workbook.write(fos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Method to insert ballots into the final file
	 * 
	 * @param pass A vector of Passports to enter
	 */
	public void writeBallots(ArrayList<Passport> pass) {

		XSSFSheet sheet = workbook.getSheet("Passport Counts");
		int colNum;
		for (Passport p : pass) {
			colNum = 0;
			Row row = sheet.createRow(rowNum++);

			enterValue(row, colNum++, p.getAge());
			enterValue(row, colNum++, p.getGender());
			enterValue(row, colNum++, p.getPostal());
			enterValue(row, colNum++, p.getFoodie());

		}
	}

	/**
	 * Helper method to enter a ballot entry into the file
	 * 
	 * @param r the current row being inserted into
	 * @param col the column number of the current row being inserted into
	 * @param value the value you want to insert
	 */
	private void enterValue(Row r, int col, String value) {
		Cell c = r.createCell(col);
		c.setCellValue(value);
	}

	/**
	 * Pulls the vote map from a an already given Passport and either adds a new
	 * entry to the master map or appends their tallies to an existing entry
	 * 
	 * @param votes The LinkedHashMap of votes from a Passport
	 */
	@SuppressWarnings("unused")
	private void getResturantVotes(LinkedHashMap<Restaurant<String, Integer, Double>, Integer> votes) {
		if (!votes.isEmpty()) {
			Iterator<Entry<Restaurant<String, Integer, Double>, Integer>> it = votes.entrySet().iterator();
			while (it.hasNext()) {
				Entry<Restaurant<String, Integer, Double>, Integer> e = it.next();
				if (map.containsKey(e.getKey())) { // Entry exists
					int val = map.get(e.getKey());
					val += e.getValue();
					map.put(e.getKey(), val);
				} else // Entry not entered
					map.put(e.getKey(), e.getValue());
			}

		}
	}

	/**
	 * Call to finally close the passport entry workbook. Should only be called once
	 * all processing has been completed.
	 */
	public void finish() {
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}