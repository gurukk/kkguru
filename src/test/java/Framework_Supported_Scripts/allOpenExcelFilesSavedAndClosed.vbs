'Option Explicit

Dim objXLApp, objXLBook, strFolder, objFSO, colProcesses, objXL
Dim scriptPath

' Get the script's parent folder path
scriptPath = CreateObject("Scripting.FileSystemObject").GetParentFolderName(WScript.ScriptFullName)

' Get the parent folder of the script's parent folder
parentParentPath = CreateObject("Scripting.FileSystemObject").GetParentFolderName(scriptPath)

'MsgBox "Parent's parent folder path: " & parentParentPath
' Specify the folder containing Excel files
strFolder = parentParentPath & "\Test_Data"

' Check if the folder exists
Set objFSO = CreateObject("Scripting.FileSystemObject")
If Not objFSO.FolderExists(strFolder) Then
    MsgBox "Folder not found: " & strFolder, vbExclamation, "Error"
    WScript.Quit
End If

' Get all Excel processes
Set colProcesses = GetObject("winmgmts:").ExecQuery("Select * from Win32_Process Where Name = 'EXCEL.EXE'")

' Loop through each Excel process
For Each objXL In colProcesses
    On Error Resume Next  ' In case accessing Workbooks throws an error

    ' Get the Excel application object for the process
    Set objXLApp = GetObject(, "Excel.Application")

    ' Loop through each workbook in the Excel instance
    For Each objXLBook In objXLApp.Workbooks
    ' Check if the workbook is in the specified folder
        If InStr(1, objXLBook.FullName, strFolder, vbTextCompare) = 1 Then
        ' Save and close the workbook
            objXLBook.Save
            objXLBook.Close False  ' Close without saving changes
        End If
    Next

    On Error GoTo 0  ' Disable error handling
Next

' Clean up
Set objFSO = Nothing
Set colProcesses = Nothing

' Quit Excel application
If Not IsEmpty(objXLApp) Then
    objXLApp.Quit
    Set objXLApp = Nothing
End If

'MsgBox "All Excel files in " & strFolder & " have been saved and closed.", vbInformation, "Done!"

WScript.Quit
