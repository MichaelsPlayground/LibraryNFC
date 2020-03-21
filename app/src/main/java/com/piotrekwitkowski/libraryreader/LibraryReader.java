package com.piotrekwitkowski.libraryreader;

import android.nfc.Tag;
import android.util.Log;

import com.piotrekwitkowski.libraryreader.nfc.AID;
import com.piotrekwitkowski.libraryreader.nfc.IsoDep;

class LibraryReader {
    private static final String TAG = "LibraryReader";
    private static final AID LIBRARY = new AID("015548");

    void processTag(Tag tag) {
        Log.i(TAG, "processTag()");
        IsoDep isoDep = IsoDep.get(tag);
        try {
            StudentId studentId = StudentId.getStudentId(isoDep);
            studentId.selectApplication(LIBRARY);
//            authenticateAES(studentId, key);
//            getFile(studentId, fileNo);

            studentId.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
