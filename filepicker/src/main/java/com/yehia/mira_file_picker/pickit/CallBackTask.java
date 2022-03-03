package com.yehia.mira_file_picker.pickit;

interface CallBackTask {
    void PickiTonUriReturned();
    void PickiTonPreExecute();
    void PickiTonProgressUpdate(int progress);
    void PickiTonPostExecute(String path, boolean wasDriveFile, boolean wasSuccessful, String reason);
}
