package Helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
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

	private static final String FILE_NAME = "/output/BalloutCounts_"
			+ new SimpleDateFormat("dd_MM_yyyy").format(new Date()) + ".xlsx";
	private File file;
	private XSSFWorkbook workbook;
	private XSSFSheet passport_sheet;
	private XSSFSheet comments_sheet;
	private XSSFSheet restaurant_sheet;

	private int rowNum; // The running count of the number of rows entered into the current workbook

	public BallotsToFile() {

		file = new File(FILE_NAME);
		workbook = new XSSFWorkbook();
		inializeWorkbook();
	}

	/**
	 * Creates the working Passport Entry workbook for all the processing of the
	 * current session. The file will remain open until the finish() method is
	 * called.
	 */
	private void inializeWorkbook() {

		passport_sheet = workbook.createSheet("Passport Counts");
		comments_sheet = workbook.createSheet("Comments");
		restaurant_sheet = workbook.createSheet("Restaurant Tally");

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

		}

		Row comment_row = comments_sheet.createRow(0);
		Cell c = comment_row.createCell(0);
		c.setCellValue("Comment");

		Row restaurant_row = restaurant_sheet.createRow(0);
		Cell c1 = restaurant_row.createCell(0);
		Cell c2 = restaurant_row.createCell(1);
		Cell c3 = restaurant_row.createCell(2);

		c1.setCellValue("Name");
		c2.setCellValue("Votes");
		c3.setCellValue("Voting Percentage");
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Method to insert ballots into the final file
	 * 
	 * @param pass An ArrayList of Passports to enter into the file
	 */
	public void writeBallots(ArrayList<Passport> pass) {

		for (Passport p : pass) {
			enterValue(passport_sheet, rowNum, 0, p.getAge());
			enterValue(passport_sheet, rowNum, 1, p.getGender());
			enterValue(passport_sheet, rowNum, 2, p.getPostal());
			enterValue(passport_sheet, rowNum, 3, p.getFoodie());

			enterValue(comments_sheet, rowNum, 0, p.getComments().getText());

			rowNum++;
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
	 * Method to insert the total restaurant tallies to the file
	 * 
	 * @param restaurants A map of the current Restaurant Count
	 */
	public void writeRestaurantTally(Map<Integer, Restaurant<String, Integer, Double>> restaurants) {
		for (Entry<Integer, Restaurant<String, Integer, Double>> e : restaurants.entrySet()) {
			int row = e.getKey();
			Restaurant<String, Integer, Double> r = e.getValue();
			enterValue(restaurant_sheet, row, 0, r.getLeft());
			enterValue(restaurant_sheet, row, 1, r.getMiddle().toString());
			enterValue(restaurant_sheet, row, 2, r.getRight().toString());

			try {
				FileOutputStream fos = new FileOutputStream(file);
				workbook.write(fos);
			} catch (FileNotFoundException e0) {
				e0.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}

	/**
	 * Internal helper method to enter a value entry into the file
	 * 
	 * @param sheet The workbook sheet you want to place the entry
	 * @param rowNum The row to insert into
	 * @param col The column to insert into
	 * @param value The value to insert
	 */
	private void enterValue(XSSFSheet sheet, int rowNum, int col, String value) {
		Row r = sheet.createRow(rowNum);
		Cell c = r.createCell(col);
		c.setCellValue(value);
	}

	/**
	 * Call to close the passport entry workbook. Should only be called once all
	 * processing has been completed.
	 */
	public void finish() {
		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}