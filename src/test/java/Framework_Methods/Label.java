package Framework_Methods;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static Framework_Methods.Generic_Methods.writeLogsInfoIntoExcel;
import static Framework_Methods.Web_Control_Common_Methods.*;


public class Label {
    private static final Logger logger = LogManager.getLogger(Label.class);
    //Label_Click
    public static boolean Label_Click(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String labelName = "";
        Locator labelLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                labelLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                labelLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, labelLocator);
            if (methodStatus) {
                if (labelLocator.isVisible()) {
                    labelLocator.click();
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Label is not Visible");
                }
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Label is not Visible");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert labelLocator != null;
            int elementCount = labelLocator.count();
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


    //Verify_Label_Text_Equals
    public static boolean Verify_Label_Text_Equals(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String labelName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator labelLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                labelLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                labelLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, labelLocator);
            if (methodStatus) {
                if (labelLocator.isVisible()) {
                    labelName = labelLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Button is Visible");
                    if (labelName.equals(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected Button Names are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected Button Names are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Button is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Button name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Button name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert labelLocator != null;
            int elementCount = labelLocator.count();
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

    //Verify_Label_Text_Equals_Ignore_Case
    public static boolean Verify_Label_Text_Equals_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String labelName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator labelLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                labelLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                labelLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, labelLocator);
            if (methodStatus) {
                if (labelLocator.isVisible()) {
                    labelName = labelLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Button is Visible");
                    if (labelName.equalsIgnoreCase(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected Button Names are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected Button Names are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Button is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Button name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Button name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert labelLocator != null;
            int elementCount = labelLocator.count();
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

    //Verify_Label_Text_Contains
    public static boolean Verify_Label_Text_Contains(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String labelName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator labelLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                labelLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                labelLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, labelLocator);
            if (methodStatus) {
                if (labelLocator.isVisible()) {
                    labelName = labelLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Button is Visible");
                    if (labelName.contains(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected Button Names are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected Button Names are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Button is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Button name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Button name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert labelLocator != null;
            int elementCount = labelLocator.count();
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
    public static boolean Verify_Label_Text_Contains_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String labelName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString().toUpperCase();
        Locator labelLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                labelLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                labelLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, labelLocator);
            if (methodStatus) {
                if (labelLocator.isVisible()) {
                    labelName = labelLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Button is Visible");
                    if (labelName.toUpperCase().contains(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected Button Names are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected Button Names are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Button is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Button name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Button name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert labelLocator != null;
            int elementCount = labelLocator.count();
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

    //Verify_Is_Label_Visible
    public static boolean Verify_Is_Label_Visible(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String labelName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator labelLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                labelLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                labelLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, labelLocator);
            if (methodStatus) {
                if (labelLocator.isVisible()) {
                    labelName = labelLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Label is Visible");
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Label is not Visible");
                }
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Label is not Visible");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert labelLocator != null;
            int elementCount = labelLocator.count();
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

    //Verify_Is_Label_Enabled
    public static boolean Verify_Is_Label_Enabled(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String labelName = "";
        Locator labelLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                labelLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                labelLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, labelLocator);
            if (methodStatus) {
                if (labelLocator.isEnabled()) {
                    labelName = labelLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Label is Enabled");
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Label is not Enabled");
                }
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Label is not Enabled");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert labelLocator != null;
            int elementCount = labelLocator.count();
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


    //Verify_Is_Label_Disabled
    public static boolean Verify_Is_Label_Disabled(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String labelName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator labelLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                labelLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                labelLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, labelLocator);
            if (methodStatus) {
                if (labelLocator.isDisabled()) {
                    labelName = labelLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", labelName + " " + "Label is Disabled");
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Label is not Disabled");
                }
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", labelName + " " + "Label is not Disabled");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert labelLocator != null;
            int elementCount = labelLocator.count();
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
