package Framework_Methods;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static Framework_Methods.Generic_Methods.writeLogsInfoIntoExcel;
import static Framework_Methods.Web_Control_Common_Methods.*;

public class Dropdown {
    private static final Logger logger = LogManager.getLogger(Dropdown.class);
    // Method to verify the count of options in a dropdown
    public static boolean Verify_Count_Of_Options_In_Dropdown(Page page, HashMap<String, String> map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        //Locator dropdownLocator = null;
        int optionsAvailableCountInDropdown = 0;
        int expectedOptionsAvailableCountInDropdown = Integer.parseInt(map.get("CONTROL_VALUE"));
        try {
            // Wait for page load (implement waitForPageLoad according to your needs)
            waitForPageLoad(page);
            optionsAvailableCountInDropdown = page.locator(map.get("ATTRIBUTE_VALUE")).count();
            if (expectedOptionsAvailableCountInDropdown == optionsAvailableCountInDropdown) {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Number of options in the dropdown: " + optionsAvailableCountInDropdown);
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected Number of options available in the dropdown are matched");
                methodStatus = true;
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected Number of options available in the dropdown are not matched");
            }
        } catch (Exception e) {
            // Log any exceptions
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            // Ensure proper logging of test case status
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        // Handle partial assertion case
        if (map.get("VALIDATION_TYPE").equals("Partial_Assertion")) {
            methodStatus = true;
        }
        return methodStatus;
    }

    //Verify_Option_Is_Available_InDropdown
    public static boolean Verify_Option_Is_Available_In_Dropdown(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        //String linkName = null;
        //Locator dropdownLocator = null;
        try {
            waitForPageLoad(page);
            // Define your XPath expression
            String xpathExpression = map.get("ATTRIBUTE_VALUE").toString();
            // Use Playwright's API to query and get text content of elements
            List<ElementHandle> optionElements = page.querySelectorAll(xpathExpression);
            String textContent = null;
            boolean textFound = false;
            for (ElementHandle element : optionElements) {
                textContent = element.innerText();
                if (textContent.equals(map.get("CONTROL_VALUE").toString().trim())) {
                    textFound = true;
                    //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected option in the dropdown is matched");
                    methodStatus = true;
                    break;
                }
            }
            // Validate the sorting order
            if (textFound) {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", map.get("CONTROL_VALUE").toString().trim() + " Option is available in Dropdown");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", map.get("CONTROL_VALUE").toString().trim() + " Option is not available in Dropdown");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        if (map.get("VALIDATION_TYPE").equals("Partial_Assertion")) {
            methodStatus = true;
        }
        return methodStatus;
    }


    //Select_Value_From_Dropdown
    public static boolean Select_Value_From_Dropdown(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        //String dropdownName = "";
        Locator dropdownLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                dropdownLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                dropdownLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, dropdownLocator);
            if (methodStatus) {
                if (dropdownLocator.isVisible()) {
                    //dropdownName = dropdownLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Dropdown is Visible");
                    if (dropdownLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Dropdown is Enabled");
                        dropdownLocator.selectOption(map.get("CONTROL_VALUE").toString());
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Dropdown is not Enabled");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Dropdown is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Value from Dropdown has been selected successfully");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to select the value from dropdown");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert dropdownLocator != null;
            int elementCount = dropdownLocator.count();
            if (elementCount > 0) {
                writeTCsInfoIntoExcelWithElementMarking(page, reportLogFileName, map, methodStatus);
            } else {
                writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
            }
        }
        if (map.get("VALIDATION_TYPE").equals("Partial_Assertion")) {
            methodStatus = true;
        }
        return methodStatus;
    }

    //Select_Value_From_Web_Dropdown
    public static boolean Select_Value_From_Web_Dropdown(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            String[] dropdownLocators = map.get("ATTRIBUTE_VALUE").toString().split("<<=>>");
            String dropdownLocator = dropdownLocators[0].trim();
            Page.WaitForSelectorOptions optionsForElementLoad = new Page.WaitForSelectorOptions().setTimeout(60000);
            ElementHandle elementDropdown = waitForAnElement(page, dropdownLocator.toString(), 30000);
            String dropdownSelectValue = dropdownLocators[1].trim();
            String elementPath;
            if (dropdownSelectValue.contains("%s")) {
                String dynamicValue = map.get("CONTROL_VALUE").toString();
                elementPath = (dropdownSelectValue).replace("%s", dynamicValue);
            } else {
                elementPath = dropdownSelectValue;
            }
            boolean isDisplayed = elementDropdown.isVisible();
            boolean isEnabled = elementDropdown.isEnabled();
            if (isDisplayed && isEnabled) {
                elementDropdown.click();
                page.waitForTimeout(1000);
                ElementHandle option = page.querySelector(elementPath);
                Thread.sleep(1000);
                option.click();
                methodStatus = true;
            } else {
                System.out.println("Control is not displayed on the page");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }


    //Verify_Is_Dropdown_Visible
    public static boolean Verify_Is_Dropdown_Visible(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        //String dropdownName = "";
        Locator dropdownLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                dropdownLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                dropdownLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, dropdownLocator);
            if (methodStatus) {
                if (dropdownLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Dropdown is Visible");
                    dropdownLocator.focus();//.selectOption(map.get("CONTROL_VALUE").toString());
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Dropdown is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Value from Dropdown has been selected successfully");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to select the value from dropdown");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert dropdownLocator != null;
            int elementCount = dropdownLocator.count();
            if (elementCount > 0) {
                writeTCsInfoIntoExcelWithElementMarking(page, reportLogFileName, map, methodStatus);
            } else {
                writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
            }
        }
        if (map.get("VALIDATION_TYPE").equals("Partial_Assertion")) {
            methodStatus = true;
        }
        return methodStatus;
    }


    //Verify_Is_Dropdown_Enabled
    public static boolean Verify_Is_Dropdown_Enabled(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator dropdownLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                dropdownLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                dropdownLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, dropdownLocator);
            if (methodStatus) {
                if (dropdownLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Dropdown is Visible");
                    if (dropdownLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Dropdown is Enabled");
                        dropdownLocator.focus();//.selectOption(map.get("CONTROL_VALUE").toString());
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Dropdown is not Enabled");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Dropdown is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Value from Dropdown has been selected successfully");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to select the value from dropdown");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert dropdownLocator != null;
            int elementCount = dropdownLocator.count();
            if (elementCount > 0) {
                writeTCsInfoIntoExcelWithElementMarking(page, reportLogFileName, map, methodStatus);
            } else {
                writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
            }
        }
        if (map.get("VALIDATION_TYPE").equals("Partial_Assertion")) {
            methodStatus = true;
        }
        return methodStatus;
    }
}


