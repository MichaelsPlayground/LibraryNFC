package com.piotrekwitkowski.libraryreader;

import android.util.Log;

import com.piotrekwitkowski.libraryreader.nfc.AESKey;
import com.piotrekwitkowski.libraryreader.nfc.AID;
import com.piotrekwitkowski.libraryreader.nfc.ByteUtils;
import com.piotrekwitkowski.libraryreader.nfc.DESFire;
import com.piotrekwitkowski.libraryreader.nfc.DESFireException;
import com.piotrekwitkowski.libraryreader.nfc.HCE;
import com.piotrekwitkowski.libraryreader.nfc.Iso7816;
import com.piotrekwitkowski.libraryreader.nfc.IsoDep;
import com.piotrekwitkowski.libraryreader.nfc.Response;

import java.io.IOException;
import java.util.Arrays;

class StudentId {
    private static final String TAG = "StudentId";
    private final IsoDep isoDep;
    enum idForm {PHYSICAL, HCE}

    private StudentId(IsoDep isoDep) {
        this.isoDep = isoDep;
    }

    static StudentId getStudentId(IsoDep isoDep) throws Exception {
        Log.i(TAG, "getStudentId()");
        isoDep.connect();

        idForm idForm = getIdForm(isoDep);
        Log.i(TAG, "ID form: "+ idForm);

        if (idForm == StudentId.idForm.PHYSICAL) {
            return new StudentId(isoDep);
        } else if (idForm == StudentId.idForm.HCE) {
            Response response = HCE.selectAndroidApp(isoDep);
            if (Arrays.equals(response.getBytes(), Iso7816.RESPONSE_SUCCESS)) {
                return new StudentId(isoDep);
            } else {
                throw new StudentIdException("HCE Mobile Application select was unsuccessful");
            }
        } else {
            throw new StudentIdException("ID form not supported");
        }
    }

    void close() throws IOException {
        isoDep.close();
    }

    private static idForm getIdForm(IsoDep isoDep) throws StudentIdException {
        Log.i(TAG, "getIdForm()");

        byte[] historicalBytes = isoDep.getHistoricalBytes();
        Log.i(TAG, "historicalBytes: " + ByteUtils.toHexString(historicalBytes));

        if (Arrays.equals(historicalBytes, new byte[]{(byte) 0x80})) {
            return idForm.PHYSICAL;
        } else if (Arrays.equals(historicalBytes, new byte[]{})) {
            return idForm.HCE;
        } else {
            throw new StudentIdException("id form not recognized");
        }
    }

    void selectApplication(AID aid) throws IOException, DESFireException {
        byte[] applicationAid = aid.getAid();
        DESFire.selectApplication(this.isoDep, applicationAid);
        Log.i(TAG, "Application selected: " + ByteUtils.toHexString(applicationAid));
    }

    void authenticateAES(AESKey key, byte keyNumber) throws Exception {
        byte[] aesKey = key.getKey();
        byte[] sessionKey = DESFire.authenticateAES(this.isoDep, aesKey, keyNumber);
        Log.i(TAG, "Session key: " + ByteUtils.toHexString(sessionKey));
    }


    byte[] getValue(byte fileNumber, byte[] offset, byte[] length) throws IOException, DESFireException {
        byte[] value = DESFire.getValue(this.isoDep, fileNumber, offset, length);
        Log.i(TAG, "File value: " + ByteUtils.toHexString(value));
        return value;
    }

}
