package Framework_Methods;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static Framework_Methods.Generic_Methods.writeLogsInfoIntoExcel;
import static Framework_Methods.Web_Control_Common_Methods.*;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class Web_Page {
    private static final Logger logger = LogManager.getLogger(Web_Page.class);
    //Page_Load
    public static boolean Page_Load(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            page.navigate(map.get("CONTROL_VALUE").toString());
            waitForPageLoad(page);
            //page.waitForLoadState();
            methodStatus = true;
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", page.title()+" has been launched successfully");
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }


    //Verify_Page_Title_Equals
    public static boolean Verify_Page_Title_Equals(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            String expectedTitle = map.get("CONTROL_VALUE").toString().trim();
            verifyPageTitle(page, map.get("CONTROL_VALUE").toString().trim(), 60);
            assertThat(page).hasTitle(map.get("CONTROL_VALUE").toString().trim());
            String actualPageTitle = page.title().trim();
            if (actualPageTitle.equals(expectedTitle)) {
                methodStatus = true;
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

    //Verify_Page_Title_Contains
    public static boolean Verify_Page_Title_Contains(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            //waitForElementsEnabled(page);
            String expectedTitle = map.get("CONTROL_VALUE").toString().trim();
            verifyPageTitle(page, map.get("CONTROL_VALUE").toString().trim(), 60);
            String actualPageTitle = page.title().trim();
            if (actualPageTitle.contains(expectedTitle)) {
                methodStatus = true;
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

    //Verify_Page_Title_Equals_Ignore_Case
    public static boolean Verify_Page_Title_Equals_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            String expectedTitle = map.get("CONTROL_VALUE").toString().trim();
            verifyPageTitle(page, expectedTitle, 60);
            String actualPageTitle = page.title().trim();
            if (actualPageTitle.equalsIgnoreCase(expectedTitle)) {
                methodStatus = true;
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

    //Verify_Page_Title_Contains_Ignore_Case
    public static boolean Verify_Page_Title_Contains_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            //waitForElementsEnabled(page);
            String expectedTitle = map.get("CONTROL_VALUE").toString().toUpperCase().trim();
            verifyPageTitle(page, expectedTitle, 30);
            String actualPageTitle = page.title().trim().toUpperCase();
            if (actualPageTitle.contains(expectedTitle)) {
                methodStatus = true;
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

}
