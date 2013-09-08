package al.uax.myactivitylist;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Receiver extends Activity{
	
	private BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			Toast.makeText(context, "Modo avion pulsado", Toast.LENGTH_LONG).show();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		RelativeLayout layout = new RelativeLayout(this);
		TextView text = new TextView(this);
		text.setTextAppearance(this, android.R.style.TextAppearance_Large);
		text.setText("Estando en la aplicación, pulsa el boton de modo avión.\nSi sales de la aplicación (llendo a Ajustes) no funcionará");
		layout.addView(text);
		setContentView(layout);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		IntentFilter filter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
		registerReceiver(receiver, filter);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		unregisterReceiver(receiver);
	}
}