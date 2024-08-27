package Framework_Methods;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Playwright;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generic_Methods {

    private static final Logger logger = LogManager.getLogger(Generic_Methods.class);

    public static String getValueFromExcel(String inputFolderPath,String fileName, String sheetName, int Row, int Column) throws IOException {
        /*ZipSecureFile.setMinInflateRatio(0);
        File file = new File(System.getProperty("user.dir") + "\\src\\test\\java\\Test_Data\\" + fileName);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        String strReturnValue="Inovar";
        return strReturnValue;*/


        ZipSecureFile.setMinInflateRatio(0); // This is to prevent potential security issues with ZIP file inflation
        File file = new File(inputFolderPath+"\\" + fileName);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);

        // Ensure row and column indices are valid
        if (sheet == null) {
            workbook.close();
            throw new IllegalArgumentException("Sheet with name " + sheetName + " not found.");
        }

        Row row = sheet.getRow(Row - 1); // Adjust for 0-based indexing
        if (row == null) {
            workbook.close();
            throw new IllegalArgumentException("Row " + Row + " not found.");
        }

        Cell cell = row.getCell(Column - 1); // Adjust for 0-based indexing
        if (cell == null) {
            workbook.close();
            throw new IllegalArgumentException("Column " + Column + " not found in row " + Row);
        }

        // Get the cell value based on cell type
        String cellValue;
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                cellValue = cell.getCellFormula(); // For simplicity, just return the formula itself
                break;
            default:
                cellValue = "";
        }

        workbook.close();
        return cellValue;

    }

    public static String[] ListOfDataFiles(String folderPath) throws IOException {
        //String folderPath = System.getProperty("user.dir") + "\\src\\test\\java\\Test_Data";
        //closeFilesInFolder();
        File folder = new File(folderPath);
        String[] filenames = new String[0];
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                filenames = Arrays.stream(files)
                        .map(File::getName)
                        .toArray(String[]::new);
                for (String filename : filenames) {
                }
            } else {
                System.out.println("No files found in the folder.");
            }
        } else {
            System.out.println("Invalid folder path or folder does not exist.");
        }
        return filenames;
    }

    // Method to close all files in a given folder
    public static void closeFilesInFolder() throws IOException {
        String folderPath = System.getProperty("user.dir") + "\\src\\test\\java\\Test_Data";
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        closeFile(file);
                        System.out.println("File closed successfully: " + file.getName());
                    } catch (IOException e) {
                        System.err.println("Error closing file: " + file.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.err.println("No files found in the directory: " + folderPath);
        }
    }

    // Method to close a single file
    public static void closeFile(File file) throws IOException {
        boolean success = false;
        int maxTries = 5;
        int tries = 0;
        while (!success && tries < maxTries) {
            tries++;
            try (FileOutputStream fos = new FileOutputStream(file)) {
                // Perform any operations related to saving the file here if needed
                fos.write("Saved and closed.".getBytes());
                success = true; // If no exception is thrown, mark success as true
            } catch (IOException e) {
                if (tries >= maxTries) {
                    throw e; // If max tries exceeded, throw the exception
                }
                // Wait for a while before retrying
                try {
                    Thread.sleep(1000); // Wait for 1 second before retrying
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    public static void htmlReportCreate(String fileName) {
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Automation_Execution_Report</title>\n" +
                "    <link rel=\"stylesheet\" type=\"text/css\" href=\"https://cdn.datatables.net/1.10.25/css/jquery.dataTables.css\">\n" +
                "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js\"></script>\n" +
                "    <script src=\"https://cdn.datatables.net/1.10.25/js/jquery.dataTables.js\"></script>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" +
                "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.17.4/xlsx.full.min.js\"></script>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/chartjs-plugin-chartjs-3d\"></script>\n" +
                "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                "    <link rel=\"stylesheet\" href=\"https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            font-size: 12px;\n" +
                "            line-height: 1.6;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            background-color: #f8f9fa;\n" +
                "        }\n" +
                "        \n" +
                "        .container {\n" +
                "            display: flex;\n" +
                "            height: 100vh;\n" +
                "            flex-direction: column;\n" +
                "        }\n" +
                "        \n" +
                "        .top-panel {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            height: 1%;\n" +
                "            width: 99.9%;\n" +
                "        }\n" +
                "        \n" +
                "        .middle-panel {\n" +
                "            display: flex;\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            height: 25%;\n" +
                "            width: 99.9%;\n" +
                "        }\n" +
                "        \n" +
                "        .middle-panel-1 {\n" +
                "            border: 1px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            width: 32%;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        \n" +
                "        .middle-panel-2 {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            width: 20%;\n" +
                "            display: flex;\n" +
                "            align-items: normal;\n" +
                "        }\n" +
                "        \n" +
                "        .middle-panel-3 {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            width: 20%;\n" +
                "            display: flex;\n" +
                "            align-items: normal;\n" +
                "        }\n" +
                "        \n" +
                "        .middle-panel-4 {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 1px;\n" +
                "            width: 27%;\n" +
                "            display: inline-flex;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-panel {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            display: flex;\n" +
                "            height: 72%;\n" +
                "            width: 99.9%;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-left-column {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            flex: 55%;\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-left-column-top {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            flex: 47%;\n" +
                "            display: flex;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-left-column-bottom {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            flex: 47%;\n" +
                "            display: flex;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-middle-column {\n" +
                "            width: 1%;\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-right-column {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 1px;\n" +
                "            flex: 50%;\n" +
                "            flex-direction: column;\n" +
                "            display: inline-block;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-right-column-temptop {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 10px;\n" +
                "            flex: 10%;\n" +
                "            display: flex;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-right-column-top {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 5px;\n" +
                "            flex: 10%;\n" +
                "            display: flex;\n" +
                "            height: 90px;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-right-column-middle {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            flex: 15%;\n" +
                "            display: flex;\n" +
                "            overflow-y: auto;\n" +
                "            height: 35px;\n" +
                "        }\n" +
                "        \n" +
                "        .bottom-right-column-bottom {\n" +
                "            border: 0px solid #ccc;\n" +
                "            padding: 0px;\n" +
                "            flex: 18%;\n" +
                "            display: flex;\n" +
                "            overflow-y: auto;\n" +
                "            height: auto;\n" +
                "        }\n" +
                "        \n" +
                "        .table-container {\n" +
                "            max-height: 100%;\n" +
                "            overflow-y: auto;\n" +
                "        }\n" +
                "        \n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            border: 1px solid #ddd;\n" +
                "            font-family: 'Verdana', serif;\n" +
                "            font-size: 12px;\n" +
                "            margin-bottom: 6px;\n" +
                "            overflow-y: auto;\n" +
                "        }\n" +
                "        \n" +
                "        th {\n" +
                "            border: 1px solid #ddd;\n" +
                "            padding: 5px;\n" +
                "            text-align: center;\n" +
                "            font-family: 'Verdana', serif;\n" +
                "            font-size: 11px;\n" +
                "            background-color: #00008B;\n" +
                "            color: white;\n" +
                "            top: 0;\n" +
                "            position: sticky;\n" +
                "        }\n" +
                "        \n" +
                "        td {\n" +
                "            border: 1px solid #ddd;\n" +
                "            padding: 5px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        \n" +
                "        .tab {\n" +
                "            border: 0px solid #ccc;\n" +
                "            background-color: transparent;\n" +
                "        }\n" +
                "        \n" +
                "        .tab button {\n" +
                "            background-color: darkblue;\n" +
                "            border: none;\n" +
                "            outline: none;\n" +
                "            cursor: pointer;\n" +
                "            padding: 6px 19px;\n" +
                "            margin-right: 9px;\n" +
                "            font-size: 10px;\n" +
                "            color: white;\n" +
                "            border-radius: 15px;\n" +
                "            position: relative;\n" +
                "            transition: color 0.3s ease, background-color 0.3s ease;\n" +
                "        }\n" +
                "        \n" +
                "        .tab button.active {\n" +
                "            background-color: #007bff;\n" +
                "            color: white;\n" +
                "            box-shadow: 0 2px 2px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        \n" +
                "        .tab button:not(.active):hover {\n" +
                "            background-color: #f0f0f0;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        \n" +
                "        .tab button.active::before {\n" +
                "            content: '';\n" +
                "            position: absolute;\n" +
                "            bottom: 0;\n" +
                "            left: 50%;\n" +
                "            transform: translateX(-50%);\n" +
                "            width: 50%;\n" +
                "            height: 3px;\n" +
                "            background-color: #fff;\n" +
                "        }\n" +
                "        \n" +
                "        .tabcontent {\n" +
                "            display: block;\n" +
                "            width: 100%;\n" +
                "            height: 385px;\n" +
                "            overflow-y: auto;\n" +
                "            padding: 1px;\n" +
                "            border: 0px solid #ccc;\n" +
                "            border-top: none;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        \n" +
                "        h3 {\n" +
                "            font-size: 14px;\n" +
                "            font-weight: bold;\n" +
                "            color: darkblue;\n" +
                "            text-transform: uppercase;\n" +
                "            letter-spacing: 1px;\n" +
                "            text-align: center;\n" +
                "            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.4);\n" +
                "            margin: 0;\n" +
                "            padding: 1px 14px;\n" +
                "            transition: all 0.3s ease;\n" +
                "        }\n" +
                "        \n" +
                "        h3:hover {\n" +
                "            transform: scale(1.05);\n" +
                "        }\n" +
                "        \n" +
                "        a {\n" +
                "            text-decoration: none;\n" +
                "        }\n" +
                "        \n" +
                "        #Summary_Table th {\n" +
                "            height: 20px;\n" +
                "            line-height: 20px;\n" +
                "            padding-top: 3px;\n" +
                "            padding-bottom: 3px;\n" +
                "        }\n" +
                "        \n" +
                "        #Summary_Table td {\n" +
                "            height: 20px;\n" +
                "            line-height: 20px;\n" +
                "            padding-top: 3px;\n" +
                "            padding-bottom: 3px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"container\">\n" +
                "    <div class=\"top-panel\"></div>\n" +
                "    <div class=\"middle-panel\">\n" +
                "        <div class=\"middle-panel-1\">\n" +
                "            <table>\n" +
                "                <tr>\n" +
                "                    <th style=\"width: 45%; text-align: left; font-size: 10px;\">\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">E</span>XECUTED\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">B</span>Y\n" +
                "                    </th>\n" +
                "                    <td style=\"width: 65%; text-align: left;\">Executed_By</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th style=\"width: 45%; text-align: left; font-size: 10px;\">\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">E</span>XECUTION\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">D</span>ATE\n" +
                "                    </th>\n" +
                "                    <td style=\"width: 65%; text-align: left;\">Execution_Date</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th style=\"width: 45%; text-align: left; font-size: 10px;\">\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">T</span>OTAL\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">E</span>XECUTIONS\n" +
                "                    </th>\n" +
                "                    <td style=\"width: 65%; text-align: left;\">Total_Summary_Executions</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th style=\"width: 45%; text-align: left; font-size: 10px;\">\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">P</span>ASSED\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">E</span>XECUTIONS\n" +
                "                    </th>\n" +
                "                    <td style=\"width: 65%; text-align: left;\">Passed_Summary_Executions</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th style=\"width: 45%; text-align: left; font-size: 10px;\">\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">F</span>AILED\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">E</span>XECUTIONS\n" +
                "                    </th>\n" +
                "                    <td style=\"width: 65%; text-align: left;\">Failed_Summary_Executions</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th style=\"width: 45%; text-align: left; font-size: 10px;\">\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">E</span>XPORT\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.25em;\">E</span>XECUTIONS\n" +
                "                    </th>\n" +
                "                    <td style=\"width: 65%; text-align: left;\">\n" +
                "                        <button id=\"exportButton\">Export to Excel</button>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "        <div class=\"middle-panel-2\">\n" +
                "            <div id=\"piechart_3d\" style=\"width: 18%; height: 10%;\">\n" +
                "                <script type=\"text/javascript\">\n" +
                "                    google.charts.load('current', {'packages': ['corechart']});\n" +
                "                    google.charts.setOnLoadCallback(drawChart);\n" +
                "                    function drawChart() {\n" +
                "                        var data = new google.visualization.DataTable();\n" +
                "                        data.addColumn('string', 'Color');\n" +
                "                        data.addColumn('number', 'Value');\n" +
                "                        data.addRows([\n" +
                "                            ['Pass', Passed_Summary_Executions],\n" +
                "                            ['Fail', Failed_Summary_Executions]\n" +
                "                        ]);\n" +
                "                        var options = {\n" +
                "                            width: 300,\n" +
                "                            height: 185,\n" +
                "                            is3D: true,\n" +
                "                            colors: ['green', 'red'],\n" +
                "                            legend: {\n" +
                "                                position: 'bottom'\n" +
                "                            },\n" +
                "                            chartArea: {\n" +
                "                                backgroundColor: 'transparent', // Remove background color\n" +
                "                                width: '90%',\n" +
                "                                height: '80%'\n" +
                "                            }\n" +
                "                        };\n" +
                "                        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));\n" +
                "                        chart.draw(data, options);\n" +
                "                    }\n" +
                "                </script>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"middle-panel-3\">\n" +
                "            <div id=\"columnchart_2d\" style=\"width: 18%; height: 10%;\">\n" +
                "                <script type=\"text/javascript\">\n" +
                "                    google.charts.load(\"current\", {packages: ['corechart']});\n" +
                "                    google.charts.setOnLoadCallback(drawChart);\n" +
                "                    function drawChart() {\n" +
                "                        var data = google.visualization.arrayToDataTable([\n" +
                "                            [\"Element\", \"Count\", {role: \"style\"}, {role: \"annotation\"}],\n" +
                "                            [\"Pass\", Passed_Summary_Executions, \"green\", Passed_Summary_Executions],\n" +
                "                            [\"Fail\", Failed_Summary_Executions, \"red\", Failed_Summary_Executions]\n" +
                "                        ]);\n" +
                "                        var options = {\n" +
                "                            width: 300,\n" +
                "                            height: 185,\n" +
                "                            bar: {groupWidth: \"50%\"},\n" +
                "                            legend: {position: \"none\"},\n" +
                "                            tooltip: {isHtml: true},\n" +
                "                            annotations: {\n" +
                "                                alwaysOutside: false,\n" +
                "                                textStyle: {\n" +
                "                                    fontSize: 12,\n" +
                "                                    bold: true,\n" +
                "                                    italic: false,\n" +
                "                                    color: '#333'\n" +
                "                                }\n" +
                "                            }\n" +
                "                        };\n" +
                "                        var chart = new google.visualization.ColumnChart(document.getElementById(\"columnchart_2d\"));\n" +
                "                        chart.draw(data, options);\n" +
                "                        setTimeout(function () {\n" +
                "                            var chartContainer = document.getElementById('columnchart_2d');\n" +
                "                            var labels = chartContainer.getElementsByTagName('text');\n" +
                "                            for (var i = 0; i < labels.length; i++) {\n" +
                "                                if (labels[i].getAttribute('text-anchor') === 'middle') {\n" +
                "                                    var y = parseFloat(labels[i].getAttribute('y'));\n" +
                "                                    labels[i].setAttribute('y', y + 15);\n" +
                "                                }\n" +
                "                            }\n" +
                "                        }, 100);\n" +
                "                    }\n" +
                "                </script>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"middle-panel-4\">\n" +
                "            &nbsp;&nbsp;&nbsp;&nbsp;<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAG4AAAAlCAYAAACjxNxUAAAAAXNSR0IArs4c6QAAAERlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAA6ABAAMAAAABAAEAAKACAAQAAAABAAAAbqADAAQAAAABAAAAJQAAAAB4BHDhAAAH1ElEQVRoBe1aaWxVRRR+BQtYJIASNwIoqFERiSagsrSNoGCxIrJpVAIhrmDApSkuCAV+IAQVBSMxhoBLJEbEhRAxKAQRWkhQmooQ2ypKRQRpkX3p8/se77yeTmfundeSGM09ycecOdvMPefO3HlTYrGI/jcZuBpPshAYF/BEvaCbDwwMsHGpboHiVeBml0GAvCV0VyZ9O6HNCLDVKtqWAZVAT60I40fGy1r0LS9e3rei+FDf8pKiEPuO0M8DxofYhamZV+a3d5ih1u9EJ57EEK1I8i3Q7k/qT6F9MCn3abJgdARg/INAK8CHsmH0MXAYkLmxZayVQC4QRE9DKX4bgwxNXf/Kkpw+5ZviCVQU1+ZXbeEzuKgYChnnfpdRiJwv2clkHOaoW4h9Si0Ds308Ja1jLgCrbU6j7zvJ7oZv17qwVq4NpB8aPnpsza+BXXtrlFisjxHjdoddA3Fu1ZYOfcqLq1k4rLzNDQzqBHlg9Xx2oN+8Tu3NXWfEWezrqQf3KRztufLu8xggncK1RbxtgJ5PGP8D7Ds75sHCiv83DhurOHt3Sad+FSX5uXvLzrManBFuQiPxpfV9oc2wK1Qsrr6gVZ7ylUHZ+haOtizevakodiadwq1CCD0X8pRNAIYCBcAngGnDVdEMMCkXAm07wDRoQn+QEVvG+RFy21zChuKOIDHYen2XtUM6haMfizcKcJG5Dbi2yuEIoOfxF/q5jqB8yN8AbW+bN903KLt1FJwl0nH1PMj77ETmNG6EQMfJMQ1sfe1gS4D5jXsRQfidEz8u7RG2wJD5rrgSFY+xw06g1POlkTlUgbdRHoRiwzbXZpSmjCdAHZPb3O9KxtNsuqtusvJnbOY8lPQkfArHVTMGMIvHVWOSz4q7FE61gMxjiRnE0V+ofOh7rcNuq7L7ymGTCflMYBEQlrT1sJG5VoNvB0xSMuqCdiGo69H16B0AJGZlPW1ARxzY+haO4cYCZvGGUaHIZ8WZb7DtBVAhU+zd4PTcH01p6jPcDbRd//rqRI/fT7FZYNGL6FZlR/vpScW5aPWq4yEr6DcnV+Rg4G3gGCBjs30A8CLtZCscT1Z6W7pERR0HXq+WE+gzoUI+K240jPUceotzSHuV4feEw55J2q5svzTsWqO/V+kLDb3urlV2stpE/5TS8XnyRGFp+WNbP7Pw5twsrnUicWJrKxwt3wBYoGXsGDQefbN4Q5M2PoUzf3MFPbAeOgcdPXe+RC4aA4W25ZhCz4AR3RHwF4rCaM3xigx9Fvp/ABKL26eNzoHwMCB20q6AjC+RN4kjW1fhGKxtQMSHoNPFO45+PuBTuA6w03Pgd8aHXoKR9ssOcGKyKpT9F0lbM9mvBMRYo/xrwLez2PKl+wlY7dCLi95W+QwrRZFOqx8+qHBhMR+GgVm8KZDp+DzY2GgzhGLHt54f7CC6DMqDgPjsAh/0TYE6xm+g2LPlyVRvb0fR158BdFPUD5z2nZHSNI7h50THIz8q3VA6QFMKx3GZHF08HZu8q3A81Ghbro6egI26QbgT0PY+iWwJn93KjytIv/mvoe8iriAZj6utvcswDfkcFZOx/wauScM/NSE6N7VwHJcx5CHN1lU4rhZ9zKYfT1s8dXHL5epgyxMf5Tru9+i3AnzoSRhpX+EZs6MjAMcWO7YzHXbpipvD4WtAx+YhiodBL9KO1sIVFpUOKSgqfbNgRll/r4ix2ETY6bjCuwrHsHyLtzn8xN9secPC06Uv8eP/J2DG4W9CF30EhdjXgOc8vaimcPiEA1OGv179wsjLHQ4XQa53AY7zgc22mU0YJCucXdY5Ho99GovHH4nFT6+aO3ePz8mHK2NyUFyL7gBkg4BVFp1NtBHCGwBum77E09yrhvEJ9GcbMuleDOYu6aDldsp5hlL1lBEja2PxBcjbxNip0+85HHgK5beNt09Co8H0kI60tsJVihJtg6uj+Mna1vFYPOGXEc/IPJy1P+wQIOHmg+FRW4iT49seRPzm5AFM1gZAPxC6iTefcn5Ls4FfgHTpZTjsUE7cjn9Vfc2ycDyRkvgNCjp1JozkH+SsjfA4N7Wo4xtwfB6dJ666Q6aVTELLxyQdS9F+rhXk50ztsb1gRul4vDmDM2LNFk+f0L1BUNNH9eeB3w3wNmAxwIf3oc9gRGQBvQA+OMfdBTBeU+gonIcA04B9wHOAi76D4jEgH5gFcGv2onateiytOVbaFVXo0jzTuaIlFlcy5zUMeAeoBCKKMhBlIMpAlIEoA1EGogxEGYgyEGUgygAz4PvjOcqWPQNdIM40VLwEoFwTb0T4m5W/m/mnrePAdoDEu0j+LpXfhGafNg2IF5sRNT4DY+F6EzAV4OUAr6Z45bYMaAvwCo7YAxwE3geuAPjnoonAaiAHGABsAkgDk+AVnpP4BkTU+AzwGo/UG5gFcCWdD/CPp88Dmu5EZz0gd6MsGIvaKIoK16i0hTp1hcU0ZcWrvsHAW0q2Lsnzuu0OQLbXjuADVxv9osIxC2efqhByiQrLe0dezOvvIbdSXlqTWMRFCe7MtsniBVJUuMD0NFp5DJ4/G97fon8PsBWIA5MAbp0k/veMfQnO8+I9KlwyW01smHgh/lcNbpXvigDtUoAHk2eB5UANUAKsBW4D+G0UOglG90Uetf9yBniS57YZUZSBKAP/mQz8Axn19RZS4LZDAAAAAElFTkSuQmCC\" alt=\"Inovar Logo\" width=\"150\" height=\"50\" style=\"margin-top: 20px;\">\n" +
                "            <h3 style=\"text-align: right; font-size: 18px;\"><span\n" +
                "                    style=\"text-transform: uppercase; font-size: 1.15em;\">&emsp;&emsp;T</span>EST<span\n" +
                "                    style=\"text-transform: uppercase; font-size: 1.15em;\">&ensp;A</span>UTOMATION<span\n" +
                "                    style=\"text-transform: uppercase; font-size: 1.15em;\">&ensp;S</span>UMMARY&ensp;</h3>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <div class=\"bottom-panel\">\n" +
                "        <div class=\"bottom-left-column\">\n" +
                "            <div class=\"bottom-left-column-top\">\n" +
                "                <table id=\"Summary_Table\" class=\"display\">\n" +
                "                    <thead>\n" +
                "                    <tr>\n" +
                "                        <th style=\"width: 100px; text-align: center; font-size: 10px;\">\n" +
                "                            <span style=\"text-transform: uppercase; font-size: 1.25em;\">W</span>ORKFLOWS\n" +
                "                        </th>\n" +
                "                        <th style=\"width: 100px; text-align: center; font-size: 10px;\">\n" +
                "                            <span style=\"text-transform: uppercase; font-size: 1.25em;\">B</span>ROWSER\n" +
                "                        </th>\n" +
                "                        <th style=\"width: 100px; text-align: center; font-size: 10px;\">\n" +
                "                            <span style=\"text-transform: uppercase; font-size: 1.25em;\">S</span>TART\n" +
                "                            <span style=\"text-transform: uppercase; font-size: 1.25em;\">T</span>IME\n" +
                "                        </th>\n" +
                "                        <th style=\"width: 100px; text-align: center; font-size: 10px;\">\n" +
                "                            <span style=\"text-transform: uppercase; font-size: 1.25em;\">E</span>ND\n" +
                "                            <span style=\"text-transform: uppercase; font-size: 1.25em;\">T</span>IME\n" +
                "                        </th>\n" +
                "                        <th style=\"width: 100px; text-align: center; font-size: 10px;\">\n" +
                "                            <span style=\"text-transform: uppercase; font-size: 1.25em;\">D</span>URATION\n" +
                "                        </th>\n" +
                "                        <th style=\"width: 100px; text-align: center; font-size: 10px;\">\n" +
                "                            <span style=\"text-transform: uppercase; font-size: 1.25em;\">S</span>TATUS\n" +
                "                        </th>\n" +
                "                    </tr>\n" +
                "                    </thead>\n" +
                "                    <tbody>\n" +
                "                    summarytablesreplacetext\n" +
                "                    </tbody>\n" +
                "                </table>\n" +
                "            </div>\n" +
                "            <script>\n" +
                "                $(document).ready(function () {\n" +
                "                    $('#Summary_Table').DataTable({\n" +
                "                        \"lengthMenu\": [[5, 10, 15], [5, 10, 15]],\n" +
                "                        \"columnDefs\": [\n" +
                "                            {\"targets\": 2, \"orderable\": true} // Enable sorting on the third column (index 2)\n" +
                "                        ],\n" +
                "                        \"order\": [] // Clear any existing sorting states\n" +
                "                    });\n" +
                "                    $(\"#firstLinkClick\").click(function () {\n" +
                "                        console.log(\"Link clicked...\");\n" +
                "                    });\n" +
                "                });\n" +
                "            </script>\n" +
                "        </div>\n" +
                "        <div class=\"bottom-middle-column\"></div>\n" +
                "        <div class=\"bottom-right-column\">\n" +
                "            <p style=\"text-align: right; font-size: 0.55em;\">Copyright © 2024 Inovar Tech&nbsp;&nbsp;&nbsp;</p>\n" +
                "            <div class=\"bottom-right-column-top\">\n" +
                "                wfstablereplace\n" +
                "            </div>\n" +
                "            <div class=\"bottom-right-column-middle\">\n" +
                "                <div class=\"tab\">\n" +
                "                    <button class=\"tablinks\" onclick=\"openTab(event, 'testCases')\"><span\n" +
                "                            style=\"text-transform: uppercase; font-size: 1.5em;\">T</span>EST\n" +
                "                        <span style=\"text-transform: uppercase; font-size: 1.5em;\">C</span>ASES\n" +
                "                    </button>\n" +
                "                    <button class=\"tablinks\" onclick=\"openTab(event, 'logs')\"><span\n" +
                "                            style=\"text-transform: uppercase; font-size: 1.5em;\">L</span>OGS\n" +
                "                    </button>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"bottom-right-column-bottom\">\n" +
                "                <div id=\"testCases\" class=\"tabcontent\">\n" +
                "                    testcasetablereplace\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div id=\"logs\" class=\"tabcontent\">\n" +
                "                logtablereplace\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<script>\n" +
                "    var passCountsTestCases = 0;\n" +
                "    var failCountsTestCases = 0;\n" +
                "</script>\n" +
                "<script>\n" +
                "    function toggleTable(...ids) {\n" +
                "        var tables = document.querySelectorAll('.toggle-table');\n" +
                "        tables.forEach(function (table) {\n" +
                "            table.style.display = 'none';\n" +
                "        });\n" +
                "        ids.forEach(function (id) {\n" +
                "            var tableToShow = document.getElementById(id);\n" +
                "            if (tableToShow) {\n" +
                "                tableToShow.style.display = 'table';\n" +
                "            }\n" +
                "        });\n" +
                "    }\n" +
                "</script>\n" +
                "<script>\n" +
                "    function toggleTable(testCasesTableId, logsTableId, wfsTableId) {\n" +
                "        var tables = document.querySelectorAll('.toggle-table');\n" +
                "        tables.forEach(function (table) {\n" +
                "            table.style.display = 'none';\n" +
                "        });\n" +
                "        document.getElementById(testCasesTableId).style.display = 'table';\n" +
                "        document.getElementById(wfsTableId).style.display = 'table';\n" +
                "        document.getElementById(logsTableId).style.display = 'table';\n" +
                "        passCountsTestCases = countTextInColumns(testCasesTableId, \"Pass\", \"passCountsTestCases\");\n" +
                "        failCountsTestCases = countTextInColumns(testCasesTableId, \"Fail\", \"failCountsTestCases\");\n" +
                "        var passCountsLogs = countTextInColumns(logsTableId, \"Pass\", \"passCountsLogs\");\n" +
                "        var failCountsLogs = countTextInColumns(logsTableId, \"Fail\", \"failCountsLogs\");\n" +
                "    }\n" +
                "</script>\n" +
                "<script>\n" +
                "    function countTextInColumns(tableName, searchText, targetDivId) {\n" +
                "        var table = document.getElementById(tableName);\n" +
                "        var counts = [0, 0, 0];\n" +
                "        for (var i = 0; i < table.rows.length; i++) {\n" +
                "            for (var j = 0; j < table.rows[i].cells.length; j++) {\n" +
                "                if (table.rows[i].cells[j].innerText.trim() === searchText) {\n" +
                "                    counts[j]++;\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        var targetDiv = document.getElementById(targetDivId);\n" +
                "        targetDiv.innerHTML = \"\";\n" +
                "        var heading = document.createElement(\"h1\");\n" +
                "        heading.textContent = searchText + \"ed Test Cases\";\n" +
                "        targetDiv.appendChild(heading);\n" +
                "        for (var i = 2; i < counts.length; i++) {\n" +
                "            var heading = document.createElement(\"h2\");\n" +
                "            heading.style.textAlign = \"center\";\n" +
                "            heading.textContent = counts[i];\n" +
                "            targetDiv.appendChild(heading);\n" +
                "        }\n" +
                "        return counts;\n" +
                "    }\n" +
                "</script>\n" +
                "<script>\n" +
                "    function openTab(evt, tabName) {\n" +
                "        var i, tabcontent, tablinks;\n" +
                "        tabcontent = document.getElementsByClassName(\"tabcontent\");\n" +
                "        for (i = 0; i < tabcontent.length; i++) {\n" +
                "            tabcontent[i].style.display = \"none\";\n" +
                "        }\n" +
                "        tablinks = document.getElementsByClassName(\"tablinks\");\n" +
                "        for (i = 0; i < tablinks.length; i++) {\n" +
                "            tablinks[i].className = tablinks[i].className.replace(\" active\", \"\");\n" +
                "        }\n" +
                "        document.getElementById(tabName).style.display = \"block\";\n" +
                "        evt.currentTarget.className += \" active\";\n" +
                "    }\n" +
                "</script>\n" +
                "<script>\n" +
                "    function displayImage(base64) {\n" +
                "        var width = 1000;\n" +
                "        var height = 500;\n" +
                "        var left = (window.innerWidth - width) / 2;\n" +
                "        var top = (window.innerHeight - height) / 2;\n" +
                "        var newWindow = window.open(\"\", \"_blank\", \"width=\" + width + \",height=\" + height + \",left=\" + left + \",top=\" + top + \",toolbar=no,location=no,menubar=no,status=no,titlebar=no\");\n" +
                "        newWindow.document.write(\"<style>body {margin: 0;}</style><img src='\" + base64 + \"' style='max-width: 100%; max-height: 100%;'>\");\n" +
                "    }\n" +
                "</script>\n" +
                "<script>\n" +
                "    document.getElementById(\"exportButton\").addEventListener(\"click\", async function () {\n" +
                "        var wb = XLSX.utils.book_new();\n" +
                "        await exportTableToExcel(\"Summary_Table\", \"Summary\", wb);\n" +
                "        await exportTablesWithPrefixToExcel(\"Table_For_Logs_\", \"Logs\", wb);\n" +
                "        await exportTablesWithPrefixToExcel(\"Table_For_TestCases_\", \"TCs\", wb);\n" +
                "        XLSX.writeFile(wb, 'Export_Execution_Report.xlsx');\n" +
                "    });\n" +
                "\n" +
                "    async function exportTableToExcel(tableId, baseSheetName, workbook) {\n" +
                "        var allData = [];\n" +
                "        var isFirstPage = true;\n" +
                "        var headerData = [];\n" +
                "        var tableSelector = `#${tableId}`;\n" +
                "        var nextPageSelector = '#Summary_Table_next'; // Adjust this selector as needed\n" +
                "\n" +
                "        while (true) {\n" +
                "            console.log(`Collecting data from ${isFirstPage ? 'first' : 'subsequent'} page`);\n" +
                "\n" +
                "            var table = document.querySelector(tableSelector);\n" +
                "            if (!table) {\n" +
                "                console.error(`Table with ID ${tableId} not found.`);\n" +
                "                break;\n" +
                "            }\n" +
                "\n" +
                "            var wsData = [];\n" +
                "            var rows = Array.from(table.querySelectorAll('tr'));\n" +
                "            if (rows.length === 0) {\n" +
                "                console.log(\"No rows found in the table.\");\n" +
                "                break;\n" +
                "            }\n" +
                "\n" +
                "            rows.forEach(function (row, index) {\n" +
                "                var rowData = [];\n" +
                "                row.querySelectorAll('th, td').forEach(function (cell) {\n" +
                "                    var cellText = cell.textContent.trim();\n" +
                "                    rowData.push(cellText);\n" +
                "                });\n" +
                "\n" +
                "                if (index === 0) {\n" +
                "                    // Capture the header row only from the first page\n" +
                "                    if (isFirstPage) {\n" +
                "                        headerData = rowData;\n" +
                "                    }\n" +
                "                } else {\n" +
                "                    // Collect data rows\n" +
                "                    wsData.push(rowData);\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "            // Add header row only once from the first page\n" +
                "            if (isFirstPage && headerData.length > 0) {\n" +
                "                allData.push(headerData);\n" +
                "            }\n" +
                "            allData = allData.concat(wsData);\n" +
                "\n" +
                "            var nextPageLink = document.querySelector(nextPageSelector);\n" +
                "            if (nextPageLink && !nextPageLink.classList.contains('disabled')) {\n" +
                "                console.log(\"Navigating to the next page.\");\n" +
                "                nextPageLink.click();\n" +
                "                await waitForPageLoad();\n" +
                "            } else {\n" +
                "                console.log(\"No more pages or pagination control not found.\");\n" +
                "                break;\n" +
                "            }\n" +
                "\n" +
                "            isFirstPage = false; // After the first page, all subsequent pages should not include headers\n" +
                "        }\n" +
                "\n" +
                "        if (allData.length > 0) {\n" +
                "            var ws = XLSX.utils.aoa_to_sheet(allData);\n" +
                "            Object.keys(ws).forEach(function (cellRef) {\n" +
                "                var cell = ws[cellRef];\n" +
                "                if (cell && cell.t === 's' && cell.v) {\n" +
                "                    cell.s = {\n" +
                "                        alignment: {horizontal: 'left', vertical: 'center'},\n" +
                "                        font: {sz: 12, bold: false, underline: false, italic: false}\n" +
                "                    };\n" +
                "                }\n" +
                "            });\n" +
                "\n" +
                "            var sheetName = generateUniqueSheetName(workbook, baseSheetName);\n" +
                "            XLSX.utils.book_append_sheet(workbook, ws, sheetName);\n" +
                "        } else {\n" +
                "            console.error(`No data collected from table with ID: ${tableId}`);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    async function waitForPageLoad() {\n" +
                "        await new Promise(resolve => setTimeout(resolve, 3000)); // 3 seconds, adjust as necessary\n" +
                "    }\n" +
                "\n" +
                "    async function exportTablesWithPrefixToExcel(tablePrefix, baseSheetNamePrefix, workbook) {\n" +
                "        var tables = document.querySelectorAll(`[id^=\"${tablePrefix}\"]`);\n" +
                "        for (var i = 0; i < tables.length; i++) {\n" +
                "            var table = tables[i];\n" +
                "            var constVariable = getConstantVariable(table.id);\n" +
                "            var baseSheetName = baseSheetNamePrefix + \"_\" + constVariable;\n" +
                "            var sheetName = generateUniqueSheetName(workbook, baseSheetName);\n" +
                "            await exportTableToExcel(table.id, sheetName, workbook);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    function generateUniqueSheetName(workbook, baseSheetName) {\n" +
                "        var sheetName = baseSheetName;\n" +
                "        var index = 1;\n" +
                "        while (workbook.SheetNames.includes(sheetName)) {\n" +
                "            sheetName = baseSheetName + \"_\" + index;\n" +
                "            index++;\n" +
                "        }\n" +
                "        return sheetName;\n" +
                "    }\n" +
                "\n" +
                "    function getConstantVariable(tableId) {\n" +
                "        return tableId.substring(tableId.lastIndexOf('_') + 1); // Example logic\n" +
                "    }\n" +
                "</script>\n" +
                "<script>\n" +
                "    const Constants = {\n" +
                "        createtestcasesconstantVariables\n" +
                "        createlogssconstantVariables\n" +
                "    }\n" +
                "    function getConstantVariable(tableName) {return Constants[tableName] || \"\";}\n" +
                "</script>\n" +
                "<script>\n" +
                "    document.addEventListener(\"DOMContentLoaded\", function () {\n" +
                "        var xpathExpressionForTab = '//*[@id=\"firstLinkClick\"]'; // Replace with your actual XPath\n" +
                "        var tabButton = document.evaluate(xpathExpressionForTab, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n" +
                "        if (tabButton) { tabButton.click();} else {console.log(\"Tab button not found or XPath is incorrect.\");}\n" +
                "        var xpathExpressionForTab = '/html/body/div/div[3]/div[3]/div[2]/div/button[1]'; // Replace with your actual XPath\n" +
                "        var tabButton = document.evaluate(xpathExpressionForTab, document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;\n" +
                "        if (tabButton) {tabButton.click();} else {console.log(\"Tab button not found or XPath is incorrect.\");}\n" +
                "    });\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>\n";
        //String filePathAndName = fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(htmlContent);
//System.out.println("HTML file created successfully at: " + filePathAndName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getBrowserNameForExportFile(String browserName) {
        BrowserType browserType;
        switch (browserName.toUpperCase().trim()) {
            case "CHROME":
                browserName = "C";
                break;
            case "EDGE":
                browserName = "E";
                break;
            case "CHROME_HEADLESS":
                browserName = "CHL";
                break;
            case "EDGE_HEADLESS":
                browserName = "EHL";
                break;
            case "FIREFOX":
                browserName = "F";
                break;
            case "FIREFOX_HEADLESS":
                browserName = "FHL";
                break;
        }
        return browserName;
    }


    //writeSummaryInfoIntoHtmlReport
    public static void writeSummaryInfoIntoHtmlReport(String excelFileName, String htmlFileName) throws IOException {
        FileInputStream fis = new FileInputStream(excelFileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet xs = wb.getSheet("Summary_Info");
        int passedTests = 0;
        int failedTests = 0;
        int rowNumber = xs.getLastRowNum();
        // System.out.println(rowNumber);
        String valueForTable = "";
        String valueTCsConstVariable = "";
        String valueLogsConstVariable = "";
//String valueWFDescConstVariable = "";
        for (int methodsCount = 1; methodsCount <= rowNumber; methodsCount++) {
            valueForTable = valueForTable + "<tr>";
// Row row = xs.getRow(methodsCount);
            Row row = xs.getRow(methodsCount);
            for (int cell = 1; cell < row.getLastCellNum(); cell++) {
                Cell cellValue = row.getCell(cell);
                if (cellValue.toString().equals("PASSED")) {
                    passedTests++;
                } else if (cellValue.toString().equals("FAILED")) {
                    failedTests++;
                }
                if (cell == 1) {
                    if (methodsCount == 1) {
                        valueForTable = valueForTable + "\n<td style=\"width: 35%; text-align: left; overflow-wrap: anywhere;\">\n<a id=\"firstLinkClick\" href=\"#\" onclick=\"(function(){ toggleTable('" + "Table_For_TestCases_" + row.getCell(0) + "','" + "Table_For_Logs_" + row.getCell(0) + "','" + "Table_For_WFs_Desc_" + row.getCell(1) + "'); countPassTextInColumns('Table_For_TestCases_" + row.getCell(0) + "'); return false; })();\">" + cellValue + "</a>\n</td>";
                        valueTCsConstVariable = valueTCsConstVariable + "Table_For_TestCases_" + row.getCell(0) + ":" + "\"" + cellValue;
                        valueLogsConstVariable = valueLogsConstVariable + "Table_For_Logs_" + row.getCell(0) + ":" + "\"" + cellValue;
                        //valueWFDescConstVariable=valueWFDescConstVariable + "Table_For_WFDesc_" + row.getCell(0) + ":" + "\"" + cellValue;
                    } else {
                        valueForTable = valueForTable + "\n<td style=\"width: 35%; text-align: left; overflow-wrap: anywhere;\">\n<a href=\"#\" onclick=\"(function(){ toggleTable('" + "Table_For_TestCases_" + row.getCell(0) + "','" + "Table_For_Logs_" + row.getCell(0) + "','" + "Table_For_WFs_Desc_" + row.getCell(1) + "'); countPassTextInColumns('Table_For_TestCases_" + row.getCell(0) + "'); return false; })();\">" + cellValue + "</a>\n</td>";
                        valueTCsConstVariable = valueTCsConstVariable + "Table_For_TestCases_" + row.getCell(0) + ":" + "\"" + cellValue;
                        valueLogsConstVariable = valueLogsConstVariable + "Table_For_Logs_" + row.getCell(0) + ":" + "\"" + cellValue;
                        //valueWFDescConstVariable=valueWFDescConstVariable + "Table_For_WFDesc_" + row.getCell(0) + ":" + "\"" + cellValue;
                    }
                } else if (cell == 3) {
                    valueForTable = valueForTable + "\n<td style=\"width: 10%; text-align: center;\">\n<a href=\"#\" onclick=\"(function(){ toggleTable('" + "Table_For_TestCases_" + row.getCell(0) + "','" + "Table_For_Logs_" + row.getCell(0) + "','" + "Table_For_WFs_Desc_" + row.getCell(1) + "'); countPassTextInColumns('Table_For_TestCases_" + row.getCell(0) + "'); return false; })();\">" + cellValue + "</a>\n</td>";
                    valueTCsConstVariable = valueTCsConstVariable + "_" + getBrowserNameForExportFile(String.valueOf(cellValue)) + "\",\n";
                    valueLogsConstVariable = valueLogsConstVariable + "_" + getBrowserNameForExportFile(String.valueOf(cellValue)) + "\",\n";
                    //valueWFDescConstVariable=valueWFDescConstVariable + "_" + getBrowserNameForExportFile(String.valueOf(cellValue)) + "\",\n";
                } else if (cell == 4) {
                    valueForTable = valueForTable + "\n<td style=\"width: 20%; text-align: center;\">\n<a href=\"#\" onclick=\"(function(){ toggleTable('" + "Table_For_TestCases_" + row.getCell(0) + "','" + "Table_For_Logs_" + row.getCell(0) + "','" + "Table_For_WFs_Desc_" + row.getCell(1) + "'); countPassTextInColumns('Table_For_TestCases_" + row.getCell(0) + "'); return false; })();\">" + cellValue + "</a>\n</td>";
                } else if (cell == 5) {
                    valueForTable = valueForTable + "\n<td style=\"width: 15%; text-align: center;\">\n<a href=\"#\" onclick=\"(function(){ toggleTable('" + "Table_For_TestCases_" + row.getCell(0) + "','" + "Table_For_Logs_" + row.getCell(0) + "','" + "Table_For_WFs_Desc_" + row.getCell(1) + "'); countPassTextInColumns('Table_For_TestCases_" + row.getCell(0) + "'); return false; })();\">" + cellValue + "</a>\n</td>";
                } else if (cell == 6) {
                    valueForTable = valueForTable + "\n<td style=\"width: 10%; text-align: center;\">\n<a href=\"#\" onclick=\"(function(){ toggleTable('" + "Table_For_TestCases_" + row.getCell(0) + "','" + "Table_For_Logs_" + row.getCell(0) + "','" + "Table_For_WFs_Desc_" + row.getCell(1) + "'); countPassTextInColumns('Table_For_TestCases_" + row.getCell(0) + "'); return false; })();\">" + cellValue + "</a>\n</td>";
                } else if (cell == 7) {
                    valueForTable = valueForTable + "\n<td style=\"width: 10%; text-align: center;\">\n<a href=\"#\" onclick=\"(function(){ toggleTable('" + "Table_For_TestCases_" + row.getCell(0) + "','" + "Table_For_Logs_" + row.getCell(0) + "','" + "Table_For_WFs_Desc_" + row.getCell(1) + "'); countPassTextInColumns('Table_For_TestCases_" + row.getCell(0) + "'); return false; })();\">" + cellValue + "</a>\n</td>";
                }
            }
            valueForTable = valueForTable + "\n</tr>";
        }
        replaceTextInHTMLReport(htmlFileName, "summarytablesreplacetext", valueForTable + "summarytablesreplacetext");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String current_Date = dateFormat.format(Calendar.getInstance().getTime());
        replaceTextInHTMLReport(htmlFileName, "Execution_Date", current_Date);
        replaceTextInHTMLReport(htmlFileName, "Executed_By", System.getProperty("user.name"));
        replaceTextInHTMLReport(htmlFileName, "Total_Summary_Executions", String.valueOf(passedTests + failedTests));
        replaceTextInHTMLReport(htmlFileName, "Passed_Summary_Executions", String.valueOf(passedTests));
        replaceTextInHTMLReport(htmlFileName, "Failed_Summary_Executions", String.valueOf(failedTests));
        replaceTextInHTMLReport(htmlFileName, "createtestcasesconstantVariables", valueTCsConstVariable + "createtestcasesconstantVariables");
        replaceTextInHTMLReport(htmlFileName, "createlogssconstantVariables", valueLogsConstVariable + "createlogssconstantVariables");
//replaceTextInHTMLReport(htmlFileName, "createwfsconstantVariables", valueWFDescConstVariable + "createwfsconstantVariables");
    }


    public static void writeTestCasesInfoIntoHtmlReport(String excelFileName, String htmlFileName, String threadID) throws IOException {
        FileInputStream fis = new FileInputStream(excelFileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet xs = wb.getSheet("TestCases_Info");

        Set<String> uniqueValues = new HashSet<>();
        for (int testCases = 1; testCases <= xs.getLastRowNum(); testCases++) {
            Row row = xs.getRow(testCases);
            Cell cellValue = row.getCell(0);

            // Check if the threadID matches before adding to uniqueValues
            if (row.getCell(0).toString().equals(threadID)) {
                uniqueValues.add(cellValue.toString());
            }
        }

        String valueForTable = null;
        for (String value : uniqueValues) {
            valueForTable = "<table border=\"1\" id=\"Table_For_TestCases_" + value + "\" class=\"toggle-table\">\n<tr>\n<th><span style=\"text-transform: uppercase; font-size: 1.25em;\">T</span>EST <span style=\"text-transform: uppercase; font-size: 1.25em;\">C</span>ASES</th>\n<th><span style=\"text-transform: uppercase; font-size: 1.25em;\">I</span>MAGES</th>\n<th><span style=\"text-transform: uppercase; font-size: 1.25em;\">S</span>TATUS</th></tr>\n";
            for (int testCases = 1; testCases <= xs.getLastRowNum(); testCases++) {
                Row row = xs.getRow(testCases);

                // Process only rows with matching threadID
                if (threadID.equals(row.getCell(0).toString())) {
                    valueForTable = valueForTable + "<tr>\n";
                    for (int testCaseCol = 1; testCaseCol < row.getLastCellNum(); testCaseCol++) {
                        Cell cellValue = row.getCell(testCaseCol);
                        if (testCaseCol == 1) {
                            valueForTable = valueForTable + "<td style=\"width: 70%; text-align: left; overflow-wrap: anywhere;\">" + cellValue.toString() + "</td>\n";
                        } else if (testCaseCol == 2) {
                            if (cellValue.toString().isEmpty()) {
                                valueForTable = valueForTable + "<td>" + "" + "</td>\n";
                            } else {
                                // Read image file
                                File file = new File(cellValue.toString());
                                FileInputStream imageFile = new FileInputStream(file);
                                byte[] imageData = new byte[(int) file.length()];
                                imageFile.read(imageData);
                                imageFile.close();

                                // Convert image to Base64
                                String base64Image = Base64.getEncoder().encodeToString(imageData);
                                valueForTable = valueForTable + "<td><a href=\"#\" onclick=\"displayImage('data:image/png;base64," + base64Image + "')\">Image</a></td>";
                            }
                        } else {
                            valueForTable = valueForTable + "<td>" + cellValue.toString() + "</td>\n";
                        }
                    }
                    valueForTable = valueForTable + "</tr>\n";
                }
            }
            valueForTable = valueForTable + "</table>\n";
            replaceTextInHTMLReport(htmlFileName, "testcasetablereplace", valueForTable + "testcasetablereplace");
        }
        wb.close();
        fis.close();
    }

    public static void writeLogsInfoIntoHtmlReport(String excelFileName, String htmlFileName, String threadID) throws IOException {
        FileInputStream fis = new FileInputStream(excelFileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet xs = wb.getSheet("Logs_Info");

        Set<String> uniqueValues = new HashSet<>();
        for (int logs = 1; logs <= xs.getLastRowNum(); logs++) {
            Row row = xs.getRow(logs);
            Cell cellValue = row.getCell(0);

            // Check if the threadID matches before adding to uniqueValues
            if (row.getCell(0).toString().equals(threadID)) {
                uniqueValues.add(cellValue.toString());
            }
        }

        String valueForTable = null;
        for (String value : uniqueValues) {
            valueForTable = "<table border=\"1\" id=\"Table_For_Logs_" + value + "\" class=\"toggle-table\"><tr>\n<th style=\"width: 15%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">D</span>ATE</th>\n<th style=\"width: 10%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">T</span>IME</th>\n<th style=\"width: 15%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">L</span>OGTYPE</th>\n<th style=\"width: 60%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">D</span>ESCRIPTION</th>\n</tr>\n";
            for (int logs = 1; logs <= xs.getLastRowNum(); logs++) {
                Row row = xs.getRow(logs);

                // Process only rows with matching threadID
                if (threadID.equals(row.getCell(0).toString())) {
                    valueForTable = valueForTable + "<tr>\n";
                    for (int logs_Cols = 1; logs_Cols < row.getLastCellNum(); logs_Cols++) {
                        Cell cellValue = row.getCell(logs_Cols);
                        if (logs_Cols == 4) {
                            valueForTable = valueForTable + "<td style=\"width: 60%; text-align: left; overflow-wrap: anywhere;\">" + cellValue.toString() + "</td>";
                        } else if (logs_Cols == 3) {
                            valueForTable = valueForTable + "<td style=\"width: 15%; text-align: center;\">" + cellValue.toString() + "</td>";
                        } else if (logs_Cols == 2) {
                            valueForTable = valueForTable + "<td style=\"width: 10%; text-align: center;\">" + cellValue.toString() + "</td>";
                        } else if (logs_Cols == 1) {
                            valueForTable = valueForTable + "<td style=\"width: 15%; text-align: center;\">" + cellValue.toString() + "</td>";
                        }
                    }
                    valueForTable = valueForTable + "\n</tr>\n";
                }
            }
            valueForTable = valueForTable + "</table>\n";
            replaceTextInHTMLReport(htmlFileName, "logtablereplace", valueForTable + "logtablereplace");
        }
        wb.close();
        fis.close();
    }

    /*//writeTestCasesInfoIntoHtmlReport
    public static void writeTestCasesInfoIntoHtmlReport(String excelFileName, String htmlFileName,String threadID) throws IOException {
        FileInputStream fis = new FileInputStream(excelFileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet xs = wb.getSheet("TestCases_Info");
        int passedTestCases = 0;
        int failedTestCases = 0;
        // Create a Set to store unique values
        Set<String> uniqueValues = new HashSet<>();
        for (int testCases = 1; testCases <= xs.getLastRowNum(); testCases++) {
        // Row row = xs.getRow(methodsCount);
            Row row = xs.getRow(testCases);
            Cell cellValue = row.getCell(0);
            uniqueValues.add(cellValue.toString());
        }
        String valueForTable = null;
        for (String value : uniqueValues) {
            valueForTable = "<table border=\"1\" id=\"Table_For_TestCases_" + value + "\" class=\"toggle-table\">\n<tr>\n<th><span style=\"text-transform: uppercase; font-size: 1.25em;\">T</span>EST <span style=\"text-transform: uppercase; font-size: 1.25em;\">C</span>ASES</th>\n<th><span style=\"text-transform: uppercase; font-size: 1.25em;\">I</span>MAGES</th>\n<th><span style=\"text-transform: uppercase; font-size: 1.25em;\">S</span>TATUS</th></tr>\n";
            for (int testCases = 1; testCases <= xs.getLastRowNum(); testCases++) {
                Row row = xs.getRow(testCases);
                if (value.equals(row.getCell(0).toString())) {
                    valueForTable = valueForTable + "<tr>\n";
                    for (int testCaseCol = 1; testCaseCol < row.getLastCellNum(); testCaseCol++) {

                        Cell cellValue = row.getCell(testCaseCol);
                        if (testCaseCol == 1) {
                            valueForTable = valueForTable + "<td style=\"width: 70%; text-align: left; overflow-wrap: anywhere;\">" + cellValue.toString() + "</td>\n";
                        } else if (testCaseCol == 2) {
                            if (cellValue.toString().isEmpty()) {
                                valueForTable = valueForTable + "<td>" + "" + "</td>\n";
                            } else {
// Read image file
                                File file = new File(cellValue.toString());
                                FileInputStream imageFile = new FileInputStream(file);
                                byte[] imageData = new byte[(int) file.length()];
                                imageFile.read(imageData);
                                imageFile.close();
// Convert image to Base64
                                String base64Image = Base64.getEncoder().encodeToString(imageData);
                                valueForTable = valueForTable + "<td><a href=\"#\" onclick=\"displayImage('data:image/png;base64," + base64Image + "')\">Image</a></td>";
                            }
                        } else {
                            valueForTable = valueForTable + "<td>" + cellValue.toString() + "</td>\n";
                        }
                    }
                    valueForTable = valueForTable + "</tr>\n";
                }
            }
            valueForTable = valueForTable + "</table>\n";
            replaceTextInHTMLReport(htmlFileName, "testcasetablereplace", valueForTable + "testcasetablereplace");
        }
    }*/

    /*//writeLogsInfoIntoHtmlReport
    public static void writeLogsInfoIntoHtmlReport(String excelFileName, String htmlFileName,String threadID) throws IOException {
        FileInputStream fis = new FileInputStream(excelFileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet xs = wb.getSheet("Logs_Info");
        // Create a Set to store unique values
        Set<String> uniqueValues = new HashSet<>();
        for (int logs = 1; logs <= xs.getLastRowNum(); logs++) {
        // Row row = xs.getRow(methodsCount);
            Row row = xs.getRow(logs);
            Cell cellValue = row.getCell(0);
            uniqueValues.add(cellValue.toString());
        }
        String valueForTable = null;
        for (String value : uniqueValues) {
            valueForTable = "<table border=\"1\" id=\"Table_For_Logs_" + value + "\" class=\"toggle-table\"><tr>\n<th style=\"width: 15%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">D</span>ATE</th>\n<th style=\"width: 10%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">T</span>IME</th>\n<th style=\"width: 15%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">L</span>OGTYPE</th>\n<th style=\"width: 60%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">D</span>ESCRIPTION</th>\n</tr>\n";
            for (int logs = 1; logs <= xs.getLastRowNum(); logs++) {
                Row row = xs.getRow(logs);
                if (value.equals(row.getCell(0).toString())) {
                    valueForTable = valueForTable + "<tr>\n";
                    for (int logs_Cols = 1; logs_Cols < row.getLastCellNum(); logs_Cols++) {
                        Cell cellValue = row.getCell(logs_Cols);
                        if (logs_Cols == 4) {
                            valueForTable = valueForTable + "<td style=\"width: 60%; text-align: left; overflow-wrap: anywhere;\">" + cellValue.toString() + "</td>";
                        } else if (logs_Cols == 3) {
                            valueForTable = valueForTable + "<td style=\"width: 15%; text-align: center;\">" + cellValue.toString() + "</td>";
                        } else if (logs_Cols == 2) {
                            valueForTable = valueForTable + "<td style=\"width: 10%; text-align: center;\">" + cellValue.toString() + "</td>";
                        } else if (logs_Cols == 1) {
                            valueForTable = valueForTable + "<td style=\"width: 15%; text-align: center;\">" + cellValue.toString() + "</td>";
                        }
                    }
                    valueForTable = valueForTable + "\n</tr>\n";
                }
            }
            valueForTable = valueForTable + "</table>\n";
            replaceTextInHTMLReport(htmlFileName, "logtablereplace", valueForTable + "logtablereplace");
        }
    }*/


    //writeWFsInfoIntoHtmlReport
    public static void writeWFsInfoIntoHtmlReport(String excelFileName, String htmlFileName, String executionDataSheetPathAndName) throws IOException {
        FileInputStream fis = new FileInputStream(excelFileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet xs = wb.getSheet("Summary_Info");
        // Create a Set to store unique values
        Set<String> uniqueValues = new HashSet<>();
        for (int wfs = 1; wfs <= xs.getLastRowNum(); wfs++) {
            // Row row = xs.getRow(methodsCount);
            Row row = xs.getRow(wfs);
            Cell cellValue = row.getCell(1);
            uniqueValues.add(cellValue.toString());
        }

        for (String value : uniqueValues) {
            String valueForTable = null;
            valueForTable = "<table border=\"1\" id=\"Table_For_WFs_Desc_" + value + "\" class=\"toggle-table\"><tr>\n<th style=\"height: 1%;width: 30%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">W</span>ORKFLOW</th>\n<th style=\"height: 1%;width: 70%; text-align: center;\"><span style=\"text-transform: uppercase; font-size: 1.25em;\">D</span>ESCRIPTION</th>\n</tr>\n";
            FileInputStream fis_WFs = new FileInputStream(executionDataSheetPathAndName);
            XSSFWorkbook wb_WFs = new XSSFWorkbook(fis_WFs);
            XSSFSheet xs_Wfs = wb_WFs.getSheet(value);
            Row row_WFs_Code = xs_Wfs.getRow(1);
            Cell cellValue_WFs_Code = row_WFs_Code.getCell(1);
            valueForTable = valueForTable + "</tr>\n<td style=\"height: 100%;width: 30%; text-align: left; overflow-wrap: anywhere;vertical-align: middle;\">" + value + "</td>";
            Row row_WFs_Desc = xs_Wfs.getRow(2);
            Cell cellValue_WFs_Desc = row_WFs_Desc.getCell(1);
            valueForTable = valueForTable + "<td style=\"height: 100%;width: 70%; text-align: left; overflow-wrap: anywhere;vertical-align: middle;\">" + cellValue_WFs_Desc.toString() + "</td>\n</tr>\n";
            valueForTable = valueForTable + "</table>\n";
            replaceTextInHTMLReport(htmlFileName, "wfstablereplace", valueForTable + "wfstablereplace");
        }
    }


    //replaceTextInHTMLReport
    public static void replaceTextInHTMLReport(String htmlFileName, String sourceText, String destinationText) throws IOException {
        try {
// Read the content of the HTML file
            BufferedReader reader = new BufferedReader(new FileReader(htmlFileName));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            String fileContent = stringBuilder.toString();
            reader.close();
// Perform text replacement
            String modifiedContent = fileContent.replace(sourceText, destinationText);
// Write the modified content back to the HTML file
            BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFileName));
            writer.write(modifiedContent);
            writer.close();
//System.out.println("Text replaced successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void writeSummaryInfoIntoExcel(String logFileName, String sheetName, String[] logAndSummaryInfo) throws IOException, InterruptedException {
        ZipSecureFile.setMinInflateRatio(0);
        File file = new File(logFileName);
        Thread.sleep(2000);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        int lastRowNum = sheet.getLastRowNum();
        Row row = sheet.createRow(++lastRowNum);
        for (int i = 0; i < logAndSummaryInfo.length; i++) {
            Cell cell_threadID = row.createCell(i);
            cell_threadID.setCellValue(logAndSummaryInfo[i]);
        }
// Write the modified workbook content back to the same file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
//System.out.println("Data added to the existing Excel file.");
        }
    }

    public static synchronized void writeLogsInfoIntoExcel(String logFileName, String sheetName, String threadID, String logInfo, String logDescription) throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        File file = new File(logFileName);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        int lastRowNum = sheet.getLastRowNum();
        Row row = sheet.createRow(++lastRowNum);
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String[] arrLogValues = {threadID, String.valueOf(date.format(formatter)), String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))), logInfo, logDescription};
        for (int i = 0; i < arrLogValues.length; i++) {
            Cell cell_threadID = row.createCell(i);
            cell_threadID.setCellValue(arrLogValues[i]);
        }
// Write the modified workbook content back to the same file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
//System.out.println("Data added to the existing Excel file.");
        }
    }

    //writeTestCasesInfoIntoExcel
    public static synchronized void writeTestCasesInfoIntoExcel(String excelFileName, String sheetName, String threadID, String testCase, String imageString, String testCaseStatus) throws IOException {
        ZipSecureFile.setMinInflateRatio(0);
        File file = new File(excelFileName);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        int lastRowNum = sheet.getLastRowNum();
        Row row = sheet.createRow(++lastRowNum);
        String[] arrLogValues = {threadID, testCase, imageString, testCaseStatus};
        for (int i = 0; i < arrLogValues.length; i++) {
            Cell cell_threadID = row.createCell(i);
            cell_threadID.setCellValue(arrLogValues[i]);
        }
// Write the modified workbook content back to the same file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
//System.out.println("Data added to the existing Excel file.");
        }
    }

    public static void writeDataIntoExcelCell(String logFileName) throws IOException {
// ZipSecureFile.setMinInflateRatio(0);
        File file = new File(System.getProperty("user.dir") + "\\src\\test\\java\\Reports\\" + logFileName);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        Row row = sheet.createRow(++lastRowNum);
        Cell cell = row.createCell(0);
        cell.setCellValue("test");
// Write the modified workbook content back to the same file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
//System.out.println("Data added to the existing Excel file.");
        }
    }

    public static void createExcelLogFile(String logFileName) throws IOException {
        try {
            File f = new File(logFileName);
            if (f.exists() && !f.isDirectory()) {
                System.out.println("Excel file already exists");
            } else {
                FileOutputStream fos = new FileOutputStream(logFileName);
                XSSFWorkbook workbook = new XSSFWorkbook();
                //Need to identify proper newly created excel sheet.
                XSSFSheet sheet = workbook.createSheet("Summary_Info");
                Row row = sheet.createRow(0);
                String arr[] = {"Thread_ID", "Workflow_Code", "Workflow_Description", "Browser", "Start_Time", "End_Time", "Duration", "Status"};
                //HashMap<String, String> logData = new HashMap();
                sheet.setZoom(100);
                for (int cell = 0; cell < arr.length; cell++) {
                    Cell cellValue = row.createCell(cell);
                    cellValue.setCellValue(arr[cell]);
                    sheet.setColumnWidth(cell, 5000);
                }
                XSSFSheet sheetForTestCases = workbook.createSheet("TestCases_Info");
                Row rowValueForTestCases = sheetForTestCases.createRow(0);
                String[] arrForTestCasesHeaders = {"Thread_ID", "Test_Case", "Image", "Status"};
                sheet.setZoom(100);
                for (int cell = 0; cell < arrForTestCasesHeaders.length; cell++) {
                    Cell cellValue = rowValueForTestCases.createCell(cell);
                    cellValue.setCellValue(arrForTestCasesHeaders[cell]);
                    sheetForTestCases.setColumnWidth(cell, 5000);
                }
                XSSFSheet sheetForLogs = workbook.createSheet("Logs_Info");
                Row rowValueForLogs = sheetForLogs.createRow(0);
                String arrForLogHeaders[] = {"Thread_ID", "Date", "Time", "Log_Type", "Description"};
                sheet.setZoom(100);
                for (int cell = 0; cell < arrForLogHeaders.length; cell++) {
                    Cell cellValue = rowValueForLogs.createCell(cell);
                    cellValue.setCellValue(arrForLogHeaders[cell]);
                    sheetForLogs.setColumnWidth(cell, 5000);
                }
                workbook.write(fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    static String globalPropertiesFileNameAndPath = System.getProperty("user.dir") + "\\src\\main\\java\\GlobalProperties\\global.properties";
    static String HTMLLogInfoPropertiesFileNameAndPath = System.getProperty("user.dir") + "\\src\\main\\java\\GlobalProperties\\HTML_Log_Info.properties";

    public static boolean isColumnAvaiInExcel(String dataSheetFile, String dataSheetName, int rowNumber, String columnHeader) throws IOException, InvalidFormatException {
        File file = new File(dataSheetFile);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(dataSheetName);
        Row row = sheet.getRow(rowNumber);
        boolean bool = false;
        for (int column = 0; column < row.getLastCellNum(); column++) {
            Cell celll = row.getCell(column);
            String columnVal = celll.toString();
            if (columnVal.equalsIgnoreCase(columnHeader)) {
                bool = true;
                break;
            } else {
                bool = false;
            }
        }
        workbook.close();
        return bool;
    }

    public static int findColumnPositionInExcel(String dataSheetFile, String dataSheetName, int rowNumber, String columnHeader) throws IOException, InvalidFormatException {
        File file = new File(dataSheetFile);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet(dataSheetName);
        Row row = sheet.getRow(rowNumber);
        int columnPosition = -1;
        try {
            for (int column = 0; column < row.getLastCellNum(); column++) {
                Cell cell = row.getCell(column);
                String columnVal = cell.toString();
                if (columnVal.equalsIgnoreCase(columnHeader)) {
                    columnPosition = column;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        workbook.close();
        return columnPosition;
    }

    public static void writeDataIntoExcelCell(int colValue, int rowValue, String sheetName) throws IOException {
// ZipSecureFile.setMinInflateRatio(0);
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\Test_Data\\TestData.xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet xs = wb.getSheet(sheetName);
        FileOutputStream fos = null;
        XSSFCell columnNumber = null;
        XSSFRow rowNumber = null;
        int colNum = colValue;
        rowNumber = xs.getRow(rowValue);
        if (rowNumber == null) {
            rowNumber = xs.createRow(rowValue);
        }
        columnNumber = rowNumber.getCell(colNum);
        if (columnNumber == null) {
            columnNumber = rowNumber.createCell(colNum);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String current_Date = dateFormat.format(Calendar.getInstance().getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        TimeZone etTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
        timeFormat.setTimeZone(etTimeZone);
        String current_Time = timeFormat.format(Calendar.getInstance().getTime());
        columnNumber.setCellValue(current_Time);
//System.out.println(current_Time);
        fos = new FileOutputStream(System.getProperty("user.dir") + "\\src\\main\\java\\Test_Data\\TestData.xlsx");
        wb.write(fos);
        wb.close();
        fos.close();
    }

    public static String[] readExecutionMethodsFromExcel(String filePath, String fileName, String sheetName) throws IOException {
        FileInputStream file = new FileInputStream(System.getProperty("user.dir") + "\\" + filePath + "\\" + fileName);
        XSSFWorkbook xwb = new XSSFWorkbook(file);
//Need to change below sheetName to executedScriptName
        String[] arr_ExecutionMethods = new String[0];
        try {
            XSSFSheet xs = xwb.getSheet(sheetName);
            arr_ExecutionMethods = new String[xs.getLastRowNum() + 1];
            for (int i = 0; i <= xs.getLastRowNum(); i++) {
                Row row = xs.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    arr_ExecutionMethods[i] = String.valueOf(row.getCell(j));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            writeLog("Error", "Execution sheet not found in Execution Data File");
            writeLog("Error", String.valueOf(e));
            arr_ExecutionMethods = null;
        }
        return arr_ExecutionMethods;
    }


    public static String executionTimeDiffCalculation(String StartTime, String EndTime) throws IOException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException, ParseException {
        // Parse start and end time strings into Date objects
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date startTime = format.parse(StartTime);
        Date endTime = format.parse(EndTime);

        // Handle the case where endTime is before startTime (i.e., the time span crosses midnight)
        if (endTime.before(startTime)) {
            // Add one day to endTime
            endTime = new Date(endTime.getTime() + TimeUnit.DAYS.toMillis(1));
        }
        // Calculate time difference in milliseconds
        long timeDifference = endTime.getTime() - startTime.getTime();
        // Format time difference as HH:mm:ss
        long hours = TimeUnit.MILLISECONDS.toHours(timeDifference);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /*public static String TimeDiffCalculation(Date StartTime, Date EndTime) throws IOException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        long timeDifference = EndTime.getTime() - StartTime.getTime();
//System.out.println(StartTime);
//System.out.println(EndTime);

        String TimeTaken = String.format("%s:%s:%s", Long.toString(TimeUnit.MILLISECONDS.toHours(Math.round(timeDifference))), TimeUnit.MILLISECONDS.toMinutes(Math.round(timeDifference)), TimeUnit.MILLISECONDS.toSeconds(Math.round(timeDifference)));
//System.out.println(String.format("Time taken %s", TimeTaken));
        return TimeTaken;
    }*/

    public static boolean isSheetAvailableInExcel(String fileName, String sheetName) throws IOException, InvalidFormatException {
        File file = new File(fileName);
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        boolean sheetAvaliableStatus = false;
        if (workbook.getNumberOfSheets() != 0) {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                if (workbook.getSheetName(i).equals(sheetName)) {
                    sheetAvaliableStatus = true;
                    break;
                } else {
                    sheetAvaliableStatus = false;
                }
            }
        }
        workbook.close();
        return sheetAvaliableStatus;
    }

    public static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
    }

    public static void createCustomDataAndTimeStampForFileNames(String dataTimeStamp) throws IOException {
        WriteDataIntoPropertiesFile(globalPropertiesFileNameAndPath, "customDataAndTimeStampForFileNames", String.valueOf(dataTimeStamp));

        WriteDataIntoPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath, "Scripts_Execution_Start_Time", getCustomTime());
    }

    public static void createReportFile() throws IOException {
        String logFileName = "Report_" + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("customDataAndTimeStampForFileNames");
        WriteDataIntoPropertiesFile(globalPropertiesFileNameAndPath, "executionReportFileName", logFileName);
    }

    public static void replaceTextInHTML(String text, String replacement) throws IOException {

        Path path = Paths.get(System.getProperty("user.dir") + "\\src\\main\\java\\Reports\\" + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("executionReportFileName") + ".html");
// Get all the lines
        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
// Do the replace operation
            List<String> list = stream.map(line -> line.replace(text, replacement)).collect(Collectors.toList());
// Write the content back
            Files.write(path, list, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String customDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("dd_MMM_YYYY_HH_mm_ss");
        Date date = new Date();
        dateFormat.format(date);
        String dateandtime = dateFormat.format(date).toString();
        return dateandtime;
    }

    public static String getCustomTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        TimeZone etTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
        timeFormat.setTimeZone(etTimeZone);
        String current_Time = timeFormat.format(Calendar.getInstance().getTime());
        return current_Time;
    }

    public static String getCustomDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-YYYY");
        Date date = new Date();
        dateFormat.format(date);
        String dateandtime = dateFormat.format(date).toString();
        return dateandtime;
    }

    public static Properties ReadDataFromPropertiesFile(String propertiesFileName) throws IOException {
        FileInputStream readPropertyFile = new FileInputStream(propertiesFileName);
        Properties pro = new Properties();
        pro.load(readPropertyFile);
        return pro;
    }


    //need to update below pageload method with default time
    public void pageLoadReadyState(WebDriver driver, String PageName) throws InterruptedException, IOException {
        String expectedPageTitle = "Test";
        Duration.ofSeconds(5);
        do {
            if (driver.getTitle().equalsIgnoreCase(PageName)) {
                break;
            }
        } while (!driver.getTitle().equalsIgnoreCase(expectedPageTitle));
    }

    public static void elementAvailabilityState(WebDriver driver, WebElement element, String elementName) throws InterruptedException {
        WebDriverWait wdWait = new WebDriverWait(driver, 5000);
        wdWait.until(ExpectedConditions.presenceOfElementLocated(By.linkText(elementName)));
    }

    public void stableZoomSizeBrowserWindow() throws InterruptedException, AWTException {
        Thread.sleep(1000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_0);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_0);
    }

    public void zoomOutBrowserWindowWithRobotClass() throws InterruptedException, AWTException {
        Thread.sleep(1000);
        Robot robot = new Robot();
        for (int i = 0; i < 3; i++) {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_SUBTRACT);
            robot.keyRelease(KeyEvent.VK_SUBTRACT);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }
    }

    public static void WriteDataIntoPropertiesFile(String requiredPropertiesFileName, String propertiesKey, String propertiesValue) throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(requiredPropertiesFileName);
        properties.load(fis);
        properties.setProperty(propertiesKey, propertiesValue);
        FileOutputStream fos = new FileOutputStream(requiredPropertiesFileName);
        properties.store(fos, "LogFileFormat ==> Options ==> TextFile,Excel" + "\n" + "LogDataAppendedToExistingFileorNot ==> Options ==> Yes, No" + "\n" + "If LogFileFormate is null then by default TextLogs will be created.");
    }

    public static void writeLog(String Category, String LogString) throws IOException {
        boolean defaultLog = true;
        String arrlogFormats[] = new String[]{};
        String logFormats = ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFileFormat");
        if (logFormats.contains(",")) {
            arrlogFormats = logFormats.split(",");
            for (int logFormatInit = 0; logFormatInit < arrlogFormats.length; logFormatInit++) {
                if (arrlogFormats[logFormatInit].contains("Excel")) {
                    writeLogInToExcelFile(Category, LogString);
                }
                if (arrlogFormats[logFormatInit].contains("TextFile")) {
                    writeLogInToTextFile(Category, LogString);
                }
            }
        } else if (logFormats.equalsIgnoreCase("Excel")) {
            writeLogInToExcelFile(Category, LogString);
        } else if (logFormats.equalsIgnoreCase("TextFile")) {
            writeLogInToTextFile(Category, LogString);
        } else if (logFormats.equalsIgnoreCase("") && defaultLog) {
            writeLogInToTextFile(Category, LogString);
        }
        String propertiesValue = "<table border=1 width=100%><tr><td>" + getCustomDate() + "</td><td>" + getCustomTime() + "</td></tr></table>";
        WriteDataIntoPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath, "ReportLogInfo", propertiesValue);
    }

    public static void createTextLogFile(String logFileName) throws IOException {
        File myObj = new File(System.getProperty("user.dir") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFilePath") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFileName") + ".txt");
        if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
            PrintWriter out = new PrintWriter(System.getProperty("user.dir") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFilePath") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFileName") + ".txt"); // Step 2
            out.println("Date" + "\t\t\t" + "Time" + "\t\t\t" + "Category" + "\t\t" + "Description");
            out.close();
        } else {
            System.out.println("File already exists.");
        }
    }

    public static void createTempReportTextFile() throws IOException {
        File myObj = new File(System.getProperty("user.dir") + "\\" + "createTempReportTextFile.txt");
        if (myObj.createNewFile()) {
//System.out.println("File created: " + myObj.getName());
            PrintWriter out = new PrintWriter(System.getProperty("user.dir") + "\\" + "createTempReportTextFile.txt"); // Step 2
            out.println("");
            out.close();
        } else {
//System.out.println("File already exists.");
        }
    }

    /*public static void createLogFile() throws IOException {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        Process process;
        if (isWindows) {
            process = Runtime.getRuntime().exec(String.format("taskkill /f /im excel.exe"));
            process = Runtime.getRuntime().exec(String.format("taskkill /f /im notepad.exe"));
        }
        String logFileName = "Logs_" + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("customDataAndTimeStampForFileNames");
        if (ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogDataAppendedToExistingFileorNot").equalsIgnoreCase("Yes")) {
            System.out.println("Data is appended to existing log file");
        } else {
            WriteDataIntoPropertiesFile(globalPropertiesFileNameAndPath, "LogFileName", logFileName);
        }
        String arrlogFormats[] = new String[]{};
        String logFormats = ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFileFormat");
        if (logFormats.contains(",")) {
            arrlogFormats = logFormats.split(",");
            for (int logFormatInit = 0; logFormatInit < arrlogFormats.length; logFormatInit++) {
                if (arrlogFormats[logFormatInit].contains("Excel")) {
                    createExcelLogFile(logFileName);
                }
                if (arrlogFormats[logFormatInit].contains("TextFile")) {
                    createTextLogFile(logFileName);
                }
            }
        } else if (logFormats.equalsIgnoreCase("Excel")) {
            createExcelLogFile(logFileName);
        } else if (logFormats.equalsIgnoreCase("TextFile")) {
            createTextLogFile(logFileName);
        }
    }*/

    public static void writeLogFileHeader(String logFileName) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(System.getProperty("user.dir") + "\\src\\main\\java\\Logs\\" + logFileName + ".txt"); // Step 2
        out.println("Date" + "\t\t\t" + "Time" + "\t\t\t" + "Category" + "\t\t" + "Description");
        out.close();
    }

    public static void writeLogInToTextFile(String Category, String LogString) throws IOException {
        /*Categories - Info, Warning, Error*/
        FileWriter fw = new FileWriter(System.getProperty("user.dir") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFilePath") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFileName") + ".txt", true);
        PrintWriter out = new PrintWriter(fw);
        String customDate = getCustomDate();
        String customTime = getCustomTime();
        out.println(customDate + "\t\t" + customTime + "\t\t" + Category + "\t\t\t" + LogString);
        out.close();
        replaceTextInHTML("Scripts_Execution_Logs", " <TR><TD style=\"text-align: center\">" + customDate + "</TD><TD style=\"text-align: center\">" + customTime + "</TD><TD style=\"text-align: center\">" + Category + "</TD><TD>&nbsp;" + LogString + "</TD></TR>" + "Scripts_Execution_Logs");
    }

    public static void writeLogInToExcelFile(String Category, String LogString) throws IOException {
// ZipSecureFile.setMinInflateRatio(0);
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFilePath") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFileName") + ".xlsx");
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet xs = wb.getSheet("Logs");
        FileOutputStream fos = null;
        XSSFCell columnNumber = null;
        Row row = xs.createRow(xs.getLastRowNum() + 1);
        String arrLogCellValues[] = {getCustomDate(), getCustomTime(), Category, LogString};
        for (int writeLogCellValues = 0; writeLogCellValues <= 3; writeLogCellValues++) {
            Cell cellDate = row.createCell(writeLogCellValues);
            cellDate.setCellValue(arrLogCellValues[writeLogCellValues]);
        }
        fos = new FileOutputStream(System.getProperty("user.dir") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFilePath") + ReadDataFromPropertiesFile(globalPropertiesFileNameAndPath).getProperty("LogFileName") + ".xlsx");
        wb.write(fos);
        wb.close();
        fos.close();
    }

    public static void HTMLExecutionSummary() throws IOException {
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\Test_Data\\TestData.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheet("Executions");
        int rowNumber = sheet.getLastRowNum();
        String strPropFileValue = null;
        int passedCount = 0;
        int failedCount = 0;
        int notexecutedCount = 0;
        int totalExecutedCount = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row1 = sheet.getRow(i);
            for (int j = 0; j <= row1.getLastCellNum(); j++) {
                strPropFileValue = ReadDataFromPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath).getProperty("HTMLReportExecutionMethodsInfo");
                Cell celll = row1.getCell(j);
                if (i == 0) {
                    WriteDataIntoPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath, "HTMLReportExecutionMethodsInfo", strPropFileValue + "<td style=\"text-align: center\"><b>" + String.valueOf(celll) + "<b></td>");
                } else if (j == 0 && i > 0) {
                    WriteDataIntoPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath, "HTMLReportExecutionMethodsInfo", strPropFileValue + "<td style=\"text-align: Left\">" + String.valueOf(celll) + "</td>");
                } else {
                    WriteDataIntoPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath, "HTMLReportExecutionMethodsInfo", strPropFileValue + "<td style=\"text-align: center\">" + String.valueOf(celll) + "</td>");
                }
            }
            WriteDataIntoPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath, "HTMLReportExecutionMethodsInfo", strPropFileValue + "</TR>");
        }
        WriteDataIntoPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath, "HTMLReportExecutionMethodsInfo", strPropFileValue + "</TABLE>");
        replaceTextInHTML("Current_Execution_Methods_List", ReadDataFromPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath).getProperty("HTMLReportExecutionMethodsInfo"));
        WriteDataIntoPropertiesFile(HTMLLogInfoPropertiesFileNameAndPath, "HTMLReportExecutionMethodsInfo", " <Table border=1 width=100% style=font-size:14px;><Tr>");
