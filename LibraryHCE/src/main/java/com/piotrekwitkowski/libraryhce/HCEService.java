package com.piotrekwitkowski.libraryhce;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;

import com.piotrekwitkowski.log.Log;
import com.piotrekwitkowski.nfc.ByteUtils;

import static com.piotrekwitkowski.libraryhce.NotificationService.NOTIFICATION_CHANNEL_DESCRIPTION;
import static com.piotrekwitkowski.libraryhce.NotificationService.NOTIFICATION_CHANNEL_NAME;

public class HCEService extends HostApduService {
    private static final String TAG = "HCEService";
    private final NotificationService notifications = new NotificationService(this);
    private static final DESFireEmulation DESFire = new DESFireEmulation();
    private static final byte[] SERVICE_FAILURE_RESPONSE = new byte[] {(byte) 0xEE};

    @Override
    public byte[] processCommandApdu(byte[] command, Bundle extras) {
        Log.reset(TAG, "processCommandApdu()");
        createNotificationChannel();
        notifications.show("<--" + ByteUtils.toHexString(command));

        try {
            Log.i(TAG, "<--" + ByteUtils.toHexString(command));
            byte[] response = DESFire.getResponse(command);
            Log.i(TAG, "-->" + ByteUtils.toHexString(response));
            return response;
        } catch (DESFireException e) {
            return SERVICE_FAILURE_RESPONSE;
        }
    }

    @Override
    public void onDeactivated(int reason) {
        Log.i(TAG, "onDeactivated(). Reason: " + reason);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = this.getString(R.string.app_name);
            NotificationChannel nc = new NotificationChannel(NOTIFICATION_CHANNEL_NAME, name, NotificationManager.IMPORTANCE_DEFAULT);
            nc.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);

            NotificationManager nm = this.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(nc);
        }
    }

}
