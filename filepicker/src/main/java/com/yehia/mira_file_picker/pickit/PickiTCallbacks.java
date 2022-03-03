package com.yehia.mira_file_picker.pickit;

import java.util.ArrayList;

public interface PickiTCallbacks {
    void PickiTonUriReturned();
    void PickiTonStartListener();
    void PickiTonProgressUpdate(int progress);
    void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason);
    void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason);
}
