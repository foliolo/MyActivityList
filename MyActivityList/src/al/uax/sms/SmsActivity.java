package al.uax.sms;

import java.util.ArrayList;

import al.uax.myactivitylist.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Este ejemplo muestra como enviar mensajes cortos (SMS) a un numero de teléfono, en primer lugar
 * usando un Intent hacia la aplicación de Mensajería; y en segundo lugar utilizando la clase
 * SmsManager.
 * 
 * Además monitoriza los mensajes recibidos mediante la clase SmsReceiver.
 *
 */
public class SmsActivity extends Activity {
	
	private static final String EXTRA_SMS_BODY = "sms_body";
	
	SmsManager mSmsManager;

	private EditText mDest;
	private EditText mMessage;
	private Button mSendIntentButton;
	private Button mSendManagerButton;
	private Button mSendMultiple;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_main);
        
        mSmsManager = SmsManager.getDefault();

        mDest = (EditText) findViewById(R.id.dest);
        mMessage = (EditText) findViewById(R.id.message);
        mSendIntentButton = (Button) findViewById(R.id.send_intent);
        mSendManagerButton = (Button) findViewById(R.id.send_manager);
        mSendMultiple = (Button) findViewById(R.id.send_manager_multiple);
        
        mSendIntentButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Uri dest = getUriDest();
				String message = getMessage();
				
				Intent intent = new Intent(Intent.ACTION_SENDTO, dest);
				intent.putExtra(EXTRA_SMS_BODY, message);
				startActivity(intent);
			}
		});
        
        mSendManagerButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String dest = getDest();
				String message = getMessage();
				mSmsManager.sendTextMessage(dest, null, message, null, null);
			}
		});
        
        mSendMultiple.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String dest = getDest();
				String message = getMessage();
				
//				ArrayList<String> msg = new ArrayList<String>();
//				if(message.length() > 160){
//					for(int i=0; i<message.length(); i+=160){
//						try{
//							msg.add(message.substring(i,i+160));
//						}catch(StringIndexOutOfBoundsException ex){
//							msg.add(message.substring(i,message.length()-1));
//						}
//					}
				
				SmsManager smsManager = SmsManager.getDefault();
			    ArrayList<String> parts = smsManager.divideMessage(message); 
					
				try{
					mSmsManager.sendMultipartTextMessage(dest, null, parts, null, null);
				}
				catch(IllegalArgumentException ex){
					Toast.makeText(SmsActivity.this, "Introduce el destinatario o el mensaje correcto", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
    }
    
    private String getDest(){
    	return mDest.getText().toString();
    }
    
    private Uri getUriDest(){
    	return Uri.parse("sms:" + getDest());
    }
    
    private String getMessage(){
    	return mMessage.getText().toString();
    }

}
