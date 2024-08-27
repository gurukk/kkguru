package Framework_Methods;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import static Framework_Methods.Generic_Methods.writeLogsInfoIntoExcel;
import static Framework_Methods.Web_Control_Common_Methods.*;

public class Header {
    private static final Logger logger = LogManager.getLogger(Header.class);
    //Verify_Header_Text_Equals
    public static boolean Verify_Header_Text_Equals(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String headerName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator headerLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                headerLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                headerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, headerLocator);
            if (methodStatus) {
                if (headerLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is Visible");
                    if (headerLocator.isEnabled()) {
                        headerName = headerLocator.innerText();
                        //System.out.println("Inovar =======>"+headerName);
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is Enabled");
                        if (headerName.equals(expectedHeaderName)) {
                            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected Names are matched");
                        }
                        else {
                            methodStatus = false;
                            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected Names are not matched");
                        }
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is not enabled");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert headerLocator != null;
            int elementCount = headerLocator.count();
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

    //Verify_Header_Text_Equals_Ignore_Case
    public static boolean Verify_Header_Text_Equals_Ignore_Case(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String headerName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator headerLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                headerLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                headerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, headerLocator);
            if (methodStatus) {
                if (headerLocator.isVisible()) {
                    //headerName = headerLocator.textContent();
                    headerName = headerLocator.innerText();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is Visible");
                    if (headerName.equalsIgnoreCase(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected Names are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected Names are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert headerLocator != null;
            int elementCount = headerLocator.count();
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

    //Verify_Header_Text_Contains
    public static boolean Verify_Header_Text_Contains(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String headerName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator headerLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                headerLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                headerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, headerLocator);
            if (methodStatus) {
                if (headerLocator.isVisible()) {
                    headerName = headerLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is Visible");
                    if (headerName.contains(expectedHeaderName)) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Actual and Expected Names are matched");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Actual and Expected Names are not matched");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert headerLocator != null;
            int elementCount = headerLocator.count();
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

    //Header_Click
    public static boolean Header_Click(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String headerName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator headerLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                headerLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                headerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, headerLocator);
            if (methodStatus) {
                if (headerLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is Visible");
                    if (headerLocator.isEnabled()) {
                        //headerName = headerLocator.innerText();
                        //System.out.println("Inovar =======>"+headerName);
                        headerLocator.click();
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is not enabled");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert headerLocator != null;
            int elementCount = headerLocator.count();
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

    //Header_Double_Click
    public static boolean Header_Double_Click(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String headerName = "";
        String expectedHeaderName = map.get("CONTROL_VALUE").toString();
        Locator headerLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                headerLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                headerLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, headerLocator);
            if (methodStatus) {
                if (headerLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is Visible");
                    if (headerLocator.isEnabled()) {
                        //headerName = headerLocator.innerText();
                        //System.out.println("Inovar =======>"+headerName);
                        headerLocator.dblclick();
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "is not enabled");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", headerName + " " + "name verification is successful");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", headerName + " " + "name verification was unsuccessful");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert headerLocator != null;
            int elementCount = headerLocator.count();
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
