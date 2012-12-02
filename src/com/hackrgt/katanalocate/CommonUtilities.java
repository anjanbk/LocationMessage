package com.hackrgt.katanalocate;

import android.content.*;

public class CommonUtilities {
    static final String SERVER_URL = "http://katanaserver.no-ip.org/gcm_server_php/register.php"; 
 
    // Google project id
    static final String SENDER_ID = "398666671901"; 
 
    /**
     * Tag used on log messages.
     */
    static final String TAG = "KatanaLocate GCM";
 
    static final String DISPLAY_MESSAGE_ACTION =
            "com.hackrgt.katanalocate.DISPLAY_MESSAGE";
 
    static final String EXTRA_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}