//Adding Images to HTML Report
        replaceTextInHTML("Images_Base64_Code_For_Current_Execution", "");
//Scripts_Execution_Logs
        replaceTextInHTML("Scripts_Execution_Logs", "");
    }


    public static BrowserType getBrowser(Playwright playwright, String browserName) {
        BrowserType browserType;
        switch (browserName.toUpperCase().trim()) {
            case "CHROME":
            case "EDGE":
            case "CHROME_HEADLESS":
            case "EDGE_HEADLESS":
            default:
                browserType = playwright.chromium();
                break;
            case "FIREFOX":
            case "FIREFOX_HEADLESS":
                browserType = playwright.firefox();
                break;
            case "SAFARI":
                browserType = playwright.webkit();
                break;
        }
        return browserType;
    }

    public static void BrowserMaximized() throws AWTException, InterruptedException {
        Robot robot = new Robot();
// Press Alt
        robot.keyPress(KeyEvent.VK_ALT);
// Press Space
        robot.keyPress(KeyEvent.VK_SPACE);
        Thread.sleep(500);
// Release Space
        robot.keyPress(KeyEvent.VK_X);
// Press X
        robot.keyRelease(KeyEvent.VK_X);
// Release X
        robot.keyRelease(KeyEvent.VK_SPACE);
// Release Alt
        robot.keyRelease(KeyEvent.VK_ALT);
        Thread.sleep(1000);
    }


    public static String ExecutionReportFolderDateAndTime() throws IOException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InvocationTargetException, InstantiationException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss_sss");
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String[] readAllExecutionMethodsFromExecutionExcelFile(String filePath) throws IOException {
        FileInputStream file = new FileInputStream(filePath);
        XSSFWorkbook xwb = new XSSFWorkbook(file);
//Need to change below sheetName to executedScriptName
        String[] arr_ExecutionMethods = new String[0];
        try {
            XSSFSheet xs = xwb.getSheet("Executions");
            arr_ExecutionMethods = new String[xs.getLastRowNum() + 1];
            for (int i = 0; i <= xs.getLastRowNum(); i++) {
                Row row = xs.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    arr_ExecutionMethods[i] = String.valueOf(row.getCell(j));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
//writeLog("Error", "Execution sheet not found in Execution Data File");
            System.out.println("Execution sheet not found in Execution Data File");
//writeLog("Error", String.valueOf(e));
            System.out.println(String.valueOf(e));
            arr_ExecutionMethods = null;
        }
        return arr_ExecutionMethods;
    }


    public static String evaluateFormulaCell(Cell cell) {
        Workbook workbook = cell.getSheet().getWorkbook();
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        CellValue cellValue = evaluator.evaluate(cell);
        switch (cellValue.getCellType()) {
            case STRING:
                return cellValue.getStringValue();
            case NUMERIC:
                return String.valueOf(cellValue.getNumberValue());
            case BOOLEAN:
                return String.valueOf(cellValue.getBooleanValue());
            default:
                return ""; // or handle as needed
        }
    }


    public static void clearCookiesForChrome() {
// Determine the OS
        String os = System.getProperty("os.name").toLowerCase();
// Set the cookies file path based on OS
        String chromeUserDataDir = "";
        if (os.contains("win")) {
            chromeUserDataDir = System.getenv("LOCALAPPDATA") + "\\Google\\Chrome\\User Data\\";
            System.out.println(chromeUserDataDir);
        } else if (os.contains("mac")) {
            chromeUserDataDir = System.getProperty("user.home") + "/Library/Application Support/Google/Chrome/Default/Cookies";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            chromeUserDataDir = System.getProperty("user.home") + "/.config/google-chrome/Default/Cookies";
        } else {
            System.out.println("Unsupported operating system: " + os);
            return;
        }

// Create a Path object for cookies file
        Path chromeUserDataPath = Paths.get(chromeUserDataDir);

        try {
// Check if the Chrome user data directory exists
            if (Files.exists(chromeUserDataPath)) {
// Delete the Chrome user data directory recursively
                deleteDirectory(chromeUserDataPath);
                System.out.println("Chrome user data including cookies have been successfully deleted.");
            } else {
                System.out.println("Chrome user data directory not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to recursively delete a directory and its contents
    private static void deleteDirectory(Path directory) throws IOException {
        Files.walk(directory)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static void clearCookiesForEdge() {
// Determine the OS
        String os = System.getProperty("os.name").toLowerCase();
// Set the cookies file path based on OS
        String chromeUserDataDir = "";
        if (os.contains("win")) {
            chromeUserDataDir = System.getenv("LOCALAPPDATA") + "\\Microsoft\\Edge\\User Data\\";
            System.out.println(chromeUserDataDir);
        } else if (os.contains("mac")) {
            chromeUserDataDir = System.getProperty("user.home") + "/Library/Application Support/Google/Chrome/Default/Cookies";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            chromeUserDataDir = System.getProperty("user.home") + "/.config/google-chrome/Default/Cookies";
        } else {
            System.out.println("Unsupported operating system: " + os);
            return;
        }

// Create a Path object for cookies file
        Path chromeUserDataPath = Paths.get(chromeUserDataDir);

        try {
// Check if the Chrome user data directory exists
            if (Files.exists(chromeUserDataPath)) {
// Delete the Chrome user data directory recursively
                deleteDirectory(chromeUserDataPath);
                System.out.println("Edge user data including cookies have been successfully deleted.");
            } else {
                System.out.println("Edge user data directory not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/* // Method to recursively delete a directory and its contents
private static void deleteDirectory(Path directory) throws IOException {
Files.walk(directory)
.sorted(Comparator.reverseOrder())
.map(Path::toFile)
.forEach(File::delete);
}*/


    public static void vbScriptRunner(String vbScriptFileName) {
        try {
            // Build the command to execute VBScript using cscript (Windows)
            ProcessBuilder pb = new ProcessBuilder("cscript", "//Nologo", vbScriptFileName);
            // Start the process
            Process process = pb.start();
            // Read output (if needed)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // Wait for the script to finish and get the exit code
            int exitCode = process.waitFor();
            //System.out.println("Script executed, exit code: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Helper method to check if the list contains sortable dates
    static boolean isSortableDates(List<String> items) {
        for (String item : items) {
            if (!isDate(item)) {
                return false;
            }
        }
        return true;
    }

    // Helper method to check if a string can be parsed as a date
    static boolean isDate(String str) {
        try {
            LocalDate.parse(str); // Adjust this based on your date format
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Comparator for sorting dates
    static Comparator<String> getComparatorForDates(boolean descending) {
        Comparator<LocalDate> comparator = null;
        if (descending) {
            comparator = comparator.reversed();
        } else {
            comparator = Comparator.naturalOrder();
        }

        Comparator<LocalDate> finalComparator = comparator;
        return (dateString1, dateString2) -> {
            LocalDate date1 = LocalDate.parse(dateString1); // Adjust this based on your date format
            LocalDate date2 = LocalDate.parse(dateString2); // Adjust this based on your date format
            return finalComparator.compare(date1, date2);
        };
    }

    // Comparator for sorting numbers
    static Comparator<String> getComparatorForNumbers(boolean descending) {
        Comparator<String> comparator = Comparator.comparingDouble(Double::parseDouble);
        if (descending) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    // Comparator for sorting strings
    static Comparator<String> getComparatorForStrings(boolean descending) {
        Comparator<String> comparator = Comparator.naturalOrder();
        if (descending) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    static String detectDateFormat(String dateString) {
        String[] formatsToTry = {
                "dd/MM/yyyy",
                "MM/dd/yyyy"

                // Example format, add more formats as needed
                // Add more date formats to cover your application's variations
        };
        for (String format : formatsToTry) {
            try {
                LocalDate.parse(dateString, DateTimeFormatter.ofPattern(format));
                return format; // Return the format that successfully parses the date
            } catch (DateTimeParseException e) {
                // Try the next format
            }
        }
        return null; // Return null if no format matches
    }

    public static boolean validateSortOrder(List<ElementHandle> items, String expectedSortOrder) throws InterruptedException {
        List<Object> contentList = new ArrayList<>();
        for (ElementHandle element : items) {
            String textContent = element.innerText().trim();
            // Detect type and convert accordingly
            Object content = parseContent(textContent);
            contentList.add(content);
        }

        boolean isValidSortOrder = true;
        for (int i = 1; i < contentList.size(); i++) {
            Object current = contentList.get(i);
            Object previous = contentList.get(i - 1);

            // Compare based on type and expected sort order
            int comparison;
            if (current instanceof String && previous instanceof String) {
                comparison = ((String) previous).compareTo((String) current);
            } else if (current instanceof Integer && previous instanceof Integer) {
                comparison = ((Integer) previous).compareTo((Integer) current);
            } else if (current instanceof LocalDate && previous instanceof LocalDate) {
                comparison = ((LocalDate) previous).compareTo((LocalDate) current);
            } else {
                isValidSortOrder = false; // Types are not compatible
                break;
            }

            // Adjust comparison based on expected sort order
            if (expectedSortOrder.equals("ASCENDING")) {
                isValidSortOrder = comparison <= 0;
            } else if (expectedSortOrder.equals("DESCENDING") || expectedSortOrder.equals("DECENDING")) {
                isValidSortOrder = comparison >= 0;
            } else {
                isValidSortOrder = false; // Invalid expected sort order
                break;
            }

            if (!isValidSortOrder) {
                break;
            }
        }

        return isValidSortOrder;
    }

    static Object parseContent(String textContent) {
        // Attempt to parse as Integer
        try {
            return (Object) Integer.parseInt(textContent);
        } catch (NumberFormatException e) {
            // Not an Integer, try parsing as date
            LocalDate date = null;
            String dateFormat = detectDateFormat(textContent);
            if (dateFormat != null) {
                try {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
                    date = LocalDate.parse(textContent, dateFormatter);
                } catch (DateTimeParseException ex) {
                    // Handle parse exception
                }
            }

            if (date != null) {
                return date;
            } else {
                return textContent; // Return textContent as String
            }
        }
    }


}