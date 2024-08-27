import Framework_Methods.Web_Control_Common_Methods;
import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static Framework_Methods.Generic_Methods.*;
import static Framework_Methods.Generic_Methods.replaceTextInHTMLReport;
import static Framework_Methods.Web_Control_Common_Methods.updateExcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import javax.swing.*;


public class Framework_Main extends Thread {


    private JFrame frame;
    private JTextField inputField;
    private JTextField outputField;
    private JButton inputBrowseButton;
    private JButton outputBrowseButton;


    private final String defaultInputPath = System.getProperty("user.dir") + "\\src\\test\\java\\Test_Data"; // Set your default input folder path here
    private final String defaultOutputPath = System.getProperty("user.dir") + "\\src\\test\\java\\Reports"; // Set your default output folder path here

    public static String inputFolderPath;
    public static String reportsFolderPath;

   /* public static String inputFolderPath = System.getProperty("user.dir") + "\\src\\test\\java\\Test_Data";
    public static String reportsFolderPath= System.getProperty("user.dir") + "\\src\\test\\java\\Reports";
*/


   /* public static String inputFolderPath = "C:\\Users\\KiranBanda\\OneDrive - Inovar Tech\\LMS_Latest_21August2024_Eve" + "\\src\\test\\java\\Test_Data";
    public static String reportsFolderPath = "C:\\Users\\KiranBanda\\OneDrive - Inovar Tech\\LMS_Latest_21August2024_Eve" + "\\src\\test\\java\\Reports";
*/
   /* public static String inputFolderPath = "D:\\Test\\Input";
    public static String reportsFolderPath= "D:\\Test\\Output";
*/

    Web_Control_Common_Methods wccm = new Web_Control_Common_Methods(reportsFolderPath + "\\Images");


