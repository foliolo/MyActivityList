package al.uax.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * BroadcastReceiver que monitoriza el evento SMS_RECEIVED para detectar
 * mensajes recibidos.
 *
 */
public class SmsReceiver extends BroadcastReceiver {
	
    private static final String TAG = "SmsReceiver";

	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String EXTRA_PDUS = "pdus";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction() == ACTION_SMS_RECEIVED) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get(EXTRA_PDUS);
                
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                
                if (messages.length > -1) {
                	
                	String s = "Message recieved: [" + messages[0].getOriginatingAddress() +"] "
                			+ messages[0].getMessageBody();
                	
                    Log.i(TAG, s);
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            }
        }

	}

}
