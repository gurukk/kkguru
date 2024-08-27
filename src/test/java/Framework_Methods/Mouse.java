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

public class Mouse {
    private static final Logger logger = LogManager.getLogger(Mouse.class);
    //Mouse_Hover
    public static boolean Mouse_Hover(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        String MouseHoverName = "";
        Locator MouseHoverLocator = null;
        try {
            waitForPageLoad(page);
            if ((map.get("ATTRIBUTE_VALUE").toString()).contains("%s")) {
                MouseHoverLocator = page.locator(String.format(map.get("ATTRIBUTE_VALUE").toString(), map.get("CONTROL_VALUE").toString()));
            } else {
                MouseHoverLocator = page.locator(map.get("ATTRIBUTE_VALUE").toString());
            }
            methodStatus = waitForPageLocator(page, MouseHoverLocator);
            if (methodStatus) {
                if (MouseHoverLocator.isVisible()) {
                    MouseHoverName = MouseHoverLocator.textContent();
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", MouseHoverName + " " + "Button is Visible");
                    if (MouseHoverLocator.isEnabled()) {
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", MouseHoverName + " " + "Button is Enabled");
                        MouseHoverLocator.hover();
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Info", MouseHoverName + " " + "Button has been clicked successfully");
                    } else {
                        methodStatus = false;
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", MouseHoverName + " " + "Button is not Enabled");
                        writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", MouseHoverName + " " + "Button click was unsuccessful");
                    }
                } else {
                    methodStatus = false;
                    writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", MouseHoverName + " " + "Button is not Visible");
                }

            } else {

            }
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            assert MouseHoverLocator != null;
            int elementCount = MouseHoverLocator.count();
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


    //Mouse_Move
    public static boolean Mouse_Move_And_Click(Page page, HashMap map, String reportLogFileName) throws IOException, InterruptedException {
        boolean methodStatus = false;

        Thread.sleep(1000);

        //page.click(".empty-area");


        // Click outside the dialog to dismiss it
        page.mouse().click(300, 300); // Adjust coordinates as needed
        methodStatus=true;
        return methodStatus;
    }


}