    private static final Logger logger = LogManager.getLogger(Framework_Main.class);
    private static CountDownLatch latch;
    String browserName;
    private static String ExecutionReportFolderName;
    private static String executionDataSheetPathAndName;
    private static String ExecutionScriptName;
    private static String ExecutionScriptDescription;
    private static String reportFileName;
    private static String excelReportFileName;
    private static String htmlReportFileName;
    private static final ThreadLocal<SimpleDateFormat> dateFormatThreadLocal = ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));

    public Framework_Main(String browserName) {
        this.browserName = browserName;
        this.latch = latch;
    }

    public Framework_Main() {

    }

    public void run() {
        boolean strMethodsReturnStatus = false;
        long threadId = Thread.currentThread().getId();
        String executionStartTime = getCurrentTime();
        Playwright playwright = Playwright.create();
        Browser browser = null;
        Page page = null;
        BrowserContext context = null;
        if (browserName.equalsIgnoreCase("Chrome")) {
            page = getBrowser(playwright, browserName).launch(new BrowserType.LaunchOptions().setHeadless(false).setChannel("chrome")
                            .setArgs(List.of("--start-maximized", "--incognito")))
                    .newContext(new Browser.NewContextOptions().setViewportSize(null))
                    .newPage();
        } else if (browserName.equalsIgnoreCase("Firefox")) {
            browser = getBrowser(playwright, browserName).launch(new BrowserType.LaunchOptions().setHeadless(false));
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(width - 30, height));
            page = browserContext.newPage();
        } else if (browserName.equalsIgnoreCase("Edge")) {
            page = getBrowser(playwright, browserName).launch(new BrowserType.LaunchOptions().setHeadless(false)
                            .setChannel("msedge").setArgs(List.of("--start-maximized", "--inprivate")))
                    .newContext(new Browser.NewContextOptions().setViewportSize(null))
                    .newPage();
        } else if (browserName.equalsIgnoreCase("Chrome_Headless")) {
            browser = getBrowser(playwright, browserName).launch(new BrowserType.LaunchOptions().setHeadless(true).setChannel("chrome"));
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(width - 30, height));
            page = browserContext.newPage();
        } else if (browserName.equalsIgnoreCase("Firefox_Headless")) {
            browser = getBrowser(playwright, browserName).launch(new BrowserType.LaunchOptions().setHeadless(true));
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(width - 30, height));
            page = browserContext.newPage();
        } else if (browserName.equalsIgnoreCase("Edge_Headless")) {
            browser = getBrowser(playwright, browserName).launch(new BrowserType.LaunchOptions().setHeadless(true).setChannel("msedge"));
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();
            BrowserContext browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(width - 30, height));
            page = browserContext.newPage();
        }
        assert browser != null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(executionDataSheetPathAndName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        XSSFSheet xs = wb.getSheet(ExecutionScriptName);
        int lastRowNumber = xs.getLastRowNum();
        LinkedHashMap<String, String> executionPerameters = new LinkedHashMap<>();
        for (int rowCount = 6; rowCount <= lastRowNumber; rowCount++) {
            Row rowForColumnHeaders = xs.getRow(5);
            Row rowForValues = xs.getRow(rowCount);
            for (int columnCount = 0; columnCount < rowForColumnHeaders.getLastCellNum(); columnCount++) {
                Cell cellHeaderName = rowForColumnHeaders.getCell(columnCount);
                String strCellHeaderValue = cellHeaderName.toString();
                Cell cellValue = rowForValues.getCell(columnCount);
                String formattedValue;
                if (cellValue != null) {
                    switch (cellValue.getCellType()) {
                        case STRING:
                            formattedValue = cellValue.getStringCellValue();
                            executionPerameters.put(strCellHeaderValue.toUpperCase(), formattedValue);
                            break;
                        case NUMERIC:
                            formattedValue = String.valueOf((long) cellValue.getNumericCellValue());
                            executionPerameters.put(strCellHeaderValue.toUpperCase(), formattedValue);
                            break;
                        case FORMULA:
                            formattedValue = evaluateFormulaCell(cellValue);
                            executionPerameters.put(strCellHeaderValue.toUpperCase(), formattedValue);
                            break;
                        default:
                            executionPerameters.put(strCellHeaderValue.toUpperCase(), "");
                    }
                }
            }
            if (executionPerameters.get("NEED_EXECUTION").equalsIgnoreCase("YES") || executionPerameters.get("NEED_EXECUTION").equalsIgnoreCase("") || executionPerameters.get("NEED_EXECUTION").equalsIgnoreCase(null) || executionPerameters.get("NEED_EXECUTION").equalsIgnoreCase("TENTATIVE")) {
                //if(executionPerameters.get("NEED_EXECUTION").equalsIgnoreCase("NO") || executionPerameters.get("NEED_EXECUTION").equalsIgnoreCase("SKIP")){
                String methodName = executionPerameters.get("CONTROL_ACTION");
                String className = "Framework_Methods." + executionPerameters.get("CONTROL_TYPE");
                Class<?> c = null;
                int rowNumber = rowCount + 1;
                logger.info("Workflow: " + ExecutionScriptName + "  Row Number: " + rowNumber + "    Method: " + methodName);
                try {
                    c = Class.forName(className);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                Class[] argTypes = new Class[]{Page.class, HashMap.class, String.class};
                Method main;
                try {
                    main = c.getDeclaredMethod(methodName, argTypes);
                } catch (NoSuchMethodException e) {
                    System.out.println("Method " + methodName + " not found");
                    break;
                }
                try {
                    strMethodsReturnStatus = (boolean) main.invoke(methodName, page, executionPerameters, excelReportFileName);
                    if (strMethodsReturnStatus) {
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        assert page != null;
        page.close();
        playwright.close();
        //String executionEndTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String executionEndTime = getCurrentTime();
        String diff = null;
        try {
            diff = executionTimeDiffCalculation(executionStartTime, executionEndTime);
        } catch (IOException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException |
                 ClassNotFoundException | InstantiationException | ParseException e) {
            throw new RuntimeException(e);
        }
        String strExecutionStatus = null;
        if (strMethodsReturnStatus) {
            strExecutionStatus = "PASSED";
        } else {
            strExecutionStatus = "FAILED";
        }
        String[] arrSummaryInfo = null;
        arrSummaryInfo = new String[]{String.valueOf(threadId), ExecutionScriptName, ExecutionScriptDescription, browserName, executionStartTime, executionEndTime, diff, strExecutionStatus};
        try {
            writeSummaryInfoIntoExcel(excelReportFileName, "Summary_Info", arrSummaryInfo);
        } catch (Exception e) {
            try {
                writeLogsInfoIntoExcel(excelReportFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", e.toString());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                writeLogsInfoIntoExcel(excelReportFileName, "Logs_Info", String.valueOf(Thread.currentThread().getId()), "Error", Arrays.toString(e.getStackTrace()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }


        try {
            writeTestCasesInfoIntoHtmlReport(excelReportFileName, htmlReportFileName, String.valueOf(threadId));


            //updateExcel(excelReportFileName, "TestCases_Info", String.valueOf(threadId));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            writeLogsInfoIntoHtmlReport(excelReportFileName, htmlReportFileName, String.valueOf(threadId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        latch.countDown();


    }

    //Main method
    public static void main(String[] args) throws IOException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {

        SwingUtilities.invokeLater(() -> {
            Framework_Main app = new Framework_Main();
            app.createAndShowGUI();
        });

        /*logger.info("SCRIPT EXECUTION HAS BEEN STARTED");
        // Example usage: save the file to the user's desktop

        //downloadExcelTemplateForDataSheet("D:/TM.xlsx");
        //Framework_Main.

        String[] filenames = ListOfDataFiles(inputFolderPath + "\\");
        for (String filename : filenames) {
            try {

                int lastIndex = filename.lastIndexOf('.');
                reportFileName = filename.substring(0, lastIndex) + "_Executions_" + customDateFormat();
                logger.info(filename.substring(0, lastIndex) + " workflows execution has been started");
                excelReportFileName = reportsFolderPath + "\\" + reportFileName + ".xlsx";
                htmlReportFileName = reportsFolderPath + "\\" + reportFileName + ".html";
                htmlReportCreate(htmlReportFileName);
                logger.info("HTML Report Teamplate has been created");
                createExcelLogFile(excelReportFileName);
                logger.info("Logs Teamplate has been created");
                executionDataSheetPathAndName = inputFolderPath + "\\" + filename;
                String[] arr = readAllExecutionMethodsFromExecutionExcelFile(executionDataSheetPathAndName);
                LinkedHashMap<String, String> executionPerameters = new LinkedHashMap<>();
                if (arr == null) {
                    System.out.println("Execution sheet not found in Execution Data File");
                } else if (arr.length == 0) {
                    System.out.println("Headers not found in Execution Data File");
                } else if (arr.length == 1) {
                    System.out.println("Methods not found");
                } else {
                    FileInputStream fis = new FileInputStream(executionDataSheetPathAndName);
                    XSSFWorkbook wb = new XSSFWorkbook(fis);
                    XSSFSheet xs = wb.getSheet("Executions");
                    for (int methodsCount = 1; methodsCount < arr.length; methodsCount++) {
                        Row row = xs.getRow(methodsCount);
                        Row rowKey = xs.getRow(0);
                        for (int cell = 0; cell < rowKey.getLastCellNum(); cell++) {
                            Cell cellValue = row.getCell(cell);
                            String v = cellValue.toString();
                            Cell keyValue = rowKey.getCell(cell);
                            String k = keyValue.toString();
                            executionPerameters.put(k, v);
                        }
                        latch = new CountDownLatch(0);
                        if (executionPerameters.get("EXECUTION_REQUIRED").toUpperCase().equalsIgnoreCase("Yes")) {
                            if (executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION").toUpperCase().contains(",")) {
                                String[] browserArray = executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION").toUpperCase().split(",");
                                latch = new CountDownLatch(browserArray.length);
                                ExecutionScriptName = executionPerameters.get("WORKFLOW_CODE").toUpperCase();
                                ExecutionScriptDescription = executionPerameters.get("WORKFLOW_DESCRIPTION");
                                for (String s : browserArray) {
                                    if (getValueFromExcel(inputFolderPath, filename, ExecutionScriptName, 4, 2).equalsIgnoreCase("YES")) {
                                        Framework_Main temp = new Framework_Main(s.trim());
                                        temp.setDaemon(false);
                                        temp.start();
                                    } else {
                                        Framework_Main temp = new Framework_Main(s.trim());
                                        temp.setDaemon(false);
                                        temp.start();
                                        try {
                                            temp.join();  // This will wait for the current thread to complete
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            } else if ((executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION") == null) || (Objects.equals(executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION"), ""))) {
                                latch = new CountDownLatch(1);
                                ExecutionScriptName = executionPerameters.get("WORKFLOW_CODE").toUpperCase();
                                ExecutionScriptDescription = executionPerameters.get("WORKFLOW_DESCRIPTION");
                                Framework_Main temp = new Framework_Main("Edge");
                                temp.setDaemon(false);
                                temp.start();
                            } else {
                                latch = new CountDownLatch(1);
                                ExecutionScriptName = executionPerameters.get("WORKFLOW_CODE").toUpperCase();
                                ExecutionScriptDescription = executionPerameters.get("WORKFLOW_DESCRIPTION");
                                Framework_Main temp = new Framework_Main(executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION").toUpperCase().trim());
                                temp.setDaemon(false);
                                temp.start();
                            }
                        }
                        try {
                            assert latch != null;
                            latch.await(); // Wait for all threads in the set to finish
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                //HTML Report builder
                writeSummaryInfoIntoHtmlReport(excelReportFileName, htmlReportFileName);
                    *//*writeTestCasesInfoIntoHtmlReport(excelReportFileName, htmlReportFileName);
                    writeLogsInfoIntoHtmlReport(excelReportFileName, htmlReportFileName);*//*
                writeWFsInfoIntoHtmlReport(excelReportFileName, htmlReportFileName, executionDataSheetPathAndName);
                replaceTextInHTMLReport(htmlReportFileName, "summarytablesreplacetext", "");
                replaceTextInHTMLReport(htmlReportFileName, "testcasetablereplace", "");
                replaceTextInHTMLReport(htmlReportFileName, "logtablereplace", "");
                replaceTextInHTMLReport(htmlReportFileName, "wfstablereplace", "");
                replaceTextInHTMLReport(htmlReportFileName, "createtestcasesconstantVariables", "");
                replaceTextInHTMLReport(htmlReportFileName, "createlogssconstantVariables", "");
                //replaceTextInHTMLReport(htmlReportFileName, "createwfsconstantVariables", "");
            }
        }
        logger.info("SCRIPT EXECUTION HAS BEEN ENDED");*/
        //logger.error("END");
    }


    private static String getCurrentTime() {
        SimpleDateFormat dateFormat = dateFormatThreadLocal.get();
        return dateFormat.format(new Date());
    }


    private void createAndShowGUI() {
        frame = new JFrame("Folder Path Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 200);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = createMainPanel();
        JPanel buttonPanel = createButtonPanel();

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set default paths
        inputField.setText(defaultInputPath);
        outputField.setText(defaultOutputPath);

        frame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel inputLabel = new JLabel("Input Folder:");
        JLabel outputLabel = new JLabel("Output Folder:");

        JPanel inputPanel = createTextFieldWithButton();
        JPanel outputPanel = createTextFieldWithButton();

        inputField = getTextFieldFromPanel(inputPanel);
        outputField = getTextFieldFromPanel(outputPanel);
        inputBrowseButton = getButtonFromPanel(inputPanel);
        outputBrowseButton = getButtonFromPanel(outputPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        mainPanel.add(inputLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(inputPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(outputLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(outputPanel, gbc);

        setupBrowseButtonActions();

        return mainPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton runButton = new JButton("Run");
        JButton cancelButton = new JButton("Cancel");

        runButton.setBackground(new Color(70, 200, 70));
        runButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(250, 70, 70));
        cancelButton.setForeground(Color.WHITE);

        buttonPanel.add(runButton);
        buttonPanel.add(cancelButton);

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleRunButtonClick();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // Close the dialog
            }
        });

        return buttonPanel;
    }

    private JPanel createTextFieldWithButton() {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(300, 30));

        JButton button = new JButton("...");
        button.setPreferredSize(new Dimension(30, 30));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBackground(new Color(100, 150, 250));
        button.setForeground(Color.WHITE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);

        return panel;
    }

    private JTextField getTextFieldFromPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField) {
                return (JTextField) comp;
            }
        }
        return null;
    }

    private JButton getButtonFromPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                return (JButton) comp;
            }
        }
        return null;
    }

    private void setupBrowseButtonActions() {
        inputBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBrowseButtonClick(inputField);
            }
        });

        outputBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBrowseButtonClick(outputField);
            }
        });
    }

    private void handleBrowseButtonClick(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = fileChooser.getSelectedFile();
            textField.setText(selectedFolder.getAbsolutePath());
        }
    }

    private void handleRunButtonClick() throws IOException {
        inputFolderPath = inputField.getText();
        reportsFolderPath = outputField.getText();
        if (validateFolderPath(inputFolderPath) && validateFolderPath(reportsFolderPath)) {
            runMainMethod(inputFolderPath, reportsFolderPath);
            frame.dispose();  // Close the dialog
            logger.info("SCRIPT EXECUTION HAS BEEN STARTED");
            String[] filenames = ListOfDataFiles(inputFolderPath + "\\");
            for (String filename : filenames) {
                try {

                    int lastIndex = filename.lastIndexOf('.');
                    reportFileName = filename.substring(0, lastIndex) + "_Executions_" + customDateFormat();
                    logger.info(filename.substring(0, lastIndex) + " workflows execution has been started");
                    excelReportFileName = reportsFolderPath + "\\" + reportFileName + ".xlsx";
                    htmlReportFileName = reportsFolderPath + "\\" + reportFileName + ".html";
                    htmlReportCreate(htmlReportFileName);
                    logger.info("HTML Report Teamplate has been created");
                    createExcelLogFile(excelReportFileName);
                    logger.info("Logs Teamplate has been created");
                    executionDataSheetPathAndName = inputFolderPath + "\\" + filename;
                    String[] arr = readAllExecutionMethodsFromExecutionExcelFile(executionDataSheetPathAndName);
                    LinkedHashMap<String, String> executionPerameters = new LinkedHashMap<>();
                    if (arr == null) {
                        System.out.println("Execution sheet not found in Execution Data File");
                    } else if (arr.length == 0) {
                        System.out.println("Headers not found in Execution Data File");
                    } else if (arr.length == 1) {
                        System.out.println("Methods not found");
                    } else {
                        FileInputStream fis = new FileInputStream(executionDataSheetPathAndName);
                        XSSFWorkbook wb = new XSSFWorkbook(fis);
                        XSSFSheet xs = wb.getSheet("Executions");
                        for (int methodsCount = 1; methodsCount < arr.length; methodsCount++) {
                            Row row = xs.getRow(methodsCount);
                            Row rowKey = xs.getRow(0);
                            for (int cell = 0; cell < rowKey.getLastCellNum(); cell++) {
                                Cell cellValue = row.getCell(cell);
                                String v = cellValue.toString();
                                Cell keyValue = rowKey.getCell(cell);
                                String k = keyValue.toString();
                                executionPerameters.put(k, v);
                            }
                            latch = new CountDownLatch(0);
                            if (executionPerameters.get("EXECUTION_REQUIRED").toUpperCase().equalsIgnoreCase("Yes")) {
                                if (executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION").toUpperCase().contains(",")) {
                                    String[] browserArray = executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION").toUpperCase().split(",");
                                    latch = new CountDownLatch(browserArray.length);
                                    ExecutionScriptName = executionPerameters.get("WORKFLOW_CODE").toUpperCase();
                                    ExecutionScriptDescription = executionPerameters.get("WORKFLOW_DESCRIPTION");
                                    for (String s : browserArray) {
                                        if (getValueFromExcel(inputFolderPath, filename, ExecutionScriptName, 4, 2).equalsIgnoreCase("YES")) {
                                            Framework_Main temp = new Framework_Main(s.trim());
                                            temp.setDaemon(false);
                                            temp.start();
                                        } else {
                                            Framework_Main temp = new Framework_Main(s.trim());
                                            temp.setDaemon(false);
                                            temp.start();
                                            try {
                                                temp.join();  // This will wait for the current thread to complete
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                } else if ((executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION") == null) || (Objects.equals(executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION"), ""))) {
                                    latch = new CountDownLatch(1);
                                    ExecutionScriptName = executionPerameters.get("WORKFLOW_CODE").toUpperCase();
                                    ExecutionScriptDescription = executionPerameters.get("WORKFLOW_DESCRIPTION");
                                    Framework_Main temp = new Framework_Main("Edge");
                                    temp.setDaemon(false);
                                    temp.start();
                                } else {
                                    latch = new CountDownLatch(1);
                                    ExecutionScriptName = executionPerameters.get("WORKFLOW_CODE").toUpperCase();
                                    ExecutionScriptDescription = executionPerameters.get("WORKFLOW_DESCRIPTION");
                                    Framework_Main temp = new Framework_Main(executionPerameters.get("BROWSERS_REQUIRED_FOR_EXECUTION").toUpperCase().trim());
                                    temp.setDaemon(false);
                                    temp.start();
                                }
                            }
                            try {
                                assert latch != null;
                                latch.await(); // Wait for all threads in the set to finish
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    //HTML Report builder
                    writeSummaryInfoIntoHtmlReport(excelReportFileName, htmlReportFileName);
                    /*writeTestCasesInfoIntoHtmlReport(excelReportFileName, htmlReportFileName);
                    writeLogsInfoIntoHtmlReport(excelReportFileName, htmlReportFileName);*/
                    writeWFsInfoIntoHtmlReport(excelReportFileName, htmlReportFileName, executionDataSheetPathAndName);
                    replaceTextInHTMLReport(htmlReportFileName, "summarytablesreplacetext", "");
                    replaceTextInHTMLReport(htmlReportFileName, "testcasetablereplace", "");
                    replaceTextInHTMLReport(htmlReportFileName, "logtablereplace", "");
                    replaceTextInHTMLReport(htmlReportFileName, "wfstablereplace", "");
                    replaceTextInHTMLReport(htmlReportFileName, "createtestcasesconstantVariables", "");
                    replaceTextInHTMLReport(htmlReportFileName, "createlogssconstantVariables", "");
                    //replaceTextInHTMLReport(htmlReportFileName, "createwfsconstantVariables", "");
                }
            }
            logger.info("SCRIPT EXECUTION HAS BEEN ENDED");
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid folder paths.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateFolderPath(String path) {
        File folder = new File(path);
        return folder.isDirectory();
    }

    private void runMainMethod(String inputPath, String outputPath) {
        // Your main method logic here
        System.out.println("Input Path: " + inputPath);
        System.out.println("Output Path: " + outputPath);

        // For example, just printing the paths here
        // Replace this with your actual processing logic
    }


    private void creteReportFolder() {
        File folder = new File(reportsFolderPath);
        if (!folder.exists()) {
            // If the folder does not exist, create it
            if (folder.mkdirs()) {
                System.out.println("Folder created: " + reportsFolderPath);
            } else {
                System.out.println("Failed to create folder: " + reportsFolderPath);
            }
        } else {
            System.out.println("Folder already exists: " + reportsFolderPath);
        }
    }


    public static void downloadExcelTemplateForDataSheet(String outputFilePath) {
        // Load the Excel file from the resources folder
        try (InputStream inputStream = Framework_Main.class.getResourceAsStream("/TestData_Template.xlsx")) {
            if (inputStream == null) {
                System.out.println("Excel file not found in resources folder.");
                return;
            }
            // Write the file to the desired location
            try (FileOutputStream outputStream = new FileOutputStream(outputFilePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                System.out.println("Excel file has been downloaded to: " + outputFilePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
