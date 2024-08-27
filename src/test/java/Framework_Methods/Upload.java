package Framework_Methods;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

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

public class Upload {

    private static final Logger logger = LogManager.getLogger(Upload.class);

    public static boolean Upload_File(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);

            String[] uploadFoldersAndFiles = map.get("CONTROL_VALUE").toString().split("<<=>>");
            String uploadFolder = uploadFoldersAndFiles[0].trim();
            String uploadFile = uploadFoldersAndFiles[1].trim();
            String uploadButtonPath = map.get("ATTRIBUTE_VALUE").toString().trim(); // Assuming you have a path or selector for the upload button

            //page.setInputFiles(uploadButtonPath,Paths.get("Upload_Test_Files/Upload_Videos/Test_Upload_Video_1.MP4"));
            FileChooser fileChooser= page.waitForFileChooser(()->{page.click(uploadButtonPath);});

            // Set the file path to upload
            Path filePath = Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator +
                    "test" + File.separator + "java" + File.separator + "Upload_Test_Files" + File.separator +
                    uploadFolder + File.separator + uploadFile);

            // Check if the file exists
            if (!Files.exists(filePath)) {
                throw new FileNotFoundException("File not found: " + filePath.toString());
            }

            fileChooser.setFiles(Paths.get(filePath.toUri()));
            // Wait for upload to complete (adjust as needed)
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            // Optional: Handle any success message or further actions
            System.out.println("File uploaded successfully!");
            // Update method status on successful upload
            methodStatus = true;
        } catch (Exception e) {
            // Handle exceptions and log errors
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            // Always write logs and TC info
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }


    //Upload_Multiple_Files
    public static boolean Upload_Multiple_Files(Page page, HashMap map, String reportLogFileName) throws IOException {
        boolean methodStatus = false;
        try {
            waitForPageLoad(page);

            // Wait for file chooser dialog
            // Locate the file input element
            ElementHandle fileInput = page.querySelector("input[type=file]");

            String[] uploadFoldersAndFiles = map.get("CONTROL_VALUE").toString().split("<<=>>");
            String uploadFolder = uploadFoldersAndFiles[0].trim();
            String uploadFile = uploadFoldersAndFiles[1].trim();


            if (uploadFile.contains(",")) {
                String[] uploadFiles = null;
                uploadFiles = uploadFile.split(",");

                System.out.println(uploadFiles.length);

                // Wait for upload to complete (adjust as needed)
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                // Optional: Handle any success message or further actions
                System.out.println("File uploaded successfully!");
                methodStatus = true;


            } else {


                // Set the file path to upload
                Path filePath = Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator +
                        "test" + File.separator + "java" + File.separator + "Upload_Test_Files" + File.separator +
                        uploadFolder + File.separator + uploadFile);
                // Upload the file
                fileInput.setInputFiles(filePath);
                // Wait for upload to complete (adjust as needed)
                page.waitForLoadState(LoadState.DOMCONTENTLOADED);
                // Optional: Handle any success message or further actions
                System.out.println("File uploaded successfully!");
                methodStatus = true;

            }





           /* // Define an array of Path objects
            Path[] filePaths = new Path[2]; // Array size is 2 for two files

            // Assign paths to the array elements
            filePaths[0] = Paths.get("/path/to/your/file1.txt");
            filePaths[1] = Paths.get("/path/to/your/file2.txt");

            // Print each path from the array
            for (Path path : filePaths) {
                System.out.println("File Path: " + path.toString());
            }*/


        } catch (Exception e) {
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            writeLogsInfoIntoExcel(reportLogFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
        } finally {
            writeTCsInfoIntoExcel(page, reportLogFileName, map, methodStatus);
        }
        return methodStatus;
    }
}
