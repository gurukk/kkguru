package Framework_Methods;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import static Framework_Methods.Generic_Methods.writeLogsInfoIntoExcel;
import static Framework_Methods.Web_Control_Common_Methods.*;
import static Framework_Methods.Web_Control_Common_Methods.writeTCsInfoIntoExcelWithElementMarking;

public class TooltipOrHint {
    private static final Logger logger = LogManager.getLogger(TooltipOrHint.class);

    //Verify_Tooltip_Or_Hint_Equals
    public static boolean Verify_Tooltip_Or_Hint_Equals(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            // Locate and hover over the element that triggers the tooltip
            Locator triggerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            // Retrieve the text content from the tooltip element
            String tooltipText = triggerLocator.textContent();
            String expectedTooltipText = map.get("CONTROL_VALUE").toString();
            if(tooltipText.equals(expectedTooltipText)) {
                methodStatus = true;
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcelWithElementMarking(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

    //Verify_Tooltip_Or_Hint_Equals_Ignore_Case
    public static boolean Verify_Tooltip_Or_Hint_Equals_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            // Locate and hover over the element that triggers the tooltip
            Locator triggerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            // Retrieve the text content from the tooltip element
            String tooltipText = triggerLocator.textContent();
            String expectedTooltipText = map.get("CONTROL_VALUE").toString();
            if(tooltipText.equalsIgnoreCase(expectedTooltipText)) {
                methodStatus = true;
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcelWithElementMarking(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

    //Verify_Tooltip_Or_Hint_Contains
    public static boolean Verify_Tooltip_Or_Hint_Contains(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            // Locate and hover over the element that triggers the tooltip
            Locator triggerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            // Retrieve the text content from the tooltip element
            String tooltipText = triggerLocator.textContent();
            String expectedTooltipText = map.get("CONTROL_VALUE").toString();
            if(tooltipText.contains(expectedTooltipText)) {
                methodStatus = true;
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcelWithElementMarking(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

    //Verify_Tooltip_Or_Hint_Contains_Ignore_Case
    public static boolean Verify_Tooltip_Or_Hint_Contains_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            // Locate and hover over the element that triggers the tooltip
            Locator triggerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            // Retrieve the text content from the tooltip element
            String tooltipText = triggerLocator.textContent().toUpperCase();
            String expectedTooltipText = map.get("CONTROL_VALUE").toString().toUpperCase();
            if(tooltipText.contains(expectedTooltipText)) {
                methodStatus = true;
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcelWithElementMarking(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

    //Verify_Validation_Message_Text_Equals
    public static boolean Verify_Validation_Message_Text_Equals(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            Page.WaitForSelectorOptions optionsForElementLoad = new Page.WaitForSelectorOptions().setTimeout(60000);
            ElementHandle elementTextBox = waitForAnElement(page, map.get("ATTRIBUTE_VALUE").toString(), 30000);
            Keyboard keyboard = page.keyboard();
            String strGetValueFromTextBox = null;

            String validationMessage = (String) elementTextBox.evaluate("element => element.validationMessage");
            System.out.println("INOVAR==========================> "+validationMessage);
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcelWithElementMarking(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }
}
