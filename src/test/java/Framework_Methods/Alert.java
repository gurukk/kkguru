package Framework_Methods;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static Framework_Methods.Generic_Methods.writeLogsInfoIntoExcel;
import static Framework_Methods.Web_Control_Common_Methods.*;

public class Alert {
    private static final Logger logger = LogManager.getLogger(Alert.class);
    //Verify_Alert_Text_Equals
    public static boolean Verify_Alert_Text_Equals(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String alertName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator alertLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                alertLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                alertLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            alertLocator.getByRole(AriaRole.valueOf("alert")).wait();
            alertLocator.getByRole(AriaRole.valueOf("alert")).focus();
            methodStatus = waitForPageLocator(page, alertLocator);
            if (methodStatus) {
                if (alertLocator.isVisible()) {
                    alertName = alertLocator.innerText();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "alert is Visible");
                    if (alertName.equals(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected alert texts are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected alert texts are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "alert is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "Alert verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "Alert verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert alertLocator != null;
            int elementCount = alertLocator.count();
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

    //Verify_Alert_Text_Equals_Ignore_Case
    public static boolean Verify_Alert_Text_Equals_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String alertName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator alertLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                alertLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                alertLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            alertLocator.getByRole(AriaRole.valueOf("alert")).wait();
            alertLocator.getByRole(AriaRole.valueOf("alert")).focus();
            methodStatus = waitForPageLocator(page, alertLocator);
            if (methodStatus) {
                if (alertLocator.isVisible()) {
                    alertName = alertLocator.innerText();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "alert is Visible");
                    if (alertName.equalsIgnoreCase(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected alert texts are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected alert texts are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "alert is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "Alert verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "Alert verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert alertLocator != null;
            int elementCount = alertLocator.count();
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

    //Verify_Alert_Text_Contains
    public static boolean Verify_Alert_Text_Contains(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String alertName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator alertLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                alertLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                alertLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, alertLocator);
            if (methodStatus) {
                if (alertLocator.isVisible()) {
                    alertName = alertLocator.innerText();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "alert is Visible");
                    if (alertName.contains(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected alert texts are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected alert texts are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "alert is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "Alert verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "Alert verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert alertLocator != null;
            int elementCount = alertLocator.count();
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

    //Verify_Label_Text_Contains_Ignore_Case
    public static boolean Verify_Alert_Text_Contains_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String alertName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator alertLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                alertLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                alertLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, alertLocator);
            if (methodStatus) {
                if (alertLocator.isVisible()) {
                    alertName = alertLocator.innerText();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "alert is Visible");
                    if (alertName.toUpperCase().contains(expectedHeaderName.toUpperCase())) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected alert texts are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected alert texts are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "alert is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "Alert verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "Alert verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert alertLocator != null;
            int elementCount = alertLocator.count();
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

    //Verify_Is_Alert_Visible
    public static boolean Verify_Is_Alert_Visible(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String alertName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator alertLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                alertLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                alertLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, alertLocator);
            if (methodStatus) {
                if (alertLocator.isVisible()) {
                    alertName = alertLocator.innerText();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "alert is Visible");
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "alert is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "Alert verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "Alert verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert alertLocator != null;
            int elementCount = alertLocator.count();
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

    //Verify_Is_Alert_Enabled
    public static boolean Verify_Is_Alert_Enabled(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String alertName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator alertLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                alertLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                alertLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, alertLocator);
            if (methodStatus) {
                if (alertLocator.isEnabled()) {
                    alertName = alertLocator.innerText();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "alert is enabled");
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "alert is not enabled");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", alertName + " " + "Alert verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", alertName + " " + "Alert verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert alertLocator != null;
            int elementCount = alertLocator.count();
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
