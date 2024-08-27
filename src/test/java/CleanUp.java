import Framework_Methods.Web_Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class CleanUp {
    private static final Logger logger = LogManager.getLogger(CleanUp.class);
    public static void main(String[] args) {
        String excelFilePath = System.getProperty("user.dir") + "\\src\\test\\java\\Test_Data\\LMS_Admin_WorkFlows_For_DashBoard_And_Browser.xlsx";
        StringBuilder values = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("CleanUp"); // Assuming you're working with the first sheet
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from row 1 (which is A2)
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(0); // Column A corresponds to index 0
                    if (cell != null) {
                        values.append(cell.toString()).append(",");
                    }
                }
            }

            // Remove the trailing comma
            if (values.length() > 0) {
                values.setLength(values.length() - 1);
            }

            String result = values.toString();
            System.out.println("Comma-separated values: " + result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
