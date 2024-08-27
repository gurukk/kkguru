Option Explicit

' Main subroutine to update Excel with Java methods and sheet names
Sub UpdateExcelFromJavaFilesAndSheetNames()
    Dim fso, testDataFolder, excelApp, workbook, executionsSheet, scriptPath, javaFolderPath
    Dim testDataFolderPath, folderJava, file, javaFile, methodName, columnRange
    Dim i, row, col, lastRow, ws

    ' Create a FileSystemObject to work with files and folders
    Set fso = CreateObject("Scripting.FileSystemObject")

    ' Get the script's parent folder path
    scriptPath = fso.GetParentFolderName(WScript.ScriptFullName)

    ' Get the parent folder of the script's parent folder
    Dim parentParentPath
    parentParentPath = fso.GetParentFolderName(scriptPath)

    ' Construct the full path to the Test_Data folder relative to the script's location
    testDataFolderPath = fso.BuildPath(parentParentPath, "Test_Data")

    ' Create an Excel Application object
    On Error Resume Next ' In case Excel is not properly handled
    Set excelApp = CreateObject("Excel.Application")
    On Error GoTo 0 ' Turn off error handling

    If excelApp Is Nothing Then
        MsgBox "Excel is not installed or cannot be accessed.", vbExclamation, "Excel Not Found"
        Exit Sub
    End If

    excelApp.Visible = True ' Make Excel visible

    ' Get a reference to the Test_Data folder
    Set testDataFolder = fso.GetFolder(testDataFolderPath)

    ' Loop through each Excel file in the Test_Data folder
    For Each file In testDataFolder.Files
        If LCase(fso.GetExtensionName(file.Path)) = "xlsx" Then
        ' Open the workbook
            On Error Resume Next ' Ignore errors if workbook open fails
            Set workbook = excelApp.Workbooks.Open(file.Path)
            On Error GoTo 0 ' Turn off error handling

            If Not workbook Is Nothing Then
            ' Clear contents from column A to the end of the sheet "Executions_Data"
                On Error Resume Next ' Ignore errors if "Executions_Data" sheet does not exist
                Set executionsSheet = workbook.Sheets("Executions_Data")
                On Error GoTo 0 ' Turn off error handling

                If Not executionsSheet Is Nothing Then
                ' Clear all contents in the "Executions_Data" sheet
                    executionsSheet.Cells.ClearContents

                    ' Write header "WorkFlow_Sheet_Names" to cell A1
                    executionsSheet.Cells(1, 1).Value = "WorkFlow_Sheet_Names"

                    ' Initialize starting row and column numbers
                    row = 2 ' Start from row 2 (after the header)
                    col = 2 ' Start from column B (2nd column)

                    ' Get the folder object for Java files
                    javaFolderPath = fso.BuildPath(parentParentPath, "Framework_Methods")
                    Set folderJava = fso.GetFolder(javaFolderPath)

                    ' Loop through each Java file in the folder
                    For Each javaFile In folderJava.Files
                        If LCase(fso.GetExtensionName(javaFile.Path)) = "java" _
                            And LCase(fso.GetBaseName(javaFile.Path)) <> "web_control_methods" _
                            And LCase(fso.GetBaseName(javaFile.Path)) <> "web_control_common_methods" _
                            And LCase(fso.GetBaseName(javaFile.Path)) <> "generic_methods" Then

                        ' Get method names from the Java file
                            methodName = GetJavaFileMethods(javaFile.Path)

                            ' Write file name to Excel (column header)
                            executionsSheet.Cells(1, col).Value = fso.GetBaseName(javaFile.Path) ' Set as header (first row)

                            ' Write methods to Excel in the same column
                            Dim methodNameItem
                            For Each methodNameItem In methodName
                                If Not IsProgrammingKeyword(methodNameItem) Then
                                    executionsSheet.Cells(row, col).Value = methodNameItem
                                    row = row + 1
                                End If
                            Next

                            ' Set column range name to match the header (starting from B2)
                            Set columnRange = executionsSheet.Range(executionsSheet.Cells(2, col), executionsSheet.Cells(executionsSheet.Rows.Count, col).End(-4162))
                            columnRange.Name = executionsSheet.Cells(1, col).Value

                            ' Move to the next column for the next file
                            col = col + 1

                            ' Reset row for the next file
                            row = 2 ' Start from row 2 (after the header)
                        End If
                    Next

                    ' Autofit columns for better visibility
                    executionsSheet.Columns.AutoFit

                    ' Loop through all sheets in the workbook except "Executions_Data" and "Executions"
                    i = 2 ' Start populating from row 2 in the "Executions_Data" sheet
                    For Each ws In workbook.Sheets
                        If ws.Name <> "Executions_Data" And ws.Name <> "Executions" And ws.Name <> "Test_Parameters" Then
                            executionsSheet.Cells(i, 1).Value = ws.Name
                            i = i + 1
                        End If
                    Next

                    ' Determine the last row of data in column A
                    lastRow = executionsSheet.Cells(executionsSheet.Rows.Count, 1).End(-4162).Row ' -4162 is xlUp in VBA

                    ' Define the range name based on the value in A1 cell
                    Dim rangeName
                    rangeName = executionsSheet.Name & "!" & executionsSheet.Cells(1, 1).Address & ":A" & lastRow

                    ' Assign the defined range name
                    On Error Resume Next
                    workbook.Names.Add executionsSheet.Cells(1, 1).Value, executionsSheet.Range("A2:A" & lastRow)
                    On Error GoTo 0

                    If Err.Number <> 0 Then
                        MsgBox "Error setting range name: " & rangeName & vbCrLf & Err.Description, vbExclamation, "Range Name Error"
                        Err.Clear
                    End If

                    ' After populating columns with Java method names, determine the last column processed
                    Dim lastProcessedColumn
                    lastProcessedColumn = col - 1 ' Since col is incremented before moving to the next file

                    ' Ensure there are columns to select
                    If lastProcessedColumn >= 2 Then
                    ' Activate the "Executions_Data" sheet
                        executionsSheet.Activate

                        ' Select cells from B1 to the last processed column and row 1 in "Executions_Data" sheet
                        Dim rangeToSelect
                        Set rangeToSelect = executionsSheet.Range(executionsSheet.Cells(1, 2), executionsSheet.Cells(1, lastProcessedColumn))

                        ' Check if range exists and contains data
                        If Not rangeToSelect Is Nothing And rangeToSelect.Cells.Count > 0 Then
                            rangeToSelect.Select

                            ' Assign range name "Control_Names" to the selected range
                            On Error Resume Next
                            workbook.Names.Add "Control_Names", rangeToSelect
                            On Error GoTo 0

                            If Err.Number <> 0 Then
                                MsgBox "Error assigning range name 'Control_Names': " & vbCrLf & Err.Description, vbExclamation, "Range Name Error"
                                Err.Clear
                            Else
                            ' Inform user of successful range selection and assignment
                                'MsgBox "Cells B1 to " & Chr(64 + lastProcessedColumn) & "1 in 'Executions_Data' sheet have been selected and assigned the range name 'Control_Names'.", vbInformation, "Selection and Range Name Assignment Complete"
                            End If
                        Else
                            MsgBox "No data found in the 'Executions_Data' sheet to assign 'Control_Names' range.", vbExclamation, "No Data"
                        End If
                    Else
                        MsgBox "No data found in the 'Executions_Data' sheet to assign 'Control_Names' range.", vbExclamation, "No Data"
                    End If

                    ' Save and close the workbook
                    workbook.Save
                    workbook.Close

                Else
                    MsgBox "Sheet 'Executions_Data' not found in workbook: " & file.Name, vbExclamation, "Sheet Not Found"
                End If
            Else
                MsgBox "Failed to open workbook: " & file.Name, vbExclamation, "Workbook Open Failed"
            End If
        End If
    Next

    ' Quit Excel application
    excelApp.Quit

    ' Clean up objects
    Set executionsSheet = Nothing
    Set workbook = Nothing
    Set excelApp = Nothing
    Set fso = Nothing
