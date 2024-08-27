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

public class Wait {
    private static final Logger logger = LogManager.getLogger(Wait.class);
    //Wait_For_Short_Time
    public static boolean Wait_For_Short_Time(Page page, HashMap map, String reportLogFileName) {
        boolean methodStatus = false;
        try {
            Thread.sleep(5000);
            methodStatus = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return methodStatus;
    }

    //Wait_For_Short_Time
    public static boolean Wait_For_Short_Time() {
        boolean methodStatus = false;
        try {
            Thread.sleep(5000);
            methodStatus = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return methodStatus;
    }

    //Wait_For_Medium_Time
    public static boolean Wait_For_Medium_Time(Page page, HashMap map, String reportLogFileName) {
        boolean methodStatus = false;
        try {
            Thread.sleep(7500);
            methodStatus = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return methodStatus;
    }

    //Wait_For_Long_Time
    public static boolean Wait_For_Long_Time(Page page, HashMap map, String reportLogFileName) {
        boolean methodStatus = false;
        try {
            Thread.sleep(10000);
            methodStatus = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return methodStatus;
    }


}
