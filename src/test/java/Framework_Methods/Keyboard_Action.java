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

public class Keyboard_Action {
    private static final Logger logger = LogManager.getLogger(Keyboard_Action.class);
    public boolean Press_Keyboard_Key(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            //page.keyboard().press(map.get("CONTROL_VALUE").toString().trim());
            page.keyboard().press("Escape");
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

    public boolean Type_Keyboard_Keys(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);
            page.keyboard().type(map.get("CONTROL_VALUE").toString().trim());
            //page.keyboard().press("Escape");
        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }

}
