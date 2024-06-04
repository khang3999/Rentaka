package vn.edu.tdc.rentaka.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import vn.edu.tdc.rentaka.services.SMSHandlingService;

public class SMSReceiver extends BroadcastReceiver {

    private static final String TAG = "SMSReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                strMessage += "SMS from " + messages[i].getOriginatingAddress();
                strMessage += " :";
                strMessage += messages[i].getMessageBody().toString();
                strMessage += "\n";
            }
            Log.i(TAG, strMessage);

            // Optionally, start a service to handle the SMS
            Intent serviceIntent = new Intent(context, SMSHandlingService.class);
            serviceIntent.putExtra("sms", strMessage);
            context.startService(serviceIntent);
        }
    }
}
