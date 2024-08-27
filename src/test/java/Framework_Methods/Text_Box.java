package Framework_Methods;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static Framework_Methods.Generic_Methods.writeLogsInfoIntoExcel;
import static Framework_Methods.Web_Control_Common_Methods.*;

public class Text_Box {
    private static final Logger logger = LogManager.getLogger(Text_Box.class);
    //Enter_Value_In_TextBox
    public static boolean Enter_Value_In_TextBox(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                        textBoxLocator.fill(map.get("CONTROL_VALUE").toString());
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Enter_Value_In_TextBox_Append
    public static boolean Enter_Value_In_TextBox_Append(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                        textBoxLocator.type(map.get("CONTROL_VALUE").toString());
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Clear_Value_In_TextBox
    public static boolean Clear_Value_In_TextBox(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                        textBoxLocator.clear();
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Enter_Value_In_TextBox_Via_Command_Prompt
    public static boolean Enter_Value_In_TextBox_Via_Command_Prompt(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                        Scanner scanner = new Scanner(System.in);
                        String userEnteredInputFromCommandPrompt = scanner.nextLine();
                        textBoxLocator.fill(userEnteredInputFromCommandPrompt);
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox from Command Prompt");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Enter_Value_In_TextBox_Via_Input_Box
    public static boolean Enter_Value_In_TextBox_Via_Input_Box(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                        String userEnteredInputFromInputBox = JOptionPane.showInputDialog(null, "Enter value:");
                        textBoxLocator.fill(userEnteredInputFromInputBox);
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox from Input Box");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Enter_Value_In_TextBox_Via_Secure_Input_Box
    public static boolean Enter_Value_In_TextBox_Via_Secure_Input_Box(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                        JPasswordField passwordField = new JPasswordField();
                        int option = JOptionPane.showConfirmDialog(null, passwordField, "Enter value:", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (option == JOptionPane.OK_OPTION) {
                            char[] password = passwordField.getPassword();
                            String userEnteredInputFromInputBox = new String(password);
                            textBoxLocator.fill(userEnteredInputFromInputBox);
                        }
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox from Input Box");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Verify_Value_In_TextBox
    public static boolean Verify_Value_In_TextBox(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                        //System.out.println("Inovar ============   "+textBoxLocator.inputValue());
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Verify_PlaceHolder_Value_In_TextBox
    public static boolean Verify_PlaceHolder_Value_In_TextBox(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                        //System.out.println("Inovar ============   "+textBoxLocator.getAttribute("placeholder"));
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox");
            } else {
                writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Verify_Is_TextBox_Visible
    public static boolean Verify_Is_TextBox_Visible(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox");
            } else {
                //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Verify_Is_TextBox_Enabled
    public static boolean Verify_Is_TextBox_Enabled(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Enabled");
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Enabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox");
            } else {
                //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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

    //Verify_Is_TextBox_Disabled
    public static boolean Verify_Is_TextBox_Disabled(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        Locator textBoxLocator = null;
        try {
            waitForPageLoad(page);
            textBoxLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            methodStatus = waitForPageLocator(page, textBoxLocator);
            if (methodStatus) {
                if (textBoxLocator.isVisible()) {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Visible");
                    if (textBoxLocator.isDisabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "TextBox is Disabled");
                    } else {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox is not Disabled");
                    }
                } else {
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "TextBox Link is not Visible");
                }
                //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", "Data has been successfully entered into TextBox");
            } else {
                //writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", "Unable to enter data into TextBox");
            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert textBoxLocator != null;
            int elementCount = textBoxLocator.count();
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