End Sub

Function GetJavaFileMethods(filePath)
    Dim fso, ts, fileContents, methodNames, pattern, regex, matches, match
    Dim methodArray(), index

    ' Create a FileSystemObject
    Set fso = CreateObject("Scripting.FileSystemObject")

    ' Initialize an array to store method names
    ReDim methodArray(0) ' Start with an empty array
    index = 0

    ' Read the contents of the Java file
    On Error Resume Next ' Handle file read errors
    Set ts = fso.OpenTextFile(filePath)
    If Err.Number <> 0 Then
        MsgBox "Error opening file: " & filePath & vbCrLf & Err.Description, vbExclamation, "File Open Error"
        Err.Clear
        Exit Function
    End If
    On Error GoTo 0 ' Turn off error handling

    ' Define a regex pattern to match method signatures
    pattern = "(Public|Private|Protected|static)?\s*[\w<>,\[\]]+\s+(\w+)\s*\([^)]*\)\s*[^;]*{"

    ' Create a Regular Expression object
    Set regex = New RegExp
    regex.Global = True
    regex.IgnoreCase = True
    regex.MultiLine = True
    regex.Pattern = pattern

    ' Find all method names using regex
    Set matches = regex.Execute(ts.ReadAll())
    For Each match In matches
    ' Expand array if needed
        If index >= UBound(methodArray) Then
            ReDim Preserve methodArray(index + 1)
        End If
        ' Add method name to the array
        methodArray(index) = match.SubMatches(1)
        index = index + 1
    Next

    ' Close the file
    ts.Close

    ' Convert array to a variant to return
    GetJavaFileMethods = methodArray
End Function

Function IsProgrammingKeyword(keyword) 'As Boolean
    Dim keywordsArray
    keywordsArray = Array("if", "else", "for", "while", "switch", "case", "break", "continue", "do", "return", "try", "catch", "finally", "throw", "throws", "assert", "new", "this", "super", "class", "extends", "implements", "interface", "void", "boolean", "byte", "char", "short", "int", "long", "float", "double", "null", "true", "false", "package", "import", "static", "final", "abstract", "synchronized", "volatile", "transient", "native", "strictfp", "const", "goto", "enum", "instanceof")

    IsProgrammingKeyword = (UBound(Filter(keywordsArray, LCase(keyword))) > -1)
End Function

' Subroutine to activate "Executions" sheet in the active workbook
Sub ActivateExecutionsSheet(ByRef excelApp)
    On Error Resume Next
    excelApp.ActiveWorkbook.Sheets("Executions").Activate
    On Error GoTo 0

    If Err.Number <> 0 Then
        MsgBox "Error activating 'Executions' sheet: " & vbCrLf & Err.Description, vbExclamation, "Activation Error"
        Err.Clear
    End If
End Sub

' Example usage:
UpdateExcelFromJavaFilesAndSheetNames
