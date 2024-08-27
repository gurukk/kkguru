package Framework_Methods;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Scanner;

import static Framework_Methods.Generic_Methods.writeLogsInfoIntoExcel;
import static Framework_Methods.Generic_Methods.writeTestCasesInfoIntoExcel;


public class Web_Control_Common_Methods {
    private static final Logger logger = LogManager.getLogger(Web_Control_Common_Methods.class);

    public static String imagesFolderPath = "";

    public Web_Control_Common_Methods(String imagesFolderPath) {
        Web_Control_Common_Methods.imagesFolderPath = imagesFolderPath;
        // Check if the folder exists
        File folder = new File(imagesFolderPath);
        if (!folder.exists()) {
            // If the folder does not exist, create it
            if (folder.mkdirs()) {
                //System.out.println("Folder created: " + imagesFolderPath);
            } else {
                //System.out.println("Failed to create folder: " + imagesFolderPath);
            }
        } else {
            //System.out.println("Folder already exists: " + imagesFolderPath);
        }
    }



    public static void updateExcel(String filePath, String sheetName, String threadId) {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet with name " + sheetName + " does not exist.");
                return;
            }

            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (cell.getCellType() == CellType.STRING && threadId.equals(cell.getStringCellValue())) {
                        int lastColumn = row.getLastCellNum();
                        Cell newCell = row.createCell(lastColumn);
                        newCell.setCellValue("Done");
                        break;
                    }
                }
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
                System.out.println("Excel file updated successfully.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void waitForElementsEnabled(Page page) {
        String script = "(() => { " +
                "const allElements = document.querySelectorAll('*');" +
                "for (let element of allElements) {" +
                "if (!element.disabled && element.offsetWidth > 0 && element.offsetHeight > 0) {" +
                "return true;" +
                "}" +
                "}" +
                "return false;" +
                "})()";
        try {
            page.waitForFunction(script, new Page.WaitForFunctionOptions().setTimeout(60000));
        } catch (PlaywrightException e) {

        }
    }


    public static boolean waitForPageContentEnabled(Page page) {
        String script = "document.readyState === 'complete' && !document.querySelector('body').hasAttribute('disabled');";
        try {
            page.waitForFunction(script, new Page.WaitForFunctionOptions().setTimeout(12000));
            return true;
        } catch (PlaywrightException e) {
            System.out.println("Error waiting for page content to be enabled: " + e.getMessage());
            return false;
        }
    }

    private static boolean waitForLoadStateWithRetry(Page page) {
        int retryCount = 3;
        int currentAttempt = 0;
        while (currentAttempt < retryCount) {
            try {
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                page.waitForLoadState(LoadState.NETWORKIDLE);
                return true;
            } catch (Exception e) {
                System.err.println("Attempt " + (currentAttempt + 1) + " failed: " + e.getMessage());
            }
            currentAttempt++;
        }
        return false;
    }

    public static void waitForPageLoad(Page page) {
        try {
            page.setDefaultTimeout(60000);
            Page.WaitForLoadStateOptions options = new Page.WaitForLoadStateOptions().setTimeout(60000);
            page.waitForLoadState(LoadState.DOMCONTENTLOADED, options);
            page.waitForLoadState(LoadState.LOAD, options);
            page.waitForLoadState(LoadState.NETWORKIDLE, options);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*public static void waitForPageLoad(Page page) {
        try {
            // Set a total timeout of 60 seconds for the entire operation
            long totalTimeout = 60000;
            long startTime = System.currentTimeMillis();

            // Wait for the DOM content to be loaded
            long remainingTime = totalTimeout - (System.currentTimeMillis() - startTime);
            if (remainingTime > 0) {
                Page.WaitForLoadStateOptions domContentLoadedOptions = new Page.WaitForLoadStateOptions().setTimeout(remainingTime);
                page.waitForLoadState(LoadState.DOMCONTENTLOADED, domContentLoadedOptions);
            }

            // Wait for the full page load (i.e., all resources have been loaded)
            remainingTime = totalTimeout - (System.currentTimeMillis() - startTime);
            if (remainingTime > 0) {
                Page.WaitForLoadStateOptions loadOptions = new Page.WaitForLoadStateOptions().setTimeout(remainingTime);
                page.waitForLoadState(LoadState.LOAD, loadOptions);
            }

            // Wait for the network to be idle (i.e., no ongoing network requests)
            remainingTime = totalTimeout - (System.currentTimeMillis() - startTime);
            if (remainingTime > 0) {
                Page.WaitForLoadStateOptions networkIdleOptions = new Page.WaitForLoadStateOptions().setTimeout(remainingTime);
                page.waitForLoadState(LoadState.NETWORKIDLE, networkIdleOptions);
            }

        } catch (Exception e) {
            System.out.println("Error during page load wait: " + e.getMessage());
        }
    }*/


    public static ElementHandle waitForAnElement(Page page, String elementString, int timeoutMillis) {
        long startTime = System.currentTimeMillis();
        ElementHandle element = null;
        while (System.currentTimeMillis() - startTime < timeoutMillis) {
            try {
                element = page.querySelector(elementString);
                if (element != null && element.isVisible()) {
                    return element;
                }
            } catch (PlaywrightException e) {
            }
        }
        assert element != null;
        element.focus();
        return null;
    }

    public static boolean waitForPageLocator(Page page, Locator locator) throws InterruptedException {

        boolean locatorInReadyState = false;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 60000) {
            int elementCount = locator.count();
            if (elementCount > 0 && locator.isVisible()) {
                try {
                        locatorInReadyState = true;
                        break;
                } catch (PlaywrightException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        if (locatorInReadyState) {
            locator.scrollIntoViewIfNeeded();
            locator.hover();
            locator.focus();
            //locator.evaluateHandle("(element) => { element.scrollIntoView({ behavior: 'smooth', block: 'center' }); }");
            locator.evaluateHandle("(element) => { " +
                    "element.scrollIntoView({ behavior: 'smooth', block: 'center' });" +
                    "element.style.border = '2px solid blue';" +  // Add a blue border to the element
                   /* "element.style.backgroundColor = 'lightblue';" + */ // Optionally add a light blue background
                    "}");

        } else {
            System.out.println("Element " + locator + " is not available.");
        }
        return locatorInReadyState;
    }


    public static String ScreenshotToBase64(Page page, String selector, String color) throws IOException {
        SimpleDateFormat uniqueImageName = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss_sss");
        String imageFilePathAndName = imagesFolderPath+"\\Image_" + uniqueImageName.format(Calendar.getInstance().getTime()) + ".PNG";

        waitForPageLoad(page);
        ElementHandle element = page.waitForSelector(selector);
        page.evaluate("element => { element.style.outline = '4px solid " + color + "'; }", element);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Path screenshotPath = Paths.get(imageFilePathAndName);
        Files.write(screenshotPath, screenshotBytes);
        page.evaluate("element => { element.style.outline = ''; }", element);
        return imageFilePathAndName;
    }

    public static String ScreenshotToBase64(Page page) throws IOException {
        SimpleDateFormat uniqueImageName = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss_sss");
        String ImageFilePathAndName = imagesFolderPath+"\\Image_" + uniqueImageName.format(Calendar.getInstance().getTime()) + ".PNG";
        waitForPageLoad(page);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions());
        Path screenshotPath = Paths.get(ImageFilePathAndName);
        Files.write(screenshotPath, screenshotBytes);
        return ImageFilePathAndName;
    }

    public static void writeTCsInfoIntoExcel(Page page, String reportLogFileName, HashMap map, boolean methodStatus) throws IOException {
        if (methodStatus) {
            //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Page has been loaded successfully");
            if (((!map.get("TEST_CASES").toString().isEmpty()) && (map.get("VALIDATION_TYPE").toString().contains("Assertion"))) && ((map.get("SHOULD_TAKE_SCREENSHOT").toString().isEmpty() || map.get("SHOULD_TAKE_SCREENSHOT").toString().equalsIgnoreCase("YES")))) {
                writeTestCasesInfoIntoExcel(reportLogFileName, "TestCases_Info", String.valueOf(Thread.currentThread().getId()), map.get("TEST_CASES").toString(), ScreenshotToBase64(page), "Pass");
            } else if (((!map.get("TEST_CASES").toString().isEmpty()) && (map.get("VALIDATION_TYPE").toString().contains("Assertion"))) && map.get("SHOULD_TAKE_SCREENSHOT").toString().equalsIgnoreCase("NO")) {
                writeTestCasesInfoIntoExcel(reportLogFileName, "TestCases_Info", String.valueOf(Thread.currentThread().getId()), map.get("TEST_CASES").toString(), "", "Pass");
            }
        } else {
            //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Page is not loaded");
            if (((!map.get("TEST_CASES").toString().isEmpty()) && (map.get("VALIDATION_TYPE").toString().contains("Assertion"))) && ((map.get("SHOULD_TAKE_SCREENSHOT").toString().isEmpty() || map.get("SHOULD_TAKE_SCREENSHOT").toString().equalsIgnoreCase("YES")))) {
                writeTestCasesInfoIntoExcel(reportLogFileName, "TestCases_Info", String.valueOf(Thread.currentThread().getId()), map.get("TEST_CASES").toString(), ScreenshotToBase64(page), "Fail");
            } else if (((!map.get("TEST_CASES").toString().isEmpty()) && (map.get("VALIDATION_TYPE").toString().contains("Assertion"))) && map.get("SHOULD_TAKE_SCREENSHOT").toString().equalsIgnoreCase("NO")) {
                writeTestCasesInfoIntoExcel(reportLogFileName, "TestCases_Info", String.valueOf(Thread.currentThread().getId()), map.get("TEST_CASES").toString(), "", "Fail");
            }
        }
    }

    public static void writeTCsInfoIntoExcelWithElementMarking(Page page, String reportLogFileName, HashMap map, boolean methodStatus) throws IOException {
        String elementLocator = "";
        if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
            elementLocator = String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString());

        } else {
            elementLocator = map.get("ATTRIBUTE_VALUE").toString();
        }

        if (methodStatus) {
            //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Page has been loaded successfully");
            if (((!map.get("TEST_CASES").toString().isEmpty()) && (map.get("VALIDATION_TYPE").toString().contains("Assertion"))) && ((map.get("SHOULD_TAKE_SCREENSHOT").toString().isEmpty() || map.get("SHOULD_TAKE_SCREENSHOT").toString().equalsIgnoreCase("YES")))) {
                writeTestCasesInfoIntoExcel(reportLogFileName, "TestCases_Info", String.valueOf(Thread.currentThread().getId()), map.get("TEST_CASES").toString(), ScreenshotToBase64(page, elementLocator, "green"), "Pass");
            } else if (((!map.get("TEST_CASES").toString().isEmpty()) && (map.get("VALIDATION_TYPE").toString().contains("Assertion"))) && (map.get("SHOULD_TAKE_SCREENSHOT").toString().equalsIgnoreCase("NO"))) {
                writeTestCasesInfoIntoExcel(reportLogFileName, "TestCases_Info", String.valueOf(Thread.currentThread().getId()), map.get("TEST_CASES").toString(), "", "Pass");
            }
        } else {
            //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Page is not loaded");
            if (((!map.get("TEST_CASES").toString().isEmpty()) && (map.get("VALIDATION_TYPE").toString().contains("Assertion"))) && ((map.get("SHOULD_TAKE_SCREENSHOT").toString().isEmpty() || map.get("SHOULD_TAKE_SCREENSHOT").toString().equalsIgnoreCase("YES")))) {
                writeTestCasesInfoIntoExcel(reportLogFileName, "TestCases_Info", String.valueOf(Thread.currentThread().getId()), map.get("TEST_CASES").toString(), ScreenshotToBase64(page, elementLocator, "red"), "Fail");
            } else if (((!map.get("TEST_CASES").toString().isEmpty()) && (map.get("VALIDATION_TYPE").toString().contains("Assertion"))) && (map.get("SHOULD_TAKE_SCREENSHOT").toString().equalsIgnoreCase("NO"))) {
                writeTestCasesInfoIntoExcel(reportLogFileName, "TestCases_Info", String.valueOf(Thread.currentThread().getId()), map.get("TEST_CASES").toString(), "", "Fail");
            }
        }
    }


    // Function to check if the element is visible
    private static void isElementVisible(Page page, ElementHandle element) {
        JavascriptExecutor js = (JavascriptExecutor) page;
        boolean elementFound = false;
        int scrollStartPosition = 0;
        int scrollEndPosition = 0;
        int maxScroll = 5000;
        while (!elementFound && scrollStartPosition <= maxScroll) {
            js.executeScript("window.scrollTo(scrollStartPosition, " + scrollEndPosition + ")");
            if (element.isVisible()) {
                elementFound = true;
            }
            scrollStartPosition = scrollStartPosition + scrollEndPosition;
            scrollEndPosition += 500;
        }
        if (elementFound) {
            System.out.println("Element found!");
        } else {
            System.out.println("Element not found after scrolling.");
        }
    }

    //Page_Down
    public static boolean Page_Down(int rownumber, Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            Number initialScrollY = (Number) page.evaluate("() => window.scrollY");
            Keyboard keyboard = page.keyboard();
            while (true) {
                keyboard.press("PageDown");
                page.waitForTimeout(1000);
                Number newScrollY = (Number) page.evaluate("() => window.scrollY");
                if (newScrollY.equals(initialScrollY)) {
                    return methodStatus = true;
                }
                initialScrollY = newScrollY;
            }
        } catch (Exception e) {
            System.out.println();
        }
        return methodStatus;
    }

    public static void verifyPageTitle(Page page, String expectedTitle, int timeoutInSeconds) {
        long startTime = System.currentTimeMillis();
        boolean isTitleMatched = false;

        while (!isTitleMatched && (System.currentTimeMillis() - startTime) < timeoutInSeconds * 1000L) {
            String actualTitle = page.title();
            if (actualTitle.contains(expectedTitle)) {
                // System.out.println("Page title matched: " + actualTitle);
                isTitleMatched = true;
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!isTitleMatched) {
            System.out.println("Page title didn't match within the timeout.");
        }
    }


}
