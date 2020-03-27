package com.piotrekwitkowski.nfc.desfire.keys;

public class ApplicationKey {
    private final AESKey aesKey;
    private final byte keyNumber;

    public ApplicationKey(AESKey aesKey, int keyNumber) {
        this.aesKey = aesKey;
        this.keyNumber = (byte) keyNumber;
    }

    public ApplicationKey(String aesKey, int keyNumber) {
        this.aesKey = new AESKey(aesKey);
        this.keyNumber = (byte) keyNumber;
    }

    public AESKey getAESKey() {
        return aesKey;
    }

    public byte getKeyNumber() {
        return keyNumber;
    }
}
